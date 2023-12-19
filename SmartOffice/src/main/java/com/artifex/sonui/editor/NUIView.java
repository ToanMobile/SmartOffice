package com.artifex.sonui.editor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import com.artifex.solib.ConfigOptions;
import com.artifex.solib.FileUtils;
import java.util.Objects;

public class NUIView extends FrameLayout {
    public ConfigOptions mDocCfgOpts;
    public NUIDocView mDocView;
    public DocStateListener mDoneListener = null;
    public SODataLeakHandlers mLeakHandlers;
    public boolean mStarted = false;

    public interface DocStateListener {

        /* renamed from: com.artifex.sonui.editor.NUIView$DocStateListener$-CC  reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$docLoaded(DocStateListener docStateListener) {
            }
        }

        void docLoaded();

        void done();
    }

    public NUIView(Context context) {
        super(context);
    }

    public boolean doKeyDown(int i, KeyEvent keyEvent) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.doKeyDown(i, keyEvent);
        }
        return false;
    }

    public void endDocSession(boolean z) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.endDocSession(z);
        }
    }

    public boolean hasHistory() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.hasHistory();
        }
        return false;
    }

    public boolean hasIndent() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.hasIndent();
        }
        return false;
    }

    public boolean hasPages() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.usePagesView();
        }
        return false;
    }

    public boolean hasReflow() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.hasReflow();
        }
        return false;
    }

    public boolean hasSearch() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.hasSearch();
        }
        return false;
    }

    public boolean hasSelectionAlignment() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.hasSelectionAlignment();
        }
        return false;
    }

    public boolean isDocModified() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.documentHasBeenModified();
        }
        return false;
    }

    public void makeNUIView(String str) {
        String extension = FileUtils.getExtension(str);
        Objects.requireNonNull(extension);
        char c = 65535;
        switch (extension.hashCode()) {
            case 97669:
                if (extension.equals("bmp")) {
                    c = 0;
                    break;
                }
                break;
            case 98299:
                if (extension.equals("cbz")) {
                    c = 1;
                    break;
                }
                break;
            case 101110:
                if (extension.equals("fb2")) {
                    c = 2;
                    break;
                }
                break;
            case 105441:
                if (extension.equals("jpg")) {
                    c = 3;
                    break;
                }
                break;
            case 111145:
                if (extension.equals("png")) {
                    c = 4;
                    break;
                }
                break;
            case 114276:
                if (extension.equals("svg")) {
                    c = 5;
                    break;
                }
                break;
            case 118907:
                if (extension.equals("xps")) {
                    c = 6;
                    break;
                }
                break;
            case 3120248:
                if (extension.equals("epub")) {
                    c = 7;
                    break;
                }
                break;
            case 3268712:
                if (extension.equals("jpeg")) {
                    c = 8;
                    break;
                }
                break;
            case 114035747:
                if (extension.equals("xhtml")) {
                    c = 9;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                this.mDocView = new NUIDocViewMuPdf(getContext());
                break;
        }
        if (this.mDocView == null && extension.equals("pdf")) {
            this.mDocView = new NUIDocViewPdf(getContext());
        }
        if (this.mDocView == null) {
            this.mDocView = NUIViewFactory.makeNUIView(str, getContext());
        }
        if (this.mDocView == null) {
            this.mDocView = new NUIDocViewOther(getContext());
        }
        this.mDocView.setDocSpecifics(this.mDocCfgOpts, this.mLeakHandlers);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.onActivityResult(i, i2, intent);
        }
    }

    public void onBackPressed(Runnable runnable) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.onBackPressed(runnable);
        }
    }

    public void onConfigurationChange(Configuration configuration) {
        this.mDocView.onConfigurationChange(configuration);
    }

    public void onDestroy() {
        this.mDocView.onDestroy();
    }

    public void onPause(final Runnable runnable) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.onPause(() -> {
                Runnable runnableNew;
                runnableNew = runnable;
                if (runnableNew != null) {
                    runnableNew.run();
                }
            });
        } else {
            runnable.run();
        }
        Utilities.hideKeyboard(getContext());
    }

    public void onResume() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.onResume();
        }
    }

    public void releaseBitmaps() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.releaseBitmaps();
        }
    }

    public void setConfigurableButtons() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setConfigurableButtons();
        }
    }

    public void setDocConfigOptions(ConfigOptions configOptions) {
        this.mDocCfgOpts = configOptions;
    }

    public void setDocDataLeakHandler(SODataLeakHandlers sODataLeakHandlers) {
        this.mLeakHandlers = sODataLeakHandlers;
    }

    public void setDocStateListener(DocStateListener docStateListener) {
        this.mDoneListener = docStateListener;
    }

    public void start(SODocSession sODocSession, ViewingState viewingState, String str) {
        if (!this.mStarted) {
            this.mStarted = true;
            makeNUIView(sODocSession.getUserPath());
            addView(this.mDocView);
            this.mDocView.start(sODocSession, viewingState, str, this.mDoneListener);
            return;
        }
        throw new RuntimeException("start() can only be called once for each NUIView instance.");
    }

    public NUIView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NUIView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void start(SOFileState sOFileState, ViewingState viewingState) {
        if (!this.mStarted) {
            this.mStarted = true;
            makeNUIView(sOFileState.getOpenedPath());
            addView(this.mDocView);
            this.mDocView.start(sOFileState, viewingState, this.mDoneListener);
            return;
        }
        throw new RuntimeException("start() can only be called once for each NUIView instance.");
    }

    public void makeNUIView(Uri uri, String str) {
        String fileTypeExtension = FileUtils.getFileTypeExtension(getContext(), uri, str);
        makeNUIView("SomeFileName." + fileTypeExtension);
    }

    public void start(Uri uri, boolean z, ViewingState viewingState, String str, String str2) {
        if (!this.mStarted) {
            this.mStarted = true;
            makeNUIView(uri, str2);
            addView(this.mDocView);
            this.mDocView.start(uri, z, viewingState, str, this.mDoneListener, true);
            return;
        }
        throw new RuntimeException("start() can only be called once for each NUIView instance.");
    }
}
