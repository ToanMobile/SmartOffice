package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;
import java.util.Arrays;
import java.util.List;

public class FontListAdapter extends BaseAdapter {
    public final Context mContext;
    public List<String> mFonts;

    public FontListAdapter(Context context) {
        this.mContext = context;
    }

    public void enumerateFonts(ArDkDoc arDkDoc) {
        this.mFonts = Arrays.asList(((SODoc) arDkDoc).getFontList().split(","));
    }

    public int getCount() {
        return this.mFonts.size();
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        String str = this.mFonts.get(i);
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_font_list_entry, (ViewGroup) null);
        }
        SOTextView sOTextView = (SOTextView) view.findViewById(R.id.name);
        sOTextView.setText((CharSequence) str);
        sOTextView.setTypeface(Typeface.create(str.toLowerCase(), 0));
        return view;
    }

    public String getItem(int i) {
        return this.mFonts.get(i);
    }
}
