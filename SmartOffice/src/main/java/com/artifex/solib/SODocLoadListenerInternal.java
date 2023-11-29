package com.artifex.solib;

public interface SODocLoadListenerInternal {
    void error(int i, int i2);

    void onLayoutCompleted();

    void onSelectionChanged(int i, int i2);

    void progress(int i, boolean z);

    void setDoc(ArDkDoc arDkDoc);
}
