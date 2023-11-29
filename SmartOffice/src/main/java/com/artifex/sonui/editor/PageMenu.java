package com.artifex.sonui.editor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class PageMenu implements PopupWindow.OnDismissListener {
    public boolean mAllowDelete;
    public final View mAnchor;
    public final Context mContext;
    public ActionListener mListener;
    public NUIPopupWindow popupWindow;

    public interface ActionListener {
        void onDelete();

        void onDuplicate();
    }

    public PageMenu(Context context, View view, boolean z, ActionListener actionListener) {
        this.mContext = context;
        this.mAnchor = view;
        this.mListener = actionListener;
        this.mAllowDelete = z;
    }

    public void onDismiss() {
        this.popupWindow.dismiss();
    }

    public void show() {
        View findViewById;
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_page_menu, (ViewGroup) null);
        ((Button) inflate.findViewById(R.id.delete_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PageMenu.this.popupWindow.dismiss();
                PageMenu.this.mListener.onDelete();
            }
        });
        ((Button) inflate.findViewById(R.id.duplicate_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PageMenu.this.popupWindow.dismiss();
                PageMenu.this.mListener.onDuplicate();
            }
        });
        if (!this.mAllowDelete && (findViewById = inflate.findViewById(R.id.delete_button_wrapper)) != null) {
            findViewById.setVisibility(8);
        }
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        this.popupWindow = nUIPopupWindow;
        nUIPopupWindow.setFocusable(true);
        this.popupWindow.setOnDismissListener(this);
        inflate.measure(0, 0);
        int height = this.mAnchor.getHeight();
        this.popupWindow.showAsDropDown(this.mAnchor, 0, (-height) - inflate.getMeasuredHeight());
    }
}
