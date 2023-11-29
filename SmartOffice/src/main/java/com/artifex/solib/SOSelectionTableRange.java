package com.artifex.solib;

public class SOSelectionTableRange {
    private long internal;

    private SOSelectionTableRange(long j) {
        this.internal = j;
    }

    private native void destroy();

    public native int columnCount();

    public void finalize() throws Throwable {
        try {
            destroy();
        } finally {
            super.finalize();
        }
    }

    public native int firstColumn();

    public native int firstRow();

    public native int rowCount();
}
