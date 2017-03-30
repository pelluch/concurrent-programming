package com.pelluch.iic3373.t1.peer;

import com.pelluch.iic3373.t1.Utils;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by pablo on 3/25/17.
 */
public class PeerConnectionHandler implements Runnable {

    private Socket clientSocket;
    private final int TOTAL_LENGTH = 499122176;
    private final int NUM_PIECES = 952;
    private final String FILE_PATH = "/home/pablo/src/concurrent-programming/tareas/t1/data/downloaded/archlinux-2017.03.01-dual.iso";
    private Seeder seeder;

    PeerConnectionHandler(Seeder seeder, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.seeder = seeder;
    }

    @Override
    public void run() {
        if(clientSocket != null) {
            try {
                InputStream stream = clientSocket.getInputStream();
                byte[] data = new byte[68];
                int count = stream.read(data);
                byte pstrlen = data[0];
                System.out.println("pstrlen: " + (int)pstrlen);
                int startIndex = 1;
                String pstr = new String(Arrays.copyOfRange(data, startIndex, startIndex + (int)pstrlen));
                System.out.println(pstr);
                startIndex += (int)pstrlen + 8;
                byte[] infoHash = Arrays.copyOfRange(data, startIndex, startIndex + 20);
                startIndex += 20;
                byte[] peerId = Arrays.copyOfRange(data, startIndex, startIndex + 20);
                String peerStr = new String(peerId);
                System.out.println(peerStr);

                // PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);

                System.out.println("Done");
                OutputStream os = clientSocket.getOutputStream();
                byte[] response = new byte[68];
                System.arraycopy(data, 0, response, 0, data.length - 20);
                byte[] seederId = seeder.getPeerIdBytes();
                System.arraycopy(seederId, 0, response,  response.length - 20, 20);
                os.write(response);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
