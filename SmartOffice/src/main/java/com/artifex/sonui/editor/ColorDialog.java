package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.artifex.R;
import com.artifex.solib.ArDkDoc;

public class ColorDialog implements View.OnTouchListener, PopupWindow.OnDismissListener {
    public static final int BG_COLORS = 2;
    public static final int FG_COLORS = 1;
    public static final int FILL_COLORS = 3;
    public static final int LINE_COLORS = 4;
    public final Point down = new Point();
    public final View mAnchor;
    public String[] mColorList = {"#000000", "#FFFFFF", "#D8D8D8", "#808080", "#EEECE1", "#1F497D", "#0070C0", "#C0504D", "#9BBB59", "#8064A2", "#4BACC6", "#F79646", "#FF0000", "#FFFF00", "#DBE5F1", "#F2DCDB", "#EBF1DD", "#00B050"};
    public final Context mContext;
    public String mCurrentSelectionColor = null;
    public final int mDialogType;
    public final ArDkDoc mDoc;
    public final ColorChangedListener mListener;
    public boolean mShowTitle = true;
    public NUIPopupWindow popupWindow;
    public int[] start;

    public ColorDialog(int i, Context context, ArDkDoc arDkDoc, View view, ColorChangedListener colorChangedListener, String str, String[] strArr) {
        String[] strArr2 = strArr;
        this.mContext = context;
        this.mAnchor = view;
        this.mListener = colorChangedListener;
        this.mDialogType = i;
        this.mDoc = arDkDoc;
        this.mCurrentSelectionColor = str.toUpperCase();
        if (strArr2 != null) {
            this.mColorList = strArr2;
        }
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

    public void setShowTitle(boolean z) {
        this.mShowTitle = z;
    }

    public void show() {
        LinearLayout[] linearLayoutArr;
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_colors, (ViewGroup) null);
        SOTextView sOTextView = (SOTextView) inflate.findViewById(R.id.color_dialog_title);
        if (!this.mShowTitle) {
            sOTextView.setVisibility(View.GONE);
        } else if (this.mDialogType == 2) {
            sOTextView.setText((CharSequence) this.mContext.getString(R.string.sodk_editor_background));
        } else {
            sOTextView.setText((CharSequence) this.mContext.getString(R.string.sodk_editor_color));
        }
        int i = 3;
        int i2 = 0;
        LinearLayout[] linearLayoutArr2 = {(LinearLayout) inflate.findViewById(R.id.fontcolors_row1), (LinearLayout) inflate.findViewById(R.id.fontcolors_row2), (LinearLayout) inflate.findViewById(R.id.fontcolors_row3)};
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            LinearLayout linearLayout = linearLayoutArr2[i3];
            int childCount = linearLayout.getChildCount();
            int i5 = 0;
            while (i5 < childCount) {
                Button button = (Button) linearLayout.getChildAt(i5);
                int i6 = i4 + 1;
                if (i6 <= this.mColorList.length) {
                    button.setVisibility(i2);
                    GradientDrawable gradientDrawable = (GradientDrawable) ((StateListDrawable) button.getBackground().mutate()).getCurrent();
                    gradientDrawable.setColor(Color.parseColor(this.mColorList[i4]));
                    gradientDrawable.setStroke(4, Color.parseColor(this.mColorList[i4]));
                    button.setTag(this.mColorList[i4]);
                    if (this.mColorList[i4].toUpperCase().equals(this.mCurrentSelectionColor)) {
                        float[] fArr = new float[i];
                        Color.colorToHSV(Color.parseColor(this.mColorList[i4]), fArr);
                        linearLayoutArr = linearLayoutArr2;
                        if (((double) fArr[2]) < 0.6d) {
                            gradientDrawable.setStroke(4, -1);
                        } else {
                            gradientDrawable.setStroke(4, -16777216);
                        }
                    } else {
                        linearLayoutArr = linearLayoutArr2;
                    }
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            ColorDialog.this.mListener.onColorChanged((String) view.getTag());
                            ColorDialog.this.onDismiss();
                        }
                    });
                } else {
                    linearLayoutArr = linearLayoutArr2;
                    button.setVisibility(View.GONE);
                }
                i5++;
                i4 = i6;
                linearLayoutArr2 = linearLayoutArr;
                i = 3;
                i2 = 0;
            }
            LinearLayout[] linearLayoutArr3 = linearLayoutArr2;
            i3++;
            i = 3;
            i2 = 0;
        }
        Button button2 = (Button) inflate.findViewById(R.id.transparent_color_button);
        int i7 = this.mDialogType;
        if (i7 == 2 || i7 == 3) {
            button2.setVisibility(View.VISIBLE);
            if (this.mCurrentSelectionColor.equals("transparent".toUpperCase())) {
                ((GradientDrawable) ((LayerDrawable) button2.getBackground().mutate()).getDrawable(0)).setStroke(4, Color.parseColor("black"));
            }
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ColorDialog.this.mListener.onColorChanged((String) view.getTag());
                    ColorDialog.this.onDismiss();
                }
            });
        } else {
            button2.setVisibility(View.GONE);
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
