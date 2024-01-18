package com.artifex.solib;

import android.os.Looper;
import android.util.Log;
import java.util.concurrent.LinkedBlockingDeque;

public class Worker {
    public boolean alive;
    public Looper mLooper = null;
    public LinkedBlockingDeque<Task> mQueue = new LinkedBlockingDeque<>();
    public Thread mThread = null;

    public static class Task implements Runnable {
        public void run() {
        }

        public void work() {
        }
    }

    public Worker(Looper looper) {
        this.mLooper = looper;
    }

    public void add(Task task) {
        if (this.alive) {
            try {
                this.mQueue.put(task);
            } catch (Throwable th) {
                StringBuilder m = new StringBuilder("exception in Worker.add: ");
                m.append(th);
                Log.e("Worker", m.toString());
            }
        }
    }
}
