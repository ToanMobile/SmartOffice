package com.artifex.sonui.editor;

import android.os.Parcel;

public class ViewingState {
    public boolean pageListVisible = false;
    public int pageNumber = 0;
    public float scale = 1.0f;
    public int scrollX = 0;
    public int scrollY = 0;

    public ViewingState() {
    }

    public ViewingState(int i) {
        this.pageNumber = i;
    }

    public ViewingState(SOFileState sOFileState) {
        this.pageNumber = sOFileState.getPageNumber();
        this.scale = sOFileState.getScale();
        this.scrollX = sOFileState.getScrollX();
        this.scrollY = sOFileState.getScrollY();
        this.pageListVisible = sOFileState.getPageListVisible();
    }

    public ViewingState(Parcel parcel) {
        boolean z = false;
        this.pageNumber = parcel.readInt();
        this.scale = parcel.readFloat();
        this.scrollX = parcel.readInt();
        this.scrollY = parcel.readInt();
        this.pageListVisible = parcel.readByte() != 0 ? true : z;
    }
}
