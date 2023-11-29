package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class HorizontalRuler extends Ruler {
    public float effectiveYScale;
    public Rect visRect = new Rect();

    public HorizontalRuler(Context context) {
        super(context);
    }

    public float getEffectiveScale() {
        return this.effectiveYScale;
    }

    public void onDraw(Canvas canvas) {
        boolean z;
        String str;
        super.onDraw(canvas);
        float[] fArr = this.mGraduations;
        if (fArr != null && fArr.length != 0) {
            getLocalVisibleRect(this.visRect);
            float f = BitmapDescriptorFactory.HUE_RED;
            boolean z2 = false;
            int i = 0;
            while (true) {
                float[] fArr2 = this.mGraduations;
                if (i < fArr2.length) {
                    float round = (float) Math.round(fArr2[i]);
                    float f2 = this.mScale;
                    int i2 = this.mOffsetX;
                    int i3 = ((int) (f * f2)) - i2;
                    int i4 = ((int) (f2 * round)) - i2;
                    if (i3 != i4) {
                        this.mRect.set(i3, 0, i4, getHeight());
                        if (canvas.getClipBounds().intersect(this.mRect)) {
                            z = true;
                        } else if (!z2) {
                            z = z2;
                        } else {
                            return;
                        }
                        this.mRect.set(i3, 0, i4, getHeight());
                        canvas.drawRect(this.mRect, this.mPaint);
                        int i5 = i - 1;
                        String str2 = new String("");
                        while (true) {
                            str = String.valueOf((char) ((i5 % 26) + 65)) + str2;
                            i5 = (i5 / 26) - 1;
                            if (i5 < 0) {
                                break;
                            }
                            str2 = str;
                        }
                        drawTextCentered(canvas, this.mPaint, str, (float) ((i3 + i4) / 2), (float) (getHeight() / 2));
                        f = round;
                        z2 = z;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void onUpdate() {
        ViewGroup.LayoutParams layoutParams;
        ViewGroup.LayoutParams layoutParams2 = getLayoutParams();
        LinearLayout linearLayout = (LinearLayout) getParent();
        if (layoutParams2 != null && linearLayout != null && (layoutParams = linearLayout.getLayoutParams()) != null) {
            Point realScreenSize = Utilities.getRealScreenSize(getContext());
            int i = realScreenSize.x;
            int i2 = realScreenSize.y;
            int dimension = (int) (((float) ((int) getResources().getDimension(R.dimen.sodk_editor_hruler_base_size))) * this.mScale);
            int min = Math.min(dimension, (((int) Math.sqrt((double) ((i2 * i2) + (i * i)))) * 7) / 100);
            this.effectiveYScale = (this.mScale * ((float) min)) / ((float) dimension);
            layoutParams2.height = min;
            requestLayout();
            invalidate();
            layoutParams.height = min;
            linearLayout.requestLayout();
            invalidate();
        }
    }

    public HorizontalRuler(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public HorizontalRuler(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
