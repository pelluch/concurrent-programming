package org.jtorrent;

import java.io.*;
import java.net.*;
import java.util.*;

import org.klomp.snark.bencode.BEncoder;

public class Tracker {

	private ServerSocket server;
	private Socket client;
	private int interval = 1;
	private String trackerId = "jTorrent";
	private int numSeeders = 1;
	private int numLeechers = 0;
	private List<Peer> peers = new ArrayList();
	private Socket conn = null;
	int portNumber = 0;
	private InetAddress peerAddress;

	public void getRequest()
	{

	}

	public Map<String, String> getQueryMap(String query) throws UnsupportedEncodingException  
	{  
		String[] params = query.split("&");  
		Map<String, String> map = new HashMap<String, String>();  
		for (String param : params)  
		{  
			String name = param.split("=")[0];  
			//System.out.println("Name of param: " + name);
			String value = param.split("=")[1];  
			map.put(name, value);  
		}  

		String infoHash = URLDecoder.decode(map.get("info_hash"), "US-ASCII");
		String peerId = URLDecoder.decode(map.get("peer_id"), "US-ASCII");

		byte[] infoBytes = infoHash.getBytes();
		byte[] peerBytes = map.get("peer_id").getBytes();
		portNumber = Integer.parseInt(map.get("port"));
		System.out.println("Port: " + portNumber);
		int uploaded = Integer.parseInt(map.get("uploaded"));
		int downloaded = Integer.parseInt(map.get("downloaded"));
		int left = Integer.parseInt(map.get("left"));
		String compact = map.get("compact");
		String event = map.get("event");
		String noPeer = map.get("no_peer_id");

		//System.out.println("Info hash: " + new String(infoHash) + " size " + infoHash.length());
		//System.out.println("Peer id: " + new String(peerId));

		return map;  
	}  

	public void addSeeders() throws IOException {

		for(int i = 0; i < numSeeders; ++i) {
			Peer peer = new Peer();
			Thread t = new Thread(peer);
			peers.add(peer);
			t.start();		
		}		
	}

	public void generateResponse(Map<String, String> params) throws IOException
	{
		Map responseDict = new HashMap();
		List<Byte> peerListDict = new ArrayList<Byte>();
		for(int i = 0; i < peers.size(); ++i) {
			byte[] peerInfo = peers.get(i).getCompactInfo();
			for(int j = 0; j < peerInfo.length; ++j) {
				peerListDict.add(peerInfo[j]);
			}
		}
		byte[] allBytes = new byte[peerListDict.size()];
		for(int i = 0; i < allBytes.length; ++i) {
			allBytes[i] = peerListDict.get(i).byteValue();
		}
		
		responseDict.put("peers", allBytes);		
		responseDict.put("interval", interval);
		responseDict.put("tracker id", trackerId);
		responseDict.put("complete", numSeeders);
		responseDict.put("incomplete", numLeechers);		

		//client = new Socket()
		OutputStream os = conn.getOutputStream();
		//client = new Socket();
		//client.connect(new InetSocketAddress("127.0.0.1", portNumber));
		//OutputStream os = client.getOutputStream();


		//client.
		// need to construct response bytes first
		byte [] mapEncoding = BEncoder.bencode(responseDict);
		String response = "";
		
		response += "HTTP/1.1 200 OK\r\n";
		response += "Content-Type: image/png\r\n";
		response += "Content-Length: " + mapEncoding.length + "\r\n";
		response += "\r\n";
		response += new String(mapEncoding, "ASCII");
	
		
		// write actual response and flush
		os.write(response.getBytes("ASCII"));
		os.flush();
		//os.close();
		//client.close();

	}

	public void listen() throws IOException
	{
		while(true)
		{		

			int count = 0;
			conn = server.accept();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// don't use buffered writer because we need to write both "text" and "binary"
			OutputStream out = conn.getOutputStream();
			peerAddress = conn.getInetAddress();
			//System.out.println("Port number: " + conn.getPort());
			System.out.println("Accepted connection");
			while (true)
			{
				count++;
				String line = reader.readLine();

				if (line == null)
				{
					System.out.println("Connection closed");
					break;
				}

				if (!line.equals(""))
				{
					byte[] b = line.getBytes("UTF-8");

					line = new String(b, "UTF-8");
					System.out.println("" + count + ": " + line);

					if(line.indexOf("?") != -1) {
						String paramString = line.substring(line.indexOf("?")+1);
						Map<String, String> params = getQueryMap(paramString);
						generateResponse(params);

					}
					//break;
				}
			}
			conn.close();
		}
	}



	public Tracker() throws IOException
	{
		this.server = new ServerSocket(40000);
		addSeeders();

	}


}
