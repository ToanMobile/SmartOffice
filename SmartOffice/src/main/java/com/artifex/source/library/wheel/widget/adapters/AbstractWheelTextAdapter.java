package com.artifex.source.library.wheel.widget.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWheelTextAdapter implements WheelViewAdapter {
    public Context context;
    public List<DataSetObserver> datasetObservers;
    public LayoutInflater inflater;
    public int itemResourceId;
    public int itemTextResourceId;
    public int textColor = -15724528;
    public int textSize = 24;

    public AbstractWheelTextAdapter(Context context2) {
        this.context = context2;
        this.itemResourceId = -1;
        this.itemTextResourceId = 0;
        this.inflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public void configureTextView(TextView textView) {
        textView.setTextColor(this.textColor);
        textView.setGravity(17);
        textView.setTextSize((float) this.textSize);
        textView.setLines(1);
        textView.setTypeface(Typeface.SANS_SERIF, 0);
    }

    public View getEmptyItem(View view, ViewGroup viewGroup) {
        return view == null ? getView(0, viewGroup) : view;
    }

    /* JADX WARNING: type inference failed for: r5v3, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public View getItem(int r3, View r4, ViewGroup r5) {
        /*
            r2 = this;
            r0 = 0
            if (r3 < 0) goto L_0x004e
            int r1 = r2.getItemsCount()
            if (r3 >= r1) goto L_0x004e
            if (r4 != 0) goto L_0x0011
            int r4 = r2.itemResourceId
            android.view.View r4 = r2.getView(r4, r5)
        L_0x0011:
            int r5 = r2.itemTextResourceId
            if (r5 != 0) goto L_0x001f
            boolean r1 = r4 instanceof android.widget.TextView     // Catch:{ ClassCastException -> 0x001d }
            if (r1 == 0) goto L_0x001f
            r0 = r4
            android.widget.TextView r0 = (android.widget.TextView) r0     // Catch:{ ClassCastException -> 0x001d }
            goto L_0x0038
        L_0x001d:
            r3 = move-exception
            goto L_0x0029
        L_0x001f:
            if (r5 == 0) goto L_0x0038
            android.view.View r5 = r4.findViewById(r5)     // Catch:{ ClassCastException -> 0x001d }
            r0 = r5
            android.widget.TextView r0 = (android.widget.TextView) r0     // Catch:{ ClassCastException -> 0x001d }
            goto L_0x0038
        L_0x0029:
            java.lang.String r4 = "AbstractWheelAdapter"
            java.lang.String r5 = "You must supply a resource ID for a TextView"
            android.util.Log.e(r4, r5)
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "AbstractWheelAdapter requires the resource ID to be a TextView"
            r4.<init>(r5, r3)
            throw r4
        L_0x0038:
            if (r0 == 0) goto L_0x004d
            java.lang.CharSequence r3 = r2.getItemText(r3)
            if (r3 != 0) goto L_0x0042
            java.lang.String r3 = ""
        L_0x0042:
            r0.setText(r3)
            int r3 = r2.itemResourceId
            r5 = -1
            if (r3 != r5) goto L_0x004d
            r2.configureTextView(r0)
        L_0x004d:
            return r4
        L_0x004e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.source.library.wheel.widget.adapters.AbstractWheelTextAdapter.getItem(int, android.view.View, android.view.ViewGroup):android.view.View");
    }

    public abstract CharSequence getItemText(int i);

    public final View getView(int i, ViewGroup viewGroup) {
        if (i == -1) {
            return new TextView(this.context);
        }
        if (i != 0) {
            return this.inflater.inflate(i, viewGroup, false);
        }
        return null;
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        if (this.datasetObservers == null) {
            this.datasetObservers = new LinkedList();
        }
        this.datasetObservers.add(dataSetObserver);
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        List<DataSetObserver> list = this.datasetObservers;
        if (list != null) {
            list.remove(dataSetObserver);
        }
    }
}
