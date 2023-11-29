package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ClippedImageView extends ImageView {
    public Path clipPath;

    public ClippedImageView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public void onDraw(Canvas canvas) {
        if (this.clipPath != null) {
            canvas.save();
        }
        Path path = this.clipPath;
        if (path != null) {
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
        if (this.clipPath != null) {
            canvas.restore();
        }
    }

    public void setClipPath(Path path) {
        this.clipPath = path;
    }

    public ClippedImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ClippedImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.clipPath = null;
        setWillNotDraw(false);
    }
}
