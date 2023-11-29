package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.Objects;

public class StripsAnimation extends ShapeAnimation {
    public StripsAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        Path path = new Path();
        String str = this.mDirection;
        Objects.requireNonNull(str);
        char c = 65535;
        switch (str.hashCode()) {
            case 3448:
                if (str.equals("ld")) {
                    c = 0;
                    break;
                }
                break;
            case 3465:
                if (str.equals("lu")) {
                    c = 1;
                    break;
                }
                break;
            case 3634:
                if (str.equals("rd")) {
                    c = 2;
                    break;
                }
                break;
            case 3651:
                if (str.equals("ru")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                path.moveTo((float) this.mWidth, BitmapDescriptorFactory.HUE_RED);
                path.lineTo((float) this.mWidth, ((float) (this.mHeight * 2)) * f);
                int i = this.mWidth;
                path.lineTo(((float) i) - (((float) (i * 2)) * f), BitmapDescriptorFactory.HUE_RED);
                path.lineTo((float) this.mWidth, BitmapDescriptorFactory.HUE_RED);
                path.close();
                break;
            case 1:
                path.moveTo((float) this.mWidth, (float) this.mHeight);
                int i2 = this.mHeight;
                path.lineTo((float) this.mWidth, ((float) i2) - (((float) (i2 * 2)) * f));
                int i3 = this.mWidth;
                path.lineTo(((float) i3) - (((float) (i3 * 2)) * f), (float) this.mHeight);
                path.lineTo((float) this.mWidth, (float) this.mHeight);
                path.close();
                break;
            case 2:
                path.moveTo(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
                path.lineTo(BitmapDescriptorFactory.HUE_RED, ((float) (this.mHeight * 2)) * f);
                path.lineTo(((float) (this.mWidth * 2)) * f, BitmapDescriptorFactory.HUE_RED);
                path.lineTo(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
                path.close();
                break;
            case 3:
                path.moveTo(BitmapDescriptorFactory.HUE_RED, (float) this.mHeight);
                int i4 = this.mHeight;
                path.lineTo(BitmapDescriptorFactory.HUE_RED, ((float) i4) - (((float) (i4 * 2)) * f));
                path.lineTo(((float) (this.mWidth * 2)) * f, (float) this.mHeight);
                path.lineTo(BitmapDescriptorFactory.HUE_RED, (float) this.mHeight);
                path.close();
                break;
        }
        this.mNewView.setClipPath(path);
        this.mNewView.postInvalidate();
    }
}
