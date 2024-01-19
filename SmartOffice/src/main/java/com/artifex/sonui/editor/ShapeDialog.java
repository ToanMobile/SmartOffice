package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.artifex.R;

public class ShapeDialog implements View.OnTouchListener, PopupWindow.OnDismissListener {
    public final Point down = new Point();
    public final View mAnchor;
    public final Context mContext;
    public final onSelectShapeListener mListener;
    public NUIPopupWindow popupWindow;
    public final Shape[] shapes = {new Shape(this, "diamond", "Diamond", ""), new Shape(this, "speech1", "WedgeEllipseCallout", ""), new Shape(this, "speech2", "WedgeRectCallout", ""), new Shape(this, "pentagon", "Pentagon", ""), new Shape(this, "star", "Star", ""), new Shape(this, "circle", "Ellipse", ""), new Shape(this, "triangle1", "Triangle", ""), new Shape(this, "triangle2", "RtTriangle", ""), new Shape(this, "arrow1", "Arrow", ""), new Shape(this, "arrow2", "LeftRightArrow", ""), new Shape(this, "text", "TextBox", "fill-color:transparent"), new Shape(this, "line", "Line", ""), new Shape(this, "arrow-line", "Line", "end-decoration:\"arrow\""), new Shape(this, "square", "Rect", ""), new Shape(this, "rounded-square", "RoundRect", "")};
    public int[] start;

    public class Shape {
        public final String name;
        public final String properties;
        public final String shape;

        public Shape(ShapeDialog shapeDialog, String str, String str2, String str3) {
            this.name = str;
            this.shape = str2;
            this.properties = str3;
        }
    }

    public interface onSelectShapeListener {
        void onSelectShape(Shape shape);
    }

    public ShapeDialog(Context context, View view, onSelectShapeListener onselectshapelistener) {
        this.mContext = context;
        this.mAnchor = view;
        this.mListener = onselectshapelistener;
    }

    public void onDismiss() {
        this.popupWindow.dismiss();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            int[] iArr = new int[2];
            this.start = iArr;
            this.popupWindow.getLocationInWindow(iArr);
            this.down.set((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
        } else if (action == 2) {
            int rawX = this.down.x - ((int) motionEvent.getRawX());
            int rawY = this.down.y - ((int) motionEvent.getRawY());
            NUIPopupWindow nUIPopupWindow = this.popupWindow;
            int[] iArr2 = this.start;
            nUIPopupWindow.update(iArr2[0] - rawX, iArr2[1] - rawY, -1, -1, true);
        }
        return true;
    }

    public void show() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_shape_dialog, (ViewGroup) null);
        ((SOTextView) inflate.findViewById(R.id.shape_dialog_title)).setText((CharSequence) this.mContext.getString(R.string.sodk_editor_shape_upper));
        LinearLayout[] linearLayoutArr = {(LinearLayout) inflate.findViewById(R.id.row1), (LinearLayout) inflate.findViewById(R.id.row2), (LinearLayout) inflate.findViewById(R.id.row3)};
        for (int i = 0; i < 3; i++) {
            LinearLayout linearLayout = linearLayoutArr[i];
            int childCount = linearLayout.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                ((ImageButton) linearLayout.getChildAt(i2)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        String str = (String) view.getTag();
                        int length = ShapeDialog.this.shapes.length;
                        for (int i = 0; i < length; i++) {
                            if (ShapeDialog.this.shapes[i].name.equalsIgnoreCase(str)) {
                                ShapeDialog shapeDialog = ShapeDialog.this;
                                shapeDialog.mListener.onSelectShape(shapeDialog.shapes[i]);
                                ShapeDialog.this.popupWindow.dismiss();
                                return;
                            }
                        }
                    }
                });
            }
        }
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(this.mContext);
        this.popupWindow = nUIPopupWindow;
        nUIPopupWindow.setContentView(inflate);
        this.popupWindow.setClippingEnabled(false);
        inflate.setOnTouchListener(this);
        this.popupWindow.setOnDismissListener(this);
        this.popupWindow.setFocusable(true);
        inflate.measure(0, 0);
        this.popupWindow.setWidth(inflate.getMeasuredWidth());
        this.popupWindow.setHeight(inflate.getMeasuredHeight());
        this.popupWindow.showAtLocation(this.mAnchor, 51, 50, 50);
    }
}
