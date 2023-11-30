package com.artifex.source.util.a.util_a.a.b;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class b {

    /* renamed from: a  reason: collision with root package name */
    public static final Map f396a;

    static {
        HashMap hashMap = new HashMap();
        f396a = hashMap;
        Class cls = Boolean.TYPE;
        hashMap.put(Boolean.class, cls);
        hashMap.put(Byte.class, Byte.TYPE);
        hashMap.put(Character.class, Character.TYPE);
        hashMap.put(Short.class, Short.TYPE);
        Class cls2 = Integer.TYPE;
        hashMap.put(Integer.class, cls2);
        Class cls3 = Float.TYPE;
        hashMap.put(Float.class, cls3);
        Class cls4 = Long.TYPE;
        hashMap.put(Long.class, cls4);
        hashMap.put(Double.class, Double.TYPE);
        hashMap.put(cls, cls);
        Class cls5 = Byte.TYPE;
        hashMap.put(cls5, cls5);
        Class cls6 = Character.TYPE;
        hashMap.put(cls6, cls6);
        Class cls7 = Short.TYPE;
        hashMap.put(cls7, cls7);
        hashMap.put(cls2, cls2);
        hashMap.put(cls3, cls3);
        hashMap.put(cls4, cls4);
        Class cls8 = Double.TYPE;
        hashMap.put(cls8, cls8);
    }

    public static void a(Class cls, String str, Object... objArr) {
        Class[] clsArr;
        Object[] objArr2;
        if (objArr == null || objArr.length <= 0) {
            clsArr = null;
        } else {
            clsArr = new Class[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                a aVar = objArr[i];
                if (aVar == null || !(aVar instanceof a)) {
                    clsArr[i] = aVar == null ? null : aVar.getClass();
                } else {
                    a aVar2 = aVar;
                    clsArr[i] = null;
                }
            }
        }
        Method a2 = a(cls, str, clsArr);
        if (objArr == null || objArr.length <= 0) {
            objArr2 = null;
        } else {
            objArr2 = new Object[objArr.length];
            for (int i2 = 0; i2 < objArr.length; i2++) {
                a aVar3 = objArr[i2];
                if (aVar3 == null || !(aVar3 instanceof a)) {
                    objArr2[i2] = aVar3;
                } else {
                    a aVar4 = aVar3;
                    objArr2[i2] = null;
                }
            }
        }
        a2.invoke((Object) null, objArr2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002a, code lost:
        if (r6.length == 0) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0024, code lost:
        if (r13.length != 0) goto L_0x0066;
     */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0073 A[EDGE_INSN: B:40:0x0073->B:30:0x0073 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x006f A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Method a(Class r11, String r12, Class... r13) {
        /*
            java.lang.reflect.Method[] r0 = r11.getDeclaredMethods()
            java.lang.String r1 = "Method name must not be null."
            java.util.Objects.requireNonNull(r12, r1)
            int r1 = r0.length
            r2 = 0
            r3 = 0
        L_0x000c:
            r4 = 1
            if (r3 >= r1) goto L_0x0072
            r5 = r0[r3]
            java.lang.String r6 = r5.getName()
            boolean r6 = r6.equals(r12)
            if (r6 == 0) goto L_0x006f
            java.lang.Class[] r6 = r5.getParameterTypes()
            if (r6 != 0) goto L_0x0027
            if (r13 == 0) goto L_0x006b
            int r6 = r13.length
            if (r6 != 0) goto L_0x0066
            goto L_0x006b
        L_0x0027:
            if (r13 != 0) goto L_0x002d
            int r6 = r6.length
            if (r6 != 0) goto L_0x0066
            goto L_0x006b
        L_0x002d:
            int r7 = r6.length
            int r8 = r13.length
            if (r7 == r8) goto L_0x0032
            goto L_0x0066
        L_0x0032:
            r7 = 0
        L_0x0033:
            int r8 = r6.length
            if (r7 >= r8) goto L_0x006b
            r8 = r6[r7]
            r9 = r13[r7]
            boolean r8 = r8.isAssignableFrom(r9)
            if (r8 != 0) goto L_0x0068
            java.util.Map r8 = f396a
            r9 = r6[r7]
            r10 = r8
            java.util.HashMap r10 = (java.util.HashMap) r10
            boolean r9 = r10.containsKey(r9)
            if (r9 == 0) goto L_0x0066
            r9 = r6[r7]
            r10 = r8
            java.util.HashMap r10 = (java.util.HashMap) r10
            java.lang.Object r9 = r10.get(r9)
            java.lang.Class r9 = (java.lang.Class) r9
            r10 = r13[r7]
            java.util.HashMap r8 = (java.util.HashMap) r8
            java.lang.Object r8 = r8.get(r10)
            boolean r8 = r9.equals(r8)
            if (r8 != 0) goto L_0x0068
        L_0x0066:
            r6 = 0
            goto L_0x006c
        L_0x0068:
            int r7 = r7 + 1
            goto L_0x0033
        L_0x006b:
            r6 = 1
        L_0x006c:
            if (r6 == 0) goto L_0x006f
            goto L_0x0073
        L_0x006f:
            int r3 = r3 + 1
            goto L_0x000c
        L_0x0072:
            r5 = 0
        L_0x0073:
            if (r5 != 0) goto L_0x008a
            java.lang.Class r0 = r11.getSuperclass()
            if (r0 == 0) goto L_0x0084
            java.lang.Class r11 = r11.getSuperclass()
            java.lang.reflect.Method r11 = a((java.lang.Class) r11, (java.lang.String) r12, (java.lang.Class[]) r13)
            return r11
        L_0x0084:
            java.lang.NoSuchMethodException r11 = new java.lang.NoSuchMethodException
            r11.<init>()
            throw r11
        L_0x008a:
            r5.setAccessible(r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.source.util.a.util_a.a.b.b.a(java.lang.Class, java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }
}
