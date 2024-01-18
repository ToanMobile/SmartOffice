package com.artifex.sonui.editor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import com.artifex.solib.ArDkBitmap;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ArDkPage;
import com.artifex.solib.ArDkRender;
import com.artifex.solib.ConfigOptions;
import com.artifex.solib.FileUtils;
import com.artifex.solib.MuPDFBitmap;
import com.artifex.solib.SOBitmap;
import com.artifex.solib.SODocLoadListener;
import com.artifex.solib.SOOutputStream;
import com.artifex.solib.SOPageListener;
import com.artifex.solib.SORenderListener;
import com.artifex.sonui.editor.Utilities;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class SODocSession {
    public final Activity mActivity;
    public boolean mCanCreateThumbnail = false;
    public boolean mCancelled = false;
    public boolean mCompleted = false;
    public ArDkDoc mDoc;
    public SOFileState mFileState = null;
    public boolean mIsPasswordProtected = false;
    public final ArDkLib mLibrary;
    public SODocSessionLoadListenerCustom mListenerCustom = null;
    public CopyOnWriteArrayList<SODocSessionLoadListener> mListeners = new CopyOnWriteArrayList<>();
    public boolean mLoadError = false;
    public boolean mOpen = false;
    public ArDkPage mPage = null;
    public int mPageCount = 0;
    public Runnable mPasswordHandler = null;
    public ArDkRender mRender = null;
    public String mUserPath = null;

    public interface SODocSessionLoadListener {
        void onCancel();

        void onDocComplete();

        void onError(int i, int i2);

        void onLayoutCompleted();

        void onPageLoad(int i);

        void onSelectionChanged(int i, int i2);
    }

    public interface SODocSessionLoadListenerCustom extends SODocSessionLoadListener {
        void onSessionComplete(boolean z);

        void onSessionReject();
    }

    public SODocSession(Activity activity, ArDkLib arDkLib) {
        this.mActivity = activity;
        this.mLibrary = arDkLib;
    }

    public void abort() {
        Utilities.dismissCurrentAlert();
        this.mOpen = false;
        this.mListeners.clear();
        this.mListenerCustom = null;
        ArDkDoc arDkDoc = this.mDoc;
        if (arDkDoc != null) {
            arDkDoc.abortLoad();
        }
        ArDkRender arDkRender = this.mRender;
        if (arDkRender != null) {
            arDkRender.destroy();
        }
        this.mRender = null;
        ArDkPage arDkPage = this.mPage;
        if (arDkPage != null) {
            arDkPage.releasePage();
        }
        this.mPage = null;
    }

    public void addLoadListener(SODocSessionLoadListener sODocSessionLoadListener) {
        if (sODocSessionLoadListener != null) {
            this.mListeners.add(sODocSessionLoadListener);
            int i = this.mPageCount;
            if (i > 0) {
                sODocSessionLoadListener.onPageLoad(i);
            }
            if (this.mCompleted) {
                sODocSessionLoadListener.onDocComplete();
            }
        }
        SODocSessionLoadListenerCustom sessionLoadListener = Utilities.getSessionLoadListener();
        this.mListenerCustom = sessionLoadListener;
        if (sessionLoadListener != null) {
            int i2 = this.mPageCount;
            if (i2 > 0) {
                sessionLoadListener.onPageLoad(i2);
            }
            if (this.mCompleted) {
                this.mListenerCustom.onDocComplete();
            }
        }
    }

    public boolean canCreateThumbnail() {
        return this.mCanCreateThumbnail;
    }

    public void createThumbnail() {
        SOFileState sOFileState = this.mFileState;
        if (sOFileState != null) {
            createThumbnail(sOFileState);
        }
    }

    public void destroy() {
        abort();
        ArDkDoc arDkDoc = this.mDoc;
        if (arDkDoc != null) {
            arDkDoc.destroyDoc();
            this.mDoc = null;
        }
    }

    public void endSession(boolean z) {
        SODocSessionLoadListenerCustom sODocSessionLoadListenerCustom = this.mListenerCustom;
        if (sODocSessionLoadListenerCustom != null) {
            sODocSessionLoadListenerCustom.onSessionComplete(z);
            this.mListenerCustom = null;
        }
        abort();
    }

    public ArDkDoc getDoc() {
        return this.mDoc;
    }

    public SOFileState getFileState() {
        return this.mFileState;
    }

    public String getUserPath() {
        return this.mUserPath;
    }

    public boolean hasLoadError() {
        return this.mLoadError;
    }

    public boolean isCancelled() {
        return this.mCancelled;
    }

    public boolean isOpen() {
        return this.mOpen;
    }

    public boolean isPasswordProtected() {
        return this.mIsPasswordProtected;
    }

    public void open(String str, ConfigOptions configOptions) {
        this.mUserPath = str;
        this.mPageCount = 0;
        this.mCompleted = false;
        this.mOpen = true;
        this.mDoc = this.mLibrary.openDocument(str, new SODocLoadListener() {
            public void onDocComplete() {
                SODocSession sODocSession = SODocSession.this;
                boolean z = sODocSession.mOpen;
                if (z && !sODocSession.mCancelled && !sODocSession.mLoadError) {
                    sODocSession.mCompleted = true;
                    if (z) {
                        for (SODocSessionLoadListener mListener : sODocSession.mListeners) {
                            mListener.onDocComplete();
                        }
                        SODocSessionLoadListenerCustom sODocSessionLoadListenerCustom = SODocSession.this.mListenerCustom;
                        if (sODocSessionLoadListenerCustom != null) {
                            sODocSessionLoadListenerCustom.onDocComplete();
                        }
                    }
                }
            }

            public void onError(int i, int i2) {
                if (i == 4096) {
                    SODocSession sODocSession = SODocSession.this;
                    sODocSession.mIsPasswordProtected = true;
                    Runnable runnable = sODocSession.mPasswordHandler;
                    if (runnable != null) {
                        runnable.run();
                        return;
                    }
                    BaseActivity currentActivity = BaseActivity.getCurrentActivity();
                    if (currentActivity != null) {
                        Utilities.passwordDialog(currentActivity, new Utilities.passwordDialogListener() {
                            public void onCancel() {
                                SODocSession.this.mDoc.abortLoad();
                                SODocSession sODocSession = SODocSession.this;
                                if (sODocSession.mOpen) {
                                    for (SODocSessionLoadListener mListener : sODocSession.mListeners) {
                                        mListener.onCancel();
                                    }
                                    SODocSessionLoadListenerCustom sODocSessionLoadListenerCustom = SODocSession.this.mListenerCustom;
                                    if (sODocSessionLoadListenerCustom != null) {
                                        sODocSessionLoadListenerCustom.onCancel();
                                    }
                                }
                                SODocSession.this.mCancelled = true;
                            }

                            public void onOK(String str) {
                                SODocSession.this.mDoc.providePassword(str);
                            }
                        });
                        return;
                    }
                }
                SODocSession sODocSession2 = SODocSession.this;
                sODocSession2.mLoadError = true;
                if (sODocSession2.mOpen) {
                    for (SODocSessionLoadListener mListener : sODocSession2.mListeners) {
                        mListener.onError(i, i2);
                    }
                    SODocSessionLoadListenerCustom sODocSessionLoadListenerCustom = SODocSession.this.mListenerCustom;
                    if (sODocSessionLoadListenerCustom != null) {
                        sODocSessionLoadListenerCustom.onError(i, i2);
                    }
                }
                SODocSession.this.mOpen = false;
            }

            public void onPageLoad(int i) {
                SODocSession sODocSession = SODocSession.this;
                if (sODocSession.mOpen && !sODocSession.mCancelled) {
                    sODocSession.mPageCount = Math.max(i, sODocSession.mPageCount);
                    SODocSession sODocSession2 = SODocSession.this;
                    for (SODocSessionLoadListener mListener : sODocSession2.mListeners) {
                        mListener.onPageLoad(i);
                    }
                    SODocSessionLoadListenerCustom sODocSessionLoadListenerCustom = SODocSession.this.mListenerCustom;
                    if (sODocSessionLoadListenerCustom != null) {
                        sODocSessionLoadListenerCustom.onPageLoad(i);
                    }
                }
            }

            @Override
            public void error(int i2, int i3) {

            }

            @Override
            public void onLayoutCompleted() {

            }

            public void onSelectionChanged(int i, int i2) {
                SODocSession sODocSession = SODocSession.this;
                boolean z = sODocSession.mOpen;
                if (z) {
                    for (SODocSessionLoadListener mListener : sODocSession.mListeners) {
                        mListener.onSelectionChanged(i, i2);
                    }
                    SODocSessionLoadListenerCustom sODocSessionLoadListenerCustom = SODocSession.this.mListenerCustom;
                    if (sODocSessionLoadListenerCustom != null) {
                        sODocSessionLoadListenerCustom.onSelectionChanged(i, i2);
                    }
                }
            }

            @Override
            public void progress(int i2, boolean z) {

            }

            @Override
            public void setDoc(ArDkDoc arDkDoc) {

            }
        }, this.mActivity, configOptions);
    }

    public void removeLoadListener(SODocSessionLoadListener sODocSessionLoadListener) {
        this.mListeners.remove(sODocSessionLoadListener);
    }

    public void setCanCreateThumbnail(boolean z) {
        this.mCanCreateThumbnail = z;
    }

    public void setFileState(SOFileState sOFileState) {
        this.mFileState = sOFileState;
    }

    public void setPasswordHandler(Runnable runnable) {
        this.mPasswordHandler = runnable;
    }

    public void createThumbnail(SOFileState sOFileState) {
        ArDkBitmap arDkBitmap;
        String thumbnail = SOFileDatabase.uniqueThumbFilePath();
        sOFileState.setThumbnail(thumbnail);
        sOFileState.deleteThumbnailFile();
        ArDkPage page = getDoc().getPage(0, rectF -> {
        });
        this.mPage = page;
        if (page != null) {
            PointF zoomToFitRect = this.mPage.zoomToFitRect((int) this.mActivity.getResources().getDimension(R.dimen.sodk_editor_thumbnail_size), 1);
            double max = Math.max(zoomToFitRect.x, zoomToFitRect.y);
            Point sizeAtZoom = this.mPage.sizeAtZoom(max);
            String internalPath = sOFileState.getInternalPath();
            int i = sizeAtZoom.x;
            int i2 = sizeAtZoom.y;
            if (FileUtils.isDocSupportedByMupdf(internalPath)) {
                arDkBitmap = new MuPDFBitmap(i, i2);
            } else {
                arDkBitmap = new SOBitmap(i, i2);
            }
            final ArDkBitmap arDkBitmap2 = arDkBitmap;
            PointF pointF = new PointF(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
            ArDkPage arDkPage = this.mPage;
            SORenderListener soRenderListener = i1 -> {
                Bitmap bitmap = arDkBitmap2.getBitmap();
                SOOutputStream sOOutputStream = new SOOutputStream(thumbnail);
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, sOOutputStream);
                sOOutputStream.close();
                SODocSession sODocSession = SODocSession.this;
                ArDkRender arDkRender = sODocSession.mRender;
                if (arDkRender != null) {
                    arDkRender.destroy();
                }
                sODocSession.mRender = null;
                ArDkPage arDkPage1 = sODocSession.mPage;
                if (arDkPage1 != null) {
                    arDkPage1.releasePage();
                }
                sODocSession.mPage = null;
            };
            Objects.requireNonNull(arDkPage);
            this.mRender = arDkPage.renderLayerAtZoomWithAlpha(-2, max, (double) pointF.x, (double) pointF.y, arDkBitmap2, (ArDkBitmap) null, soRenderListener, true, false);
        }
    }
}
