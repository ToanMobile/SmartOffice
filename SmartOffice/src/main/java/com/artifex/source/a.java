package com.artifex.source;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public /* synthetic */ class a {
    public static void open(Context context, String str) {
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = a$$ExternalSyntheticOutline0.m("http://", str);
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(268435456);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
        }
    }
}
