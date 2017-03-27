package com.pelluch.iic3373.t1.peer;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.BencodeInputStream;
import com.dampcake.bencode.BencodeOutputStream;
import com.dampcake.bencode.Type;
import com.pelluch.iic3373.t1.Peer;
import com.pelluch.iic3373.t1.bcodec.BDecoder;
import com.pelluch.iic3373.t1.bcodec.BEValue;
import com.pelluch.iic3373.t1.bcodec.BEncoder;
import com.pelluch.iic3373.t1.tracker.SocketConnectionHandler;
import com.pelluch.iic3373.t1.tracker.Tracker;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import jdk.internal.util.xml.impl.Input;
import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by pablo on 3/25/17.
 */
public class Seeder {

    private static int startingPort = 9650;
    private int port;
    private long numLeechers;
    private long numSeeders;
    private long interval;
    private List<Peer> peers = new ArrayList<>();
    private ServerSocket server;
    private ByteBuffer peerId;
    private static final String BITTORRENT_ID_PREFIX = "-qB33B0-";
    private final static String torrentFile = "/home/pablo/src/concurrent-programing/data/archlinux-2017.03.01-dual.iso.torrent";
    private byte[] infoHash;

    public Seeder() throws NoSuchAlgorithmException {
        port = startingPort++;
        String id = BITTORRENT_ID_PREFIX + UUID.randomUUID()
                .toString().split("-")[4];
        try {
            peerId = ByteBuffer.wrap(id.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            File file = new File(torrentFile);
            FileInputStream fs = new FileInputStream(torrentFile);
            byte[] torrent = new byte[(int)file.length()];
            fs.read(torrent);
            Map<String, BEValue> decoded = BDecoder.bdecode(
                    new ByteArrayInputStream(torrent)).getMap();
            Map<String, BEValue> info = decoded.get("info").getMap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BEncoder.bencode(info, baos);
            byte[] encodedInfo = baos.toByteArray();

            MessageDigest crypt;
            crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(encodedInfo);
            infoHash = crypt.digest();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void announce() {
        HttpURLConnection urlConnection = null;

        String encoded = null;
        try {
            encoded = URLEncoder.encode(new String(infoHash), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            String idStr = "-DE13E0-i3BL-O.d-)";
            String hashStr = new String(infoHash);
            String urlStr = "http://localhost:6969/announce?";
            StringBuilder builder = new StringBuilder(urlStr);
            Map<String, String> params = new HashMap<>();
            params.put("info_hash", encoded);
            params.put("peer_id", URLEncoder.encode(idStr, "UTF-8"));
            params.put("port", "" + port);
            params.put("uploaded", "0");
            params.put("downloaded", "0");
            params.put("left", "0");
            params.put("compact", "0");
            params.put("event", "completed");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            urlStr = builder.substring(0, builder.length() - 1);
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BEValue value = BDecoder.bdecode(urlConnection.getInputStream());
            Map<String, BEValue> map = value.getMap();

            this.numLeechers = map.get("incomplete").getLong();
            this.numSeeders = map.get("complete").getLong();
            this.interval = map.get("interval").getLong();
            List<BEValue> peerList = map.get("peers").getList();

            for (BEValue peerValue : peerList) {
                Map<String, BEValue> peerMap = peerValue.getMap();
                Peer peer = new Peer(peerMap.get("peer id").toString());
                peer.setIp(peerMap.get("ip").toString());
                int port = peerMap.get("port").getInt();
                peer.setPort(port);
                peers.add(peer);
            }

            System.out.println("Peers: " + peers.size());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void startListening() {
        try {
            server = new ServerSocket(port, 0, InetAddress.getLocalHost());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                Socket client = server.accept();
                System.out.println("Connection accepted!");
                Runnable connectionHandler = new PeerConnectionHandler(client);
                new Thread(connectionHandler).start();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }
}
