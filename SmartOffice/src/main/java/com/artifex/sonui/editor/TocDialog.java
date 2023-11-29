package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import androidx.core.content.ContextCompat;
import com.artifex.solib.ArDkDoc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TocDialog implements PopupWindow.OnDismissListener {
    public static TocDialog singleton;
    public final View mAnchor;
    public Button mCancelButton;
    public final Context mContext;
    public final ArDkDoc mDoc;
    public final TocDialogListener mListener;
    public NUIPopupWindow popupWindow;

    public class TocData {
        public int handle;
        public String label;
        public int level;
        public int page;
        public int parentHandle;
        public float x;
        public float y;

        public TocData(TocDialog tocDialog, int i, int i2, int i3, String str, String str2, float f, float f2, AnonymousClass1 r9) {
            this.handle = i;
            this.parentHandle = i2;
            this.label = str;
            this.x = f;
            this.y = f2;
            this.page = i3;
        }
    }

    public interface TocDialogListener {
    }

    public class TocListViewAdapter extends BaseAdapter {
        public ArrayList<TocData> listEntries = new ArrayList<>();
        public Context mContext;
        public Map<Integer, TocData> mapEntries = new HashMap();

        public TocListViewAdapter(TocDialog tocDialog, Context context) {
            this.mContext = context;
        }

        public int getCount() {
            return this.listEntries.size();
        }

        public Object getItem(int i) {
            return this.listEntries.get(i);
        }

        public long getItemId(int i) {
            return 0;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            TocData tocData = this.listEntries.get(i);
            View inflate = ((Activity) this.mContext).getLayoutInflater().inflate(R.layout.sodk_editor_toc_list_item, viewGroup, false);
            SOTextView sOTextView = (SOTextView) inflate.findViewById(R.id.toc_item);
            sOTextView.setText((CharSequence) tocData.label);
            sOTextView.setPadding(sOTextView.getPaddingLeft() + (Utilities.convertDpToPixel(40.0f) * tocData.level), sOTextView.getPaddingTop(), sOTextView.getPaddingRight(), sOTextView.getPaddingBottom());
            return inflate;
        }
    }

    public TocDialog(Context context, ArDkDoc arDkDoc, View view, TocDialogListener tocDialogListener) {
        this.mContext = context;
        this.mAnchor = view;
        this.mDoc = arDkDoc;
        this.mListener = tocDialogListener;
    }

    public final void dismiss() {
        NUIPopupWindow nUIPopupWindow = this.popupWindow;
        if (nUIPopupWindow != null) {
            nUIPopupWindow.dismiss();
        }
        singleton = null;
    }

    public void onDismiss() {
        dismiss();
    }

    public void showOrResize() {
        int i;
        int i2;
        int i3;
        int i4;
        Point screenSize = Utilities.getScreenSize(this.mContext);
        if (Utilities.isPhoneDevice(this.mContext)) {
            i3 = screenSize.x;
            i4 = screenSize.y;
            this.mCancelButton.setVisibility(0);
            this.popupWindow.setBackgroundDrawable((Drawable) null);
            i2 = 0;
            i = 0;
        } else {
            int dimension = (int) this.mContext.getResources().getDimension(R.dimen.sodk_editor_toc_offsetx);
            i4 = screenSize.y / 2;
            this.mCancelButton.setVisibility(8);
            this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this.mContext, R.drawable.sodk_editor_table_of_contents));
            int dimension2 = (int) this.mContext.getResources().getDimension(R.dimen.sodk_editor_toc_offsety);
            i2 = dimension;
            i3 = (screenSize.x / 2) - dimension;
            i = dimension2;
        }
        if (this.popupWindow.isShowing()) {
            this.popupWindow.update(i2, i, i3, i4);
            return;
        }
        this.popupWindow.setWidth(i3);
        this.popupWindow.setHeight(i4);
        this.popupWindow.showAtLocation(this.mAnchor, 0, i2, i);
    }
}
