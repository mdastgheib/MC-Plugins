package com.nova.novascoreboards.utils;


public class TPSManager implements Runnable {
    public static int TICK_COUNT = 0;
    public static long[] TICKS = new long[600];

    public static double getTPS() {
        return getTPS(100);
    }

    public static double getTPS(int ticks) {
        if (TICK_COUNT < ticks) {
            return 20D;
        }
        try {
            int target = (TICK_COUNT - 1 - ticks) % TICKS.length;
            long elapsed = System.currentTimeMillis() - TICKS[target];

            double tps = ticks / (elapsed / 1000D);
            return tps > 20D ? 20D : tps;
        } catch (Exception ex) {
            return 20D;
        }
    }

    public static long getElapsed(int tickID) {
        if (TICK_COUNT - tickID >= TICKS.length) {
        }
        long time = TICKS[(tickID % TICKS.length)];
        return System.currentTimeMillis() - time;
    }

    public void run() {
        TICKS[(TICK_COUNT % TICKS.length)] = System.currentTimeMillis();

        if (TICK_COUNT >= Integer.MAX_VALUE) {
            TICKS = new long[600];
            TICK_COUNT = 0;
        } else {
            TICK_COUNT++;
        }
    }
}