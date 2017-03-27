package com.pelluch.iic3373.t1.tracker;

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
public class SocketConnectionHandler implements Runnable {

    private Socket clientSocket;
    private String infoHash;
    private String peerId;
    private int numUploadedBytes;
    private int numDownloadedBytes;
    private int numBytesLeft;
    private String event;
    private String key;

    public SocketConnectionHandler(Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {
        if(clientSocket != null) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                String request = in.readLine();
                String[] requestParam = request.split(" ");
                String getRequest = requestParam[1];
                int paramStartIdx = getRequest.indexOf("?") + 1;
                String getParams = getRequest.substring(paramStartIdx);

                Map<String, String> paramsMap = Utils.queryToMap(URLDecoder.decode(getParams, "UTF-8"));
                this.infoHash = paramsMap.get("info_hash");
                this.peerId = paramsMap.get("peer_id");
                this.numUploadedBytes = Integer.parseInt(paramsMap.get("uploaded"));
                this.numDownloadedBytes = Integer.parseInt(paramsMap.get("downloaded"));
                this.numBytesLeft = Integer.parseInt(paramsMap.get("left"));
                this.key = paramsMap.get("key");
                this.event = paramsMap.get("event");

                while (!(inputLine = in.readLine()).equals("")) {
                    System.out.println(inputLine);
                }
                in.close();
                clientSocket.close();



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
