package com.artifex.solib;

import a.a.a.a.a.c$$ExternalSyntheticOutline0;
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
                StringBuilder m = c$$ExternalSyntheticOutline0.m("exception in Worker.add: ");
                m.append(th.toString());
                Log.e("Worker", m.toString());
            }
        }
    }
}
