package com.artifex.source.util.a.util_a.a.b.f;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import java.lang.ref.WeakReference;

public class d extends Handler implements e {

    /* renamed from: a  reason: collision with root package name */
    public WeakReference f400a;
    public final HandlerThread b;

    public d(HandlerThread handlerThread, com.artifex.source.util.a.util_a.a.b.d dVar) {
        super(handlerThread.getLooper());
        this.b = handlerThread;
    }

    public void handleMessage(Message message) {
        com.artifex.source.util.a.util_a.a.b.d dVar;
        WeakReference weakReference = this.f400a;
        if (weakReference != null && (dVar = (com.artifex.source.util.a.util_a.a.b.d) weakReference.get()) != null && message != null) {
            dVar.a(message);
        }
    }
}
