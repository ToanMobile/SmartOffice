package com.artifex.solib;

import android.graphics.PointF;
import android.graphics.RectF;
import com.artifex.sonui.editor.NUIDocView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class SODoc extends ArDkDoc {
    public final int DocType_DOC = 4;
    public final int DocType_DOCX = 5;
    public final int DocType_Error = -1;
    public final int DocType_Other = 7;
    public final int DocType_PDF = 6;
    public final int DocType_PPT = 2;
    public final int DocType_PPTX = 3;
    public final int DocType_XLS = 0;
    public final int DocType_XLSX = 1;
    private long internal;
    public int mExternalClipDataHash = 0;
    public int mFlowMode = 1;
    public boolean searchBackwards;
    public SOSearchListener searchListener;
    public boolean searchMatchCase;
    public boolean searchRunning;
    public String searchText;

    public enum HorizontalAlignment {
        HORIZONTAL_ALIGN_LEFT(0),
        HORIZONTAL_ALIGN_CENTER(1),
        HORIZONTAL_ALIGN_RIGHT(2),
        HORIZONTAL_ALIGN_JUSTIFY(3),
        HORIZONTAL_ALIGN_INVALID(-1);
        
        private final int mAlignment;

        /* access modifiers changed from: public */
        HorizontalAlignment(int i) {
            this.mAlignment = i;
        }

        public static HorizontalAlignment findByValue(int i) {
            for (HorizontalAlignment horizontalAlignment : values()) {
                if (horizontalAlignment.mAlignment == i) {
                    return horizontalAlignment;
                }
            }
            return HORIZONTAL_ALIGN_INVALID;
        }

        public int getAlignment() {
            return this.mAlignment;
        }
    }

    public class SOSelectionContext {
        public int length;
        public int start;
        public String text;

        public SOSelectionContext() {
        }
    }

    public enum VerticalAlignment {
        VERTICAL_ALIGN_TOP(0),
        VERTICAL_ALIGN_CENTER(1),
        VERTICAL_ALIGN_BOTTOM(2),
        VERTICAL_ALIGN_INVALID(-1);
        
        private final int mAlignment;

        /* access modifiers changed from: public */
        VerticalAlignment(int i) {
            this.mAlignment = i;
        }

        public static VerticalAlignment findByValue(int i) {
            for (VerticalAlignment verticalAlignment : values()) {
                if (verticalAlignment.mAlignment == i) {
                    return verticalAlignment;
                }
            }
            return VERTICAL_ALIGN_INVALID;
        }

        public int getAlignment() {
            return this.mAlignment;
        }
    }

    public SODoc(long j) {
        this.internal = j;
    }

    private native void destroy();

    private native int getNumPagesInternal();

    private native String getSelectionNaturalDimensionsInternal();

    private native void insertImageAtSelection(String str);

    private native void insertImageCenterPage(int i, String str);

    private native void nativeCloseSearch();

    private native void nativeInsertAutoshapeCenterPage(int i, String str, String str2, boolean z, float f, float f2);

    private native int nativeSearch(String str, boolean z, boolean z2);

    private native void nativeSetSearchStart(int i, float f, float f2);

    private native int saveToInternal(String str, SODocSaveListener sODocSaveListener);

    private native int saveToPDFInternal(String str, boolean z, SODocSaveListener sODocSaveListener);

//    private void searchProgress(int i, int i2, float f, float f2, float f3, float f4) {
//        final int i3 = i;
//        final int i4 = i2;
//        final float f5 = f;
//        final float f6 = f2;
//        final float f7 = f3;
//        final float f8 = f4;
//        ArDkLib.runOnUiThread(new Runnable() {
//            public void run() {
//                int i = i3;
//                if (i != 5) {
//                    SODoc.this.searchRunning = false;
//                }
//                if (i == 0) {
//                    if (searchListener != null) {
//                        searchListener.found(i4, new RectF(f5, f6, f7, f8));
//                    }
//                } else if (i == 1) {
//                    if (searchListener != null) {
//                        searchListener.notFound();
//                    }
//                } else if (i == 2) {
//                    if (searchListener != null) {
//                        NUIDocView.access$2800(NUIDocView.this);
//                    }
//                } else if (i == 3) {
//                    SOSearchListener sOSearchListener4 = SODoc.this.searchListener;
//                    if (sOSearchListener4 != null) {
//                        NUIDocView.access$2800(NUIDocView.this);
//                    }
//                } else if (i == 5) {
//                    SOSearchListener sOSearchListener5 = SODoc.this.searchListener;
//                } else if (i != 6) {
//                    SOSearchListener sOSearchListener6 = SODoc.this.searchListener;
//                    if (sOSearchListener6 != null) {
//                        NUIDocView.access$2600(NUIDocView.this);
//                        NUIDocView.this.mIsSearching = false;
//                    }
//                } else {
//                    SOSearchListener sOSearchListener7 = SODoc.this.searchListener;
//                    if (sOSearchListener7 != null) {
//                        NUIDocView.access$2600(NUIDocView.this);
//                        NUIDocView.this.mIsSearching = false;
//                    }
//                }
//            }
//        });
//    }

    private native void setSelectionListStyle(int i);

    public native void abortLoad();

    public native void acceptTrackedChange();

    public native void addBlankPage(int i);

    public native void addColumnsLeft();

    public native void addColumnsRight();

    public native void addHighlightAnnotation();

    public native void addRowsAbove();

    public native void addRowsBelow();

    public native void adjustSelection(int i, int i2, int i3);

    public boolean androidClipboardHasData(boolean z) {
        if (clipboardHasData()) {
            return true;
        }
        if (!z) {
            return false;
        }
        return ArDkLib.clipboardHasText();
    }

    public boolean canPrint() {
        return true;
    }

    public boolean canRedo() {
        return getCurrentEdit() < getNumEdits();
    }

    public boolean canSave() {
        return isSavable();
    }

    public boolean canUndo() {
        return getCurrentEdit() > 0;
    }

    public native void cancelSearch();

    public native void clearSelection();

    public native boolean clipboardHasData();

    public void closeSearch() {
        nativeCloseSearch();
        this.searchRunning = false;
    }

    public void createESignatureAt(PointF pointF, int i) {
    }

    public native void createInkAnnotation(int i, SOPoint[] sOPointArr, float f, int i2);

    public void createSignatureAt(PointF pointF, int i) {
    }

    public native void createTextAnnotationAt(PointF pointF, int i);

    public native void deleteChar();

    public native void deleteColumns();

    public native void deleteHighlightAnnotation();

    public native void deletePage(int i);

    public native void deleteRows();

    public void destroyDoc() {
        destroy();
    }

    public void doRedo(Runnable runnable) {
        int currentEdit = getCurrentEdit();
        if (currentEdit < getNumEdits()) {
            clearSelection();
            setCurrentEdit(currentEdit + 1);
        }
        runnable.run();
    }

    public void doUndo(Runnable runnable) {
        int currentEdit = getCurrentEdit();
        if (currentEdit > 0) {
            clearSelection();
            setCurrentEdit(currentEdit - 1);
        }
        runnable.run();
    }

    public native boolean docSupportsDrawing();

    public native boolean docSupportsReview();

    public native void duplicatePage(int i);

    public native int enumerateToc(SOEnumerateTocListener sOEnumerateTocListener);

    public void exportTo(String str, SODocSaveListener sODocSaveListener, String str2) {
    }

    public void finalize() throws Throwable {
        try {
            destroy();
        } finally {
            super.finalize();
        }
    }

    public native void forwardDeleteChar();

    public native boolean getAnnotationCanBePlacedAtArbitraryPosition();

    public native String getAuthor();

    public native String[] getBgColorList();

    public native String getClipboardAsText();

    public native int getCurrentEdit();

    public String getDateFormatPattern() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public native int getDocType();

    public native String getFontList();

    public native boolean getHasBeenModified();

    public native int[] getIndentationLevel();

    public native int getNumEdits();

    public native ArDkPage getPage(int i, SOPageListener sOPageListener);

    public native String getSelectedCellFormat();

    public native float getSelectedColumnWidth();

    public native RectF getSelectedObjectBounds();

    public native float getSelectedRowHeight();

    public native String getSelectedTrackedChangeAuthor();

    public native String getSelectedTrackedChangeComment();

    public native String getSelectedTrackedChangeDate();

    public native int getSelectedTrackedChangeType();

    public native int getSelectionAlignment();

    public native int getSelectionAlignmentV();

    public native String getSelectionAnnotationAuthor();

    public native String getSelectionAnnotationComment();

    public native String getSelectionAnnotationDate();

    public native ArDkBitmap getSelectionAsBitmap();

    public native String getSelectionAsText();

    public native String getSelectionBackgroundColor();

    public native boolean getSelectionCanBeAbsolutelyPositioned();

    public native boolean getSelectionCanBeCopied();

    public native boolean getSelectionCanBeDeleted();

    public native boolean getSelectionCanBePasteTarget();

    public native boolean getSelectionCanBeResized();

    public native boolean getSelectionCanBeRotated();

    public native boolean getSelectionCanCreateAnnotation();

    public native boolean getSelectionCanHaveBackgroundColorApplied();

    public native boolean getSelectionCanHaveForegroundColorApplied();

    public native boolean getSelectionCanHaveHorizontalAlignmentApplied();

    public native boolean getSelectionCanHavePictureInserted();

    public native boolean getSelectionCanHaveShapeInserted();

    public native boolean getSelectionCanHaveTextAltered();

    public native boolean getSelectionCanHaveTextStyleApplied();

    public native boolean getSelectionCanHaveVerticalAlignmentApplied();

    public native SOSelectionContext getSelectionContext();

    public native String getSelectionFillColor();

    public native String getSelectionFontColor();

    public native String getSelectionFontName();

    public native double getSelectionFontSize();

    public native boolean getSelectionHasAssociatedPopup();

    public native boolean getSelectionIsAlterableAnnotation();

    public native boolean getSelectionIsAlterableTextSelection();

    public native boolean getSelectionIsBold();

    public native boolean getSelectionIsItalic();

    public native boolean getSelectionIsLinethrough();

    public native boolean getSelectionIsTablePart();

    public native boolean getSelectionIsUnderlined();

    public native String getSelectionLineColor();

    public native int getSelectionLineType();

    public native float getSelectionLineWidth();

    public native boolean getSelectionListStyleIsDecimal();

    public native boolean getSelectionListStyleIsDisc();

    public native boolean getSelectionListStyleIsNone();

    public PointF getSelectionNaturalDimensions() {
        PointF pointF = new PointF(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
        String selectionNaturalDimensionsInternal = getSelectionNaturalDimensionsInternal();
        if (selectionNaturalDimensionsInternal != null) {
            String[] split = selectionNaturalDimensionsInternal.split(",");
            if (split.length == 2) {
                pointF.set(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
        }
        return pointF;
    }

    public native boolean getSelectionPermitsInlineTextEntry();

    public native float getSelectionRotation();

    public native boolean getShowingTrackedChanges();

    public native boolean getTableCellsMerged();

    public native boolean getTrackingChanges();

    public boolean hasAcroForm() {
        return false;
    }

    public boolean hasXFAForm() {
        return false;
    }

    public void insertAutoShape(int i, String str, String str2) {
        nativeInsertAutoshapeCenterPage(i, str, str2, true, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public void insertImage(String str) {
        insertImageAtSelection(str);
    }

    public native SOLinkData interpretLinkUrl(String str);

    public native boolean isSavable();

    public boolean isSearchRunning() {
        return this.searchRunning;
    }

    public boolean isSelectionInkAnnotation() {
        return false;
    }

    public native void movePage(int i, int i2);

    public native void moveTableSelectionDown();

    public native void moveTableSelectionLeft();

    public native void moveTableSelectionRight();

    public native void moveTableSelectionUp();

    public native boolean nextTrackedChange();

    public native boolean previousTrackedChange();

    public native void processKeyCommand(int i);

    public native boolean providePassword(String str);

    public native void rejectTrackedChange();

    public void saveTo(String str, final SODocSaveListener sODocSaveListener) {
        if (saveToInternal(str, new SODocSaveListener() {
            public void onComplete(final int i, final int i2) {
                ArDkLib.runOnUiThread(new Runnable() {
                    public void run() {
                        sODocSaveListener.onComplete(i, i2);
                    }
                });
            }
        }) != 0) {
            throw new OutOfMemoryError();
        }
    }

    public void saveToPDF(String str, boolean z, final SODocSaveListener sODocSaveListener) {
        if (saveToPDFInternal(str, z, new SODocSaveListener() {
            public void onComplete(final int i, final int i2) {
                ArDkLib.runOnUiThread(new Runnable() {
                    public void run() {
                        sODocSaveListener.onComplete(i, i2);
                    }
                });
            }
        }) != 0) {
            throw new OutOfMemoryError();
        }
    }

    public int search() {
        String str = this.searchText;
        if (str == null) {
            throw new IllegalArgumentException("No Search Text specified");
        } else if (!this.searchRunning) {
            this.searchRunning = true;
            try {
                int nativeSearch = nativeSearch(str, this.searchMatchCase, this.searchBackwards);
                if (nativeSearch != 0) {
                }
                return nativeSearch;
            } finally {
                this.searchRunning = false;
            }
        } else {
            throw new IllegalArgumentException("Search already in progess");
        }
    }

    public native void selectionCopyToClip();

    public native void selectionCutToClip();

    public native void selectionDelete();

    public native boolean selectionIsAutoshapeOrImage();

    public native boolean selectionIsReviewable();

    public native void selectionPaste(int i);

    public native SOSelectionTableRange selectionTableRange();

    public native boolean setAuthor(String str);

    public native void setClipboardFromText(String str);

    public native void setCurrentEdit(int i);

    public native void setFlowModeInternal(int i, float f, float f2);

    public void setForceReload(boolean z) {
    }

    public void setForceReloadAtResume(boolean z) {
    }

    public native void setIndentationLevel(int i);

    public void setSearchBackwards(boolean z) {
        if (!this.searchRunning) {
            this.searchBackwards = z;
            return;
        }
        throw new IllegalArgumentException("Search already in progess");
    }

    public void setSearchListener(SOSearchListener sOSearchListener) {
        if (!this.searchRunning) {
            this.searchListener = sOSearchListener;
            return;
        }
        throw new IllegalArgumentException("Search already in progess");
    }

    public void setSearchMatchCase(boolean z) {
        if (!this.searchRunning) {
            this.searchMatchCase = z;
            return;
        }
        throw new IllegalArgumentException("Search already in progess");
    }

    public void setSearchStart(int i, float f, float f2) {
        if (!this.searchRunning) {
            nativeSetSearchStart(i, f, f2);
            return;
        }
        throw new IllegalArgumentException("Search already in progess");
    }

    public void setSearchString(String str) {
        if (!this.searchRunning) {
            this.searchText = new String(str);
            return;
        }
        throw new IllegalArgumentException("Search already in progess");
    }

    public native void setSelectedCellFormat(String str);

    public native void setSelectedColumnWidth(float f);

    public native void setSelectedObjectBounds(float f, float f2, float f3, float f4);

    public native void setSelectedObjectPosition(float f, float f2);

    public native void setSelectedRowHeight(float f);

    public native void setSelectionAlignment(int i);

    public native void setSelectionAlignmentV(int i);

    public native void setSelectionAnnotationComment(String str);

    public native void setSelectionArrangeBack();

    public native void setSelectionArrangeBackwards();

    public native void setSelectionArrangeForwards();

    public native void setSelectionArrangeFront();

    public native void setSelectionBackgroundColor(String str);

    public native void setSelectionBackgroundTransparent();

    public native void setSelectionFillColor(String str);

    public native void setSelectionFontColor(String str);

    public native void setSelectionFontName(String str);

    public native void setSelectionFontSize(double d);

    public void setSelectionInkColor(int i) {
    }

    public void setSelectionInkThickness(float f) {
    }

    public native void setSelectionIsBold(boolean z);

    public native void setSelectionIsItalic(boolean z);

    public native void setSelectionIsLinethrough(boolean z);

    public native void setSelectionIsUnderlined(boolean z);

    public native void setSelectionLineColor(String str);

    public native void setSelectionLineType(int i);

    public native void setSelectionLineWidth(float f);

    public void setSelectionListStyleDecimal() {
        setSelectionListStyle(1);
    }

    public void setSelectionListStyleDisc() {
        setSelectionListStyle(2);
    }

    public void setSelectionListStyleNone() {
        setSelectionListStyle(0);
    }

    public native void setSelectionRotation(float f);

    public native void setSelectionText(String str);

    public native void setShowingTrackedChanges(boolean z);

    public native void setTableCellsMerged(boolean z);

    public native void setTrackingChanges(boolean z);

    public void insertImage(int i, String str) {
        insertImageCenterPage(i, str);
    }
}
