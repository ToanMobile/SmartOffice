package com.artifex.sonui.editor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;

import com.artifex.R;
import com.artifex.mupdf.fitz.Document;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SODoc;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditNumberFormatAccounting {
    public String[] cur_descriptions;
    public String[] cur_formats;

    public static void access$000(EditNumberFormatAccounting editNumberFormatAccounting, ArDkDoc arDkDoc, WheelView wheelView, CheckBox checkBox) {
        String str = editNumberFormatAccounting.cur_formats[wheelView.getCurrentItem()];
        if (checkBox.isChecked()) {
            str = str.replace("#,##0", "#,##0.00");
        }
        ((SODoc) arDkDoc).setSelectedCellFormat(str);
    }

    public void show(Context context, View view, final ArDkDoc arDkDoc) {
        int identifier;
        EditNumberFormatAccounting editNumberFormatAccounting = new EditNumberFormatAccounting();
        String extractAssetToString = FileUtils.extractAssetToString(context, "currencies.json");
        int i = 0;
        if (extractAssetToString != null) {
            try {
                JSONArray jSONArray = new JSONObject(extractAssetToString).getJSONArray("currencies");
                editNumberFormatAccounting.cur_descriptions = new String[jSONArray.length()];
                editNumberFormatAccounting.cur_formats = new String[jSONArray.length()];
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i2);
                    String string = ""; // TODO jSONObject.getString(Tracker.ConsentPartner.KEY_DESCRIPTION);
                    String string2 = jSONObject.getString(Document.META_FORMAT);
                    String string3 = jSONObject.getString("token");
                    if (!(string3 == null || string3.isEmpty() || (identifier = context.getResources().getIdentifier(string3, "string", context.getPackageName())) == 0)) {
                        string = context.getString(identifier);
                    }
                    editNumberFormatAccounting.cur_descriptions[i2] = string;
                    editNumberFormatAccounting.cur_formats[i2] = string2;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        View inflate = View.inflate(context, R.layout.sodk_editor_number_format_accounting, (ViewGroup) null);
        final WheelView wheelView = (WheelView) inflate.findViewById(R.id.cur_wheel);
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(context, editNumberFormatAccounting.cur_descriptions);
        arrayWheelAdapter.textSize = 18;
        arrayWheelAdapter.textColor = context.getResources().getColor(R.color.sodk_editor_wheel_item_text_color);
        wheelView.setViewAdapter(arrayWheelAdapter);
        wheelView.setVisibleItems(5);
        final CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.decimal_places_checkbox);
        String selectedCellFormat = ((SODoc) arDkDoc).getSelectedCellFormat();
        checkBox.setChecked(selectedCellFormat.contains("#,##0.00"));
        String replace = selectedCellFormat.replace("#,##0.00", "#,##0");
        while (true) {
            String[] strArr = editNumberFormatAccounting.cur_formats;
            if (i >= strArr.length) {
                break;
            } else if (replace.equals(strArr[i])) {
                wheelView.setCurrentItem(i);
                break;
            } else {
                i++;
            }
        }
        wheelView.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                EditNumberFormatAccounting.access$000(EditNumberFormatAccounting.this, arDkDoc, wheelView, checkBox);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                EditNumberFormatAccounting.access$000(EditNumberFormatAccounting.this, arDkDoc, wheelView, checkBox);
            }
        });
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        nUIPopupWindow.setFocusable(true);
        nUIPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                wheelView.scrollingListeners.clear();
                EditNumberFormatAccounting editNumberFormatAccounting = EditNumberFormatAccounting.this;
                editNumberFormatAccounting.cur_descriptions = null;
                editNumberFormatAccounting.cur_formats = null;
            }
        });
        nUIPopupWindow.showAsDropDown(view, 30, 30);
    }
}
