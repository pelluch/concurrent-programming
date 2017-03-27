package com.pelluch.iic3373.t1.tracker;

import com.dampcake.bencode.BencodeOutputStream;
import com.pelluch.iic3373.t1.Peer;
import com.pelluch.iic3373.t1.tracker.Tracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pablo on 3/25/17.
 */
public abstract class ConnectionHandler {
    protected Tracker tracker;
    public ConnectionHandler(Tracker tracker) {
        this.tracker = tracker;
    }

    protected ByteArrayOutputStream generateResponse(Map<String, String> paramsMap,
                                    int port)
        throws IOException {
        Peer peer;
        String peerId = paramsMap.get("peer_id");
        if(tracker.hasPeer(peerId)) {
            peer = tracker.getPeer(peerId);
        } else {
            peer = new Peer(peerId);
            tracker.addPeer(peer);
        }

        peer.setNumUploadedBytes(Integer.parseInt(paramsMap.get("uploaded")));
        peer.setNumDownloadedBytes(Integer.parseInt(paramsMap.get("downloaded")));
        peer.setNumBytesLeft(Integer.parseInt(paramsMap.get("left")));
        peer.updateState(paramsMap.get("event"));
        if(peer.getState() == Peer.State.STOPPED) {
            tracker.removePeer(peerId);
        }
        peer.setIp(Tracker.HOST);
        peer.setPort(Integer.parseInt(paramsMap.get("port")));

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        BencodeOutputStream os = new BencodeOutputStream(byteStream);
        Map<String, Object> dict = new HashMap<>();
        dict.put("interval", 1);
        dict.put("complete", tracker.getNumSeeds());
        dict.put("incomplete", tracker.getNumLeeches());
        List<Peer> peers = tracker.getPeers();
        List<Map<String, Object>> mappedPeers = new ArrayList<>();
        for(Peer otherPeer : peers) {
            mappedPeers.add(otherPeer.getMap());
        }
        dict.put("peers", mappedPeers);
        os.writeDictionary(dict);


        // responseStream.write("Asdasdasd".getBytes());

        String peerStr = "";

        for(Peer otherPeer : peers) {
            peerStr += " peer_id = " + otherPeer.getPeerId() + " ip = " + otherPeer.getIp() + " port = " + otherPeer.getPort();
        }
        System.out.println("Responding with: interval = 1, complete = " + tracker.getNumSeeds() + ", " +
            "incomplete = " + tracker.getNumLeeches() + " peers: " + peerStr);

        // httpExchange.sendResponseHeaders(200, );
        return byteStream;
        //os.close();
    }
}
