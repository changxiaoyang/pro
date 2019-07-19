package com.cxy890.stater.support;

/**
 * @author BD-PC27
 */
public class SimpleStopWatch {

    private long startMillis;

    private long currentMillis;

    public SimpleStopWatch(boolean start) {
        if (start) start();
    }

    public void start() {
        this.startMillis = System.currentTimeMillis();
        this.currentMillis = startMillis;
    }

    public long issueAndReset() {
        long current = System.currentTimeMillis();
        long result = current - this.currentMillis;
        this.currentMillis = current;
        return result;
    }

    public long totalCostMillis() {
        long current = System.currentTimeMillis();
        long result = current - this.startMillis;
        this.currentMillis = current;
        return result;
    }


}
