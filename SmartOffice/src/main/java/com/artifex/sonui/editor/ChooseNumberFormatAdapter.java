package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class ChooseNumberFormatAdapter extends BaseAdapter {
    public static final int NUMBER_FORMATS_ACCOUNTING = 6;
    public static final int NUMBER_FORMATS_CURRENCY = 4;
    public static final int NUMBER_FORMATS_CUSTOM = 7;
    public static final int NUMBER_FORMATS_DATETIME = 1;
    public static final int NUMBER_FORMATS_FRACTION = 3;
    public static final int NUMBER_FORMATS_GENERAL = 0;
    public static final int NUMBER_FORMATS_NUMBER = 2;
    public static final int NUMBER_FORMATS_PERCENTAGE = 5;
    public static final int NUM_CATEGORIES = 8;
    public int mArrowWidth;
    public String[] mCategories;
    public final Context mContext;
    public int mGridWidth;
    public int mItemWidth = 0;

    public ChooseNumberFormatAdapter(Activity activity, int i) {
        Context baseContext = activity.getBaseContext();
        this.mContext = baseContext;
        String[] strArr = new String[8];
        this.mCategories = strArr;
        strArr[0] = activity.getString(R.string.sodk_editor_format_category_general);
        this.mCategories[1] = activity.getString(R.string.sodk_editor_format_category_date_and_time);
        this.mCategories[2] = activity.getString(R.string.sodk_editor_format_category_number);
        this.mCategories[3] = activity.getString(R.string.sodk_editor_format_category_fraction);
        this.mCategories[4] = activity.getString(R.string.sodk_editor_format_category_currency);
        this.mCategories[5] = activity.getString(R.string.sodk_editor_format_category_percentage);
        this.mCategories[6] = activity.getString(R.string.sodk_editor_format_category_accounting);
        this.mCategories[7] = activity.getString(R.string.sodk_editor_format_category_custom);
        View inflate = LayoutInflater.from(baseContext).inflate(R.layout.sodk_editor_number_formats_view, (ViewGroup) null);
        SOTextView sOTextView = (SOTextView) inflate.findViewById(R.id.name);
        for (int i2 = 0; i2 < 8; i2++) {
            sOTextView.setText((CharSequence) this.mCategories[i2]);
            sOTextView.measure(0, 0);
            int measuredWidth = sOTextView.getMeasuredWidth();
            if (measuredWidth > this.mItemWidth) {
                this.mItemWidth = measuredWidth;
            }
        }
        SOTextView sOTextView2 = (SOTextView) inflate.findViewById(R.id.arrow);
        sOTextView2.measure(0, 0);
        int measuredWidth2 = sOTextView2.getMeasuredWidth();
        this.mArrowWidth = measuredWidth2;
        this.mGridWidth = (this.mItemWidth + measuredWidth2) * i;
    }

    public int getCount() {
        return this.mCategories.length;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        viewGroup.getLayoutParams().width = this.mGridWidth;
        ((LinearLayout) viewGroup.getParent()).requestLayout();
        String str = this.mCategories[i];
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_number_formats_view, (ViewGroup) null);
        }
        SOTextView sOTextView = (SOTextView) view.findViewById(R.id.name);
        if (sOTextView != null) {
            sOTextView.setText((CharSequence) str);
            sOTextView.getLayoutParams().width = this.mItemWidth;
        }
        SOTextView sOTextView2 = (SOTextView) view.findViewById(R.id.arrow);
        if (sOTextView2 != null) {
            sOTextView2.getLayoutParams().width = this.mArrowWidth;
            if (str.equals(this.mCategories[0])) {
                sOTextView2.setText((CharSequence) "");
            }
        }
        return view;
    }

    public String getItem(int i) {
        return this.mCategories[i];
    }
}
