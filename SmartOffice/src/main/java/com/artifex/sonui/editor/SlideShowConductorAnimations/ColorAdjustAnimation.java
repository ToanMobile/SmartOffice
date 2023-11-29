package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class ColorAdjustAnimation implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    public int effect;
    public ColorMatrix hueMatrix = new ColorMatrix();
    public ColorMatrix luminanceMatrix = new ColorMatrix();
    public ColorMatrix matrix = new ColorMatrix();
    public ColorMatrix saturationMatrix = new ColorMatrix();
    public final ValueAnimator valueAnimator;
    public SlideShowConductorView viewToAnim;

    public ColorAdjustAnimation(int i, boolean z, int i2, int i3, SlideShowConductorView slideShowConductorView) {
        int i4 = 2;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{BitmapDescriptorFactory.HUE_RED, 1.0f});
        this.valueAnimator = ofFloat;
        ofFloat.setDuration((long) i2);
        ofFloat.setRepeatCount(i - 1);
        ofFloat.setRepeatMode(!z ? 1 : i4);
        this.effect = i3;
        this.viewToAnim = slideShowConductorView;
        ofFloat.addUpdateListener(this);
        ofFloat.addListener(this);
    }

    public void onAnimationCancel(Animator animator) {
    }

    public void onAnimationEnd(Animator animator) {
        this.viewToAnim.concatColorAdjustmentMatrix(this.matrix);
        this.valueAnimator.removeUpdateListener(this);
        this.valueAnimator.removeListener(this);
    }

    public void onAnimationRepeat(Animator animator) {
    }

    public void onAnimationStart(Animator animator) {
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator2) {
        this.hueMatrix.reset();
        this.saturationMatrix.reset();
        this.luminanceMatrix.reset();
        this.matrix.reset();
        int i = this.effect;
        if (i == 0) {
            float animatedFraction = valueAnimator2.getAnimatedFraction();
            float f = 1.0f - animatedFraction;
            this.hueMatrix.set(new float[]{f, 0.0f, animatedFraction, 0.0f, 0.0f, animatedFraction, f, 0.0f, 0.0f, 0.0f, 0.0f, animatedFraction, f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        } else if (i == 1) {
            float animatedFraction2 = valueAnimator2.getAnimatedFraction();
            float f2 = 1.0f - animatedFraction2;
            this.hueMatrix.set(new float[]{f2, animatedFraction2, 0.0f, 0.0f, 0.0f, 0.0f, f2, animatedFraction2, 0.0f, 0.0f, animatedFraction2, 0.0f, f2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        } else if (i == 2) {
            float animatedFraction3 = valueAnimator2.getAnimatedFraction();
            float f3 = 1.0f - animatedFraction3;
            float f4 = animatedFraction3 * 0.5f;
            this.saturationMatrix.setSaturation(2.0f);
            this.hueMatrix.set(new float[]{f3, f4, f4, 0.0f, 0.0f, f4, f3, f4, 0.0f, 0.0f, f4, f4, f3, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        } else if (i == 3) {
            this.saturationMatrix.setSaturation(1.0f - (valueAnimator2.getAnimatedFraction() * 0.125f));
            float animatedFraction4 = 1.0f - (valueAnimator2.getAnimatedFraction() * 0.25f);
            this.luminanceMatrix.setScale(animatedFraction4, animatedFraction4, animatedFraction4, 1.0f);
        } else if (i == 4) {
            this.saturationMatrix.setSaturation(1.0f - (valueAnimator2.getAnimatedFraction() * 0.7f));
        } else if (i == 5) {
            this.saturationMatrix.setSaturation((valueAnimator2.getAnimatedFraction() * 0.125f) + 1.0f);
            float animatedFraction5 = (valueAnimator2.getAnimatedFraction() * 0.25f) + 1.0f;
            this.luminanceMatrix.setScale(animatedFraction5, animatedFraction5, animatedFraction5, 1.0f);
        }
        this.matrix.postConcat(this.hueMatrix);
        this.matrix.postConcat(this.saturationMatrix);
        this.matrix.postConcat(this.luminanceMatrix);
        this.viewToAnim.applyColorAdjustmentMatrix(this.matrix);
        this.viewToAnim.invalidate();
    }
}
