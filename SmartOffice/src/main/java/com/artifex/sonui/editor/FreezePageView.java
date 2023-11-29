package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import com.artifex.solib.ArDkDoc;

public class FreezePageView extends DocExcelPageView {
    public int lastLayoutLeft = -1;
    public int lastLayoutTop = -1;
    public int maxScrollX;
    public int maxScrollY;
    public int minScrollX;
    public int minScrollY;
    public int scrollX;
    public int scrollY;

    public FreezePageView(Context context, ArDkDoc arDkDoc) {
        super(context, arDkDoc);
    }

    public void layout(int i, int i2, int i3, int i4) {
        super.layout(i, i2, i3, i4);
        setChildRect(new Rect(i, i2, i3, i4));
        int i5 = this.lastLayoutLeft;
        if (!(i5 == -1 || i5 == i)) {
            this.scrollX -= i5 - i;
        }
        this.lastLayoutLeft = i;
        int i6 = this.lastLayoutTop;
        if (!(i6 == -1 || i6 == i2)) {
            this.scrollY -= i6 - i2;
        }
        this.lastLayoutTop = i2;
    }

    public Point pageToScreen(Point point) {
        Point pageToView = pageToView(point.x, point.y);
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        pageToView.x += iArr[0];
        pageToView.y += iArr[1];
        return pageToView;
    }

    public Point pageToView(int i, int i2) {
        Point point = new Point(pageToView(i), pageToView(i2));
        point.x -= this.scrollX;
        point.y -= this.scrollY;
        return point;
    }

    public final int scaleInt(int i, float f, float f2) {
        return (int) ((((double) i) * ((double) f2)) / ((double) f));
    }

    public Point screenToPage(int i, int i2) {
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        int[] screenToWindow = Utilities.screenToWindow(iArr, getContext());
        int i3 = i2 - screenToWindow[1];
        int i4 = (i - screenToWindow[0]) + this.scrollX;
        int i5 = i3 + this.scrollY;
        double factor = getFactor();
        return new Point((int) (((double) i4) / factor), (int) (((double) i5) / factor));
    }

    public void scrollBy(int i, int i2) {
        int i3 = this.scrollX + i;
        this.scrollX = i3;
        int i4 = this.scrollY + i2;
        this.scrollY = i4;
        int i5 = this.minScrollX;
        if (i3 < i5) {
            this.scrollX = i5;
        }
        int i6 = this.minScrollY;
        if (i4 < i6) {
            this.scrollY = i6;
        }
        if (i > 0) {
            if (getWidth() + this.scrollX > this.maxScrollX) {
                this.scrollX -= i;
            }
        }
        if (i2 > 0) {
            if (getHeight() + this.scrollY > this.maxScrollY) {
                this.scrollY -= i2;
            }
        }
    }

    public void setNewScale(float f) {
        this.scrollX = scaleInt(this.scrollX, this.mScale, f);
        this.scrollY = scaleInt(this.scrollY, this.mScale, f);
        this.minScrollX = scaleInt(this.minScrollX, this.mScale, f);
        this.minScrollY = scaleInt(this.minScrollY, this.mScale, f);
        this.maxScrollX = scaleInt(this.maxScrollX, this.mScale, f);
        this.maxScrollY = scaleInt(this.maxScrollY, this.mScale, f);
        this.mScale = f;
        scrollBy(0, 0);
    }

    public void setOrigin() {
        this.mRenderOrigin.set((float) (-this.scrollX), (float) (-this.scrollY));
    }

    public void setScrollingLimits(int i, int i2, int i3, int i4) {
        this.minScrollX = i;
        this.minScrollY = i2;
        this.maxScrollX = i3;
        this.maxScrollY = i4;
    }
}
