package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import java.util.Objects;

public class WheelAnimation extends ShapeAnimation {
    public WheelAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public final void doSpokes(int i, Path path, RectF rectF, float f) {
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = 360 / i;
            path.moveTo(rectF.centerX(), rectF.centerY());
            path.arcTo(rectF, (float) ((i2 * i3) - 90), (((float) i3) * f) - 0.05f);
            path.lineTo(rectF.centerX(), rectF.centerY());
        }
    }

    public void doStep(float f) {
        Path path = new Path();
        int i = this.mHeight;
        int i2 = this.mWidth;
        int sqrt = (int) Math.sqrt((double) ((i2 * i2) + (i * i)));
        int i3 = this.mWidth;
        int i4 = this.mHeight;
        RectF rectF = new RectF((float) ((i3 / 2) - sqrt), (float) ((i4 / 2) - sqrt), (float) ((i3 / 2) + sqrt), (float) ((i4 / 2) + sqrt));
        String str = this.mDirection;
        Objects.requireNonNull(str);
        char c = 65535;
        switch (str.hashCode()) {
            case -1055763538:
                if (str.equals("threespoke")) {
                    c = 0;
                    break;
                }
                break;
            case 338780454:
                if (str.equals("fourspoke")) {
                    c = 1;
                    break;
                }
                break;
            case 1185388381:
                if (str.equals("eightspoke")) {
                    c = 2;
                    break;
                }
                break;
            case 1828778944:
                if (str.equals("twospoke")) {
                    c = 3;
                    break;
                }
                break;
            case 2020771814:
                if (str.equals("onespoke")) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                doSpokes(3, path, rectF, f);
                break;
            case 1:
                doSpokes(4, path, rectF, f);
                break;
            case 2:
                doSpokes(8, path, rectF, f);
                break;
            case 3:
                doSpokes(2, path, rectF, f);
                break;
            case 4:
                doSpokes(1, path, rectF, f);
                break;
        }
        this.mNewView.setClipPath(path);
        this.mNewView.invalidate();
    }
}
