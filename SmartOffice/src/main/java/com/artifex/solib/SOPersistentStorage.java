package com.artifex.solib;

import android.content.Context;
import java.util.Map;

public interface SOPersistentStorage {
    Map<String, ?> getAllStringPreferences(Object obj);

    Object getStorageObject(Context context, String str);

    String getStringPreference(Object obj, String str, String str2);

    void removePreference(Object obj, String str);

    void setStringPreference(Object obj, String str, String str2);
}
