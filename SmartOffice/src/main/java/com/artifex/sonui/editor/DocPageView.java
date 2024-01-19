package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.artifex.R;
import com.artifex.solib.ArDkBitmap;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ArDkPage;
import com.artifex.solib.ArDkRender;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.SODoc;
import com.artifex.solib.SOHyperlink;
import com.artifex.solib.SOPageListener;
import com.artifex.solib.SOPoint;
import com.artifex.solib.SORenderListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import kotlin.KotlinVersion;

public class DocPageView extends View implements SOPageListener {
    public static Paint lowResPainter;
    public Path clipPath;
    public final Rect drawRect;
    public final Rect drawRectHold = new Rect();
    public float drawScale;
    public float drawScaleHold;
    public boolean isCurrent;
    public Bitmap lowResBitmap;
    public Point lowResScreenSize;
    public int mBackgroundColor;
    public int mBackgroundColorHold;
    public ArDkBitmap mBitmapDraw;
    public ArDkBitmap mBitmapDrawHold = null;
    public ArDkBitmap mBitmapRender = null;
    public final Paint mBlankPainter;
    public final Paint mBorderPainter;
    public final Rect mBorderRect;
    public final Rect mChildRect;
    public SODataLeakHandlers mDataLeakHandlers;
    public ArDkDoc mDoc;
    public DocView mDocView;
    public Rect mDrawToRect = new Rect();
    public final Rect mDstRect;
    public boolean mFinished = false;
    public ArrayList<InkAnnotation> mInkAnnots;
    public int mLayer = -2;
    public ArDkPage mPage;
    public int mPageNum = -1;
    public Rect mPageRect = new Rect();
    public final Paint mPainter;
    public ArDkRender mRender = null;
    public PointF mRenderOrigin = new PointF();
    public Rect mRenderToRect = new Rect();
    public float mScale = 1.0f;
    public final Paint mSelectedBorderPainter;
    public Point mSize;
    public final Rect mSrcRect;
    public double mZoom = 1.0d;
    public DisplayMetrics metrics;
    public final Rect renderRect = new Rect();
    public float renderScale;
    public int[] screenLoc = new int[2];
    public boolean valid;

    public interface ExternalLinkListener {
        void handleExternalLink(int i, Rect rect);
    }

    public class InkAnnotation {
        public ArrayList<PointF> mArc = new ArrayList<>();
        public int mLineColor;
        public float mLineThickness;

        public InkAnnotation(int i, float f) {
            this.mLineColor = i;
            this.mLineThickness = f;
        }

        public void add(PointF pointF) {
            this.mArc.add(new SOPoint(pointF, this.mArc.size() == 0 ? 0 : 1));
        }

        public void draw(Canvas canvas) {
            Path path = new Path();
            PointF pointF = new PointF();
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(this.mLineThickness * ((float) DocPageView.this.getFactor()));
            paint.setColor(this.mLineColor);
            if (this.mArc.size() >= 2) {
                Iterator<PointF> it = this.mArc.iterator();
                DocPageView.this.pageToView(it.next(), pointF);
                float f = pointF.x;
                float f2 = pointF.y;
                path.moveTo(f, f2);
                while (it.hasNext()) {
                    DocPageView.this.pageToView(it.next(), pointF);
                    float f3 = pointF.x;
                    float f4 = pointF.y;
                    path.quadTo(f, f2, (f3 + f) / 2.0f, (f4 + f2) / 2.0f);
                    f = f3;
                    f2 = f4;
                }
                path.lineTo(f, f2);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(path, paint);
                return;
            }
            DocPageView.this.pageToView(this.mArc.get(0), pointF);
            canvas.drawCircle(pointF.x, pointF.y, (this.mLineThickness * ((float) DocPageView.this.getFactor())) / 2.0f, paint);
        }

        public int getLineColor() {
            return this.mLineColor;
        }

        public float getLineThickness() {
            return this.mLineThickness;
        }

        public Rect getRect() {
            Iterator<PointF> it = this.mArc.iterator();
            Rect rect = null;
            int i = 0;
            while (it.hasNext()) {
                PointF next = it.next();
                i++;
                if (i == 1) {
                    float f = next.x;
                    float f2 = next.y;
                    rect = new Rect((int) f, (int) f2, (int) f, (int) f2);
                } else {
                    rect.union((int) next.x, (int) next.y);
                }
            }
            return rect;
        }

        public SOPoint[] points() {
            return (SOPoint[]) this.mArc.toArray(new SOPoint[0]);
        }

        public void setLineColor(int i) {
            this.mLineColor = i;
        }

        public void setLineThickness(float f) {
            this.mLineThickness = f;
        }
    }

    public DocPageView(Context context, ArDkDoc arDkDoc) {
        super(context);
        Context context2 = getContext();
        int i = R.color.sodk_editor_page_default_bg_color;
        this.mBackgroundColorHold = ContextCompat.getColor(context2, i);
        this.mBitmapDraw = null;
        this.drawRect = new Rect();
        this.mBackgroundColor = ContextCompat.getColor(getContext(), i);
        this.mSrcRect = new Rect();
        this.mDstRect = new Rect();
        this.mBorderRect = new Rect();
        this.isCurrent = false;
        this.valid = true;
        this.lowResBitmap = null;
        this.lowResScreenSize = null;
        this.mDocView = null;
        this.clipPath = null;
        this.mChildRect = new Rect();
        setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        resetBackground();
        this.mDoc = arDkDoc;
        this.mPainter = new Paint();
        Paint paint = new Paint();
        this.mBlankPainter = paint;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.mBackgroundColor);
        Paint paint2 = new Paint();
        this.mBorderPainter = paint2;
        paint2.setColor(ContextCompat.getColor(getContext(), R.color.sodk_editor_page_border_color));
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth((float) Utilities.convertDpToPixel(2.0f));
        Paint paint3 = new Paint();
        this.mSelectedBorderPainter = paint3;
        setSelectedBorderColor(ContextCompat.getColor(getContext(), R.color.sodk_editor_selected_page_border_color));
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth((float) Utilities.convertDpToPixel((float) context.getResources().getInteger(R.integer.sodk_editor_selected_page_border_width)));
        if (lowResPainter == null) {
            Paint paint4 = new Paint();
            lowResPainter = paint4;
            paint4.setAntiAlias(true);
            lowResPainter.setFilterBitmap(true);
            lowResPainter.setDither(true);
        }
        this.metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(this.metrics);
    }

    private void getDataLeakHandlers() {
        try {
            SODataLeakHandlers dataLeakHandlers = this.mDocView.getDataLeakHandlers();
            this.mDataLeakHandlers = dataLeakHandlers;
            if (dataLeakHandlers == null) {
                throw new ClassNotFoundException();
            }
        } catch (ExceptionInInitializerError unused) {
            Log.e("DocPageView", String.format("getDataLeakHandlers() experienced unexpected exception [%s]", new Object[]{"ExceptionInInitializerError"}));
        } catch (LinkageError unused2) {
            Log.e("DocPageView", String.format("getDataLeakHandlers() experienced unexpected exception [%s]", new Object[]{"LinkageError"}));
        } catch (SecurityException unused3) {
            Log.e("DocPageView", String.format("getDataLeakHandlers() experienced unexpected exception [%s]", new Object[]{"SecurityException"}));
        } catch (ClassNotFoundException unused4) {
            Log.i("DocPageView", "DataLeakHandlers implementation unavailable");
        }
    }

    public boolean canDoubleTap(int i, int i2) {
        return true;
    }

    public void changePage(int i) {
        if (isFinished() || !this.valid || this.mDoc == null) {
            return;
        }
        if (i != this.mPageNum || this.mPage == null) {
            this.mPageNum = i;
            dropPage();
            ArDkPage page = this.mDoc.getPage(this.mPageNum, this);
            this.mPage = page;
            ArDkDoc arDkDoc = this.mDoc;
            Objects.requireNonNull(arDkDoc);
            if (page != null) {
                arDkDoc.mPages.add(page);
            }
        }
    }

    public void clearContent() {
        this.mBitmapDraw = null;
        invalidate();
    }

    public void clearInk() {
        if (this.mInkAnnots != null) {
            invalidate();
            this.mInkAnnots.clear();
        }
    }

    public void continueDrawInk(float f, float f2) {
        ArrayList<InkAnnotation> arrayList = this.mInkAnnots;
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList<InkAnnotation> arrayList2 = this.mInkAnnots;
            arrayList2.get(arrayList2.size() - 1).add(screenToPage(new PointF(f, f2)));
            invalidate();
        }
    }

    public void drawBackgroundColor(Canvas canvas) {
        this.mBlankPainter.setColor(this.mBackgroundColor);
        Rect rect = new Rect();
        getLocalVisibleRect(rect);
        canvas.drawRect(rect, this.mBlankPainter);
    }

    public void dropPage() {
        ArDkPage arDkPage = this.mPage;
        if (arDkPage != null) {
            ArDkDoc arDkDoc = this.mDoc;
            Objects.requireNonNull(arDkDoc);
            if (arDkPage != null) {
                arDkDoc.mPages.remove(arDkPage);
            }
            this.mPage.releasePage();
        }
        this.mPage = null;
    }

    public void endDrawInk() {
    }

    public void endRenderPass() {
        this.mBitmapDraw = this.mBitmapDrawHold;
        this.drawRect.set(this.drawRectHold);
        this.drawScale = this.drawScaleHold;
        this.mBackgroundColor = this.mBackgroundColorHold;
    }

    public void finish() {
        this.mFinished = true;
        stopRender();
        ArDkPage arDkPage = this.mPage;
        if (arDkPage != null) {
            arDkPage.destroyPage();
            this.mPage = null;
        }
        this.mBitmapDraw = null;
        this.mBitmapDrawHold = null;
        this.mBitmapRender = null;
        this.mDoc = null;
    }

    public Rect getChildRect() {
        return this.mChildRect;
    }

    public Path getClipPath() {
        return this.clipPath;
    }

    public ArDkDoc getDoc() {
        return this.mDoc;
    }

    public DocView getDocView() {
        return this.mDocView;
    }

    public double getFactor() {
        return this.mZoom * ((double) this.mScale);
    }

    public ArDkPage getPage() {
        return this.mPage;
    }

    public int getPageNumber() {
        return this.mPageNum;
    }

    public int getReflowWidth() {
        return this.mPage.sizeAtZoom(1.0d).x;
    }

    public ArDkSelectionLimits getSelectionLimits() {
        ArDkPage arDkPage = this.mPage;
        if (arDkPage == null) {
            return null;
        }
        return arDkPage.selectionLimits();
    }

    public Point getSize() {
        return this.mSize;
    }

    public int getUnscaledHeight() {
        DocView docView = getDocView();
        if (docView != null && docView.getReflowMode()) {
            return this.mPage.sizeAtZoom(this.mZoom).y;
        }
        Point point = this.mSize;
        if (point == null) {
            return 0;
        }
        return point.y;
    }

    public int getUnscaledWidth() {
        DocView docView = getDocView();
        if (docView != null && docView.getReflowMode()) {
            return this.mPage.sizeAtZoom(this.mZoom).x;
        }
        Point point = this.mSize;
        if (point == null) {
            return 0;
        }
        return point.x;
    }

    public double getZoom() {
        return this.mZoom;
    }

    public double getZoomScale() {
        return this.mZoom * ((double) this.mScale);
    }

    public boolean handleFullscreenTap(int i, int i2) {
        return tryHyperlink(new Point(i, i2), (ExternalLinkListener) null);
    }

    public boolean isCurrent() {
        return this.isCurrent;
    }

    public boolean isFinished() {
        return this.mFinished;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void launchHyperLink(String str) {
        SODataLeakHandlers sODataLeakHandlers = this.mDataLeakHandlers;
        if (sODataLeakHandlers != null) {
            try {
                sODataLeakHandlers.launchUrlHandler(str);
            } catch (UnsupportedOperationException unused) {
            }
        } else {
            Utilities.launchUrl(getContext(), str);
        }
    }

    public void onDoubleTap(int i, int i2) {
        Point screenToPage = screenToPage(i, i2);
        if (!((SODoc) getDoc()).selectionIsAutoshapeOrImage()) {
            this.mPage.select(2, (double) screenToPage.x, (double) screenToPage.y);
        }
        NUIDocView.currentNUIDocView().showUI(true);
    }

    public void onDraw(Canvas canvas) {
        Rect rect;
        if (this.mFinished || !isShown() || this.mPage == null) {
            return;
        }
        if (this.lowResBitmap != null) {
            Rect rect2 = new Rect(0, 0, this.lowResBitmap.getWidth(), this.lowResBitmap.getHeight());
            Point screenSize = Utilities.getScreenSize(getContext());
            Point point = this.lowResScreenSize;
            if (point == null || screenSize == null || (point.x == screenSize.x && point.y == screenSize.y)) {
                rect = this.drawRect;
            } else {
                rect = new Rect();
                getLocalVisibleRect(rect);
            }
            canvas.drawBitmap(this.lowResBitmap, rect2, rect, this.mPainter);
        } else if (this.valid) {
            drawBackgroundColor(canvas);
            if (this.clipPath != null) {
                canvas.save();
            }
            ArDkBitmap arDkBitmap = this.mBitmapDraw;
            if (arDkBitmap != null && !arDkBitmap.getBitmap().isRecycled()) {
                this.mSrcRect.set(arDkBitmap.getRect());
                this.mDstRect.set(this.drawRect);
                float f = this.drawScale;
                float f2 = this.mScale;
                if (f != f2) {
                    Rect rect3 = this.mDstRect;
                    rect3.left = (int) ((f2 / f) * ((float) rect3.left));
                    rect3.top = (int) ((f2 / f) * ((float) rect3.top));
                    rect3.right = (int) ((f2 / f) * ((float) rect3.right));
                    rect3.bottom = (int) ((f2 / f) * ((float) rect3.bottom));
                }
                Path path = this.clipPath;
                if (path != null) {
                    canvas.clipPath(path);
                }
                canvas.drawBitmap(arDkBitmap.getBitmap(), this.mSrcRect, this.mDstRect, this.mPainter);
                this.mBorderRect.set(0, 0, getWidth(), getHeight());
                if (this.isCurrent) {
                    canvas.drawRect(this.mBorderRect, this.mSelectedBorderPainter);
                } else {
                    canvas.drawRect(this.mBorderRect, this.mBorderPainter);
                }
                if (this.clipPath != null) {
                    canvas.restore();
                }
                ArrayList<InkAnnotation> arrayList = this.mInkAnnots;
                if (arrayList != null) {
                    Iterator<InkAnnotation> it = arrayList.iterator();
                    while (it.hasNext()) {
                        InkAnnotation next = it.next();
                        next.getRect();
                        next.draw(canvas);
                    }
                }
            }
        }
    }

    public void onFullscreen(boolean z) {
    }

    public void onPause() {
    }

    public void onReflowScale(DocPageView docPageView) {
        Point point;
        this.mZoom = docPageView.mZoom;
        this.mScale = docPageView.mScale;
        Point point2 = this.mSize;
        if (!(point2 == null || (point = docPageView.mSize) == null)) {
            point2.x = point.x;
            point2.y = point.y;
        }
        requestLayout();
    }

    public boolean onSingleTap(int i, int i2, boolean z, ExternalLinkListener externalLinkListener) {
        Point screenToPage = screenToPage(i, i2);
        if (tryHyperlink(screenToPage, externalLinkListener)) {
            return true;
        }
        if (!z) {
            return false;
        }
        SODoc sODoc = (SODoc) getDoc();
        if (!sODoc.selectionIsAutoshapeOrImage()) {
            sODoc.clearSelection();
        }
        this.mPage.select(3, (double) screenToPage.x, (double) screenToPage.y);
        return false;
    }

    public Point pageToScreen(Point point) {
        double factor = getFactor();
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        return new Point(((int) (((double) point.x) * factor)) + iArr[0], ((int) (((double) point.y) * factor)) + iArr[1]);
    }

    public int pageToView(int i) {
        return (int) (((double) i) * getFactor());
    }

    public void render(ArDkBitmap arDkBitmap, SORenderListener sORenderListener) {
        final SORenderListener sORenderListener2 = sORenderListener;
        if (!this.mFinished && this.mPage != null) {
            Rect rect = new Rect();
            if (!getLocalVisibleRect(rect)) {
                sORenderListener2.progress(0);
                return;
            }
            Rect rect2 = new Rect();
            if (!getGlobalVisibleRect(rect2)) {
                sORenderListener2.progress(0);
                return;
            }
            this.mRenderToRect.set(rect2);
            Rect rect3 = this.mRenderToRect;
            int i = NUIDocView.OVERSIZE_MARGIN;
            rect3.offset(i, i);
            this.mDrawToRect.set(rect);
            setPageRect();
            int min = Math.min(Math.max(this.mRenderToRect.top - this.mPageRect.top, 0), NUIDocView.OVERSIZE_MARGIN);
            int min2 = Math.min(Math.max(this.mPageRect.bottom - this.mRenderToRect.bottom, 0), NUIDocView.OVERSIZE_MARGIN);
            int min3 = Math.min(Math.max(this.mRenderToRect.left - this.mPageRect.left, 0), NUIDocView.OVERSIZE_MARGIN);
            int min4 = Math.min(Math.max(this.mPageRect.right - this.mRenderToRect.right, 0), NUIDocView.OVERSIZE_MARGIN);
            DocView docView = getDocView();
            if (docView != null && docView.pagesShowing()) {
                if (getParent() instanceof DocListPagesView) {
                    min3 = Math.min(Math.max(this.mRenderToRect.left - this.mPageRect.left, 0), 0);
                } else {
                    min4 = Math.min(Math.max(this.mPageRect.right - this.mRenderToRect.right, 0), 0);
                }
            }
            Rect rect4 = this.mRenderToRect;
            rect4.top -= min;
            rect4.bottom += min2;
            rect4.left -= min3;
            rect4.right += min4;
            Rect rect5 = this.mDrawToRect;
            rect5.top -= min;
            rect5.bottom += min2;
            rect5.left -= min3;
            rect5.right += min4;
            int i2 = rect4.left;
            if (i2 < 0) {
                int i3 = -i2;
                rect4.left = i2 + i3;
                rect5.left += i3;
            }
            if (rect4.right > arDkBitmap.getWidth()) {
                int width = this.mRenderToRect.right - arDkBitmap.getWidth();
                this.mRenderToRect.right -= width;
                this.mDrawToRect.right -= width;
            }
            Rect rect6 = this.mRenderToRect;
            int i4 = rect6.top;
            if (i4 < 0) {
                int i5 = -i4;
                rect6.top = i4 + i5;
                this.mDrawToRect.top += i5;
            }
            if (rect6.bottom > arDkBitmap.getHeight()) {
                int height = this.mRenderToRect.bottom - arDkBitmap.getHeight();
                this.mRenderToRect.bottom -= height;
                this.mDrawToRect.bottom -= height;
            }
            this.renderRect.set(this.mDrawToRect);
            this.renderScale = this.mScale;
            Rect rect7 = this.mRenderToRect;
            this.mBitmapRender = arDkBitmap.createBitmap(rect7.left, rect7.top, rect7.right, rect7.bottom);
            boolean z = ArDkLib.getAppConfigOptions().mSettingsBundle.getBoolean("InvertContentInDarkModeKey", false) && ArDkLib.isNightMode(getContext());
            setOrigin();
            ArDkPage arDkPage = this.mPage;
            int i6 = this.mLayer;
            double d = ((double) this.mScale) * this.mZoom;
            PointF pointF = this.mRenderOrigin;
            this.mRender = arDkPage.renderLayerAtZoomWithAlpha(i6, d, (double) pointF.x, (double) pointF.y, this.mBitmapRender, (ArDkBitmap) null, new SORenderListener() {
                public void progress(int i) {
                    Bitmap bitmap;
                    DocPageView docPageView = DocPageView.this;
                    if (!docPageView.mFinished) {
                        docPageView.stopRender();
                        int i2 = 0;
                        if (i == 0) {
                            DocPageView docPageView2 = DocPageView.this;
                            docPageView2.mBitmapDrawHold = docPageView2.mBitmapRender;
                            docPageView2.drawRectHold.set(docPageView2.renderRect);
                            DocPageView docPageView3 = DocPageView.this;
                            docPageView3.drawScaleHold = docPageView3.renderScale;
                            ArDkBitmap arDkBitmap = docPageView3.mBitmapDrawHold;
                            if (docPageView3.valid) {
                                if (arDkBitmap == null || arDkBitmap.getRect() == null || (bitmap = arDkBitmap.getBitmap()) == null || bitmap.isRecycled()) {
                                    i2 = -1;
                                } else {
                                    Rect rect = arDkBitmap.getRect();
                                    int i3 = rect.left + 5;
                                    int i4 = rect.top + 5;
                                    int i5 = rect.right - 5;
                                    int i6 = rect.bottom - 5;
                                    int[] iArr = {arDkBitmap.getBitmap().getPixel(i3, i4), arDkBitmap.getBitmap().getPixel(i5, i4), arDkBitmap.getBitmap().getPixel(i3, i6), arDkBitmap.getBitmap().getPixel(i5, i6)};
                                    int i7 = 0;
                                    int i8 = 0;
                                    int i9 = 0;
                                    int i10 = 0;
                                    while (i2 < 4) {
                                        int i11 = iArr[i2];
                                        i8 += (i11 >> 16) & KotlinVersion.MAX_COMPONENT_VALUE;
                                        i9 += (i11 >> 8) & KotlinVersion.MAX_COMPONENT_VALUE;
                                        i10 += i11 & KotlinVersion.MAX_COMPONENT_VALUE;
                                        i7 += i11 >>> 24;
                                        i2++;
                                    }
                                    i2 = Color.argb(i7 / 4, i8 / 4, i9 / 4, i10 / 4);
                                }
                            }
                            docPageView3.mBackgroundColorHold = i2;
                        } else {
                            System.out.printf("render error %d for page %d  %n", i, DocPageView.this.mPageNum);
                        }
                        sORenderListener2.progress(i);
                    }
                }
            }, true, z);
        }
    }

    public void resetBackground() {
        Context context = getContext();
        int i = R.color.sodk_editor_page_default_bg_color;
        this.mBackgroundColor = ContextCompat.getColor(context, i);
        this.mBackgroundColorHold = ContextCompat.getColor(getContext(), i);
    }

    public void resize(int i, int i2) {
        ArDkPage arDkPage = this.mPage;
        if (arDkPage != null) {
            PointF zoomToFitRect = arDkPage.zoomToFitRect(i, i2);
            double max = Math.max(zoomToFitRect.x, zoomToFitRect.y);
            this.mZoom = max;
            this.mSize = this.mPage.sizeAtZoom(max);
        }
    }

    public void saveInk() {
        ArrayList<InkAnnotation> arrayList = this.mInkAnnots;
        if (arrayList != null) {
            Iterator<InkAnnotation> it = arrayList.iterator();
            InkAnnotation next = it.next();
            ArrayList arrayList2 = new ArrayList(Arrays.asList(next.points()));
            while (it.hasNext()) {
                arrayList2.addAll(new ArrayList(Arrays.asList(it.next().points())));
            }
            if (!arrayList2.isEmpty()) {
                getDoc().createInkAnnotation(getPageNumber(), (SOPoint[]) arrayList2.toArray(new SOPoint[arrayList2.size()]), next.getLineThickness(), next.getLineColor());
            }
        }
        ArrayList<InkAnnotation> arrayList3 = this.mInkAnnots;
        if (arrayList3 != null) {
            arrayList3.clear();
        }
        invalidate();
    }

    public Rect screenRect() {
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        Rect rect = new Rect();
        rect.set(iArr[0], iArr[1], getChildRect().width() + iArr[0], getChildRect().height() + iArr[1]);
        return rect;
    }

    public Point screenToPage(Point point) {
        return screenToPage(point.x, point.y);
    }

    public void selectTopLeft() {
        this.mPage.select(2, 0.0d, 0.0d);
    }

    public ArDkSelectionLimits selectionLimits() {
        if (this.mFinished) {
            return null;
        }
        return this.mPage.selectionLimits();
    }

    public void setCaret(int i, int i2) {
        Point screenToPage = screenToPage(i, i2);
        SOHyperlink objectAtPoint = this.mPage.objectAtPoint((float) screenToPage.x, (float) screenToPage.y);
        if ((objectAtPoint == null || objectAtPoint.url == null) && objectAtPoint.pageNum == -1) {
            this.mPage.select(3, (double) screenToPage.x, (double) screenToPage.y);
        }
    }

    public void setChildRect(Rect rect) {
        this.mChildRect.set(rect);
    }

    public void setClipPath(Path path) {
        this.clipPath = path;
    }

    public void setCurrent(boolean z) {
        if (z != this.isCurrent) {
            this.isCurrent = z;
            invalidate();
        }
    }

    public void setDocView(DocView docView) {
        this.mDocView = docView;
        getDataLeakHandlers();
    }

    public void setInkLineColor(int i) {
        ArrayList<InkAnnotation> arrayList = this.mInkAnnots;
        if (arrayList != null) {
            Iterator<InkAnnotation> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().setLineColor(i);
            }
            invalidate();
        }
    }

    public void setInkLineThickness(float f) {
        ArrayList<InkAnnotation> arrayList = this.mInkAnnots;
        if (arrayList != null) {
            Iterator<InkAnnotation> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().setLineThickness(f);
            }
            invalidate();
        }
    }

    public void setLayer(int i) {
        this.mLayer = i;
    }

    public void setNewScale(float f) {
        this.mScale = f;
    }

    public void setOrigin() {
        PointF pointF = this.mRenderOrigin;
        Rect rect = this.mDrawToRect;
        pointF.set((float) (-rect.left), (float) (-rect.top));
    }

    public void setPageRect() {
        getLocationInWindow(this.screenLoc);
        Rect rect = this.mPageRect;
        int[] iArr = this.screenLoc;
        rect.set(iArr[0], iArr[1], getChildRect().width() + iArr[0], getChildRect().height() + this.screenLoc[1]);
        Rect rect2 = this.mPageRect;
        int i = NUIDocView.OVERSIZE_MARGIN;
        rect2.offset(i, i);
    }

    public void setSelectedBorderColor(int i) {
        this.mSelectedBorderPainter.setColor(i);
    }

    public void setSelectionEnd(Point point) {
        Point screenToPage = screenToPage(point);
        PointF pointF = new PointF((float) screenToPage.x, (float) screenToPage.y);
        this.mPage.select(1, (double) pointF.x, (double) pointF.y);
    }

    public void setSelectionStart(Point point) {
        Point screenToPage = screenToPage(point);
        PointF pointF = new PointF((float) screenToPage.x, (float) screenToPage.y);
        this.mPage.select(0, (double) pointF.x, (double) pointF.y);
    }

    public void setValid(boolean z) {
        ArDkBitmap arDkBitmap;
        if (z != this.valid) {
            this.valid = z;
            if (!z) {
                if (isShown() && (arDkBitmap = this.mBitmapDraw) != null && !arDkBitmap.getBitmap().isRecycled()) {
                    this.lowResScreenSize = Utilities.getScreenSize(getContext());
                    int width = this.mBitmapDraw.getWidth() / 2;
                    int height = this.mBitmapDraw.getHeight() / 2;
                    Rect rect = new Rect(0, 0, width, height);
                    this.lowResBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(this.lowResBitmap);
                    ArDkBitmap arDkBitmap2 = this.mBitmapDraw;
                    canvas.drawBitmap(arDkBitmap2.getBitmap(), arDkBitmap2.getRect(), rect, lowResPainter);
                }
                this.mBitmapDraw = null;
                this.mBitmapDrawHold = null;
                this.mBitmapRender = null;
            } else {
                Bitmap bitmap = this.lowResBitmap;
                if (bitmap != null) {
                    bitmap.recycle();
                }
                this.lowResBitmap = null;
            }
            invalidate();
        }
    }

    public void setupPage(int i, int i2, int i3) {
        if (!isFinished() && this.valid) {
            changePage(i);
            resize(i2, 1);
        }
    }

    public boolean sizeViewToPage() {
        Point point;
        ArDkPage arDkPage = this.mPage;
        if (arDkPage == null || (point = this.mSize) == null) {
            return false;
        }
        int i = point.x;
        int i2 = point.y;
        Point sizeAtZoom = arDkPage.sizeAtZoom(this.mZoom);
        this.mSize = sizeAtZoom;
        if (sizeAtZoom == null) {
            return false;
        }
        if (sizeAtZoom.x == i && sizeAtZoom.y == i2) {
            return false;
        }
        return true;
    }

    public void startDrawInk(float f, float f2, int i, float f3) {
        if (this.mInkAnnots == null) {
            this.mInkAnnots = new ArrayList<>();
        }
        InkAnnotation inkAnnotation = new InkAnnotation(i, f3);
        this.mInkAnnots.add(inkAnnotation);
        inkAnnotation.add(screenToPage(new PointF(f, f2)));
        invalidate();
    }

    public void startRenderPass() {
    }

    public void stopRender() {
        ArDkRender arDkRender = this.mRender;
        if (arDkRender != null) {
            arDkRender.abort();
            this.mRender.destroy();
            this.mRender = null;
        }
    }

    public boolean tryHyperlink(Point point, ExternalLinkListener externalLinkListener) {
        SOHyperlink objectAtPoint = this.mPage.objectAtPoint((float) point.x, (float) point.y);
        if (objectAtPoint == null) {
            return false;
        }
        if (objectAtPoint.url != null) {
            if (this.mDocView.getDocConfigOptions().mSettingsBundle.getBoolean("LaunchUrlEnabledKey", true)) {
                launchHyperLink(objectAtPoint.url);
            }
            return true;
        }
        int i = objectAtPoint.pageNum;
        if (i == -1) {
            return false;
        }
        if (externalLinkListener != null) {
            externalLinkListener.handleExternalLink(i, objectAtPoint.bbox);
        }
        return true;
    }

    public void update(RectF rectF) {
        DocView docView = this.mDocView;
        if ((docView == null || !docView.getUpdatesPaused()) && !this.mFinished && isShown()) {
            ((Activity) getContext()).runOnUiThread(() -> {
                NUIDocView currentNUIDocView = NUIDocView.currentNUIDocView();
                if (currentNUIDocView != null) {
                    currentNUIDocView.triggerRender();
                }
            });
        }
    }

    public Point viewToPage(int i, int i2) {
        double factor = getFactor();
        return new Point((int) (((double) i) / factor), (int) (((double) i2) / factor));
    }

    public Point pageToView(int i, int i2) {
        return new Point(pageToView(i), pageToView(i2));
    }

    public PointF screenToPage(PointF pointF) {
        return new PointF(screenToPage((int) pointF.x, (int) pointF.y));
    }

    public void pageToView(PointF pointF, PointF pointF2) {
        float factor = (float) getFactor();
        pointF2.set(pointF.x * factor, pointF.y * factor);
    }

    public int viewToPage(int i) {
        return (int) (((double) i) / getFactor());
    }

    public Point screenToPage(int i, int i2) {
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        int[] screenToWindow = Utilities.screenToWindow(iArr, getContext());
        int i3 = i2 - screenToWindow[1];
        double factor = getFactor();
        return new Point((int) (((double) (i - screenToWindow[0])) / factor), (int) (((double) i3) / factor));
    }

    public void pageToView(Rect rect, Rect rect2) {
        double factor = getFactor();
        rect2.set((int) (((double) rect.left) * factor), (int) (((double) rect.top) * factor), (int) (((double) rect.right) * factor), (int) (((double) rect.bottom) * factor));
    }

    public Rect pageToScreen(RectF rectF) {
        double factor = getFactor();
        int i = (int) (((double) rectF.bottom) * factor);
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        return new Rect(((int) (((double) rectF.left) * factor)) + iArr[0], ((int) (((double) rectF.top) * factor)) + iArr[1], ((int) (((double) rectF.right) * factor)) + iArr[0], i + iArr[1]);
    }

    public Rect screenToPage(Rect rect) {
        Point screenToPage = screenToPage(rect.left, rect.top);
        Point screenToPage2 = screenToPage(rect.right, rect.bottom);
        return new Rect(screenToPage.x, screenToPage.y, screenToPage2.x, screenToPage2.y);
    }

    public Rect pageToView(RectF rectF) {
        double factor = getFactor();
        Rect rect = new Rect();
        rect.left = (int) Math.round(((double) rectF.left) * factor);
        rect.top = (int) Math.round(((double) rectF.top) * factor);
        rect.right = (int) Math.round(((double) rectF.right) * factor);
        rect.bottom = (int) Math.round(((double) rectF.bottom) * factor);
        return rect;
    }
}
