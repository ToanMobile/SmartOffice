package com.artifex.sonui.editor;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SODoc;
import java.util.HashMap;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditNumberFormatDateTime {
    public String[] countries;
    public String[] countries_localized;
    public HashMap<String, String[]> countryFormatMap;
    public String[] formats;

    public static void show(Context context, View view, final ArDkDoc arDkDoc) {
        String[] strArr;
        String string;
        EditNumberFormatDateTime editNumberFormatDateTime = new EditNumberFormatDateTime();
        String extractAssetToString = FileUtils.extractAssetToString(context, "date_time.json");
        if (extractAssetToString != null) {
            try {
                JSONObject jSONObject = new JSONObject(extractAssetToString);
                JSONArray jSONArray = jSONObject.getJSONArray("countries");
                editNumberFormatDateTime.countries = new String[jSONArray.length()];
                editNumberFormatDateTime.countries_localized = new String[jSONArray.length()];
                for (int i = 0; i < jSONArray.length(); i++) {
                    String string2 = jSONArray.getString(i);
                    editNumberFormatDateTime.countries[i] = string2;
                    String[] strArr2 = editNumberFormatDateTime.countries_localized;
                    String replace = string2.replace(" ", "_");
                    int identifier = context.getResources().getIdentifier(replace, "string", context.getPackageName());
                    if (!(identifier == 0 || (string = context.getString(identifier)) == null || string.isEmpty())) {
                        replace = string;
                    }
                    strArr2[i] = replace;
                }
                editNumberFormatDateTime.countryFormatMap = new HashMap<>();
                JSONObject jSONObject2 = jSONObject.getJSONObject("formats");
                int i2 = 0;
                while (true) {
                    strArr = editNumberFormatDateTime.countries;
                    if (i2 >= strArr.length) {
                        break;
                    }
                    JSONArray jSONArray2 = jSONObject2.getJSONArray(strArr[i2]);
                    int length = jSONArray2.length();
                    String[] strArr3 = new String[length];
                    for (int i3 = 0; i3 < jSONArray2.length(); i3++) {
                        strArr3[i3] = jSONArray2.getString(i3);
                    }
                    HashMap<String, String[]> hashMap = editNumberFormatDateTime.countryFormatMap;
                    String str = editNumberFormatDateTime.countries[i2];
                    String[] strArr4 = new String[length];
                    for (int i4 = 0; i4 < length; i4++) {
                        String str2 = strArr3[i4];
                        int lastIndexOf = str2.lastIndexOf("]");
                        if (lastIndexOf >= 0 && str2.indexOf("[h]") != 0) {
                            str2 = str2.substring(lastIndexOf + 1);
                        }
                        int indexOf = str2.indexOf(";@");
                        if (indexOf >= 0) {
                            str2 = str2.substring(0, indexOf);
                        }
                        strArr4[i4] = str2;
                    }
                    hashMap.put(str, strArr4);
                    i2++;
                }
                editNumberFormatDateTime.formats = editNumberFormatDateTime.countryFormatMap.get(strArr[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        View inflate = View.inflate(context, R.layout.sodk_editor_number_format_datetime, (ViewGroup) null);
        final WheelView wheelView = (WheelView) inflate.findViewById(R.id.country_wheel);
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(context, editNumberFormatDateTime.countries_localized);
        arrayWheelAdapter.textSize = 18;
        Resources resources = context.getResources();
        int i5 = R.color.sodk_editor_wheel_item_text_color;
        arrayWheelAdapter.textColor = resources.getColor(i5);
        wheelView.setViewAdapter(arrayWheelAdapter);
        wheelView.setVisibleItems(5);
        final WheelView wheelView2 = (WheelView) inflate.findViewById(R.id.format_wheel);
        final ArrayWheelAdapter arrayWheelAdapter2 = new ArrayWheelAdapter(context, editNumberFormatDateTime.formats);
        arrayWheelAdapter2.textSize = 18;
        arrayWheelAdapter2.textColor = context.getResources().getColor(i5);
        wheelView2.setViewAdapter(arrayWheelAdapter2);
        wheelView2.setVisibleItems(5);
        String selectedCellFormat = ((SODoc) arDkDoc).getSelectedCellFormat();
        int i6 = 0;
        loop4:
        while (true) {
            String[] strArr5 = editNumberFormatDateTime.countries;
            if (i6 >= strArr5.length) {
                wheelView.setCurrentItem(0);
                wheelView2.setCurrentItem(0);
                break;
            }
            T[] tArr = (String[]) editNumberFormatDateTime.countryFormatMap.get(strArr5[i6]);
            for (int i7 = 0; i7 < tArr.length; i7++) {
                if (tArr[i7].equals(selectedCellFormat)) {
                    wheelView.setCurrentItem(i6);
                    ((ArrayWheelAdapter) wheelView2.getViewAdapter()).items = tArr;
                    wheelView2.invalidateWheel(true);
                    editNumberFormatDateTime.formats = tArr;
                    wheelView2.setCurrentItem(i7);
                    break loop4;
                }
            }
            i6++;
        }
        final WheelView wheelView3 = wheelView;
        final WheelView wheelView4 = wheelView2;
        final ArDkDoc arDkDoc2 = arDkDoc;
        wheelView.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                String str = EditNumberFormatDateTime.this.countries[wheelView3.getCurrentItem()];
                EditNumberFormatDateTime editNumberFormatDateTime = EditNumberFormatDateTime.this;
                editNumberFormatDateTime.formats = editNumberFormatDateTime.countryFormatMap.get(str);
                arrayWheelAdapter2.items = EditNumberFormatDateTime.this.formats;
                wheelView4.invalidateWheel(true);
                wheelView4.setCurrentItem(0);
                ((SODoc) arDkDoc2).setSelectedCellFormat(EditNumberFormatDateTime.this.formats[0]);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        wheelView2.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                ((SODoc) arDkDoc).setSelectedCellFormat(EditNumberFormatDateTime.this.formats[wheelView2.getCurrentItem()]);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        nUIPopupWindow.setFocusable(true);
        nUIPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                wheelView.scrollingListeners.clear();
                wheelView2.scrollingListeners.clear();
                EditNumberFormatDateTime editNumberFormatDateTime = EditNumberFormatDateTime.this;
                editNumberFormatDateTime.countries = null;
                editNumberFormatDateTime.formats = null;
                editNumberFormatDateTime.countryFormatMap = null;
            }
        });
        nUIPopupWindow.showAsDropDown(view, 30, 30);
    }
}
