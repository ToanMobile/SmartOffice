package com.artifex.sonui.editor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.HorizontalScrollView;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class SOHorizontalScrollView extends HorizontalScrollView implements View.OnTouchListener {
    public int mAnimateDistance;
    public int mAnimationAmount;
    public ViewPropertyAnimator mAnimator;
    public int mIndicatorMargin = Utilities.convertDpToPixel(5.0f);
    public float mIndicatorWidthInches = 0.2f;
    public int mIndicatorWidthPixels;
    public Drawable mLeftIndicator;
    public Drawable mRightIndicator;
    public boolean mShowLeftIndicator = false;
    public boolean mShowRightIndicator = false;

    public SOHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnTouchListener(this);
        this.mLeftIndicator = ContextCompat.getDrawable(context, R.drawable.sodk_editor_scrollind_left);
        this.mRightIndicator = ContextCompat.getDrawable(context, R.drawable.sodk_editor_scrollind_right);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        this.mIndicatorWidthPixels = (int) (((float) displayMetrics.densityDpi) * this.mIndicatorWidthInches);
    }

    private int getChildWidth() {
        View childAt = getChildAt(0);
        int width = childAt.getWidth();
        if (!Utilities.isPhoneDevice(getContext())) {
            return width;
        }
        return (int) (((float) width) * childAt.getScaleX());
    }

    private int getParentWidth() {
        return ((View) getParent()).getWidth();
    }

    public final void doAnimate() {
        ViewPropertyAnimator viewPropertyAnimator = this.mAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.translationX((float) this.mAnimateDistance).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    SOHorizontalScrollView sOHorizontalScrollView = SOHorizontalScrollView.this;
                    sOHorizontalScrollView.mAnimateDistance = (-sOHorizontalScrollView.mAnimationAmount) - sOHorizontalScrollView.mAnimateDistance;
                    sOHorizontalScrollView.doAnimate();
                }
            });
        }
    }

    public boolean mayAnimate() {
        return getChildWidth() > getParentWidth();
    }

    public void onDrawForeground(Canvas canvas) {
        if (this.mAnimator == null) {
            updateIndicatorVisibility();
            int height = getHeight();
            int width = ((View) getParent()).getWidth();
            int scrollX = getScrollX();
            if (this.mShowLeftIndicator) {
                this.mLeftIndicator.setBounds(new Rect(scrollX, 0, this.mIndicatorWidthPixels + scrollX, height));
                this.mLeftIndicator.draw(canvas);
            }
            if (this.mShowRightIndicator) {
                int i = width + scrollX;
                this.mRightIndicator.setBounds(new Rect(i - this.mIndicatorWidthPixels, 0, i, height));
                this.mRightIndicator.draw(canvas);
            }
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        updateIndicatorVisibility();
        return false;
    }

    public void startAnimate() {
        int childWidth = getChildWidth() - getParentWidth();
        this.mAnimationAmount = childWidth;
        this.mAnimationAmount = Math.min(childWidth, 100); //Math.min(childWidth, LogSeverity.ERROR_VALUE);
        this.mAnimator = animate();
        this.mAnimateDistance = -this.mAnimationAmount;
        setTranslationX(BitmapDescriptorFactory.HUE_RED);
        doAnimate();
    }

    public void stopAnimate() {
        ViewPropertyAnimator viewPropertyAnimator = this.mAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        this.mAnimator = null;
        setTranslationX(BitmapDescriptorFactory.HUE_RED);
    }

    public final void updateIndicatorVisibility() {
        int childWidth = getChildWidth();
        int scrollX = getScrollX();
        int parentWidth = getParentWidth();
        int i = this.mIndicatorMargin;
        boolean z = true;
        this.mShowLeftIndicator = scrollX >= i;
        if (parentWidth + scrollX + i >= childWidth) {
            z = false;
        }
        this.mShowRightIndicator = z;
    }
}
