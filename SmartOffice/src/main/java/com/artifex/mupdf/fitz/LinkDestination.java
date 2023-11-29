package com.artifex.mupdf.fitz;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class LinkDestination extends Location {
    public static final int LINK_DEST_FIT = 0;
    public static final int LINK_DEST_FIT_B = 1;
    public static final int LINK_DEST_FIT_BH = 3;
    public static final int LINK_DEST_FIT_BV = 5;
    public static final int LINK_DEST_FIT_H = 2;
    public static final int LINK_DEST_FIT_R = 6;
    public static final int LINK_DEST_FIT_V = 4;
    public static final int LINK_DEST_XYZ = 7;
    public float height = this.height;
    public int type;
    public float width = this.width;
    public float x;
    public float y;
    public float zoom;

    public LinkDestination(int i, int i2, int i3, float f, float f2, float f3, float f4, float f5) {
        super(i, i2);
        this.type = i3;
        this.x = f;
        this.y = f2;
        this.zoom = f5;
    }

    public static LinkDestination Fit(int i, int i2) {
        return new LinkDestination(i, i2, 0, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public static LinkDestination FitB(int i, int i2) {
        return new LinkDestination(i, i2, 1, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public static LinkDestination FitBH(int i, int i2, float f) {
        return new LinkDestination(i, i2, 3, BitmapDescriptorFactory.HUE_RED, f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public static LinkDestination FitBV(int i, int i2, float f) {
        return new LinkDestination(i, i2, 5, f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public static LinkDestination FitH(int i, int i2, float f) {
        return new LinkDestination(i, i2, 2, BitmapDescriptorFactory.HUE_RED, f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public static LinkDestination FitR(int i, int i2, float f, float f2, float f3, float f4) {
        return new LinkDestination(i, i2, 6, f, f2, f3, f4, BitmapDescriptorFactory.HUE_RED);
    }

    public static LinkDestination FitV(int i, int i2, float f) {
        return new LinkDestination(i, i2, 4, f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public static LinkDestination XYZ(int i, int i2, float f, float f2, float f3) {
        return new LinkDestination(i, i2, 7, f, f2, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, f3);
    }
}
