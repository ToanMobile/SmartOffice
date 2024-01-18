package com.artifex.sonui.editor;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapter;


public class EditFont {
    public static String[] fontSizes = {"6 pt", "8 pt", "9 pt", "10 pt", "12 pt", "14 pt", "16 pt", "18 pt", "20 pt", "24 pt", "30 pt", "36 pt", "48 pt", "60 pt", "72 pt"};
    public View anchor;
    public Context context;
    public ArDkDoc doc;
    public String[] fontNames;
    public WheelView fontWheel;
    public WheelView sizeWheel;

    public EditFont(Context context2, View view, ArDkDoc arDkDoc) {
        this.context = context2;
        this.anchor = view;
        this.doc = arDkDoc;
    }

    public void show() {
        FontListAdapter fontListAdapter = new FontListAdapter(this.context);
        fontListAdapter.enumerateFonts(this.doc);
        int count = fontListAdapter.getCount();
        this.fontNames = new String[count];
        for (int i = 0; i < count; i++) {
            this.fontNames[i] = fontListAdapter.getItem(i);
        }
        View inflate = View.inflate(this.context, R.layout.sodk_editor_edit_font, (ViewGroup) null);
        this.fontWheel = (WheelView) inflate.findViewById(R.id.left_wheel);
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(this.context, this.fontNames);
        arrayWheelAdapter.textSize = 18;
        Resources resources = this.context.getResources();
        int i2 = R.color.sodk_editor_wheel_item_text_color;
        arrayWheelAdapter.textColor = resources.getColor(i2);
        this.fontWheel.setViewAdapter(arrayWheelAdapter);
        this.fontWheel.setVisibleItems(5);
        this.sizeWheel = (WheelView) inflate.findViewById(R.id.right_wheel);
        ArrayWheelAdapter arrayWheelAdapter2 = new ArrayWheelAdapter(this.context, fontSizes);
        arrayWheelAdapter2.textSize = 18;
        arrayWheelAdapter2.textColor = this.context.getResources().getColor(i2);
        this.sizeWheel.setViewAdapter(arrayWheelAdapter2);
        this.sizeWheel.setVisibleItems(5);
        String selectionFontName = Utilities.getSelectionFontName(this.doc);
        int i3 = 0;
        while (true) {
            String[] strArr = this.fontNames;
            if (i3 >= strArr.length) {
                break;
            } else if (strArr[i3].equalsIgnoreCase(selectionFontName)) {
                this.fontWheel.setCurrentItem(i3);
                break;
            } else {
                i3++;
            }
        }
        double selectionFontSize = ((SODoc) this.doc).getSelectionFontSize();
        int i4 = 0;
        while (true) {
            String[] strArr2 = fontSizes;
            if (i4 >= strArr2.length) {
                break;
            }
            String str = strArr2[i4];
            if (((double) ((float) Integer.parseInt(str.substring(0, str.length() - 3)))) >= selectionFontSize) {
                this.sizeWheel.setCurrentItem(i4);
                break;
            }
            i4++;
        }
        WheelView wheelView = this.fontWheel;
        wheelView.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                EditFont editFont = EditFont.this;
                ((SODoc) editFont.doc).setSelectionFontName(editFont.fontNames[editFont.fontWheel.getCurrentItem()]);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        WheelView wheelView2 = this.sizeWheel;
        wheelView2.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                EditFont editFont = EditFont.this;
                String str = EditFont.fontSizes[editFont.sizeWheel.getCurrentItem()];
                ((SODoc) editFont.doc).setSelectionFontSize((double) ((float) Integer.parseInt(str.substring(0, str.length() - 3))));
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        nUIPopupWindow.setFocusable(true);
        nUIPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                EditFont editFont = EditFont.this;
                WheelView wheelView = editFont.fontWheel;
                if (wheelView != null) {
                    wheelView.scrollingListeners.clear();
                }
                WheelView wheelView2 = editFont.sizeWheel;
                if (wheelView2 != null) {
                    wheelView2.scrollingListeners.clear();
                }
            }
        });
        nUIPopupWindow.showAsDropDown(this.anchor, 30, 30);
    }
}
