package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.Objects;

public class WipeAnimation extends ShapeAnimation {
    public WipeAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        int i = (int) (((float) this.mHeight) * f);
        int i2 = (int) (((float) this.mWidth) * f);
        Path path = new Path();
        Path path2 = new Path();
        String str = this.mDirection;
        Objects.requireNonNull(str);
        char c = 65535;
        switch (str.hashCode()) {
            case 100:
                if (str.equals("d")) {
                    c = 0;
                    break;
                }
                break;
            case 108:
                if (str.equals("l")) {
                    c = 1;
                    break;
                }
                break;
            case 114:
                if (str.equals("r")) {
                    c = 2;
                    break;
                }
                break;
            case 117:
                if (str.equals("u")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                float f2 = (float) i;
                path.addRect(BitmapDescriptorFactory.HUE_RED, f2, (float) this.mWidth, (float) this.mHeight, Path.Direction.CW);
                path2.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, f2, Path.Direction.CW);
                break;
            case 1:
                path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) (this.mWidth - i2), (float) this.mHeight, Path.Direction.CW);
                int i3 = this.mWidth;
                path2.addRect((float) (i3 - i2), BitmapDescriptorFactory.HUE_RED, (float) i3, (float) this.mHeight, Path.Direction.CW);
                break;
            case 2:
                float f3 = (float) i2;
                path.addRect(f3, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) this.mHeight, Path.Direction.CW);
                path2.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, f3, (float) this.mHeight, Path.Direction.CW);
                break;
            case 3:
                path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) (this.mHeight - i), Path.Direction.CW);
                int i4 = this.mHeight;
                path2.addRect(BitmapDescriptorFactory.HUE_RED, (float) (i4 - i), (float) this.mWidth, (float) i4, Path.Direction.CW);
                break;
        }
        this.mOldView.setClipPath(path);
        this.mOldView.postInvalidate();
        this.mNewView.setClipPath(path2);
        this.mNewView.postInvalidate();
    }
}
