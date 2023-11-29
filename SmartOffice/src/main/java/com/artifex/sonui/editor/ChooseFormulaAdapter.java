package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.artifex.solib.SOLib;

public class ChooseFormulaAdapter extends BaseAdapter {
    public final Context mContext;
    public final String[] mFormulas;

    public ChooseFormulaAdapter(Activity activity, String str) {
        this.mContext = activity.getBaseContext();
        this.mFormulas = SOLib.getLib(activity).getFormulae(str);
    }

    public int getCount() {
        return this.mFormulas.length;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        String[] split = this.mFormulas[i].split("\\|");
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_formula_view, (ViewGroup) null);
        }
        ((SOTextView) view.findViewById(R.id.formula)).setText((CharSequence) split[0]);
        ((SOTextView) view.findViewById(R.id.description)).setText((CharSequence) split[1]);
        return view;
    }

    public String getItem(int i) {
        return this.mFormulas[i].split("\\|")[0];
    }
}
