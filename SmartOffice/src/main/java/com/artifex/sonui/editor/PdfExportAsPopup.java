package com.artifex.sonui.editor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.artifex.R;

public class PdfExportAsPopup implements PopupWindow.OnDismissListener {
    public final View mAnchor;
    public final Context mContext;
    public final OnClickInterface mOnClickInterface;
    public NUIPopupWindow popupWindow;

    public class ExportFormatAdapterData {
        public String mFormat;
        public int mIconDrawble;
        public int mIconString;

        public ExportFormatAdapterData(PdfExportAsPopup pdfExportAsPopup, int i, int i2, String str) {
            this.mIconString = i;
            this.mIconDrawble = i2;
            this.mFormat = str;
        }

        public String getFormat() {
            return this.mFormat;
        }

        public int getIconDrawable() {
            return this.mIconDrawble;
        }

        public int getIconString() {
            return this.mIconString;
        }
    }

    public class FormatGridAdapter extends RecyclerView.Adapter<FormatGridAdapter.ViewHolder> {
        public Context mContext;
        public ExportFormatAdapterData[] mLocalDataSet;
        public OnClickInterface mOnClickInterface;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final ImageView mIconView;
            public final SOTextView mTextView;

            public ViewHolder(FormatGridAdapter formatGridAdapter, View view) {
                super(view);
                this.mTextView = (SOTextView) view.findViewById(R.id.doc_icon_text);
                this.mIconView = (ImageView) view.findViewById(R.id.doc_icon);
            }

            public ImageView getIconView() {
                return this.mIconView;
            }

            public SOTextView getTextView() {
                return this.mTextView;
            }
        }

        public FormatGridAdapter(Context context, ExportFormatAdapterData[] exportFormatAdapterDataArr, OnClickInterface onClickInterface) {
            this.mLocalDataSet = exportFormatAdapterDataArr;
            this.mContext = context;
            this.mOnClickInterface = onClickInterface;
        }

        public int getItemCount() {
            return this.mLocalDataSet.length;
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.getTextView().setText(this.mLocalDataSet[i].getIconString());
            viewHolder.getIconView().setImageDrawable(this.mContext.getResources().getDrawable(this.mLocalDataSet[i].getIconDrawable()));
            viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    PdfExportAsPopup.this.popupWindow.dismiss();
                    FormatGridAdapter formatGridAdapter = FormatGridAdapter.this;
                    OnClickInterface onClickInterface = formatGridAdapter.mOnClickInterface;
                    if (onClickInterface != null) {
                        onClickInterface.onClick(formatGridAdapter.mLocalDataSet[i].getFormat());
                    }
                }
            });
            viewHolder.getIconView().setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    PdfExportAsPopup.this.popupWindow.dismiss();
                    FormatGridAdapter formatGridAdapter = FormatGridAdapter.this;
                    OnClickInterface onClickInterface = formatGridAdapter.mOnClickInterface;
                    if (onClickInterface != null) {
                        onClickInterface.onClick(formatGridAdapter.mLocalDataSet[i].getFormat());
                    }
                }
            });
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sodk_editor_pdf_export_as_item, viewGroup, false));
        }
    }

    public interface OnClickInterface {
        void onClick(String str);
    }

    public PdfExportAsPopup(Context context, View view, OnClickInterface onClickInterface) {
        this.mContext = context;
        this.mAnchor = view;
        this.mOnClickInterface = onClickInterface;
    }

    public void onDismiss() {
        this.popupWindow.dismiss();
    }

    public void show() {
        ExportFormatAdapterData[] exportFormatAdapterDataArr = {new ExportFormatAdapterData(this, R.string.sodk_editor_pdf_export_as_text, R.drawable.sodk_editor_icon_txt, "txt")};
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.sodk_editor_pdf_export_as, (ViewGroup) null);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.exportTypesView);
        recyclerView.setAdapter(new FormatGridAdapter(this.mContext, exportFormatAdapterDataArr, this.mOnClickInterface));
        recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 1, RecyclerView.HORIZONTAL, false));
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(this.mContext);
        this.popupWindow = nUIPopupWindow;
        nUIPopupWindow.setContentView(inflate);
        this.popupWindow.setClippingEnabled(false);
        this.popupWindow.setOnDismissListener(this);
        this.popupWindow.setFocusable(true);
        int i = this.mContext.getResources().getDisplayMetrics().widthPixels;
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.sodk_editor_pdf_extract_as_item_width);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R.dimen.sodk_editor_pdf_extract_as_popup_margin);
        int min = Math.min(dimensionPixelSize * 1, i - (dimensionPixelSize2 * 2));
        int i2 = min % dimensionPixelSize;
        if (i2 == 0) {
            i2 = 0;
        } else if ((1 | ((min ^ dimensionPixelSize) >> 31)) <= 0) {
            i2 += dimensionPixelSize;
        }
        int dimensionPixelSize3 = (this.mContext.getResources().getDimensionPixelSize(R.dimen.sodk_editor_pdf_extract_as_popup_padding) * 2) + (this.mContext.getResources().getDimensionPixelSize(R.dimen.sodk_editor_pdf_extract_as_item_padding) * 2) + (min - i2);
        this.popupWindow.setWidth(dimensionPixelSize3);
        int[] iArr = new int[2];
        this.mAnchor.getLocationOnScreen(iArr);
        int i3 = (i - dimensionPixelSize2) - (iArr[0] + dimensionPixelSize3);
        if (i3 > 0) {
            i3 = 0;
        }
        this.popupWindow.showAsDropDown(this.mAnchor, i3, 0);
    }
}
