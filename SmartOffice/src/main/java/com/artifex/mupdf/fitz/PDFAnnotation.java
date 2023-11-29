package com.artifex.mupdf.fitz;

import java.util.Date;
import java.util.Objects;

public class PDFAnnotation {
    public static final int IS_HIDDEN = 2;
    public static final int IS_INVISIBLE = 1;
    public static final int IS_LOCKED = 128;
    public static final int IS_LOCKED_CONTENTS = 512;
    public static final int IS_NO_ROTATE = 16;
    public static final int IS_NO_VIEW = 32;
    public static final int IS_NO_ZOOM = 8;
    public static final int IS_PRINT = 4;
    public static final int IS_READ_ONLY = 64;
    public static final int IS_TOGGLE_NO_VIEW = 256;
    public static final int LINE_ENDING_BUTT = 6;
    public static final int LINE_ENDING_CIRCLE = 2;
    public static final int LINE_ENDING_CLOSED_ARROW = 5;
    public static final int LINE_ENDING_DIAMOND = 3;
    public static final int LINE_ENDING_NONE = 0;
    public static final int LINE_ENDING_OPEN_ARROW = 4;
    public static final int LINE_ENDING_R_CLOSED_ARROW = 8;
    public static final int LINE_ENDING_R_OPEN_ARROW = 7;
    public static final int LINE_ENDING_SLASH = 9;
    public static final int LINE_ENDING_SQUARE = 1;
    public static final int TYPE_3D = 26;
    public static final int TYPE_CARET = 14;
    public static final int TYPE_CIRCLE = 5;
    public static final int TYPE_FILE_ATTACHMENT = 17;
    public static final int TYPE_FREE_TEXT = 2;
    public static final int TYPE_HIGHLIGHT = 8;
    public static final int TYPE_INK = 15;
    public static final int TYPE_LINE = 3;
    public static final int TYPE_LINK = 1;
    public static final int TYPE_MOVIE = 19;
    public static final int TYPE_POLYGON = 6;
    public static final int TYPE_POLY_LINE = 7;
    public static final int TYPE_POPUP = 16;
    public static final int TYPE_PRINTER_MARK = 23;
    public static final int TYPE_PROJECTION = 27;
    public static final int TYPE_REDACT = 12;
    public static final int TYPE_RICH_MEDIA = 20;
    public static final int TYPE_SCREEN = 22;
    public static final int TYPE_SOUND = 18;
    public static final int TYPE_SQUARE = 4;
    public static final int TYPE_SQUIGGLY = 10;
    public static final int TYPE_STAMP = 13;
    public static final int TYPE_STRIKE_OUT = 11;
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_TRAP_NET = 24;
    public static final int TYPE_UNDERLINE = 9;
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_WATERMARK = 25;
    public static final int TYPE_WIDGET = 21;
    private long pointer;

    static {
        Context.init();
    }

    public PDFAnnotation(long j) {
        this.pointer = j;
    }

    public void addInkList(Point[] pointArr) {
        addInkListStroke();
        for (Point addInkListStrokeVertex : pointArr) {
            addInkListStrokeVertex(addInkListStrokeVertex);
        }
    }

    public native void addInkListStroke();

    public native void addInkListStrokeVertex(float f, float f2);

    public void addInkListStrokeVertex(Point point) {
        addInkListStrokeVertex(point.x, point.y);
    }

    public native void addQuadPoint(Quad quad);

    public native void addVertex(float f, float f2);

    public void addVertex(Point point) {
        addVertex(point.x, point.y);
    }

    public native void clearInkList();

    public native void clearQuadPoints();

    public native void clearVertices();

    public void destroy() {
        finalize();
    }

    public boolean equals(Object obj) {
        if ((obj instanceof PDFAnnotation) && this.pointer == ((PDFAnnotation) obj).pointer) {
            return true;
        }
        return false;
    }

    public native void eventBlur();

    public native void eventDown();

    public native void eventEnter();

    public native void eventExit();

    public native void eventFocus();

    public native void eventUp();

    public native void finalize();

    public native String getAuthor();

    public native float getBorder();

    public native Rect getBounds();

    public native float[] getColor();

    public native String getContents();

    public Date getCreationDate() {
        return new Date(getCreationDateNative());
    }

    public native long getCreationDateNative();

    public native DefaultAppearance getDefaultAppearance();

    public native PDFObject getFileSpecification();

    public native int getFlags();

    public native String getIcon();

    public Point[][] getInkList() {
        int inkListCount = getInkListCount();
        Point[][] pointArr = new Point[inkListCount][];
        for (int i = 0; i < inkListCount; i++) {
            int inkListStrokeCount = getInkListStrokeCount(i);
            pointArr[i] = new Point[inkListStrokeCount];
            for (int i2 = 0; i2 < inkListStrokeCount; i2++) {
                pointArr[i][i2] = getInkListStrokeVertex(i, i2);
            }
        }
        return pointArr;
    }

    public native int getInkListCount();

    public native int getInkListStrokeCount(int i);

    public native Point getInkListStrokeVertex(int i, int i2);

    public native float[] getInteriorColor();

    public native int getLanguage();

    public native Point[] getLine();

    public native int[] getLineEndingStyles();

    public Date getModificationDate() {
        return new Date(getModificationDateNative());
    }

    public native long getModificationDateNative();

    public native PDFObject getObject();

    public native float getOpacity();

    public native Quad getQuadPoint(int i);

    public native int getQuadPointCount();

    public Quad[] getQuadPoints() {
        int quadPointCount = getQuadPointCount();
        Quad[] quadArr = new Quad[quadPointCount];
        for (int i = 0; i < quadPointCount; i++) {
            quadArr[i] = getQuadPoint(i);
        }
        return quadArr;
    }

    public native int getQuadding();

    public native Rect getRect();

    public native int getType();

    public native Point getVertex(int i);

    public native int getVertexCount();

    public Point[] getVertices() {
        int vertexCount = getVertexCount();
        Point[] pointArr = new Point[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            pointArr[i] = getVertex(i);
        }
        return pointArr;
    }

    public native boolean hasAuthor();

    public native boolean hasIcon();

    public native boolean hasInkList();

    public native boolean hasInteriorColor();

    public native boolean hasLine();

    public native boolean hasLineEndingStyles();

    public native boolean hasOpen();

    public native boolean hasQuadPoints();

    public native boolean hasVertices();

    public int hashCode() {
        return Objects.hash(new Object[]{Long.valueOf(this.pointer)});
    }

    public native boolean isOpen();

    public native void run(Device device, Matrix matrix, Cookie cookie);

    public void setAppearance(String str, String str2, Matrix matrix, Rect rect, PDFObject pDFObject, Buffer buffer) {
        setNativeAppearance(str, str2, matrix, rect, pDFObject, buffer);
    }

    public native void setAuthor(String str);

    public native void setBorder(float f);

    public native void setColor(float[] fArr);

    public native void setContents(String str);

    public native void setCreationDate(long j);

    public void setCreationDate(Date date) {
        setCreationDate(date.getTime());
    }

    public native void setDefaultAppearance(String str, float f, float[] fArr);

    public native void setFileSpecification(PDFObject pDFObject);

    public native void setFlags(int i);

    public native void setIcon(String str);

    public void setInkList(Point[][] pointArr) {
        clearInkList();
        for (Point[] pointArr2 : pointArr) {
            addInkListStroke();
            for (Point addInkListStrokeVertex : pointArr[r2]) {
                addInkListStrokeVertex(addInkListStrokeVertex);
            }
        }
    }

    public native void setInteriorColor(float[] fArr);

    public native void setIsOpen(boolean z);

    public native void setLanguage(int i);

    public native void setLine(Point point, Point point2);

    public native void setLineEndingStyles(int i, int i2);

    public void setLineEndingStyles(int[] iArr) {
        setLineEndingStyles(iArr[0], iArr[1]);
    }

    public native void setModificationDate(long j);

    public void setModificationDate(Date date) {
        setModificationDate(date.getTime());
    }

    public native void setNativeAppearance(String str, String str2, Matrix matrix, Rect rect, PDFObject pDFObject, Buffer buffer);

    public native void setNativeAppearanceDisplayList(String str, String str2, Matrix matrix, DisplayList displayList);

    public native void setOpacity(float f);

    public void setQuadPoints(Quad[] quadArr) {
        clearQuadPoints();
        for (Quad addQuadPoint : quadArr) {
            addQuadPoint(addQuadPoint);
        }
    }

    public native void setQuadding(int i);

    public native void setRect(Rect rect);

    public void setVertices(Point[] pointArr) {
        clearVertices();
        for (Point addVertex : pointArr) {
            addVertex(addVertex);
        }
    }

    public native DisplayList toDisplayList();

    public native Pixmap toPixmap(Matrix matrix, ColorSpace colorSpace, boolean z);

    public native boolean update();

    public void setAppearance(String str, Matrix matrix, Rect rect, PDFObject pDFObject, Buffer buffer) {
        setNativeAppearance(str, (String) null, matrix, rect, pDFObject, buffer);
    }

    public void setAppearance(String str, Rect rect, PDFObject pDFObject, Buffer buffer) {
        setNativeAppearance(str, (String) null, (Matrix) null, rect, pDFObject, buffer);
    }

    public void setAppearance(Matrix matrix, Rect rect, PDFObject pDFObject, Buffer buffer) {
        setNativeAppearance((String) null, (String) null, matrix, rect, pDFObject, buffer);
    }

    public void setAppearance(Rect rect, PDFObject pDFObject, Buffer buffer) {
        setNativeAppearance((String) null, (String) null, (Matrix) null, rect, pDFObject, buffer);
    }

    public void setAppearance(String str, String str2, Matrix matrix, DisplayList displayList) {
        setNativeAppearanceDisplayList(str, str2, matrix, displayList);
    }

    public void setAppearance(String str, Matrix matrix, DisplayList displayList) {
        setNativeAppearanceDisplayList(str, (String) null, matrix, displayList);
    }

    public void setAppearance(String str, DisplayList displayList) {
        setNativeAppearanceDisplayList(str, (String) null, (Matrix) null, displayList);
    }

    public void setAppearance(Matrix matrix, DisplayList displayList) {
        setNativeAppearanceDisplayList((String) null, (String) null, matrix, displayList);
    }

    public void setAppearance(DisplayList displayList) {
        setNativeAppearanceDisplayList((String) null, (String) null, (Matrix) null, displayList);
    }
}
