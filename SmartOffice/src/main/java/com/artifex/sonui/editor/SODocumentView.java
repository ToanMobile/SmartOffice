package com.artifex.sonui.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;

public class SODocumentView extends DocumentView {
    public SODocumentView(Context context) {
        super(context);
    }

    private SODoc getDoc() {
        DocView docView;
        ArDkDoc doc;
        NUIDocView currentNUIDocView = NUIDocView.currentNUIDocView();
        if (currentNUIDocView == null || (docView = currentNUIDocView.getDocView()) == null || (doc = docView.getDoc()) == null) {
            return null;
        }
        return (SODoc) doc;
    }

    public void freezeFirstColumn() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null && (nUIDocView instanceof NUIDocViewXls)) {
            ((NUIDocViewXls) nUIDocView).onFreezeFirstCol((View) null);
        }
    }

    public void freezeFirstRow() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null && (nUIDocView instanceof NUIDocViewXls)) {
            ((NUIDocViewXls) nUIDocView).onFreezeFirstRow((View) null);
        }
    }

    public void freezeSelectedCell() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null && (nUIDocView instanceof NUIDocViewXls)) {
            ((NUIDocViewXls) nUIDocView).onFreezeCell((View) null);
        }
    }

    public void freezeToggleShown() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null && (nUIDocView instanceof NUIDocViewXls)) {
            ((NUIDocViewXls) nUIDocView).onFreezeEnable((View) null);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = r0.getFontList();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String[] getFontList() {
        /*
            r2 = this;
            com.artifex.solib.SODoc r0 = r2.getDoc()
            if (r0 == 0) goto L_0x0013
            java.lang.String r0 = r0.getFontList()
            if (r0 == 0) goto L_0x0013
            java.lang.String r1 = ","
            java.lang.String[] r0 = r0.split(r1)
            return r0
        L_0x0013:
            r0 = 0
            java.lang.String[] r0 = new java.lang.String[r0]
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.SODocumentView.getFontList():java.lang.String[]");
    }

    public SODoc.HorizontalAlignment getHorizontalAlignment() {
        SODoc doc;
        if (this.mDocView == null || (doc = getDoc()) == null) {
            return SODoc.HorizontalAlignment.HORIZONTAL_ALIGN_INVALID;
        }
        return SODoc.HorizontalAlignment.findByValue(doc.getSelectionAlignment());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0008, code lost:
        r0 = getDoc();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int[] getIndentationLevel() {
        /*
            r1 = this;
            com.artifex.sonui.editor.NUIDocView r0 = r1.mDocView
            if (r0 == 0) goto L_0x0013
            boolean r0 = r0 instanceof com.artifex.sonui.editor.NUIDocViewDoc
            if (r0 == 0) goto L_0x0013
            com.artifex.solib.SODoc r0 = r1.getDoc()
            if (r0 == 0) goto L_0x0013
            int[] r0 = r0.getIndentationLevel()
            return r0
        L_0x0013:
            r0 = 0
            int[] r0 = new int[r0]
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.SODocumentView.getIndentationLevel():int[]");
    }

    public String getSelectionFontName() {
        SODoc doc = getDoc();
        return doc != null ? doc.getSelectionFontName() : "";
    }

    public double getSelectionFontSize() {
        SODoc doc = getDoc();
        if (doc != null) {
            return doc.getSelectionFontSize();
        }
        return 0.0d;
    }

    public boolean getSelectionIsBold() {
        SODoc doc = getDoc();
        if (doc != null) {
            return doc.getSelectionIsBold();
        }
        return false;
    }

    public boolean getSelectionIsItalic() {
        SODoc doc = getDoc();
        if (doc != null) {
            return doc.getSelectionIsItalic();
        }
        return false;
    }

    public boolean getSelectionIsLinethrough() {
        SODoc doc = getDoc();
        if (doc != null) {
            return doc.getSelectionIsLinethrough();
        }
        return false;
    }

    public boolean getSelectionIsUnderlined() {
        SODoc doc = getDoc();
        if (doc != null) {
            return doc.getSelectionIsUnderlined();
        }
        return false;
    }

    public SODoc.VerticalAlignment getVerticalAlignment() {
        SODoc doc;
        if (this.mDocView == null || (doc = getDoc()) == null) {
            return SODoc.VerticalAlignment.VERTICAL_ALIGN_INVALID;
        }
        return SODoc.VerticalAlignment.findByValue(doc.getSelectionAlignmentV());
    }

    public boolean isFreezeShown() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView == null || !(nUIDocView instanceof NUIDocViewXls)) {
            return false;
        }
        return ((NUIDocViewXls) nUIDocView).isFreezeShown();
    }

    public boolean isFrozen() {
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView == null || !(nUIDocView instanceof NUIDocViewXls)) {
            return false;
        }
        return ((NUIDocViewXls) nUIDocView).isFrozen();
    }

    public void setIndentationLevel(int i) {
        SODoc doc;
        NUIDocView nUIDocView = this.mDocView;
        if (nUIDocView != null && (nUIDocView instanceof NUIDocViewDoc) && (doc = getDoc()) != null) {
            doc.setIndentationLevel(i);
        }
    }

    public void setSelectionFontName(String str) {
        SODoc doc = getDoc();
        if (doc != null) {
            doc.setSelectionFontName(str);
        }
    }

    public void setSelectionFontSize(double d) {
        SODoc doc = getDoc();
        if (doc != null) {
            doc.setSelectionFontSize(Math.min(Math.max(d, 6.0d), 72.0d));
        }
    }

    public void setSelectionHorizontalAlignment(SODoc.HorizontalAlignment horizontalAlignment) {
        SODoc doc;
        if (this.mDocView != null && (doc = getDoc()) != null) {
            doc.setSelectionAlignment(horizontalAlignment.getAlignment());
        }
    }

    public void setSelectionIsBold(boolean z) {
        SODoc doc = getDoc();
        if (doc != null) {
            doc.setSelectionIsBold(z);
        }
    }

    public void setSelectionIsItalic(boolean z) {
        SODoc doc = getDoc();
        if (doc != null) {
            doc.setSelectionIsItalic(z);
        }
    }

    public void setSelectionIsLinethrough(boolean z) {
        SODoc doc = getDoc();
        if (doc != null) {
            doc.setSelectionIsLinethrough(z);
        }
    }

    public void setSelectionIsUnderlined(boolean z) {
        SODoc doc = getDoc();
        if (doc != null) {
            doc.setSelectionIsUnderlined(z);
        }
    }

    public void setSelectionVerticalAlignment(SODoc.VerticalAlignment verticalAlignment) {
        SODoc doc;
        if (this.mDocView != null && (doc = getDoc()) != null) {
            doc.setSelectionAlignmentV(verticalAlignment.getAlignment());
        }
    }

    public SODocumentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SODocumentView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
