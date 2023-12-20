package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import kotlin.KotlinVersion;

public class DragHandle extends FrameLayout implements View.OnTouchListener {
    public static final int DRAG = 7;
    public static final int RESIZE_BOTTOM_LEFT = 5;
    public static final int RESIZE_BOTTOM_RIGHT = 6;
    public static final int RESIZE_TOP_LEFT = 3;
    public static final int RESIZE_TOP_RIGHT = 4;
    public static final int ROTATE = 8;
    public static final int SELECTION_BOTTOM_RIGHT = 2;
    public static final int SELECTION_TOP_LEFT = 1;
    public int downX;
    public int downY;
    public int dragSlop = 0;
    public int mDragDeltaX;
    public int mDragDeltaY;
    public DragHandleListener mDragHandleListener = null;
    public boolean mIsDragging = false;
    public int mKind = 0;
    public int mPositionX = 0;
    public int mPositionY = 0;

    public DragHandle(Context context, int i, int i2) {
        super(context);
        this.mKind = i2;
        View.inflate(context, i, this);
        setOnTouchListener(this);
        this.dragSlop = (int) context.getResources().getDimension(R.dimen.sodk_editor_drag_slop);
    }

    public int getKind() {
        return this.mKind;
    }

    public Point getPosition() {
        return new Point(this.mPositionX, this.mPositionY);
    }

    public boolean isDragKind() {
        return this.mKind == 7;
    }

    public boolean isResizeKind() {
        int i = this.mKind;
        return i == 3 || i == 4 || i == 5 || i == 6;
    }

    public boolean isRotateKind() {
        return this.mKind == 8;
    }

    public boolean isSelectionKind() {
        int i = this.mKind;
        return i == 1 || i == 2;
    }

    public void moveTo(int i, int i2) {
        offsetLeftAndRight(i - this.mPositionX);
        offsetTopAndBottom(i2 - this.mPositionY);
        this.mPositionX = i;
        this.mPositionY = i2;
        invalidate();
    }

    public Point offsetCircleToEdge() {
        int i;
        ImageView imageView = (ImageView) findViewById(R.id.handle_image);
        int i2 = 0;
        if (imageView != null) {
            int intrinsicHeight = (int) (((((double) imageView.getDrawable().getIntrinsicHeight()) * ((double) imageView.getScaleY())) * 0.707d) / 2.0d);
            int i3 = this.mKind;
            int i4 = (i3 == 1 || i3 == 3 || i3 == 5) ? -intrinsicHeight : intrinsicHeight;
            if (i3 == 1 || i3 == 3 || i3 == 4) {
                intrinsicHeight = -intrinsicHeight;
            }
            i = intrinsicHeight;
            i2 = i4;
        } else {
            i = 0;
        }
        return new Point(i2, i);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        offsetLeftAndRight(this.mPositionX);
        offsetTopAndBottom(this.mPositionY);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        DragHandleListener dragHandleListener;
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        int action = motionEvent.getAction() & KotlinVersion.MAX_COMPONENT_VALUE;
        if (action == 0) {
            Point position = getPosition();
            this.mDragDeltaX = rawX - position.x;
            this.mDragDeltaY = rawY - position.y;
            this.downX = rawX;
            this.downY = rawY;
        } else if (action == 1) {
            if (this.mIsDragging && (dragHandleListener = this.mDragHandleListener) != null) {
                dragHandleListener.onEndDrag(this);
            }
            this.mIsDragging = false;
        } else if (action == 2) {
            if (!this.mIsDragging) {
                int abs = Math.abs(rawX - this.downX);
                int abs2 = Math.abs(rawY - this.downY);
                int i = this.dragSlop;
                if (abs > i || abs2 > i) {
                    this.mIsDragging = true;
                    DragHandleListener dragHandleListener2 = this.mDragHandleListener;
                    if (dragHandleListener2 != null) {
                        dragHandleListener2.onStartDrag(this);
                    }
                }
            }
            if (this.mIsDragging) {
                moveTo(rawX - this.mDragDeltaX, rawY - this.mDragDeltaY);
                DragHandleListener dragHandleListener3 = this.mDragHandleListener;
                if (dragHandleListener3 != null) {
                    dragHandleListener3.onDrag(this);
                }
            }
        }
        return true;
    }

    public void setDragHandleListener(DragHandleListener dragHandleListener) {
        this.mDragHandleListener = dragHandleListener;
    }

    public void show(boolean z) {
        int visibility = getVisibility();
        int i = z ? View.VISIBLE : View.GONE;
        if (i != visibility) {
            setVisibility(i);
            requestLayout();
        }
    }
}
