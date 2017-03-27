package com.pelluch.iic3373.t1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pablo on 3/25/17.
 */
public class Peer {

    public enum State {
        STARTED,
        STOPPED,
        COMPLETED
    }

    private State state;
    private String peerId;
    private int numUploadedBytes;

    public State getState() {
        return state;
    }

    public Peer(String peerId) {
        this.peerId = peerId;
        this.state = State.STARTED;
    }
    public String getPeerId() {
        return peerId;
    }


    public int getNumUploadedBytes() {
        return numUploadedBytes;
    }

    public void setNumUploadedBytes(int numUploadedBytes) {
        this.numUploadedBytes = numUploadedBytes;
    }

    public int getNumDownloadedBytes() {
        return numDownloadedBytes;
    }

    public void setNumDownloadedBytes(int numDownloadedBytes) {
        this.numDownloadedBytes = numDownloadedBytes;
    }

    public int getNumBytesLeft() {
        return numBytesLeft;
    }

    public void setNumBytesLeft(int numBytesLeft) {
        this.numBytesLeft = numBytesLeft;
    }

    private int numDownloadedBytes;
    private int numBytesLeft;
    private String ip;
    private int port;

    public String getIp() {
        return ip;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("peer id", peerId);
        map.put("ip", ip);
        map.put("port", port);
        return map;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void updateState(String event) {
        if(event == null || event.isEmpty()) {
            return;
        }
        switch (event) {
            case "started":
                state = State.STARTED;
                break;
            case "stopped":
                state = State.STOPPED;
                break;
            case "completed":
                state = State.COMPLETED;
                break;
        }
    }

}
