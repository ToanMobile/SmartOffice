package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Intent;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ConfigOptions;
import java.io.IOException;

public interface SODataLeakHandlers {
    void customSaveHandler(String str, ArDkDoc arDkDoc, String str2, SOCustomSaveComplete sOCustomSaveComplete) throws UnsupportedOperationException, IOException;

    void doInsert();

    void exportPdfAsHandler(String str, String str2, ArDkDoc arDkDoc) throws UnsupportedOperationException;

    void finaliseDataLeakHandlers();

    void initDataLeakHandlers(Activity activity, ConfigOptions configOptions) throws IOException;

    void insertImageHandler(NUIDocView nUIDocView) throws UnsupportedOperationException;

    void insertPhotoHandler(NUIDocView nUIDocView) throws UnsupportedOperationException;

    void launchUrlHandler(String str) throws UnsupportedOperationException;

    void onActivityResult(int i, int i2, Intent intent);

    void openInHandler(String str, ArDkDoc arDkDoc) throws UnsupportedOperationException;

    void openPdfInHandler(String str, ArDkDoc arDkDoc) throws UnsupportedOperationException;

    void pauseHandler(ArDkDoc arDkDoc, boolean z, Runnable runnable);

    void postSaveHandler(SOSaveAsComplete sOSaveAsComplete);

    void printHandler(SODocSession sODocSession) throws UnsupportedOperationException;

    void saveAsHandler(String str, ArDkDoc arDkDoc, SOSaveAsComplete sOSaveAsComplete) throws UnsupportedOperationException;

    void saveAsPdfHandler(String str, ArDkDoc arDkDoc) throws UnsupportedOperationException;

    void saveAsSecureHandler(String str, ArDkDoc arDkDoc, String str2, String str3, SOSaveAsComplete sOSaveAsComplete) throws UnsupportedOperationException;

    void shareHandler(String str, ArDkDoc arDkDoc) throws UnsupportedOperationException;
}
