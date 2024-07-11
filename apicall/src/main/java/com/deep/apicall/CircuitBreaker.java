package com.deep.apicall;

public class CircuitBreaker {
    private enum State { CLOSED, OPEN, HALF_OPEN }
    private State state = State.CLOSED;
    private long lastFailureTime;
    private int failureCount;
    private final int failureThreshold;
    private final long retryTimePeriod;

    public CircuitBreaker(int failureThreshold, long retryTimePeriod) {
        this.failureThreshold = failureThreshold;
        this.retryTimePeriod = retryTimePeriod;
    }

    public synchronized boolean allowRequest() {
        switch (state) {
            case OPEN:
                if (System.currentTimeMillis() - lastFailureTime > retryTimePeriod) {
                    state = State.HALF_OPEN;
                    return true;
                }
                return false;
            case HALF_OPEN:
                return true;
            case CLOSED:
                return true;
            default:
                return true;
        }
    }

    public synchronized void recordFailure() {
        failureCount++;
        if (failureCount >= failureThreshold) {
            state = State.OPEN;
            lastFailureTime = System.currentTimeMillis();
        }
    }

    public synchronized void recordSuccess() {
        state = State.CLOSED;
        failureCount = 0;
    }
}
