package com.artifex.source.library.wheel.widget.adapters;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class ArrayWheelAdapterColor<T> extends AbstractWheelTextAdapter {
    public T[] items;

    public ArrayWheelAdapterColor(Context context, T[] tArr) {
        super(context);
        this.items = tArr;
    }

    public void configureTextView(TextView textView) {
        super.configureTextView(textView);
        String charSequence = textView.getText().toString();
        if (charSequence.endsWith("(red)")) {
            textView.setText(charSequence.replace("(red)", ""));
            textView.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    public CharSequence getItemText(int i) {
        if (i < 0) {
            return null;
        }
        T[] tArr = this.items;
        if (i >= tArr.length) {
            return null;
        }
        T t = tArr[i];
        if (t instanceof CharSequence) {
            return (CharSequence) t;
        }
        return t.toString();
    }

    public int getItemsCount() {
        return this.items.length;
    }
}
