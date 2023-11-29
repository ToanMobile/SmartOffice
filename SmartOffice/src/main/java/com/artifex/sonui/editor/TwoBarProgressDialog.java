package com.artifex.sonui.editor;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TwoBarProgressDialog {
    public ProgressBar mBar1;
    public TextView mBar1Label;
    public ProgressBar mBar2;
    public TextView mBar2Label;
    public AlertDialog mDialog;
    public Runnable mOnCancelRunnable;

    public TwoBarProgressDialog(Activity activity, String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.Theme_AppCompat_DayNight_Dialog_Alert));
        View inflate = LayoutInflater.from(activity).inflate(R.layout.sodk_editor_twobar_progress, (ViewGroup) null);
        builder.setView(inflate);
        builder.setTitle(str);
        ((Button) inflate.findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TwoBarProgressDialog.this.mDialog.dismiss();
                Runnable runnable = TwoBarProgressDialog.this.mOnCancelRunnable;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        AlertDialog create = builder.create();
        this.mDialog = create;
        create.setCancelable(false);
        this.mBar1Label = (TextView) inflate.findViewById(R.id.bar1_label);
        this.mBar2Label = (TextView) inflate.findViewById(R.id.bar2_label);
        this.mBar1 = (ProgressBar) inflate.findViewById(R.id.bar1);
        this.mBar2 = (ProgressBar) inflate.findViewById(R.id.bar2);
    }

    public void dismiss() {
        this.mDialog.dismiss();
    }

    public void setBar1Label(String str) {
        this.mBar1Label.setText(str);
    }

    public void setBar1Max(int i) {
        this.mBar1.setMax(i);
    }

    public void setBar1Value(int i) {
        this.mBar1.setProgress(i);
    }

    public void setBar2Label(String str) {
        this.mBar2Label.setText(str);
    }

    public void setBar2Max(int i) {
        this.mBar2.setMax(i);
    }

    public void setBar2Value(int i) {
        this.mBar2.setProgress(i);
    }

    public void setCancelRunnable(Runnable runnable) {
        this.mOnCancelRunnable = runnable;
    }

    public void show() {
        this.mDialog.show();
    }
}
