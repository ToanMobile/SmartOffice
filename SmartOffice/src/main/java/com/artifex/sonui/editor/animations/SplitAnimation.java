package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.Objects;

public class SplitAnimation extends ShapeAnimation {
    public SplitAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        int i = (int) (((float) this.mHeight) * f);
        int i2 = (int) (((float) this.mWidth) * f);
        Path path = new Path();
        String str = this.mDirection;
        Objects.requireNonNull(str);
        char c = 65535;
        switch (str.hashCode()) {
            case -1211507980:
                if (str.equals("horzin")) {
                    c = 0;
                    break;
                }
                break;
            case -819940842:
                if (str.equals("vertin")) {
                    c = 1;
                    break;
                }
                break;
            case 351643773:
                if (str.equals("vertout")) {
                    c = 2;
                    break;
                }
                break;
            case 1097964383:
                if (str.equals("horzout")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                int i3 = i / 2;
                path.addRect(BitmapDescriptorFactory.HUE_RED, (float) i3, (float) this.mWidth, (float) (this.mHeight - i3), Path.Direction.CW);
                break;
            case 1:
                int i4 = i2 / 2;
                path.addRect((float) i4, BitmapDescriptorFactory.HUE_RED, (float) (this.mWidth - i4), (float) this.mHeight, Path.Direction.CW);
                break;
            case 2:
                int i5 = i2 / 2;
                path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) ((this.mWidth / 2) - i5), (float) this.mHeight, Path.Direction.CW);
                int i6 = this.mWidth;
                path.addRect((float) ((i6 / 2) + i5), BitmapDescriptorFactory.HUE_RED, (float) i6, (float) this.mHeight, Path.Direction.CW);
                break;
            case 3:
                int i7 = i / 2;
                path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) ((this.mHeight / 2) - i7), Path.Direction.CW);
                int i8 = this.mHeight;
                path.addRect(BitmapDescriptorFactory.HUE_RED, (float) ((i8 / 2) + i7), (float) this.mWidth, (float) i8, Path.Direction.CW);
                break;
        }
        this.mOldView.setClipPath(path);
        this.mOldView.postInvalidate();
    }
}
