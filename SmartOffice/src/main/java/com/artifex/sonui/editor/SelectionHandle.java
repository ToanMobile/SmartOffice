package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import kotlin.KotlinVersion;

public class SelectionHandle extends View implements View.OnTouchListener {
    public static final int KIND_END = 2;
    public static final int KIND_START = 1;
    public int downX;
    public int downY;
    public int dragSlop = 0;
    public int mDragDeltaX;
    public int mDragDeltaY;
    public SelectionHandleListener mDragHandleListener;
    public boolean mIsDragging = false;
    public int mKind = 0;
    public boolean mMayDraw = false;
    public int mPositionX = 0;
    public int mPositionY = 0;

    public interface SelectionHandleListener {
        void onDrag(SelectionHandle selectionHandle);

        void onEndDrag(SelectionHandle selectionHandle);

        void onStartDrag(SelectionHandle selectionHandle);
    }

    public SelectionHandle(Context context) {
        super(context);
        init(context);
    }

    public int getKind() {
        return this.mKind;
    }

    public Point getPoint() {
        Point point = new Point();
        int i = getLayoutParams().width;
        int i2 = getLayoutParams().height;
        int i3 = this.mKind;
        if (i3 == 1) {
            point.set((i / 2) + this.mPositionX, this.mPositionY + i2);
        } else if (i3 == 2) {
            point.set((i / 2) + this.mPositionX, this.mPositionY);
        }
        return point;
    }

    public Point getPosition() {
        return new Point(this.mPositionX, this.mPositionY);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public final void init(Context context) {
        setOnTouchListener(this);
        this.dragSlop = (int) context.getResources().getDimension(R.dimen.sodk_editor_drag_slop);
        setWillNotDraw(false);
        this.mKind = Integer.parseInt((String) getTag());
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                SelectionHandle selectionHandle = SelectionHandle.this;
                selectionHandle.offsetLeftAndRight(selectionHandle.mPositionX);
                SelectionHandle selectionHandle2 = SelectionHandle.this;
                selectionHandle2.offsetTopAndBottom(selectionHandle2.mPositionY);
                SelectionHandle.this.invalidate();
            }
        });
        moveTo(0, 0);
    }

    public void moveTo(int i, int i2) {
        setX((float) i);
        setY((float) i2);
        this.mPositionX = i;
        this.mPositionY = i2;
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mMayDraw) {
            Paint paint = new Paint();
            paint.setColor(-7829249);
            int width = getWidth();
            int height = getHeight();
            Path path = new Path();
            int i = this.mKind;
            if (i == 1) {
                float f = (float) (width / 4);
                path.moveTo(f, BitmapDescriptorFactory.HUE_RED);
                float f2 = (float) ((height * 2) / 3);
                path.lineTo(f, f2);
                path.lineTo((float) (width / 2), (float) height);
                float f3 = (float) ((width * 3) / 4);
                path.lineTo(f3, f2);
                path.lineTo(f3, BitmapDescriptorFactory.HUE_RED);
                path.lineTo(f, BitmapDescriptorFactory.HUE_RED);
                path.close();
            } else if (i == 2) {
                float f4 = (float) (width / 4);
                float f5 = (float) height;
                path.moveTo(f4, f5);
                float f6 = (float) (height / 3);
                path.lineTo(f4, f6);
                path.lineTo((float) (width / 2), BitmapDescriptorFactory.HUE_RED);
                float f7 = (float) ((width * 3) / 4);
                path.lineTo(f7, f6);
                path.lineTo(f7, f5);
                path.lineTo(f4, f5);
                path.close();
            }
            canvas.drawPath(path, paint);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        SelectionHandleListener selectionHandleListener;
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        if (!new Rect(iArr[0], iArr[1], view.getWidth() + iArr[0], view.getHeight() + iArr[1]).contains(rawX, rawY) && !this.mIsDragging) {
            return false;
        }
        int action = motionEvent.getAction() & KotlinVersion.MAX_COMPONENT_VALUE;
        if (action == 0) {
            Point position = getPosition();
            this.mDragDeltaX = rawX - position.x;
            this.mDragDeltaY = rawY - position.y;
            this.downX = rawX;
            this.downY = rawY;
        } else if (action == 1) {
            this.mIsDragging = false;
            SelectionHandleListener selectionHandleListener2 = this.mDragHandleListener;
            if (selectionHandleListener2 != null) {
                selectionHandleListener2.onEndDrag(this);
            }
        } else if (action == 2) {
            if (!this.mIsDragging) {
                int abs = Math.abs(rawX - this.downX);
                int abs2 = Math.abs(rawY - this.downY);
                int i = this.dragSlop;
                if (abs > i || abs2 > i) {
                    this.mIsDragging = true;
                    SelectionHandleListener selectionHandleListener3 = this.mDragHandleListener;
                    if (selectionHandleListener3 != null) {
                        selectionHandleListener3.onStartDrag(this);
                    }
                }
            }
            moveTo(rawX - this.mDragDeltaX, rawY - this.mDragDeltaY);
            if (this.mIsDragging && (selectionHandleListener = this.mDragHandleListener) != null) {
                selectionHandleListener.onDrag(this);
            }
        }
        return true;
    }

    public void setMayDraw(boolean z) {
        if (z != this.mMayDraw) {
            this.mMayDraw = z;
            invalidate();
        }
    }

    public void setPoint(int i, int i2) {
        int i3 = getLayoutParams().width;
        int i4 = getLayoutParams().height;
        int i5 = this.mKind;
        if (i5 == 1) {
            moveTo(i - (i3 / 2), i2 - i4);
        } else if (i5 == 2) {
            moveTo(i - (i3 / 2), i2);
        }
    }

    public void setSelectionHandleListener(SelectionHandleListener selectionHandleListener) {
        this.mDragHandleListener = selectionHandleListener;
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public SelectionHandle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public SelectionHandle(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }
}
