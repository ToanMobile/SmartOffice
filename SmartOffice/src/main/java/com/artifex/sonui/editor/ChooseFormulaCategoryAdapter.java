package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.artifex.R;
import com.artifex.solib.SOLib;

public class ChooseFormulaCategoryAdapter extends BaseAdapter {
    public final String[] mCategories;
    public final Context mContext;

    public ChooseFormulaCategoryAdapter(Activity activity) {
        this.mContext = activity.getBaseContext();
        this.mCategories = SOLib.getLib(activity).getFormulaeCategories();
    }

    public int getCount() {
        return this.mCategories.length;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        String str = this.mCategories[i];
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_formula_category_view, (ViewGroup) null);
        }
        ((SOTextView) view.findViewById(R.id.name)).setText((CharSequence) str);
        return view;
    }

    public String getItem(int i) {
        return this.mCategories[i];
    }
}
