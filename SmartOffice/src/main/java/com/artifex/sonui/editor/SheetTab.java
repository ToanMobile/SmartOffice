package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import androidx.core.content.ContextCompat;

public class SheetTab extends LinearLayout {
    public static boolean mIsEditingEnabled = false;
    public boolean mHighlight = false;
    public int mSheetNumber = 0;

    public SheetTab(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.sodk_editor_sheet_tab, this);
    }

    private View getOuterView() {
        return findViewById(R.id.sheetTab);
    }

    private Space getSpacerView() {
        return (Space) findViewById(R.id.spacer);
    }

    private SOTextView getXView() {
        return (SOTextView) findViewById(R.id.button2);
    }

    public static void setEditingEbabled(boolean z) {
        mIsEditingEnabled = z;
    }

    public SOTextView getNameView() {
        return (SOTextView) findViewById(R.id.button1);
    }

    public int getSheetNumber() {
        return this.mSheetNumber;
    }

    public String getText() {
        return getNameView().getText().toString();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mHighlight) {
            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(getContext(), R.color.sodk_editor_excel_sheet_tab_highlight_color));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) Utilities.convertDpToPixel((float) getContext().getResources().getInteger(R.integer.sodk_editor_selected_page_border_width)));
            canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
        }
    }

    public boolean performClick() {
        return getOuterView().performClick();
    }

    public void setHighlight(boolean z) {
        this.mHighlight = z;
        invalidate();
    }

    public void setOnClickDelete(final OnClickListener onClickListener) {
        getXView().setOnClickListener(new OnClickListener(this) {
            public void onClick(View view) {
                onClickListener.onClick(this);
            }
        });
    }

    public void setOnClickTab(final OnClickListener onClickListener) {
        getOuterView().setOnClickListener(new OnClickListener(this) {
            public void onClick(View view) {
                onClickListener.onClick(this);
            }
        });
    }

    public void setOnLongClickTab(final OnLongClickListener onLongClickListener) {
        getOuterView().setOnLongClickListener(new OnLongClickListener(this) {
            public boolean onLongClick(View view) {
                onLongClickListener.onLongClick(this);
                return true;
            }
        });
    }

    public void setSelected(boolean z) {
        getNameView().setSelected(z);
        getXView().setSelected(z);
        if (z) {
            setBackgroundResource(R.drawable.sodk_editor_sheet_tab_selected);
            if (mIsEditingEnabled) {
                showXView(true);
            } else {
                showXView(false);
            }
        } else {
            setBackgroundResource(R.drawable.sodk_editor_sheet_tab);
            showXView(false);
        }
    }

    public void setSheetNumber(int i) {
        this.mSheetNumber = i;
    }

    public void setText(String str) {
        getNameView().setText((CharSequence) str);
    }

    public void showXView(boolean z) {
        if (z) {
            getXView().setVisibility(0);
            getSpacerView().setVisibility(0);
            return;
        }
        getXView().setVisibility(8);
        getSpacerView().setVisibility(8);
    }

    public SheetTab(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
