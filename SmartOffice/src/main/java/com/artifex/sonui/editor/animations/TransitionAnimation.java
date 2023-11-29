package com.artifex.sonui.editor.animations;

import android.view.animation.Animation;

public class TransitionAnimation extends Animation implements Animation.AnimationListener {
    public TransitionAnimationListener mListener = null;

    public interface TransitionAnimationListener {
        void done();
    }

    public TransitionAnimation(int i) {
        setDuration((long) i);
        setAnimationListener(this);
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }
}
