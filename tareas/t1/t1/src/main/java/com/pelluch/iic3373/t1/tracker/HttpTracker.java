package com.pelluch.iic3373.t1.tracker;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.*;

/**
 * Created by pablo on 3/25/17.
 */
public class HttpTracker extends Tracker {

    private HttpServer server;

    HttpTracker(int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(InetAddress.getLocalHost(), port), 0);
            server.createContext(URL, new HttpConnectionHandler(this));
            server.setExecutor(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeServer() {
        server.stop(1);
    }

    @Override
    public void startListening() {
        server.start();
    }

}
