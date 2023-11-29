package com.artifex.mupdf.fitz;

import a.a.a.a.a.c$$ExternalSyntheticOutline0;

public class ColorSpace {
    public static ColorSpace DeviceBGR = new ColorSpace(nativeDeviceBGR());
    public static ColorSpace DeviceCMYK = new ColorSpace(nativeDeviceCMYK());
    public static ColorSpace DeviceGray = new ColorSpace(nativeDeviceGray());
    public static ColorSpace DeviceRGB = new ColorSpace(nativeDeviceRGB());
    private long pointer;

    static {
        Context.init();
    }

    private ColorSpace(long j) {
        this.pointer = j;
    }

    public static ColorSpace fromPointer(long j) {
        ColorSpace colorSpace = DeviceGray;
        if (j == colorSpace.pointer) {
            return colorSpace;
        }
        ColorSpace colorSpace2 = DeviceRGB;
        if (j == colorSpace2.pointer) {
            return colorSpace2;
        }
        ColorSpace colorSpace3 = DeviceBGR;
        if (j == colorSpace3.pointer) {
            return colorSpace3;
        }
        ColorSpace colorSpace4 = DeviceCMYK;
        if (j == colorSpace4.pointer) {
            return colorSpace4;
        }
        return new ColorSpace(j);
    }

    private static native long nativeDeviceBGR();

    private static native long nativeDeviceCMYK();

    private static native long nativeDeviceGray();

    private static native long nativeDeviceRGB();

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public native int getNumberOfComponents();

    public String toString() {
        if (this == DeviceGray) {
            return "DeviceGray";
        }
        if (this == DeviceRGB) {
            return "DeviceRGB";
        }
        if (this == DeviceBGR) {
            return "DeviceBGR";
        }
        if (this == DeviceCMYK) {
            return "DeviceCMYK";
        }
        StringBuilder m = c$$ExternalSyntheticOutline0.m("ColorSpace(");
        m.append(getNumberOfComponents());
        m.append(")");
        return m.toString();
    }
}
