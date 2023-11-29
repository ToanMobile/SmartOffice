package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.view.animation.Animation;
import com.artifex.sonui.editor.SlideShowConductor;
import com.artifex.sonui.editor.SlideShowConductorView;

public abstract class FadeAnimation extends Animation implements Animation.AnimationListener {
    public FadeListener mListener = null;
    public int subType = 0;
    public int transitionType = 0;
    public SlideShowConductorView viewToAnim;

    public interface FadeListener {
    }

    public FadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        setDuration((long) i2);
        setAnimationListener(this);
        setRepeatCount(i - 1);
        setRepeatMode(z ? 2 : 1);
        this.viewToAnim = slideShowConductorView;
    }

    public void onAnimationEnd(Animation animation) {
        FadeListener fadeListener = this.mListener;
        if (fadeListener != null) {
            SlideShowConductor.SlideShowConductorFadeTask.this.view.clearAnimation();
            this.mListener = null;
        }
        setAnimationListener((AnimationListener) null);
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }
}
