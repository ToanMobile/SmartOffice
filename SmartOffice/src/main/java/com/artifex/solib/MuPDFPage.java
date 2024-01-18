package com.artifex.solib;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import com.artifex.mupdf.fitz.Document;
import com.artifex.mupdf.fitz.Link;
import com.artifex.mupdf.fitz.LinkDestination;
import com.artifex.mupdf.fitz.Matrix;
import com.artifex.mupdf.fitz.PDFAnnotation;
import com.artifex.mupdf.fitz.PDFDocument;
import com.artifex.mupdf.fitz.PDFPage;
import com.artifex.mupdf.fitz.PDFWidget;
import com.artifex.mupdf.fitz.Page;
import com.artifex.mupdf.fitz.Quad;
import com.artifex.mupdf.fitz.Rect;
import com.artifex.mupdf.fitz.StructuredText;
import com.artifex.solib.Worker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class MuPDFPage extends ArDkPage {
    public static int mTextSelPageNum = -1;
    public String lastSearch = "";
    public CopyOnWriteArrayList<MuPDFAnnotation> mAnnotations = new CopyOnWriteArrayList<>();
    public volatile boolean mDestroyed = false;
    public MuPDFDoc mDoc;
    public Page mPage;
    public ArrayList<SOPageListener> mPageListeners = new ArrayList<>();
    public int mPageNumber;
    public Rect[] mSelectionRects = null;
    public StructuredText mStructuredText;
    public boolean mTextDirty = false;
    public ArrayList<MuPDFWidget> mupdfWidgets = null;
    public volatile boolean needsUpdate = false;
    public Rect pageBounds = new Rect();
    public CopyOnWriteArrayList<Rect> pdfWidgetRects = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<PDFWidget> pdfWidgets = new CopyOnWriteArrayList<>();
    public int searchIndex;
    public Quad[][] searchResults;
    public Point selectionEnd = null;
    public Point selectionStart = null;
    public boolean updatingSelectedRedaction = false;

    public MuPDFPage(MuPDFDoc muPDFDoc, Page page, int i) {
        this.mPage = page;
        this.mPageNumber = i;
        this.mDoc = muPDFDoc;
        this.pageBounds = page.getBounds();
        refreshPageElements();
    }

    public static float[] colorToArray(int i) {
        return new float[]{((float) Color.red(i)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.blue(i)) / 255.0f};
    }

    public static PDFPage getPDFPage(Page page) {
        try {
            return (PDFPage) page;
        } catch (Exception unused) {
            return null;
        }
    }

    public static Quad[] rectsToQuads(Rect[] rectArr) {
        Quad[] quadArr = new Quad[rectArr.length];
        int i = 0;
        for (Rect rect : rectArr) {
            float f = rect.x0;
            float f2 = rect.y0;
            float f3 = rect.x1;
            float f4 = rect.y1;
            quadArr[i] = new Quad(f, f2, f3, f2, f, f4, f3, f4);
            i++;
        }
        return quadArr;
    }

    public final void addAnnotation(int i, String str) {
        this.mDoc.checkForWorkerThread();
        PDFPage pDFPage = getPDFPage(this.mPage);
        if (pDFPage != null && mTextSelPageNum == this.mPageNumber) {
            PDFAnnotation createAnnotation = pDFPage.createAnnotation(i);
            if (i == 8) {
                createAnnotation.setColor(colorToArray(16776960));
            }
            createAnnotation.setQuadPoints(rectsToQuads(this.mSelectionRects));
            createAnnotation.setAuthor(str);
            createAnnotation.setModificationDate(new Date());
            createAnnotation.update();
            this.mDoc.mIsModified = true;
        }
    }

    public final boolean annotationIsVisible(PDFAnnotation pDFAnnotation) {
        this.mDoc.checkForWorkerThread();
        int flags = pDFAnnotation.getFlags();
        return (flags & 1) == 0 && (flags & 2) == 0;
    }

    public void clearSelection() {
        this.mSelectionRects = null;
        this.selectionStart = null;
        this.selectionEnd = null;
        mTextSelPageNum = -1;
        updatePageRect((Rect) null);
        this.mDoc.onSelectionUpdate(this.mPageNumber);
    }

    public final void constrainToPage(Rect rect) {
        Rect bounds = this.mPage.getBounds();
        float f = rect.x0;
        float f2 = bounds.x0;
        float f3 = BitmapDescriptorFactory.HUE_RED;
        float f4 = f < f2 ? f2 - f : BitmapDescriptorFactory.HUE_RED;
        float f5 = rect.x1;
        float f6 = bounds.x1;
        if (f5 > f6) {
            f4 = f6 - f5;
        }
        float f7 = rect.y0;
        float f8 = bounds.y0;
        if (f7 < f8) {
            f3 = f8 - f7;
        }
        float f9 = rect.y1;
        float f10 = bounds.y1;
        if (f9 > f10) {
            f3 = f10 - f9;
        }
        rect.x0 = f + f4;
        rect.x1 = f5 + f4;
        rect.y0 = f7 + f3;
        rect.y1 = f9 + f3;
    }

    public int countAnnotations(int i) {
        CopyOnWriteArrayList<MuPDFAnnotation> copyOnWriteArrayList;
        int i2 = 0;
        if (!(getPDFPage(this.mPage) == null || (copyOnWriteArrayList = this.mAnnotations) == null)) {
            Iterator<MuPDFAnnotation> it = copyOnWriteArrayList.iterator();
            while (it.hasNext()) {
                if (it.next().type == i) {
                    i2++;
                }
            }
        }
        return i2;
    }

    public void destroyPage() {
        if (!this.mDestroyed) {
            this.mDestroyed = true;
            MuPDFDoc muPDFDoc = this.mDoc;
            if (muPDFDoc == null) {
                Log.e("MuPDFPage", "destroyPage() mDoc is NULL");
            } else {
                muPDFDoc.mWorker.add(new Worker.Task() {
                    public void run() {
                        MuPDFPage muPDFPage = MuPDFPage.this;
                        muPDFPage.mDoc = null;
                        muPDFPage.mPage = null;
                    }

                    public void work() {
                        try {
                            ArrayList<SOPageListener> arrayList = MuPDFPage.this.mPageListeners;
                            if (arrayList != null) {
                                arrayList.clear();
                            }
                            Page page = MuPDFPage.this.mPage;
                            if (page != null) {
                                page.destroy();
                            }
                        } catch (Exception e) {
                            Log.e("MuPDFPage", "Bad Thing happened destroying a MuPDFPage");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public void finalize() throws Throwable {
    }

    public int findSelectableAnnotAtPoint(Point point, int i) {
        CopyOnWriteArrayList<MuPDFAnnotation> copyOnWriteArrayList;
        if (!(getPDFPage(this.mPage) == null || (copyOnWriteArrayList = this.mAnnotations) == null || copyOnWriteArrayList.size() == 0)) {
            Iterator<MuPDFAnnotation> it = this.mAnnotations.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                MuPDFAnnotation next = it.next();
                if (next != null) {
                    int i3 = next.type;
                    if ((i3 != 21) && ((i3 == i || i == -1) && next.rect.contains((float) point.x, (float) point.y))) {
                        return i2;
                    }
                }
                i2++;
            }
        }
        return -1;
    }

    public MuPDFAnnotation getAnnotation(int i) {
        CopyOnWriteArrayList<MuPDFAnnotation> copyOnWriteArrayList = this.mAnnotations;
        if (copyOnWriteArrayList == null || copyOnWriteArrayList.size() <= i) {
            return null;
        }
        return this.mAnnotations.get(i);
    }

    public android.graphics.Rect getAnnotationRect(int i) {
        if (i >= this.mAnnotations.size()) {
            return null;
        }
        Rect rect = this.mAnnotations.get(i).rect;
        return new android.graphics.Rect((int) rect.x0, (int) rect.y0, (int) rect.x1, (int) rect.y1);
    }

    public Quad[] getQuads(Rect rect) {
        updateText();
        return this.mStructuredText.highlight(new com.artifex.mupdf.fitz.Point((float) ((int) rect.x0), (float) ((int) rect.y0)), new com.artifex.mupdf.fitz.Point((float) ((int) rect.x1), (float) ((int) rect.y1)));
    }

    public Point getSelectedAnnotEnd() {
        Quad[] quadArr;
        MuPDFAnnotation selectedAnnotation = this.mDoc.getSelectedAnnotation();
        if (selectedAnnotation == null || (quadArr = selectedAnnotation.mQuadPoints) == null || quadArr.length <= 0) {
            return null;
        }
        return new Point((int) quadArr[quadArr.length - 1].lr_x, (int) quadArr[quadArr.length - 1].lr_y);
    }

    public Point getSelectedAnnotStart() {
        Quad[] quadArr;
        MuPDFAnnotation selectedAnnotation = this.mDoc.getSelectedAnnotation();
        if (selectedAnnotation == null || (quadArr = selectedAnnotation.mQuadPoints) == null || quadArr.length <= 0) {
            return null;
        }
        return new Point((int) quadArr[0].ul_x, (int) quadArr[0].ul_y);
    }

    public SOHyperlink objectAtPoint(float f, float f2) {
        Link[] links;
        Document document = this.mDoc.mDocument;
        if (!(document == null || (links = this.mPage.getLinks()) == null || links.length == 0)) {
            for (Link link : links) {
                if (link.bounds.contains(f, f2)) {
                    SOHyperlink sOHyperlink = new SOHyperlink();
                    LinkDestination resolveLinkDestination = document.resolveLinkDestination(link);
                    sOHyperlink.pageNum = resolveLinkDestination.page;
                    if (!link.isExternal()) {
                        float f3 = resolveLinkDestination.x;
                        float f4 = resolveLinkDestination.y;
                        sOHyperlink.bbox = new android.graphics.Rect((int) f3, (int) f4, (int) f3, (int) f4);
                        sOHyperlink.url = null;
                    } else {
                        sOHyperlink.bbox = null;
                        sOHyperlink.url = link.uri;
                    }
                    return sOHyperlink;
                }
            }
        }
        return null;
    }

    public void refreshPageElements() {
        PDFWidget[] widgets;
        PDFAnnotation[] annotations;
        Worker worker = this.mDoc.mWorker;
        Objects.requireNonNull(worker);
        if (Thread.currentThread().equals(worker.mThread)) {
            this.mDoc.checkForWorkerThread();
            this.mAnnotations.clear();
            PDFPage pDFPage = getPDFPage(this.mPage);
            if (!(pDFPage == null || (annotations = pDFPage.getAnnotations()) == null || annotations.length <= 0)) {
                for (PDFAnnotation pDFAnnotation : annotations) {
                    if (pDFAnnotation != null && annotationIsVisible(pDFAnnotation)) {
                        pDFAnnotation.update();
                        this.mAnnotations.add(new MuPDFAnnotation(this.mDoc, this, pDFAnnotation));
                    }
                }
            }
            this.mDoc.checkForWorkerThread();
            this.pdfWidgets.clear();
            this.pdfWidgetRects.clear();
            PDFPage pDFPage2 = getPDFPage(this.mPage);
            if (!(pDFPage2 == null || (widgets = pDFPage2.getWidgets()) == null || widgets.length <= 0)) {
                for (PDFWidget pDFWidget : widgets) {
                    if (pDFWidget != null) {
                        pDFWidget.update();
                        this.pdfWidgets.add(pDFWidget);
                        this.pdfWidgetRects.add(pDFWidget.getRect());
                    }
                }
            }
            if (getPDFPage(this.mPage) != null) {
                this.mupdfWidgets = new ArrayList<>();
                CopyOnWriteArrayList<PDFWidget> copyOnWriteArrayList = this.pdfWidgets;
                if (copyOnWriteArrayList != null && copyOnWriteArrayList.size() != 0) {
                    Iterator<PDFWidget> it = this.pdfWidgets.iterator();
                    Iterator<Rect> it2 = this.pdfWidgetRects.iterator();
                    while (it.hasNext()) {
                        PDFWidget next = it.next();
                        Rect next2 = it2.next();
                        int flags = next.getFlags();
                        int fieldFlags = next.getFieldFlags();
                        int fieldType = next.getFieldType();
                        if (annotationIsVisible(next) && (flags & 32) == 0 && (flags & 64) == 0) {
                            if ((fieldFlags & 1) == 0 || fieldType == 6) {
                                MuPDFWidget muPDFWidget = new MuPDFWidget(this.mDoc, next);
                                muPDFWidget.safeBounds = new android.graphics.Rect((int) next2.x0, (int) next2.y0, (int) next2.x1, (int) next2.y1);
                                this.mupdfWidgets.add(muPDFWidget);
                            }
                        }
                    }
                    return;
                }
                return;
            }
            return;
        }
        throw new RuntimeException("MuPDFPage.refreshPageElements should be run on the worker thread.");
    }

    public void releasePage() {
    }

    public ArDkRender renderLayerAtZoomWithAlpha(int i, double d, double d2, double d3, ArDkBitmap arDkBitmap, ArDkBitmap arDkBitmap2, SORenderListener sORenderListener, boolean z, boolean z2) {
        arDkBitmap.getWidth();
        arDkBitmap.getHeight();
        final ArDkBitmap arDkBitmap3 = arDkBitmap;
        android.graphics.Rect rect = arDkBitmap3.rect;
        final Matrix Identity = Matrix.Identity();
        Identity.scale((float) d);
        Worker worker = this.mDoc.mWorker;
        MuPDFRender muPDFRender = new MuPDFRender();
        final SORenderListener sORenderListener2 = sORenderListener;
        Worker.Task r0 = new Worker.Task() {
            public final boolean failed = false;

            public void run() {
                if (sORenderListener2 == null) {
                    return;
                }
                if (MuPDFPage.this.mDestroyed || arDkBitmap3.bitmap.isRecycled()) {
                    sORenderListener2.progress(1);
                } else if (this.failed) {
                    sORenderListener2.progress(1);
                } else {
                    sORenderListener2.progress(0);
                }
            }

            /* JADX WARNING: Removed duplicated region for block: B:43:0x00c7 A[SYNTHETIC, Splitter:B:43:0x00c7] */
            /* JADX WARNING: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void work() {
                /*
                    r13 = this;
                    java.lang.String r0 = "MuPDFPage"
                    com.artifex.solib.MuPDFPage r1 = com.artifex.solib.MuPDFPage.this
                    boolean r1 = r1.mDestroyed
                    if (r1 == 0) goto L_0x0009
                    return
                L_0x0009:
                    com.artifex.solib.Worker r1 = r2
                    boolean r1 = r1.alive
                    if (r1 != 0) goto L_0x0010
                    return
                L_0x0010:
                    com.artifex.solib.MuPDFPage r1 = com.artifex.solib.MuPDFPage.this
                    boolean r1 = r1.needsUpdate
                    if (r1 == 0) goto L_0x0038
                    com.artifex.solib.MuPDFPage r1 = com.artifex.solib.MuPDFPage.this
                    r2 = 0
                    r1.needsUpdate = r2
                    com.artifex.solib.MuPDFPage r1 = com.artifex.solib.MuPDFPage.this
                    com.artifex.solib.MuPDFDoc r2 = r1.mDoc
                    r2.checkForWorkerThread()
                    com.artifex.solib.MuPDFDoc r2 = r1.mDoc
                    com.artifex.mupdf.fitz.Document r2 = r2.mDocument
                    com.artifex.mupdf.fitz.Page r3 = r1.mPage
                    r3.destroy()
                    int r3 = r1.mPageNumber
                    com.artifex.mupdf.fitz.Page r2 = r2.loadPage((int) r3)
                    r1.mPage = r2
                    com.artifex.solib.MuPDFPage r1 = com.artifex.solib.MuPDFPage.this
                    r1.update()
                L_0x0038:
                    com.artifex.mupdf.fitz.Cookie r1 = new com.artifex.mupdf.fitz.Cookie
                    r1.<init>()
                    com.artifex.solib.MuPDFRender r2 = r3
                    r2.mCookie = r1
                    r2 = 1
                    r3 = 0
                    com.artifex.solib.ArDkBitmap r4 = r4     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    android.graphics.Bitmap r4 = r4.bitmap     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    boolean r4 = r4.isRecycled()     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    if (r4 != 0) goto L_0x0078
                    com.artifex.mupdf.fitz.android.AndroidDrawDevice r4 = new com.artifex.mupdf.fitz.android.AndroidDrawDevice     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    com.artifex.solib.ArDkBitmap r5 = r4     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    android.graphics.Bitmap r6 = r5.bitmap     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    int r5 = r5     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    int r7 = -r5
                    int r5 = r6     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    int r8 = -r5
                    int r9 = r7     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    int r10 = r8     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    int r11 = r9     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    int r12 = r10     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    r5 = r4
                    r5.<init>(r6, r7, r8, r9, r10, r11, r12)     // Catch:{ Exception -> 0x00aa, all -> 0x00a7 }
                    com.artifex.solib.MuPDFPage r5 = com.artifex.solib.MuPDFPage.this     // Catch:{ Exception -> 0x0076 }
                    com.artifex.mupdf.fitz.Page r5 = r5.mPage     // Catch:{ Exception -> 0x0076 }
                    com.artifex.mupdf.fitz.Matrix r6 = r11     // Catch:{ Exception -> 0x0076 }
                    r5.run(r4, r6, r1)     // Catch:{ Exception -> 0x0076 }
                    boolean r5 = r12     // Catch:{ Exception -> 0x0076 }
                    if (r5 == 0) goto L_0x0079
                    r4.invertLuminance()     // Catch:{ Exception -> 0x0076 }
                    goto L_0x0079
                L_0x0076:
                    r5 = move-exception
                    goto L_0x00ac
                L_0x0078:
                    r4 = r3
                L_0x0079:
                    if (r4 == 0) goto L_0x00e3
                    com.artifex.solib.MuPDFPage r5 = com.artifex.solib.MuPDFPage.this
                    boolean r5 = r5.mDestroyed
                    if (r5 != 0) goto L_0x00e3
                    com.artifex.solib.ArDkBitmap r5 = r4
                    android.graphics.Bitmap r5 = r5.bitmap
                    boolean r5 = r5.isRecycled()
                    if (r5 != 0) goto L_0x00e3
                    r4.close()     // Catch:{ Exception -> 0x009b }
                    r4.destroy()     // Catch:{ Exception -> 0x009b }
                    com.artifex.solib.MuPDFRender r4 = r3     // Catch:{ Exception -> 0x009b }
                    r4.mCookie = r3     // Catch:{ Exception -> 0x009b }
                    r1.destroy()     // Catch:{ Exception -> 0x009b }
                    goto L_0x00e3
                L_0x0099:
                    r0 = move-exception
                    goto L_0x00a6
                L_0x009b:
                    r1 = move-exception
                    r13.failed = r2     // Catch:{ all -> 0x0099 }
                    java.lang.String r1 = r1.getMessage()     // Catch:{ all -> 0x0099 }
                    android.util.Log.e(r0, r1)     // Catch:{ all -> 0x0099 }
                    goto L_0x00e3
                L_0x00a6:
                    throw r0
                L_0x00a7:
                    r5 = move-exception
                    r4 = r3
                    goto L_0x00e5
                L_0x00aa:
                    r5 = move-exception
                    r4 = r3
                L_0x00ac:
                    r13.failed = r2     // Catch:{ all -> 0x00e4 }
                    java.lang.String r5 = r5.getMessage()     // Catch:{ all -> 0x00e4 }
                    android.util.Log.e(r0, r5)     // Catch:{ all -> 0x00e4 }
                    if (r4 == 0) goto L_0x00e3
                    com.artifex.solib.MuPDFPage r5 = com.artifex.solib.MuPDFPage.this
                    boolean r5 = r5.mDestroyed
                    if (r5 != 0) goto L_0x00e3
                    com.artifex.solib.ArDkBitmap r5 = r4
                    android.graphics.Bitmap r5 = r5.bitmap
                    boolean r5 = r5.isRecycled()
                    if (r5 != 0) goto L_0x00e3
                    r4.close()     // Catch:{ Exception -> 0x00d7 }
                    r4.destroy()     // Catch:{ Exception -> 0x00d7 }
                    com.artifex.solib.MuPDFRender r4 = r3     // Catch:{ Exception -> 0x00d7 }
                    r4.mCookie = r3     // Catch:{ Exception -> 0x00d7 }
                    r1.destroy()     // Catch:{ Exception -> 0x00d7 }
                    goto L_0x00e3
                L_0x00d5:
                    r0 = move-exception
                    goto L_0x00e2
                L_0x00d7:
                    r1 = move-exception
                    r13.failed = r2     // Catch:{ all -> 0x00d5 }
                    java.lang.String r1 = r1.getMessage()     // Catch:{ all -> 0x00d5 }
                    android.util.Log.e(r0, r1)     // Catch:{ all -> 0x00d5 }
                    goto L_0x00e3
                L_0x00e2:
                    throw r0
                L_0x00e3:
                    return
                L_0x00e4:
                    r5 = move-exception
                L_0x00e5:
                    if (r4 == 0) goto L_0x0113
                    com.artifex.solib.MuPDFPage r6 = com.artifex.solib.MuPDFPage.this
                    boolean r6 = r6.mDestroyed
                    if (r6 != 0) goto L_0x0113
                    com.artifex.solib.ArDkBitmap r6 = r4
                    android.graphics.Bitmap r6 = r6.bitmap
                    boolean r6 = r6.isRecycled()
                    if (r6 != 0) goto L_0x0113
                    r4.close()     // Catch:{ Exception -> 0x0107 }
                    r4.destroy()     // Catch:{ Exception -> 0x0107 }
                    com.artifex.solib.MuPDFRender r4 = r3     // Catch:{ Exception -> 0x0107 }
                    r4.mCookie = r3     // Catch:{ Exception -> 0x0107 }
                    r1.destroy()     // Catch:{ Exception -> 0x0107 }
                    goto L_0x0113
                L_0x0105:
                    r0 = move-exception
                    goto L_0x0112
                L_0x0107:
                    r1 = move-exception
                    r13.failed = r2     // Catch:{ all -> 0x0105 }
                    java.lang.String r1 = r1.getMessage()     // Catch:{ all -> 0x0105 }
                    android.util.Log.e(r0, r1)     // Catch:{ all -> 0x0105 }
                    goto L_0x0113
                L_0x0112:
                    throw r0
                L_0x0113:
                    throw r5
                */
                throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.MuPDFPage.AnonymousClass1.work():void");
            }
        };
        if (worker.alive) {
            try {
                worker.mQueue.addFirst(r0);
            } catch (Throwable th) {
                StringBuilder m = new StringBuilder("exception in Worker.addFirst: ");
                m.append(th.toString());
                Log.e("Worker", m.toString());
            }
        }
        return muPDFRender;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0089 A[LOOP:2: B:32:0x0089->B:36:0x00a3, LOOP_START, PHI: r12 
      PHI: (r12v3 int) = (r12v2 int), (r12v12 int) binds: [B:31:0x0086, B:36:0x00a3] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x015a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int select(int r8, double r9, double r11) {
        /*
            r7 = this;
            android.graphics.Point r0 = new android.graphics.Point
            int r9 = (int) r9
            int r10 = (int) r11
            r0.<init>(r9, r10)
            com.artifex.solib.MuPDFDoc r9 = r7.mDoc
            r10 = -1
            r9.selectedAnnotPagenum = r10
            r9.selectedAnnotIndex = r10
            r9 = 1
            if (r8 != 0) goto L_0x0023
            r7.selectionStart = r0
            r7.updateSelectionRects()
            com.artifex.solib.MuPDFDoc r8 = r7.mDoc
            int r10 = r7.mPageNumber
            r8.onSelectionUpdate(r10)
            com.artifex.mupdf.fitz.Rect r8 = r7.pageBounds
            r7.updatePageRect(r8)
            return r9
        L_0x0023:
            if (r8 != r9) goto L_0x0037
            r7.selectionEnd = r0
            r7.updateSelectionRects()
            com.artifex.solib.MuPDFDoc r8 = r7.mDoc
            int r10 = r7.mPageNumber
            r8.onSelectionUpdate(r10)
            com.artifex.mupdf.fitz.Rect r8 = r7.pageBounds
            r7.updatePageRect(r8)
            return r9
        L_0x0037:
            r7.updateText()
            com.artifex.mupdf.fitz.StructuredText r8 = r7.mStructuredText
            r11 = 0
            if (r8 != 0) goto L_0x0042
        L_0x003f:
            r2 = r11
            goto L_0x011f
        L_0x0042:
            com.artifex.mupdf.fitz.StructuredText$TextBlock[] r8 = r8.getBlocks()
            r12 = 0
            if (r8 == 0) goto L_0x0063
            int r1 = r8.length
            r2 = 0
        L_0x004b:
            if (r2 >= r1) goto L_0x0063
            r3 = r8[r2]
            if (r3 == 0) goto L_0x0060
            com.artifex.mupdf.fitz.Rect r4 = r3.bbox
            int r5 = r0.x
            float r5 = (float) r5
            int r6 = r0.y
            float r6 = (float) r6
            boolean r4 = r4.contains(r5, r6)
            if (r4 == 0) goto L_0x0060
            goto L_0x0064
        L_0x0060:
            int r2 = r2 + 1
            goto L_0x004b
        L_0x0063:
            r3 = r11
        L_0x0064:
            if (r3 != 0) goto L_0x0067
            goto L_0x003f
        L_0x0067:
            com.artifex.mupdf.fitz.StructuredText$TextLine[] r8 = r3.lines
            if (r8 == 0) goto L_0x0085
            int r1 = r8.length
            r2 = 0
        L_0x006d:
            if (r2 >= r1) goto L_0x0085
            r3 = r8[r2]
            if (r3 == 0) goto L_0x0082
            com.artifex.mupdf.fitz.Rect r4 = r3.bbox
            int r5 = r0.x
            float r5 = (float) r5
            int r6 = r0.y
            float r6 = (float) r6
            boolean r4 = r4.contains(r5, r6)
            if (r4 == 0) goto L_0x0082
            goto L_0x0086
        L_0x0082:
            int r2 = r2 + 1
            goto L_0x006d
        L_0x0085:
            r3 = r11
        L_0x0086:
            if (r3 != 0) goto L_0x0089
            goto L_0x003f
        L_0x0089:
            com.artifex.mupdf.fitz.StructuredText$TextChar[] r8 = r3.chars
            int r1 = r8.length
            if (r12 >= r1) goto L_0x00a6
            r8 = r8[r12]
            com.artifex.mupdf.fitz.Quad r8 = r8.quad
            com.artifex.mupdf.fitz.Rect r8 = r8.toRect()
            int r1 = r0.x
            float r1 = (float) r1
            int r2 = r0.y
            float r2 = (float) r2
            boolean r8 = r8.contains(r1, r2)
            if (r8 == 0) goto L_0x00a3
            goto L_0x00a7
        L_0x00a3:
            int r12 = r12 + 1
            goto L_0x0089
        L_0x00a6:
            r12 = -1
        L_0x00a7:
            if (r12 != r10) goto L_0x00aa
            goto L_0x003f
        L_0x00aa:
            com.artifex.mupdf.fitz.StructuredText$TextChar[] r8 = r3.chars
            r8 = r8[r12]
            boolean r8 = r8.isWhitespace()
            if (r8 == 0) goto L_0x00b5
            goto L_0x003f
        L_0x00b5:
            r8 = r12
        L_0x00b6:
            int r0 = r8 + 1
            com.artifex.mupdf.fitz.StructuredText$TextChar[] r1 = r3.chars
            int r2 = r1.length
            if (r0 >= r2) goto L_0x00c7
            r1 = r1[r0]
            boolean r1 = r1.isWhitespace()
            if (r1 != 0) goto L_0x00c7
            r8 = r0
            goto L_0x00b6
        L_0x00c7:
            int r0 = r12 + -1
            if (r0 < 0) goto L_0x00d7
            com.artifex.mupdf.fitz.StructuredText$TextChar[] r1 = r3.chars
            r1 = r1[r0]
            boolean r1 = r1.isWhitespace()
            if (r1 != 0) goto L_0x00d7
            r12 = r0
            goto L_0x00c7
        L_0x00d7:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            com.artifex.mupdf.fitz.Rect r1 = new com.artifex.mupdf.fitz.Rect
            r1.<init>()
        L_0x00e1:
            if (r12 > r8) goto L_0x00fa
            com.artifex.mupdf.fitz.StructuredText$TextChar[] r2 = r3.chars
            r2 = r2[r12]
            r0.add(r2)
            com.artifex.mupdf.fitz.StructuredText$TextChar[] r2 = r3.chars
            r2 = r2[r12]
            com.artifex.mupdf.fitz.Quad r2 = r2.quad
            com.artifex.mupdf.fitz.Rect r2 = r2.toRect()
            r1.union(r2)
            int r12 = r12 + 1
            goto L_0x00e1
        L_0x00fa:
            float r8 = r1.x0
            double r2 = (double) r8
            double r2 = java.lang.Math.ceil(r2)
            int r8 = (int) r2
            float r12 = r1.y0
            double r2 = (double) r12
            double r2 = java.lang.Math.ceil(r2)
            int r12 = (int) r2
            float r0 = r1.x1
            double r2 = (double) r0
            double r2 = java.lang.Math.floor(r2)
            int r0 = (int) r2
            float r1 = r1.y1
            double r1 = (double) r1
            double r1 = java.lang.Math.floor(r1)
            int r1 = (int) r1
            android.graphics.Rect r2 = new android.graphics.Rect
            r2.<init>(r8, r12, r0, r1)
        L_0x011f:
            if (r2 == 0) goto L_0x015a
            android.graphics.Point r8 = new android.graphics.Point
            int r10 = r2.left
            int r11 = r2.top
            r8.<init>(r10, r11)
            r7.selectionStart = r8
            android.graphics.Point r8 = new android.graphics.Point
            int r10 = r2.right
            int r11 = r2.bottom
            r8.<init>(r10, r11)
            r7.selectionEnd = r8
            r7.updateSelectionRects()
            int r8 = r7.mPageNumber
            mTextSelPageNum = r8
            com.artifex.mupdf.fitz.Rect r8 = new com.artifex.mupdf.fitz.Rect
            int r10 = r2.left
            float r10 = (float) r10
            int r11 = r2.top
            float r11 = (float) r11
            int r12 = r2.right
            float r12 = (float) r12
            int r0 = r2.bottom
            float r0 = (float) r0
            r8.<init>(r10, r11, r12, r0)
            r7.updatePageRect(r8)
            com.artifex.solib.MuPDFDoc r8 = r7.mDoc
            int r10 = r7.mPageNumber
            r8.onSelectionUpdate(r10)
            goto L_0x016a
        L_0x015a:
            r7.mSelectionRects = r11
            com.artifex.mupdf.fitz.Rect r8 = r7.pageBounds
            r7.updatePageRect(r8)
            com.artifex.solib.MuPDFDoc r8 = r7.mDoc
            int r11 = r7.mPageNumber
            r8.onSelectionUpdate(r11)
            mTextSelPageNum = r10
        L_0x016a:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.MuPDFPage.select(int, double, double):int");
    }

    public ArDkSelectionLimits selectionLimits() {
        MuPDFDoc muPDFDoc = this.mDoc;
        int i = muPDFDoc.selectedAnnotPagenum;
        int selectedAnnotationIndex = muPDFDoc.getSelectedAnnotationIndex();
        if (i == this.mPageNumber && selectedAnnotationIndex >= 0 && selectedAnnotationIndex < this.mAnnotations.size()) {
            return new MuPDFSelectionLimits(toRect(this.mAnnotations.get(selectedAnnotationIndex).rect));
        }
        Rect[] rectArr = this.mSelectionRects;
        if (rectArr == null || rectArr.length <= 0) {
            return null;
        }
        Rect[] rectArr2 = this.mSelectionRects;
        PointF pointF = new PointF(rectArr2[0].x0, rectArr2[0].y0);
        Rect[] rectArr3 = this.mSelectionRects;
        return new MuPDFSelectionLimits(pointF, new PointF(rectArr3[rectArr3.length - 1].x1, rectArr3[rectArr3.length - 1].y1));
    }

    public Point sizeAtZoom(double d) {
        Rect rect = this.pageBounds;
        return new Point((int) (((double) (rect.x1 - rect.x0)) * d), (int) (d * ((double) (rect.y1 - rect.y0))));
    }

    public final android.graphics.Rect toRect(Rect rect) {
        return new android.graphics.Rect((int) rect.x0, (int) rect.y0, (int) rect.x1, (int) rect.y1);
    }

    public final RectF toRectF(Rect rect) {
        return new RectF(rect.x0, rect.y0, rect.x1, rect.y1);
    }

    public void update() {
        PDFPage pDFPage;
        MuPDFDoc muPDFDoc = this.mDoc;
        if (muPDFDoc != null) {
            muPDFDoc.checkForWorkerThread();
            if (this.mDoc.mDocument != null && (pDFPage = getPDFPage(this.mPage)) != null) {
                this.mTextDirty = true;
                pDFPage.update();
                refreshPageElements();
            }
        }
    }

    public final void updatePageRect(Rect rect) {
        Iterator<SOPageListener> it = this.mPageListeners.iterator();
        while (it.hasNext()) {
            SOPageListener next = it.next();
            if (rect != null) {
                next.update(toRectF(rect));
            } else {
                next.update((RectF) null);
            }
        }
    }

    public void updateSelectedRedaction(final android.graphics.Rect rect) {
        if (!this.updatingSelectedRedaction) {
            this.updatingSelectedRedaction = true;
            MuPDFAnnotation selectedAnnotation = this.mDoc.getSelectedAnnotation();
            if (selectedAnnotation != null && selectedAnnotation.type == 12) {
                this.mDoc.mWorker.add(new Worker.Task() {
                    public void run() {
                        MuPDFPage.this.updatingSelectedRedaction = false;
                    }

                    public void work() {
                        MuPDFAnnotation selectedAnnotation = MuPDFPage.this.mDoc.getSelectedAnnotation();
                        PDFDocument pDFDocument = (PDFDocument) MuPDFPage.this.mDoc.mDocument;
                        pDFDocument.beginOperation("updateSelectedRedaction");
                        if (selectedAnnotation.mQuadPointCount == 0) {
                            Rect rect2 = new Rect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom);
                            selectedAnnotation.mDoc.checkForWorkerThread();
                            selectedAnnotation.rect = rect2;
                            selectedAnnotation.mAnnotation.setRect(rect2);
                        } else if (rect == null) {
                            Quad[] rectsToQuads = MuPDFPage.rectsToQuads(MuPDFPage.this.mSelectionRects);
                            if (rectsToQuads.length > 0) {
                                selectedAnnotation.mDoc.checkForWorkerThread();
                                selectedAnnotation.mQuadPoints = rectsToQuads;
                                selectedAnnotation.mAnnotation.setQuadPoints(rectsToQuads);
                            }
                        } else {
                            Quad[] quads = MuPDFPage.this.getQuads(new Rect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom));
                            if (quads != null && quads.length > 0) {
                                selectedAnnotation.mDoc.checkForWorkerThread();
                                selectedAnnotation.mQuadPoints = quads;
                                selectedAnnotation.mAnnotation.setQuadPoints(quads);
                            }
                        }
                        selectedAnnotation.mDoc.checkForWorkerThread();
                        selectedAnnotation.mAnnotation.update();
                        MuPDFPage muPDFPage = MuPDFPage.this;
                        muPDFPage.mDoc.update(muPDFPage.mPageNumber);
                        pDFDocument.endOperation();
                    }
                });
            }
        }
    }

    public final void updateSelectionRects() {
        updateSelectionRects(this.selectionStart, this.selectionEnd);
    }

    public final void updateText() {
        this.mDoc.checkForWorkerThread();
        PDFPage pDFPage = getPDFPage(this.mPage);
        if (pDFPage != null) {
            if (this.mTextDirty) {
                this.mStructuredText = null;
                this.mTextDirty = false;
            }
            if (this.mStructuredText == null) {
                this.mStructuredText = pDFPage.toStructuredText();
                return;
            }
            return;
        }
        this.mStructuredText = null;
    }

    public PointF zoomToFitRect(int i, int i2) {
        Rect rect = this.pageBounds;
        return new PointF(((float) i) / (rect.x1 - rect.x0), ((float) i2) / (rect.y1 - rect.y0));
    }

    public void updateSelectionRects(Point point, Point point2) {
        Quad[] quads;
        this.mSelectionRects = null;
        mTextSelPageNum = -1;
        if (point != null && point2 != null && this.mStructuredText != null && (quads = getQuads(new Rect((float) point.x, (float) point.y, (float) point2.x, (float) point2.y))) != null && quads.length > 0) {
            this.mSelectionRects = new Rect[quads.length];
            mTextSelPageNum = this.mPageNumber;
            for (int i = 0; i < quads.length; i++) {
                this.mSelectionRects[i] = quads[i].toRect();
            }
        }
    }
}
