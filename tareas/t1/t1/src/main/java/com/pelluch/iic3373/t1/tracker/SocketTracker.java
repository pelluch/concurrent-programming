package com.pelluch.iic3373.t1.tracker;

import java.io.IOException;
import java.net.*;

/**
 * Created by pablo on 3/25/17.
 */
public class SocketTracker extends Tracker {

    private ServerSocket server;
    public SocketTracker(int port) {
        try {
            server = new ServerSocket(port, 0, InetAddress.getLocalHost());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeServer() {
        if(!server.isClosed()) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startListening() {
        while(true) {
            try {
                Socket client = server.accept();
                Runnable connectionHandler = new SocketConnectionHandler(client);
                new Thread(connectionHandler).start();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


}
