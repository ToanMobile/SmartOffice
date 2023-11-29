package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import com.artifex.solib.ArDkBitmap;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkPage;
import com.artifex.solib.SOBitmap;
import com.artifex.solib.SOLib;
import com.artifex.solib.SORenderListener;
import java.util.ArrayList;

public class SlideShowPageLayout extends RelativeLayout implements AnimatableView, SlideShowConductorViewManager {
    public int childViewRenderCount = 0;
    public SlideShowConductor conductor;
    public boolean finished = false;
    public SlideAnimationsListener listener = null;
    public ArrayList<AnimationLayerView> mAnimViews = new ArrayList<>();
    public ArDkDoc mDoc;
    public LayerBitmapManager mLayerBitmapMan = null;
    public ArDkBitmap mPageBitmap;
    public SlideShowPageView mPageView;
    public SOLib soLib;

    public SlideShowPageLayout(ArDkDoc arDkDoc, LayerBitmapManager layerBitmapManager, Context context, SOLib sOLib) {
        super(context);
        this.soLib = sOLib;
        setLayoutParams(new LayoutParams(-1, -1));
        this.mDoc = arDkDoc;
        Point realScreenSize = Utilities.getRealScreenSize(getContext());
        int max = Math.max(realScreenSize.x, realScreenSize.y);
        int i = (max * 120) / 100;
        NUIDocView.OVERSIZE_MARGIN = (i - max) / 2;
        this.mPageBitmap = new SOBitmap(i, i);
        SlideShowPageView slideShowPageView = new SlideShowPageView(getContext(), this.mDoc);
        this.mPageView = slideShowPageView;
        addView(slideShowPageView);
        this.mLayerBitmapMan = layerBitmapManager;
    }

    public void add(SlideShowConductorView slideShowConductorView) {
        AnimationLayerView animationLayerView = (AnimationLayerView) slideShowConductorView;
        if (animationLayerView.getParent() == null) {
            addView(animationLayerView);
            invalidate();
        }
    }

    public void animationsCompleted() {
        SlideAnimationsListener slideAnimationsListener = this.listener;
        if (slideAnimationsListener != null) {
            slideAnimationsListener.animationsCompleted(getPageNumber());
        }
    }

    public void animationsRunning() {
        SlideAnimationsListener slideAnimationsListener = this.listener;
        if (slideAnimationsListener != null) {
            slideAnimationsListener.animating(getPageNumber());
        }
    }

    public void animationsStarted() {
        SlideAnimationsListener slideAnimationsListener = this.listener;
        if (slideAnimationsListener != null) {
            slideAnimationsListener.animationsStarted(getPageNumber());
        }
    }

    public void animationsWaiting() {
        SlideAnimationsListener slideAnimationsListener = this.listener;
        if (slideAnimationsListener != null) {
            slideAnimationsListener.animationsWaiting(getPageNumber());
        }
    }

    public synchronized void clearUpAnimTiles(boolean z) {
        if (!this.mAnimViews.isEmpty()) {
            for (int size = this.mAnimViews.size() - 1; size >= 0; size--) {
                AnimationLayerView animationLayerView = this.mAnimViews.get(size);
                removeView(animationLayerView);
                animationLayerView.dispose(z);
                this.mAnimViews.remove(animationLayerView);
            }
            invalidate();
        }
    }

    public void destroyLayer(SlideShowConductorView slideShowConductorView) {
        AnimationLayerView animationLayerView = (AnimationLayerView) slideShowConductorView;
        removeView(animationLayerView);
        animationLayerView.dispose(true);
        this.mAnimViews.remove(animationLayerView);
    }

    public void endRenderPass() {
        SlideShowPageView slideShowPageView;
        if (!this.finished && (slideShowPageView = this.mPageView) != null) {
            slideShowPageView.endRenderPass();
            if (!this.mAnimViews.isEmpty()) {
                for (int i = 0; i < this.mAnimViews.size(); i++) {
                    AnimationLayerView animationLayerView = this.mAnimViews.get(i);
                    if (animationLayerView.getParent() != null) {
                        animationLayerView.endRenderPass();
                    }
                }
            }
        }
    }

    public void finish() {
        Bitmap bitmap;
        SlideShowConductor slideShowConductor = this.conductor;
        if (slideShowConductor != null) {
            slideShowConductor.stop();
        }
        this.conductor = null;
        ArDkBitmap arDkBitmap = this.mPageBitmap;
        if (!(arDkBitmap == null || (bitmap = arDkBitmap.bitmap) == null)) {
            bitmap.recycle();
        }
        this.mPageBitmap = null;
        Runtime.getRuntime().gc();
        SlideShowPageView slideShowPageView = this.mPageView;
        if (slideShowPageView != null) {
            slideShowPageView.finish();
        }
        removeView(this.mPageView);
        this.mPageView = null;
        clearUpAnimTiles(false);
        this.mAnimViews = null;
        this.mLayerBitmapMan = null;
        this.mDoc = null;
        this.soLib = null;
        this.listener = null;
        this.finished = true;
    }

    public Path getClipPath() {
        SlideShowPageView slideShowPageView;
        if (this.finished || (slideShowPageView = this.mPageView) == null) {
            return null;
        }
        Path clipPath = slideShowPageView.getClipPath();
        if (clipPath != null) {
            clipPath.offset(this.mPageView.getX(), this.mPageView.getY());
        }
        return clipPath;
    }

    public ArDkPage getPage() {
        SlideShowPageView slideShowPageView;
        if (this.finished || (slideShowPageView = this.mPageView) == null) {
            return null;
        }
        return slideShowPageView.getPage();
    }

    public int getPageNumber() {
        SlideShowPageView slideShowPageView;
        if (this.finished || (slideShowPageView = this.mPageView) == null) {
            return 0;
        }
        return slideShowPageView.getPageNumber();
    }

    public double getZoomScale() {
        SlideShowPageView slideShowPageView;
        if (this.finished || (slideShowPageView = this.mPageView) == null) {
            return 1.0d;
        }
        return slideShowPageView.getZoomScale();
    }

    public void invalidate() {
        super.invalidate();
        SlideShowPageView slideShowPageView = this.mPageView;
        if (slideShowPageView != null) {
            slideShowPageView.invalidate();
        }
        ArrayList<AnimationLayerView> arrayList = this.mAnimViews;
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int i = 0; i < this.mAnimViews.size(); i++) {
                AnimationLayerView animationLayerView = this.mAnimViews.get(i);
                if (animationLayerView.getParent() != null) {
                    animationLayerView.invalidate();
                }
            }
        }
    }

    public SlideShowConductorView newLayer(ArDkDoc arDkDoc, ArDkPage arDkPage, int i, PointF pointF, RectF rectF) {
        AnimationLayerView animationLayerView = new AnimationLayerView(getContext(), arDkDoc, arDkPage, i, pointF, rectF, this.mLayerBitmapMan);
        animationLayerView.resize(this.mPageView.getWidth(), this.mPageView.getHeight());
        this.mAnimViews.add(animationLayerView);
        return animationLayerView;
    }

    public boolean nextAnim() {
        SlideShowConductor slideShowConductor = this.conductor;
        if (slideShowConductor == null) {
            return false;
        }
        if (!slideShowConductor.getRunning()) {
            startSlideAnimations();
        }
        if (this.conductor.sendEvent(1)) {
            return true;
        }
        return false;
    }

    public boolean prevAnim() {
        return false;
    }

    public void remove(SlideShowConductorView slideShowConductorView) {
        removeView((AnimationLayerView) slideShowConductorView);
        invalidate();
    }

    public void render(final SORenderListener sORenderListener) {
        if (!this.finished && this.mPageView != null && this.childViewRenderCount <= 0) {
            if (!this.mAnimViews.isEmpty()) {
                this.childViewRenderCount = this.mAnimViews.size() + 1;
            } else {
                this.childViewRenderCount = 1;
            }
            this.mPageView.render(this.mPageBitmap, new SORenderListener() {
                public void progress(int i) {
                    SlideShowPageLayout slideShowPageLayout = SlideShowPageLayout.this;
                    int i2 = slideShowPageLayout.childViewRenderCount - 1;
                    slideShowPageLayout.childViewRenderCount = i2;
                    if (i2 == 0) {
                        sORenderListener.progress(i);
                    }
                }
            });
            if (!this.mAnimViews.isEmpty()) {
                for (int i = 0; i < this.mAnimViews.size(); i++) {
                    AnimationLayerView animationLayerView = this.mAnimViews.get(i);
                    if (animationLayerView.getParent() == null) {
                        int i2 = this.childViewRenderCount - 1;
                        this.childViewRenderCount = i2;
                        if (i2 == 0) {
                            sORenderListener.progress(0);
                        }
                    } else {
                        animationLayerView.render(new SORenderListener() {
                            public void progress(int i) {
                                SlideShowPageLayout slideShowPageLayout = SlideShowPageLayout.this;
                                int i2 = slideShowPageLayout.childViewRenderCount - 1;
                                slideShowPageLayout.childViewRenderCount = i2;
                                if (i2 == 0) {
                                    sORenderListener.progress(i);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public void resize(int i, int i2, boolean z) {
        if (!this.finished) {
            resizePageView(i, i2);
            if (z) {
                this.mPageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        SlideShowPageLayout.this.mPageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        SlideShowPageLayout.this.mPageView.startRenderPass();
                        SlideShowPageLayout slideShowPageLayout = SlideShowPageLayout.this;
                        slideShowPageLayout.mPageView.render(slideShowPageLayout.mPageBitmap, new SORenderListener() {
                            public void progress(int i) {
                                if (i == 0) {
                                    SlideShowPageLayout.this.mPageView.endRenderPass();
                                    SlideShowPageLayout.this.mPageView.invalidate();
                                }
                            }
                        });
                    }
                });
                if (!this.mAnimViews.isEmpty()) {
                    for (int i3 = 0; i3 < this.mAnimViews.size(); i3++) {
                        final AnimationLayerView animationLayerView = this.mAnimViews.get(i3);
                        if (animationLayerView.getParent() != null) {
                            animationLayerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(this) {
                                public void onGlobalLayout() {
                                    animationLayerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    animationLayerView.startRenderPass();
                                    animationLayerView.render(new SORenderListener() {
                                        public void progress(int i) {
                                            if (i == 0) {
                                                animationLayerView.endRenderPass();
                                                animationLayerView.invalidate();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public final void resizePageView(int i, int i2) {
        this.mPageView.resize(i, i2);
        Point size = this.mPageView.getSize();
        LayoutParams layoutParams = (LayoutParams) this.mPageView.getLayoutParams();
        layoutParams.width = size.x;
        layoutParams.height = size.y;
        layoutParams.addRule(15, -1);
        layoutParams.addRule(14, -1);
        this.mPageView.setLayoutParams(layoutParams);
        LayoutParams layoutParams2 = new LayoutParams(-2, -2);
        layoutParams2.width = size.x;
        layoutParams2.height = size.y;
        layoutParams2.addRule(15, -1);
        layoutParams2.addRule(14, -1);
        setLayoutParams(layoutParams2);
        if (!this.mAnimViews.isEmpty()) {
            for (int i3 = 0; i3 < this.mAnimViews.size(); i3++) {
                this.mAnimViews.get(i3).resize(i, i2);
            }
        }
    }

    public void setClipPath(Path path) {
        Path path2;
        Path path3;
        if (!this.finished && this.mPageView != null) {
            if (path != null) {
                path2 = new Path(path);
                path2.offset(-this.mPageView.getX(), -this.mPageView.getY());
            } else {
                path2 = null;
            }
            this.mPageView.setClipPath(path2);
            if (!this.mAnimViews.isEmpty()) {
                for (int i = 0; i < this.mAnimViews.size(); i++) {
                    AnimationLayerView animationLayerView = this.mAnimViews.get(i);
                    if (animationLayerView.getParent() != null) {
                        if (path != null) {
                            path3 = new Path(path);
                            path3.offset(-animationLayerView.getX(), -animationLayerView.getY());
                        } else {
                            path3 = null;
                        }
                        animationLayerView.setClipPath(path3);
                    }
                }
            }
        }
    }

    public void setListener(SlideAnimationsListener slideAnimationsListener) {
        this.listener = slideAnimationsListener;
    }

    public void setupPage(int i, int i2, int i3) {
        if (!this.finished) {
            clearUpAnimTiles(true);
            this.childViewRenderCount = 0;
            SlideShowPageView slideShowPageView = this.mPageView;
            if (slideShowPageView != null) {
                slideShowPageView.changePage(i);
                resizePageView(i2, i3);
                if (this.soLib.isAnimationEnabled()) {
                    SlideShowConductor slideShowConductor = new SlideShowConductor(this.mDoc, this.mPageView.getPage(), this);
                    this.conductor = slideShowConductor;
                    slideShowConductor.start();
                    if (this.conductor.pageHasAnimations()) {
                        this.mPageView.setLayer(-1);
                    } else {
                        this.mPageView.setLayer(-2);
                    }
                }
            }
        }
    }

    public void startRenderPass() {
        SlideShowPageView slideShowPageView;
        if (!this.finished && (slideShowPageView = this.mPageView) != null) {
            slideShowPageView.startRenderPass();
            if (!this.mAnimViews.isEmpty()) {
                for (int i = 0; i < this.mAnimViews.size(); i++) {
                    AnimationLayerView animationLayerView = this.mAnimViews.get(i);
                    if (animationLayerView.getParent() != null) {
                        animationLayerView.startRenderPass();
                    }
                }
            }
        }
    }

    public void startSlideAnimations() {
        SlideShowConductor slideShowConductor = this.conductor;
        if (slideShowConductor != null && slideShowConductor.pageHasAnimations()) {
            if (!this.conductor.getRunning()) {
                this.conductor.start();
            }
            this.conductor.sendEvent(0);
        }
    }
}
