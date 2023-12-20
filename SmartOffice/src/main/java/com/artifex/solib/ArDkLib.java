package com.artifex.solib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.artifex.mupdf.fitz.OutlineIterator;
import com.artifex.solib.MuPDFDoc;
import com.artifex.solib.Worker;
import java.util.Objects;

public abstract class ArDkLib {
    public static ConfigOptions mAppConfigOptions;
    public static SOClipboardHandler mClipboardHandler;
    public static SOSecureFS mSecureFS;

    public interface EnumeratePdfTocListener {
        void done();

        void nextTocEntry(int i, int i2, int i3, String str, String str2, float f, float f2);
    }

    public static boolean clipboardHasText() {
        if (getClipboardHandler() != null) {
            return getClipboardHandler().clipboardHasPlaintext();
        }
        Log.d("ArDkLib", "No implementation of the SOClipboardHandler interface found");
        throw new RuntimeException();
    }

    public static void enumeratePdfToc(ArDkDoc arDkDoc, final EnumeratePdfTocListener enumeratePdfTocListener) {
        MuPDFDoc muPDFDoc = (MuPDFDoc) arDkDoc;
        MuPDFDoc.MuPDFEnumerateTocListener r0 = new MuPDFDoc.MuPDFEnumerateTocListener() {
        };
        Objects.requireNonNull(muPDFDoc);
        //TODO TOAN
        /*muPDFDoc.mWorker.add(new Worker.Task(r0) {
            public final *//* synthetic *//* MuPDFEnumerateTocListener val$listener;

            {
                this.val$listener = r2;
            }

            public void run() {
                enumeratePdfTocListener.done();
            }

            public void work() {
                try {
                    MuPDFDoc muPDFDoc = MuPDFDoc.this;
                    muPDFDoc.handleCounter = 0;
                    OutlineIterator outlineIterator = muPDFDoc.mDocument.outlineIterator();
                    if (outlineIterator != null) {
                        MuPDFDoc.this.processOutline(outlineIterator, false, 0, this.val$listener);
                    }
                } catch (Exception unused) {
                }
            }
        });*/
    }

    public static ConfigOptions getAppConfigOptions() {
        ConfigOptions configOptions = mAppConfigOptions;
        if (configOptions != null) {
            return configOptions;
        }
        throw new RuntimeException("No registered ConfigOptions found.");
    }

    public static SOClipboardHandler getClipboardHandler() {
        if (mClipboardHandler == null) {
            mClipboardHandler = new DefaultClipboardHandler();
        }
        return mClipboardHandler;
    }

    public static String getClipboardText() {
        if (getClipboardHandler() != null) {
            return getClipboardHandler().getPlainTextFromClipoard();
        }
        Log.d("ArDkLib", "No implementation of the SOClipboardHandler interface found");
        throw new RuntimeException();
    }

    public static boolean isNightMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & 48) == 32;
    }

    public static void putTextToClipboard(String str) {
        if (str != null && !str.isEmpty()) {
            if (getClipboardHandler() != null) {
                getClipboardHandler().putPlainTextToClipboard(str);
            } else {
                Log.d("ArDkLib", "No implementation of the SOClipboardHandler interface found");
                throw new RuntimeException();
            }
        }
    }

    public static final void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public abstract ArDkBitmap createBitmap(int i, int i2);

    public abstract ArDkDoc openDocument(String str, SODocLoadListener sODocLoadListener, Context context, ConfigOptions configOptions);

    public abstract void reclaimMemory();
}
