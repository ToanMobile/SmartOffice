package com.artifex.source.library.wheel.widget.adapters;

import android.content.Context;

public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {
    public T[] items;

    public ArrayWheelAdapter(Context context, T[] tArr) {
        super(context);
        this.items = tArr;
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
