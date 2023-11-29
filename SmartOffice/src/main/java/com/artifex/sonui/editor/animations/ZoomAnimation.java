package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.Objects;

public class ZoomAnimation extends ShapeAnimation {
    public ZoomAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        Path path = new Path();
        String str = this.mDirection;
        Objects.requireNonNull(str);
        if (str.equals("in")) {
            RectF rectF = new RectF(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) this.mHeight);
            rectF.inset((((float) this.mWidth) * f) / 2.0f, (((float) this.mHeight) * f) / 2.0f);
            path.addRect(rectF, Path.Direction.CW);
        } else if (str.equals("out")) {
            RectF rectF2 = new RectF(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) this.mHeight);
            path.addRect(rectF2, Path.Direction.CW);
            float f2 = 1.0f - f;
            rectF2.inset((((float) this.mWidth) * f2) / 2.0f, (((float) this.mHeight) * f2) / 2.0f);
            path.addRect(rectF2, Path.Direction.CW);
            path.setFillType(Path.FillType.EVEN_ODD);
        }
        this.mOldView.setClipPath(path);
        this.mOldView.postInvalidate();
    }
}
