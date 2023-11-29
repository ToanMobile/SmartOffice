package com.artifex.sonui.editor;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SOEditText extends FrameLayout {
    public static Constructor mConstructor;
    public EditText mEditText;

    public interface Constructor {
        EditText construct(Context context);

        EditText construct(Context context, AttributeSet attributeSet);

        EditText construct(Context context, AttributeSet attributeSet, int i);

        EditText construct(Context context, AttributeSet attributeSet, int i, int i2);
    }

    public SOEditText(Context context) {
        super(context);
        Constructor constructor = mConstructor;
        if (constructor != null) {
            this.mEditText = constructor.construct(getContext());
        } else {
            this.mEditText = new EditText(getContext());
        }
        init();
    }

    public static void setConstructor(Constructor constructor) {
        mConstructor = constructor;
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        this.mEditText.addTextChangedListener(textWatcher);
    }

    public boolean callOnClick() {
        return this.mEditText.callOnClick();
    }

    public void clearFocus() {
        this.mEditText.clearFocus();
    }

    public int getSelectionEnd() {
        return this.mEditText.getSelectionEnd();
    }

    public int getSelectionStart() {
        return this.mEditText.getSelectionStart();
    }

    public Object getTag(int i) {
        return this.mEditText.getTag(i);
    }

    public Editable getText() {
        return this.mEditText.getText();
    }

    public final void init() {
        super.addView(this.mEditText);
        setFocusable(false);
        setPadding(0, 0, 0, 0);
    }

    public boolean isEnabled() {
        return this.mEditText.isEnabled();
    }

    public boolean isFieldFocused() {
        EditText editText = this.mEditText;
        if (editText != null) {
            return editText.isFocused();
        }
        return false;
    }

    public void removeTextChangedListener(TextWatcher textWatcher) {
        this.mEditText.removeTextChangedListener(textWatcher);
    }

    public void selectAll() {
        this.mEditText.selectAll();
    }

    public void setCustomInsertionActionModeCallback(ActionMode.Callback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
            this.mEditText.setCustomInsertionActionModeCallback(callback);
        }
    }

    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        this.mEditText.setCustomSelectionActionModeCallback(callback);
    }

    public void setEnabled(boolean z) {
        this.mEditText.setEnabled(z);
    }

    public void setFilters(InputFilter[] inputFilterArr) {
        this.mEditText.setFilters(inputFilterArr);
    }

    public void setFocusableInTouchMode(boolean z) {
        this.mEditText.setFocusableInTouchMode(z);
    }

    public void setImeActionLabel(CharSequence charSequence, int i) {
        this.mEditText.setImeActionLabel(charSequence, i);
    }

    public void setImeOptions(int i) {
        this.mEditText.setImeOptions(i);
    }

    public void setInputType(int i) {
        this.mEditText.setInputType(i);
    }

    public void setLayoutParams(LayoutParams layoutParams) {
        this.mEditText.setLayoutParams(layoutParams);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mEditText.setOnClickListener(onClickListener);
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
        this.mEditText.setOnEditorActionListener(onEditorActionListener);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.mEditText.setOnFocusChangeListener(onFocusChangeListener);
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        this.mEditText.setOnKeyListener(onKeyListener);
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mEditText.setOnTouchListener(onTouchListener);
    }

    public void setSelectAllOnFocus(boolean z) {
        this.mEditText.setSelectAllOnFocus(z);
    }

    public void setSelection(int i, int i2) {
        this.mEditText.setSelection(i, i2);
    }

    public void setSingleLine(boolean z) {
        this.mEditText.setSingleLine(z);
    }

    public void setTag(int i, Object obj) {
        this.mEditText.setTag(i, obj);
    }

    public void setText(String str) {
        this.mEditText.setText(str);
    }

    public void setTextSize(int i, float f) {
        this.mEditText.setTextSize(i, f);
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setVisibility(i);
        }
    }

    public void setSelection(int i) {
        this.mEditText.setSelection(i);
    }

    public void setSingleLine() {
        this.mEditText.setSingleLine();
    }

    public SOEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Constructor constructor = mConstructor;
        if (constructor != null) {
            this.mEditText = constructor.construct(getContext(), attributeSet);
        } else {
            this.mEditText = new EditText(getContext(), attributeSet);
        }
        init();
    }

    public SOEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Constructor constructor = mConstructor;
        if (constructor != null) {
            this.mEditText = constructor.construct(getContext(), attributeSet, i);
        } else {
            this.mEditText = new EditText(getContext(), attributeSet, i);
        }
        init();
    }

    public SOEditText(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Constructor constructor = mConstructor;
        if (constructor != null) {
            this.mEditText = constructor.construct(getContext(), attributeSet, i, i2);
        } else {
            this.mEditText = new EditText(getContext(), attributeSet, i, i2);
        }
        init();
    }
}
