package com.artifex.solib;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class SOAffineTransform {

    /* renamed from: a  reason: collision with root package name */
    public float f2132a = 1.0f;
    public float b = BitmapDescriptorFactory.HUE_RED;
    public float c = BitmapDescriptorFactory.HUE_RED;
    public float d = 1.0f;
    public float x = BitmapDescriptorFactory.HUE_RED;
    public float y = BitmapDescriptorFactory.HUE_RED;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || SOAffineTransform.class != obj.getClass()) {
            return false;
        }
        SOAffineTransform sOAffineTransform = (SOAffineTransform) obj;
        return Float.compare(sOAffineTransform.f2132a, this.f2132a) == 0 && Float.compare(sOAffineTransform.b, this.b) == 0 && Float.compare(sOAffineTransform.c, this.c) == 0 && Float.compare(sOAffineTransform.d, this.d) == 0 && Float.compare(sOAffineTransform.x, this.x) == 0 && Float.compare(sOAffineTransform.y, this.y) == 0;
    }

    public int hashCode() {
        float f = this.f2132a;
        int i = 0;
        int floatToIntBits = (f != BitmapDescriptorFactory.HUE_RED ? Float.floatToIntBits(f) : 0) * 31;
        float f2 = this.b;
        int floatToIntBits2 = (floatToIntBits + (f2 != BitmapDescriptorFactory.HUE_RED ? Float.floatToIntBits(f2) : 0)) * 31;
        float f3 = this.c;
        int floatToIntBits3 = (floatToIntBits2 + (f3 != BitmapDescriptorFactory.HUE_RED ? Float.floatToIntBits(f3) : 0)) * 31;
        float f4 = this.d;
        int floatToIntBits4 = (floatToIntBits3 + (f4 != BitmapDescriptorFactory.HUE_RED ? Float.floatToIntBits(f4) : 0)) * 31;
        float f5 = this.x;
        int floatToIntBits5 = (floatToIntBits4 + (f5 != BitmapDescriptorFactory.HUE_RED ? Float.floatToIntBits(f5) : 0)) * 31;
        float f6 = this.y;
        if (f6 != BitmapDescriptorFactory.HUE_RED) {
            i = Float.floatToIntBits(f6);
        }
        return floatToIntBits5 + i;
    }
}
