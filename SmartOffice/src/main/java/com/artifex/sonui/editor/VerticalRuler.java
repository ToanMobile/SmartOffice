package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class VerticalRuler extends Ruler {
    public float effectiveXScale;
    public Rect visRect = new Rect();

    public VerticalRuler(Context context) {
        super(context);
    }

    public float getEffectiveScale() {
        return this.effectiveXScale;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float[] fArr = this.mGraduations;
        if (fArr != null && fArr.length != 0) {
            getLocalVisibleRect(this.visRect);
            float f = BitmapDescriptorFactory.HUE_RED;
            int i = 0;
            boolean z = false;
            while (true) {
                float[] fArr2 = this.mGraduations;
                if (i < fArr2.length) {
                    float round = (float) Math.round(fArr2[i]);
                    float f2 = this.mScale;
                    int i2 = this.mOffsetY;
                    int i3 = ((int) (f * f2)) - i2;
                    int i4 = ((int) (f2 * round)) - i2;
                    if (i3 != i4) {
                        this.mRect.set(0, i3, getWidth(), i4);
                        if (canvas.getClipBounds().intersect(this.mRect)) {
                            z = true;
                        } else if (z) {
                            return;
                        }
                        canvas.drawRect(this.mRect, this.mPaint);
                        if (i > 0) {
                            Canvas canvas2 = canvas;
                            drawTextCentered(canvas2, this.mPaint, String.valueOf(i), (float) (getWidth() / 2), (float) ((i3 + i4) / 2));
                        }
                        f = round;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void onUpdate() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        LinearLayout linearLayout = (LinearLayout) ((Activity) getContext()).findViewById(R.id.hruler_spacer);
        if (layoutParams != null && linearLayout != null && linearLayout.getLayoutParams() != null) {
            Point realScreenSize = Utilities.getRealScreenSize(getContext());
            int i = realScreenSize.x;
            int i2 = realScreenSize.y;
            int dimension = (int) (((float) ((int) getResources().getDimension(R.dimen.sodk_editor_vruler_base_size))) * this.mScale);
            int min = Math.min(dimension, (((int) Math.sqrt((double) ((i2 * i2) + (i * i)))) * 7) / 100);
            this.effectiveXScale = (this.mScale * ((float) min)) / ((float) dimension);
            layoutParams.width = min;
            requestLayout();
            invalidate();
            linearLayout.getLayoutParams().width = min;
            linearLayout.requestLayout();
            linearLayout.invalidate();
        }
    }

    public VerticalRuler(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VerticalRuler(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
