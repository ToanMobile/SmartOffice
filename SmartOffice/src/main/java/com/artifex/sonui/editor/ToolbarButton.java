package com.artifex.sonui.editor;

import a.a.a.a.b.f.a$$ExternalSyntheticOutline0;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class ToolbarButton extends Button {
    public float mTextSize;

    public ToolbarButton(Context context) {
        super(context);
        init();
    }

    public static int getMaxWidth(ToolbarButton[] toolbarButtonArr) {
        int length = toolbarButtonArr.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (toolbarButtonArr[i2] != null) {
                toolbarButtonArr[i2].measure(-2, -2);
                int measuredWidth = toolbarButtonArr[i2].getMeasuredWidth();
                if (measuredWidth > i) {
                    i = measuredWidth;
                }
            }
        }
        return i;
    }

    public static void setAllSameSize(int i, ToolbarButton[] toolbarButtonArr) {
        int length = toolbarButtonArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (toolbarButtonArr[i2] != null) {
                toolbarButtonArr[i2].setWidth(i);
            }
        }
    }

    public void hideText() {
        setTextSize(BitmapDescriptorFactory.HUE_RED);
    }

    public final void init() {
        this.mTextSize = getTextSize();
    }

    public void setDrawableColor(int i) {
        Drawable[] compoundDrawables = getCompoundDrawables();
        for (int i2 = 0; i2 < compoundDrawables.length; i2++) {
            if (compoundDrawables[i2] != null) {
                DrawableCompat.Api21Impl.setTint(compoundDrawables[i2], i);
            }
        }
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        if (z) {
            setAlpha(1.0f);
        } else {
            setAlpha(0.4f);
        }
    }

    public void setImageResource(int i) {
        setCompoundDrawablesWithIntrinsicBounds((Drawable) null, ContextCompat.getDrawable(getContext(), i), (Drawable) null, (Drawable) null);
    }

    public void showText() {
        setTextSize(0, this.mTextSize);
    }

    public ToolbarButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public static void setAllSameSize(ToolbarButton[] toolbarButtonArr) {
        int measuredWidth;
        int length = toolbarButtonArr.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (toolbarButtonArr[i2] != null) {
                String[] split = toolbarButtonArr[i2].getText().toString().split(" ");
                if (split.length == 2) {
                    toolbarButtonArr[i2].setText(split[0] + "\n" + split[1]);
                } else if (split.length == 3) {
                    toolbarButtonArr[i2].setText(split[0] + " " + split[1] + "\n" + split[2]);
                }
                i++;
            }
        }
        if (i > 0) {
            for (int i3 = 0; i3 < length; i3++) {
                if (toolbarButtonArr[i3] != null) {
                    String charSequence = toolbarButtonArr[i3].getText().toString();
                    if (!charSequence.contains("\n")) {
                        toolbarButtonArr[i3].setText(a$$ExternalSyntheticOutline0.m(charSequence, "\n"));
                    }
                }
            }
        }
        int i4 = 0;
        for (int i5 = 0; i5 < length; i5++) {
            if (toolbarButtonArr[i5] != null && (measuredWidth = toolbarButtonArr[i5].getMeasuredWidth()) > i4) {
                i4 = measuredWidth;
            }
        }
        setAllSameSize(i4, toolbarButtonArr);
    }

    public ToolbarButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }
}
