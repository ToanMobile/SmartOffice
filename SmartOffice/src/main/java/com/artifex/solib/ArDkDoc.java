package com.artifex.solib;

import android.graphics.PointF;
import java.util.ArrayList;

public abstract class ArDkDoc {
    public boolean mIsModified = false;
    public int mNumPages = 0;
    public ArrayList<ArDkPage> mPages = new ArrayList<>();
    public int mSelectionEndPage = 0;
    public int mSelectionStartPage = 0;

    public abstract void abortLoad();

    public abstract void addHighlightAnnotation();

    public abstract boolean canPrint();

    public abstract boolean canRedo();

    public abstract boolean canSave();

    public abstract boolean canUndo();

    public abstract void cancelSearch();

    public abstract void clearSelection();

    public abstract void closeSearch();

    public abstract void createESignatureAt(PointF pointF, int i);

    public abstract void createInkAnnotation(int i, SOPoint[] sOPointArr, float f, int i2);

    public abstract void createSignatureAt(PointF pointF, int i);

    public abstract void createTextAnnotationAt(PointF pointF, int i);

    public abstract void deleteHighlightAnnotation();

    public abstract void destroyDoc();

    public abstract void doRedo(Runnable runnable);

    public abstract void doUndo(Runnable runnable);

    public abstract void exportTo(String str, SODocSaveListener sODocSaveListener, String str2);

    public abstract String getAuthor();

    public abstract String getDateFormatPattern();

    public boolean getHasBeenModified() {
        return this.mIsModified;
    }

    public int getNumPages() {
        return this.mNumPages;
    }

    public abstract ArDkPage getPage(int i, SOPageListener sOPageListener);

    public abstract String getSelectionAnnotationAuthor();

    public abstract String getSelectionAnnotationComment();

    public abstract String getSelectionAnnotationDate();

    public abstract String getSelectionAsText();

    public abstract boolean getSelectionCanBeAbsolutelyPositioned();

    public abstract boolean getSelectionCanBeDeleted();

    public abstract boolean getSelectionCanBeResized();

    public abstract boolean getSelectionCanBeRotated();

    public abstract boolean getSelectionHasAssociatedPopup();

    public abstract boolean getSelectionIsAlterableTextSelection();

    public abstract boolean hasAcroForm();

    public abstract boolean hasXFAForm();

    public abstract boolean isSearchRunning();

    public abstract boolean isSelectionInkAnnotation();

    public abstract void processKeyCommand(int i);

    public abstract boolean providePassword(String str);

    public abstract void saveTo(String str, SODocSaveListener sODocSaveListener);

    public abstract void saveToPDF(String str, boolean z, SODocSaveListener sODocSaveListener);

    public abstract int search();

    public abstract void selectionDelete();

    public abstract boolean setAuthor(String str);

    public abstract void setForceReload(boolean z);

    public abstract void setForceReloadAtResume(boolean z);

    public abstract void setSearchBackwards(boolean z);

    public abstract void setSearchListener(SOSearchListener sOSearchListener);

    public abstract void setSearchMatchCase(boolean z);

    public abstract void setSearchStart(int i, float f, float f2);

    public abstract void setSearchString(String str);

    public abstract void setSelectionAnnotationComment(String str);

    public abstract void setSelectionInkColor(int i);

    public abstract void setSelectionInkThickness(float f);
}
