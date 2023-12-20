package com.artifex.sonui.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.WheelViewAdapter;

public class LineWidthDialog {
    public static final float[] values = {0.25f, 0.5f, 1.0f, 1.5f, 3.0f, 4.5f, 6.0f, 8.0f, 12.0f, 18.0f, 24.0f};

    public class LineWidthAdapter implements WheelViewAdapter {
        public Context mContext;
        public float[] mValues;

        public LineWidthAdapter(LineWidthDialog lineWidthDialog, Context context, float[] fArr) {
            this.mValues = fArr;
            this.mContext = context;
        }

        public View getEmptyItem(View view, ViewGroup viewGroup) {
            return null;
        }

        @SuppressLint("WrongConstant")
        public View getItem(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.sodk_editor_line_width_item, viewGroup, false);
            }
            float f = LineWidthDialog.values[i];
            ((SOTextView) view.findViewById(R.id.value)).setText((CharSequence) Utilities.formatFloat(f) + " pt");
            int i2 = (int) ((f * 3.0f) / 2.0f);
            if (i2 < 1) {
                i2 = 1;
            }
            View findViewById = view.findViewById(R.id.bar);
            findViewById.getLayoutParams().height = i2;
            findViewById.setLayoutParams(findViewById.getLayoutParams());
            return view;
        }

        public int getItemsCount() {
            return this.mValues.length;
        }

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        }
    }

    public static void show(Context context, View view, final ArDkDoc arDkDoc) {
        LineWidthDialog lineWidthDialog = new LineWidthDialog();
        float selectionLineWidth = ((SODoc) arDkDoc).getSelectionLineWidth();
        View inflate = View.inflate(context, R.layout.sodk_editor_line_width_dialog, (ViewGroup) null);
        final WheelView wheelView = (WheelView) inflate.findViewById(R.id.wheel);
        wheelView.setViewAdapter(new LineWidthAdapter(lineWidthDialog, context, values));
        wheelView.setVisibleItems(5);
        int i = 0;
        wheelView.setCurrentItem(0);
        while (true) {
            float[] fArr = values;
            if (i < fArr.length) {
                if (fArr[i] == selectionLineWidth) {
                    wheelView.setCurrentItem(i);
                }
                i++;
            } else {
                wheelView.scrollingListeners.add(new OnWheelScrollListener() {
                    public void onScrollingFinished(WheelView wheelView) {
                        ((SODoc) arDkDoc).setSelectionLineWidth(LineWidthDialog.values[wheelView.getCurrentItem()]);
                    }

                    public void onScrollingStarted(WheelView wheelView) {
                    }
                });
                NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
                nUIPopupWindow.setFocusable(true);
                nUIPopupWindow.setOnDismissListener(() -> wheelView.scrollingListeners.clear());
                nUIPopupWindow.showAsDropDown(view, 30, 30);
                return;
            }
        }
    }
}
