package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.MuPDFLib;
import com.artifex.solib.SODocSaveListener;
import com.artifex.sonui.editor.NUIDocView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class DocumentView extends NUIView {
    public ChangePageListener mChangePageListener = null;
    public DocumentListener mDocumentListener = null;

    public interface ChangePageListener {
        void onPage(int i);
    }

    public interface EnumeratePdfTocListener {
        void done();

        void nextTocEntry(int i, int i2, int i3, String str, String str2, float f, float f2);
    }

    public class MyLifecycleObserver implements LifecycleObserver {
        public DocumentView mDocumentView;

        public MyLifecycleObserver(DocumentView documentView, DocumentView documentView2) {
            this.mDocumentView = documentView2;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy() {
            this.mDocumentView.onDestroy();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause() {
            this.mDocumentView.onPause((Runnable) null);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume() {
            this.mDocumentView.onResume();
        }
    }

    public interface OnSaveListener {
        void done(boolean z);
    }

    public DocumentView(Context context) {
        super(context);
    }

    private LifecycleOwner getLifecycleOwner() {
        if (!isAttachedToWindow()) {
            return null;
        }
        for (Context context = getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof LifecycleOwner) {
                return (LifecycleOwner) context;
            }
        }
        return null;
    }

    public static void initialize(Context context) {
        NUIDocView.registerGetContentLauncher(context);
    }

    public static boolean unlockAppKit(Context context, String str) {
        Activity activity = (Activity) context;
        MuPDFLib.getLib();
        return false;
    }

    public void author() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.author();
        }
    }

    public boolean canApplyRedactions() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.canApplyRedactions().booleanValue();
        }
        return false;
    }

    public boolean canDeleteSelection() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.canDeleteSelection();
        }
        return false;
    }

    public boolean canMarkTextRedaction() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.canMarkRedaction().booleanValue();
        }
        return false;
    }

    public boolean canRedo() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.canRedo();
        }
        return false;
    }

    public boolean canRemoveRedaction() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.canRemoveRedaction().booleanValue();
        }
        return false;
    }

    public boolean canSelect() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.canSelect();
        }
        return false;
    }

    public boolean canUndo() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.canUndo();
        }
        return false;
    }

    public void clearSelection() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.clearSelection();
        }
    }

    public void deleteSelectedText() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.deleteSelectedText();
        }
    }

    public void deleteSelection() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.deleteSelection();
        }
    }

    public void enterFullScreen(Runnable runnable) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.enterFullScreen(runnable);
        }
    }

    public void enumeratePdfToc(final EnumeratePdfTocListener enumeratePdfTocListener) {
        ArDkLib.enumeratePdfToc(this.mDocView.getDoc(), new ArDkLib.EnumeratePdfTocListener(this) {
            public void done() {
                enumeratePdfTocListener.done();
            }

            public void nextTocEntry(int i, int i2, int i3, String str, String str2, float f, float f2) {
                enumeratePdfTocListener.nextTocEntry(i, i2, i3, str, str2, f, f2);
            }
        });
    }

    public void exportTo(String str, String str2, SODocSaveListener sODocSaveListener) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.exportTo(str, str2, sODocSaveListener);
        }
    }

    public void findNextSignature() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.findNextSignature();
        }
    }

    public int findPageContainingPoint(Point point) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView == null) {
            return -1;
        }
        return nUIDocView.findPageContainingPoint(point);
    }

    public void findPreviousSignature() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.findPreviousSignature();
        }
    }

    public void firstPage() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.firstPage();
        }
    }

    public String getAuthor() {
        NUIDocView nUIDocView = this.mDocView;
        return nUIDocView != null ? nUIDocView.getAuthor() : "";
    }

    public int getFlowMode() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.getFlowMode();
        }
        return 1;
    }

    public int getLineColor() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.getLineColor();
        }
        return 0;
    }

    public float getLineThickness() {
        NUIDocView nUIDocView = this.mDocView;
        return nUIDocView != null ? nUIDocView.getLineThickness() : BitmapDescriptorFactory.HUE_RED;
    }

    public int getPageCount() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.getPageCount();
        }
        return 0;
    }

    public int getPageNumber() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.getPageNumber();
        }
        return 0;
    }

    public String getPersistedAuthor() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.getPersistedAuthor();
        }
        return null;
    }

    public float getScaleFactor() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.getScaleFactor();
        }
        return -1.0f;
    }

    public int getScrollPositionX() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.getScrollPositionX();
        }
        return -1;
    }

    public int getScrollPositionY() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.getScrollPositionY();
        }
        return -1;
    }

    public String getSelectedText() {
        NUIDocView nUIDocView = this.mDocView;
        return nUIDocView != null ? nUIDocView.getSelectedText() : "";
    }

    public int getSignatureCount() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.getSignatureCount();
        }
        return 0;
    }

    public void goToPage(int i) {
        if (this.mDocView != null) {
            if (i < 0) {
                i = 0;
            }
            if (i > getPageCount() - 1) {
                i = getPageCount() - 1;
            }
            Utilities.hideKeyboard(getContext());
            this.mDocView.goToPage(i, true);
        }
    }

    public void gotoInternalLocation(int i, RectF rectF) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.gotoInternalLocation(i, rectF);
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

    public boolean hasNextHistory() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.hasNextHistory();
        }
        return false;
    }

    public boolean hasPreviousHistory() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.hasPreviousHistory();
        }
        return false;
    }

    public boolean hasRedo() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.hasRedo();
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

    public boolean hasUndo() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.hasUndo();
        }
        return false;
    }

    public void hidePageList() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.hidePages();
        }
    }

    public void highlightSelection() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.highlightSelection();
        }
    }

    public void historyNext() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.historyNext();
        }
    }

    public void historyPrevious() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.historyPrevious();
        }
    }

    public boolean isAlterableTextSelection() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.isAlterableTextSelection();
        }
        return false;
    }

    public boolean isDigitalSignatureMode() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.isDigitalSignatureMode();
        }
        return false;
    }

    public boolean isDocumentModified() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.isDocumentModified();
        }
        return false;
    }

    public boolean isDrawModeOn() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.isDrawModeOn();
        }
        return false;
    }

    public boolean isESignatureMode() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.isESignatureMode();
        }
        return false;
    }

    public boolean isNoteModeOn() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.isNoteModeOn();
        }
        return false;
    }

    public boolean isPageListVisible() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.isPageListVisible();
        }
        return false;
    }

    public boolean isSelectionInkAnnotation() {
        ArDkDoc doc;
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView == null || (doc = nUIDocView.getDoc()) == null) {
            return false;
        }
        return doc.isSelectionInkAnnotation();
    }

    public boolean isTOCEnabled() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.isTOCEnabled();
        }
        return false;
    }

    public void lastPage() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.lastPage();
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        LifecycleOwner lifecycleOwner = getLifecycleOwner();
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver(new MyLifecycleObserver(this, this));
            ((Context) lifecycleOwner).registerComponentCallbacks(new ComponentCallbacks2(this) {
                public void onConfigurationChanged(Configuration configuration) {
                    this.onConfigurationChange(configuration);
                }

                public void onLowMemory() {
                }

                public void onTrimMemory(int i) {
                }
            });
            if (lifecycleOwner instanceof FragmentActivity) {
                ((AppCompatActivity) lifecycleOwner).getOnBackPressedDispatcher().addCallback(lifecycleOwner, new OnBackPressedCallback(this, true) {
                    public void handleOnBackPressed() {
                        this.onBackPressed((Runnable) null);
                    }
                });
            }
        }
    }

    public void openIn() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.openIn();
        }
    }

    public void print() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.print();
        }
    }

    public void providePassword(String str) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.providePassword(str);
        }
    }

    public void redactApply() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.redactApply();
        }
    }

    public boolean redactGetMarkTextMode() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.redactGetMarkTextMode();
        }
        return false;
    }

    public boolean redactIsMarkingArea() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.redactIsMarkingArea();
        }
        return false;
    }

    public void redactMarkArea() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.redactMarkArea();
        }
    }

    public void redactMarkText() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.redactMarkText();
        }
    }

    public void redactRemove() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.redactRemove();
        }
    }

    public void redo() {
        NUIDocView nUIDocView;
        if (canRedo() && (nUIDocView = this.mDocView) != null) {
            nUIDocView.doRedo();
        }
    }

    public void reload(String str) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.reloadFile(str);
        }
    }

    public void save() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.save();
        }
    }

    public void saveAs() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.saveAs();
        }
    }

    public void savePDF() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.savePDF();
        }
    }

    public void saveTo(String str, SODocSaveListener sODocSaveListener) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.saveTo(str, sODocSaveListener);
        }
    }

    public void saveToPDF(String str, SODocSaveListener sODocSaveListener) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.saveToPDF(str, sODocSaveListener);
        }
    }

    public Point screenToPage(int i, Point point) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView == null) {
            return null;
        }
        return nUIDocView.screenToPage(i, point);
    }

    public void searchBackward(String str) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.searchBackward(str);
        }
    }

    public void searchForward(String str) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.searchForward(str);
        }
    }

    public void secureSave() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.secureSave();
        }
    }

    public boolean select(Point point) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.select(point);
        }
        return false;
    }

    public void setAuthor(String str) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setAuthor(str);
        }
    }

    public void setDigitalSignatureModeOff() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setDigitalSignatureModeOff();
        }
    }

    public void setDigitalSignatureModeOn() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setDigitalSignatureModeOn();
        }
    }

    public void setDocumentListener(DocumentListener documentListener) {
        this.mDocumentListener = documentListener;
    }

    public void setDrawModeOff() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setDrawModeOff();
        }
    }

    public void setDrawModeOn() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setDrawModeOn();
        }
    }

    public void setESignatureModeOff() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setESignatureModeOff();
        }
    }

    public void setESignatureModeOn(View view) {
        if (view != null) {
            NUIDocView nUIDocView = this.mDocView;
            if (nUIDocView != null) {
                nUIDocView.setESignatureModeOn(view);
                return;
            }
            return;
        }
        throw new RuntimeException("DocumentView.setESignatureModeOn requires a non-null View.");
    }

    public void setFlowMode(int i) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setFlowMode(i);
        }
    }

    public void setFlowModeNormal() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setFlowMode(1);
        }
    }

    public void setFlowModeReflow() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setFlowMode(2);
        }
    }

    public void setFlowModeResize() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setFlowMode(3);
        }
    }

    public void setLineColor(int i) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setLineColor(i);
        }
    }

    public void setLineThickness(float f) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setLineThickness(f);
        }
    }

    public void setNoteModeOff() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setNoteModeOff();
        }
    }

    public void setNoteModeOn() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setNoteModeOn();
        }
    }

    public void setOnUpdateUI(Runnable runnable) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setOnUpdateUI(runnable);
        }
    }

    public void setPageChangeListener(ChangePageListener changePageListener) {
        this.mChangePageListener = changePageListener;
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setPageChangeListener(new NUIDocView.ChangePageListener() {
                public void onPage(int i) {
                    ChangePageListener changePageListener = DocumentView.this.mChangePageListener;
                    if (changePageListener != null) {
                        changePageListener.onPage(i);
                    }
                }
            });
        }
    }

    public void setScaleAndScroll(float f, int i, int i2) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setScaleAndScroll(f, i, i2);
        }
    }

    public void setSelectionInkColor(int i) {
        ArDkDoc doc;
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null && (doc = nUIDocView.getDoc()) != null) {
            doc.setSelectionInkColor(i);
        }
    }

    public void setSelectionInkThickness(float f) {
        ArDkDoc doc;
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null && (doc = nUIDocView.getDoc()) != null) {
            doc.setSelectionInkThickness(f);
        }
    }

    public void setSelectionText(String str) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setSelectionText(str);
        }
    }

    public void setSigningHandler(NUIDocView.SigningHandler signingHandler) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.setSigningHandler(signingHandler);
        }
    }

    public void share() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.share();
        }
    }

    public boolean shouldConfigureExportPdfAsButton() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.shouldConfigureExportPdfAsButton();
        }
        return false;
    }

    public boolean shouldConfigureSaveAsPDFButton() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.shouldConfigureSaveAsPDFButton();
        }
        return false;
    }

    public void showPageList() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.showPages();
        }
    }

    public void start(Uri uri, int i, boolean z) {
        makeNUIView(uri, (String) null);
        addView(this.mDocView);
        this.mDocView.setDocumentListener(this.mDocumentListener);
        if (i < 0) {
            i = 0;
        }
        Uri uri2 = uri;
        this.mDocView.start(uri2, false, new ViewingState(i), (String) null, this.mDoneListener, z);
    }

    public void tableOfContents() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.tableOfContents();
        }
    }

    public void undo() {
        NUIDocView nUIDocView;
        if (canUndo() && (nUIDocView = this.mDocView) != null) {
            nUIDocView.doUndo();
        }
    }

    public void exportTo(String str) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.exportTo(str);
        }
    }

    public void print(String str, Runnable runnable) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.print(str, runnable);
        }
    }

    public void save(OnSaveListener onSaveListener) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            nUIDocView.save(onSaveListener);
        }
    }

    public boolean select(Point point, Point point2) {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null) {
            return nUIDocView.select(point, point2);
        }
        return false;
    }

    public DocumentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DocumentView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
