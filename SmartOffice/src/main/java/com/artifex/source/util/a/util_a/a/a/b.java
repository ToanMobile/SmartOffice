package com.artifex.source.util.a.util_a.a.a;

import android.os.Handler;
import android.os.Message;

public class b implements Handler.Callback {

    /* renamed from: a  reason: collision with root package name */
    public final /* synthetic */ c f394a;

    public b(c cVar) {
        this.f394a = cVar;
    }

    public boolean handleMessage(Message message) {
        if (this.f394a.j >= this.f394a.i) {
            boolean unused = this.f394a.k = false;
            int unused2 = this.f394a.j = 0;
            this.f394a.l.quit();
            return false;
        } else if (this.f394a.a()) {
            boolean unused3 = this.f394a.k = false;
            int unused4 = this.f394a.j = 0;
            this.f394a.l.quit();
            return true;
        } else {
            this.f394a.m.sendEmptyMessageDelayed(1, (long) (this.f394a.h * 1));
            c.b(this.f394a);
            return false;
        }
    }
}
