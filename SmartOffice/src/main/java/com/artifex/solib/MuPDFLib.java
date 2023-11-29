package com.artifex.solib;

import a.a.a.a.a.c$$ExternalSyntheticOutline0;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.artifex.mupdf.fitz.Document;
import com.artifex.solib.Worker;
import com.artifex.sonui.editor.SODocSession;
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

    public void finalize() throws Throwable {
        super.finalize();
    }

    public ArDkDoc openDocument(final String str, final SODocLoadListener sODocLoadListener, Context context, ConfigOptions configOptions) {
        final MuPDFDoc muPDFDoc = new MuPDFDoc(Looper.myLooper(), sODocLoadListener, context, configOptions);
        Worker worker = muPDFDoc.mWorker;
        Objects.requireNonNull(worker);
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    Worker worker = Worker.this;
                    if (worker.alive) {
                        try {
                            Task take = worker.mQueue.take();
                            take.work();
                            new Handler(Worker.this.mLooper).post(take);
                        } catch (InterruptedException unused) {
                        } catch (Throwable th) {
                            StringBuilder m = c$$ExternalSyntheticOutline0.m("exception in Worker thread: ");
                            m.append(th.toString());
                            Log.e("Worker", m.toString());
                        }
                    } else {
                        return;
                    }
                }
            }
        });
        worker.mThread = thread;
        thread.start();
        worker.alive = true;
        muPDFDoc.mWorker.add(new Worker.Task() {
            public boolean docOpened = false;
            public boolean needsPassword = false;

            public void run() {
                if (!this.docOpened) {
                    SODocLoadListener sODocLoadListener = sODocLoadListener;
                    if (sODocLoadListener != null) {
                        ((SODocSession.AnonymousClass1) sODocLoadListener).onError(4, 0);
                    }
                } else if (this.needsPassword) {
                    SODocLoadListener sODocLoadListener2 = sODocLoadListener;
                    if (sODocLoadListener2 != null) {
                        ((SODocSession.AnonymousClass1) sODocLoadListener2).onError(4096, 0);
                    }
                } else {
                    muPDFDoc.loadNextPage();
                }
            }

            public void work() {
                Document openFile = MuPDFDoc.openFile(str);
                if (openFile != null) {
                    this.docOpened = true;
                    MuPDFDoc muPDFDoc = muPDFDoc;
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
