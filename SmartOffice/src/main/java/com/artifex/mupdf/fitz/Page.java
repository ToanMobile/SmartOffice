package com.artifex.mupdf.fitz;

public class Page {
    private long pointer;

    static {
        Context.init();
    }

    public Page(long j) {
        this.pointer = j;
    }

    public Link createLink(Rect rect, LinkDestination linkDestination) {
        return createLink(rect, getDocument().formatLinkURI(linkDestination));
    }

    public native Link createLink(Rect rect, String str);

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public native Rect getBounds();

    public native Document getDocument();

    public native Link[] getLinks();

    public void run(Device device, Matrix matrix) {
        run(device, matrix, (Cookie) null);
    }

    public native void run(Device device, Matrix matrix, Cookie cookie);

    public native void runPageAnnots(Device device, Matrix matrix, Cookie cookie);

    public native void runPageContents(Device device, Matrix matrix, Cookie cookie);

    public native void runPageWidgets(Device device, Matrix matrix, Cookie cookie);

    public native Quad[][] search(String str);

    public native byte[] textAsHtml();

    public DisplayList toDisplayList() {
        return toDisplayList(true);
    }

    public native DisplayList toDisplayList(boolean z);

    public Pixmap toPixmap(Matrix matrix, ColorSpace colorSpace, boolean z) {
        return toPixmap(matrix, colorSpace, z, true);
    }

    public native Pixmap toPixmap(Matrix matrix, ColorSpace colorSpace, boolean z, boolean z2);

    public StructuredText toStructuredText() {
        return toStructuredText((String) null);
    }

    public native StructuredText toStructuredText(String str);
}
