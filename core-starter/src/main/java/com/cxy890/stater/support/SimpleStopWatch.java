package com.cxy890.stater.support;

/**
 * @author BD-PC27
 */
public class SimpleStopWatch {

    private long startMillis;

    public SimpleStopWatch(boolean start) {
        if (start) start();
    }

    public void start() {
        this.startMillis = System.currentTimeMillis();
    }

    public long issueAndReset() {
        long current = System.currentTimeMillis();
        long result = current - this.startMillis;
        this.startMillis = current;
        return result;
    }


}
