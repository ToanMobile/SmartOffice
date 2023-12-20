package com.artifex.solib;

public interface SODocLoadListener {
    void error(int i2, int i3);

    void onLayoutCompleted();

    void onSelectionChanged(int i2, int i3);

    void progress(int i2, boolean z);

    void setDoc(ArDkDoc arDkDoc);
}
