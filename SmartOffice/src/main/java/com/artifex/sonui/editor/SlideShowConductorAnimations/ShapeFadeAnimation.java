package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import com.artifex.sonui.editor.SlideShowConductorView;

public abstract class ShapeFadeAnimation extends FadeAnimation {
    public int mHeight = 0;
    public int mWidth = 0;

    static {
        Class<ShapeFadeAnimation> cls = ShapeFadeAnimation.class;
    }

    public ShapeFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
        SlideShowConductorView slideShowConductorView2 = this.viewToAnim;
        if (slideShowConductorView2 != null) {
            this.mHeight = slideShowConductorView2.getSize().y;
            this.mWidth = this.viewToAnim.getSize().x;
        }
    }

    public void applyTransformation(float f, Transformation transformation) {
        super.applyTransformation(f, transformation);
        doStep(f);
    }

    public void doStep(float f) {
    }

    public void onAnimationEnd(Animation animation) {
        super.onAnimationEnd(animation);
        this.viewToAnim.setClipPath((Path) null);
        this.viewToAnim = null;
    }
}
