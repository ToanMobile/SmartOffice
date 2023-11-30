package com.artifex.source;

import java.lang.reflect.Field;
import java.util.concurrent.LinkedBlockingQueue;

public class f {
    public final /* synthetic */ int $r8$classId;

    /* renamed from: a  reason: collision with root package name */
    public Object f401a;

    public f() {
        this.$r8$classId = 0;
        this.f401a = new LinkedBlockingQueue();
    }

    public String toString() {
        switch (this.$r8$classId) {
            case 1:
                return ((Field) this.f401a).toString();
            default:
                return super.toString();
        }
    }

    public f(Field field) {
        this.$r8$classId = 1;
        this.f401a = field;
    }
}
