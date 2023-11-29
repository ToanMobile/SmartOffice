package com.artifex.solib;

import a.a.a.a.b.f.a$$ExternalSyntheticOutline0;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline1;
import com.artifex.sonui.editor.SODocSession;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

public class SOLib extends ArDkLib {
    public static SOLib singleton;
    private long internal;
    public Thread stderrThread;
    public Thread stdoutThread;

    static {
        Log.w("ArDkLib", "loading shared library");
        System.loadLibrary("so");
    }

    private SOLib(Activity activity) {
        try {
            SOSecureFS sOSecureFS = ArDkLib.mSecureFS;
            if (sOSecureFS != null) {
                initSecureFS(sOSecureFS);
                if (ArDkLib.getClipboardHandler() != null) {
                    preInitLib();
                    AnonymousClass2 r0 = new Thread() {
                        public void run() {
                            SOLib.this.logStdout();
                        }
                    };
                    this.stdoutThread = r0;
                    r0.start();
                    AnonymousClass3 r02 = new Thread() {
                        public void run() {
                            SOLib.this.logStderr();
                        }
                    };
                    this.stderrThread = r02;
                    r02.start();
                    String language = Locale.getDefault().getLanguage();
                    String country = Locale.getDefault().getCountry();
                    if (country != null && !country.isEmpty()) {
                        language = AbstractResolvableFuture$$ExternalSyntheticOutline1.m(language, "-", country);
                    }
                    long initLib = initLib(language);
                    this.internal = initLib;
                    if (initLib != 0) {
                        String m = a$$ExternalSyntheticOutline0.m(FileUtils.getTempPathRoot(activity), "/tmp/");
                        if (FileUtils.fileExists(m) && m != null) {
                            SOSecureFS sOSecureFS2 = FileUtils.mSecureFs;
                            if (sOSecureFS2 == null || !sOSecureFS2.isSecurePath(m)) {
                                FileUtils.deleteRecursive(m);
                            } else {
                                FileUtils.mSecureFs.recursivelyRemoveDirectory(m);
                            }
                        }
                        FileUtils.createDirectory(m);
                        if (setTempPath(m) != 0) {
                            throw new IllegalArgumentException("SOLib error in setTempPath");
                        } else if (installFonts("/system/fonts/") != 0) {
                            throw new IllegalArgumentException("SOLib error in installFonts");
                        }
                    } else {
                        throw new NullPointerException("SOLib initialisation failed");
                    }
                } else {
                    Log.d("ArDkLib", "No implementation of the SOClipboardHandler interface found");
                    throw new RuntimeException();
                }
            } else {
                throw new ClassNotFoundException();
            }
        } catch (ExceptionInInitializerError unused) {
            Log.e("ArDkLib", String.format("SOLib() experienced unexpected exception [%s]", new Object[]{"ExceptionInInitializerError"}));
        } catch (LinkageError unused2) {
            Log.e("ArDkLib", String.format("SOLib() experienced unexpected exception [%s]", new Object[]{"LinkageError"}));
        } catch (SecurityException unused3) {
            Log.e("ArDkLib", String.format("SOLib() experienced unexpected exception [%s]", new Object[]{"SecurityException"}));
        } catch (ClassNotFoundException unused4) {
            Log.i("ArDkLib", "SecureFS implementation unavailable");
        }
    }

    private native void finLib();

    public static synchronized SOLib getLib(Activity activity) {
        SOLib sOLib;
        synchronized (SOLib.class) {
            if (singleton == null) {
                Log.w("ArDkLib", "creating new SOLib");
                singleton = new SOLib(activity);
            }
            sOLib = singleton;
        }
        return sOLib;
    }

    private native long initLib(String str);

    /* access modifiers changed from: private */
    public native void logStderr();

    /* access modifiers changed from: private */
    public native void logStdout();

    private native void preInitLib();

    private native int setTempPath(String str);

    private native void stopLoggingOutputInternal();

    public ArDkBitmap createBitmap(int i, int i2) {
        return new SOBitmap(i, i2);
    }

    public native void finSecureFS();

    public void finalize() throws Throwable {
        try {
            finSecureFS();
            finLib();
            stopLoggingOutputInternal();
            try {
                Thread thread = this.stdoutThread;
                if (thread != null) {
                    thread.join();
                }
                Thread thread2 = this.stderrThread;
                if (thread2 != null) {
                    thread2.join();
                }
            } catch (InterruptedException unused) {
            }
        } finally {
            super.finalize();
        }
    }

    public native int getDocTypeFromFileContents(String str);

    public native int getDocTypeFromFileExtension(String str);

    public native String[] getFormulae(String str);

    public native String[] getFormulaeCategories();

    public native String[] getVersionInfo();

    public native void initSecureFS(SOSecureFS sOSecureFS);

    public native int installFonts(String str);

    public native boolean isAnimationEnabled();

    public native boolean isDocTypeDoc(String str);

    public native boolean isDocTypeExcel(String str);

    public native boolean isDocTypeImage(String str);

    public native boolean isDocTypeOther(String str);

    public native boolean isDocTypePdf(String str);

    public native boolean isDocTypePowerPoint(String str);

    public native boolean isTrackChangesEnabled();

    public ArDkDoc openDocument(String str, final SODocLoadListener sODocLoadListener, Context context, ConfigOptions configOptions) {
        if (configOptions != null) {
            setAnimationEnabled(configOptions.mSettingsBundle.getBoolean("AnimationFeatureEnabledKey", false));
            setTrackChangesEnabled(configOptions.isTrackChangesFeatureEnabled());
        }
        AnonymousClass1 r5 = new SODocLoadListenerInternal() {
            public ArDkDoc mDoc = null;
            public int mLastSelectionID = 0;
            public Random mRandom = new Random();

            public static void access$200(AnonymousClass1 r1, int i, int i2) {
                r1.mLastSelectionID = r1.mRandom.nextInt();
                ArDkDoc arDkDoc = r1.mDoc;
                arDkDoc.mSelectionStartPage = i;
                arDkDoc.mSelectionEndPage = i2;
                ((SODocSession.AnonymousClass1) sODocLoadListener).onSelectionChanged(i, i2);
            }

            public void error(final int i, final int i2) {
                ArDkLib.runOnUiThread(new Runnable() {
                    public void run() {
                        ((SODocSession.AnonymousClass1) sODocLoadListener).onError(i, i2);
                    }
                });
            }

            public void onLayoutCompleted() {
                ArDkLib.runOnUiThread(new Runnable() {
                    public void run() {
                        SODocSession.AnonymousClass1 r0 = (SODocSession.AnonymousClass1) sODocLoadListener;
                        SODocSession sODocSession = SODocSession.this;
                        boolean z = sODocSession.mOpen;
                        if (z && z) {
                            Iterator<SODocSession.SODocSessionLoadListener> it = sODocSession.mListeners.iterator();
                            while (it.hasNext()) {
                                it.next().onLayoutCompleted();
                            }
                            SODocSession.SODocSessionLoadListenerCustom sODocSessionLoadListenerCustom = SODocSession.this.mListenerCustom;
                            if (sODocSessionLoadListenerCustom != null) {
                                sODocSessionLoadListenerCustom.onLayoutCompleted();
                            }
                        }
                    }
                });
            }

            public void onSelectionChanged(final int i, final int i2) {
                ArDkLib.runOnUiThread(new Runnable() {
                    public void run() {
                        int i = i;
                        int i2 = i2;
                        if (i > i2) {
                            final int i3 = AnonymousClass1.this.mLastSelectionID;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    int i = i3;
                                    AnonymousClass3 r1 = AnonymousClass3.this;
                                    AnonymousClass1 r2 = AnonymousClass1.this;
                                    if (i == r2.mLastSelectionID) {
                                        AnonymousClass1.access$200(r2, i, i2);
                                    }
                                }
                            }, 100);
                            return;
                        }
                        AnonymousClass1.access$200(AnonymousClass1.this, i, i2);
                    }
                });
            }

            public void progress(final int i, final boolean z) {
                ArDkLib.runOnUiThread(new Runnable() {
                    public void run() {
                        AnonymousClass1 r0 = AnonymousClass1.this;
                        ArDkDoc arDkDoc = r0.mDoc;
                        if (arDkDoc != null) {
                            arDkDoc.mNumPages = i;
                        }
                        ((SODocSession.AnonymousClass1) sODocLoadListener).onPageLoad(i);
                        if (z) {
                            ((SODocSession.AnonymousClass1) sODocLoadListener).onDocComplete();
                        }
                    }
                });
            }

            public void setDoc(ArDkDoc arDkDoc) {
                this.mDoc = arDkDoc;
            }
        };
        ArDkDoc openDocumentInternal = openDocumentInternal(str, r5);
        r5.mDoc = openDocumentInternal;
        return openDocumentInternal;
    }

    public native ArDkDoc openDocumentInternal(String str, SODocLoadListenerInternal sODocLoadListenerInternal);

    public void reclaimMemory() {
        Runtime.getRuntime().gc();
    }

    public native void setAnimationEnabled(boolean z);

    public native void setTrackChangesEnabled(boolean z);
}
