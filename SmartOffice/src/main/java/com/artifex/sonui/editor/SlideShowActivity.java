package com.artifex.sonui.editor;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.artifex.R;
import com.artifex.solib.SOLib;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class SlideShowActivity extends BaseActivity implements SlideShowViewListener {
    public static SODocSession useSession;
    public SODocSession mSession;
    public SlideShowView mSlideShowView = null;

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        public MyGestureListener() {
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            float x = motionEvent2.getX() - motionEvent.getX();
            if (Math.abs(x) <= Math.abs(motionEvent2.getY() - motionEvent.getY()) || Math.abs(x) <= 100.0f || Math.abs(f) <= 100.0f) {
                return true;
            }
            if (x > BitmapDescriptorFactory.HUE_RED) {
                SlideShowActivity.this.mSlideShowView.goBack();
                return true;
            }
            SlideShowActivity.this.mSlideShowView.goForward();
            return true;
        }

        public void onLongPress(MotionEvent motionEvent) {
            SlideShowActivity.this.finish();
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            SlideShowActivity.this.mSlideShowView.goForward();
            return false;
        }
    }

    public static void setSession(SODocSession sODocSession) {
        useSession = sODocSession;
    }

    public void finish() {
        findViewById(R.id.sodk_editor_mask).setOnTouchListener((View.OnTouchListener) null);
        this.mSession = null;
        this.mSlideShowView.dispose();
        this.mSlideShowView = null;
        super.finish();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SODocSession sODocSession = useSession;
        this.mSession = sODocSession;
        SlideShowView.setDoc(sODocSession.getDoc());
        SlideShowView.setLib(SOLib.getLib(this));
        setContentView(R.layout.sodk_editor_slide_show);
        SODocSession sODocSession2 = this.mSession;
        if (sODocSession2 == null || sODocSession2.getDoc() == null) {
            super.finish();
            return;
        }
        View findViewById = findViewById(R.id.sodk_editor_mask);
        final GestureDetector gestureDetector = new GestureDetector(getBaseContext(), new MyGestureListener());
        findViewById.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        });
        SlideShowView slideShowView = (SlideShowView) findViewById(R.id.sodk_editor_slide_show_view);
        this.mSlideShowView = slideShowView;
        slideShowView.setListener(this);
    }

    public void slideAnimating(int i) {
    }

    public void slideAnimationsCompleted(int i) {
    }

    public void slideAnimationsStarted(int i) {
    }

    public void slideAnimationsWaiting(int i) {
    }

    public void slideEnded(int i) {
    }

    public void slideShowCompleted() {
        finish();
    }

    public void slideStarted(int i) {
    }
}
