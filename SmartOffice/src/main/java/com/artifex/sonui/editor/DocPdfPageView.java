package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.MuPDFWidget;
import java.util.ArrayList;
import java.util.Iterator;

public class DocPdfPageView extends DocPageView {
    public DocPdfPageView(Context context, ArDkDoc arDkDoc) {
        super(context, arDkDoc);
    }

    public void collectFormFields() {
    }

    public void createESignatureAt(float f, float f2) {
        getDoc().createESignatureAt(screenToPage(new PointF(f, f2)), getPageNumber());
    }

    public void createNote(float f, float f2) {
        getDoc().createTextAnnotationAt(screenToPage(new PointF(f, f2)), getPageNumber());
        invalidate();
    }

    public void createSignatureAt(float f, float f2) {
        getDoc().createSignatureAt(screenToPage(new PointF(f, f2)), getPageNumber());
    }

    public void drawSearchHighlight(Canvas canvas) {
    }

    public void drawSelection(Canvas canvas) {
    }

    public void drawWatermark(Canvas canvas) {
    }

    public MuPDFWidget getNewestWidget() {
        return null;
    }

    public void launchHyperLink(final String str) {
        Context context = getContext();
        Utilities.yesNoMessage((Activity) context, context.getString(R.string.sodk_editor_open_link), "\n" + str + "\n", context.getString(R.string.sodk_editor_OK), context.getString(R.string.sodk_editor_cancel), new Runnable() {
            public void run() {
                DocPdfPageView.super.launchHyperLink(str);
            }
        }, (Runnable) () -> {
        });
    }

    public void onDoubleTap(int i, int i2) {
        Point screenToPage = screenToPage(i, i2);
        this.mPage.select(2, (double) screenToPage.x, (double) screenToPage.y);
        NUIDocView.currentNUIDocView().showUI(true);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isValid() && this.mPage != null) {
            drawSelection(canvas);
            drawSearchHighlight(canvas);
            drawWatermark(canvas);
        }
    }

    public void saveInk() {
        ArrayList<InkAnnotation> arrayList = this.mInkAnnots;
        if (arrayList != null) {
            Iterator<InkAnnotation> it = arrayList.iterator();
            while (it.hasNext()) {
                InkAnnotation next = it.next();
                getDoc().createInkAnnotation(getPageNumber(), next.points(), next.getLineThickness(), next.getLineColor());
            }
        }
        ArrayList<InkAnnotation> arrayList2 = this.mInkAnnots;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
        invalidate();
    }
}
