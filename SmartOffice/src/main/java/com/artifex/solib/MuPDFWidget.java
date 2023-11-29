package com.artifex.solib;

import android.graphics.Rect;
import com.artifex.mupdf.fitz.ColorSpace;
import com.artifex.mupdf.fitz.Image;
import com.artifex.mupdf.fitz.PDFDocument;
import com.artifex.mupdf.fitz.PDFWidget;
import com.artifex.mupdf.fitz.Pixmap;
import com.artifex.solib.Worker;

public class MuPDFWidget {
    public boolean mCreatedInThisSession = false;
    public MuPDFDoc mDoc;
    public int mFieldFlags = 0;
    public boolean mIsSigned = false;
    public int mKind = 0;
    public int mMaxChars = 0;
    public int mTextFormat = 0;
    public long mTimeSigned = -1;
    public String mValue;
    public PDFWidget mWidget;
    public Rect safeBounds = null;

    public interface RenderAppearanceListener {
    }

    public MuPDFWidget(MuPDFDoc muPDFDoc, PDFWidget pDFWidget) {
        muPDFDoc.checkForWorkerThread();
        this.mDoc = muPDFDoc;
        this.mWidget = pDFWidget;
        this.mIsSigned = pDFWidget.isSigned();
        this.mKind = this.mWidget.getFieldType();
        this.mTextFormat = this.mWidget.getTextFormat();
        this.mFieldFlags = this.mWidget.getFieldFlags();
        this.mMaxChars = this.mWidget.getMaxLen();
        this.mWidget.isEditing();
        this.mValue = this.mWidget.getValue();
    }

    public Rect getBounds() {
        return new Rect(this.safeBounds);
    }

    public final int getFlags(SignatureAppearance signatureAppearance) {
        int i = signatureAppearance.showLogo ? 32 : 0;
        if (signatureAppearance.showLabels) {
            i |= 1;
        }
        if (signatureAppearance.showDn) {
            i |= 2;
        }
        if (signatureAppearance.showDate) {
            i |= 4;
        }
        if (signatureAppearance.showLeftText) {
            i |= 16;
        }
        return signatureAppearance.showName ? i | 8 : i;
    }

    public final Image getImage(SignatureAppearance signatureAppearance) {
        Image image;
        String str = signatureAppearance.imagePath;
        if (str != null) {
            byte[] readFileBytes = FileUtils.readFileBytes(str);
            if (readFileBytes.length > 0) {
                image = new Image(readFileBytes);
                if (!signatureAppearance.showLeftImage && image == null) {
                    Pixmap pixmap = new Pixmap(ColorSpace.DeviceBGR, 0, 0, 1, 1, true);
                    pixmap.clear();
                    return new Image(pixmap);
                }
            }
        }
        image = null;
        return !signatureAppearance.showLeftImage ? image : image;
    }

    public final Rect makeRect(com.artifex.mupdf.fitz.Rect rect, float f, float f2) {
        return new Rect(Math.round(rect.x0 + f), Math.round(rect.y0 + f2), Math.round(rect.x1 + f), Math.round(rect.y1 + f2));
    }

    public void setEditingState(final boolean z) {
        final Waiter waiter = new Waiter();
        this.mDoc.mWorker.add(new Worker.Task() {
            public void run() {
            }

            public void work() {
                MuPDFWidget.this.mWidget.setEditing(z);
                MuPDFWidget.this.mWidget.update();
                waiter.done();
            }
        });
        waiter.doWait();
    }

    public boolean setValue(final String str, final boolean z) {
        this.mValue = str;
        final Waiter waiter = new Waiter();
        this.mDoc.mWorker.add(new Worker.Task() {
            public void run() {
            }

            public void work() {
                boolean z;
                MuPDFWidget muPDFWidget = MuPDFWidget.this;
                String str = str;
                boolean z2 = z;
                muPDFWidget.mDoc.checkForWorkerThread();
                boolean z3 = false;
                if (muPDFWidget.mWidget != null) {
                    PDFDocument pDFDocument = muPDFWidget.mDoc.getPDFDocument();
                    if (z2) {
                        pDFDocument.beginOperation("set field value");
                    } else {
                        pDFDocument.beginImplicitOperation();
                    }
                    z = true;
                    if (muPDFWidget.mWidget.isEditing() || str == null || !str.equals("")) {
                        String value = muPDFWidget.mWidget.getValue();
                        z3 = muPDFWidget.mWidget.setTextValue(str);
                        muPDFWidget.mWidget.update();
                        if (z3) {
                            int i = muPDFWidget.mKind;
                            if (i == 4 || i == 3) {
                                muPDFWidget.mDoc.mIsModified = true;
                            } else if (!str.trim().equals(value.trim())) {
                                muPDFWidget.mDoc.mIsModified = true;
                            }
                        }
                        if (z3 && !str.trim().equals(value.trim())) {
                            muPDFWidget.mDoc.mIsModified = true;
                        }
                        pDFDocument.endOperation();
                    } else {
                        muPDFWidget.mWidget.setEditing(true);
                        muPDFWidget.mWidget.setTextValue("");
                        muPDFWidget.mWidget.setEditing(false);
                        pDFDocument.endOperation();
                        waiter.value = z;
                        waiter.done();
                    }
                }
                z = z3;
                waiter.value = z;
                waiter.done();
            }
        });
        waiter.doWait();
        return waiter.value;
    }
}
