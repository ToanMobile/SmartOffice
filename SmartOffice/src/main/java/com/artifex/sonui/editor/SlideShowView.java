package com.artifex.sonui.editor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SOLib;
import com.artifex.solib.SOPage;
import com.artifex.solib.SORenderListener;
import com.artifex.solib.SOTransition;
import com.artifex.sonui.editor.SlideShowConductorAnimations.DissolveFadeAnimation$$ExternalSyntheticOutline0;
import com.artifex.sonui.editor.animations.BlindsAnimation;
import com.artifex.sonui.editor.animations.CheckerAnimation;
import com.artifex.sonui.editor.animations.CircleAnimation;
import com.artifex.sonui.editor.animations.DiamondAnimation;
import com.artifex.sonui.editor.animations.DissolveAnimation;
import com.artifex.sonui.editor.animations.PlusAnimation;
import com.artifex.sonui.editor.animations.RandomBarsAnimation;
import com.artifex.sonui.editor.animations.SplitAnimation;
import com.artifex.sonui.editor.animations.StripsAnimation;
import com.artifex.sonui.editor.animations.TransitionAnimation;
import com.artifex.sonui.editor.animations.WedgeAnimation;
import com.artifex.sonui.editor.animations.WheelAnimation;
import com.artifex.sonui.editor.animations.WipeAnimation;
import com.artifex.sonui.editor.animations.ZoomAnimation;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.logging.type.LogSeverity;
import java.util.Objects;

public class SlideShowView extends RelativeLayout implements MemoryInfoProvider, ComponentCallbacks2, SlideAnimationsListener {
    public static ArDkDoc useDoc;
    public static SOLib useLib;
    public String TAG = "SlideShowView";
    public int animationCounter = 0;
    public SlideShowViewListener listener = null;
    public int mCurrentView = 0;
    public ArDkDoc mDoc;
    public int mLastView = 1;
    public LayerBitmapManager mLayerBitmapMan = null;
    public SOLib mLib;
    public int mPageIndex = -1;
    public final SlideShowPageLayout[] mPageViews = {null, null};
    public RelativeLayout mSlideParent;
    public boolean trimmingMemory = false;
    public ViewPropertyAnimator vpa = null;

    public SlideShowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public static /* synthetic */ int access$610(SlideShowView slideShowView) {
        int i = slideShowView.animationCounter;
        slideShowView.animationCounter = i - 1;
        return i;
    }

    public static void access$700(SlideShowView slideShowView, SlideShowPageLayout slideShowPageLayout, SlideShowPageLayout slideShowPageLayout2) {
        Objects.requireNonNull(slideShowView);
        slideShowPageLayout.clearUpAnimTiles(true);
        slideShowPageLayout2.startSlideAnimations();
    }

    public static void setDoc(ArDkDoc arDkDoc) {
        useDoc = arDkDoc;
    }

    public static void setLib(SOLib sOLib) {
        useLib = sOLib;
    }

    public final ClippedImageView addImageViewCopy(View view, Bitmap bitmap) {
        ClippedImageView clippedImageView = new ClippedImageView(getContext());
        clippedImageView.setImageBitmap(bitmap);
        this.mSlideParent.addView(clippedImageView);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.width = view.getWidth();
        layoutParams.height = view.getHeight();
        layoutParams.addRule(15, -1);
        clippedImageView.setLayoutParams(layoutParams);
        return clippedImageView;
    }

    public final void alphaFade(final SlideShowPageLayout slideShowPageLayout, final SlideShowPageLayout slideShowPageLayout2, int i) {
        this.animationCounter++;
        slideShowPageLayout2.setAlpha(BitmapDescriptorFactory.HUE_RED);
        slideShowPageLayout2.setVisibility(View.VISIBLE);
        setViewPropertyAnimator(slideShowPageLayout2).alpha(1.0f).setDuration((long) i).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                slideShowPageLayout.setVisibility(4);
                SlideShowView.access$610(SlideShowView.this);
                SlideShowView.access$700(SlideShowView.this, slideShowPageLayout, slideShowPageLayout2);
            }
        });
    }

    public void animating(int i) {
        SlideShowViewListener slideShowViewListener = this.listener;
        if (slideShowViewListener != null) {
            slideShowViewListener.slideAnimating(i);
        }
    }

    public void animationsCompleted(int i) {
        SlideShowViewListener slideShowViewListener = this.listener;
        if (slideShowViewListener != null) {
            slideShowViewListener.slideAnimationsCompleted(i);
        }
    }

    public void animationsStarted(int i) {
        SlideShowViewListener slideShowViewListener = this.listener;
        if (slideShowViewListener != null) {
            slideShowViewListener.slideAnimationsStarted(i);
        }
    }

    public void animationsWaiting(int i) {
        SlideShowViewListener slideShowViewListener = this.listener;
        if (slideShowViewListener != null) {
            slideShowViewListener.slideAnimationsWaiting(i);
        }
    }

    public boolean checkMemoryAvailable(long j) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) getContext().getSystemService("activity")).getMemoryInfo(memoryInfo);
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory() + (runtime.maxMemory() - runtime.totalMemory());
        if (freeMemory > memoryInfo.totalMem / 25) {
            this.trimmingMemory = false;
        }
        if (this.trimmingMemory || freeMemory <= 5242880 || memoryInfo.lowMemory || freeMemory <= (j * 5) / 4) {
            return false;
        }
        return true;
    }

    public void dispose() {
        this.mDoc = null;
        this.mLib = null;
        this.listener = null;
        int i = 0;
        while (true) {
            SlideShowPageLayout[] slideShowPageLayoutArr = this.mPageViews;
            if (i >= slideShowPageLayoutArr.length) {
                break;
            }
            if (slideShowPageLayoutArr[i] != null) {
                slideShowPageLayoutArr[i].finish();
                this.mPageViews[i] = null;
            }
            i++;
        }
        this.mSlideParent.removeAllViews();
        this.mSlideParent = null;
        this.mLayerBitmapMan.setMemoryInfoProvider((MemoryInfoProvider) null);
        this.mLayerBitmapMan.dispose();
        this.mLayerBitmapMan = null;
        ViewPropertyAnimator viewPropertyAnimator = this.vpa;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.setListener((Animator.AnimatorListener) null);
            this.vpa.cancel();
            this.vpa = null;
        }
    }

    public final void doSlide() {
        SlideShowViewListener slideShowViewListener = this.listener;
        if (slideShowViewListener != null) {
            slideShowViewListener.slideStarted(this.mPageIndex);
        }
        int i = 1 - this.mCurrentView;
        this.mCurrentView = i;
        this.mLastView = 1 - this.mLastView;
        this.mSlideParent.bringChildToFront(this.mPageViews[i]);
        this.mPageViews[this.mCurrentView].setVisibility(4);
        this.mPageViews[this.mCurrentView].setupPage(this.mPageIndex, this.mSlideParent.getWidth(), this.mSlideParent.getHeight());
        this.mPageViews[this.mCurrentView].startRenderPass();
        this.mPageViews[this.mCurrentView].render(new SORenderListener() {
            public void progress(int i) {
                if (i == 0) {
                    SlideShowView slideShowView = SlideShowView.this;
                    slideShowView.mPageViews[slideShowView.mCurrentView].endRenderPass();
                    SlideShowView slideShowView2 = SlideShowView.this;
                    Log.i("sonui", String.format("view w,h = %d %d", new Object[]{Integer.valueOf(slideShowView2.mPageViews[slideShowView2.mCurrentView].getMeasuredWidth()), Integer.valueOf(slideShowView2.mPageViews[slideShowView2.mCurrentView].getMeasuredHeight())}));
                    SOTransition slideTransition = ((SOPage) slideShowView2.mPageViews[slideShowView2.mCurrentView].getPage()).getSlideTransition();
                    Log.i("sonui", String.format("page %d transition: %s", new Object[]{Integer.valueOf(slideShowView2.mPageIndex + 1), (String) slideTransition.mRawValue}));
                    slideShowView2.mPageViews[slideShowView2.mLastView].setAlpha(1.0f);
                    slideShowView2.mPageViews[slideShowView2.mLastView].setVisibility(View.VISIBLE);
                    slideShowView2.mPageViews[slideShowView2.mLastView].setClipPath((Path) null);
                    slideShowView2.mPageViews[slideShowView2.mCurrentView].setAlpha(1.0f);
                    slideShowView2.mPageViews[slideShowView2.mCurrentView].setVisibility(View.VISIBLE);
                    slideShowView2.mPageViews[slideShowView2.mCurrentView].setClipPath((Path) null);
                    String str = (String) slideTransition.mType;
                    Objects.requireNonNull(str);
                    char c = 65535;
                    switch (str.hashCode()) {
                        case -1386333250:
                            if (str.equals("blinds")) {
                                c = 0;
                                break;
                            }
                            break;
                        case -1360216880:
                            if (str.equals("circle")) {
                                c = 1;
                                break;
                            }
                            break;
                        case -891985829:
                            if (str.equals("strips")) {
                                c = 2;
                                break;
                            }
                            break;
                        case -593123110:
                            if (str.equals("alphafade")) {
                                c = 3;
                                break;
                            }
                            break;
                        case 98882:
                            if (str.equals("cut")) {
                                c = 4;
                                break;
                            }
                            break;
                        case 3059457:
                            if (str.equals("comb")) {
                                c = 5;
                                break;
                            }
                            break;
                        case 3135100:
                            if (str.equals("fade")) {
                                c = 6;
                                break;
                            }
                            break;
                        case 3444122:
                            if (str.equals("plus")) {
                                c = 7;
                                break;
                            }
                            break;
                        case 3452485:
                            if (str.equals("pull")) {
                                c = 8;
                                break;
                            }
                            break;
                        case 3452698:
                            if (str.equals("push")) {
                                c = 9;
                                break;
                            }
                            break;
                        case 3649607:
                            if (str.equals("wipe")) {
                                c = 10;
                                break;
                            }
                            break;
                        case 3744723:
                            if (str.equals("zoom")) {
                                c = 11;
                                break;
                            }
                            break;
                        case 94852023:
                            if (str.equals("cover")) {
                                c = 12;
                                break;
                            }
                            break;
                        case 109648666:
                            if (str.equals("split")) {
                                c = 13;
                                break;
                            }
                            break;
                        case 113007284:
                            if (str.equals("wedge")) {
                                c = 14;
                                break;
                            }
                            break;
                        case 113097563:
                            if (str.equals("wheel")) {
                                c = 15;
                                break;
                            }
                            break;
                        case 116052483:
                            if (str.equals("randombars")) {
                                c = 16;
                                break;
                            }
                            break;
                        case 287951985:
                            if (str.equals("dissolve")) {
                                c = 17;
                                break;
                            }
                            break;
                        case 307310845:
                            if (str.equals("newsflash")) {
                                c = 18;
                                break;
                            }
                            break;
                        case 742313909:
                            if (str.equals("checker")) {
                                c = 19;
                                break;
                            }
                            break;
                        case 1655054676:
                            if (str.equals("diamond")) {
                                c = 20;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            SlideShowPageLayout[] slideShowPageLayoutArr = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout = slideShowPageLayoutArr[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout2 = slideShowPageLayoutArr[slideShowView2.mCurrentView];
                            int i2 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            BlindsAnimation blindsAnimation = new BlindsAnimation((String) slideTransition.mDirection, slideShowPageLayout, slideShowPageLayout2, i2);
                            blindsAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout2, slideShowPageLayout) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout2.startAnimation(blindsAnimation);
                            return;
                        case 1:
                            SlideShowPageLayout[] slideShowPageLayoutArr2 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout3 = slideShowPageLayoutArr2[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout4 = slideShowPageLayoutArr2[slideShowView2.mCurrentView];
                            int i3 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            CircleAnimation circleAnimation = new CircleAnimation((String) slideTransition.mDirection, slideShowPageLayout3, slideShowPageLayout4, i3);
                            circleAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout4, slideShowPageLayout3) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout4.startAnimation(circleAnimation);
                            return;
                        case 2:
                            SlideShowPageLayout[] slideShowPageLayoutArr3 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout5 = slideShowPageLayoutArr3[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout6 = slideShowPageLayoutArr3[slideShowView2.mCurrentView];
                            int i4 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            StripsAnimation stripsAnimation = new StripsAnimation((String) slideTransition.mDirection, slideShowPageLayout5, slideShowPageLayout6, i4);
                            stripsAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout6, slideShowPageLayout5) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout6.startAnimation(stripsAnimation);
                            return;
                        case 3:
                            SlideShowPageLayout[] slideShowPageLayoutArr4 = slideShowView2.mPageViews;
                            slideShowView2.alphaFade(slideShowPageLayoutArr4[slideShowView2.mLastView], slideShowPageLayoutArr4[slideShowView2.mCurrentView], slideTransition.mDuration);
                            return;
                        case 4:
                            String str2 = (String) slideTransition.mDirection;
                            Objects.requireNonNull(str2);
                            if (str2.equals("thrublk")) {
                                SlideShowPageLayout[] slideShowPageLayoutArr5 = slideShowView2.mPageViews;
                                SlideShowPageLayout slideShowPageLayout7 = slideShowPageLayoutArr5[slideShowView2.mLastView];
                                SlideShowPageLayout slideShowPageLayout8 = slideShowPageLayoutArr5[slideShowView2.mCurrentView];
                                int i5 = slideTransition.mDuration;
                                slideShowView2.animationCounter = 1;
                                slideShowPageLayout8.setVisibility(4);
                                slideShowPageLayout7.setVisibility(4);
                                new Handler().postDelayed(new Runnable(slideShowPageLayout8, slideShowPageLayout7) {
                                    public final /* synthetic */ SlideShowPageLayout val$newView;
                                    public final /* synthetic */ SlideShowPageLayout val$oldView;

                                    {
                                        this.val$newView = r2;
                                        this.val$oldView = r3;
                                    }

                                    public void run() {
                                        this.val$newView.setVisibility(View.VISIBLE);
                                        SlideShowView slideShowView = SlideShowView.this;
                                        slideShowView.animationCounter = 0;
                                        SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                    }
                                }, (long) i5);
                                return;
                            } else if (str2.equals("any")) {
                                SlideShowPageLayout[] slideShowPageLayoutArr6 = slideShowView2.mPageViews;
                                SlideShowPageLayout slideShowPageLayout9 = slideShowPageLayoutArr6[slideShowView2.mLastView];
                                SlideShowPageLayout slideShowPageLayout10 = slideShowPageLayoutArr6[slideShowView2.mCurrentView];
                                slideShowPageLayout10.setVisibility(View.VISIBLE);
                                slideShowPageLayout9.setVisibility(4);
                                slideShowPageLayout9.clearUpAnimTiles(true);
                                slideShowPageLayout10.startSlideAnimations();
                                return;
                            } else {
                                return;
                            }
                        case 5:
                            String str3 = (String) slideTransition.mDirection;
                            SlideShowPageLayout[] slideShowPageLayoutArr7 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout11 = slideShowPageLayoutArr7[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout12 = slideShowPageLayoutArr7[slideShowView2.mCurrentView];
                            int i6 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 2;
                            slideShowPageLayout12.setVisibility(4);
                            int measuredWidth = slideShowPageLayout12.getMeasuredWidth();
                            int measuredHeight = slideShowPageLayout12.getMeasuredHeight();
                            Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(createBitmap);
                            slideShowPageLayout12.layout(0, 0, measuredWidth, measuredHeight);
                            slideShowPageLayout12.draw(canvas);
                            ClippedImageView addImageViewCopy = slideShowView2.addImageViewCopy(slideShowPageLayout12, createBitmap);
                            ClippedImageView addImageViewCopy2 = slideShowView2.addImageViewCopy(slideShowPageLayout12, createBitmap);
                            Path path = new Path();
                            for (int i7 = 0; i7 < 20; i7 += 2) {
                                if (str3.equals("horz")) {
                                    path.addRect(BitmapDescriptorFactory.HUE_RED, (float) ((i7 * measuredHeight) / 20), (float) measuredWidth, (float) DissolveFadeAnimation$$ExternalSyntheticOutline0.m(i7, 1, measuredHeight, 20), Path.Direction.CW);
                                } else {
                                    path.addRect((float) ((i7 * measuredWidth) / 20), BitmapDescriptorFactory.HUE_RED, (float) DissolveFadeAnimation$$ExternalSyntheticOutline0.m(i7, 1, measuredWidth, 20), (float) measuredHeight, Path.Direction.CW);
                                }
                            }
                            addImageViewCopy.setClipPath(path);
                            Path path2 = new Path();
                            int i8 = 1;
                            int i9 = 20;
                            while (i8 < i9) {
                                if (str3.equals("horz")) {
                                    path2.addRect(BitmapDescriptorFactory.HUE_RED, (float) ((i8 * measuredHeight) / i9), (float) measuredWidth, (float) DissolveFadeAnimation$$ExternalSyntheticOutline0.m(i8, 1, measuredHeight, 20), Path.Direction.CW);
                                } else {
                                    path2.addRect((float) ((i8 * measuredWidth) / 20), BitmapDescriptorFactory.HUE_RED, (float) DissolveFadeAnimation$$ExternalSyntheticOutline0.m(i8, 1, measuredWidth, 20), (float) measuredHeight, Path.Direction.CW);
                                }
                                i9 = 20;
                                i8 += 2;
                            }
                            addImageViewCopy2.setClipPath(path2);
                            if (str3.equals("horz")) {
                                addImageViewCopy.setTranslationX((float) (-measuredWidth));
                                addImageViewCopy2.setTranslationX((float) measuredWidth);
                            } else {
                                addImageViewCopy.setTranslationY((float) (-measuredHeight));
                                addImageViewCopy2.setTranslationY((float) measuredHeight);
                            }
                            ViewPropertyAnimator animate = addImageViewCopy.animate();
                            long j = (long) i6;
                            SlideShowView slideShowView3 = slideShowView2;
                            SlideShowPageLayout slideShowPageLayout13 = slideShowPageLayout12;
                            ClippedImageView clippedImageView = addImageViewCopy;
                            ClippedImageView clippedImageView2 = addImageViewCopy2;
                            SlideShowPageLayout slideShowPageLayout14 = slideShowPageLayout11;
                            animate.translationX(BitmapDescriptorFactory.HUE_RED).translationY(BitmapDescriptorFactory.HUE_RED).setDuration(j).setListener(new AnimatorListenerAdapter(slideShowPageLayout13, clippedImageView, clippedImageView2, slideShowPageLayout14, animate) {
                                public final /* synthetic */ ViewPropertyAnimator val$anim1;
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;
                                public final /* synthetic */ ClippedImageView val$v1;
                                public final /* synthetic */ ClippedImageView val$v2;

                                {
                                    this.val$newView = r2;
                                    this.val$v1 = r3;
                                    this.val$v2 = r4;
                                    this.val$oldView = r5;
                                    this.val$anim1 = r6;
                                }

                                public void onAnimationEnd(Animator animator) {
                                    SlideShowView.access$610(SlideShowView.this);
                                    if (SlideShowView.this.animationCounter == 0) {
                                        this.val$newView.setVisibility(View.VISIBLE);
                                        SlideShowView.this.mSlideParent.removeView(this.val$v1);
                                        SlideShowView.this.mSlideParent.removeView(this.val$v2);
                                        SlideShowView.access$700(SlideShowView.this, this.val$oldView, this.val$newView);
                                    }
                                    this.val$anim1.cancel();
                                    this.val$anim1.setListener((Animator.AnimatorListener) null);
                                }
                            });
                            ViewPropertyAnimator animate2 = addImageViewCopy2.animate();
                            animate2.translationX(BitmapDescriptorFactory.HUE_RED).translationY(BitmapDescriptorFactory.HUE_RED).setDuration(j).setListener(new AnimatorListenerAdapter(slideShowPageLayout13, clippedImageView, clippedImageView2, slideShowPageLayout14, animate2) {
                                public final /* synthetic */ ViewPropertyAnimator val$anim2;
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;
                                public final /* synthetic */ ClippedImageView val$v1;
                                public final /* synthetic */ ClippedImageView val$v2;

                                {
                                    this.val$newView = r2;
                                    this.val$v1 = r3;
                                    this.val$v2 = r4;
                                    this.val$oldView = r5;
                                    this.val$anim2 = r6;
                                }

                                public void onAnimationEnd(Animator animator) {
                                    SlideShowView.access$610(SlideShowView.this);
                                    if (SlideShowView.this.animationCounter == 0) {
                                        this.val$newView.setVisibility(View.VISIBLE);
                                        SlideShowView.this.mSlideParent.removeView(this.val$v1);
                                        SlideShowView.this.mSlideParent.removeView(this.val$v2);
                                        SlideShowView.access$700(SlideShowView.this, this.val$oldView, this.val$newView);
                                    }
                                    this.val$anim2.cancel();
                                    this.val$anim2.setListener((Animator.AnimatorListener) null);
                                }
                            });
                            return;
                        case 6:
                            String str4 = (String) slideTransition.mDirection;
                            Objects.requireNonNull(str4);
                            if (str4.equals("thrublk")) {
                                SlideShowPageLayout[] slideShowPageLayoutArr8 = slideShowView2.mPageViews;
                                SlideShowPageLayout slideShowPageLayout15 = slideShowPageLayoutArr8[slideShowView2.mLastView];
                                SlideShowPageLayout slideShowPageLayout16 = slideShowPageLayoutArr8[slideShowView2.mCurrentView];
                                int i10 = slideTransition.mDuration;
                                slideShowView2.animationCounter = 1;
                                slideShowPageLayout16.setAlpha(BitmapDescriptorFactory.HUE_RED);
                                slideShowView2.setViewPropertyAnimator(slideShowPageLayout15).alpha(BitmapDescriptorFactory.HUE_RED).setDuration((long) (i10 / 2)).setListener(new AnimatorListenerAdapter(slideShowPageLayout15, slideShowPageLayout16, i10) {
                                    public final /* synthetic */ int val$duration;
                                    public final /* synthetic */ SlideShowPageLayout val$newView;
                                    public final /* synthetic */ SlideShowPageLayout val$oldView;

                                    {
                                        this.val$oldView = r2;
                                        this.val$newView = r3;
                                        this.val$duration = r4;
                                    }

                                    public void onAnimationEnd(Animator animator) {
                                        this.val$oldView.setVisibility(4);
                                        this.val$newView.setVisibility(View.VISIBLE);
                                        SlideShowView slideShowView = SlideShowView.this;
                                        SlideShowPageLayout slideShowPageLayout = this.val$newView;
                                        ArDkDoc arDkDoc = SlideShowView.useDoc;
                                        slideShowView.setViewPropertyAnimator(slideShowPageLayout).alpha(1.0f).setDuration((long) (this.val$duration / 2)).setListener(new AnimatorListenerAdapter() {
                                            public void onAnimationEnd(Animator animator) {
                                                AnonymousClass25 r3 = AnonymousClass25.this;
                                                SlideShowView slideShowView = SlideShowView.this;
                                                slideShowView.animationCounter = 0;
                                                SlideShowView.access$700(slideShowView, r3.val$oldView, r3.val$newView);
                                            }
                                        });
                                    }
                                });
                                return;
                            } else if (str4.equals("any")) {
                                SlideShowPageLayout[] slideShowPageLayoutArr9 = slideShowView2.mPageViews;
                                slideShowView2.alphaFade(slideShowPageLayoutArr9[slideShowView2.mLastView], slideShowPageLayoutArr9[slideShowView2.mCurrentView], slideTransition.mDuration);
                                return;
                            } else {
                                return;
                            }
                        case 7:
                            SlideShowPageLayout[] slideShowPageLayoutArr10 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout17 = slideShowPageLayoutArr10[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout18 = slideShowPageLayoutArr10[slideShowView2.mCurrentView];
                            int i11 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            PlusAnimation plusAnimation = new PlusAnimation((String) slideTransition.mDirection, slideShowPageLayout17, slideShowPageLayout18, i11);
                            plusAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout18, slideShowPageLayout17) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout18.startAnimation(plusAnimation);
                            return;
                        case 8:
                            String str5 = (String) slideTransition.mDirection;
                            SlideShowPageLayout[] slideShowPageLayoutArr11 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout19 = slideShowPageLayoutArr11[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout20 = slideShowPageLayoutArr11[slideShowView2.mCurrentView];
                            int i12 = slideTransition.mDuration;
                            slideShowView2.mSlideParent.bringChildToFront(slideShowPageLayout19);
                            int measuredHeight2 = slideShowPageLayout19.getMeasuredHeight();
                            int measuredWidth2 = slideShowPageLayout19.getMeasuredWidth();
                            float f = (str5.equals("l") || str5.equals("lu") || str5.equals("ld")) ? (float) (-measuredWidth2) : BitmapDescriptorFactory.HUE_RED;
                            if (str5.equals("r") || str5.equals("ru") || str5.equals("rd")) {
                                f = (float) measuredWidth2;
                            }
                            float f2 = (str5.equals(GoogleApiAvailabilityLight.TRACKING_SOURCE_DIALOG) || str5.equals("rd") || str5.equals("ld")) ? (float) measuredHeight2 : BitmapDescriptorFactory.HUE_RED;
                            if (str5.equals("u") || str5.equals("ru") || str5.equals("lu")) {
                                f2 = (float) (-measuredHeight2);
                            }
                            slideShowView2.animationCounter = 1;
                            slideShowView2.setViewPropertyAnimator(slideShowPageLayout19).translationX(f).translationY(f2).setDuration((long) i12).setListener(new AnimatorListenerAdapter(slideShowPageLayout20, slideShowPageLayout19) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void onAnimationEnd(Animator animator) {
                                    SlideShowView.this.mSlideParent.bringChildToFront(this.val$newView);
                                    this.val$oldView.setTranslationX(BitmapDescriptorFactory.HUE_RED);
                                    this.val$oldView.setTranslationY(BitmapDescriptorFactory.HUE_RED);
                                    SlideShowView.access$610(SlideShowView.this);
                                    SlideShowView.access$700(SlideShowView.this, this.val$oldView, this.val$newView);
                                }
                            });
                            return;
                        case 9:
                            String str6 = (String) slideTransition.mDirection;
                            SlideShowPageLayout[] slideShowPageLayoutArr12 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout21 = slideShowPageLayoutArr12[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout22 = slideShowPageLayoutArr12[slideShowView2.mCurrentView];
                            int i13 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 2;
                            int measuredHeight3 = str6.equals(GoogleApiAvailabilityLight.TRACKING_SOURCE_DIALOG) ? slideShowPageLayout22.getMeasuredHeight() : 0;
                            if (str6.equals("u")) {
                                measuredHeight3 = -slideShowPageLayout22.getMeasuredHeight();
                            }
                            int i14 = str6.equals("l") ? -slideShowPageLayout22.getMeasuredWidth() : 0;
                            if (str6.equals("r")) {
                                i14 = slideShowPageLayout22.getMeasuredWidth();
                            }
                            slideShowPageLayout22.setTranslationY((float) (-measuredHeight3));
                            slideShowPageLayout22.setTranslationX((float) (-i14));
                            slideShowPageLayout22.setVisibility(View.VISIBLE);
                            ViewPropertyAnimator animate3 = slideShowPageLayout22.animate();
                            long j2 = (long) i13;
                            animate3.translationX(BitmapDescriptorFactory.HUE_RED).translationY(BitmapDescriptorFactory.HUE_RED).setDuration(j2).setListener(new AnimatorListenerAdapter(slideShowPageLayout22, animate3, slideShowPageLayout21) {
                                public final /* synthetic */ ViewPropertyAnimator val$anim1;
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$anim1 = r3;
                                    this.val$oldView = r4;
                                }

                                public void onAnimationEnd(Animator animator) {
                                    this.val$newView.setTranslationY(BitmapDescriptorFactory.HUE_RED);
                                    this.val$newView.setTranslationX(BitmapDescriptorFactory.HUE_RED);
                                    SlideShowView.access$610(SlideShowView.this);
                                    this.val$anim1.setListener((Animator.AnimatorListener) null);
                                    this.val$anim1.cancel();
                                    SlideShowView.access$700(SlideShowView.this, this.val$oldView, this.val$newView);
                                }
                            });
                            slideShowPageLayout21.setVisibility(View.VISIBLE);
                            ViewPropertyAnimator animate4 = slideShowPageLayout21.animate();
                            animate4.translationX((float) i14).translationY((float) measuredHeight3).setDuration(j2).setListener(new AnimatorListenerAdapter(slideShowPageLayout21, animate4) {
                                public final /* synthetic */ ViewPropertyAnimator val$anim2;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$oldView = r2;
                                    this.val$anim2 = r3;
                                }

                                public void onAnimationEnd(Animator animator) {
                                    this.val$oldView.setTranslationY(BitmapDescriptorFactory.HUE_RED);
                                    this.val$oldView.setTranslationX(BitmapDescriptorFactory.HUE_RED);
                                    SlideShowView.access$610(SlideShowView.this);
                                    this.val$anim2.setListener((Animator.AnimatorListener) null);
                                    this.val$anim2.cancel();
                                }
                            });
                            return;
                        case 10:
                            SlideShowPageLayout[] slideShowPageLayoutArr13 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout23 = slideShowPageLayoutArr13[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout24 = slideShowPageLayoutArr13[slideShowView2.mCurrentView];
                            int i15 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            WipeAnimation wipeAnimation = new WipeAnimation((String) slideTransition.mDirection, slideShowPageLayout23, slideShowPageLayout24, i15);
                            wipeAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout24, slideShowPageLayout23) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout24.startAnimation(wipeAnimation);
                            return;
                        case 11:
                            SlideShowPageLayout[] slideShowPageLayoutArr14 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout25 = slideShowPageLayoutArr14[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout26 = slideShowPageLayoutArr14[slideShowView2.mCurrentView];
                            int i16 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            slideShowView2.mSlideParent.bringChildToFront(slideShowPageLayout25);
                            ZoomAnimation zoomAnimation = new ZoomAnimation((String) slideTransition.mDirection, slideShowPageLayout25, slideShowPageLayout26, i16);
                            zoomAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout26, slideShowPageLayout25) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView.this.mSlideParent.bringChildToFront(this.val$newView);
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout26.startAnimation(zoomAnimation);
                            return;
                        case 12:
                            String str7 = (String) slideTransition.mDirection;
                            SlideShowPageLayout[] slideShowPageLayoutArr15 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout27 = slideShowPageLayoutArr15[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout28 = slideShowPageLayoutArr15[slideShowView2.mCurrentView];
                            int i17 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            int measuredHeight4 = str7.contains(GoogleApiAvailabilityLight.TRACKING_SOURCE_DIALOG) ? slideShowPageLayout28.getMeasuredHeight() : 0;
                            if (str7.contains("u")) {
                                measuredHeight4 = -slideShowPageLayout28.getMeasuredHeight();
                            }
                            int i18 = str7.contains("l") ? -slideShowPageLayout28.getMeasuredWidth() : 0;
                            if (str7.contains("r")) {
                                i18 = slideShowPageLayout28.getMeasuredWidth();
                            }
                            slideShowPageLayout28.setTranslationY((float) (-measuredHeight4));
                            slideShowPageLayout28.setTranslationX((float) (-i18));
                            slideShowPageLayout28.setVisibility(View.VISIBLE);
                            slideShowView2.setViewPropertyAnimator(slideShowPageLayout28).translationX(BitmapDescriptorFactory.HUE_RED).translationY(BitmapDescriptorFactory.HUE_RED).setDuration((long) i17).setListener(new AnimatorListenerAdapter(slideShowPageLayout27, slideShowPageLayout28) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$oldView = r2;
                                    this.val$newView = r3;
                                }

                                public void onAnimationEnd(Animator animator) {
                                    SlideShowView.access$610(SlideShowView.this);
                                    SlideShowView.access$700(SlideShowView.this, this.val$oldView, this.val$newView);
                                }
                            });
                            return;
                        case 13:
                            SlideShowPageLayout[] slideShowPageLayoutArr16 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout29 = slideShowPageLayoutArr16[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout30 = slideShowPageLayoutArr16[slideShowView2.mCurrentView];
                            int i19 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            slideShowView2.mSlideParent.bringChildToFront(slideShowPageLayout29);
                            SplitAnimation splitAnimation = new SplitAnimation((String) slideTransition.mDirection, slideShowPageLayout29, slideShowPageLayout30, i19);
                            splitAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout30, slideShowPageLayout29) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView.this.mSlideParent.bringChildToFront(this.val$newView);
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout30.startAnimation(splitAnimation);
                            return;
                        case 14:
                            SlideShowPageLayout[] slideShowPageLayoutArr17 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout31 = slideShowPageLayoutArr17[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout32 = slideShowPageLayoutArr17[slideShowView2.mCurrentView];
                            int i20 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            WedgeAnimation wedgeAnimation = new WedgeAnimation((String) slideTransition.mDirection, slideShowPageLayout31, slideShowPageLayout32, i20);
                            wedgeAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout32, slideShowPageLayout31) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout32.startAnimation(wedgeAnimation);
                            return;
                        case 15:
                            SlideShowPageLayout[] slideShowPageLayoutArr18 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout33 = slideShowPageLayoutArr18[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout34 = slideShowPageLayoutArr18[slideShowView2.mCurrentView];
                            int i21 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            WheelAnimation wheelAnimation = new WheelAnimation((String) slideTransition.mDirection, slideShowPageLayout33, slideShowPageLayout34, i21);
                            wheelAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout34, slideShowPageLayout33) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout34.startAnimation(wheelAnimation);
                            return;
                        case 16:
                            SlideShowPageLayout[] slideShowPageLayoutArr19 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout35 = slideShowPageLayoutArr19[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout36 = slideShowPageLayoutArr19[slideShowView2.mCurrentView];
                            int i22 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            RandomBarsAnimation randomBarsAnimation = new RandomBarsAnimation((String) slideTransition.mDirection, slideShowPageLayout35, slideShowPageLayout36, i22);
                            randomBarsAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout36, slideShowPageLayout35) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout36.startAnimation(randomBarsAnimation);
                            return;
                        case 17:
                            SlideShowPageLayout[] slideShowPageLayoutArr20 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout37 = slideShowPageLayoutArr20[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout38 = slideShowPageLayoutArr20[slideShowView2.mCurrentView];
                            int i23 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            DissolveAnimation dissolveAnimation = new DissolveAnimation((String) slideTransition.mDirection, slideShowPageLayout37, slideShowPageLayout38, i23);
                            dissolveAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout38, slideShowPageLayout37) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout38.startAnimation(dissolveAnimation);
                            return;
                        case 18:
                            String str8 = (String) slideTransition.mDirection;
                            SlideShowPageLayout[] slideShowPageLayoutArr21 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout39 = slideShowPageLayoutArr21[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout40 = slideShowPageLayoutArr21[slideShowView2.mCurrentView];
                            int i24 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            slideShowPageLayout40.setScaleX(BitmapDescriptorFactory.HUE_RED);
                            slideShowPageLayout40.setScaleY(BitmapDescriptorFactory.HUE_RED);
                            slideShowPageLayout40.setRotation(BitmapDescriptorFactory.HUE_RED);
                            slideShowPageLayout40.setAlpha(0.3f);
                            slideShowPageLayout40.setVisibility(View.VISIBLE);
                            slideShowView2.setViewPropertyAnimator(slideShowPageLayout40).scaleX(1.0f).scaleY(1.0f).rotation(360.0f).alpha(1.0f).setDuration((long) i24).setListener(new AnimatorListenerAdapter(slideShowPageLayout40, slideShowPageLayout39) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void onAnimationEnd(Animator animator) {
                                    this.val$newView.setRotation(BitmapDescriptorFactory.HUE_RED);
                                    SlideShowView.access$610(SlideShowView.this);
                                    SlideShowView.access$700(SlideShowView.this, this.val$oldView, this.val$newView);
                                }
                            });
                            return;
                        case 19:
                            SlideShowPageLayout[] slideShowPageLayoutArr22 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout41 = slideShowPageLayoutArr22[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout42 = slideShowPageLayoutArr22[slideShowView2.mCurrentView];
                            int i25 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            CheckerAnimation checkerAnimation = new CheckerAnimation((String) slideTransition.mDirection, slideShowPageLayout41, slideShowPageLayout42, i25);
                            checkerAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout42, slideShowPageLayout41) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout42.startAnimation(checkerAnimation);
                            return;
                        case 20:
                            SlideShowPageLayout[] slideShowPageLayoutArr23 = slideShowView2.mPageViews;
                            SlideShowPageLayout slideShowPageLayout43 = slideShowPageLayoutArr23[slideShowView2.mLastView];
                            SlideShowPageLayout slideShowPageLayout44 = slideShowPageLayoutArr23[slideShowView2.mCurrentView];
                            int i26 = slideTransition.mDuration;
                            slideShowView2.animationCounter = 1;
                            DiamondAnimation diamondAnimation = new DiamondAnimation((String) slideTransition.mDirection, slideShowPageLayout43, slideShowPageLayout44, i26);
                            diamondAnimation.mListener = new TransitionAnimation.TransitionAnimationListener(slideShowPageLayout44, slideShowPageLayout43) {
                                public final /* synthetic */ SlideShowPageLayout val$newView;
                                public final /* synthetic */ SlideShowPageLayout val$oldView;

                                {
                                    this.val$newView = r2;
                                    this.val$oldView = r3;
                                }

                                public void done() {
                                    this.val$newView.clearAnimation();
                                    SlideShowView slideShowView = SlideShowView.this;
                                    slideShowView.animationCounter = 0;
                                    SlideShowView.access$700(slideShowView, this.val$oldView, this.val$newView);
                                }
                            };
                            slideShowPageLayout44.startAnimation(diamondAnimation);
                            return;
                        default:
                            SlideShowPageLayout[] slideShowPageLayoutArr24 = slideShowView2.mPageViews;
                            slideShowView2.alphaFade(slideShowPageLayoutArr24[slideShowView2.mLastView], slideShowPageLayoutArr24[slideShowView2.mCurrentView], LogSeverity.ERROR_VALUE);
                            return;
                    }
                }
            }
        });
    }

    public PointF getPageViewOffset() {
        int[] iArr = {0, 0};
        SlideShowPageLayout[] slideShowPageLayoutArr = this.mPageViews;
        int i = this.mCurrentView;
        if (slideShowPageLayoutArr[i] != null) {
            slideShowPageLayoutArr[i].getLocationInWindow(iArr);
        }
        return new PointF((float) iArr[0], (float) iArr[1]);
    }

    public double getPageViewZoomScale() {
        SlideShowPageLayout[] slideShowPageLayoutArr = this.mPageViews;
        int i = this.mCurrentView;
        if (slideShowPageLayoutArr[i] != null) {
            return slideShowPageLayoutArr[i].getZoomScale();
        }
        return 1.0d;
    }

    public void goBack() {
        if (!this.mPageViews[this.mCurrentView].prevAnim()) {
            previousSlide();
        }
    }

    public void goForward() {
        if (!this.mPageViews[this.mCurrentView].nextAnim()) {
            nextSlide();
        }
    }

    public final void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.sodk_editor_slide_show_layout, this);
        this.mDoc = useDoc;
        this.mLib = useLib;
        LayerBitmapManager layerBitmapManager = new LayerBitmapManager();
        this.mLayerBitmapMan = layerBitmapManager;
        layerBitmapManager.setMemoryInfoProvider(this);
        if (this.mDoc == null || this.mLib == null) {
            throw new IllegalArgumentException("Document Session or Smart Office library is invalid ");
        }
        findViewById(R.id.sodk_editor_slideshow_doc_inner_container).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                SlideShowView slideShowView = SlideShowView.this;
                int i = R.id.sodk_editor_slideshow_doc_inner_container;
                slideShowView.findViewById(i).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                SlideShowView slideShowView2 = SlideShowView.this;
                slideShowView2.mPageViews[0] = new SlideShowPageLayout(slideShowView2.mDoc, slideShowView2.mLayerBitmapMan, slideShowView2.getContext(), slideShowView2.mLib);
                slideShowView2.mPageViews[1] = new SlideShowPageLayout(slideShowView2.mDoc, slideShowView2.mLayerBitmapMan, slideShowView2.getContext(), slideShowView2.mLib);
                RelativeLayout relativeLayout = (RelativeLayout) slideShowView2.findViewById(i);
                slideShowView2.mSlideParent = relativeLayout;
                relativeLayout.removeAllViews();
                slideShowView2.mSlideParent.addView(slideShowView2.mPageViews[0]);
                slideShowView2.mSlideParent.addView(slideShowView2.mPageViews[1]);
                slideShowView2.mPageViews[0].setListener(slideShowView2);
                slideShowView2.mPageViews[1].setListener(slideShowView2);
                slideShowView2.mSlideParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        SlideShowView.this.mSlideParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        SlideShowView.this.nextSlide();
                    }
                });
            }
        });
    }

    public void nextSlide() {
        int i;
        if (this.animationCounter <= 0) {
            SlideShowViewListener slideShowViewListener = this.listener;
            if (slideShowViewListener != null && (i = this.mPageIndex) >= 0) {
                slideShowViewListener.slideEnded(i);
            }
            int i2 = this.mPageIndex + 1;
            this.mPageIndex = i2;
            if (i2 >= this.mDoc.getNumPages()) {
                SlideShowViewListener slideShowViewListener2 = this.listener;
                if (slideShowViewListener2 != null) {
                    slideShowViewListener2.slideShowCompleted();
                    return;
                }
                return;
            }
            doSlide();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mSlideParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                SlideShowView.this.mSlideParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                SlideShowView.this.resize(SlideShowView.this.mSlideParent.getWidth(), SlideShowView.this.mSlideParent.getHeight(), true);
            }
        });
    }

    public void onLowMemory() {
    }

    public void onTrimMemory(int i) {
        Runtime runtime = Runtime.getRuntime();
        runtime.maxMemory();
        runtime.totalMemory();
        runtime.freeMemory();
        if (i == 5 || i == 10 || i == 15) {
            this.trimmingMemory = true;
            if (this.animationCounter == 0) {
                this.mPageViews[1 - this.mCurrentView].clearUpAnimTiles(true);
            }
        }
    }

    public void previousSlide() {
        int i;
        if (this.animationCounter <= 0 && (i = this.mPageIndex) > 0) {
            SlideShowViewListener slideShowViewListener = this.listener;
            if (slideShowViewListener != null) {
                slideShowViewListener.slideEnded(i);
            }
            this.mPageIndex--;
            doSlide();
        }
    }

    public void resize(int i, int i2, boolean z) {
        for (int i3 = 0; i3 <= 1; i3++) {
            if (this.mPageViews[i3].getPageNumber() >= 0) {
                this.mPageViews[i3].resize(i, i2, z);
            }
        }
    }

    public void setListener(SlideShowViewListener slideShowViewListener) {
        this.listener = slideShowViewListener;
    }

    public final synchronized ViewPropertyAnimator setViewPropertyAnimator(View view) {
        if (view == null) {
            return null;
        }
        ViewPropertyAnimator viewPropertyAnimator = this.vpa;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.setListener((Animator.AnimatorListener) null);
            this.vpa.cancel();
            this.vpa = null;
        }
        ViewPropertyAnimator animate = view.animate();
        this.vpa = animate;
        return animate;
    }

    public SlideShowView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public SlideShowView(Context context) {
        super(context);
        init();
    }
}
