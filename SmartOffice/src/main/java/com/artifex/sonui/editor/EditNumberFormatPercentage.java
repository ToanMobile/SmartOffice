package com.artifex.sonui.editor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapter;

public class EditNumberFormatPercentage {
    public static final String[] descriptions;
    public static final String[] formats = {"0%", "0.0%", "0.00%", "0.000%", "0.0000%", "0.00000%", "0.000000%", "0.0000000%", "0.00000000%", "0.000000000%", "0.0000000000%"};

    static {
        StringBuilder sb = new StringBuilder();
        Double valueOf = Double.valueOf(0.123456789d);
        sb.append(String.format("%.9f", new Object[]{valueOf}));
        sb.append("%");
        descriptions = new String[]{String.format("%d", new Object[]{0}) + "%", String.format("%.1f", new Object[]{Double.valueOf(0.1d)}) + "%", String.format("%.2f", new Object[]{Double.valueOf(0.12d)}) + "%", String.format("%.3f", new Object[]{Double.valueOf(0.123d)}) + "%", String.format("%.4f", new Object[]{Double.valueOf(0.1234d)}) + "%", String.format("%.5f", new Object[]{Double.valueOf(0.12345d)}) + "%", String.format("%.6f", new Object[]{Double.valueOf(0.123456d)}) + "%", String.format("%.7f", new Object[]{Double.valueOf(0.1234567d)}) + "%", String.format("%.8f", new Object[]{Double.valueOf(0.12345678d)}) + "%", sb.toString(), String.format("%.10f", new Object[]{valueOf})};
    }

    public void show(Context context, View view, final ArDkDoc arDkDoc) {
        EditNumberFormatPercentage editNumberFormatPercentage = new EditNumberFormatPercentage();
        String selectedCellFormat = ((SODoc) arDkDoc).getSelectedCellFormat();
        View inflate = View.inflate(context, R.layout.sodk_editor_number_format_percentage, (ViewGroup) null);
        final WheelView wheelView = (WheelView) inflate.findViewById(R.id.wheel);
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(context, descriptions);
        arrayWheelAdapter.textSize = 18;
        arrayWheelAdapter.textColor = context.getResources().getColor(R.color.sodk_editor_wheel_item_text_color);
        wheelView.setViewAdapter(arrayWheelAdapter);
        wheelView.setVisibleItems(5);
        wheelView.setCurrentItem(0);
        int i = 0;
        while (true) {
            String[] strArr = formats;
            if (i < strArr.length) {
                if (strArr[i].equals(selectedCellFormat)) {
                    wheelView.setCurrentItem(0);
                }
                i++;
            } else {
                wheelView.scrollingListeners.add(new OnWheelScrollListener() {
                    public void onScrollingFinished(WheelView wheelView) {
                        ((SODoc) arDkDoc).setSelectedCellFormat(EditNumberFormatPercentage.formats[wheelView.getCurrentItem()]);
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
