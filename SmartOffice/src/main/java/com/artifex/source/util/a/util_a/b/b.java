package com.artifex.source.util.a.util_a.b;

import com.artifex.source.util.a.util_a.a.b.f.d;
import com.artifex.source.util.a.util_a.b.g.a;
import android.os.Handler;
import android.os.Message;
import com.google.logging.type.LogSeverity;

public class b implements Handler.Callback {

    /* renamed from: a  reason: collision with root package name */
    public final /* synthetic */ c f403a;

    public b(c cVar) {
        this.f403a = cVar;
    }

    public boolean handleMessage(Message message) {
        c cVar = this.f403a;
        if (cVar.b >= cVar.f404a) {
            cVar.b = 0;
            cVar.quit();
            a.f402a = LogSeverity.ERROR_VALUE;
            return false;
        }
        byte[] bArr = null;
        try {
            bArr = a.a(cVar.e, cVar.f);
        } catch (Throwable unused) {
        }
        if (bArr != null) {
            d b = com.artifex.source.util.a.util_a.a.b.f.b.f398a.b();
            c cVar2 = this.f403a;
            b.post(new d(bArr, cVar2.d, cVar2));
            return true;
        }
        c cVar3 = this.f403a;
        cVar3.g.sendEmptyMessageDelayed(1, (long) (cVar3.c * 1));
        this.f403a.b++;
        return false;
    }
}
