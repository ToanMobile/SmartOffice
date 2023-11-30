package com.artifex.sonui.editor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapter;

public class ListWheelDialog {
    public boolean allowTabAndEnter = true;
    public AlertDialog dialog = null;
    public boolean finished = false;

    public interface ListWheelDialogListener {
        void cancel();

        void update(String str);
    }

    public void show(Context context, String[] strArr, String str, ListWheelDialogListener listWheelDialogListener, boolean z) {
        this.allowTabAndEnter = z;
        show(context, strArr, str, listWheelDialogListener);
    }

    public void show(Context context, final String[] strArr, String str, final ListWheelDialogListener listWheelDialogListener) {
        View inflate = View.inflate(context, R.layout.sodk_editor_wheel_chooser_dialog, (ViewGroup) null);
        final WheelView wheelView = (WheelView) inflate.findViewById(R.id.wheel);
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(context, strArr);
        wheelView.setViewAdapter(arrayWheelAdapter);
        wheelView.setVisibleItems(5);
        arrayWheelAdapter.textColor = context.getResources().getColor(R.color.sodk_editor_wheel_item_text_color);
        int i = -1;
        int i2 = 0;
        while (true) {
            if (i2 >= strArr.length) {
                break;
            } else if (str.equals(strArr[i2])) {
                i = i2;
                break;
            } else {
                i2++;
            }
        }
        if (i >= 0) {
            wheelView.setCurrentItem(i);
        }
        this.dialog = new AlertDialog.Builder(context).setView(inflate).create();
        inflate.findViewById(R.id.sodk_editor_cancel_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ListWheelDialog listWheelDialog = ListWheelDialog.this;
                ListWheelDialogListener listWheelDialogListener = listWheelDialogListener;
                listWheelDialog.finished = true;
                listWheelDialog.dialog.dismiss();
                listWheelDialogListener.cancel();
            }
        });
        inflate.findViewById(R.id.sodk_editor_update_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ListWheelDialog listWheelDialog = ListWheelDialog.this;
                listWheelDialog.finished = true;
                listWheelDialog.dialog.dismiss();
                listWheelDialogListener.update(strArr[wheelView.getCurrentItem()]);
            }
        });
        this.dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (ListWheelDialog.this.allowTabAndEnter && keyEvent.getAction() == 1 && (i == 61 || i == 66)) {
                    ListWheelDialog.this.finished = true;
                    dialogInterface.dismiss();
                    listWheelDialogListener.update(strArr[wheelView.getCurrentItem()]);
                }
                if (keyEvent.getAction() == 1 && i == 4) {
                    ListWheelDialog listWheelDialog = ListWheelDialog.this;
                    ListWheelDialogListener listWheelDialogListener = listWheelDialogListener;
                    listWheelDialog.finished = true;
                    listWheelDialog.dialog.dismiss();
                    listWheelDialogListener.cancel();
                }
                return true;
            }
        });
        this.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                if (!ListWheelDialog.this.finished) {
                    listWheelDialogListener.cancel();
                }
            }
        });
        Window window = this.dialog.getWindow();
        window.setLayout(-2, -2);
        window.setGravity(17);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialog.show();
    }
}
