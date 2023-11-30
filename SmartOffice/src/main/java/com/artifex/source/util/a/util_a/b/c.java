package com.artifex.source.util.a.util_a.b;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import com.yandex.metrica.YandexMetricaDefaultValues;

public class c extends HandlerThread {

    /* renamed from: a  reason: collision with root package name */
    public int f404a = 2;
    public int b = 0;
    public int c = YandexMetricaDefaultValues.DEFAULT_MAX_REPORTS_COUNT_UPPER_BOUND;
    public Context d;
    public String e;
    public byte[] f;
    public Handler g;
    public Handler.Callback h = new b(this);

    public c(String str, Context context, String str2, byte[] bArr) {
        super(str);
        this.d = context;
        this.e = str2;
        this.f = bArr;
    }
}
