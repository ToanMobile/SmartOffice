package com.artifex.sonui.editor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AuthorDialog {

    public interface AuthorDialogListener {
        void onCancel();

        void onOK(String str);
    }

    public static void show(final Activity activity, final AuthorDialogListener authorDialogListener, String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.sodk_editor_alert_dialog_style));
        View inflate = LayoutInflater.from(activity).inflate(R.layout.sodk_editor_author_dialog, (ViewGroup) null);
        builder.setView(inflate);
        builder.setTitle(activity.getResources().getString(R.string.sodk_editor_author_dialog_title));
        final SOEditText sOEditText = (SOEditText) inflate.findViewById(R.id.editTextDialogUserInput);
        sOEditText.setText(str);
        sOEditText.selectAll();
        builder.setPositiveButton(activity.getResources().getString(R.string.sodk_editor_update), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Utilities.hideKeyboard(activity);
                dialogInterface.dismiss();
                if (authorDialogListener != null) {
                    authorDialogListener.onOK(sOEditText.getText().toString());
                }
            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.sodk_editor_retain), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Utilities.hideKeyboard(activity);
                dialogInterface.dismiss();
                AuthorDialogListener authorDialogListenerNew = authorDialogListener;
                if (authorDialogListenerNew != null) {
                    authorDialogListenerNew.onCancel();
                }
            }
        });
        builder.create().show();
    }
}
