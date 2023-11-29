package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.ArrayList;
import java.util.Random;

public class RandomBarsAnimation extends ShapeAnimation {
    public ArrayList<Integer> positions = new ArrayList<>();
    public ArrayList<Integer> sizes = new ArrayList<>();

    public RandomBarsAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
        int i2;
        Random random = new Random();
        int i3 = 0;
        while (true) {
            if (this.mDirection.equals("horz")) {
                i2 = random.nextInt(this.mHeight / 25);
            } else {
                i2 = random.nextInt(this.mWidth / 25);
            }
            this.positions.add(new Integer(i3));
            this.sizes.add(new Integer(i2));
            i3 += i2;
            if (this.mDirection.equals("horz")) {
                if (i3 >= this.mHeight) {
                    return;
                }
            } else if (i3 >= this.mWidth) {
                return;
            }
        }
    }

    public void doStep(float f) {
        Path path = new Path();
        for (int i = 0; i < this.positions.size(); i++) {
            int intValue = this.positions.get(i).intValue();
            float intValue2 = ((float) this.sizes.get(i).intValue()) * f;
            if (this.mDirection.equals("horz")) {
                float f2 = (float) intValue;
                path.addRect(BitmapDescriptorFactory.HUE_RED, f2, (float) this.mWidth, f2 + intValue2, Path.Direction.CW);
            } else if (this.mDirection.equals("vert")) {
                float f3 = (float) intValue;
                path.addRect(f3, BitmapDescriptorFactory.HUE_RED, f3 + intValue2, (float) this.mHeight, Path.Direction.CW);
            }
        }
        this.mNewView.setClipPath(path);
        this.mNewView.invalidate();
    }
}
