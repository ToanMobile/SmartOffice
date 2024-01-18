package com.artifex.sonui.editor;

import android.content.Context;
import android.util.Base64;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SOPreferences;
import java.io.UnsupportedEncodingException;

public class SOFileState {
    public static boolean mDontAutoOpen = false;
    public final SOFileDatabase mDatabase;
    public String mForeignData;
    public boolean mHasChanges;
    public final String mInternalPath;
    public long mLastAccess;
    public String mOpenedPath;
    public int mPageNumber;
    public boolean mPagesListVisible;
    public float mScale;
    public int mScrollX;
    public int mScrollY;
    public String mThumbPath;
    public String mUserPath;

    public SOFileState(String str, String str2, String str3, long j, boolean z, String str4, SOFileDatabase sOFileDatabase, int i, float f, int i2, int i3, boolean z2) {
        this.mPageNumber = 0;
        this.mScale = 1.0f;
        this.mScrollX = 0;
        this.mScrollY = 0;
        this.mPagesListVisible = false;
        this.mUserPath = str;
        this.mInternalPath = str2;
        this.mLastAccess = j;
        this.mHasChanges = z;
        this.mDatabase = sOFileDatabase;
        this.mThumbPath = str4;
        this.mOpenedPath = str3;
        this.mForeignData = null;
        this.mPageNumber = i;
        this.mScale = f;
        this.mScrollX = i2;
        this.mScrollY = i3;
        this.mPagesListVisible = z2;
    }

    public static void clearAutoOpen(Context context) {
        SOPreferences.setStringPreference(SOPreferences.getPreferencesObject(context, "general"), "autoOpen", "");
    }

    public static String decode(String str) {
        if (str.equals("--null--")) {
            return null;
        }
        if (str.equals("--empty--")) {
            return "";
        }
        try {
            return new String(Base64.decode(str, 0), "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return "";
        }
    }

    public static String encode(String str) {
        if (str == null) {
            return "--null--";
        }
        if (str.isEmpty()) {
            return "--empty--";
        }
        try {
            return Base64.encodeToString(str.getBytes("UTF-8"), 0);
        } catch (UnsupportedEncodingException unused) {
            return "";
        }
    }

    public static SOFileState fromString(String str, SOFileDatabase sOFileDatabase) {
        String str2 = str;
        if (str2 == null || str.isEmpty()) {
            return null;
        }
        String[] split = str2.split("\\|");
        if (split.length < 6) {
            return null;
        }
        String str3 = "";
        String decode = decode(split[0]);
        String decode2 = decode(split[1]);
        long j = 0;
        j = Long.parseLong(split[2], 10);
        boolean equals = split[3].equals("TRUE");
        String decode3 = decode(split[4]);
        String decode4 = decode(split[5]);
        if (split.length >= 7) {
            str3 = decode(split[6]);
        }
        SOFileState sOFileState = new SOFileState(decode, decode2, decode4, j, equals, decode3, sOFileDatabase, split.length >= 8 ? Integer.parseInt(split[7], 10) : 0, split.length >= 9 ? Float.parseFloat(split[8]) : 1.0f, split.length >= 10 ? Integer.parseInt(split[9], 10) : 0, split.length >= 11 ? Integer.parseInt(split[10], 10) : 0, split.length >= 12 ? Boolean.parseBoolean(split[11]) : false);
        sOFileState.setForeignData(str3);
        return sOFileState;
    }

    public static SOFileState getAutoOpen(Context context) {
        String stringPreference = SOPreferences.getStringPreference(SOPreferences.getPreferencesObject(context, "general"), "autoOpen", "");
        if (stringPreference == null || stringPreference.isEmpty()) {
            return null;
        }
        return fromString(stringPreference, SOFileDatabase.getDatabase());
    }

    public static void setAutoOpen(Context context, SOFileState sOFileState) {
        if (!mDontAutoOpen) {
            SOPreferences.setStringPreference(SOPreferences.getPreferencesObject(context, "general"), "autoOpen", toString(sOFileState));
        }
    }

    public static String toString(SOFileState sOFileState) {
        StringBuilder m = new StringBuilder();
        m.append(encode(sOFileState.mUserPath));
        m.append("|");
        StringBuilder m2 = new StringBuilder(m.toString());
        m2.append(encode(sOFileState.mInternalPath));
        m2.append("|");
        StringBuilder m3 = new StringBuilder(m2.toString());
        m3.append(sOFileState.mLastAccess);
        m3.append("|");
        StringBuilder m4 = new StringBuilder(m3.toString()).append(sOFileState.mHasChanges ? "TRUE" : "FALSE").append("|");
        m4.append(encode(sOFileState.mThumbPath));
        m4.append("|");
        StringBuilder m5 = new StringBuilder(m4.toString());
        m5.append(encode(sOFileState.mOpenedPath));
        m5.append("|");
        StringBuilder m6 = new StringBuilder(m5.toString());
        m6.append(encode(sOFileState.mForeignData));
        m6.append("|");
        StringBuilder m7 = new StringBuilder(m6.toString());
        m7.append(String.valueOf(sOFileState.mPageNumber));
        m7.append("|");
        StringBuilder m8 = new StringBuilder(m7.toString());
        m8.append(String.valueOf(sOFileState.mScale));
        m8.append("|");
        StringBuilder m9 = new StringBuilder(m8.toString());
        m9.append(String.valueOf(sOFileState.mScrollX));
        m9.append("|");
        StringBuilder m10 = new StringBuilder(m9.toString());
        m10.append(String.valueOf(sOFileState.mScrollY));
        m10.append("|");
        StringBuilder m11 = new StringBuilder(m10.toString());
        m11.append(String.valueOf(sOFileState.mPagesListVisible));
        m11.append("|");
        return m11.toString();
    }

    public void closeFile() {
        FileUtils.deleteFile(this.mInternalPath);
        updateAccess();
        persist();
    }

    public SOFileState copy() {
        return fromString(toString(this), SOFileDatabase.getDatabase());
    }

    public void deleteThumbnailFile() {
        if (FileUtils.fileExists(this.mThumbPath)) {
            FileUtils.deleteFile(this.mThumbPath);
        }
    }

    public String getForeignData() {
        return this.mForeignData;
    }

    public String getInternalPath() {
        return this.mInternalPath;
    }

    public long getLastAccess() {
        return this.mLastAccess;
    }

    public String getOpenedPath() {
        return this.mOpenedPath;
    }

    public boolean getPageListVisible() {
        return this.mPagesListVisible;
    }

    public int getPageNumber() {
        return this.mPageNumber;
    }

    public float getScale() {
        return this.mScale;
    }

    public int getScrollX() {
        return this.mScrollX;
    }

    public int getScrollY() {
        return this.mScrollY;
    }

    public String getThumbnail() {
        return this.mThumbPath;
    }

    public String getUserPath() {
        return this.mUserPath;
    }

    public boolean hasChanges() {
        return this.mHasChanges;
    }

    public boolean isTemplate() {
        String str = this.mUserPath;
        if (str != null && !str.isEmpty()) {
            return false;
        }
        return true;
    }

    public void openFile(boolean z) {
        if (!FileUtils.fileExists(this.mInternalPath)) {
            FileUtils.copyFile(this.mUserPath, this.mInternalPath, true);
        } else {
            String str = this.mUserPath;
            if (str == null) {
                str = this.mOpenedPath;
            }
            if (FileUtils.fileLastModified(str) > this.mLastAccess) {
                FileUtils.copyFile(this.mUserPath, this.mInternalPath, true);
            }
        }
        if (z) {
            this.mUserPath = null;
            return;
        }
        updateAccess();
        persist();
    }

    public final void persist() {
        String str;
        SOFileDatabase sOFileDatabase = this.mDatabase;
        if (sOFileDatabase != null && (str = this.mUserPath) != null) {
            sOFileDatabase.setValue(str, this);
        }
    }

    public void saveFile() {
        if (FileUtils.fileExists(this.mInternalPath)) {
            String str = this.mInternalPath;
            String str2 = this.mUserPath;
            if (FileUtils.fileExists(str)) {
                boolean fileExists = FileUtils.fileExists(str2);
                String m = str2 + "xxx";
                if (!FileUtils.fileExists(m) && (!fileExists || FileUtils.renameFile(str2, m))) {
                    if (FileUtils.copyFile(str, str2, true)) {
                        FileUtils.deleteFile(m);
                    } else if (fileExists) {
                        FileUtils.renameFile(m, str2);
                    }
                }
            }
            this.mOpenedPath = this.mUserPath;
            this.mHasChanges = false;
            updateAccess();
            persist();
        }
    }

    public void setForeignData(String str) {
        this.mForeignData = str;
        persist();
    }

    public void setHasChanges(boolean z) {
        this.mHasChanges = z;
    }

    public void setOpenedPath(String str) {
        this.mOpenedPath = str;
    }

    public void setPageListVisible(boolean z) {
        this.mPagesListVisible = z;
    }

    public void setPageNumber(int i) {
        this.mPageNumber = i;
    }

    public void setScale(float f) {
        this.mScale = f;
    }

    public void setScrollX(int i) {
        this.mScrollX = i;
    }

    public void setScrollY(int i) {
        this.mScrollY = i;
    }

    public void setThumbnail(String str) {
        this.mThumbPath = str;
        persist();
    }

    public void setUserPath(String str) {
        this.mUserPath = str;
    }

    public void updateAccess() {
        this.mLastAccess = System.currentTimeMillis();
    }

    public SOFileState(String str, String str2, SOFileDatabase sOFileDatabase, int i) {
        this(str, str2, str, 0, false, "", sOFileDatabase, i, 1.0f, 0, 0, false);
    }

    public SOFileState(SOFileState sOFileState) {
        this.mPageNumber = 0;
        this.mScale = 1.0f;
        this.mScrollX = 0;
        this.mScrollY = 0;
        this.mPagesListVisible = false;
        this.mUserPath = sOFileState.mUserPath;
        this.mInternalPath = sOFileState.mInternalPath;
        this.mLastAccess = sOFileState.mLastAccess;
        this.mHasChanges = sOFileState.mHasChanges;
        this.mDatabase = sOFileState.mDatabase;
        this.mThumbPath = sOFileState.mThumbPath;
        this.mOpenedPath = sOFileState.mOpenedPath;
        this.mForeignData = sOFileState.getForeignData();
        this.mPageNumber = sOFileState.mPageNumber;
        this.mScale = sOFileState.mScale;
        this.mScrollX = sOFileState.mScrollX;
        this.mScrollY = sOFileState.mScrollY;
        this.mPagesListVisible = sOFileState.mPagesListVisible;
    }
}
