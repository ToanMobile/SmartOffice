package com.artifex.source.library.wheel.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;

import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.google.logging.type.LogSeverity;
import com.vungle.warren.downloader.DownloadRequest;

public class WheelScroller {
    public Handler animationHandler = new Handler() {
        public void handleMessage(Message message) {
            scroller.computeScrollOffset();
            int currY = scroller.getCurrY();
            int i = lastScrollY - currY;
            lastScrollY = currY;
            if (i != 0) {
                // TODO: ((WheelView.AnonymousClass1) listener).onScroll(i);
            }
            if (Math.abs(currY - scroller.getFinalY()) < 1) {
                scroller.getFinalY();
                scroller.forceFinished(true);
            }
            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(message.what);
            } else if (message.what == 0) {
                justify();
            } else {
                if (isScrollingPerformed) {
                    if (wheelView.isScrollingPerformed) {
                        for (OnWheelScrollListener onScrollingFinished : wheelView.scrollingListeners) {
                            onScrollingFinished.onScrollingFinished(wheelView);
                        }
                        wheelView.isScrollingPerformed = false;
                    }
                    wheelView.scrollingOffset = 0;
                    wheelView.invalidate();
                    isScrollingPerformed = false;
                }
            }
        }
    };
    public Context context;
    public GestureDetector gestureDetector;
    public GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            WheelScroller wheelScroller = WheelScroller.this;
            wheelScroller.lastScrollY = 0;
            wheelScroller.scroller.fling(0, 0, 0, (int) (-f2), 0, 0, DownloadRequest.Priority.CRITICAL, Integer.MAX_VALUE);
            WheelScroller.this.setNextMessage(0);
            return true;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return true;
        }
    };
    public boolean isScrollingPerformed;
    public int lastScrollY;
    public float lastTouchedY;
    public ScrollingListener listener;
    public Scroller scroller;

    public WheelView wheelView;

    public interface ScrollingListener {
    }

    public WheelScroller(Context context2, WheelView wheelView, ScrollingListener scrollingListener) {
        GestureDetector gestureDetector2 = new GestureDetector(context2, this.gestureListener);
        this.gestureDetector = gestureDetector2;
        gestureDetector2.setIsLongpressEnabled(false);
        this.scroller = new Scroller(context2);
        this.listener = scrollingListener;
        this.context = context2;
        this.wheelView = wheelView;
    }

    public final void justify() {
        if (Math.abs(wheelView.scrollingOffset) > 1) {
            wheelView.scroller.scroll(wheelView.scrollingOffset, 0);
        }
        setNextMessage(1);
    }

    public void scroll(int i, int i2) {
        this.scroller.forceFinished(true);
        this.lastScrollY = 0;
        this.scroller.startScroll(0, 0, 0, i, i2 != 0 ? i2 : LogSeverity.WARNING_VALUE);
        setNextMessage(0);
        startScrolling();
    }

    public final void setNextMessage(int i) {
        this.animationHandler.removeMessages(0);
        this.animationHandler.removeMessages(1);
        this.animationHandler.sendEmptyMessage(i);
    }

    public final void startScrolling() {
        if (!this.isScrollingPerformed) {
            this.isScrollingPerformed = true;
            wheelView.isScrollingPerformed = true;
            for (OnWheelScrollListener onScrollingStarted : wheelView.scrollingListeners) {
                onScrollingStarted.onScrollingStarted(wheelView);
            }
        }
    }
}
