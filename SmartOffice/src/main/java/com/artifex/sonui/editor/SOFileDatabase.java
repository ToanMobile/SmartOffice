package com.artifex.sonui.editor;

import com.artifex.source.util.a.util_a.a.a.c$$ExternalSyntheticOutline0;
import android.content.Context;
import android.util.Log;
import androidx.fragment.app.BackStackRecord$$ExternalSyntheticOutline0;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SOPreferences;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class SOFileDatabase {
    public static Context mContext;
    public static SOFileDatabase mDatabase;
    public static Object mSharedPrefs;
    public static String mTempFolderPath;
    public static String mTempThumbsPath;

    public class StateAndKey {
        public String key;
        public SOFileState state;

        public StateAndKey(SOFileDatabase sOFileDatabase) {
        }
    }

    public static SOFileDatabase getDatabase() {
        return mDatabase;
    }

    public static void init(Context context) {
        if (mDatabase == null) {
            mContext = context;
            mDatabase = new SOFileDatabase();
            mSharedPrefs = SOPreferences.getPreferencesObject(mContext, "fileDatabase2");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtils.getTempPathRoot(mContext));
        String str = File.separator;
        mTempFolderPath = BackStackRecord$$ExternalSyntheticOutline0.m(sb, str, ".tmpint", str);
        mTempThumbsPath = FileUtils.getTempPathRoot(mContext) + str + ".thumbs" + str;
        if (!FileUtils.fileExists(mTempFolderPath)) {
            FileUtils.createDirectory(mTempFolderPath);
        }
        if (!FileUtils.fileExists(mTempThumbsPath)) {
            FileUtils.createDirectory(mTempThumbsPath);
        }
    }

    public static String uniqueThumbFilePath() {
        if (!FileUtils.fileExists(mTempThumbsPath)) {
            FileUtils.createDirectory(mTempThumbsPath);
        }
        if (!FileUtils.fileExists(mTempThumbsPath)) {
            return "";
        }
        return mTempThumbsPath + UUID.randomUUID().toString() + ".png";
    }

    public void clearAll() {
        for (Map.Entry next : SOPreferences.getAllStringPreferences(mSharedPrefs).entrySet()) {
            SOFileState value = getValue((String) next.getKey());
            if (value != null) {
                FileUtils.deleteFile(value.getInternalPath());
                FileUtils.deleteFile(value.getThumbnail());
                deleteValue((String) next.getKey());
            }
        }
    }

    public void deleteEntry(String str) {
        deleteValue(str);
    }

    public final void deleteValue(String str) {
        Object obj = mSharedPrefs;
        if (SOPreferences.getPersistentStorage() != null) {
            SOPreferences.getPersistentStorage().removePreference(obj, str);
        } else {
            Log.d("SOPreferences", "No implementation of the SOPersistentStorage interface found");
            throw new RuntimeException();
        }
    }

    public ArrayList<StateAndKey> getStatesAndKeys() {
        ArrayList<StateAndKey> arrayList = new ArrayList<>();
        Map<String, ?> allStringPreferences = SOPreferences.getAllStringPreferences(mSharedPrefs);
        if (allStringPreferences == null) {
            return arrayList;
        }
        for (Map.Entry next : allStringPreferences.entrySet()) {
            StateAndKey stateAndKey = new StateAndKey(this);
            stateAndKey.state = getValue((String) next.getKey());
            stateAndKey.key = (String) next.getKey();
            arrayList.add(stateAndKey);
        }
        return arrayList;
    }

    public SOFileState getValue(String str) {
        return SOFileState.fromString(SOPreferences.getStringPreference(mSharedPrefs, str, ""), this);
    }

    public void setValue(String str, SOFileState sOFileState) {
        SOPreferences.setStringPreference(mSharedPrefs, str, SOFileState.toString(sOFileState));
    }

    public SOFileState stateForPath(String str, boolean z) {
        return stateForPath(str, z, false);
    }

    public SOFileState stateForPath(String str, boolean z, boolean z2) {
        String str2;
        SOFileState value = !z2 ? getValue(str) : null;
        if (value == null || value.getUserPath().isEmpty()) {
            StringBuilder m = c$$ExternalSyntheticOutline0.m(".");
            m.append(FileUtils.getExtension(str));
            String sb = m.toString();
            if (!FileUtils.fileExists(mTempFolderPath)) {
                FileUtils.createDirectory(mTempFolderPath);
            }
            if (FileUtils.fileExists(mTempFolderPath)) {
                str2 = mTempFolderPath + UUID.randomUUID().toString() + sb;
            } else {
                str2 = "";
            }
            if (!str2.equals("")) {
                value = new SOFileState(str, str2, this, 0);
                FileUtils.deleteFile(str2);
                if (!z) {
                    setValue(str, value);
                }
            }
        }
        return value;
    }
}
