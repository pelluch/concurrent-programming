package org.jtorrent;

import java.io.*;
import java.net.*;
import java.util.*;

import org.klomp.snark.bencode.*;

import java.util.Random;

public class Peer implements Runnable {

	private static Random randomGenerator = new Random();
	private byte[] peer_id = new byte[20];
	private String ip = "localhost";
	private static int startingPortNumber = 45000;
	private int portNumber;
	private Map encodedInfo = new HashMap();
	private Socket client;
	private ServerSocket server;
	
	public Peer() throws IOException
	{
		portNumber = startingPortNumber++;
		//randomGenerator.nextBytes(peer_id);
		for(int i = 0; i < 20; ++i)
		{
			peer_id[i] = 'A';
		}
		encodedInfo.put("peer id", peer_id);
		encodedInfo.put("ip", ip);
		encodedInfo.put("port", portNumber);
		
		server = new ServerSocket(portNumber);
	}
	
	public Map getEncodedInfo()
	{
		//System.out.println("PEER INFO " + encodedInfo.get("peer_id"));
		return encodedInfo;
	}
	
	public byte[] getCompactInfo()
	{
		byte[] info = new byte[6];
		info[0] = 127;
		info[1] = 0;
		info[2] = 0;
		info[3] = 1;
		info[4] = (byte)((portNumber & 240) >> 4);
		info[5] = (byte)(portNumber & 15);
		return info;
	}

	public void acceptConnections() throws IOException {
		while(true) {
			
			Socket client = server.accept();			
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			// don't use buffered writer because we need to write both "text" and "binary"
			OutputStream out = client.getOutputStream();
			String line = reader.readLine();
			System.out.println("ACCEPTED PEER CONNECTION");
			if (line == null)
			{
				System.out.println("Connection closed");
				break;
			}
			
			if (!line.equals(""))
			{
				byte[] b = line.getBytes("UTF-8");
				line = new String(b, "UTF-8");
				System.out.println("ACCEPTED PEER CONNECTION: " + line);				
				//break;
			}
		}
	}
	@Override
	public void run()  {
		// TODO Auto-generated method stub
		try {
			acceptConnections();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
