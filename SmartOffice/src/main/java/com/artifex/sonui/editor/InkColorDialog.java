package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class InkColorDialog implements View.OnTouchListener, PopupWindow.OnDismissListener {
    public static final int BG_COLORS = 2;
    public static final int FG_COLORS = 1;
    public final Point down = new Point();
    public final View mAnchor;
    public boolean mAutoDismiss = false;
    public final Context mContext;
    public final int mCurrentColor;
    public final int mDialogType;
    public final String[] mFgColors = {"#000000", "#FFFFFF", "#D8D8D8", "#808080", "#EEECE1", "#1F497D", "#0070C0", "#C0504D", "#9BBB59", "#8064A2", "#4BACC6", "#F79646", "#FF0000", "#FFFF00", "#DBE5F1", "#F2DCDB", "#EBF1DD", "#00B050"};
    public final ColorChangedListener mListener;
    public boolean mShowTitle = true;
    public NUIPopupWindow popupWindow;
    public int[] start;

    public interface ColorChangedListener {
        void onColorChanged(String str);
    }

    public InkColorDialog(int i, int i2, Context context, View view, ColorChangedListener colorChangedListener, boolean z) {
        this.mContext = context;
        this.mAnchor = view;
        this.mListener = colorChangedListener;
        this.mDialogType = i;
        this.mAutoDismiss = z;
        this.mCurrentColor = i2;
    }

    public void dismiss() {
        this.popupWindow.dismiss();
    }

    public void onDismiss() {
        dismiss();
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

    public void setShowTitle(boolean z) {
        this.mShowTitle = z;
    }

    public void show() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_colors, (ViewGroup) null);
        SOTextView sOTextView = (SOTextView) inflate.findViewById(R.id.color_dialog_title);
        if (!this.mShowTitle) {
            sOTextView.setVisibility(8);
        } else if (this.mDialogType == 2) {
            sOTextView.setText((CharSequence) this.mContext.getString(R.string.sodk_editor_background));
        } else {
            sOTextView.setText((CharSequence) this.mContext.getString(R.string.sodk_editor_color));
        }
        String[] strArr = this.mFgColors;
        int i = 3;
        int i2 = 0;
        LinearLayout[] linearLayoutArr = {(LinearLayout) inflate.findViewById(R.id.fontcolors_row1), (LinearLayout) inflate.findViewById(R.id.fontcolors_row2), (LinearLayout) inflate.findViewById(R.id.fontcolors_row3)};
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            LinearLayout linearLayout = linearLayoutArr[i3];
            int childCount = linearLayout.getChildCount();
            int i5 = 0;
            while (i5 < childCount) {
                Button button = (Button) linearLayout.getChildAt(i5);
                int i6 = i4 + 1;
                if (i6 <= strArr.length) {
                    button.setVisibility(i2);
                    GradientDrawable gradientDrawable = (GradientDrawable) ((StateListDrawable) button.getBackground().mutate()).getCurrent();
                    gradientDrawable.setColor(Color.parseColor(strArr[i4]));
                    gradientDrawable.setStroke(4, Color.parseColor(strArr[i4]));
                    button.setTag(strArr[i4]);
                    if (Color.parseColor(strArr[i4].toUpperCase()) == this.mCurrentColor) {
                        float[] fArr = new float[i];
                        Color.colorToHSV(Color.parseColor(strArr[i4]), fArr);
                        if (((double) fArr[2]) < 0.6d) {
                            gradientDrawable.setStroke(4, -1);
                        } else {
                            gradientDrawable.setStroke(4, -16777216);
                        }
                    }
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            InkColorDialog.this.mListener.onColorChanged((String) view.getTag());
                            InkColorDialog inkColorDialog = InkColorDialog.this;
                            if (inkColorDialog.mAutoDismiss) {
                                inkColorDialog.dismiss();
                            }
                        }
                    });
                } else {
                    button.setVisibility(8);
                }
                i5++;
                i4 = i6;
                i = 3;
                i2 = 0;
            }
            i3++;
            i = 3;
            i2 = 0;
        }
        Button button2 = (Button) inflate.findViewById(R.id.transparent_color_button);
        if (this.mDialogType == 2) {
            button2.setVisibility(0);
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    InkColorDialog.this.mListener.onColorChanged((String) view.getTag());
                }
            });
        } else {
            button2.setVisibility(8);
        }
        int i7 = Utilities.getScreenSize(this.mContext).x;
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        this.popupWindow = nUIPopupWindow;
        nUIPopupWindow.setFocusable(true);
        inflate.measure(0, 0);
        this.popupWindow.showAtLocation(this.mAnchor, 51, (i7 - inflate.getMeasuredWidth()) - 15, 100);
        this.popupWindow.setClippingEnabled(false);
        inflate.setOnTouchListener(this);
        this.popupWindow.setOnDismissListener(this);
    }
}
