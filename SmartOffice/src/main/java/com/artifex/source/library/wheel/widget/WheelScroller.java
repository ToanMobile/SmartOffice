package com.artifex.source.library.wheel.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;
import com.google.logging.type.LogSeverity;
import com.vungle.warren.downloader.DownloadRequest;
import com.artifex.source.library.wheel.widget.WheelView;

public class WheelScroller {
    public Handler animationHandler = new Handler() {
        public void handleMessage(Message message) {
            WheelScroller.this.scroller.computeScrollOffset();
            int currY = WheelScroller.this.scroller.getCurrY();
            WheelScroller wheelScroller = WheelScroller.this;
            int i = wheelScroller.lastScrollY - currY;
            wheelScroller.lastScrollY = currY;
            if (i != 0) {
                ((WheelView.AnonymousClass1) wheelScroller.listener).onScroll(i);
            }
            if (Math.abs(currY - WheelScroller.this.scroller.getFinalY()) < 1) {
                WheelScroller.this.scroller.getFinalY();
                WheelScroller.this.scroller.forceFinished(true);
            }
            if (!WheelScroller.this.scroller.isFinished()) {
                WheelScroller.this.animationHandler.sendEmptyMessage(message.what);
            } else if (message.what == 0) {
                WheelScroller.this.justify();
            } else {
                WheelScroller wheelScroller2 = WheelScroller.this;
                if (wheelScroller2.isScrollingPerformed) {
                    WheelView.AnonymousClass1 r0 = (WheelView.AnonymousClass1) wheelScroller2.listener;
                    WheelView wheelView = WheelView.this;
                    if (wheelView.isScrollingPerformed) {
                        for (OnWheelScrollListener onScrollingFinished : wheelView.scrollingListeners) {
                            onScrollingFinished.onScrollingFinished(wheelView);
                        }
                        WheelView.this.isScrollingPerformed = false;
                    }
                    WheelView wheelView2 = WheelView.this;
                    wheelView2.scrollingOffset = 0;
                    wheelView2.invalidate();
                    wheelScroller2.isScrollingPerformed = false;
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

    public interface ScrollingListener {
    }

    public WheelScroller(Context context2, ScrollingListener scrollingListener) {
        GestureDetector gestureDetector2 = new GestureDetector(context2, this.gestureListener);
        this.gestureDetector = gestureDetector2;
        gestureDetector2.setIsLongpressEnabled(false);
        this.scroller = new Scroller(context2);
        this.listener = scrollingListener;
        this.context = context2;
    }

    public final void justify() {
        WheelView.AnonymousClass1 r0 = (WheelView.AnonymousClass1) this.listener;
        if (Math.abs(WheelView.this.scrollingOffset) > 1) {
            WheelView wheelView = WheelView.this;
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
            WheelView wheelView = WheelView.this;
            wheelView.isScrollingPerformed = true;
            for (OnWheelScrollListener onScrollingStarted : wheelView.scrollingListeners) {
                onScrollingStarted.onScrollingStarted(wheelView);
            }
        }
    }
}
