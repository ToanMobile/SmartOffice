package com.artifex.mupdf.fitz;

public class PDFPage extends Page {
    public static final int REDACT_IMAGE_NONE = 0;
    public static final int REDACT_IMAGE_PIXELS = 2;
    public static final int REDACT_IMAGE_REMOVE = 1;

    static {
        Context.init();
    }

    private PDFPage(long j) {
        super(j);
    }

    public PDFWidget activateWidgetAt(float f, float f2) {
        for (PDFWidget pDFWidget : getWidgets()) {
            if (pDFWidget.getBounds().contains(f, f2)) {
                pDFWidget.eventEnter();
                pDFWidget.eventDown();
                pDFWidget.eventFocus();
                pDFWidget.eventUp();
                pDFWidget.eventExit();
                pDFWidget.eventBlur();
                return pDFWidget;
            }
        }
        return null;
    }

    public boolean applyRedactions() {
        return applyRedactions(true, 2);
    }

    public native boolean applyRedactions(boolean z, int i);

    public native PDFAnnotation createAnnotation(int i);

    public Link createLinkFit(Rect rect, int i) {
        return createLink(rect, LinkDestination.Fit(0, i));
    }

    public Link createLinkFitB(Rect rect, int i) {
        return createLink(rect, LinkDestination.FitB(0, i));
    }

    public Link createLinkFitBH(Rect rect, int i, float f) {
        return createLink(rect, LinkDestination.FitBH(0, i, f));
    }

    public Link createLinkFitBV(Rect rect, int i, float f) {
        return createLink(rect, LinkDestination.FitBV(0, i, f));
    }

    public Link createLinkFitH(Rect rect, int i, float f) {
        return createLink(rect, LinkDestination.FitH(0, i, f));
    }

    public Link createLinkFitR(Rect rect, int i, float f, float f2, float f3, float f4) {
        return createLink(rect, LinkDestination.FitR(0, i, f, f2, f3, f4));
    }

    public Link createLinkFitV(Rect rect, int i, float f) {
        return createLink(rect, LinkDestination.FitV(0, i, f));
    }

    public Link createLinkXYZ(Rect rect, int i, float f, float f2, float f3) {
        return createLink(rect, LinkDestination.XYZ(0, i, f, f2, f3));
    }

    public native PDFWidget createSignature();

    public native void deleteAnnotation(PDFAnnotation pDFAnnotation);

    public native PDFAnnotation[] getAnnotations();

    public native Matrix getTransform();

    public native PDFWidget[] getWidgets();

    public native boolean update();
}
