package com.artifex.solib;

public class SORender extends ArDkRender {
    private long internal;

    public SORender(long j) {
        this.internal = j;
    }

    public native void abort();

    public native void destroy();
}
