package com.artifex.solib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.artifex.mupdf.fitz.Document;
import java.util.Objects;

public class MuPDFLib extends ArDkLib {
    public static MuPDFLib singleton;

    public static MuPDFLib getLib() {
        if (singleton == null) {
            Log.w("ArDkLib", "creating new SOLib");
            singleton = new MuPDFLib();
            if (ArDkLib.getClipboardHandler() == null) {
                Log.d("ArDkLib", "No implementation of the SOClipboardHandler interface found");
                throw new RuntimeException();
            }
        }
        return singleton;
    }

    public ArDkBitmap createBitmap(int i, int i2) {
        return new MuPDFBitmap(i, i2);
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public ArDkDoc openDocument(final String str, final SODocLoadListener sODocLoadListener, Context context, ConfigOptions configOptions) {
        final MuPDFDoc muPDFDoc = new MuPDFDoc(Looper.myLooper(), sODocLoadListener, context, configOptions);
        Worker worker = muPDFDoc.mWorker;
        Objects.requireNonNull(worker);
        Thread thread = new Thread(() -> {
            while (true) {
                if (worker.alive) {
                    try {
                        Worker.Task take = worker.mQueue.take();
                        take.work();
                        new Handler(worker.mLooper).post(take);
                    } catch (InterruptedException unused) {
                    } catch (Throwable th) {
                        Log.e("Worker", "exception in Worker thread: " + th);
                    }
                } else {
                    return;
                }
            }
        });
        worker.mThread = thread;
        thread.start();
        worker.alive = true;
        muPDFDoc.mWorker.add(new Worker.Task() {
            boolean docOpened = false;
            boolean needsPassword = false;

            public void run() {
                if (!this.docOpened) {
                    if (sODocLoadListener != null) {
                        sODocLoadListener.onError(4, 0);
                    }
                } else if (this.needsPassword) {
                    if (sODocLoadListener != null) {
                        sODocLoadListener.onError(4096, 0);
                    }
                } else {
                    muPDFDoc.loadNextPage();
                }
            }

            public void work() {
                Document openFile = muPDFDoc.openFile(str);
                if (openFile != null) {
                    this.docOpened = true;
                    muPDFDoc.mDocument = openFile;
                    muPDFDoc.mOpenedPath = str;
                    if (openFile.needsPassword()) {
                        this.needsPassword = true;
                    } else {
                        muPDFDoc.afterValidation();
                    }
                }
            }
        });
        return muPDFDoc;
    }

    public void reclaimMemory() {
        com.artifex.mupdf.fitz.Context.emptyStore();
        Runtime.getRuntime().gc();
    }
}
