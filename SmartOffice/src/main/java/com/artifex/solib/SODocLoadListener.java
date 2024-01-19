package com.artifex.solib;

public interface SODocLoadListener {

    void onSelectionChanged(int i2, int i3);

    void progress(int i2, boolean z);

    void setDoc(ArDkDoc arDkDoc);

    void onDocComplete();

    void onError(int i, int i2);

    void onPageLoad(int i);

    void onLayoutCompleted();
}
