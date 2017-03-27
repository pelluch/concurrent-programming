package com.pelluch.iic3373.t1.peer;

import java.security.NoSuchAlgorithmException;

/**
 * Created by pablo on 3/25/17.
 */
public class PeerRunner {
    private final static String torrent = "/home/pablo/src/concurrent-programing/data/archlinux-2017.03.01-dual.iso.torrent";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Seeder seeder = new Seeder();
        seeder.announce();
        seeder.startListening();
    }
}
