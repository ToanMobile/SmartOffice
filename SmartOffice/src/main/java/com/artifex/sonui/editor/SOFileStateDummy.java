package com.artifex.sonui.editor;

public class SOFileStateDummy extends SOFileState {
    public SOFileStateDummy(String str) {
        super(str, str, (SOFileDatabase) null, 0);
    }

    public void closeFile() {
    }

    public void deleteThumbnailFile() {
    }

    public String getThumbnail() {
        return null;
    }

    public void openFile(boolean z) {
    }

    public void saveFile() {
    }

    public void setThumbnail(String str) {
    }
}
