package com.pelluch.iic3373.t1.peer;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Created by pablo on 3/25/17.
 */
public class PeerMain {
    private final static String torrent = "/home/pablo/src/concurrent-programming/tareas/t1/data/archlinux-2017.03.01-dual.iso.torrent";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Seeder seeder = new Seeder();
        seeder.announce();

        Thread t = new Thread(seeder::startListening);
        t.start();

        Runtime.getRuntime().addShutdownHook(new Thread(seeder::closeServer));

        Scanner keyboard = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Enter command (quit to exit):");
            String input = keyboard.nextLine();
            if(input != null) {
                System.out.println("Your input is : " + input);
                if ("q".equals(input)) {
                    System.out.println("Exit programm");
                    exit = true;
                    seeder.closeServer();
                }
            }
        }
        keyboard.close();
    }
}
