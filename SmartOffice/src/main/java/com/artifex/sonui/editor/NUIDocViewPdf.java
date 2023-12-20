package com.artifex.sonui.editor;

import com.artifex.source.util.a.util_a.a.b.f.a$$ExternalSyntheticOutline0;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager$$ExternalSyntheticOutline0;
import com.artifex.mupdf.fitz.Document;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.FileUtils;
import com.artifex.solib.MuPDFDoc;
import com.artifex.solib.MuPDFPage;
import com.artifex.solib.SOLinkData;
import com.artifex.solib.SOOutputStream;
import com.artifex.solib.SOPreferences;
import com.artifex.solib.Worker;
import com.artifex.sonui.editor.DrawSignatureDialog;
import com.artifex.sonui.editor.History;
import com.artifex.sonui.editor.NUIDocView;
import com.artifex.sonui.editor.NUIDocViewPdf;
import com.artifex.sonui.editor.NUIView;
import com.artifex.sonui.editor.ThreeChoicePopup;
import com.artifex.sonui.editor.TocDialog;
import com.artifex.sonui.editor.Utilities;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class NUIDocViewPdf extends NUIDocView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public OnClickListener coverListener = new OnClickListener(this) {
        public void onClick(View view) {
        }
    };
    public boolean firstSelectionCleared = false;
    public ToolbarButton mAuthorButton;
    public ToolbarButton mDigSignatureButton;
    public ToolbarButton mESignatureButton;
    public ToolbarButton mHighlightButton;
    public ToolbarButton mNextLinkButton;
    public ToolbarButton mNextSignatureButton;
    public ToolbarButton mNoteButton;
    public ToolbarButton mPreviousLinkButton;
    public ToolbarButton mPreviousSignatureButton;
    public ToolbarButton mRedactApplyButton;
    public ToolbarButton mRedactMarkAreaButton;
    public ToolbarButton mRedactMarkButton;
    public ToolbarButton mRedactRemoveButton;
    public ToolbarButton mRedactSecureSaveButton;
    public Button mTocButton;
    public ToolbarButton mToggleAnnotButton;
    public boolean needsReload = false;
    public boolean signingInProgress = false;
    public boolean tocEnabled = false;

    public NUIDocViewPdf(Context context) {
        super(context);
    }

    public void afterFirstLayoutComplete() {
        super.afterFirstLayoutComplete();
        if (this.mDocCfgOptions.isPDFAnnotationEnabled()) {
            this.mToggleAnnotButton = (ToolbarButton) createToolbarButton(R.id.show_annot_button);
            this.mHighlightButton = (ToolbarButton) createToolbarButton(R.id.highlight_button);
            this.mNoteButton = (ToolbarButton) createToolbarButton(R.id.note_button);
            this.mAuthorButton = (ToolbarButton) createToolbarButton(R.id.author_button);
            this.mDrawButton = (ToolbarButton) createToolbarButton(R.id.draw_button);
            this.mDrawLineColorButton = (ToolbarButton) createToolbarButton(R.id.line_color_button);
            this.mDrawLineThicknessButton = (ToolbarButton) createToolbarButton(R.id.line_thickness_button);
            this.mDeleteInkButton = (ToolbarButton) createToolbarButton(R.id.delete_button);
            this.mDigSignatureButton = (ToolbarButton) createToolbarButton(R.id.dig_sig_button);
            this.mESignatureButton = (ToolbarButton) createToolbarButton(R.id.ink_sig_button);
            this.mNextSignatureButton = (ToolbarButton) createToolbarButton(R.id.next_sig_button);
            this.mPreviousSignatureButton = (ToolbarButton) createToolbarButton(R.id.prev_sig_button);
        }
        this.mRedactMarkButton = (ToolbarButton) createToolbarButton(R.id.redact_button_mark);
        this.mRedactMarkAreaButton = (ToolbarButton) createToolbarButton(R.id.redact_button_mark_area);
        this.mRedactRemoveButton = (ToolbarButton) createToolbarButton(R.id.redact_button_remove);
        this.mRedactApplyButton = (ToolbarButton) createToolbarButton(R.id.redact_button_apply);
        this.mRedactSecureSaveButton = (ToolbarButton) createToolbarButton(R.id.redact_button_secure_save);
        Objects.requireNonNull(this.mDocCfgOptions);
        Objects.requireNonNull(this.mDocCfgOptions);
        Objects.requireNonNull(this.mDocCfgOptions);
        if (!this.mDocCfgOptions.isSecureRedactionsEnabled()) {
            findViewById(R.id.redact_button_secure_save_wrapper).setVisibility(View.GONE);
        }
        Objects.requireNonNull(this.mDocCfgOptions);
        if (!this.mDocCfgOptions.isDigitalSignaturesEnabled()) {
            ToolbarButton toolbarButton = this.mDigSignatureButton;
            if (toolbarButton != null) {
                toolbarButton.setVisibility(View.GONE);
            }
            ToolbarButton toolbarButton2 = this.mNextSignatureButton;
            if (toolbarButton2 != null) {
                toolbarButton2.setVisibility(View.GONE);
            }
            ToolbarButton toolbarButton3 = this.mPreviousSignatureButton;
            if (toolbarButton3 != null) {
                toolbarButton3.setVisibility(View.GONE);
            }
        }
        Button button = (Button) createToolbarButton(R.id.toc_button);
        this.mTocButton = button;
        button.setEnabled(false);
        this.tocEnabled = false;
        hideUnusedButtons();
        this.mPreviousLinkButton = (ToolbarButton) createToolbarButton(R.id.previous_link_button);
        this.mNextLinkButton = (ToolbarButton) createToolbarButton(R.id.next_link_button);
    }

    public Boolean canApplyRedactions() {
        return Boolean.valueOf(((MuPDFDoc) getDoc()).hasRedactionsToApply());
    }

    public Boolean canMarkRedaction() {
        return Boolean.valueOf(getDoc().getSelectionIsAlterableTextSelection());
    }

    public Boolean canRemoveRedaction() {
        return Boolean.valueOf(getDoc().getSelectionCanBeDeleted() && ((MuPDFDoc) getDoc()).selectionIsRedaction());
    }

    public boolean canSelect() {
        return true;
    }

    public void checkXFA() {
        if (this.mDocCfgOptions.isFormFillingEnabled()) {
            if (this.mPageCount == 0) {
                boolean hasXFAForm = getDoc().hasXFAForm();
                boolean hasAcroForm = getDoc().hasAcroForm();
                if (hasXFAForm && !hasAcroForm) {
                    Utilities.showMessage((Activity) getContext(), getContext().getString(R.string.sodk_editor_xfa_title), getContext().getString(R.string.sodk_editor_xfa_body));
                }
                if (hasXFAForm && hasAcroForm) {
                    ((MuPDFDoc) getDoc()).mShowXFAWarning = true;
                }
            }
        }
    }

    public PageAdapter createAdapter() {
        return new PageAdapter(activity(), this, 2);
    }

    public void createEditButtons() {
    }

    public void createEditButtons2() {
    }

    public void createInputView() {
    }

    public void createInsertButtons() {
    }

    public DocView createMainView(Activity activity) {
        return new DocPdfView(activity);
    }

    public void createReviewButtons() {
    }

    public void deleteSelectedText() {
    }

    public void deleteSelection() {
        onDeleteButton((View) null);
    }

    public void doCopy() {
        boolean isExtClipboardOutEnabled = this.mDocCfgOptions.isExtClipboardOutEnabled();
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        String selectionAsText = muPDFDoc.getSelectionAsText();
        if (selectionAsText != null) {
            muPDFDoc.mInternalClipData = selectionAsText;
            if (isExtClipboardOutEnabled) {
                ArDkLib.putTextToClipboard(selectionAsText);
            }
            muPDFDoc.mExternalClipDataHash = ArDkLib.getClipboardText().hashCode();
        }
    }

    public void doRedo() {
        final DocView docView = getDocView();
        ArDkDoc doc = this.mSession.getDoc();
        if (doc.canRedo()) {
            docView.beforeRedo();
            doc.doRedo(new Runnable() {
                public void run() {
                    docView.afterRedo();
                    NUIDocViewPdf.this.updateUIAppearance();
                }
            });
        }
    }

    public void doUndo() {
        final DocView docView = getDocView();
        ArDkDoc doc = this.mSession.getDoc();
        if (doc.canUndo()) {
            docView.beforeUndo();
            doc.doUndo(new Runnable() {
                public void run() {
                    docView.afterUndo();
                    NUIDocViewPdf.this.updateUIAppearance();
                }
            });
        }
    }

    public void findNextSignature() {
        if (this.mDocCfgOptions.isDigitalSignaturesEnabled()) {
            onNextSignatureButton((View) null);
        }
    }

    public void findPreviousSignature() {
        if (this.mDocCfgOptions.isDigitalSignaturesEnabled()) {
            onPreviousSignatureButton((View) null);
        }
    }

    public int getBorderColor() {
        return ContextCompat.getColor(getContext(), R.color.sodk_editor_header_pdf_color);
    }

    public InputView getInputView() {
        return null;
    }

    public int getLayoutId() {
        return R.layout.sodk_editor_pdf_document;
    }

    public DocPdfView getPdfDocView() {
        return (DocPdfView) getDocView();
    }

    public int getSignatureCount() {
        if (!this.mDocCfgOptions.isDigitalSignaturesEnabled()) {
            return 0;
        }
        return getPdfDocView().getSignatureCount();
    }

    public boolean getSigningInProgress() {
        return this.signingInProgress;
    }

    public TabData[] getTabData() {
        if (this.mTabs == null) {
            this.mTabs = new TabData[5];
            int i = this.mDocCfgOptions.isRedactionsEnabled() ? 0 : 8;
            int i2 = this.mDocCfgOptions.isFormSigningFeatureEnabled() ? 0 : 8;
            if (!this.mDocCfgOptions.isEditingEnabled() || !this.mDocCfgOptions.isPDFAnnotationEnabled()) {
                this.mTabs[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_left, 0);
                TabData[] tabDataArr = this.mTabs;
                String string = getContext().getString(R.string.sodk_editor_tab_annotate);
                int i3 = R.id.annotateTab;
                int i4 = R.layout.sodk_editor_tab;
                tabDataArr[1] = new TabData(this, string, i3, i4, 8);
                this.mTabs[2] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_redact), R.id.redactTab, i4, i);
                this.mTabs[3] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_sign), R.id.signTab, i4, i2);
                this.mTabs[4] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_pages), R.id.pagesTab, R.layout.sodk_editor_tab_right, 0);
            } else {
                this.mTabs[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_left, 0);
                TabData[] tabDataArr2 = this.mTabs;
                String string2 = getContext().getString(R.string.sodk_editor_tab_annotate);
                int i5 = R.id.annotateTab;
                int i6 = R.layout.sodk_editor_tab;
                tabDataArr2[1] = new TabData(this, string2, i5, i6, 0);
                this.mTabs[2] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_redact), R.id.redactTab, i6, 0);
                this.mTabs[3] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_sign), R.id.signTab, i6, 0);
                this.mTabs[4] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_pages), R.id.pagesTab, R.layout.sodk_editor_tab_right, 0);
            }
        }
        return this.mTabs;
    }

    public int getTabSelectedColor() {
        if (getResources().getInteger(R.integer.sodk_editor_ui_doc_tab_color_from_doctype) == 0) {
            return ContextCompat.getColor(activity(), R.color.sodk_editor_header_color_selected);
        }
        return ContextCompat.getColor(activity(), R.color.sodk_editor_header_pdf_color);
    }

    public int getTabUnselectedColor() {
        if (getResources().getInteger(R.integer.sodk_editor_ui_doc_tabbar_color_from_doctype) == 0) {
            return ContextCompat.getColor(activity(), R.color.sodk_editor_header_color);
        }
        return Utilities.colorForDocExt(getContext(), getDocFileExtension());
    }

    public void goBack(Runnable runnable) {
        super.goBack(runnable);
    }

    public boolean hasHistory() {
        return true;
    }

    public boolean hasRedo() {
        return true;
    }

    public boolean hasUndo() {
        return true;
    }

    public void hideUnusedButtons() {
        super.hideUnusedButtons();
        ToolbarButton toolbarButton = this.mSavePdfButton;
        if (toolbarButton != null) {
            toolbarButton.setVisibility(View.GONE);
        }
        ToolbarButton toolbarButton2 = this.mOpenPdfInButton;
        if (toolbarButton2 != null) {
            toolbarButton2.setVisibility(View.GONE);
        }
    }

    public void highlightSelection() {
        getDoc().addHighlightAnnotation();
    }

    public void historyNext() {
        onNextLinkButton();
    }

    public void historyPrevious() {
        onPreviousLinkButton();
    }

    public boolean inputViewHasFocus() {
        return false;
    }

    public boolean isDigitalSignatureMode() {
        if (!this.mDocCfgOptions.isDigitalSignaturesEnabled()) {
            return false;
        }
        return getPdfDocView().getSignatureMode();
    }

    public boolean isESignatureMode() {
        return getPdfDocView().getESignatureMode();
    }

    public boolean isNoteModeOn() {
        return getPdfDocView().getNoteMode();
    }

    public boolean isRedactionMode() {
        String currentTab = getCurrentTab();
        return currentTab != null && currentTab.equals("REDACT");
    }

    public boolean isTOCEnabled() {
        return this.tocEnabled;
    }

    public void layoutAfterPageLoad() {
    }

    public void onClick(View view) {
        if (!this.mFinished) {
            super.onClick(view);
            if (view == this.mToggleAnnotButton) {
                onToggleAnnotationsButton(view);
            }
            if (view == this.mHighlightButton) {
                onHighlightButton(view);
            }
            if (view == this.mNoteButton) {
                onNoteButton(view);
            }
            if (view == this.mAuthorButton) {
                onAuthorButton(view);
            }
            if (view == this.mTocButton) {
                onTocButton();
            }
            if (view == this.mDigSignatureButton) {
                onSignatureButton2(view);
            }
            if (view == this.mESignatureButton) {
                onESignatureButton(view);
            }
            if (view == this.mNextSignatureButton) {
                onNextSignatureButton(view);
            }
            if (view == this.mPreviousSignatureButton) {
                onPreviousSignatureButton(view);
            }
            if (view == this.mRedactMarkButton) {
                onRedactMark(view);
            }
            if (view == this.mRedactMarkAreaButton) {
                onRedactMarkArea(view);
            }
            if (view == this.mRedactRemoveButton) {
                onRedactRemove(view);
            }
            if (view == this.mRedactApplyButton) {
                onRedactApply(view);
            }
            if (view == this.mRedactSecureSaveButton) {
                onRedactSecureSave(view);
            }
            if (view == this.mPreviousLinkButton) {
                onPreviousLinkButton();
            }
            if (view == this.mNextLinkButton) {
                onNextLinkButton();
            }
        }
    }

    public void onConfigurationChange(Configuration configuration) {
        super.onConfigurationChange(configuration);
        SignatureDialog.onConfigurationChange();
    }

    public void onDeviceSizeChange() {
        super.onDeviceSizeChange();
        try {
            TocDialog tocDialog = TocDialog.singleton;
            if (tocDialog != null) {
                tocDialog.showOrResize();
            }
        } catch (Exception unused) {
            TocDialog.singleton = null;
        }
    }

    public void onDocCompleted() {
        if (!this.mFinished) {
            this.mCompleted = true;
            ((DocPdfView) getDocView()).collectSignatures(true);
            updateUIAppearance();
            if (!this.firstSelectionCleared) {
                this.mSession.getDoc().clearSelection();
                this.firstSelectionCleared = true;
            }
            int numPages = this.mSession.getDoc().getNumPages();
            this.mPageCount = numPages;
            if (numPages <= 0) {
                Utilities.showMessage((Activity) getContext(), getContext().getString(R.string.sodk_editor_error), Utilities.getOpenErrorDescription(getContext(), 17));
                disableUI();
                return;
            }
            this.mAdapter.setCount(numPages);
            layoutNow();
            this.mTocButton.setEnabled(false);
            this.tocEnabled = false;
            setButtonColor(this.mTocButton, getResources().getInteger(R.color.sodk_editor_button_disabled));
            ArDkLib.enumeratePdfToc(getDoc(), new ArDkLib.EnumeratePdfTocListener() {
                public void done() {
                }

                public void nextTocEntry(int i, int i2, int i3, String str, String str2, float f, float f2) {
                    NUIDocViewPdf.this.mTocButton.setEnabled(true);
                    NUIDocViewPdf nUIDocViewPdf = NUIDocViewPdf.this;
                    nUIDocViewPdf.tocEnabled = true;
                    nUIDocViewPdf.setButtonColor(nUIDocViewPdf.mTocButton, nUIDocViewPdf.getResources().getInteger(R.color.sodk_editor_header_button_enabled_tint));
                }
            });
            if (this.mSession.getDoc().getAuthor() == null) {
                this.mSession.getDoc().setAuthor(SOPreferences.getStringPreference(SOPreferences.getPreferencesObject(activity(), "general"), "DocAuthKey", Utilities.getApplicationName(activity())));
            }
            DocumentListener documentListener = this.mDocumentListener;
            if (documentListener != null) {
                documentListener.onDocCompleted();
            }
            NUIView.DocStateListener docStateListener = this.mDocStateListener;
            if (docStateListener != null) {
                docStateListener.docLoaded();
            }
        }
    }

    public void onESignatureButton(View view) {
        boolean fileExists = FileUtils.fileExists(SOPreferences.getStringPreference(SOPreferences.getPreferencesObject(getContext(), "general"), "eSignaturePath", "path"));
        ThreeChoicePopup threeChoicePopup = new ThreeChoicePopup();
        threeChoicePopup.setChoice1Enabled(fileExists);
        threeChoicePopup.show(getContext(), view, new ThreeChoicePopup.ResultListener() {
            public void onChoice(int i) {
                if (i == 1) {
                    NUIDocViewPdf.this.getPdfDocView().onESignatureMode();
                    NUIDocViewPdf.this.updateUIAppearance();
                } else if (i == 2) {
                    new DrawSignatureDialog(NUIDocViewPdf.this.getContext(), new DrawSignatureDialog.DrawSignatureListener() {
                        public void onCancel() {
                        }

                        public void onSave(Bitmap bitmap) {
                            String m = a$$ExternalSyntheticOutline0.m(SignatureStyle.getSigDirPath(NUIDocViewPdf.this.getContext()), "esig-image.png");
                            SOOutputStream sOOutputStream = new SOOutputStream(m);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, sOOutputStream);
                            sOOutputStream.flush();
                            sOOutputStream.close();
                            SOPreferences.setStringPreference(SOPreferences.getPreferencesObject(NUIDocViewPdf.this.getContext(), "general"), "eSignaturePath", m);
                        }
                    }).show();
                }
            }
        }, getContext().getString(R.string.sodk_editor_place_signature), getContext().getString(R.string.sodk_editor_edit_signature), getContext().getString(R.string.sodk_editor_cancel));
    }

    public void onFullScreen(View view) {
        getPdfDocView().resetModes();
        updateUIAppearance();
        super.onFullScreen(view);
    }

    public void onHighlightButton(View view) {
        highlightSelection();
    }

    public final void onNextLinkButton() {
        History.HistoryItem next = getDocView().getHistory().next();
        if (next != null) {
            getDocView().onHistoryItem(next);
            updateUIAppearance();
        }
    }

    public void onNextSignatureButton(View view) {
        ((DocPdfView) getDocView()).onNextSignature();
        updateUIAppearance();
    }

    public void onNoteButton(View view) {
        getPdfDocView().onNoteMode();
        updateUIAppearance();
    }

    public void onPageLoaded(int i) {
        checkXFA();
        super.onPageLoaded(i);
    }

    public void onPauseCommon() {
        DocPdfView pdfDocView = getPdfDocView();
        if (pdfDocView != null) {
            pdfDocView.resetDrawMode();
        }
        super.onPauseCommon();
    }

    public final void onPreviousLinkButton() {
        History.HistoryItem previous = getDocView().getHistory().previous();
        if (previous != null) {
            getDocView().onHistoryItem(previous);
            updateUIAppearance();
        }
    }

    public void onPreviousSignatureButton(View view) {
        ((DocPdfView) getDocView()).onPreviousSignature();
        updateUIAppearance();
    }

    public void onRedactApply(View view) {
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        muPDFDoc.mWorker.add(new Worker.Task() {
            public void run(
/*
Method generation error in method: com.artifex.solib.MuPDFDoc.18.run():void, dex: classes.dex
            jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFDoc.18.run():void, class status: UNLOADED
            	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
            	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.util.ArrayList.forEach(ArrayList.java:1259)
            	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
            	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
            	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
            	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
            	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
            	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
            	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.util.ArrayList.forEach(ArrayList.java:1259)
            	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
            	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
            	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
            	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
            	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
            	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
            	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
            	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
            	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
            	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            
*/

            public void work(
/*
Method generation error in method: com.artifex.solib.MuPDFDoc.18.work():void, dex: classes.dex
            jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFDoc.18.work():void, class status: UNLOADED
            	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
            	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.util.ArrayList.forEach(ArrayList.java:1259)
            	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
            	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
            	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
            	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
            	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
            	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
            	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.util.ArrayList.forEach(ArrayList.java:1259)
            	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
            	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
            	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
            	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
            	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
            	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
            	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
            	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
            	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
            	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
            	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            
*/
        });
        muPDFDoc.clearSelection();
        updateUIAppearance();
    }

    public void onRedactMark(View view) {
        if (getPdfDocView().getMarkTextMode()) {
            getPdfDocView().setMarkTextMode(false);
            updateUIAppearance();
            return;
        }
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        if (MuPDFPage.mTextSelPageNum >= 0) {
            Objects.requireNonNull(muPDFDoc);
            int i = MuPDFPage.mTextSelPageNum;
            if (i != -1) {
                muPDFDoc.mWorker.add(new Worker.Task(i, false) {
                    public final /* synthetic */ int val$pageNum;
                    public final /* synthetic */ boolean val$select;

                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run(
/*
Method generation error in method: com.artifex.solib.MuPDFDoc.16.run():void, dex: classes.dex
                    jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFDoc.16.run():void, class status: UNLOADED
                    	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
                    	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                    	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                    	at java.util.ArrayList.forEach(ArrayList.java:1259)
                    	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                    	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                    	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                    	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                    	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                    	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                    	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                    	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                    	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                    	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                    	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                    	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                    	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                    	at java.util.ArrayList.forEach(ArrayList.java:1259)
                    	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                    	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                    	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                    	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                    	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                    	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                    	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                    	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                    	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                    	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                    	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                    	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                    	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                    	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                    	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                    	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                    	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                    
*/

                    public void work(
/*
Method generation error in method: com.artifex.solib.MuPDFDoc.16.work():void, dex: classes.dex
                    jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFDoc.16.work():void, class status: UNLOADED
                    	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
                    	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                    	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                    	at java.util.ArrayList.forEach(ArrayList.java:1259)
                    	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                    	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                    	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                    	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                    	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                    	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                    	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                    	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                    	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                    	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                    	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                    	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                    	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                    	at java.util.ArrayList.forEach(ArrayList.java:1259)
                    	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                    	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                    	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                    	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                    	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                    	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                    	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                    	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                    	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                    	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                    	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                    	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                    	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                    	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                    	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                    	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                    	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                    
*/
                });
            }
            updateUIAppearance();
            return;
        }
        clearSelection();
        getPdfDocView().setMarkTextMode(true);
        getPdfDocView().hideHandles();
        updateUIAppearance();
    }

    public void onRedactMarkArea(View view) {
        getPdfDocView().toggleMarkAreaMode();
        updateUIAppearance();
    }

    public void onRedactRemove(View view) {
        if (getDoc().getSelectionCanBeDeleted()) {
            getDoc().selectionDelete();
            getPdfDocView().onSelectionDelete();
            updateUIAppearance();
        }
    }

    public void onRedactSecureSave(View view) {
        Utilities.OCRDataLoader oCRLoader = Utilities.getOCRLoader();
        if (oCRLoader != null) {
            oCRLoader.load(getContext(), new Utilities.OCRDataLoadListener() {
                public void done(boolean z, String str, String str2, String str3) {
                    if (z) {
                        NUIDocViewPdf nUIDocViewPdf = NUIDocViewPdf.this;
                        int i = NUIDocViewPdf.$r8$clinit;
                        String userPath = nUIDocViewPdf.mFileState.getUserPath();
                        if (userPath == null) {
                            userPath = nUIDocViewPdf.mFileState.getOpenedPath();
                        }
                        String name = new File(userPath).getName();
                        nUIDocViewPdf.mDataLeakHandlers.saveAsSecureHandler(FragmentManager$$ExternalSyntheticOutline0.m(Utilities.removeExtension(name), str3, ".", FileUtils.getExtension(name)), nUIDocViewPdf.getDoc(), str, str2, new SOSaveAsComplete(nUIDocViewPdf) {
                            public void onComplete(int i, String str) {
                            }

                            public boolean onFilenameSelected(String str) {
                                return true;
                            }
                        });
                        return;
                    }
                    Utilities.showMessage((Activity) NUIDocViewPdf.this.getContext(), NUIDocViewPdf.this.getContext().getString(R.string.sodk_editor_error), NUIDocViewPdf.this.getContext().getString(R.string.sodk_editor_getting_language_data_files));
                }
            });
        } else {
            Utilities.showMessage((Activity) getContext(), getContext().getString(R.string.sodk_editor_error), getContext().getString(R.string.sodk_editor_no_ocrdataLoader));
        }
    }

    public void onRedoButton(View view) {
        doRedo();
    }

    public void onReflowButton(View view) {
    }

    public void onSearch() {
        super.onSearch();
    }

    public void onSelectionChanged() {
        super.onSelectionChanged();
        getPdfDocView().onSelectionChanged();
    }

    public void onSignatureButton2(View view) {
        getPdfDocView().onSignatureMode();
        updateUIAppearance();
    }

    public void onTabChanged(String str) {
        super.onTabChanged(str);
    }

    public void onTabChanging(String str, String str2) {
        getPdfDocView().saveNoteData();
        if (str.equals(getContext().getString(R.string.sodk_editor_tab_annotate)) && getPdfDocView().getDrawMode()) {
            getPdfDocView().onDrawMode();
        }
    }

    public final void onTocButton() {
        Context context = getContext();
        ArDkDoc doc = getDoc();
        TocDialog tocDialog = new TocDialog(context, doc, this, new TocDialog.TocDialogListener() {
        });
        TocDialog tocDialog2 = TocDialog.singleton;
        if (tocDialog2 != null) {
            tocDialog2.dismiss();
        }
        TocDialog.singleton = tocDialog;
        View inflate = LayoutInflater.from(context).inflate(R.layout.sodk_editor_table_of_contents, (ViewGroup) null);
        ListView listView = (ListView) inflate.findViewById(R.id.List);
        TocDialog.TocListViewAdapter tocListViewAdapter = new TocDialog.TocListViewAdapter(tocDialog, context);
        listView.setAdapter(tocListViewAdapter);
        ArDkLib.enumeratePdfToc(doc, new ArDkLib.EnumeratePdfTocListener(tocListViewAdapter, listView, inflate) {
            public final /* synthetic */ TocListViewAdapter val$adapter;
            public final /* synthetic */ ListView val$list;
            public final /* synthetic */ View val$popupView;

            {
                this.val$adapter = r2;
                this.val$list = r3;
                this.val$popupView = r4;
            }

            public void done() {
                this.val$list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        TocData tocData = AnonymousClass1.this.val$adapter.listEntries.get(i);
                        if (tocData.page >= 0) {
                            float f = tocData.x;
                            float f2 = tocData.y;
                            SOLinkData sOLinkData = new SOLinkData(tocData.page, new RectF(f, f2, f + 1.0f, 1.0f + f2));
                            if (Utilities.isPhoneDevice(TocDialog.this.mContext)) {
                                TocDialog.this.dismiss();
                            }
                            NUIDocViewPdf.AnonymousClass2 r3 = (NUIDocViewPdf.AnonymousClass2) TocDialog.this.mListener;
                            DocView docView = NUIDocViewPdf.this.getDocView();
                            docView.addHistory(docView.getScrollX(), docView.getScrollY(), docView.getScale(), true);
                            docView.addHistory(docView.getScrollX(), docView.getScrollY() - docView.scrollBoxToTopAmount(sOLinkData.page, sOLinkData.box), docView.getScale(), false);
                            docView.scrollBoxToTop(sOLinkData.page, sOLinkData.box);
                            NUIDocViewPdf.this.updateUIAppearance();
                        }
                    }
                });
                TocDialog.this.mCancelButton = (Button) this.val$popupView.findViewById(R.id.cancel_button);
                TocDialog.this.mCancelButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        TocDialog.this.dismiss();
                    }
                });
                TocDialog.this.popupWindow = new NUIPopupWindow(this.val$popupView);
                TocDialog.this.popupWindow.setFocusable(true);
                TocDialog.this.popupWindow.setClippingEnabled(false);
                TocDialog tocDialog = TocDialog.this;
                tocDialog.popupWindow.setOnDismissListener(tocDialog);
                TocDialog.this.showOrResize();
            }

            public void nextTocEntry(int i, int i2, int i3, String str, String str2, float f, float f2) {
                TocDialog.TocListViewAdapter tocListViewAdapter = this.val$adapter;
                TocDialog.TocData tocData = new TocDialog.TocData(TocDialog.this, i, i2, i3, str, str2, f, f2, (AnonymousClass1) null);
                tocListViewAdapter.mapEntries.put(Integer.valueOf(i), tocData);
                int i4 = 0;
                TocDialog.TocData tocData2 = tocData;
                while (tocData2 != null) {
                    int i5 = tocData2.parentHandle;
                    if (i5 == 0) {
                        break;
                    }
                    i4++;
                    tocData2 = tocListViewAdapter.mapEntries.get(Integer.valueOf(i5));
                }
                tocData.level = i4;
                tocListViewAdapter.listEntries.add(tocData);
            }
        });
    }

    public void onToggleAnnotationsButton(View view) {
    }

    public void onUndoButton(View view) {
        doUndo();
    }

    public void preSaveQuestion(final Runnable runnable, final Runnable runnable2) {
        if (((MuPDFDoc) getDoc()).hasRedactionsToApply()) {
            Utilities.yesNoMessage((Activity) getContext(), "", getContext().getString(R.string.sodk_editor_redact_confirm_save), getContext().getString(R.string.sodk_editor_yes), getContext().getString(R.string.sodk_editor_no), new Runnable(this) {
                public void run() {
                    Runnable runnable = runnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }, new Runnable(this) {
                public void run() {
                    Runnable runnable = runnable2;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public void prepareToGoBack() {
        SODocSession sODocSession = this.mSession;
        if ((sODocSession == null || sODocSession.getDoc() != null) && this.mCompleted && getPdfDocView() != null) {
            getPdfDocView().saveInk();
            getPdfDocView().resetModes();
        }
    }

    public void redactApply() {
        onRedactApply((View) null);
    }

    public boolean redactGetMarkTextMode() {
        return getPdfDocView().getMarkTextMode();
    }

    public boolean redactIsMarkingArea() {
        return getPdfDocView().getMarkAreaMode();
    }

    public void redactMarkArea() {
        onRedactMarkArea((View) null);
    }

    public void redactMarkText() {
        onRedactMark((View) null);
    }

    public void redactRemove() {
        onRedactRemove((View) null);
    }

    public void reloadFile() {
        reloadFile((String) null);
    }

    public void secureSave() {
        onRedactSecureSave((View) null);
    }

    public void setConfigurableButtons() {
        super.setConfigurableButtons();
        hideUnusedButtons();
    }

    public void setDigitalSignatureModeOff() {
        if (this.mDocCfgOptions.isDigitalSignaturesEnabled()) {
            getPdfDocView().setSignatureMode(false);
            updateUIAppearance();
        }
    }

    public void setDigitalSignatureModeOn() {
        if (this.mDocCfgOptions.isDigitalSignaturesEnabled()) {
            getPdfDocView().setSignatureMode(true);
            updateUIAppearance();
        }
    }

    public void setDrawModeOff() {
        getPdfDocView().setDrawModeOff();
        updateUIAppearance();
    }

    public void setDrawModeOn() {
        getPdfDocView().onDrawMode();
        updateUIAppearance();
    }

    public void setESignatureModeOff() {
        getPdfDocView().setESignatureMode(false);
        updateUIAppearance();
    }

    public void setESignatureModeOn(View view) {
        onESignatureButton(view);
    }

    public void setNeedsReload() {
        this.needsReload = true;
    }

    public void setNoteModeOff() {
        getPdfDocView().setNoteMode(false);
        updateUIAppearance();
    }

    public void setNoteModeOn() {
        getPdfDocView().setNoteMode(true);
        updateUIAppearance();
    }

    public void setSigningInProgress(boolean z) {
        this.signingInProgress = z;
        if (z) {
            this.needsReload = false;
        }
        if (!z && this.needsReload) {
            reloadFile();
            this.needsReload = false;
        }
        View findViewById = findViewById(R.id.signing_cover);
        if (z) {
            findViewById.setOnClickListener(this.coverListener);
            findViewById.setVisibility(View.VISIBLE);
            return;
        }
        findViewById.setVisibility(View.GONE);
    }

    public void setupTabs() {
        super.setupTabs();
        Objects.requireNonNull(this.mDocCfgOptions);
        super.measureTabs();
    }

    public boolean shouldConfigureExportPdfAsButton() {
        return true;
    }

    public boolean shouldConfigureSaveAsPDFButton() {
        return false;
    }

    public void tableOfContents() {
        onTocButton();
    }

    public void updateUIAppearance() {
        updateSearchUIAppearance();
        DocPdfView pdfDocView = getPdfDocView();
        updateSaveUIAppearance();
        updateUndoUIAppearance();
        ArDkSelectionLimits selectionLimits = getDocView().getSelectionLimits();
        boolean z = false;
        boolean z2 = selectionLimits != null && selectionLimits.getIsActive() && !selectionLimits.getIsCaret();
        boolean selectionCanBeDeleted = getDoc().getSelectionCanBeDeleted();
        ToolbarButton toolbarButton = this.mCopyButton2;
        if (toolbarButton != null) {
            toolbarButton.setEnabled(z2);
        }
        if (this.mDocCfgOptions.isPDFAnnotationEnabled()) {
            this.mHighlightButton.setEnabled(getDoc().getSelectionIsAlterableTextSelection());
            boolean noteMode = pdfDocView.getNoteMode();
            this.mNoteButton.setSelected(noteMode);
            findViewById(R.id.note_holder).setSelected(noteMode);
            boolean signatureMode = pdfDocView.getSignatureMode();
            this.mDigSignatureButton.setSelected(signatureMode);
            findViewById(R.id.dig_sig_holder).setSelected(signatureMode);
            boolean eSignatureMode = pdfDocView.getESignatureMode();
            this.mESignatureButton.setSelected(eSignatureMode);
            findViewById(R.id.ink_sig_holder).setSelected(eSignatureMode);
            boolean drawMode = pdfDocView.getDrawMode();
            this.mDeleteInkButton.setEnabled((drawMode && ((DocPdfView) getDocView()).hasNotSavedInk()) || selectionCanBeDeleted);
            this.mNoteButton.setEnabled(!drawMode && !signatureMode && !eSignatureMode);
            this.mAuthorButton.setEnabled(!drawMode && !signatureMode && !noteMode && !eSignatureMode);
            this.mHighlightButton.setEnabled(!drawMode && !signatureMode && !noteMode && !eSignatureMode);
            this.mDigSignatureButton.setEnabled(!drawMode && !noteMode);
            this.mESignatureButton.setEnabled(!drawMode && !noteMode);
            int countSignatures = getPdfDocView().countSignatures();
            this.mNextSignatureButton.setEnabled(countSignatures > 0);
            this.mPreviousSignatureButton.setEnabled(countSignatures > 0);
            this.mDrawLineColorButton.setDrawableColor(((DocPdfView) getDocView()).getInkLineColor());
            findViewById(R.id.draw_tools_holder).setSelected(drawMode);
        }
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        boolean markAreaMode = pdfDocView.getMarkAreaMode();
        boolean markTextMode = pdfDocView.getMarkTextMode();
        this.mRedactMarkButton.setEnabled(!markAreaMode);
        this.mRedactMarkButton.setSelected(markTextMode);
        this.mRedactMarkAreaButton.setEnabled(!markTextMode);
        this.mRedactMarkAreaButton.setSelected(markAreaMode);
        this.mRedactRemoveButton.setEnabled(!markAreaMode && selectionCanBeDeleted && muPDFDoc.selectionIsRedaction());
        ToolbarButton toolbarButton2 = this.mRedactApplyButton;
        if (!markAreaMode && muPDFDoc.hasRedactionsToApply()) {
            z = true;
        }
        toolbarButton2.setEnabled(z);
        History history = pdfDocView.getHistory();
        this.mPreviousLinkButton.setEnabled(history.canPrevious());
        this.mNextLinkButton.setEnabled(history.canNext());
        getPdfDocView().onSelectionChanged();
        doUpdateCustomUI();
    }

    public boolean usePauseHandler() {
        return !this.signingInProgress;
    }

    public void reloadFile(String str) {
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        if (!getSigningInProgress()) {
            boolean z = muPDFDoc.mForceReloadAtResume;
            boolean z2 = muPDFDoc.mForceReload;
            muPDFDoc.mForceReloadAtResume = false;
            muPDFDoc.mForceReload = false;
            if (z) {
                str = this.mSession.getFileState().getInternalPath();
            } else if (z2) {
                str = this.mSession.getFileState().getUserPath();
                if (str == null) {
                    str = this.mSession.getFileState().getOpenedPath();
                }
            } else if (str == null) {
                str = this.mSession.getFileState().getOpenedPath();
                if (!muPDFDoc.mLastSaveWasIncremental) {
                    long j = muPDFDoc.mLoadTime;
                    long fileLastModified = FileUtils.fileLastModified(str);
                    if (fileLastModified == 0 || fileLastModified < j) {
                        return;
                    }
                } else {
                    return;
                }
            }
            String str2 = str;
            if (!z) {
                muPDFDoc.mOpenedPath = str2;
            }
            final ProgressDialog createAndShowWaitSpinner = Utilities.createAndShowWaitSpinner(getContext());
            muPDFDoc.mWorker.add(new Worker.Task(str2, z2 || z, muPDFDoc, new MuPDFDoc.ReloadListener() {
            }) {
                public Document newDoc;
                public ArrayList<MuPDFPage> newPages;
                public final /* synthetic */ boolean val$forced;
                public final /* synthetic */ ReloadListener val$listener;
                public final /* synthetic */ String val$path;
                public final /* synthetic */ MuPDFDoc val$thisMupdfDoc;

                public void run(
/*
Method generation error in method: com.artifex.solib.MuPDFDoc.30.run():void, dex: classes.dex
                jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFDoc.30.run():void, class status: UNLOADED
                	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
                	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.util.ArrayList.forEach(ArrayList.java:1259)
                	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.util.ArrayList.forEach(ArrayList.java:1259)
                	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                
*/

                public void work(
/*
Method generation error in method: com.artifex.solib.MuPDFDoc.30.work():void, dex: classes.dex
                jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFDoc.30.work():void, class status: UNLOADED
                	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
                	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.util.ArrayList.forEach(ArrayList.java:1259)
                	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.util.ArrayList.forEach(ArrayList.java:1259)
                	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                
*/
            });
        }
    }

    public NUIDocViewPdf(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NUIDocViewPdf(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
