package com.artifex.solib;

import android.content.Context;
import android.util.Log;
import java.util.Map;

public class SOPreferences {
    public static SOPersistentStorage mDefaultPersistentStorage;
    public static SOPersistentStorage mPersistentStorage;

    public static Map<String, ?> getAllStringPreferences(Object obj) {
        if (getPersistentStorage() != null) {
            return getPersistentStorage().getAllStringPreferences(obj);
        }
        Log.d("SOPreferences", "No implementation of the SOPersistentStorage interface found");
        throw new RuntimeException();
    }

    public static SOPersistentStorage getPersistentStorage() {
        SOPersistentStorage sOPersistentStorage = mPersistentStorage;
        if (sOPersistentStorage != null) {
            return sOPersistentStorage;
        }
        if (mDefaultPersistentStorage == null) {
            mDefaultPersistentStorage = new DefaultPersistentStorage();
        }
        return mDefaultPersistentStorage;
    }

    public static Object getPreferencesObject(Context context, String str) {
        if (getPersistentStorage() != null) {
            return getPersistentStorage().getStorageObject(context, str);
        }
        Log.d("SOPreferences", "No implementation of the SOPersistentStorage interface found");
        throw new RuntimeException();
    }

    public static String getStringPreference(Object obj, String str, String str2) {
        if (getPersistentStorage() != null) {
            return getPersistentStorage().getStringPreference(obj, str, str2);
        }
        Log.d("SOPreferences", "No implementation of the SOPersistentStorage interface found");
        throw new RuntimeException();
    }

    public static void setStringPreference(Object obj, String str, String str2) {
        if (getPersistentStorage() != null) {
            getPersistentStorage().setStringPreference(obj, str, str2);
        } else {
            Log.d("SOPreferences", "No implementation of the SOPersistentStorage interface found");
            throw new RuntimeException();
        }
    }
}
