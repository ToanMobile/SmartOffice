package com.artifex.sonui.editor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.core.content.ContextCompat;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;
import java.util.Objects;
import com.artifex.source.library.wheel.widget.OnWheelScrollListener;
import com.artifex.source.library.wheel.widget.WheelView;
import com.artifex.source.library.wheel.widget.adapters.ArrayWheelAdapter;

public class EditNumberFormatCustom {
    public static String[] descriptions;
    public static String[] fixed_descriptions;
    public static final String[] fixed_formats = {"General", "0", "[$£-809]#,##0", "yyyy-mm-dd;@", "# ?/?"};
    public static String[] formats;
    public LinearLayout editButton = null;
    public SOTextView editIndicator = null;
    public WheelView formatWheel = null;
    public ArDkDoc theDocument;
    public ArrayWheelAdapter<String> wheelAdapter = null;

    public interface TextListener {
        void setText(String str);
    }

    public static int access$000(EditNumberFormatCustom editNumberFormatCustom, String str) {
        Objects.requireNonNull(editNumberFormatCustom);
        int i = 0;
        while (true) {
            String[] strArr = formats;
            if (i >= strArr.length) {
                return -1;
            }
            if (str.equals(strArr[i])) {
                return i;
            }
            i++;
        }
    }

    public static void access$1000(EditNumberFormatCustom editNumberFormatCustom, final Context context, String str, String str2, final TextListener textListener) {
        Objects.requireNonNull(editNumberFormatCustom);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.sodk_editor_alert_dialog_style);
        builder.setTitle(str);
        View inflate = LayoutInflater.from(context).inflate(R.layout.sodk_editor_number_format_prompt, (ViewGroup) null);
        final SOEditText sOEditText = (SOEditText) inflate.findViewById(R.id.editTextDialogUserInput);
        sOEditText.setText(str2);
        builder.setView(inflate);
        builder.setPositiveButton(R.string.sodk_editor_OK, new DialogInterface.OnClickListener(editNumberFormatCustom) {
            public void onClick(DialogInterface dialogInterface, int i) {
                Utilities.hideKeyboard(context, sOEditText);
                textListener.setText(sOEditText.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.sodk_editor_cancel, new DialogInterface.OnClickListener(editNumberFormatCustom) {
            public void onClick(DialogInterface dialogInterface, int i) {
                Utilities.hideKeyboard(context, sOEditText);
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public static void access$200(EditNumberFormatCustom editNumberFormatCustom, ArDkDoc arDkDoc, WheelView wheelView) {
        Objects.requireNonNull(editNumberFormatCustom);
        ((SODoc) arDkDoc).setSelectedCellFormat(formats[wheelView.getCurrentItem()]);
    }

    public static void show(final Context context, View view, final ArDkDoc arDkDoc) {
        EditNumberFormatCustom editNumberFormatCustom = new EditNumberFormatCustom();
        int i = 0;
        String[] strArr = {context.getString(R.string.sodk_editor_format_category_general), context.getString(R.string.sodk_editor_format_category_number), context.getString(R.string.sodk_editor_format_category_currency), context.getString(R.string.sodk_editor_format_category_date_and_time), context.getString(R.string.sodk_editor_format_category_fraction)};
        fixed_descriptions = strArr;
        if (descriptions == null) {
            descriptions = (String[]) strArr.clone();
        }
        if (formats == null) {
            formats = (String[]) fixed_formats.clone();
        }
        editNumberFormatCustom.theDocument = arDkDoc;
        View inflate = View.inflate(context, R.layout.sodk_editor_number_format_custom, (ViewGroup) null);
        editNumberFormatCustom.formatWheel = (WheelView) inflate.findViewById(R.id.custom_wheel);
        ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<>(context, descriptions);
        editNumberFormatCustom.wheelAdapter = arrayWheelAdapter;
        arrayWheelAdapter.textSize = 18;
        arrayWheelAdapter.textColor = context.getResources().getColor(R.color.sodk_editor_wheel_item_text_color);
        editNumberFormatCustom.formatWheel.setViewAdapter(editNumberFormatCustom.wheelAdapter);
        editNumberFormatCustom.formatWheel.setVisibleItems(5);
        editNumberFormatCustom.editButton = (LinearLayout) inflate.findViewById(R.id.edit_function_wrapper);
        editNumberFormatCustom.editIndicator = (SOTextView) inflate.findViewById(R.id.edit_function_indicator);
        ((LinearLayout) inflate.findViewById(R.id.create_new_wrapper)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditNumberFormatCustom editNumberFormatCustom = EditNumberFormatCustom.this;
                Context context = context;
                EditNumberFormatCustom.access$1000(editNumberFormatCustom, context, context.getString(R.string.sodk_editor_create_new), "", new TextListener() {
                    public void setText(String str) {
                        if (str != null && !str.isEmpty()) {
                            int access$000 = EditNumberFormatCustom.access$000(EditNumberFormatCustom.this, str);
                            if (access$000 >= 0) {
                                EditNumberFormatCustom.this.formatWheel.setCurrentItem(access$000);
                                AnonymousClass1 r5 = AnonymousClass1.this;
                                EditNumberFormatCustom editNumberFormatCustom = EditNumberFormatCustom.this;
                                EditNumberFormatCustom.access$200(editNumberFormatCustom, arDkDoc, editNumberFormatCustom.formatWheel);
                                return;
                            }
                            EditNumberFormatCustom.descriptions = EditNumberFormatCustom.this.appendToArray(EditNumberFormatCustom.descriptions, str);
                            EditNumberFormatCustom.formats = EditNumberFormatCustom.this.appendToArray(EditNumberFormatCustom.formats, str);
                            EditNumberFormatCustom editNumberFormatCustom2 = EditNumberFormatCustom.this;
                            editNumberFormatCustom2.wheelAdapter.items = EditNumberFormatCustom.descriptions;
                            editNumberFormatCustom2.formatWheel.invalidateWheel(true);
                            EditNumberFormatCustom.this.formatWheel.setCurrentItem(EditNumberFormatCustom.formats.length - 1);
                            AnonymousClass1 r52 = AnonymousClass1.this;
                            EditNumberFormatCustom editNumberFormatCustom3 = EditNumberFormatCustom.this;
                            editNumberFormatCustom3.enableEditButton(context, true, editNumberFormatCustom3.editButton, editNumberFormatCustom3.editIndicator);
                            AnonymousClass1 r53 = AnonymousClass1.this;
                            EditNumberFormatCustom editNumberFormatCustom4 = EditNumberFormatCustom.this;
                            EditNumberFormatCustom.access$200(editNumberFormatCustom4, arDkDoc, editNumberFormatCustom4.formatWheel);
                        }
                    }
                });
            }
        });
        editNumberFormatCustom.editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final int currentItem = EditNumberFormatCustom.this.formatWheel.getCurrentItem();
                if (currentItem >= EditNumberFormatCustom.fixed_descriptions.length) {
                    String str = EditNumberFormatCustom.descriptions[currentItem];
                    EditNumberFormatCustom editNumberFormatCustom = EditNumberFormatCustom.this;
                    Context context = context;
                    EditNumberFormatCustom.access$1000(editNumberFormatCustom, context, context.getString(R.string.sodk_editor_edit_function), str, new TextListener() {
                        public void setText(String str) {
                            if (str != null && !str.isEmpty()) {
                                int access$000 = EditNumberFormatCustom.access$000(EditNumberFormatCustom.this, str);
                                if (access$000 >= 0) {
                                    EditNumberFormatCustom.this.formatWheel.setCurrentItem(access$000);
                                    AnonymousClass2 r4 = AnonymousClass2.this;
                                    EditNumberFormatCustom editNumberFormatCustom = EditNumberFormatCustom.this;
                                    EditNumberFormatCustom.access$200(editNumberFormatCustom, arDkDoc, editNumberFormatCustom.formatWheel);
                                    return;
                                }
                                T[] tArr = EditNumberFormatCustom.descriptions;
                                int i = currentItem;
                                tArr[i] = str;
                                EditNumberFormatCustom.formats[i] = str;
                                EditNumberFormatCustom editNumberFormatCustom2 = EditNumberFormatCustom.this;
                                editNumberFormatCustom2.wheelAdapter.items = tArr;
                                editNumberFormatCustom2.formatWheel.invalidateWheel(true);
                                EditNumberFormatCustom.this.formatWheel.setCurrentItem(currentItem);
                                AnonymousClass2 r42 = AnonymousClass2.this;
                                EditNumberFormatCustom editNumberFormatCustom3 = EditNumberFormatCustom.this;
                                EditNumberFormatCustom.access$200(editNumberFormatCustom3, arDkDoc, editNumberFormatCustom3.formatWheel);
                            }
                        }
                    });
                }
            }
        });
        editNumberFormatCustom.enableEditButton(context, false, editNumberFormatCustom.editButton, editNumberFormatCustom.editIndicator);
        WheelView wheelView = editNumberFormatCustom.formatWheel;
        String selectedCellFormat = ((SODoc) arDkDoc).getSelectedCellFormat();
        while (true) {
            String[] strArr2 = formats;
            if (i >= strArr2.length) {
                descriptions = editNumberFormatCustom.appendToArray(descriptions, selectedCellFormat);
                formats = editNumberFormatCustom.appendToArray(formats, selectedCellFormat);
                editNumberFormatCustom.wheelAdapter.items = descriptions;
                editNumberFormatCustom.formatWheel.invalidateWheel(true);
                editNumberFormatCustom.formatWheel.setCurrentItem(formats.length - 1);
                editNumberFormatCustom.enableEditButton(context, true, editNumberFormatCustom.editButton, editNumberFormatCustom.editIndicator);
                ArDkDoc arDkDoc2 = editNumberFormatCustom.theDocument;
                SODoc sODoc = (SODoc) arDkDoc2;
                sODoc.setSelectedCellFormat(formats[editNumberFormatCustom.formatWheel.getCurrentItem()]);
                break;
            } else if (selectedCellFormat.equals(strArr2[i])) {
                wheelView.setCurrentItem(i);
                if (i + 1 > fixed_formats.length) {
                    editNumberFormatCustom.enableEditButton(context, true, editNumberFormatCustom.editButton, editNumberFormatCustom.editIndicator);
                }
            } else {
                i++;
            }
        }
        WheelView wheelView2 = editNumberFormatCustom.formatWheel;
        wheelView2.scrollingListeners.add(new OnWheelScrollListener() {
            public void onScrollingFinished(WheelView wheelView) {
                EditNumberFormatCustom editNumberFormatCustom = EditNumberFormatCustom.this;
                Context context = context;
                boolean z = editNumberFormatCustom.formatWheel.getCurrentItem() >= EditNumberFormatCustom.fixed_descriptions.length;
                EditNumberFormatCustom editNumberFormatCustom2 = EditNumberFormatCustom.this;
                editNumberFormatCustom.enableEditButton(context, z, editNumberFormatCustom2.editButton, editNumberFormatCustom2.editIndicator);
                EditNumberFormatCustom editNumberFormatCustom3 = EditNumberFormatCustom.this;
                EditNumberFormatCustom.access$200(editNumberFormatCustom3, arDkDoc, editNumberFormatCustom3.formatWheel);
            }

            public void onScrollingStarted(WheelView wheelView) {
            }
        });
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        nUIPopupWindow.setFocusable(true);
        nUIPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                EditNumberFormatCustom.this.formatWheel.scrollingListeners.clear();
            }
        });
        nUIPopupWindow.showAsDropDown(view, 30, 30);
    }

    public final String[] appendToArray(String[] strArr, String str) {
        String[] strArr2 = new String[(strArr.length + 1)];
        System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        strArr2[strArr.length] = str;
        return strArr2;
    }

    public final void enableEditButton(Context context, boolean z, LinearLayout linearLayout, SOTextView sOTextView) {
        linearLayout.setEnabled(z);
        if (z) {
            sOTextView.setTextColor(ContextCompat.getColor(context, R.color.sodk_editor_xls_custom_num_edit_enabled_color));
        } else {
            sOTextView.setTextColor(ContextCompat.getColor(context, R.color.sodk_editor_xls_custom_num_edit_disabled_color));
        }
    }
}
