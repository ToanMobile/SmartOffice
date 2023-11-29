package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import com.artifex.solib.MuPDFDoc;
import com.artifex.solib.MuPDFWidget;
import com.artifex.sonui.editor.DocView;

public class PDFFormEditor extends RelativeLayout {
    public MuPDFDoc mDoc = null;
    public DocView mDocView = null;
    public boolean mDocViewAtRest = true;
    public ViewTreeObserver mDocViewTreeObserver;
    public SOEditText mEditText = null;
    public EditorListener mEditorListener = null;
    public ViewTreeObserver.OnGlobalLayoutListener mLayoutListener;
    public String mOriginalValue;
    public int mPageNumber;
    public DocMuPdfPageView mPageView = null;
    public boolean mStopped = false;
    public MuPDFWidget mWidget = null;
    public Rect mWidgetBounds = null;

    public interface EditorListener {
        void onStopped();
    }

    public PDFFormEditor(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWillNotDraw(false);
    }

    public boolean cancel() {
        return true;
    }

    public void doubleTap(float f, float f2) {
    }

    public SOEditText getEditText() {
        return null;
    }

    public String getOriginalValue() {
        return this.mOriginalValue;
    }

    public String getValue() {
        return this.mEditText.getText().toString();
    }

    public boolean isStopped() {
        return this.mStopped;
    }

    public final void matchWidgetSizeAndPosition() {
        if (getVisibility() == 0) {
            Rect rect = new Rect();
            rect.set(this.mWidgetBounds);
            this.mPageView.pageToView(rect, rect);
            rect.offset(this.mPageView.getLeft(), this.mPageView.getTop());
            rect.offset(-this.mDocView.getScrollX(), -this.mDocView.getScrollY());
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            layoutParams.leftMargin = rect.left;
            layoutParams.topMargin = rect.top;
            setLayoutParams(layoutParams);
            this.mEditText.setLayoutParams(new LayoutParams(rect.width(), rect.height()));
            invalidate();
        }
    }

    public void onGlobalLayout() {
        this.mDocViewAtRest = this.mDocView.isAtRest();
        invalidate();
    }

    public void onRenderComplete() {
        matchWidgetSizeAndPosition();
        invalidate();
    }

    public void scrollCaretIntoView() {
    }

    public void scrollIntoView() {
        this.mDocView.scrollBoxIntoView(this.mPageNumber, new RectF(this.mWidgetBounds), true);
    }

    public void setInitialValue() {
    }

    public void setNewValue(String str) {
    }

    public void setupInput() {
    }

    public void show() {
        setVisibility(0);
        this.mEditText.requestFocus();
        matchWidgetSizeAndPosition();
    }

    public void singleTap(float f, float f2) {
    }

    public void start(DocMuPdfPageView docMuPdfPageView, int i, MuPDFDoc muPDFDoc, DocView docView, MuPDFWidget muPDFWidget, Rect rect, EditorListener editorListener) {
        this.mDoc = muPDFDoc;
        this.mWidget = muPDFWidget;
        this.mPageView = docMuPdfPageView;
        this.mDocView = docView;
        this.mEditText = getEditText();
        this.mEditorListener = editorListener;
        this.mPageNumber = i;
        this.mStopped = false;
        this.mWidgetBounds = new Rect(rect);
        setupInput();
        show();
        setInitialValue();
        ViewTreeObserver viewTreeObserver = this.mDocView.getViewTreeObserver();
        this.mDocViewTreeObserver = viewTreeObserver;
        AnonymousClass1 r2 = new ViewTreeObserver.OnGlobalLayoutListener(this) {
            public void onGlobalLayout() {
                this.onGlobalLayout();
            }
        };
        this.mLayoutListener = r2;
        viewTreeObserver.addOnGlobalLayoutListener(r2);
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent motionEvent) {
                PDFFormEditor.this.doubleTap(motionEvent.getX(), motionEvent.getY());
                return true;
            }

            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                PDFFormEditor.this.singleTap(motionEvent.getX(), motionEvent.getY());
                return true;
            }
        });
        this.mEditText.setOnTouchListener(new OnTouchListener(this) {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    public boolean stop() {
        this.mStopped = true;
        this.mPageView.invalidate();
        SOEditText sOEditText = this.mEditText;
        if (sOEditText != null) {
            sOEditText.clearFocus();
            this.mEditText.setOnTouchListener((OnTouchListener) null);
        }
        MuPDFDoc muPDFDoc = this.mDoc;
        if (muPDFDoc != null) {
            muPDFDoc.jsEventListener2 = null;
        }
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.setShowKeyboardListener((DocView.ShowKeyboardListener) null);
        }
        ViewTreeObserver viewTreeObserver = this.mDocViewTreeObserver;
        if (viewTreeObserver != null) {
            viewTreeObserver.removeOnGlobalLayoutListener(this.mLayoutListener);
            this.mDocViewTreeObserver = null;
            this.mLayoutListener = null;
        }
        setVisibility(8);
        return true;
    }
}
