package com.artifex.solib;

import com.artifex.mupdf.fitz.PDFAnnotation;
import com.artifex.mupdf.fitz.Quad;
import com.artifex.mupdf.fitz.Rect;
import java.util.Date;

public class MuPDFAnnotation {
    public PDFAnnotation mAnnotation;
    public String mAuthor;
    public String mContents;
    public MuPDFDoc mDoc;
    public Date mModDate;
    public MuPDFPage mPage;
    public int mQuadPointCount;
    public Quad[] mQuadPoints;
    public Rect rect = this.mAnnotation.getRect();
    public int type;

    public MuPDFAnnotation(MuPDFDoc muPDFDoc, MuPDFPage muPDFPage, PDFAnnotation pDFAnnotation) {
        muPDFDoc.checkForWorkerThread();
        this.mDoc = muPDFDoc;
        this.mAnnotation = pDFAnnotation;
        this.mPage = muPDFPage;
        this.type = pDFAnnotation.getType();
        try {
            this.mQuadPointCount = this.mAnnotation.getQuadPointCount();
            this.mQuadPoints = this.mAnnotation.getQuadPoints();
        } catch (Exception unused) {
            this.mQuadPoints = null;
            this.mQuadPointCount = 0;
        }
        try {
            this.mModDate = this.mAnnotation.getModificationDate();
        } catch (Exception unused2) {
            this.mModDate = null;
        }
        try {
            this.mAuthor = this.mAnnotation.getAuthor();
        } catch (Exception unused3) {
            this.mAuthor = null;
        }
        try {
            this.mContents = this.mAnnotation.getContents();
        } catch (Exception unused4) {
            this.mContents = null;
        }
    }

    public PDFAnnotation getPDFAnnotation() {
        this.mDoc.checkForWorkerThread();
        return this.mAnnotation;
    }

    public boolean isInk() {
        return this.type == 15;
    }

    public void setRect(Rect rect2) {
        this.mDoc.checkForWorkerThread();
        this.rect = rect2;
        this.mAnnotation.setRect(rect2);
    }
}
