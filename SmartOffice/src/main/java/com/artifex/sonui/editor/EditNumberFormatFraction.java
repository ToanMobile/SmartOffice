package com.artifex.sonui.editor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapter;

public class EditNumberFormatFraction {
    public static final String[] formats = {"# ?/?", "# ??/??", "#\\ ???/???", "#\\ ?/2", "#\\ ?/4", "#\\ ?/8", "#\\ ?/16", "#\\ ?/10", "#\\ ?/100"};
    public String[] descriptions;

    public static void show(Context context, View view, final ArDkDoc arDkDoc) {
        EditNumberFormatFraction editNumberFormatFraction = new EditNumberFormatFraction();
        int i = 0;
        editNumberFormatFraction.descriptions = new String[]{context.getString(R.string.sodk_editor_up_to_one_digit), context.getString(R.string.sodk_editor_up_to_two_digits), context.getString(R.string.sodk_editor_up_to_three_digits), context.getString(R.string.sodk_editor_as_halves), context.getString(R.string.sodk_editor_as_quarters), context.getString(R.string.sodk_editor_as_eighths), context.getString(R.string.sodk_editor_as_sixteenths), context.getString(R.string.sodk_editor_as_tenths), context.getString(R.string.sodk_editor_as_hundredths)};
        String selectedCellFormat = ((SODoc) arDkDoc).getSelectedCellFormat();
        View inflate = View.inflate(context, R.layout.sodk_editor_number_format_fractions, (ViewGroup) null);
        final WheelView wheelView = inflate.findViewById(R.id.wheel);
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(context, editNumberFormatFraction.descriptions);
        arrayWheelAdapter.textSize = 18;
        arrayWheelAdapter.textColor = context.getResources().getColor(R.color.sodk_editor_wheel_item_text_color);
        wheelView.setViewAdapter(arrayWheelAdapter);
        wheelView.setVisibleItems(5);
        wheelView.setCurrentItem(0);
        while (true) {
            String[] strArr = formats;
            if (i >= strArr.length) {
                break;
            } else if (strArr[i].equals(selectedCellFormat)) {
                wheelView.setCurrentItem(i);
                break;
            } else {
                i++;
            }
        }
        wheelView.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                ((SODoc) arDkDoc).setSelectedCellFormat(EditNumberFormatFraction.formats[wheelView.getCurrentItem()]);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        nUIPopupWindow.setFocusable(true);
        nUIPopupWindow.setOnDismissListener(() -> wheelView.scrollingListeners.clear());
        nUIPopupWindow.showAsDropDown(view, 30, 30);
    }
}
