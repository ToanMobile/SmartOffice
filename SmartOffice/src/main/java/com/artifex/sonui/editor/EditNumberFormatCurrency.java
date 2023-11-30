package com.artifex.sonui.editor;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import com.artifex.mupdf.fitz.Document;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SODoc;
import com.kochava.base.Tracker;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapter;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapterColor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditNumberFormatCurrency {
    public static final String[] neg_descriptions;
    public static final String[] neg_formats = {"DEC", "DEC;[Red]DEC", "DEC;[Red]\\-DEC", "DEC_);(DEC)", "DEC_);[Red](DEC)"};
    public WheelView curWheel;
    public String[] cur_descriptions;
    public String[] cur_formats;
    public ArDkDoc doc;
    public WheelView negWheel;
    public CheckBox twoPlacesCheck;

    static {
        Double valueOf = Double.valueOf(-1234.1d);
        Double valueOf2 = Double.valueOf(1234.1d);
        neg_descriptions = new String[]{String.format("%.2f", new Object[]{valueOf}), String.format("%.2f (red)", new Object[]{valueOf2}), String.format("%.2f (red)", new Object[]{valueOf}), String.format("(%.2f)", new Object[]{valueOf2}), String.format("(%.2f) (red)", new Object[]{valueOf2})};
    }

    public static void access$000(EditNumberFormatCurrency editNumberFormatCurrency) {
        String str = editNumberFormatCurrency.cur_formats[editNumberFormatCurrency.curWheel.getCurrentItem()];
        if (editNumberFormatCurrency.twoPlacesCheck.isChecked()) {
            str = str.replace("#,##0", "#,##0.00");
        }
        ((SODoc) editNumberFormatCurrency.doc).setSelectedCellFormat(neg_formats[editNumberFormatCurrency.negWheel.getCurrentItem()].replace("DEC", str));
    }

    public static void show(Context context, View view, ArDkDoc arDkDoc) {
        int identifier;
        EditNumberFormatCurrency editNumberFormatCurrency = new EditNumberFormatCurrency();
        String extractAssetToString = FileUtils.extractAssetToString(context, "currencies.json");
        if (extractAssetToString != null) {
            try {
                JSONArray jSONArray = new JSONObject(extractAssetToString).getJSONArray("currencies");
                editNumberFormatCurrency.cur_descriptions = new String[jSONArray.length()];
                editNumberFormatCurrency.cur_formats = new String[jSONArray.length()];
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    String string = jSONObject.getString(Tracker.ConsentPartner.KEY_DESCRIPTION);
                    String string2 = jSONObject.getString(Document.META_FORMAT);
                    String string3 = jSONObject.getString("token");
                    if (!(string3 == null || string3.isEmpty() || (identifier = context.getResources().getIdentifier(string3, "string", context.getPackageName())) == 0)) {
                        string = context.getString(identifier);
                    }
                    editNumberFormatCurrency.cur_descriptions[i] = string;
                    editNumberFormatCurrency.cur_formats[i] = string2;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        View inflate = View.inflate(context, R.layout.sodk_editor_number_format_currency, (ViewGroup) null);
        editNumberFormatCurrency.doc = arDkDoc;
        editNumberFormatCurrency.curWheel = (WheelView) inflate.findViewById(R.id.left_wheel);
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(context, editNumberFormatCurrency.cur_descriptions);
        arrayWheelAdapter.textSize = 18;
        Resources resources = context.getResources();
        int i2 = R.color.sodk_editor_wheel_item_text_color;
        arrayWheelAdapter.textColor = resources.getColor(i2);
        editNumberFormatCurrency.curWheel.setViewAdapter(arrayWheelAdapter);
        editNumberFormatCurrency.curWheel.setVisibleItems(5);
        editNumberFormatCurrency.negWheel = (WheelView) inflate.findViewById(R.id.right_wheel);
        ArrayWheelAdapterColor arrayWheelAdapterColor = new ArrayWheelAdapterColor(context, neg_descriptions);
        arrayWheelAdapterColor.textSize = 18;
        arrayWheelAdapterColor.textColor = context.getResources().getColor(i2);
        editNumberFormatCurrency.negWheel.setViewAdapter(arrayWheelAdapterColor);
        editNumberFormatCurrency.negWheel.setVisibleItems(5);
        editNumberFormatCurrency.twoPlacesCheck = (CheckBox) inflate.findViewById(R.id.decimal_places_checkbox);
        String selectedCellFormat = ((SODoc) editNumberFormatCurrency.doc).getSelectedCellFormat();
        editNumberFormatCurrency.twoPlacesCheck.setChecked(selectedCellFormat.contains("#,##0.00"));
        String replace = selectedCellFormat.replace("#,##0.00", "#,##0");
        int i3 = 0;
        loop1:
        while (true) {
            if (i3 >= editNumberFormatCurrency.cur_formats.length) {
                break;
            }
            int i4 = 0;
            while (true) {
                String[] strArr = neg_formats;
                if (i4 >= strArr.length) {
                    break;
                } else if (strArr[i4].replace("DEC", editNumberFormatCurrency.cur_formats[i3]).equals(replace)) {
                    editNumberFormatCurrency.curWheel.setCurrentItem(i3);
                    editNumberFormatCurrency.negWheel.setCurrentItem(i4);
                    break loop1;
                } else {
                    i4++;
                }
            }
            i3++;
        }
        WheelView wheelView = editNumberFormatCurrency.negWheel;
        wheelView.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                EditNumberFormatCurrency.access$000(EditNumberFormatCurrency.this);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        WheelView wheelView2 = editNumberFormatCurrency.curWheel;
        wheelView2.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                EditNumberFormatCurrency.access$000(EditNumberFormatCurrency.this);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        editNumberFormatCurrency.twoPlacesCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                EditNumberFormatCurrency.access$000(EditNumberFormatCurrency.this);
            }
        });
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        nUIPopupWindow.setFocusable(true);
        nUIPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                EditNumberFormatCurrency editNumberFormatCurrency = EditNumberFormatCurrency.this;
                editNumberFormatCurrency.curWheel.scrollingListeners.clear();
                editNumberFormatCurrency.negWheel.scrollingListeners.clear();
                editNumberFormatCurrency.curWheel = null;
                editNumberFormatCurrency.negWheel = null;
                editNumberFormatCurrency.twoPlacesCheck = null;
                editNumberFormatCurrency.doc = null;
                editNumberFormatCurrency.cur_descriptions = null;
                editNumberFormatCurrency.cur_formats = null;
            }
        });
        nUIPopupWindow.showAsDropDown(view, 30, 30);
    }
}
