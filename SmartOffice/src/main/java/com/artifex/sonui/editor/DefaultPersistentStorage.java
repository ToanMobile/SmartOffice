package com.artifex.sonui.editor;

import android.content.Context;
import android.content.SharedPreferences;
import com.artifex.solib.SOPersistentStorage;
import java.util.Map;

public class DefaultPersistentStorage implements SOPersistentStorage {
    public Map<String, ?> getAllStringPreferences(Object obj) {
        return ((SharedPreferences) obj).getAll();
    }

    public Object getStorageObject(Context context, String str) {
        return context.getSharedPreferences(str, 0);
    }

    public String getStringPreference(Object obj, String str, String str2) {
        return ((SharedPreferences) obj).getString(str, str2);
    }

    public void removePreference(Object obj, String str) {
        SharedPreferences.Editor edit = ((SharedPreferences) obj).edit();
        edit.remove(str);
        edit.commit();
    }

    public void setStringPreference(Object obj, String str, String str2) {
        SharedPreferences.Editor edit = ((SharedPreferences) obj).edit();
        edit.putString(str, str2);
        edit.commit();
    }
}
