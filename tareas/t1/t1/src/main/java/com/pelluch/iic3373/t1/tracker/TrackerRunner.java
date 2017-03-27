package com.pelluch.iic3373.t1.tracker;

/**
 * Created by pablo on 3/25/17.
 */
public class TrackerRunner {
    public static void main(String[] args) {
        /*

        */
        HttpTracker tracker = new HttpTracker(6969);
        tracker.startListening();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                tracker.closeServer();
            }
        });

    }
}
