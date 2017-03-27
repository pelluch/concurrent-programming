package com.pelluch.iic3373.t1.peer;

import com.pelluch.iic3373.t1.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by pablo on 3/25/17.
 */
public class PeerConnectionHandler implements Runnable {

    private Socket clientSocket;
    PeerConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        if(clientSocket != null) {
            try {
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String line = reader.readLine();

                while(line != null && !line.isEmpty()) {
                    System.out.println(line);
                    line = reader.readLine();
                }
                reader.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
