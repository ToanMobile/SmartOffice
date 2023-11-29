package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import com.artifex.sonui.editor.AnimatableView;
import com.artifex.sonui.editor.animations.TransitionAnimation;

public class ShapeAnimation extends TransitionAnimation {
    public String mDirection;
    public int mHeight;
    public AnimatableView mNewView;
    public AnimatableView mOldView;
    public int mWidth = this.mNewView.getMeasuredWidth();

    static {
        Class<ShapeAnimation> cls = ShapeAnimation.class;
    }

    public ShapeAnimation(String str, View view, View view2, int i) {
        super(i);
        this.mOldView = (AnimatableView) view;
        AnimatableView animatableView = (AnimatableView) view2;
        this.mNewView = animatableView;
        this.mDirection = str;
        this.mHeight = animatableView.getMeasuredHeight();
    }

    public void applyTransformation(float f, Transformation transformation) {
        super.applyTransformation(f, transformation);
        doStep(f);
    }

    public void doStep(float f) {
    }

    public void onAnimationEnd(Animation animation) {
        TransitionAnimationListener transitionAnimationListener = this.mListener;
        if (transitionAnimationListener != null) {
            transitionAnimationListener.done();
            this.mListener = null;
        }
        setAnimationListener((AnimationListener) null);
        this.mOldView.setClipPath((Path) null);
        this.mNewView.setClipPath((Path) null);
        this.mOldView = null;
        this.mNewView = null;
    }
}
