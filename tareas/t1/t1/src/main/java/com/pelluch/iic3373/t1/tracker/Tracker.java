package com.pelluch.iic3373.t1.tracker;

import com.pelluch.iic3373.t1.Peer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pablo on 3/25/17.
 */
public abstract class Tracker {

    protected Map<String, Peer> peers = new HashMap<>();
    protected final static String URL = "/announce";
    public final static String HOST = "localhost";

    public abstract void closeServer();
    public abstract void startListening();

    public int getNumSeeds() {
        int numSeeds = 0;
        for(Peer peer : peers.values()) {
            if(
                    peer.getState() != Peer.State.STOPPED &&
                            (peer.getState() == Peer.State.COMPLETED || peer.getNumBytesLeft() == 0))
                ++numSeeds;
        }
        return numSeeds;
    }

    public int getNumLeeches() {
        int numLeeches = 0;
        for(Peer peer : peers.values()) {
            if(
                    peer.getState() != Peer.State.STOPPED &&
                            (peer.getState() == Peer.State.STARTED && peer.getNumBytesLeft() > 0))
                ++numLeeches;
        }
        return numLeeches;
    }

    public List<Peer> getPeers() {
        return new ArrayList<Peer>(peers.values());
    }

    public void removePeer(String peerId) {
        peers.remove(peerId);
    }

    public boolean hasPeer(String peerId) {
        return peers.containsKey(peerId);
    }

    public Peer getPeer(String peerId) {
        return peers.get(peerId);
    }

    public void addPeer(Peer peer) {
        peers.put(peer.getPeerId(), peer);
    }


}
