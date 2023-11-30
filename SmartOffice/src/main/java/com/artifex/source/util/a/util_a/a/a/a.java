package com.artifex.source.util.a.util_a.a.a;

public class a {

    /* renamed from: a  reason: collision with root package name */
    public static int f393a = -1;

    public static String encodeByte(byte[] bArr) {
        int i;
        int length = bArr.length;
        StringBuffer stringBuffer = new StringBuffer(((length / 3) + 1) * 4);
        int i2 = 0;
        while (i2 < length) {
            stringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt((bArr[i2] >> 2) & 63));
            int i3 = (bArr[i2] << 4) & 63;
            int i4 = i2 + 1;
            if (i4 < length) {
                i3 |= (bArr[i4] >> 4) & 15;
            }
            stringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(i3));
            if (i4 < length) {
                int i5 = (bArr[i4] << 2) & 63;
                i = i4 + 1;
                if (i < length) {
                    i5 |= (bArr[i] >> 6) & 3;
                }
                stringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(i5));
            } else {
                i = i4 + 1;
                stringBuffer.append('=');
            }
            if (i < length) {
                stringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(bArr[i] & 63));
            } else {
                stringBuffer.append('=');
            }
            i2 = i + 1;
        }
        String stringBuffer2 = stringBuffer.toString();
        int length2 = stringBuffer2.length();
        byte[] bArr2 = new byte[length2];
        for (int i6 = 0; i6 < length2; i6++) {
            bArr2[i6] = (byte) stringBuffer2.charAt(i6);
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        for (int i7 = 0; i7 < length2; i7++) {
            stringBuffer3.append((char) bArr2[i7]);
        }
        return stringBuffer3.toString();
    }
}
