package com.artifex.source.util.a.util_a.a.a;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import com.yandex.metrica.YandexMetricaDefaultValues;
import java.net.HttpURLConnection;
import java.util.Locale;

public abstract class c {

    /* renamed from: a  reason: collision with root package name */
    private String f395a;
    public String b = "";
    private int c;
    private int d;
    private byte[] e;
    private int f = -1;
    private byte[] g = null;
    /* access modifiers changed from: private */
    public int h = YandexMetricaDefaultValues.DEFAULT_MAX_REPORTS_COUNT_UPPER_BOUND;
    /* access modifiers changed from: private */
    public int i = 2;
    /* access modifiers changed from: private */
    public int j = 0;
    /* access modifiers changed from: private */
    public boolean k = false;
    /* access modifiers changed from: private */
    public HandlerThread l;
    /* access modifiers changed from: private */
    public Handler m;
    private boolean n = false;
    private HttpURLConnection o = null;

    public c(Context context, String str) {
        this.f395a = (str == null || str.length() <= 0) ? "" : str;
    }

    private void a(int i2) {
        String str;
        StringBuilder sb;
        HttpURLConnection httpURLConnection;
        String str2 = i2 != 1 ? i2 != 2 ? "" : "application/octet-stream" : "application/json; charset=utf-8";
        if (str2.length() > 0) {
            this.o.addRequestProperty("Content-Type", str2);
        }
        HttpURLConnection httpURLConnection2 = this.o;
        StringBuilder m2 = c$$ExternalSyntheticOutline0.m("sessionid=");
        m2.append(this.f395a);
        httpURLConnection2.addRequestProperty("Cookie", m2.toString());
        try {
            String language = Locale.getDefault().getLanguage();
            if (language.equalsIgnoreCase("zh")) {
                httpURLConnection = this.o;
                sb = new StringBuilder();
                sb.append(Locale.getDefault().toString());
                sb.append(",");
                sb.append(language);
                str = ";q=0.9";
            } else {
                httpURLConnection = this.o;
                sb = new StringBuilder();
                sb.append(Locale.getDefault().toString());
                sb.append(",");
                sb.append(language);
                str = ";q=0.9,en-US;q=0.6,en;q=0.4";
            }
            sb.append(str);
            httpURLConnection.addRequestProperty("Accept-Language", sb.toString());
        } catch (Throwable unused) {
        }
    }

    public static /* synthetic */ int b(c cVar) {
        int i2 = cVar.j;
        cVar.j = i2 + 1;
        return i2;
    }

    public void a(int i2, int i3, byte[] bArr) {
        if (!this.k) {
            this.c = i2;
            this.d = i3;
            this.e = bArr;
            this.k = true;
            this.j = 0;
            HandlerThread handlerThread = new HandlerThread("request");
            this.l = handlerThread;
            handlerThread.start();
            Handler handler = new Handler(this.l.getLooper(), new b(this));
            this.m = handler;
            handler.sendEmptyMessage(1);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b0, code lost:
        if (r3 != null) goto L_0x00ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00b6, code lost:
        if (r3 != null) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00c0, code lost:
        if (r3 == null) goto L_0x00c7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00c2, code lost:
        r3.disconnect();
        r8.o = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00c7, code lost:
        if (r4 == false) goto L_0x00ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00c9, code lost:
        r3 = r8.f;
        r5 = r8.g;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00cf, code lost:
        if (r3 != 200) goto L_0x00ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00d1, code lost:
        if (r5 == null) goto L_0x00ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00d4, code lost:
        if (r5.length <= 0) goto L_0x00ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00e2, code lost:
        if (((java.lang.Integer) com.pgl.ssdk.ces.a.meta(223, (android.content.Context) null, r5)).intValue() != 0) goto L_0x00e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00e4, code lost:
        com.pgl.ssdk.ces.d.h = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00e7, code lost:
        com.pgl.ssdk.ces.d.h = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00ea, code lost:
        com.pgl.ssdk.ces.d.h = false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a() {
        /*
            r8 = this;
            r0 = 1
            r1 = 0
            r2 = 0
            java.net.URL r3 = new java.net.URL     // Catch:{ all -> 0x00b9 }
            java.lang.String r4 = r8.b     // Catch:{ all -> 0x00b9 }
            r3.<init>(r4)     // Catch:{ all -> 0x00b9 }
            boolean r4 = r8.n     // Catch:{ all -> 0x00b9 }
            if (r4 == 0) goto L_0x0024
            java.lang.String r4 = "TLS"
            javax.net.ssl.SSLContext r4 = javax.net.ssl.SSLContext.getInstance(r4)     // Catch:{ all -> 0x00b9 }
            javax.net.ssl.SSLSocketFactory r4 = r4.getSocketFactory()     // Catch:{ all -> 0x00b9 }
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(r4)     // Catch:{ all -> 0x00b9 }
            java.net.Proxy r4 = java.net.Proxy.NO_PROXY     // Catch:{ all -> 0x00b9 }
            java.net.URLConnection r3 = r3.openConnection(r4)     // Catch:{ all -> 0x00b9 }
        L_0x0021:
            java.net.HttpURLConnection r3 = (java.net.HttpURLConnection) r3     // Catch:{ all -> 0x00b9 }
            goto L_0x0029
        L_0x0024:
            java.net.URLConnection r3 = r3.openConnection()     // Catch:{ all -> 0x00b9 }
            goto L_0x0021
        L_0x0029:
            r8.o = r3     // Catch:{ all -> 0x00b9 }
            int r4 = r8.h     // Catch:{ all -> 0x00b9 }
            r3.setConnectTimeout(r4)     // Catch:{ all -> 0x00b9 }
            java.net.HttpURLConnection r3 = r8.o     // Catch:{ all -> 0x00b9 }
            int r4 = r8.h     // Catch:{ all -> 0x00b9 }
            r3.setReadTimeout(r4)     // Catch:{ all -> 0x00b9 }
            int r3 = r8.c     // Catch:{ all -> 0x00b9 }
            if (r3 == r0) goto L_0x0056
            r4 = 3
            if (r3 == r4) goto L_0x0053
            r4 = 4
            if (r3 == r4) goto L_0x0050
            r4 = 5
            if (r3 == r4) goto L_0x004d
            r4 = 6
            if (r3 == r4) goto L_0x004a
            java.lang.String r3 = "GET"
            goto L_0x0058
        L_0x004a:
            java.lang.String r3 = "TRACE"
            goto L_0x0058
        L_0x004d:
            java.lang.String r3 = "HEAD"
            goto L_0x0058
        L_0x0050:
            java.lang.String r3 = "DELETE"
            goto L_0x0058
        L_0x0053:
            java.lang.String r3 = "PUT"
            goto L_0x0058
        L_0x0056:
            java.lang.String r3 = "POST"
        L_0x0058:
            java.net.HttpURLConnection r4 = r8.o     // Catch:{ all -> 0x00b9 }
            r4.setRequestMethod(r3)     // Catch:{ all -> 0x00b9 }
            int r3 = r8.d     // Catch:{ all -> 0x00b9 }
            r8.a((int) r3)     // Catch:{ all -> 0x00b9 }
            byte[] r3 = r8.e     // Catch:{ all -> 0x00b9 }
            if (r3 == 0) goto L_0x007f
            int r3 = r3.length     // Catch:{ all -> 0x00b9 }
            if (r3 <= 0) goto L_0x007f
            java.net.HttpURLConnection r3 = r8.o     // Catch:{ all -> 0x00b9 }
            r3.setDoOutput(r0)     // Catch:{ all -> 0x00b9 }
            java.net.HttpURLConnection r3 = r8.o     // Catch:{ all -> 0x00b9 }
            java.io.OutputStream r3 = r3.getOutputStream()     // Catch:{ all -> 0x00b9 }
            byte[] r4 = r8.e     // Catch:{ all -> 0x00b9 }
            r3.write(r4)     // Catch:{ all -> 0x00b9 }
            r3.flush()     // Catch:{ all -> 0x00b9 }
            r3.close()     // Catch:{ all -> 0x00b9 }
        L_0x007f:
            java.net.HttpURLConnection r3 = r8.o     // Catch:{ all -> 0x00b9 }
            r3.connect()     // Catch:{ all -> 0x00b9 }
            java.net.HttpURLConnection r3 = r8.o     // Catch:{ all -> 0x00af }
            int r3 = r3.getResponseCode()     // Catch:{ all -> 0x00af }
            r8.f = r3     // Catch:{ all -> 0x00af }
            java.net.HttpURLConnection r3 = r8.o     // Catch:{ all -> 0x00af }
            java.io.InputStream r3 = r3.getInputStream()     // Catch:{ all -> 0x00af }
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x00b0 }
            r4.<init>()     // Catch:{ all -> 0x00b0 }
            r5 = 1024(0x400, float:1.435E-42)
            byte[] r6 = new byte[r5]     // Catch:{ all -> 0x00b0 }
        L_0x009b:
            int r7 = r3.read(r6, r2, r5)     // Catch:{ all -> 0x00b0 }
            if (r7 <= 0) goto L_0x00a5
            r4.write(r6, r2, r7)     // Catch:{ all -> 0x00b0 }
            goto L_0x009b
        L_0x00a5:
            byte[] r4 = r4.toByteArray()     // Catch:{ all -> 0x00b0 }
            r8.g = r4     // Catch:{ all -> 0x00b0 }
        L_0x00ab:
            r3.close()     // Catch:{ all -> 0x00b9 }
            goto L_0x00b3
        L_0x00af:
            r3 = r1
        L_0x00b0:
            if (r3 == 0) goto L_0x00b3
            goto L_0x00ab
        L_0x00b3:
            java.net.HttpURLConnection r3 = r8.o
            r4 = 1
            if (r3 == 0) goto L_0x00c7
            goto L_0x00c2
        L_0x00b9:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x00ed }
            java.net.HttpURLConnection r3 = r8.o
            r4 = 0
            if (r3 == 0) goto L_0x00c7
        L_0x00c2:
            r3.disconnect()
            r8.o = r1
        L_0x00c7:
            if (r4 == 0) goto L_0x00ec
            int r3 = r8.f
            byte[] r5 = r8.g
            r6 = 200(0xc8, float:2.8E-43)
            if (r3 != r6) goto L_0x00ec
            if (r5 == 0) goto L_0x00ec
            int r3 = r5.length     // Catch:{ all -> 0x00ea }
            if (r3 <= 0) goto L_0x00ec
            r3 = 223(0xdf, float:3.12E-43)
            java.lang.Object r1 = com.pgl.ssdk.ces.a.meta(r3, r1, r5)     // Catch:{ all -> 0x00ea }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ all -> 0x00ea }
            int r1 = r1.intValue()     // Catch:{ all -> 0x00ea }
            if (r1 != 0) goto L_0x00e7
            com.pgl.ssdk.ces.d.h = r0     // Catch:{ all -> 0x00ea }
            goto L_0x00ec
        L_0x00e7:
            com.pgl.ssdk.ces.d.h = r2     // Catch:{ all -> 0x00ea }
            goto L_0x00ec
        L_0x00ea:
            com.pgl.ssdk.ces.d.h = r2
        L_0x00ec:
            return r4
        L_0x00ed:
            r0 = move-exception
            java.net.HttpURLConnection r2 = r8.o
            if (r2 == 0) goto L_0x00f7
            r2.disconnect()
            r8.o = r1
        L_0x00f7:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.source.util.a.util_a.a.a.c.a():boolean");
    }
}
