package com.artifex.sonui.editor;

import a.a.a.a.b.f.a$$ExternalSyntheticOutline0;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.SODoc;
import com.artifex.solib.SOSelectionTableRange;
import com.artifex.sonui.editor.NUIDocView;
import com.artifex.sonui.editor.NUIView;
import com.google.android.ads.mediationtestsuite.dataobjects.TestResult;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class NUIDocViewXls extends NUIDocView {
    public static final int CELL_TAG = R.string.sodk_editor_cellbox_last_value;
    public boolean addingPage = false;
    public String currentFormulaCategory = null;
    public ListPopupWindow currentFormulaPopup = null;
    public boolean deletingPage = false;
    public int draggingIndex = -1;
    public ToolbarButton mAlignOptionsButton;
    public SOEditText mCellHeightBox;
    public SOTextView mCellHeightLabel;
    public SOEditText mCellWidthBox;
    public SOTextView mCellWidthLabel;
    public int mCurrentSheet = -1;
    public ToolbarButton mDecreaseCellHeightButton;
    public ToolbarButton mDecreaseCellWidthButton;
    public ToolbarButton mDeleteColumnButton;
    public ToolbarButton mDeleteRowButton;
    public int mDropIndex = -1;
    public SOTextView mFXButton;
    public ToolbarButton mFormulaDateTimeButton;
    public ToolbarButton mFormulaEngineeringButton;
    public ToolbarButton mFormulaFinancialButton;
    public ToolbarButton mFormulaInformationButton;
    public ToolbarButton mFormulaLogicalButton;
    public ToolbarButton mFormulaLookupButton;
    public ToolbarButton mFormulaMathsButton;
    public ToolbarButton mFormulaStatisticalButton;
    public ToolbarButton mFormulaSumButton;
    public ToolbarButton mFormulaTextButton;
    public ToolbarButton mFreezeCellButton;
    public ToolbarButton mFreezeEnableButton;
    public ToolbarButton mFreezeFirstColButton;
    public ToolbarButton mFreezeFirstRowButton;
    public ToolbarButton mIncreaseCellHeightButton;
    public ToolbarButton mIncreaseCellWidthButton;
    public ToolbarButton mInsertColumnLeftButton;
    public ToolbarButton mInsertColumnRightButton;
    public ToolbarButton mInsertRowAboveButton;
    public ToolbarButton mInsertRowBelowButton;
    public ToolbarButton mMergeCellsButton;
    public NUIEditToolbar mNuiEditToolbar = new NUIEditToolbar();
    public ToolbarButton mNumberFormatButton;
    public String[] mSheetNames = null;
    public boolean once = false;
    public boolean undoredo = false;

    public NUIDocViewXls(Context context) {
        super(context);
    }

    public static void access$100(NUIDocViewXls nUIDocViewXls) {
        Utilities.hideKeyboard(nUIDocViewXls.activity());
        nUIDocViewXls.mCellWidthBox.clearFocus();
        nUIDocViewXls.mCellHeightBox.clearFocus();
    }

    public static void access$200(NUIDocViewXls nUIDocViewXls, float f) {
        float min = Math.min(Math.max((((float) Math.round((((SODoc) nUIDocViewXls.getDoc()).getSelectedColumnWidth() * 2.54f) * 2.0f)) / 2.0f) + f, 0.15f), 30.0f);
        ((SODoc) nUIDocViewXls.getDoc()).setSelectedColumnWidth(min / 2.54f);
        nUIDocViewXls.setCellBox(nUIDocViewXls.mCellWidthBox, min);
    }

    public static void access$400(NUIDocViewXls nUIDocViewXls, float f) {
        float min = Math.min(Math.max((((float) Math.round((((SODoc) nUIDocViewXls.getDoc()).getSelectedRowHeight() * 2.54f) * 2.0f)) / 2.0f) + f, 0.15f), 30.0f);
        ((SODoc) nUIDocViewXls.getDoc()).setSelectedRowHeight(min / 2.54f);
        nUIDocViewXls.setCellBox(nUIDocViewXls.mCellHeightBox, min);
    }

    public static void access$500(NUIDocViewXls nUIDocViewXls, int i) {
        nUIDocViewXls.setCurrentSheet(i);
    }

    private int getCurrentSheet() {
        DocExcelView docExcelView = (DocExcelView) getDocView();
        if (docExcelView != null) {
            return docExcelView.getCurrentSheet();
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public void setColumnWidthFromField(SOEditText sOEditText) {
        String obj = sOEditText.getText().toString();
        int i = CELL_TAG;
        String str = (String) sOEditText.getTag(i);
        if (obj != null && str != null && !obj.equals(str)) {
            float makeCellDimension = makeCellDimension(obj);
            if (makeCellDimension == BitmapDescriptorFactory.HUE_RED) {
                sOEditText.setText((String) sOEditText.getTag(i));
                return;
            }
            sOEditText.setTag(i, obj);
            ((SODoc) getDoc()).setSelectedColumnWidth(makeCellDimension);
        }
    }

    private void setCurrentSheet(int i) {
        if (i != getCurrentSheet() && !this.undoredo) {
            DocExcelView docExcelView = (DocExcelView) getDocView();
            docExcelView.copyEditTextToCell();
            getDoc().clearSelection();
            docExcelView.setEditText("");
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.excel_sheets_bar);
        int numPages = getDoc().getNumPages();
        int i2 = 0;
        while (i2 < numPages) {
            View childAt = linearLayout.getChildAt(i2);
            if (childAt != null) {
                childAt.setSelected(i2 == i);
                if (numPages == 1) {
                    ((SheetTab) childAt).showXView(false);
                }
            }
            i2++;
        }
        this.mCurrentPageNum = i;
        ((DocExcelView) getDocView()).setCurrentSheet(i);
        onSelectionChanged();
        setPageNumberText();
    }

    /* access modifiers changed from: private */
    public void setRowHeightFromField(SOEditText sOEditText) {
        String obj = sOEditText.getText().toString();
        int i = CELL_TAG;
        String str = (String) sOEditText.getTag(i);
        if (obj != null && str != null && !obj.equals(str)) {
            float makeCellDimension = makeCellDimension(obj);
            if (makeCellDimension == BitmapDescriptorFactory.HUE_RED) {
                sOEditText.setText((String) sOEditText.getTag(i));
                return;
            }
            sOEditText.setTag(i, obj);
            ((SODoc) getDoc()).setSelectedRowHeight(makeCellDimension);
        }
    }

    public void afterCut() {
        ((DocExcelView) getDocView()).setEditText("");
    }

    public void afterFirstLayoutComplete() {
        super.afterFirstLayoutComplete();
        this.mNuiEditToolbar.create(this);
        this.mInsertRowAboveButton = (ToolbarButton) createToolbarButton(R.id.insert_row_above_button);
        this.mInsertRowBelowButton = (ToolbarButton) createToolbarButton(R.id.insert_row_below_button);
        this.mInsertColumnLeftButton = (ToolbarButton) createToolbarButton(R.id.insert_column_left_button);
        this.mInsertColumnRightButton = (ToolbarButton) createToolbarButton(R.id.insert_column_right_button);
        this.mDeleteRowButton = (ToolbarButton) createToolbarButton(R.id.delete_row_button);
        this.mDeleteColumnButton = (ToolbarButton) createToolbarButton(R.id.delete_column_button);
        this.mFreezeCellButton = (ToolbarButton) createToolbarButton(R.id.freeze_cell);
        this.mFreezeFirstRowButton = (ToolbarButton) createToolbarButton(R.id.freeze_first_row);
        this.mFreezeFirstColButton = (ToolbarButton) createToolbarButton(R.id.freeze_first_col);
        this.mFreezeEnableButton = (ToolbarButton) createToolbarButton(R.id.freeze_show);
        this.mMergeCellsButton = (ToolbarButton) createToolbarButton(R.id.merge_cells_button);
        this.mNumberFormatButton = (ToolbarButton) createToolbarButton(R.id.number_format_button);
        this.mIncreaseCellWidthButton = (ToolbarButton) createToolbarButton(R.id.cell_width_up_button);
        this.mDecreaseCellWidthButton = (ToolbarButton) createToolbarButton(R.id.cell_width_down_button);
        this.mIncreaseCellHeightButton = (ToolbarButton) createToolbarButton(R.id.cell_height_up_button);
        this.mDecreaseCellHeightButton = (ToolbarButton) createToolbarButton(R.id.cell_height_down_button);
        this.mCellWidthLabel = (SOTextView) findViewById(R.id.cell_width_label);
        this.mCellHeightLabel = (SOTextView) findViewById(R.id.cell_height_label);
        ToolbarButton.setAllSameSize(new ToolbarButton[]{this.mMergeCellsButton, this.mNumberFormatButton});
        this.mFormulaSumButton = (ToolbarButton) createToolbarButton(R.id.formula_sum);
        this.mFormulaDateTimeButton = (ToolbarButton) createToolbarButton(R.id.formula_datetime);
        this.mFormulaEngineeringButton = (ToolbarButton) createToolbarButton(R.id.formula_engineering);
        this.mFormulaFinancialButton = (ToolbarButton) createToolbarButton(R.id.formula_financial);
        this.mFormulaInformationButton = (ToolbarButton) createToolbarButton(R.id.formula_information);
        this.mFormulaLogicalButton = (ToolbarButton) createToolbarButton(R.id.formula_logical);
        this.mFormulaLookupButton = (ToolbarButton) createToolbarButton(R.id.formula_lookup);
        this.mFormulaMathsButton = (ToolbarButton) createToolbarButton(R.id.formula_maths);
        this.mFormulaStatisticalButton = (ToolbarButton) createToolbarButton(R.id.formula_statistical);
        ToolbarButton toolbarButton = (ToolbarButton) createToolbarButton(R.id.formula_text);
        this.mFormulaTextButton = toolbarButton;
        ToolbarButton.setAllSameSize(new ToolbarButton[]{this.mFormulaSumButton, this.mFormulaDateTimeButton, this.mFormulaEngineeringButton, this.mFormulaFinancialButton, this.mFormulaInformationButton, this.mFormulaLogicalButton, this.mFormulaLookupButton, this.mFormulaMathsButton, this.mFormulaStatisticalButton, toolbarButton});
        this.mFXButton = (SOTextView) createToolbarButton(R.id.fx_button);
        SOEditText sOEditText = (SOEditText) findViewById(R.id.cell_width_box);
        this.mCellWidthBox = sOEditText;
        Context context = getContext();
        int i = R.string.sodk_editor_ime_action_label_done;
        sOEditText.setImeActionLabel(context.getString(i), 66);
        this.mCellWidthBox.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!NUIDocViewXls.this.mFinished) {
                    SOEditText sOEditText = (SOEditText) view.getParent();
                    sOEditText.setTag(NUIDocViewXls.CELL_TAG, sOEditText.getText().toString());
                    sOEditText.selectAll();
                }
            }
        });
        this.mCellWidthBox.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                SOEditText sOEditText = (SOEditText) view.getParent();
                if (z) {
                    sOEditText.callOnClick();
                } else {
                    NUIDocViewXls.this.setColumnWidthFromField(sOEditText);
                }
            }
        });
        this.mCellWidthBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                NUIDocViewXls.this.setColumnWidthFromField((SOEditText) textView.getParent());
                NUIDocViewXls.access$100(NUIDocViewXls.this);
                return true;
            }
        });
        this.mIncreaseCellWidthButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                if (!nUIDocViewXls.mFinished) {
                    NUIDocViewXls.access$100(nUIDocViewXls);
                    NUIDocViewXls.access$200(NUIDocViewXls.this, 0.5f);
                }
            }
        });
        this.mDecreaseCellWidthButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                if (!nUIDocViewXls.mFinished) {
                    NUIDocViewXls.access$100(nUIDocViewXls);
                    NUIDocViewXls.access$200(NUIDocViewXls.this, -0.5f);
                }
            }
        });
        SOEditText sOEditText2 = (SOEditText) findViewById(R.id.cell_height_box);
        this.mCellHeightBox = sOEditText2;
        sOEditText2.setImeActionLabel(getContext().getString(i), 66);
        this.mCellHeightBox.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!NUIDocViewXls.this.mFinished) {
                    SOEditText sOEditText = (SOEditText) view.getParent();
                    sOEditText.setTag(NUIDocViewXls.CELL_TAG, sOEditText.getText().toString());
                    sOEditText.selectAll();
                }
            }
        });
        this.mCellHeightBox.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                SOEditText sOEditText = (SOEditText) view.getParent();
                if (z) {
                    sOEditText.callOnClick();
                } else {
                    NUIDocViewXls.this.setRowHeightFromField(sOEditText);
                }
            }
        });
        this.mCellHeightBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                NUIDocViewXls.this.setRowHeightFromField((SOEditText) textView.getParent());
                NUIDocViewXls.access$100(NUIDocViewXls.this);
                return true;
            }
        });
        this.mIncreaseCellHeightButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                if (!nUIDocViewXls.mFinished) {
                    NUIDocViewXls.access$100(nUIDocViewXls);
                    NUIDocViewXls.access$400(NUIDocViewXls.this, 0.5f);
                }
            }
        });
        this.mDecreaseCellHeightButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                if (!nUIDocViewXls.mFinished) {
                    NUIDocViewXls.access$100(nUIDocViewXls);
                    NUIDocViewXls.access$400(NUIDocViewXls.this, -0.5f);
                }
            }
        });
    }

    public void afterPaste() {
        ((DocExcelView) getDocView()).setEditText(((SODoc) getDoc()).getSelectionAsText());
    }

    public final int calculateCurrentSheet() {
        createSheetNamesArray();
        getCurrentSheet();
        String[] strArr = this.mSheetNames;
        int i = this.mCurrentSheet;
        this.mSheetNames = null;
        this.mCurrentSheet = -1;
        if (strArr == null || i == -1) {
            return Math.min(getPageCount(), getCurrentSheet());
        }
        String[] createSheetNamesArray = createSheetNamesArray();
        int i2 = 0;
        if (strArr.length > createSheetNamesArray.length) {
            for (int i3 = 0; i3 < createSheetNamesArray.length; i3++) {
                if (!strArr[i3].equals(createSheetNamesArray[i3])) {
                    return Math.max(0, i3 - 1);
                }
            }
            return createSheetNamesArray.length - 1;
        } else if (strArr.length < createSheetNamesArray.length) {
            while (i2 < strArr.length) {
                if (!strArr[i2].equals(createSheetNamesArray[i2])) {
                    return i2;
                }
                i2++;
            }
            return createSheetNamesArray.length - 1;
        } else {
            while (i2 < strArr.length) {
                if (!strArr[i2].equals(createSheetNamesArray[i2]) && strArr[i].equals(createSheetNamesArray[i2])) {
                    return i2;
                }
                i2++;
            }
            int i4 = getDoc().mSelectionStartPage;
            return (i4 < 0 || i4 >= getPageCount()) ? i : i4;
        }
    }

    public void clickSheetButton(int i, boolean z) {
        SheetTab sheetTab;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.excel_sheets_bar);
        if (!(linearLayout == null || (sheetTab = (SheetTab) linearLayout.getChildAt(i)) == null)) {
            sheetTab.performClick();
        }
        if (!z) {
            setSearchStart();
        }
    }

    public final void copyFromCellToField() {
        SOSelectionTableRange selectionTableRange = ((SODoc) getDoc()).selectionTableRange();
        if (selectionTableRange != null && selectionTableRange.rowCount() == 1 && selectionTableRange.columnCount() == 1) {
            ((DocExcelView) getDocView()).setEditText(((SODoc) getDoc()).getSelectionAsText());
        }
    }

    public void createEditButtons2() {
        this.mAlignOptionsButton = (ToolbarButton) createToolbarButton(R.id.align_options_button);
    }

    public void createInputView() {
    }

    public void createInsertButtons() {
    }

    public DocView createMainView(Activity activity) {
        DocExcelView docExcelView = new DocExcelView(activity);
        if (Utilities.isPhoneDevice(activity)) {
            docExcelView.setScale(1.5f);
        }
        return docExcelView;
    }

    public void createPagesButtons() {
    }

    public void createReviewButtons() {
    }

    public final String[] createSheetNamesArray() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.excel_sheets_bar);
        int numPages = getDoc().getNumPages();
        String[] strArr = new String[numPages];
        for (int i = 0; i < numPages; i++) {
            SheetTab sheetTab = (SheetTab) linearLayout.getChildAt(i);
            if (sheetTab != null) {
                strArr[i] = sheetTab.getText();
            }
        }
        return strArr;
    }

    public void doBold() {
        this.mNuiEditToolbar.doBold();
    }

    public void doCopy() {
        this.mNuiEditToolbar.doCopy();
    }

    public void doCut() {
        this.mNuiEditToolbar.doCut();
    }

    public void doItalic() {
        this.mNuiEditToolbar.doItalic();
    }

    public void doPaste() {
        this.mNuiEditToolbar.doPaste();
    }

    public void doRedo() {
        if (!this.undoredo) {
            this.mSheetNames = createSheetNamesArray();
            this.mCurrentSheet = getCurrentSheet();
            super.doRedo();
            copyFromCellToField();
            this.undoredo = true;
        }
    }

    public void doSave() {
        preSave();
        super.doSave();
    }

    public void doSaveAs(boolean z, SOSaveAsComplete sOSaveAsComplete) {
        preSave();
        super.doSaveAs(z, sOSaveAsComplete);
    }

    public void doUnderline() {
        this.mNuiEditToolbar.doUnderline();
    }

    public void doUndo() {
        if (!this.undoredo) {
            this.mSheetNames = createSheetNamesArray();
            this.mCurrentSheet = getCurrentSheet();
            super.doUndo();
            copyFromCellToField();
            this.undoredo = true;
        }
    }

    public void focusInputView() {
    }

    public int getBorderColor() {
        return ContextCompat.getColor(getContext(), R.color.sodk_editor_header_xls_color);
    }

    public int getLayoutId() {
        return R.layout.sodk_editor_excel_document;
    }

    public String getPageNumberText() {
        return String.format(getResources().getConfiguration().locale, getContext().getString(R.string.sodk_editor_sheet_d_of_d), new Object[]{Integer.valueOf(getCurrentSheet() + 1), Integer.valueOf(getPageCount())});
    }

    public TabData[] getTabData() {
        if (this.mTabs == null) {
            this.mTabs = new TabData[5];
            if (this.mDocCfgOptions.isEditingEnabled()) {
                this.mTabs[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_left, 0);
                TabData[] tabDataArr = this.mTabs;
                String string = getContext().getString(R.string.sodk_editor_tab_edit);
                int i = R.id.editTab;
                int i2 = R.layout.sodk_editor_tab;
                tabDataArr[1] = new TabData(this, string, i, i2, 0);
                this.mTabs[2] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_insert), R.id.insertTab, i2, 0);
                this.mTabs[3] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_format), R.id.formatTab, i2, 0);
                this.mTabs[4] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_formulas), R.id.formulasTab, R.layout.sodk_editor_tab_right, 0);
            } else {
                this.mTabs[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_one, 0);
                TabData[] tabDataArr2 = this.mTabs;
                String string2 = getContext().getString(R.string.sodk_editor_tab_edit);
                int i3 = R.id.editTab;
                int i4 = R.layout.sodk_editor_tab;
                tabDataArr2[1] = new TabData(this, string2, i3, i4, 8);
                this.mTabs[2] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_insert), R.id.insertTab, i4, 8);
                this.mTabs[3] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_format), R.id.formatTab, i4, 8);
                this.mTabs[4] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_formulas), R.id.formulasTab, R.layout.sodk_editor_tab_right, 8);
            }
        }
        return this.mTabs;
    }

    public int getTabSelectedColor() {
        if (getResources().getInteger(R.integer.sodk_editor_ui_doc_tab_color_from_doctype) == 0) {
            return ContextCompat.getColor(activity(), R.color.sodk_editor_header_color_selected);
        }
        return ContextCompat.getColor(activity(), R.color.sodk_editor_header_xls_color);
    }

    public int getTabUnselectedColor() {
        if (getResources().getInteger(R.integer.sodk_editor_ui_doc_tabbar_color_from_doctype) == 0) {
            return ContextCompat.getColor(activity(), R.color.sodk_editor_header_color);
        }
        return ContextCompat.getColor(activity(), R.color.sodk_editor_header_xls_color);
    }

    public void handleStartPage() {
    }

    public boolean hasSelectionAlignment() {
        return true;
    }

    public boolean isFreezeShown() {
        return ((DocExcelView) getDocView()).isFreezeShown();
    }

    public boolean isFrozen() {
        return ((DocExcelView) getDocView()).isFrozen();
    }

    public final float makeCellDimension(String str) {
        if (str.isEmpty()) {
            return BitmapDescriptorFactory.HUE_RED;
        }
        try {
            float parseFloat = Float.parseFloat(str);
            return (parseFloat < 0.15f || parseFloat > 30.0f) ? BitmapDescriptorFactory.HUE_RED : parseFloat / 2.54f;
        } catch (NumberFormatException unused) {
            return BitmapDescriptorFactory.HUE_RED;
        }
    }

    public void onAlignOptionsButton(View view) {
        new AlignmentDialog(getContext(), getDoc(), view).show();
    }

    public void onClick(View view) {
        if (!this.mFinished) {
            super.onClick(view);
            if (view == this.mAlignOptionsButton) {
                onAlignOptionsButton(view);
            }
            if (view == this.mInsertRowAboveButton) {
                onInsertRowAbove(view);
            }
            if (view == this.mInsertRowBelowButton) {
                onInsertRowBelow(view);
            }
            if (view == this.mInsertColumnLeftButton) {
                onInsertColumnLeft(view);
            }
            if (view == this.mInsertColumnRightButton) {
                onInsertColumnRight(view);
            }
            if (view == this.mDeleteRowButton) {
                onDeleteRow(view);
            }
            if (view == this.mDeleteColumnButton) {
                onDeleteColumn(view);
            }
            if (view == this.mMergeCellsButton) {
                onMergeCellsButton(view);
            }
            if (view == this.mNumberFormatButton) {
                onNumberFormatButton(view);
            }
            if (view == this.mFreezeCellButton) {
                onFreezeCell(view);
            }
            if (view == this.mFreezeFirstRowButton) {
                onFreezeFirstRow(view);
            }
            if (view == this.mFreezeFirstColButton) {
                onFreezeFirstCol(view);
            }
            if (view == this.mFreezeEnableButton) {
                onFreezeEnable(view);
            }
            if (view == this.mFormulaSumButton) {
                onFormulaSumButton(view);
            }
            if (view == this.mFormulaDateTimeButton) {
                onFormulaDateTimeButton(view);
            }
            if (view == this.mFormulaEngineeringButton) {
                onFormulaEngineeringButton(view);
            }
            if (view == this.mFormulaFinancialButton) {
                onFormulaFinancialButton(view);
            }
            if (view == this.mFormulaInformationButton) {
                onFormulaInformationButton(view);
            }
            if (view == this.mFormulaLogicalButton) {
                onFormulaLogicalButton(view);
            }
            if (view == this.mFormulaLookupButton) {
                onFormulaLookupButton(view);
            }
            if (view == this.mFormulaMathsButton) {
                onFormulaMathsButton(view);
            }
            if (view == this.mFormulaStatisticalButton) {
                onFormulaStatisticalButton(view);
            }
            if (view == this.mFormulaTextButton) {
                onFormulaTextButton(view);
            }
            if (view == this.mFXButton) {
                onClickFunctionButton(view);
            }
        }
    }

    public void onClickFunctionButton(View view) {
        Utilities.hideKeyboard(getContext());
        View inflate = FrameLayout.inflate(getContext(), R.layout.sodk_editor_formula_categories, (ViewGroup) null);
        GridView gridView = (GridView) inflate.findViewById(R.id.grid);
        final ChooseFormulaCategoryAdapter chooseFormulaCategoryAdapter = new ChooseFormulaCategoryAdapter(activity());
        gridView.setAdapter(chooseFormulaCategoryAdapter);
        final NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        nUIPopupWindow.setFocusable(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(this) {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                nUIPopupWindow.dismiss();
                String item = chooseFormulaCategoryAdapter.getItem(i);
                NUIDocViewXls nUIDocViewXls = this;
                int i2 = NUIDocViewXls.CELL_TAG;
                nUIDocViewXls.onFormulaCategory(item);
            }
        });
        nUIPopupWindow.showAsDropDown(view, 30, 30);
    }

    public void onDeleteColumn(View view) {
        ((DocExcelView) getDocView()).deleteSelectedColumns();
    }

    public void onDeleteRow(View view) {
        ((DocExcelView) getDocView()).deleteSelectedRows();
    }

    public void onDocCompleted() {
        super.onDocCompleted();
        setPageCount(getPageCount());
        refreshSheetButtons();
        if (this.addingPage) {
            setCurrentSheet(getDoc().getNumPages() - 1);
        } else if (!this.deletingPage) {
            setCurrentSheet(calculateCurrentSheet());
        } else if (getCurrentSheet() == 0) {
            setCurrentSheet(0);
        } else {
            setCurrentSheet(getCurrentSheet() - 1);
        }
        this.addingPage = false;
        this.deletingPage = false;
        if (!this.once) {
            if (this.mViewingState != null) {
                ((DocExcelView) getDocView()).handleViewingState();
                if (this.mViewingState.pageNumber >= getDoc().getNumPages()) {
                    this.mViewingState.pageNumber = 0;
                }
                this.mCurrentPageNum = this.mViewingState.pageNumber;
                onSelectionChanged();
                setPageNumberText();
                refreshSheetButtons();
            }
            this.once = true;
        }
        NUIView.DocStateListener docStateListener = this.mDocStateListener;
        if (docStateListener != null) {
            docStateListener.docLoaded();
        }
    }

    public final void onFormulaCategory(final String str) {
        ListPopupWindow listPopupWindow = this.currentFormulaPopup;
        if (listPopupWindow != null) {
            listPopupWindow.dismiss();
        }
        if (getKeyboardHeight() > 0) {
            Utilities.hideKeyboard(getContext());
            final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                    String str = str;
                    int i = NUIDocViewXls.CELL_TAG;
                    nUIDocViewXls.onFormulaCategory2(str);
                }
            });
            return;
        }
        onFormulaCategory2(str);
    }

    public final void onFormulaCategory2(String str) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity());
        listPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity(), R.drawable.sodk_editor_formula_popup));
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(this.mFXButton);
        listPopupWindow.setHorizontalOffset(30);
        listPopupWindow.setVerticalOffset(30);
        final ChooseFormulaAdapter chooseFormulaAdapter = new ChooseFormulaAdapter(activity(), str);
        listPopupWindow.setAdapter(chooseFormulaAdapter);
        Point screenSize = Utilities.getScreenSize(getContext());
        listPopupWindow.setContentWidth(screenSize.x / 2);
        int[] iArr = new int[2];
        this.mFXButton.getLocationInWindow(iArr);
        int height = ((screenSize.y - Utilities.screenToWindow(iArr, getContext())[1]) - this.mFXButton.getHeight()) - 60;
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(0, 0);
        int count = chooseFormulaAdapter.getCount();
        FrameLayout frameLayout = null;
        View view = null;
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < count; i3++) {
            int itemViewType = chooseFormulaAdapter.getItemViewType(i3);
            if (itemViewType != i2) {
                view = null;
                i2 = itemViewType;
            }
            if (frameLayout == null) {
                frameLayout = new FrameLayout(getContext());
            }
            view = chooseFormulaAdapter.getView(i3, view, frameLayout);
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            i += view.getMeasuredHeight();
        }
        listPopupWindow.setHeight(Math.min(height, i));
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                listPopupWindow.dismiss();
                DocExcelView docExcelView = (DocExcelView) NUIDocViewXls.this.getDocView();
                String editText = docExcelView.getEditText();
                String item = chooseFormulaAdapter.getItem(i);
                if (editText == null || editText.isEmpty()) {
                    item = a$$ExternalSyntheticOutline0.m("=", item);
                }
                docExcelView.insertEditText(item);
                docExcelView.copyEditTextToCell();
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                nUIDocViewXls.currentFormulaPopup = null;
                nUIDocViewXls.currentFormulaCategory = null;
            }
        });
        listPopupWindow.show();
        this.currentFormulaPopup = listPopupWindow;
        this.currentFormulaCategory = str;
        listPopupWindow.getListView().setDivider((Drawable) null);
        listPopupWindow.getListView().setVerticalScrollBarEnabled(true);
    }

    public void onFormulaDateTimeButton(View view) {
        onFormulaCategory("Date and Time");
    }

    public void onFormulaEngineeringButton(View view) {
        onFormulaCategory("Engineering");
    }

    public void onFormulaFinancialButton(View view) {
        onFormulaCategory("Financial");
    }

    public void onFormulaInformationButton(View view) {
        onFormulaCategory("Information");
    }

    public void onFormulaLogicalButton(View view) {
        onFormulaCategory("Logical");
    }

    public void onFormulaLookupButton(View view) {
        onFormulaCategory("Lookup");
    }

    public void onFormulaMathsButton(View view) {
        onFormulaCategory("Maths");
    }

    public void onFormulaStatisticalButton(View view) {
        onFormulaCategory("Statistical");
    }

    public void onFormulaSumButton(View view) {
        DocExcelView docExcelView = (DocExcelView) getDocView();
        String string = getContext().getString(R.string.sodk_editor_autosum_text);
        String editText = docExcelView.getEditText();
        if (editText == null || editText.isEmpty()) {
            string = a$$ExternalSyntheticOutline0.m("=", string);
        }
        docExcelView.insertEditText(string);
        ((SODoc) getDoc()).setSelectionText(docExcelView.getEditText());
    }

    public void onFormulaTextButton(View view) {
        onFormulaCategory("Text");
    }

    public void onFreezeCell(View view) {
        ((DocExcelView) getDocView()).onFreezeCell();
    }

    public void onFreezeEnable(View view) {
        ((DocExcelView) getDocView()).toggleFreezeShown();
    }

    public void onFreezeFirstCol(View view) {
        ((DocExcelView) getDocView()).onFreezeFirstCol();
    }

    public void onFreezeFirstRow(View view) {
        ((DocExcelView) getDocView()).onFreezeFirstRow();
    }

    public void onFullScreenHide() {
        findViewById(R.id.fx_bar).setVisibility(8);
        super.onFullScreenHide();
    }

    public void onInsertColumnLeft(View view) {
        ((DocExcelView) getDocView()).addColumnsLeft();
    }

    public void onInsertColumnRight(View view) {
        ((DocExcelView) getDocView()).addColumnsRight();
    }

    public void onInsertRowAbove(View view) {
        ((DocExcelView) getDocView()).addRowsAbove();
    }

    public void onInsertRowBelow(View view) {
        ((DocExcelView) getDocView()).addRowsBelow();
    }

    public void onLayoutChanged() {
        super.onLayoutChanged();
        if (this.mDropIndex != -1) {
            refreshSheetButtons();
            setCurrentSheet(this.mDropIndex);
            this.mDropIndex = -1;
            setSearchStart();
        }
        if (this.undoredo) {
            refreshSheetButtons();
            setCurrentSheet(calculateCurrentSheet());
            this.undoredo = false;
        }
        updateUIAppearance();
    }

    public void onMergeCellsButton(View view) {
        boolean tableCellsMerged = ((SODoc) getDoc()).getTableCellsMerged();
        ((SODoc) getDoc()).setTableCellsMerged(!tableCellsMerged);
        if (!tableCellsMerged) {
            ((DocExcelView) getDocView()).setEditText(((SODoc) getDoc()).getSelectionAsText());
        }
    }

    public void onNumberFormatButton(View view) {
        Utilities.hideKeyboard(getContext());
        View inflate = FrameLayout.inflate(getContext(), R.layout.sodk_editor_number_formats, (ViewGroup) null);
        GridView gridView = (GridView) inflate.findViewById(R.id.grid);
        int i = (!Utilities.isPhoneDevice(getContext()) || Utilities.isLandscapePhone(getContext())) ? 2 : 1;
        gridView.setNumColumns(i);
        gridView.setAdapter(new ChooseNumberFormatAdapter(activity(), i));
        final NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        nUIPopupWindow.setFocusable(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                nUIPopupWindow.dismiss();
                Utilities.hideKeyboard(NUIDocViewXls.this.getContext());
                switch (i) {
                    case 0:
                        ((SODoc) NUIDocViewXls.this.getDoc()).setSelectedCellFormat("General");
                        return;
                    case 1:
                        Activity activity = NUIDocViewXls.this.activity();
                        NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                        EditNumberFormatDateTime.show(activity, nUIDocViewXls.mNumberFormatButton, nUIDocViewXls.getDoc());
                        return;
                    case 2:
                        Activity activity2 = NUIDocViewXls.this.activity();
                        NUIDocViewXls nUIDocViewXls2 = NUIDocViewXls.this;
                        EditNumberFormatNumber.show(activity2, nUIDocViewXls2.mNumberFormatButton, nUIDocViewXls2.getDoc());
                        return;
                    case 3:
                        Activity activity3 = NUIDocViewXls.this.activity();
                        NUIDocViewXls nUIDocViewXls3 = NUIDocViewXls.this;
                        EditNumberFormatFraction.show(activity3, nUIDocViewXls3.mNumberFormatButton, nUIDocViewXls3.getDoc());
                        return;
                    case 4:
                        Activity activity4 = NUIDocViewXls.this.activity();
                        NUIDocViewXls nUIDocViewXls4 = NUIDocViewXls.this;
                        EditNumberFormatCurrency.show(activity4, nUIDocViewXls4.mNumberFormatButton, nUIDocViewXls4.getDoc());
                        return;
                    case 5:
                        Activity activity5 = NUIDocViewXls.this.activity();
                        NUIDocViewXls nUIDocViewXls5 = NUIDocViewXls.this;
                        EditNumberFormatPercentage.show(activity5, nUIDocViewXls5.mNumberFormatButton, nUIDocViewXls5.getDoc());
                        return;
                    case 6:
                        Activity activity6 = NUIDocViewXls.this.activity();
                        NUIDocViewXls nUIDocViewXls6 = NUIDocViewXls.this;
                        EditNumberFormatAccounting.show(activity6, nUIDocViewXls6.mNumberFormatButton, nUIDocViewXls6.getDoc());
                        return;
                    case 7:
                        Activity activity7 = NUIDocViewXls.this.activity();
                        NUIDocViewXls nUIDocViewXls7 = NUIDocViewXls.this;
                        EditNumberFormatCustom.show(activity7, nUIDocViewXls7.mNumberFormatButton, nUIDocViewXls7.getDoc());
                        return;
                    default:
                        return;
                }
            }
        });
        nUIPopupWindow.showAsDropDown(view, 30, 30);
    }

    public void onOrientationChange() {
        getDocView().onOrientationChange();
        if (!isFullScreen()) {
            if (!Utilities.isLandscapePhone(getContext()) || !isKeyboardVisible()) {
                showUI(true);
            } else {
                showUI(false);
            }
        }
        onDeviceSizeChange();
        ListPopupWindow listPopupWindow = this.currentFormulaPopup;
        if (listPopupWindow != null) {
            String str = this.currentFormulaCategory;
            listPopupWindow.dismiss();
            onFormulaCategory(str);
        }
    }

    public void onPageLoaded(int i) {
        if (i == 1) {
            ((DocExcelView) getDocView()).clearChildViews();
        }
        refreshSheetButtons();
        super.onPageLoaded(i);
    }

    public void onSelectionChanged() {
        super.onSelectionChanged();
        setPageCount(getPageCount());
        SOSelectionTableRange selectionTableRange = ((SODoc) getDoc()).selectionTableRange();
        View findViewById = findViewById(R.id.formulas_toolbar);
        if (selectionTableRange != null && selectionTableRange.rowCount() == 1 && selectionTableRange.columnCount() == 1) {
            setViewAndChildrenEnabled(findViewById, true);
        } else {
            setViewAndChildrenEnabled(findViewById, false);
        }
    }

    public void onShowKeyboard(final boolean z) {
        if (isActivityActive() && getPageCount() > 0) {
            this.keyboardShown = z;
            onShowKeyboardPreventPush(z);
            DocView docView = getDocView();
            if (docView != null) {
                docView.onShowKeyboard(z);
            }
            if (isLandscapePhone()) {
                if (!isFullScreen()) {
                    showUI(!z);
                }
                if (z) {
                    requestLayout();
                    final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            viewTreeObserver.removeOnGlobalLayoutListener(this);
                            ((DocExcelView) NUIDocViewXls.this.getDocView()).scrollSelectedCellAboveKeyboard();
                        }
                    });
                }
            } else if (z && !isSearchFocused()) {
                ((DocExcelView) getDocView()).scrollSelectedCellAboveKeyboard();
            }
            ListPopupWindow listPopupWindow = this.currentFormulaPopup;
            if (listPopupWindow != null) {
                String str = this.currentFormulaCategory;
                listPopupWindow.dismiss();
                onFormulaCategory(str);
            }
            final ViewTreeObserver viewTreeObserver2 = getViewTreeObserver();
            viewTreeObserver2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    viewTreeObserver2.removeOnGlobalLayoutListener(this);
                    DocView docView = NUIDocViewXls.this.getDocView();
                    if (docView != null) {
                        docView.onShowKeyboard(z);
                    }
                    Runnable runnable = NUIDocViewXls.this.mShowHideKeyboardRunnable;
                    if (runnable != null) {
                        runnable.run();
                        NUIDocViewXls.this.mShowHideKeyboardRunnable = null;
                    }
                    NUIDocViewXls.this.layoutNow();
                }
            });
        }
    }

    public void preSave() {
        ((DocExcelView) getDocView()).copyEditTextToCell();
    }

    public void prepareToGoBack() {
        SODocSession session;
        SOFileState fileState;
        DocView docView = getDocView();
        if (docView != null && this.mCompleted && ((DocExcelView) docView).copyEditTextToCell() && (session = getSession()) != null && (fileState = session.getFileState()) != null) {
            fileState.setHasChanges(true);
        }
    }

    public final void refreshSheetButtons() {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.excel_sheets_bar);
        linearLayout.removeAllViews();
        SheetTab.setEditingEbabled(this.mDocCfgOptions.isEditingEnabled());
        int numPages = getDoc().getNumPages();
        final Activity activity = activity();
        for (int i = 1; i <= numPages; i++) {
            int i2 = i - 1;
            final String pageTitle = ((DocExcelView) getDocView()).getPageTitle(i2);
            SheetTab sheetTab = new SheetTab(activity);
            sheetTab.setText(pageTitle);
            sheetTab.setSheetNumber(i2);
            sheetTab.setOnClickTab(new OnClickListener() {
                public void onClick(View view) {
                    if (!NUIDocViewXls.this.mFinished) {
                        NUIDocViewXls.access$500(NUIDocViewXls.this, ((SheetTab) view).getSheetNumber());
                    }
                }
            });
            if (this.mDocCfgOptions.isEditingEnabled()) {
                sheetTab.setOnLongClickTab(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        int sheetNumber = ((SheetTab) view).getSheetNumber();
                        View childAt = linearLayout.getChildAt(sheetNumber);
                        NUIDocViewXls.this.draggingIndex = sheetNumber;
                        String str = pageTitle;
                        ClipData newPlainText = ClipData.newPlainText(str, str);
                        DragShadowBuilder dragShadowBuilder = new DragShadowBuilder(childAt);
                        if (Build.VERSION.SDK_INT >= 24) {
                            view.startDragAndDrop(newPlainText, dragShadowBuilder, (Object) null, 0);
                            return true;
                        }
                        view.startDrag(newPlainText, dragShadowBuilder, (Object) null, 0);
                        return true;
                    }
                });
                linearLayout.setOnDragListener(new OnDragListener() {
                    public int dropIndex = -1;

                    public boolean onDrag(View view, DragEvent dragEvent) {
                        int action = dragEvent.getAction();
                        int i = -1;
                        if (action == 2) {
                            int i2 = 0;
                            while (true) {
                                if (i2 >= linearLayout.getChildCount() - 1) {
                                    break;
                                }
                                SheetTab sheetTab = (SheetTab) linearLayout.getChildAt(i2);
                                if (dragEvent.getX() > ((float) sheetTab.getLeft()) && dragEvent.getX() < ((float) sheetTab.getRight())) {
                                    i = i2;
                                    break;
                                }
                                i2++;
                            }
                            setDropIndex(i);
                        } else if (action == 3) {
                            int i3 = this.dropIndex;
                            if (i3 != -1) {
                                NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                                if (i3 != nUIDocViewXls.draggingIndex) {
                                    nUIDocViewXls.mDropIndex = i3;
                                    ((SODoc) nUIDocViewXls.getDoc()).movePage(NUIDocViewXls.this.draggingIndex, this.dropIndex);
                                }
                            }
                            NUIDocViewXls.this.draggingIndex = -1;
                            setDropIndex(-1);
                        } else if (action == 4) {
                            NUIDocViewXls.this.draggingIndex = -1;
                            setDropIndex(-1);
                        } else if (action == 5) {
                            setDropIndex(-1);
                        } else if (action == 6) {
                            setDropIndex(-1);
                        }
                        return true;
                    }

                    public final void setDropIndex(int i) {
                        this.dropIndex = i;
                        int i2 = 0;
                        while (true) {
                            boolean z = true;
                            if (i2 < linearLayout.getChildCount() - 1) {
                                SheetTab sheetTab = (SheetTab) linearLayout.getChildAt(i2);
                                if (i2 != this.dropIndex || i2 == NUIDocViewXls.this.draggingIndex) {
                                    z = false;
                                }
                                sheetTab.setHighlight(z);
                                i2++;
                            } else {
                                return;
                            }
                        }
                    }
                });
                sheetTab.setOnClickDelete(new OnClickListener() {
                    public void onClick(View view) {
                        if (!NUIDocViewXls.this.mFinished) {
                            SheetTab sheetTab = (SheetTab) view;
                            String text = sheetTab.getText();
                            final int sheetNumber = sheetTab.getSheetNumber();
                            Activity activity = activity;
                            String string = NUIDocViewXls.this.getContext().getString(R.string.sodk_editor_delete_worksheet_q);
                            Utilities.yesNoMessage(activity, string, NUIDocViewXls.this.getContext().getString(R.string.sodk_editor_do_you_want_to_delete_the_sheet) + text + "\" ?", NUIDocViewXls.this.getContext().getString(R.string.sodk_editor_yes), NUIDocViewXls.this.getContext().getString(R.string.sodk_editor_no), new Runnable() {
                                public void run() {
                                    NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                                    nUIDocViewXls.deletingPage = true;
                                    nUIDocViewXls.getDoc().clearSelection();
                                    DocExcelView docExcelView = (DocExcelView) NUIDocViewXls.this.getDocView();
                                    if (docExcelView != null) {
                                        docExcelView.onSheetDeleted();
                                    }
                                    ((SODoc) NUIDocViewXls.this.getDoc()).deletePage(sheetNumber);
                                }
                            }, (Runnable) null);
                        }
                    }
                });
            }
            linearLayout.addView(sheetTab);
            sheetTab.setSelected(getCurrentSheet() == i2);
            if (numPages == 1) {
                sheetTab.showXView(false);
            }
        }
        if (this.mDocCfgOptions.isEditingEnabled()) {
            Button button = (Button) activity().getLayoutInflater().inflate(R.layout.sodk_editor_sheet_tab_plus, linearLayout, false);
            button.setText("+");
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    NUIDocViewXls nUIDocViewXls = NUIDocViewXls.this;
                    if (!nUIDocViewXls.mFinished) {
                        nUIDocViewXls.addingPage = true;
                        ((SODoc) NUIDocViewXls.this.getDoc()).addBlankPage(nUIDocViewXls.getDoc().getNumPages());
                    }
                }
            });
            linearLayout.addView(button);
        }
    }

    public void resetInputView() {
    }

    public final void setCellBox(SOEditText sOEditText, float f) {
        int selectionStart = sOEditText.getSelectionStart();
        int selectionEnd = sOEditText.getSelectionEnd();
        boolean z = selectionStart == 0 && selectionEnd > selectionStart && selectionEnd == sOEditText.getText().length();
        sOEditText.setText(String.format("%.2f", new Object[]{Float.valueOf(f)}));
        if (z) {
            sOEditText.selectAll();
        }
    }

    public void setSearchStart() {
        int currentSheet = getCurrentSheet();
        if (currentSheet >= 0) {
            getDoc().setSearchStart(currentSheet, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
        }
    }

    public final void setTextViewEnabled(SOTextView sOTextView, boolean z) {
        if (z) {
            sOTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.sodk_editor_button_text));
        } else {
            sOTextView.setTextColor(TestResult.NEUTRAL_COLOR);
        }
    }

    public boolean showKeyboard() {
        return false;
    }

    public void showUI(boolean z) {
        if (this.mShowUI) {
            super.showUI(z);
            if (this.mDocCfgOptions.isFullscreenEnabled() && z && this.mDocCfgOptions.isEditingEnabled()) {
                findViewById(R.id.fx_bar).setVisibility(0);
            }
        }
    }

    public void updateEditUIAppearance() {
        boolean z;
        this.mNuiEditToolbar.updateEditUIAppearanceXls();
        ArDkSelectionLimits selectionLimits = getDocView().getSelectionLimits();
        if (selectionLimits != null) {
            z = selectionLimits.getIsActive();
            if (z) {
                boolean isCaret = selectionLimits.getIsCaret();
            }
            if (z) {
                boolean isCaret2 = selectionLimits.getIsCaret();
            }
        } else {
            z = false;
        }
        this.mAlignOptionsButton.setEnabled(z);
    }

    public void updateReviewUIAppearance() {
    }

    public void updateUIAppearance() {
        boolean z;
        boolean z2;
        super.updateUIAppearance();
        ArDkSelectionLimits selectionLimits = getDocView().getSelectionLimits();
        boolean z3 = true;
        if (selectionLimits != null) {
            boolean isActive = selectionLimits.getIsActive();
            z = isActive && !selectionLimits.getIsCaret();
            z2 = isActive && selectionLimits.getIsCaret();
        } else {
            z2 = false;
            z = false;
        }
        this.mInsertRowAboveButton.setEnabled(z2 || z);
        this.mInsertRowBelowButton.setEnabled(z2 || z);
        this.mInsertColumnLeftButton.setEnabled(z2 || z);
        this.mInsertColumnRightButton.setEnabled(z2 || z);
        this.mDeleteRowButton.setEnabled(z2 || z);
        this.mDeleteColumnButton.setEnabled(z2 || z);
        this.mAlignOptionsButton.setEnabled(z2 || z);
        this.mNumberFormatButton.setEnabled(z2 || z);
        this.mFormulaSumButton.setEnabled(z2 || z);
        this.mFormulaDateTimeButton.setEnabled(z2 || z);
        this.mFormulaEngineeringButton.setEnabled(z2 || z);
        this.mFormulaFinancialButton.setEnabled(z2 || z);
        this.mFormulaInformationButton.setEnabled(z2 || z);
        this.mFormulaLogicalButton.setEnabled(z2 || z);
        this.mFormulaLookupButton.setEnabled(z2 || z);
        this.mFormulaMathsButton.setEnabled(z2 || z);
        this.mFormulaStatisticalButton.setEnabled(z2 || z);
        this.mFormulaTextButton.setEnabled(z2 || z);
        ((DocExcelView) getDocView()).onSelectionChanged();
        float selectedColumnWidth = ((SODoc) getDoc()).getSelectedColumnWidth();
        if (selectedColumnWidth > BitmapDescriptorFactory.HUE_RED) {
            setCellBox(this.mCellWidthBox, selectedColumnWidth * 2.54f);
        }
        this.mCellWidthBox.setEnabled(z2 || z);
        this.mIncreaseCellWidthButton.setEnabled(z2 || z);
        this.mDecreaseCellWidthButton.setEnabled(z2 || z);
        setTextViewEnabled(this.mCellWidthLabel, z2 || z);
        float selectedRowHeight = ((SODoc) getDoc()).getSelectedRowHeight();
        if (selectedRowHeight > BitmapDescriptorFactory.HUE_RED) {
            setCellBox(this.mCellHeightBox, selectedRowHeight * 2.54f);
        }
        this.mCellHeightBox.setEnabled(z2 || z);
        this.mIncreaseCellHeightButton.setEnabled(z2 || z);
        this.mDecreaseCellHeightButton.setEnabled(z2 || z);
        setTextViewEnabled(this.mCellHeightLabel, z2 || z);
        boolean tableCellsMerged = ((SODoc) getDoc()).getTableCellsMerged();
        SOSelectionTableRange selectionTableRange = ((SODoc) getDoc()).selectionTableRange();
        if (!z || (!tableCellsMerged && (selectionTableRange == null || (selectionTableRange.columnCount() < 2 && selectionTableRange.rowCount() < 2)))) {
            this.mMergeCellsButton.setEnabled(false);
        } else {
            this.mMergeCellsButton.setEnabled(true);
        }
        if (tableCellsMerged) {
            this.mMergeCellsButton.setText(getResources().getString(R.string.sodk_editor_unmerge_cells_upper));
        } else {
            this.mMergeCellsButton.setText(getResources().getString(R.string.sodk_editor_merge_cells_upper));
        }
        if (isFrozen()) {
            this.mFreezeCellButton.setText(getContext().getString(R.string.sodk_editor_unfreeze_panes));
        } else {
            this.mFreezeCellButton.setText(getContext().getString(R.string.sodk_editor_freeze_panes));
        }
        if (isFreezeShown()) {
            this.mFreezeEnableButton.setImageResource(R.drawable.sodk_editor_icon_toggle_on);
        } else {
            this.mFreezeEnableButton.setImageResource(R.drawable.sodk_editor_icon_toggle_off);
        }
        boolean isFrozen = isFrozen();
        Point selectedCell = ((DocExcelView) getDocView()).getSelectedCell();
        boolean z4 = (selectedCell == null || (selectedCell.x == 0 && selectedCell.y == 0)) ? false : true;
        ToolbarButton toolbarButton = this.mFreezeCellButton;
        if ((!z4 && !isFrozen) || !isFreezeShown()) {
            z3 = false;
        }
        toolbarButton.setEnabled(z3);
        this.mFreezeFirstRowButton.setEnabled(isFreezeShown());
        this.mFreezeFirstColButton.setEnabled(isFreezeShown());
    }

    public boolean usePagesView() {
        return false;
    }

    public NUIDocViewXls(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NUIDocViewXls(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
