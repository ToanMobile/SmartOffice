package com.artifex.sonui.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.artifex.R;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.WheelViewAdapter;

public class LineTypeDialog {
    public static final float[][] patterns = {new float[]{1.0f, BitmapDescriptorFactory.HUE_RED}, new float[]{1.0f, 1.0f}, new float[]{1.0f, 3.0f}, new float[]{3.0f, 1.0f}, new float[]{4.0f, 3.0f}, new float[]{8.0f, 3.0f}, new float[]{3.0f, 1.0f, 1.0f, 1.0f}, new float[]{4.0f, 3.0f, 1.0f, 3.0f}, new float[]{8.0f, 3.0f, 1.0f, 3.0f}, new float[]{3.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}, new float[]{8.0f, 3.0f, 1.0f, 3.0f, 1.0f, 3.0f}};
    public static final int[] styles = {0, 2, 5, 1, 6, 7, 3, 8, 9, 4, 10};

    public class LineTypeAdapter implements WheelViewAdapter {
        public Context mContext;
        public int[] mStyles;

        public LineTypeAdapter(LineTypeDialog lineTypeDialog, Context context, int[] iArr) {
            this.mStyles = iArr;
            this.mContext = context;
        }

        public View getEmptyItem(View view, ViewGroup viewGroup) {
            return null;
        }

        @SuppressLint("WrongConstant")
        public View getItem(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.sodk_editor_line_type_item, viewGroup, false);
            }
            int i2 = this.mStyles[i];
            ((DottedLineView) view.findViewById(R.id.bar)).setPattern(LineTypeDialog.patterns[i]);
            return view;
        }

        public int getItemsCount() {
            return this.mStyles.length;
        }

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        }
    }

    public void show(Context context, View view, final ArDkDoc arDkDoc) {
        LineTypeDialog lineTypeDialog = new LineTypeDialog();
        int selectionLineType = ((SODoc) arDkDoc).getSelectionLineType();
        View inflate = View.inflate(context, R.layout.sodk_editor_line_width_dialog, (ViewGroup) null);
        final WheelView wheelView = (WheelView) inflate.findViewById(R.id.wheel);
        wheelView.setViewAdapter(new LineTypeAdapter(lineTypeDialog, context, styles));
        wheelView.setVisibleItems(5);
        int i = 0;
        wheelView.setCurrentItem(0);
        while (true) {
            int[] iArr = styles;
            if (i < iArr.length) {
                if (iArr[i] == selectionLineType) {
                    wheelView.setCurrentItem(i);
                }
                i++;
            } else {
                wheelView.scrollingListeners.add(new OnWheelScrollListener() {
                    public void onScrollingFinished(WheelView wheelView) {
                        ((SODoc) arDkDoc).setSelectionLineType(LineTypeDialog.styles[wheelView.getCurrentItem()]);
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
