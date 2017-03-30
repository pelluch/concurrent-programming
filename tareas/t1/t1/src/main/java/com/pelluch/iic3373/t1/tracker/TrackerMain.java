package com.pelluch.iic3373.t1.tracker;

import java.util.Scanner;

/**
 * Created by pablo on 3/25/17.
 */
public class TrackerMain {
    public static void main(String[] args) {
        /*

        */
        HttpTracker tracker = new HttpTracker(6969);
        tracker.startListening();
        Runtime.getRuntime().addShutdownHook(new Thread(tracker::closeServer));

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
                    tracker.closeServer();
                }
            }
        }
        keyboard.close();

    }
}
