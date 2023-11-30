package com.artifex.source.util.a.util_a.b;

import android.content.Context;
import com.google.gson.Gson;
import com.zoho.desk.conversation.pojo.ZDLayoutDetail;
import com.zoho.desk.conversation.pojo.ZDMessage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import kotlin.jvm.functions.Function1;

public class a {

    /* renamed from: a  reason: collision with root package name */
    public static int f402a = 504;
    public static String b = null;
    public static String c = "";
    public static Context d;

    public static long a(ZDMessage zDMessage) {
        for (ZDLayoutDetail next : zDMessage.getLayouts()) {
            if (next.getType().equals("INPUT")) {
                return Long.parseLong((String) ((Hashtable) new Gson().fromJson(next.getContent(), Hashtable.class)).get("size"));
            }
        }
        return 0;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|(3:5|6|(5:8|14|15|16|18))|9|10|11|12|13|14|15|16|18) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0034 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String a() {
        /*
            java.lang.String r0 = ""
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ all -> 0x004b }
            r1.<init>()     // Catch:{ all -> 0x004b }
            java.lang.String r2 = "os"
            java.lang.String r3 = "Android"
            r1.put(r2, r3)     // Catch:{ all -> 0x004b }
            java.lang.String r2 = "version"
            java.lang.String r3 = "1.0.0"
            r1.put(r2, r3)     // Catch:{ all -> 0x004b }
            java.lang.String r2 = c     // Catch:{ all -> 0x004b }
            java.lang.String r3 = "token_id"
            if (r2 == 0) goto L_0x0027
            int r2 = r2.length()     // Catch:{ all -> 0x004b }
            if (r2 <= 0) goto L_0x0027
            java.lang.String r2 = c     // Catch:{ all -> 0x004b }
            r1.put(r3, r2)     // Catch:{ all -> 0x004b }
            goto L_0x0037
        L_0x0027:
            r2 = 303(0x12f, float:4.25E-43)
            android.content.Context r4 = d     // Catch:{ all -> 0x0034 }
            r5 = 0
            java.lang.Object r2 = com.pgl.ssdk.ces.a.meta(r2, r4, r5)     // Catch:{ all -> 0x0034 }
            r1.put(r3, r2)     // Catch:{ all -> 0x0034 }
            goto L_0x0037
        L_0x0034:
            r1.put(r3, r0)     // Catch:{ all -> 0x004b }
        L_0x0037:
            java.lang.String r2 = "code"
            int r3 = f402a     // Catch:{ all -> 0x004b }
            r1.put(r2, r3)     // Catch:{ all -> 0x004b }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x004b }
            byte[] r1 = r1.getBytes()     // Catch:{ all -> 0x004b }
            r2 = 2
            java.lang.String r0 = android.util.Base64.encodeToString(r1, r2)     // Catch:{ all -> 0x004b }
        L_0x004b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.source.util.a.util_a.b.a.a():java.lang.String");
    }

    public static String a(long j) {
        return (((double) (j / 1000)) / 1000.0d) + "MB";
    }

    public static final <T> void appendElement(Appendable appendable, T t, Function1<? super T, ? extends CharSequence> function1) {
        boolean z;
        if (function1 != null) {
            appendable.append((CharSequence) function1.invoke(t));
            return;
        }
        if (t == null) {
            z = true;
        } else {
            z = t instanceof CharSequence;
        }
        if (z) {
            appendable.append((CharSequence) t);
        } else if (t instanceof Character) {
            appendable.append(((Character) t).charValue());
        } else {
            appendable.append(String.valueOf(t));
        }
    }

    public static Object invoke(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e2) {
            throw new RuntimeException(e2);
        }
    }
}
