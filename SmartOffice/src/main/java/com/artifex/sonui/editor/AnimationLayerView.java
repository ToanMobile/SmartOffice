package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.artifex.solib.ArDkBitmap;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ArDkPage;
import com.artifex.solib.ArDkRender;
import com.artifex.solib.SOBitmap;
import com.artifex.solib.SOPage;
import com.artifex.solib.SORenderListener;

public class AnimationLayerView extends View implements AnimatableView, SlideShowConductorView {
    public static Paint lowResPainter;
    public SOBitmap alphaBM = null;
    public Path clipPath = null;
    public ColorMatrix colorMatrix = new ColorMatrix();
    public SOBitmap colourBM = null;
    public final Rect drawRect = new Rect();
    public final Rect drawRectHold = new Rect();
    public double drawScale;
    public double drawScaleHold;
    public Bitmap lowResBitmap = null;
    public Point lowResScreenSize = null;
    public SOBitmap mBitmapDraw = null;
    public SOBitmap mBitmapDrawHold = null;
    public SOBitmap mBitmapRender = null;
    public final Paint mBlankPainter;
    public LayerBitmapManager mBmManager;
    public ArDkDoc mDoc;
    public final Rect mDstRect = new Rect();
    public boolean mFinished = false;
    public final int mLayerId;
    public ArDkPage mPage;
    public final Paint mPainter;
    public PointF mPosition;
    public ArDkRender mRender = null;
    public final PointF mRenderOrigin;
    public Rect mRenderToRect;
    public Point mSize;
    public final Rect mSrcRect = new Rect();
    public double mZoomScale = 1.0d;
    public int maxSideSize;
    public final Rect renderRect = new Rect();
    public double renderScale;
    public SOBitmap theBitmap = null;
    public int tileSize;
    public boolean valid = true;

    public class TileRenderer implements SORenderListener {
        public int across = 0;
        public int down = 0;
        public final SORenderListener listener;
        public int tilesAcross;
        public int tilesDown;

        public TileRenderer(int i, int i2, SORenderListener sORenderListener) {
            this.tilesAcross = i;
            this.tilesDown = i2;
            this.listener = sORenderListener;
        }

        public void progress(int i) {
            if (i != 0) {
                AnimationLayerView.this.clearContent();
                this.listener.progress(i);
                return;
            }
            int i2 = this.across + 1;
            this.across = i2;
            if (i2 == this.tilesAcross) {
                this.down++;
                this.across = 0;
            }
            int i3 = this.down;
            if (i3 == this.tilesDown) {
                AnimationLayerView animationLayerView = AnimationLayerView.this;
                animationLayerView.mBitmapDrawHold = animationLayerView.mBitmapRender;
                animationLayerView.drawRectHold.set(animationLayerView.renderRect);
                AnimationLayerView animationLayerView2 = AnimationLayerView.this;
                animationLayerView2.drawScaleHold = animationLayerView2.renderScale;
                this.listener.progress(i);
                return;
            }
            AnimationLayerView.access$400(AnimationLayerView.this, this.across, i3, this);
        }
    }

    public AnimationLayerView(Context context, ArDkDoc arDkDoc, ArDkPage arDkPage, int i, PointF pointF, RectF rectF, LayerBitmapManager layerBitmapManager) {
        super(context);
        if (arDkDoc == null || arDkPage == null || pointF == null || rectF == null || layerBitmapManager == null) {
            throw new IllegalArgumentException("Constructor parameter(s) unexpectedly null");
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        this.mDoc = arDkDoc;
        this.mPage = arDkPage;
        this.mLayerId = i;
        this.mBmManager = layerBitmapManager;
        this.mPainter = new Paint();
        Paint paint = new Paint();
        this.mBlankPainter = paint;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0);
        if (lowResPainter == null) {
            Paint paint2 = new Paint();
            lowResPainter = paint2;
            paint2.setAntiAlias(true);
            lowResPainter.setFilterBitmap(true);
            lowResPainter.setDither(true);
        }
        this.mRenderToRect = new Rect((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
        this.mRenderOrigin = pointF;
        Point point = new Point((int) rectF.width(), (int) rectF.height());
        this.mSize = point;
        layoutParams.width = point.x;
        layoutParams.height = point.y;
        setLayoutParams(layoutParams);
        PointF pointF2 = new PointF(-pointF.x, -pointF.y);
        this.mPosition = pointF2;
        setX(pointF2.x);
        setY(this.mPosition.y);
    }

    public static void access$400(AnimationLayerView animationLayerView, int i, int i2, SORenderListener sORenderListener) {
        AnimationLayerView animationLayerView2 = animationLayerView;
        final SORenderListener sORenderListener2 = sORenderListener;
        synchronized (animationLayerView) {
            int i3 = animationLayerView2.tileSize;
            int i4 = i * i3;
            int i5 = i2 * i3;
            int i6 = i4 + i3;
            int i7 = i3 + i5;
            SOBitmap sOBitmap = animationLayerView2.mBitmapRender;
            Rect rect = sOBitmap.rect;
            int i8 = rect.right;
            int i9 = i6 > i8 ? i8 : i6;
            int i10 = rect.bottom;
            final SOBitmap sOBitmap2 = new SOBitmap(sOBitmap, i4, i5, i9, i7 > i10 ? i10 : i7);
            SOBitmap sOBitmap3 = animationLayerView2.mBmManager.get(animationLayerView2.tileSize, ArDkBitmap.Type.RGBA8888);
            if (sOBitmap3 != null) {
                animationLayerView2.colourBM = new SOBitmap(sOBitmap3, 0, 0, sOBitmap2.getWidth(), sOBitmap2.getHeight());
            } else {
                animationLayerView2.colourBM = null;
            }
            SOBitmap sOBitmap4 = animationLayerView2.mBmManager.get(animationLayerView2.tileSize, ArDkBitmap.Type.A8);
            if (sOBitmap4 != null) {
                animationLayerView2.alphaBM = new SOBitmap(sOBitmap4, 0, 0, sOBitmap2.getWidth(), sOBitmap2.getHeight());
            } else {
                animationLayerView2.alphaBM = null;
            }
            SOBitmap sOBitmap5 = animationLayerView2.colourBM;
            if (sOBitmap5 == null || animationLayerView2.alphaBM == null) {
                if (sOBitmap5 != null) {
                    animationLayerView2.mBmManager.addToCache(sOBitmap5);
                    animationLayerView2.colourBM = null;
                }
                SOBitmap sOBitmap6 = animationLayerView2.alphaBM;
                if (sOBitmap6 != null) {
                    animationLayerView2.mBmManager.addToCache(sOBitmap6);
                    animationLayerView2.alphaBM = null;
                }
                ((TileRenderer) sORenderListener2).progress(7);
            } else {
                PointF pointF = animationLayerView2.mRenderOrigin;
                double d = animationLayerView2.mZoomScale;
                PointF pointF2 = new PointF((float) ((((double) pointF.x) * d) - ((double) i4)), (float) ((((double) pointF.y) * d) - ((double) i5)));
                animationLayerView2.mRender = animationLayerView2.mPage.renderLayerAtZoomWithAlpha(animationLayerView2.mLayerId, animationLayerView2.mZoomScale, (double) pointF2.x, (double) pointF2.y, animationLayerView2.colourBM, animationLayerView2.alphaBM, new SORenderListener() {
                    public void progress(int i) {
                        SOBitmap sOBitmap;
                        AnimationLayerView animationLayerView = AnimationLayerView.this;
                        if (!animationLayerView.mFinished) {
                            animationLayerView.stopRender();
                            if (i == 0) {
                                AnimationLayerView animationLayerView2 = AnimationLayerView.this;
                                SOBitmap sOBitmap2 = animationLayerView2.colourBM;
                                if (!(sOBitmap2 == null || (sOBitmap = animationLayerView2.alphaBM) == null)) {
                                    sOBitmap2.alphaCombine(sOBitmap2, sOBitmap);
                                }
                            } else {
                                System.out.printf("render error %d for page %s\n", new Object[]{Integer.valueOf(i), ((SOPage) AnimationLayerView.this.mPage).getPageTitle()});
                            }
                            AnimationLayerView animationLayerView3 = AnimationLayerView.this;
                            animationLayerView3.mBmManager.addToCache(animationLayerView3.colourBM);
                            AnimationLayerView animationLayerView4 = AnimationLayerView.this;
                            animationLayerView4.mBmManager.addToCache(animationLayerView4.alphaBM);
                            AnimationLayerView animationLayerView5 = AnimationLayerView.this;
                            animationLayerView5.colourBM = null;
                            animationLayerView5.alphaBM = null;
                            sORenderListener2.progress(i);
                        }
                    }
                }, true, ArDkLib.getAppConfigOptions().mSettingsBundle.getBoolean("InvertContentInDarkModeKey", false) && ArDkLib.isNightMode(animationLayerView.getContext()));
            }
        }
    }

    public void applyColorAdjustmentMatrix(ColorMatrix colorMatrix2) {
        ColorMatrix colorMatrix3 = new ColorMatrix(this.colorMatrix);
        colorMatrix3.postConcat(colorMatrix2);
        this.mPainter.setColorFilter(new ColorMatrixColorFilter(colorMatrix3));
    }

    public void begin() {
    }

    public void clearContent() {
        this.mBitmapDraw = null;
        invalidate();
    }

    public void commit() {
    }

    public void concatColorAdjustmentMatrix(ColorMatrix colorMatrix2) {
        this.colorMatrix.postConcat(colorMatrix2);
        this.mPainter.setColorFilter(new ColorMatrixColorFilter(this.colorMatrix));
    }

    public void dispose(boolean z) {
        this.mFinished = true;
        stopRender();
        SOBitmap sOBitmap = this.theBitmap;
        if (sOBitmap != null) {
            sOBitmap.dispose();
            this.theBitmap = null;
        }
        SOBitmap sOBitmap2 = this.colourBM;
        if (sOBitmap2 != null) {
            if (z) {
                this.mBmManager.addToCache(sOBitmap2);
            } else {
                sOBitmap2.dispose();
            }
        }
        this.colourBM = null;
        SOBitmap sOBitmap3 = this.alphaBM;
        if (sOBitmap3 != null) {
            if (z) {
                this.mBmManager.addToCache(sOBitmap3);
            } else {
                sOBitmap3.dispose();
            }
        }
        this.alphaBM = null;
        this.mBitmapDraw = null;
        this.mBitmapDrawHold = null;
        this.mBitmapRender = null;
        this.mPage = null;
        this.mDoc = null;
        this.mBmManager = null;
        Runtime.getRuntime().gc();
    }

    public void endRenderPass() {
        this.mBitmapDraw = this.mBitmapDrawHold;
        this.drawRect.set(this.drawRectHold);
        this.drawScale = this.drawScaleHold;
        invalidate();
    }

    public Path getClipPath() {
        return this.clipPath;
    }

    public ArDkDoc getDoc() {
        return this.mDoc;
    }

    public ArDkPage getPage() {
        return this.mPage;
    }

    public Point getSize() {
        return this.mSize;
    }

    public double getZoomScale() {
        return this.mZoomScale;
    }

    public boolean isFinished() {
        return this.mFinished;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void onDraw(Canvas canvas) {
        Rect rect;
        if (this.mFinished || !isShown()) {
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
            this.mBlankPainter.setColor(0);
            Rect rect3 = new Rect();
            getLocalVisibleRect(rect3);
            canvas.drawRect(rect3, this.mBlankPainter);
            if (this.clipPath != null) {
                canvas.save();
            }
            SOBitmap sOBitmap = this.mBitmapDraw;
            if (sOBitmap != null && !sOBitmap.bitmap.isRecycled()) {
                this.mSrcRect.set(sOBitmap.rect);
                this.mDstRect.set(this.drawRect);
                double d = this.drawScale;
                double d2 = this.mZoomScale;
                if (d != d2) {
                    Rect rect4 = this.mDstRect;
                    rect4.left = (int) ((d2 / d) * ((double) rect4.left));
                    rect4.top = (int) ((d2 / d) * ((double) rect4.top));
                    rect4.right = (int) ((d2 / d) * ((double) rect4.right));
                    rect4.bottom = (int) ((d2 / d) * ((double) rect4.bottom));
                }
                Path path = this.clipPath;
                if (path != null) {
                    canvas.clipPath(path);
                }
                canvas.drawBitmap(sOBitmap.bitmap, this.mSrcRect, this.mDstRect, this.mPainter);
                if (this.clipPath != null) {
                    canvas.restore();
                }
            }
        }
    }

    public void render(SORenderListener sORenderListener) {
        if (!this.mFinished) {
            if (sORenderListener != null) {
                Rect rect = new Rect();
                Point point = this.mSize;
                rect.set(0, 0, point.x, point.y);
                SOBitmap sOBitmap = this.theBitmap;
                if (sOBitmap != null) {
                    this.mRenderToRect.set(rect);
                    this.renderRect.set(this.mRenderToRect);
                    this.renderScale = this.mZoomScale;
                    this.mBitmapDraw = null;
                    Rect rect2 = this.mRenderToRect;
                    this.mBitmapRender = new SOBitmap(sOBitmap, rect2.left, rect2.top, rect2.right, rect2.bottom);
                    TileRenderer tileRenderer = new TileRenderer((int) Math.ceil(((double) rect.width()) / ((double) this.tileSize)), (int) Math.ceil(((double) rect.height()) / ((double) this.tileSize)), sORenderListener);
                    access$400(this, tileRenderer.across, tileRenderer.down, tileRenderer);
                    return;
                }
                return;
            }
            throw new IllegalArgumentException("Render listener cannot be null");
        }
    }

    public void resize(int i, int i2) {
        ArDkPage arDkPage = this.mPage;
        if (arDkPage != null) {
            PointF zoomToFitRect = arDkPage.zoomToFitRect(i, i2);
            resize((double) Math.min(zoomToFitRect.x, zoomToFitRect.y));
        }
    }

    public void setClipPath(Path path) {
        this.clipPath = path;
    }

    public void setOpacity(float f) {
        setAlpha(f);
    }

    public void setPosition(PointF pointF) {
        PointF pointF2 = this.mPosition;
        double d = this.mZoomScale;
        pointF2.set((float) (((double) pointF.x) * d), (float) (((double) pointF.y) * d));
        setX(this.mPosition.x);
        setY(this.mPosition.y);
    }

    public void setRotation(float f) {
        super.setRotation((float) Math.toDegrees((double) f));
    }

    public void setScale(float f, float f2) {
        setScaleX(f);
        setScaleY(f2);
    }

    public void setTransform(Matrix matrix) {
    }

    public void setValid(boolean z) {
        if (z != this.valid) {
            this.valid = z;
            if (!z) {
                SOBitmap sOBitmap = this.mBitmapDraw;
                if (sOBitmap != null && !sOBitmap.bitmap.isRecycled()) {
                    this.lowResScreenSize = Utilities.getScreenSize(getContext());
                    int width = this.mBitmapDraw.getWidth() / 2;
                    int height = this.mBitmapDraw.getHeight() / 2;
                    Rect rect = new Rect(0, 0, width, height);
                    this.lowResBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(this.lowResBitmap);
                    SOBitmap sOBitmap2 = this.mBitmapDraw;
                    canvas.drawBitmap(sOBitmap2.bitmap, sOBitmap2.rect, rect, lowResPainter);
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

    public void setVisibility(boolean z) {
        setVisibility(z ? 0 : 4);
    }

    public void setZPosition(int i) {
        setZ((float) i);
    }

    public void setZoomScale(double d) {
        resize(d);
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

    public final void resize(double d) {
        double d2 = this.mZoomScale;
        if (d2 != d || this.theBitmap == null) {
            Point point = this.mSize;
            point.x = (int) ((double) Math.round((((double) this.mSize.x) * d) / d2));
            point.y = (int) ((double) Math.round((((double) this.mSize.y) * d) / d2));
            PointF pointF = this.mPosition;
            float round = (float) ((double) Math.round((((double) this.mPosition.x) * d) / d2));
            pointF.x = round;
            float round2 = (float) ((double) Math.round((((double) this.mPosition.y) * d) / d2));
            pointF.y = round2;
            setX(round);
            setY(round2);
            this.mZoomScale = d;
            if (this.theBitmap == null) {
                Point realScreenSize = Utilities.getRealScreenSize(getContext());
                int max = Math.max(realScreenSize.x, realScreenSize.y);
                this.tileSize = max / 12;
                int i = realScreenSize.x;
                float f = max == i ? ((float) i) / ((float) realScreenSize.y) : ((float) realScreenSize.y) / ((float) i);
                Point point2 = this.mSize;
                int max2 = (int) (((float) Math.max(point2.x, point2.y)) * f);
                this.maxSideSize = max2;
                if (max2 > max) {
                    this.maxSideSize = max;
                }
                int i2 = this.maxSideSize;
                if (i2 > 0) {
                    this.theBitmap = this.mBmManager.get(i2, ArDkBitmap.Type.RGBA8888);
                }
            }
            SOBitmap sOBitmap = this.theBitmap;
            if (sOBitmap != null) {
                if (this.mSize.x > sOBitmap.bitmap.getWidth()) {
                    this.mSize.x = this.theBitmap.bitmap.getWidth();
                }
                if (this.mSize.y > this.theBitmap.bitmap.getHeight()) {
                    this.mSize.y = this.theBitmap.bitmap.getHeight();
                }
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                Point point3 = this.mSize;
                layoutParams.width = point3.x;
                layoutParams.height = point3.y;
                setLayoutParams(layoutParams);
            }
        }
    }

    public void render() {
        startRenderPass();
        render(new SORenderListener() {
            public void progress(int i) {
                if (i == 0) {
                    AnimationLayerView.this.endRenderPass();
                }
            }
        });
    }

    public void dispose() {
        dispose(true);
    }
}
