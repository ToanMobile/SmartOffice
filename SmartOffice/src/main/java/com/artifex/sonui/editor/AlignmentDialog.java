package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.artifex.R;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;
import java.lang.reflect.Array;

public class AlignmentDialog implements View.OnTouchListener, PopupWindow.OnDismissListener, View.OnClickListener {
    public final ImageButton[][] buttons = ((ImageButton[][]) Array.newInstance(ImageButton.class, new int[]{3, 3}));
    public final Point down = new Point();
    public final View mAnchor;
    public final Context mContext;
    public final ArDkDoc mDoc;
    public NUIPopupWindow popupWindow;
    public int[] start;

    public AlignmentDialog(Context context, ArDkDoc arDkDoc, View view) {
        this.mContext = context;
        this.mAnchor = view;
        this.mDoc = arDkDoc;
    }

    public void onClick(View view) {
        String[] split = ((String) view.getTag()).split(",");
        int parseInt = Integer.parseInt(split[0]);
        int parseInt2 = Integer.parseInt(split[1]);
        if (parseInt2 == 0) {
            ((SODoc) this.mDoc).setSelectionAlignment(0);
        } else if (parseInt2 == 1) {
            ((SODoc) this.mDoc).setSelectionAlignment(1);
        } else if (parseInt2 == 2) {
            ((SODoc) this.mDoc).setSelectionAlignment(2);
        }
        if (parseInt == 0) {
            ((SODoc) this.mDoc).setSelectionAlignmentV(0);
        } else if (parseInt == 1) {
            ((SODoc) this.mDoc).setSelectionAlignmentV(1);
        } else if (parseInt == 2) {
            ((SODoc) this.mDoc).setSelectionAlignmentV(2);
        }
        updateUI();
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
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_alignment_dialog, (ViewGroup) null);
        LinearLayout[] linearLayoutArr = {(LinearLayout) inflate.findViewById(R.id.row1), (LinearLayout) inflate.findViewById(R.id.row2), (LinearLayout) inflate.findViewById(R.id.row3)};
        for (int i = 0; i < 3; i++) {
            LinearLayout linearLayout = linearLayoutArr[i];
            int childCount = linearLayout.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                ImageButton imageButton = (ImageButton) linearLayout.getChildAt(i2);
                this.buttons[i][i2] = imageButton;
                imageButton.setTag("" + i + "," + i2);
                imageButton.setOnClickListener(this);
            }
        }
        updateUI();
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        this.popupWindow = nUIPopupWindow;
        nUIPopupWindow.setFocusable(true);
        inflate.measure(0, 0);
        Rect rect = new Rect();
        this.mAnchor.getGlobalVisibleRect(rect);
        this.popupWindow.showAtLocation(this.mAnchor, 51, rect.left, rect.bottom);
        this.popupWindow.setClippingEnabled(false);
        inflate.setOnTouchListener(this);
        this.popupWindow.setOnDismissListener(this);
    }

    public final void updateUI() {
        int selectionAlignment = ((SODoc) this.mDoc).getSelectionAlignment();
        int selectionAlignmentV = ((SODoc) this.mDoc).getSelectionAlignmentV();
        int i = 0;
        while (i < 3) {
            int i2 = 0;
            while (i2 < 3) {
                this.buttons[i][i2].setSelected(i2 == selectionAlignment && i == selectionAlignmentV);
                i2++;
            }
            i++;
        }
    }
}
