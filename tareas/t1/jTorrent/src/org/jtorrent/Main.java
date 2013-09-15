package org.jtorrent;
import org.klomp.snark.bencode.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class Main {


	public static byte[] toSHA1(byte[] convertme) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		}
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		byte[] values = md.digest(convertme);
		return md.digest(convertme);
	}

	
	public static void generateTorrent(String torrentPath, String announceUrl, File testFile) throws IOException
	{
		//Archivo de prueba

		File torrentFile = new File(torrentPath);
		//info
		Map metaInfo = new HashMap();
		Map info = new HashMap();
		//info --> files

		int pieceLength = 131072;
		
		//BEValue pathsValue = new BEValue(fullPath);
		Number fileLengthNumber = testFile.length();
		int fileLength = (int)testFile.length();
		int numPieces = fileLength/pieceLength;
		int rest = fileLength % pieceLength;
		int numBytes = 20*numPieces;
		if(rest > 0)
		{
			numBytes += 20;
		}
		byte[] pieces = new byte[numBytes];
		FileInputStream reader = new FileInputStream(testFile);

		for(int j = 0; j < numPieces; ++j) {
			
			byte[] currentPiece = new byte[pieceLength];
			int result = reader.read(currentPiece);
			byte[] sha1 = toSHA1(currentPiece);
			for(int i = 0; i < 20; ++i)
			{
				pieces[j*20 + i] = sha1[i];
			}
		}
		
		if(rest > 0)
		{
			byte[] currentPiece = new byte[rest];
			int result = reader.read(currentPiece);
			byte[] sha1 = toSHA1(currentPiece);
			for(int i = 0; i < 20; ++i)
			{
				pieces[numPieces*20 + i] = sha1[i];
			}
		}

		//BEValue fileLengthValue = new BEValue(fileList[i].length());
		//BEValue pathsValue = new BEValue(pathList);

		info.put("length", fileLengthNumber);
		info.put("name", "arquitectura.pdf");
		info.put("piece length", pieceLength);
		info.put("pieces", pieces);
	
		metaInfo.put("info", info);
		metaInfo.put("announce", announceUrl);

		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(torrentFile, false);
			BEncoder.bencode(metaInfo, writer);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
			}
		}
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String torrentPath = "arquitectura.torrent";
		String announceUrl = "http://127.0.0.1:40000/announce";
		File testFile = new File("/home/pablo/arquitectura.pdf");
		generateTorrent(torrentPath, announceUrl, testFile);
		Tracker tracker = new Tracker();
		tracker.listen();
		
		
	}

}
