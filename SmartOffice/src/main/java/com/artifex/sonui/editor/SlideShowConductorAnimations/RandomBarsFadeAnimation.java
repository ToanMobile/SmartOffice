package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;

import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.Random;

public class RandomBarsFadeAnimation extends ShapeFadeAnimation {
    public ArrayList<Integer> positions = null;
    public ArrayList<Integer> sizes = null;

    public RandomBarsFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
        this.subType = 10;
    }

    public void doStep(float f) {
        int i;
        if (this.positions == null) {
            Random random = new Random();
            this.positions = new ArrayList<>();
            this.sizes = new ArrayList<>();
            int i2 = 0;
            while (true) {
                if (this.subType == 10) {
                    i = random.nextInt(this.mHeight / 25);
                } else {
                    i = random.nextInt(this.mWidth / 25);
                }
                this.positions.add(Integer.valueOf(i2));
                this.sizes.add(Integer.valueOf(i));
                i2 += i;
                if (this.subType == 10) {
                    if (i2 >= this.mHeight) {
                        break;
                    }
                } else if (i2 >= this.mWidth) {
                    break;
                }
            }
        }
        Path path = new Path();
        if (this.transitionType == 1) {
            f = 1.0f - f;
        }
        for (int i3 = 0; i3 < this.positions.size(); i3++) {
            int intValue = this.positions.get(i3).intValue();
            float intValue2 = ((float) this.sizes.get(i3).intValue()) * f;
            int i4 = this.subType;
            if (i4 == 10) {
                float f2 = (float) intValue;
                path.addRect(BitmapDescriptorFactory.HUE_RED, f2, (float) this.mWidth, f2 + intValue2, Path.Direction.CW);
            } else if (i4 == 5) {
                float f3 = (float) intValue;
                path.addRect(f3, BitmapDescriptorFactory.HUE_RED, f3 + intValue2, (float) this.mHeight, Path.Direction.CW);
            }
        }
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.invalidate();
        }
    }
}
