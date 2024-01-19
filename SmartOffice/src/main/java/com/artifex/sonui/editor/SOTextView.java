package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.artifex.R;

public class SOTextView extends FrameLayout {
    public static Constructor mConstructor;
    public OnClickListener mClickListener = null;
    public TextView mTextView;

    public interface Constructor {
        TextView construct(Context context);

        TextView construct(Context context, AttributeSet attributeSet);

        TextView construct(Context context, AttributeSet attributeSet, int i);

        TextView construct(Context context, AttributeSet attributeSet, int i, int i2);
    }

    public SOTextView(Context context) {
        super(context);
        Constructor constructor = mConstructor;
        if (constructor != null) {
            this.mTextView = constructor.construct(getContext());
        } else {
            this.mTextView = new TextView(getContext());
        }
        init();
    }

    public static void setConstructor(Constructor constructor) {
        mConstructor = constructor;
    }

    public Drawable[] getCompoundDrawables() {
        return this.mTextView.getCompoundDrawables();
    }

    public CharSequence getText() {
        return this.mTextView.getText();
    }

    public final void init() {
        // TODO this.mTextView.setId(R.id.sodk_editor_text_view);
        super.addView(this.mTextView);
        setPadding(0, 0, 0, 0);
        OnClickListener onClickListener = this.mClickListener;
        if (onClickListener != null) {
            this.mTextView.setOnClickListener(onClickListener);
        }
    }

    public void setCompoundDrawablePadding(int i) {
        this.mTextView.setCompoundDrawablePadding(i);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        this.mTextView.setCompoundDrawablesWithIntrinsicBounds(i, i2, i3, i4);
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        TextView textView = this.mTextView;
        if (textView != null) {
            textView.setEnabled(z);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        TextView textView = this.mTextView;
        if (textView == null) {
            this.mClickListener = onClickListener;
        } else {
            textView.setOnClickListener(onClickListener);
        }
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        this.mTextView.setOnKeyListener(onKeyListener);
    }

    public void setSelected(boolean z) {
        this.mTextView.setSelected(z);
    }

    public void setText(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
    }

    public void setTextColor(int i) {
        this.mTextView.setTextColor(i);
    }

    public void setTextSize(int i, float f) {
        this.mTextView.setTextSize(i, f);
    }

    public void setTypeface(Typeface typeface) {
        this.mTextView.setTypeface(typeface);
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        TextView textView = this.mTextView;
        if (textView != null) {
            textView.setVisibility(i);
        }
    }

    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        this.mTextView.setText(charSequence, bufferType);
    }

    public void setTextSize(float f) {
        this.mTextView.setTextSize(f);
    }

    public final void setText(int i) {
        this.mTextView.setText(i);
    }

    public SOTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Constructor constructor = mConstructor;
        if (constructor != null) {
            this.mTextView = constructor.construct(getContext(), attributeSet);
        } else {
            this.mTextView = new TextView(getContext(), attributeSet);
        }
        init();
    }

    public SOTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Constructor constructor = mConstructor;
        if (constructor != null) {
            this.mTextView = constructor.construct(getContext(), attributeSet, i);
        } else {
            this.mTextView = new TextView(getContext(), attributeSet, i);
        }
        init();
    }

    public SOTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Constructor constructor = mConstructor;
        if (constructor != null) {
            this.mTextView = constructor.construct(getContext(), attributeSet, i, i2);
        } else {
            this.mTextView = new TextView(getContext(), attributeSet, i, i2);
        }
        init();
    }
}
