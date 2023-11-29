package com.artifex.solib;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import com.artifex.solib.animation.SOAnimationCommand;

public class SOPage extends ArDkPage {
    public volatile boolean discarded = false;
    private long internal;

    public SOPage(long j) {
        this.internal = j;
    }

    private native String getSlideTransitionInternal();

    private native ArDkRender nativeRenderAtZoom(int i, double d, double d2, double d3, Bitmap bitmap, int i2, Bitmap bitmap2, int i3, int i4, int i5, int i6, SORenderListenerInternal sORenderListenerInternal);

    public void destroyPage() {
    }

    public native void discard();

    public void finalize() throws Throwable {
        try {
            if (!this.discarded) {
                Log.w("SOPage.finalize", toString() + " was not yet discarded");
            }
        } finally {
            super.finalize();
        }
    }

    public native SOAnimationCommand[] getAnimations();

    public native float[] getHorizontalRuler();

    public native String getPageTitle();

    public SOTransition getSlideTransition() {
        return new SOTransition(getSlideTransitionInternal());
    }

    public native Point getTopLeftCell();

    public native float[] getVerticalRuler();

    public native SOHyperlink objectAtPoint(float f, float f2);

    public void releasePage() {
        if (!this.discarded) {
            this.discarded = true;
            discard();
            return;
        }
        Log.w("SOPage.releasePage", toString() + " is already discarded");
    }

    public ArDkRender renderLayerAtZoomWithAlpha(int i, double d, double d2, double d3, ArDkBitmap arDkBitmap, ArDkBitmap arDkBitmap2, SORenderListener sORenderListener, boolean z, boolean z2) {
        int i2;
        int i3;
        int i4;
        ArDkBitmap arDkBitmap3 = arDkBitmap;
        ArDkBitmap arDkBitmap4 = arDkBitmap2;
        Rect rect = arDkBitmap3.rect;
        Bitmap bitmap = arDkBitmap3.bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bitmap2 = arDkBitmap4 != null ? arDkBitmap4.bitmap : null;
        int i5 = rect.left;
        if (i5 < 0 || (i2 = rect.top) < 0 || (i3 = rect.right) > width || (i4 = rect.bottom) > height) {
            throw new IllegalArgumentException("Render rectangle out of range");
        }
        final boolean z3 = z;
        final boolean z4 = z2;
        final ArDkBitmap arDkBitmap5 = arDkBitmap;
        final SORenderListener sORenderListener2 = sORenderListener;
        return nativeRenderAtZoom(i, d, d2, d3, bitmap, width, bitmap2, i5, i2, i3 - i5, i4 - i2, new SORenderListenerInternal() {
            public void progress(final int i) {
                if (z3) {
                    if (z4 && i == 0) {
                        ((SOBitmap) arDkBitmap5).invertLuminance();
                    }
                    ArDkLib.runOnUiThread(new Runnable() {
                        public void run() {
                            sORenderListener2.progress(i);
                        }
                    });
                    return;
                }
                sORenderListener2.progress(i);
            }
        });
    }

    public native int select(int i, double d, double d2);

    public native SOSelectionLimits selectionLimits();

    public native void setSelectionLimitsBox(float f, float f2, float f3, float f4);

    public native boolean setTopLeftCell(int i, int i2);

    public native Point sizeAtZoom(double d);

    public native PointF zoomToFitRect(int i, int i2);
}
