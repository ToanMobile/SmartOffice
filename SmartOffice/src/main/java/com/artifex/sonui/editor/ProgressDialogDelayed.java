package com.artifex.sonui.editor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class ProgressDialogDelayed extends ProgressDialog {
    public int delay = 0;
    public boolean dismissed = false;

    public ProgressDialogDelayed(Context context, int i) {
        super(context, R.style.sodk_editor_alert_dialog_style);
        this.delay = i;
    }

    public void dismiss() {
        if (super.isShowing()) {
            super.dismiss();
        }
        this.dismissed = true;
    }

    public void show() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ProgressDialogDelayed progressDialogDelayed = ProgressDialogDelayed.this;
                if (!progressDialogDelayed.dismissed) {
                    ProgressDialogDelayed.super.show();
                }
            }
        }, (long) this.delay);
    }
}
