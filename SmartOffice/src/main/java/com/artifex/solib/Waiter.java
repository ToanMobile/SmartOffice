package com.artifex.solib;

import com.vungle.warren.AdLoader;

public class Waiter {
    public volatile Object lock = new Object();
    public volatile long startingThreadId = Thread.currentThread().getId();
    public long timeout = AdLoader.RETRY_DELAY;
    public volatile boolean value = false;
    public volatile boolean waiting;

    public Waiter() {
    }

    public boolean doWait() {
        if (Thread.currentThread().getId() == this.startingThreadId) {
            synchronized (this.lock) {
                this.waiting = true;
                long currentTimeMillis = System.currentTimeMillis();
                long j = this.timeout;
                while (true) {
                    if (!this.waiting) {
                        break;
                    }
                    try {
                        this.lock.wait(j);
                        if (this.waiting) {
                            long currentTimeMillis2 = System.currentTimeMillis();
                            long j2 = this.timeout;
                            j = j2 - (currentTimeMillis2 - currentTimeMillis);
                            if (j2 != 0 && j <= 0) {
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        this.waiting = false;
                        return false;
                    }
                }
                if (!this.waiting) {
                    return true;
                }
                this.waiting = false;
                return false;
            }
        }
        throw new RuntimeException("Waiter.doWait must be called on the same thread is its constructor.");
    }

    public void done() {
        if (Thread.currentThread().getId() != this.startingThreadId) {
            synchronized (this.lock) {
                if (this.waiting) {
                    this.waiting = false;
                    this.lock.notify();
                    return;
                }
                return;
            }
        }
        throw new RuntimeException("Waiter.done must NOT be called on the same thread is its constructor.");
    }

    public Waiter(long j) {
        this.timeout = j;
    }
}
