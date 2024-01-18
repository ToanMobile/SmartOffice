package com.artifex.sonui.editor;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapter;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapterColor;

public class EditNumberFormatNumber {
    public static final String[] left_descriptions;
    public static final String[] left_formats = {"DEC", "DEC;[Red]DEC", "DEC;[Red]\\-DEC", "DEC_);(DEC)", "DEC_);[Red](DEC)"};
    public static final String[] right_descriptions;
    public static final String[] right_formats = {"0", "0.0", "0.00", "0.000", "0.0000", "0.00000", "0.000000", "0.0000000", "0.00000000", "0.000000000", "0.0000000000"};
    public ArDkDoc doc;
    public WheelView leftWheel;
    public WheelView rightWheel;
    public CheckBox scientificCheck;
    public CheckBox thousandsSepCheck;

    static {
        Double valueOf = Double.valueOf(-1234.1d);
        Double valueOf2 = Double.valueOf(1234.1d);
        left_descriptions = new String[]{String.format("%.2f", valueOf), String.format("%.2f (red)", valueOf2), String.format("%.2f (red)", valueOf), String.format("(%.2f)", valueOf2), String.format("(%.2f) (red)", valueOf2)};
        Object[] objArr = {Double.valueOf(0.1d)};
        Object[] objArr2 = {Double.valueOf(0.12d)};
        Object[] objArr3 = {Double.valueOf(0.123d)};
        Object[] objArr4 = {Double.valueOf(0.1234d)};
        Object[] objArr5 = {Double.valueOf(0.12345d)};
        Object[] objArr6 = {Double.valueOf(0.123456d)};
        Object[] objArr7 = {Double.valueOf(0.1234567d)};
        Object[] objArr8 = {Double.valueOf(0.12345678d)};
        Double valueOf3 = Double.valueOf(0.123456789d);
        right_descriptions = new String[]{String.format("%d", 0), String.format("%.1f", objArr), String.format("%.2f", objArr2), String.format("%.3f", objArr3), String.format("%.4f", objArr4), String.format("%.5f", objArr5), String.format("%.6f", objArr6), String.format("%.7f", objArr7), String.format("%.8f", objArr8), String.format("%.9f", valueOf3), String.format("%.10f", valueOf3)};
    }

    public static void access$000(EditNumberFormatNumber editNumberFormatNumber) {
        String str = right_formats[editNumberFormatNumber.rightWheel.getCurrentItem()];
        if (editNumberFormatNumber.thousandsSepCheck.isChecked()) {
            str = str + "#,##";
        }
        if (editNumberFormatNumber.scientificCheck.isChecked()) {
            str = str + "E+00";
        }
        ((SODoc) editNumberFormatNumber.doc).setSelectedCellFormat(left_formats[editNumberFormatNumber.leftWheel.getCurrentItem()].replace("DEC", str));
    }

    /** @noinspection rawtypes*/
    public void show(Context context, View view, ArDkDoc arDkDoc) {
        EditNumberFormatNumber editNumberFormatNumber = new EditNumberFormatNumber();
        View inflate = View.inflate(context, R.layout.sodk_editor_number_format_number, (ViewGroup) null);
        editNumberFormatNumber.doc = arDkDoc;
        editNumberFormatNumber.leftWheel = (WheelView) inflate.findViewById(R.id.left_wheel);
        ArrayWheelAdapterColor arrayWheelAdapterColor = new ArrayWheelAdapterColor(context, left_descriptions);
        arrayWheelAdapterColor.textSize = 18;
        Resources resources = context.getResources();
        int i = R.color.sodk_editor_wheel_item_text_color;
        arrayWheelAdapterColor.textColor = resources.getColor(i);
        editNumberFormatNumber.leftWheel.setViewAdapter(arrayWheelAdapterColor);
        editNumberFormatNumber.leftWheel.setVisibleItems(5);
        editNumberFormatNumber.rightWheel = (WheelView) inflate.findViewById(R.id.right_wheel);
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(context, right_descriptions);
        arrayWheelAdapter.textSize = 18;
        arrayWheelAdapter.textColor = context.getResources().getColor(i);
        editNumberFormatNumber.rightWheel.setViewAdapter(arrayWheelAdapter);
        editNumberFormatNumber.rightWheel.setVisibleItems(5);
        editNumberFormatNumber.thousandsSepCheck = (CheckBox) inflate.findViewById(R.id.thousand_sep_checkbox);
        editNumberFormatNumber.scientificCheck = (CheckBox) inflate.findViewById(R.id.scientific_checkbox);
        String selectedCellFormat = ((SODoc) editNumberFormatNumber.doc).getSelectedCellFormat();
        editNumberFormatNumber.thousandsSepCheck.setChecked(selectedCellFormat.contains("#,##"));
        String replace = selectedCellFormat.replace("#,##", "");
        editNumberFormatNumber.scientificCheck.setChecked(replace.contains("E+00"));
        String replace2 = replace.replace("E+00", "");
        int i2 = 0;
        loop0:
        while (true) {
            if (i2 >= right_formats.length) {
                break;
            }
            int i3 = 0;
            while (true) {
                String[] strArr = left_formats;
                if (i3 >= strArr.length) {
                    break;
                } else if (strArr[i3].replace("DEC", right_formats[i2]).equals(replace2)) {
                    editNumberFormatNumber.leftWheel.setCurrentItem(i3);
                    editNumberFormatNumber.rightWheel.setCurrentItem(i2);
                    break loop0;
                } else {
                    i3++;
                }
            }
            i2++;
        }
        WheelView wheelView = editNumberFormatNumber.rightWheel;
        wheelView.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                EditNumberFormatNumber.access$000(editNumberFormatNumber);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        WheelView wheelView2 = editNumberFormatNumber.leftWheel;
        wheelView2.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                EditNumberFormatNumber.access$000(editNumberFormatNumber);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        editNumberFormatNumber.scientificCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                EditNumberFormatNumber.access$000(editNumberFormatNumber);
            }
        });
        editNumberFormatNumber.thousandsSepCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                EditNumberFormatNumber.access$000(editNumberFormatNumber);
            }
        });
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        nUIPopupWindow.setFocusable(true);
        nUIPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                editNumberFormatNumber.leftWheel.scrollingListeners.clear();
                editNumberFormatNumber.rightWheel.scrollingListeners.clear();
                editNumberFormatNumber.leftWheel = null;
                editNumberFormatNumber.rightWheel = null;
                editNumberFormatNumber.thousandsSepCheck = null;
                editNumberFormatNumber.scientificCheck = null;
                editNumberFormatNumber.doc = null;
            }
        });
        nUIPopupWindow.showAsDropDown(view, 30, 30);
    }
}
