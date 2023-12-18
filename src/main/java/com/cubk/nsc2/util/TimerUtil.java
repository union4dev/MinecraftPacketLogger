package com.cubk.nsc2.util;

public class TimerUtil {
    public long lastMS = System.currentTimeMillis();
    public long time;

    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (System.currentTimeMillis() - lastMS > time) {
            if (reset) reset();
            return true;
        }

        return false;
    }

    public long elapsed() {
        return System.currentTimeMillis() - lastMS;
    }

    public boolean hasTimeElapsed(long time) {
        this.time = time;
        return System.currentTimeMillis() - lastMS > this.time;
    }

    public long getTime() {
        return System.currentTimeMillis() - lastMS;
    }

    public void setTime(long time) {
        lastMS = time;
    }

    public static final class CPSDelay {
        private final TimerUtil timerUtils = new TimerUtil();

        public boolean shouldAttack(int cps) {
            int aps = 20 / cps;
            return timerUtils.hasTimeElapsed(50 * aps, true);
        }

        public void reset() {
            timerUtils.reset();
        }
    }
}
