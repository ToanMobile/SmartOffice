package com.artifex.solib.animation;

public class SOAnimationEasings {
    public static float bounceEaseOut(float f) {
        double d = (double) f;
        return d < 0.36363636363636365d ? ((121.0f * f) * f) / 16.0f : d < 0.7272727272727273d ? (((9.075f * f) * f) - (f * 9.9f)) + 3.4f : d < 0.9d ? (((12.066482f * f) * f) - (f * 19.635458f)) + 8.898061f : (((10.8f * f) * f) - (f * 20.52f)) + 10.72f;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x010f, code lost:
        r0 = (float) r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0173, code lost:
        r20 = (1.0f - r0) * 0.5f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0177, code lost:
        r0 = r20;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x01a3, code lost:
        r0 = 1.0f - r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x01b0, code lost:
        r0 = r0 * 0.5f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x01bc, code lost:
        r0 = (float) r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0219, code lost:
        r0 = r0 + 1.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x021e, code lost:
        r1 = r1 * r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0228, code lost:
        r16 = (r1 * r0) * r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0235, code lost:
        r16 = ((r16 * r0) * r0) + 1.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x023b, code lost:
        r0 = r16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0242, code lost:
        r0 = (r1 * r0) + 1.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0249, code lost:
        r0 = r0 * r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0253, code lost:
        r1 = r1 * r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0255, code lost:
        r0 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x026f, code lost:
        return (r0 * r23) + r22;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static float ease(int r21, float r22, float r23, float r24) {
        /*
            r0 = r24
            r1 = -4596931000999123804(0xc0346b9c347764a4, double:-20.420352248333657)
            r3 = 4626441035855652004(0x40346b9c347764a4, double:20.420352248333657)
            r5 = 0
            r7 = -1054867456(0xffffffffc1200000, float:-10.0)
            r8 = 1082130432(0x40800000, float:4.0)
            r9 = 1092616192(0x41200000, float:10.0)
            r10 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r12 = 4614256656552045848(0x400921fb54442d18, double:3.141592653589793)
            r14 = 4611686018427387904(0x4000000000000000, double:2.0)
            r16 = 1056964608(0x3f000000, float:0.5)
            r17 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            r19 = 1073741824(0x40000000, float:2.0)
            r20 = 1065353216(0x3f800000, float:1.0)
            switch(r21) {
                case 1: goto L_0x0269;
                case 2: goto L_0x0263;
                case 3: goto L_0x024c;
                case 4: goto L_0x0247;
                case 5: goto L_0x023e;
                case 6: goto L_0x0221;
                case 7: goto L_0x021c;
                case 8: goto L_0x020f;
                case 9: goto L_0x01f2;
                case 10: goto L_0x01ed;
                case 11: goto L_0x01e4;
                case 12: goto L_0x01cb;
                case 13: goto L_0x01bf;
                case 14: goto L_0x01b4;
                case 15: goto L_0x01a7;
                case 16: goto L_0x0199;
                case 17: goto L_0x018f;
                case 18: goto L_0x0162;
                case 19: goto L_0x0151;
                case 20: goto L_0x013f;
                case 21: goto L_0x0112;
                case 22: goto L_0x00fd;
                case 23: goto L_0x00e9;
                case 24: goto L_0x00b5;
                case 25: goto L_0x00ad;
                case 26: goto L_0x00a7;
                case 27: goto L_0x008a;
                case 28: goto L_0x0078;
                case 29: goto L_0x0063;
                case 30: goto L_0x002a;
                default: goto L_0x0028;
            }
        L_0x0028:
            goto L_0x026b
        L_0x002a:
            double r1 = (double) r0
            int r3 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x0044
            float r0 = r0 * r19
            float r1 = r0 * r0
            float r1 = r1 * r0
            double r2 = (double) r0
            double r2 = r2 * r12
            double r2 = java.lang.Math.sin(r2)
            float r2 = (float) r2
            float r0 = r0 * r2
            float r1 = r1 - r0
            float r1 = r1 * r16
            goto L_0x0255
        L_0x0044:
            float r0 = r0 * r19
            float r0 = r0 - r20
            float r0 = r20 - r0
            float r1 = r0 * r0
            float r1 = r1 * r0
            double r2 = (double) r0
            double r2 = r2 * r12
            double r2 = java.lang.Math.sin(r2)
            float r2 = (float) r2
            float r0 = r0 * r2
            float r1 = r1 - r0
            float r20 = r20 - r1
            float r20 = r20 * r16
            float r20 = r20 + r16
            r1 = r20
            goto L_0x0255
        L_0x0063:
            float r0 = r20 - r0
            float r1 = r0 * r0
            float r1 = r1 * r0
            double r2 = (double) r0
            double r2 = r2 * r12
            double r2 = java.lang.Math.sin(r2)
            float r2 = (float) r2
            float r0 = r0 * r2
            float r1 = r1 - r0
            float r0 = r20 - r1
            goto L_0x026b
        L_0x0078:
            float r1 = r0 * r0
            float r1 = r1 * r0
            double r2 = (double) r0
            double r2 = r2 * r12
            double r2 = java.lang.Math.sin(r2)
            float r2 = (float) r2
            float r0 = r0 * r2
            float r0 = r1 - r0
            goto L_0x026b
        L_0x008a:
            double r1 = (double) r0
            int r3 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x0099
            float r0 = r0 * r19
            float r0 = r20 - r0
            float r0 = bounceEaseOut(r0)
            goto L_0x0173
        L_0x0099:
            float r0 = r0 * r19
            float r0 = r0 - r20
            float r0 = bounceEaseOut(r0)
            float r0 = r0 * r16
            float r0 = r0 + r16
            goto L_0x026b
        L_0x00a7:
            float r0 = bounceEaseOut(r24)
            goto L_0x026b
        L_0x00ad:
            float r0 = r20 - r0
            float r0 = bounceEaseOut(r0)
            goto L_0x01a3
        L_0x00b5:
            double r5 = (double) r0
            int r8 = (r5 > r17 ? 1 : (r5 == r17 ? 0 : -1))
            if (r8 >= 0) goto L_0x00cf
            float r0 = r0 * r19
            double r1 = (double) r0
            double r1 = r1 * r3
            double r1 = java.lang.Math.sin(r1)
            float r0 = r0 - r20
            float r0 = r0 * r9
            double r3 = (double) r0
            double r3 = java.lang.Math.pow(r14, r3)
            double r3 = r3 * r1
            goto L_0x00e6
        L_0x00cf:
            float r0 = r0 * r19
            float r0 = r0 - r20
            float r3 = r0 + r20
            double r3 = (double) r3
            double r3 = r3 * r1
            double r1 = java.lang.Math.sin(r3)
            float r0 = r0 * r7
            double r3 = (double) r0
            double r3 = java.lang.Math.pow(r14, r3)
            double r3 = r3 * r1
            double r3 = r3 + r14
        L_0x00e6:
            float r0 = (float) r3
            goto L_0x01b0
        L_0x00e9:
            float r3 = r0 + r20
            double r3 = (double) r3
            double r3 = r3 * r1
            double r1 = java.lang.Math.sin(r3)
            float r0 = r0 * r7
            double r3 = (double) r0
            double r3 = java.lang.Math.pow(r14, r3)
            double r3 = r3 * r1
            double r3 = r3 + r10
            goto L_0x010f
        L_0x00fd:
            double r1 = (double) r0
            double r1 = r1 * r3
            double r1 = java.lang.Math.sin(r1)
            float r0 = r0 - r20
            float r0 = r0 * r9
            double r3 = (double) r0
            double r3 = java.lang.Math.pow(r14, r3)
            double r3 = r3 * r1
        L_0x010f:
            float r0 = (float) r3
            goto L_0x026b
        L_0x0112:
            double r1 = (double) r0
            int r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x026b
            int r3 = (r1 > r10 ? 1 : (r1 == r10 ? 0 : -1))
            if (r3 != 0) goto L_0x011d
            goto L_0x026b
        L_0x011d:
            int r3 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x012e
            r1 = 1101004800(0x41a00000, float:20.0)
            float r0 = r0 * r1
            float r0 = r0 - r9
            double r0 = (double) r0
            double r0 = java.lang.Math.pow(r14, r0)
            float r0 = (float) r0
            goto L_0x01b0
        L_0x012e:
            r1 = -1090519040(0xffffffffbf000000, float:-0.5)
            r2 = -1046478848(0xffffffffc1a00000, float:-20.0)
            float r0 = r0 * r2
            float r0 = r0 + r9
            double r2 = (double) r0
            double r2 = java.lang.Math.pow(r14, r2)
            float r0 = (float) r2
            float r0 = r0 * r1
            goto L_0x0219
        L_0x013f:
            double r1 = (double) r0
            int r3 = (r1 > r10 ? 1 : (r1 == r10 ? 0 : -1))
            if (r3 != 0) goto L_0x0146
            goto L_0x026b
        L_0x0146:
            float r0 = r0 * r7
            double r0 = (double) r0
            double r0 = java.lang.Math.pow(r14, r0)
            float r0 = (float) r0
            float r20 = r20 - r0
            goto L_0x0177
        L_0x0151:
            double r1 = (double) r0
            int r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r3 != 0) goto L_0x0158
            goto L_0x026b
        L_0x0158:
            float r0 = r0 - r20
            float r0 = r0 * r9
            double r0 = (double) r0
            double r0 = java.lang.Math.pow(r14, r0)
            goto L_0x01bc
        L_0x0162:
            double r1 = (double) r0
            int r3 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x017b
            float r0 = r0 * r0
            float r0 = r0 * r8
            float r0 = r20 - r0
            double r0 = (double) r0
            double r0 = java.lang.Math.sqrt(r0)
            float r0 = (float) r0
        L_0x0173:
            float r20 = r20 - r0
            float r20 = r20 * r16
        L_0x0177:
            r0 = r20
            goto L_0x026b
        L_0x017b:
            float r0 = r0 * r19
            r1 = 1077936128(0x40400000, float:3.0)
            float r1 = r0 - r1
            float r1 = -r1
            float r0 = r0 - r20
            float r0 = r0 * r1
            double r0 = (double) r0
            double r0 = java.lang.Math.sqrt(r0)
            float r0 = (float) r0
            float r0 = r0 + r20
            goto L_0x01b0
        L_0x018f:
            float r19 = r19 - r0
            float r0 = r0 * r19
            double r0 = (double) r0
            double r0 = java.lang.Math.sqrt(r0)
            goto L_0x01bc
        L_0x0199:
            float r0 = r0 * r0
            float r0 = r20 - r0
            double r0 = (double) r0
            double r0 = java.lang.Math.sqrt(r0)
            float r0 = (float) r0
        L_0x01a3:
            float r0 = r20 - r0
            goto L_0x026b
        L_0x01a7:
            double r0 = (double) r0
            double r0 = r0 * r12
            double r0 = java.lang.Math.cos(r0)
            double r10 = r10 - r0
            float r0 = (float) r10
        L_0x01b0:
            float r0 = r0 * r16
            goto L_0x026b
        L_0x01b4:
            double r0 = (double) r0
            double r0 = r0 * r12
            double r0 = r0 / r14
            double r0 = java.lang.Math.sin(r0)
        L_0x01bc:
            float r0 = (float) r0
            goto L_0x026b
        L_0x01bf:
            float r0 = r0 - r20
            double r0 = (double) r0
            double r0 = r0 * r12
            double r0 = r0 / r14
            double r0 = java.lang.Math.sin(r0)
            float r0 = (float) r0
            goto L_0x0219
        L_0x01cb:
            double r1 = (double) r0
            int r3 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x01d9
            r1 = 1098907648(0x41800000, float:16.0)
            float r1 = r1 * r0
            float r1 = r1 * r0
            float r1 = r1 * r0
            goto L_0x0228
        L_0x01d9:
            float r0 = r0 * r19
            float r0 = r0 - r19
            float r16 = r16 * r0
            float r16 = r16 * r0
            float r16 = r16 * r0
            goto L_0x0235
        L_0x01e4:
            float r0 = r0 - r20
            float r1 = r0 * r0
            float r1 = r1 * r0
            float r1 = r1 * r0
            goto L_0x0242
        L_0x01ed:
            float r1 = r0 * r0
            float r1 = r1 * r0
            goto L_0x021e
        L_0x01f2:
            double r1 = (double) r0
            int r3 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x0200
            r1 = 1090519040(0x41000000, float:8.0)
            float r1 = r1 * r0
            float r1 = r1 * r0
            float r1 = r1 * r0
            goto L_0x0253
        L_0x0200:
            float r0 = r0 - r20
            r1 = -1056964608(0xffffffffc1000000, float:-8.0)
            float r1 = r1 * r0
            float r1 = r1 * r0
            float r1 = r1 * r0
            float r1 = r1 * r0
            float r1 = r1 + r20
            goto L_0x0255
        L_0x020f:
            float r1 = r0 - r20
            float r2 = r1 * r1
            float r2 = r2 * r1
            float r0 = r20 - r0
            float r0 = r0 * r2
        L_0x0219:
            float r0 = r0 + r20
            goto L_0x026b
        L_0x021c:
            float r1 = r0 * r0
        L_0x021e:
            float r1 = r1 * r0
            goto L_0x0249
        L_0x0221:
            double r1 = (double) r0
            int r3 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x022f
            float r1 = r0 * r8
        L_0x0228:
            float r1 = r1 * r0
            float r1 = r1 * r0
            r16 = r1
            goto L_0x023b
        L_0x022f:
            float r0 = r0 * r19
            float r0 = r0 - r19
            float r16 = r16 * r0
        L_0x0235:
            float r16 = r16 * r0
            float r16 = r16 * r0
            float r16 = r16 + r20
        L_0x023b:
            r0 = r16
            goto L_0x026b
        L_0x023e:
            float r0 = r0 - r20
            float r1 = r0 * r0
        L_0x0242:
            float r1 = r1 * r0
            float r0 = r1 + r20
            goto L_0x026b
        L_0x0247:
            float r1 = r0 * r0
        L_0x0249:
            float r0 = r0 * r1
            goto L_0x026b
        L_0x024c:
            double r1 = (double) r0
            int r3 = (r1 > r17 ? 1 : (r1 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x0257
            float r1 = r0 * r19
        L_0x0253:
            float r1 = r1 * r0
        L_0x0255:
            r0 = r1
            goto L_0x026b
        L_0x0257:
            r1 = -1073741824(0xffffffffc0000000, float:-2.0)
            float r1 = r1 * r0
            float r1 = r1 * r0
            float r0 = r0 * r8
            float r0 = r0 + r1
            float r0 = r0 - r20
            goto L_0x026b
        L_0x0263:
            float r1 = r0 - r19
            float r1 = r1 * r0
            float r0 = -r1
            goto L_0x026b
        L_0x0269:
            float r0 = r0 * r0
        L_0x026b:
            float r0 = r0 * r23
            float r0 = r0 + r22
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.animation.SOAnimationEasings.ease(int, float, float, float):float");
    }
}
