package com.artifex.sonui.editor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public static BaseActivity mCurrentActivity;
    public static PermissionResultHandler mPermissionResultHandler;
    public static ResultHandler mResultHandler;
    public static ResumeHandler mResumeHandler;

    public interface PermissionResultHandler {
        boolean handle(int i, String[] strArr, int[] iArr);
    }

    public interface ResultHandler {
        boolean handle(int i, int i2, Intent intent);
    }

    public interface ResumeHandler {
        void handle();
    }

    public static BaseActivity getCurrentActivity() {
        return mCurrentActivity;
    }

    public static void setPermissionResultHandler(PermissionResultHandler permissionResultHandler) {
        mPermissionResultHandler = permissionResultHandler;
    }

    public static void setResultHandler(ResultHandler resultHandler) {
        mResultHandler = resultHandler;
    }

    public static void setResumeHandler(ResumeHandler resumeHandler) {
        mResumeHandler = resumeHandler;
    }

    public boolean isSlideShow() {
        return false;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        ResultHandler resultHandler = mResultHandler;
        if (resultHandler == null || !resultHandler.handle(i, i2, intent)) {
            super.onActivityResult(i, i2, intent);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onPause() {
        mCurrentActivity = null;
        super.onPause();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        PermissionResultHandler permissionResultHandler = mPermissionResultHandler;
        if (permissionResultHandler == null || !permissionResultHandler.handle(i, strArr, iArr)) {
            super.onRequestPermissionsResult(i, strArr, iArr);
        }
    }

    public void onResume() {
        mCurrentActivity = this;
        super.onResume();
        ResumeHandler resumeHandler = mResumeHandler;
        if (resumeHandler != null) {
            resumeHandler.handle();
        }
    }
}
