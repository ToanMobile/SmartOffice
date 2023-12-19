package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.artifex.mupdf.fitz.PDFWidget;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ConfigOptions;
import com.artifex.solib.MuPDFDoc;
import com.artifex.solib.MuPDFWidget;
import com.artifex.solib.Waiter;
import com.artifex.solib.Worker;
import com.artifex.sonui.editor.DocPageView;
import com.artifex.sonui.editor.PDFFormEditor;
import com.artifex.sonui.editor.SelectionHandle;
import java.util.ArrayList;
import java.util.Objects;

public class PDFFormTextEditor extends PDFFormEditor {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean first = false;
    public boolean mDragging = false;
    public int mSelEnd = -1;
    public int mSelStart = -1;
    public SelectionHandle mSelectionHandleLower = null;
    public SelectionHandle mSelectionHandleUpper = null;
    public boolean mSetInitialSelection = false;
    public Rect[] mTextRects = null;
    public boolean mWaitingForRender = false;
    public TextWatcher mWatcher = null;
    public boolean messageDisplayed = false;
    public PopupWindow popupWindow;
    public boolean scrollIntoViewRequested = false;

    public PDFFormTextEditor(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public static void access$1600(PDFFormTextEditor pDFFormTextEditor) {
        int selectionStart = pDFFormTextEditor.mEditText.getSelectionStart();
        int selectionEnd = pDFFormTextEditor.mEditText.getSelectionEnd();
        if (selectionStart != selectionEnd) {
            String obj = pDFFormTextEditor.mEditText.getText().toString();
            String substring = obj.substring(0, selectionStart);
            String substring2 = obj.substring(selectionEnd);
            SOEditText sOEditText = pDFFormTextEditor.mEditText;
            sOEditText.setText(substring + substring2);
            pDFFormTextEditor.setEditTextSelection(selectionStart, selectionStart);
        }
    }

    public static void access$600(PDFFormTextEditor pDFFormTextEditor, int i, Point point) {
        int selectionStart = pDFFormTextEditor.mEditText.getSelectionStart();
        int selectionEnd = pDFFormTextEditor.mEditText.getSelectionEnd();
        int selectionFromTap = pDFFormTextEditor.selectionFromTap((float) point.x, (float) point.y);
        if (selectionFromTap < 0) {
            return;
        }
        if (i == 1) {
            if (point.y < 0) {
                selectionFromTap = 0;
            }
            int i2 = selectionEnd - 1;
            if (selectionFromTap >= i2) {
                selectionFromTap = i2;
            }
            pDFFormTextEditor.setEditTextSelection(selectionFromTap, selectionEnd);
            pDFFormTextEditor.invalidate();
        } else if (i == 2) {
            int i3 = selectionStart + 1;
            if (selectionFromTap <= i3) {
                selectionFromTap = i3;
            }
            pDFFormTextEditor.setEditTextSelection(selectionStart, Math.min(selectionFromTap, pDFFormTextEditor.mEditText.getText().toString().length()));
            pDFFormTextEditor.invalidate();
        }
    }

    private Rect getCaretRect() {
        int i;
        int convertDpToPixel = Utilities.convertDpToPixel(4.0f);
        Rect[] rectArr = this.mTextRects;
        if (rectArr == null || (i = this.mSelStart) < 0 || i > rectArr.length - 1) {
            return null;
        }
        Rect rect = new Rect();
        Rect rect2 = this.mTextRects[this.mSelStart];
        rect.set(rect2.left, rect2.top, rect2.right, rect2.bottom);
        Rect pageToView = pageToView(rect);
        int i2 = (convertDpToPixel / 2) + pageToView.right;
        pageToView.right = i2;
        pageToView.left = i2 - convertDpToPixel;
        return pageToView;
    }

    /* access modifiers changed from: private */
    public String getSelectedText() {
        return this.mEditText.getText().subSequence(this.mEditText.getSelectionStart(), this.mEditText.getSelectionEnd()).toString();
    }

    /* access modifiers changed from: private */
    public void setWidgetText(String str) {
        this.mWidget.setValue(str, this.first);
        this.first = false;
        this.mDoc.update(this.mPageNumber);
        this.mWaitingForRender = true;
    }

    public boolean cancel() {
        Utilities.dismissCurrentAlert();
        this.messageDisplayed = false;
        this.mEditText.setText(this.mOriginalValue);
        return stop();
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.mSelectionHandleUpper.dispatchTouchEvent(motionEvent) || this.mSelectionHandleLower.dispatchTouchEvent(motionEvent)) {
            return true;
        }
        if (isStopped() && motionEvent.getAction() == 0) {
            if (this.mPageView.onSingleTap((int) motionEvent.getRawX(), (int) motionEvent.getRawY(), false, (DocPageView.ExternalLinkListener) null)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void doubleTap(float f, float f2) {
        Rect[] rectArr = this.mTextRects;
        if (rectArr != null && rectArr.length > 0) {
            for (int i = 0; i < this.mTextRects.length; i++) {
                Rect[] rectArr2 = this.mTextRects;
                if (pageToView(new Rect(rectArr2[i].left, rectArr2[i].top, rectArr2[i].right, rectArr2[i].bottom)).contains((int) f, (int) f2)) {
                    String obj = this.mEditText.getText().toString();
                    if (obj.length() > 0) {
                        int min = Math.min(i, obj.length() - 1);
                        while (min > 0 && obj.charAt(min) != ' ' && obj.charAt(min) != 10) {
                            min--;
                        }
                        if (obj.charAt(min) == ' ' || obj.charAt(min) == 10) {
                            min++;
                        }
                        int min2 = Math.min(i, obj.length() - 1);
                        while (min2 < obj.length() - 1 && obj.charAt(min2) != ' ' && obj.charAt(min2) != 10) {
                            min2++;
                        }
                        if (obj.charAt(min2) == ' ' || obj.charAt(min2) == 10) {
                            min2--;
                        }
                        setEditTextSelection(min, min2 + 1);
                        invalidate();
                        showMenu();
                        return;
                    }
                }
            }
            singleTap(f, f2);
        }
        Utilities.showKeyboard(getContext());
    }

    public SOEditText getEditText() {
        return (SOEditText) findViewById(R.id.pdf_text_editor);
    }

    public final void hideMenu() {
        PopupWindow popupWindow2 = this.popupWindow;
        if (popupWindow2 != null) {
            popupWindow2.dismiss();
        }
        this.popupWindow = null;
    }

    public void onDraw(Canvas canvas) {
        if (!this.mStopped) {
            super.onDraw(canvas);
            if (this.mDocViewAtRest && !this.mWaitingForRender) {
                if (this.mSelStart == this.mSelEnd) {
                    Paint paint = new Paint();
                    paint.setColor(-65536);
                    Utilities.convertDpToPixel(4.0f);
                    Rect caretRect = getCaretRect();
                    if (caretRect != null) {
                        Rect rect = new Rect(caretRect);
                        if (new Rect(0, 0, getWidth(), getHeight()).intersect(caretRect)) {
                            canvas.drawRect(rect, paint);
                            return;
                        }
                        return;
                    }
                    return;
                }
                Paint paint2 = new Paint();
                paint2.setColor(-12303292);
                paint2.setAlpha(50);
                Rect[] rectArr = this.mTextRects;
                if (rectArr != null && rectArr.length > 0) {
                    int min = Math.min(this.mSelEnd + 1, rectArr.length);
                    for (int i = this.mSelStart; i < min; i++) {
                        Rect[] rectArr2 = this.mTextRects;
                        canvas.drawRect(pageToView(new Rect(rectArr2[i].left, rectArr2[i].top, rectArr2[i].right, rectArr2[i].bottom)), paint2);
                    }
                }
            }
        }
    }

    public void onGlobalLayout() {
        super.onGlobalLayout();
        this.mSelectionHandleUpper.setMayDraw(this.mDocViewAtRest);
        this.mSelectionHandleLower.setMayDraw(this.mDocViewAtRest);
        onSelectionChanged(this.mSelStart, this.mSelEnd);
    }

    public void onRenderComplete() {
        super.onRenderComplete();
        if (this.mWaitingForRender) {
            final AnonymousClass8 r0 = new Runnable() {
                public void run() {
                    PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                    if (pDFFormTextEditor.mSetInitialSelection) {
                        int length = pDFFormTextEditor.mEditText.getText().length();
                        PDFFormTextEditor pDFFormTextEditor2 = PDFFormTextEditor.this;
                        pDFFormTextEditor2.mEditText.setSelection(0, length);
                        pDFFormTextEditor2.onSelectionChanged(0, length);
                        PDFFormTextEditor.this.mSetInitialSelection = false;
                    } else {
                        pDFFormTextEditor.onSelectionChanged(pDFFormTextEditor.mEditText.getSelectionStart(), PDFFormTextEditor.this.mEditText.getSelectionEnd());
                    }
                    PDFFormTextEditor pDFFormTextEditor3 = PDFFormTextEditor.this;
                    if (pDFFormTextEditor3.mWaitingForRender) {
                        pDFFormTextEditor3.scrollIntoView();
                    }
                    PDFFormTextEditor pDFFormTextEditor4 = PDFFormTextEditor.this;
                    pDFFormTextEditor4.mWaitingForRender = false;
                    pDFFormTextEditor4.invalidate();
                }
            };
            this.mDoc.mWorker.add(new Worker.Task() {
                public Rect[] rects;

                public void run() {
                    PDFFormTextEditor.this.mTextRects = this.rects;
                    Runnable runnable = r0;
                    if (runnable != null) {
                        runnable.run();
                    }
                    PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                    if (pDFFormTextEditor.scrollIntoViewRequested) {
                        pDFFormTextEditor.scrollIntoViewInternal();
                    }
                    PDFFormTextEditor.this.scrollIntoViewRequested = false;
                }

                public void work() {
                    Rect[] rectArr;
                    MuPDFWidget muPDFWidget = PDFFormTextEditor.this.mWidget;
                    muPDFWidget.mDoc.checkForWorkerThread();
                    if (muPDFWidget.mWidget != null) {
                        ArrayList arrayList = new ArrayList();
                        com.artifex.mupdf.fitz.Rect bounds = muPDFWidget.mWidget.getBounds();
                        for (PDFWidget.TextWidgetLineLayout textWidgetLineLayout : muPDFWidget.mWidget.layoutTextWidget().lines) {
                            Rect makeRect = muPDFWidget.makeRect(textWidgetLineLayout.rect, bounds.x0, bounds.y0);
                            makeRect.right = makeRect.left;
                            arrayList.add(makeRect);
                            for (PDFWidget.TextWidgetCharLayout textWidgetCharLayout : textWidgetLineLayout.chars) {
                                arrayList.add(muPDFWidget.makeRect(textWidgetCharLayout.rect, bounds.x0, bounds.y0));
                            }
                        }
                        rectArr = new Rect[arrayList.size()];
                        arrayList.toArray(rectArr);
                    } else {
                        rectArr = null;
                    }
                    this.rects = rectArr;
                }
            });
        }
    }

    public final void onSelectionChanged(int i, int i2) {
        this.mSelStart = i;
        this.mSelEnd = i2;
        Rect[] rectArr = this.mTextRects;
        if (rectArr != null && rectArr.length > 0) {
            if (isStopped()) {
                this.mSelectionHandleUpper.hide();
                this.mSelectionHandleLower.hide();
                return;
            }
            if (!this.mDragging) {
                int i3 = this.mSelStart;
                if (i3 == this.mSelEnd) {
                    this.mSelectionHandleUpper.hide();
                    this.mSelectionHandleLower.hide();
                } else {
                    Rect pageToView = pageToView(this.mTextRects[i3]);
                    this.mSelectionHandleUpper.setPoint(pageToView.left, pageToView.top);
                    Rect pageToView2 = pageToView(this.mTextRects[Math.min(this.mSelEnd, this.mTextRects.length - 1)]);
                    this.mSelectionHandleLower.setPoint(pageToView2.right, pageToView2.bottom);
                    this.mSelectionHandleUpper.show();
                    this.mSelectionHandleLower.show();
                }
            }
            invalidate();
        }
    }

    public final Rect pageToView(Rect rect) {
        Rect rect2 = new Rect(rect);
        double factor = this.mPageView.getFactor();
        Rect rect3 = this.mWidgetBounds;
        rect2.offset(-rect3.left, -rect3.top);
        rect2.left = (int) (((double) rect2.left) * factor);
        rect2.top = (int) (((double) rect2.top) * factor);
        rect2.right = (int) (((double) rect2.right) * factor);
        rect2.bottom = (int) (((double) rect2.bottom) * factor);
        return rect2;
    }

    public void scrollCaretIntoView() {
        Rect caretRect;
        if (this.mSelStart == this.mSelEnd && (caretRect = getCaretRect()) != null) {
            Rect rect = new Rect(caretRect);
            double factor = this.mPageView.getFactor();
            rect.left = (int) (((double) rect.left) / factor);
            rect.top = (int) (((double) rect.top) / factor);
            rect.right = (int) (((double) rect.right) / factor);
            rect.bottom = (int) (((double) rect.bottom) / factor);
            Rect rect2 = this.mWidgetBounds;
            rect.offset(rect2.left, rect2.top);
            this.mDocView.scrollBoxIntoView(this.mPageNumber, new RectF(rect), true, Utilities.convertDpToPixel(50.0f));
        }
    }

    public void scrollIntoView() {
        if (this.mWaitingForRender) {
            this.scrollIntoViewRequested = true;
        } else {
            scrollIntoViewInternal();
        }
    }

    public final void scrollIntoViewInternal() {
        int i;
        Rect[] rectArr = this.mTextRects;
        if (rectArr == null || rectArr.length <= 0 || this.mSelStart == (i = this.mSelEnd)) {
            scrollCaretIntoView();
            return;
        }
        int min = Math.min(i, rectArr.length - 1);
        Rect[] rectArr2 = this.mTextRects;
        int i2 = this.mSelStart;
        this.mDocView.scrollBoxIntoView(this.mPageNumber, new RectF(new Rect(rectArr2[i2].left, rectArr2[i2].top, rectArr2[min].right, rectArr2[min].bottom)), true, Utilities.convertDpToPixel(50.0f));
    }

    public final int selectionFromTap(float f, float f2) {
        int i;
        int i2;
        Rect[] rectArr = this.mTextRects;
        if (rectArr == null || rectArr.length <= 0) {
            return -1;
        }
        int i3 = 0;
        while (true) {
            Rect[] rectArr2 = this.mTextRects;
            if (i3 < rectArr2.length) {
                Rect[] rectArr3 = this.mTextRects;
                if (pageToView(new Rect(rectArr3[i3].left, rectArr3[i3].top, rectArr3[i3].right, rectArr3[i3].bottom)).contains((int) f, (int) f2)) {
                    return i3 - 1;
                }
                i3++;
            } else {
                if (rectArr2 == null || rectArr2.length <= 0) {
                    i2 = -1;
                    i = -1;
                } else {
                    i2 = -1;
                    i = -1;
                    for (int i4 = 0; i4 < this.mTextRects.length; i4++) {
                        Rect[] rectArr4 = this.mTextRects;
                        Rect pageToView = pageToView(new Rect(rectArr4[i4].left, rectArr4[i4].top, rectArr4[i4].right, rectArr4[i4].bottom));
                        if (f2 >= ((float) pageToView.top) && f2 <= ((float) pageToView.bottom)) {
                            i2 = i4;
                            if (i == -1) {
                                i = i2;
                            }
                        }
                    }
                }
                if (i != -1 && f < ((float) this.mTextRects[i].left)) {
                    return i;
                }
                if (i2 != -1) {
                    return i2;
                }
                return this.mTextRects.length - 1;
            }
        }
    }

    public final void setEditTextSelection(int i, int i2) {
        this.mEditText.setSelection(i, i2);
        onSelectionChanged(i, i2);
    }

    public void setInitialValue() {
        this.mWidget.setEditingState(true);
        String trim = this.mWidget.mValue.trim();
        this.mOriginalValue = trim;
        this.mEditText.setText(trim);
        this.mWaitingForRender = true;
        this.mSetInitialSelection = true;
        this.first = true;
    }

    public void setNewValue(String str) {
        this.mWidget.setEditingState(true);
        this.mOriginalValue = str;
        this.mEditText.setText(str);
        setWidgetText(str);
        this.mWaitingForRender = true;
        this.mSetInitialSelection = true;
        this.first = true;
    }

    public void setupInput() {
        int i = this.mWidget.mTextFormat;
        this.mEditText.setInputType((i != 1 ? i != 3 ? i != 4 ? 1 : 32 : 16 : 12290) | PDFWidget.PDF_CH_FIELD_IS_SORT);
        boolean z = (this.mWidget.mFieldFlags & 4096) != 0;
        this.mEditText.setSingleLine(!z);
        this.mEditText.setImeOptions((!z ? 5 : 1) | 268435456 | 33554432);
        int i2 = this.mWidget.mMaxChars;
        this.mEditText.setFilters(i2 > 0 ? new InputFilter[]{new InputFilter.LengthFilter(i2)} : new InputFilter[0]);
    }

    public void show() {
        this.mEditText.requestFocus();
        super.show();
    }

    public void showMenu() {
        if (this.popupWindow != null) {
            hideMenu();
        }
        final ConfigOptions docConfigOptions = this.mDocView.getDocConfigOptions();
        View inflate = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.sodk_editor_form_edittext_popup, (ViewGroup) null);
        boolean z = true;
        PopupWindow popupWindow2 = new PopupWindow(inflate, -2, -2, true);
        this.popupWindow = popupWindow2;
        popupWindow2.setOutsideTouchable(true);
        this.popupWindow.setFocusable(false);
        this.popupWindow.showAsDropDown(this, 0, this.mSelectionHandleLower.getVisibility() == 0 ? this.mSelectionHandleLower.getHeight() : 0);
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                PDFFormTextEditor.this.popupWindow = null;
            }
        });
        View findViewById = inflate.findViewById(R.id.select_all);
        View findViewById2 = inflate.findViewById(R.id.cut);
        View findViewById3 = inflate.findViewById(R.id.copy);
        View findViewById4 = inflate.findViewById(R.id.paste);
        int selectionStart = this.mEditText.getSelectionStart();
        int selectionEnd = this.mEditText.getSelectionEnd();
        int length = this.mEditText.getText().toString().length();
        if (selectionStart == selectionEnd) {
            findViewById2.setVisibility(View.GONE);
            findViewById3.setVisibility(View.GONE);
        }
        if (selectionStart == 0 && selectionEnd == length) {
            findViewById.setVisibility(View.GONE);
        }
        if (docConfigOptions != null) {
            MuPDFDoc muPDFDoc = this.mDoc;
            boolean isExtClipboardInEnabled = docConfigOptions.isExtClipboardInEnabled();
            if (muPDFDoc.mInternalClipData == null) {
                if (!isExtClipboardInEnabled) {
                    z = false;
                } else {
                    z = ArDkLib.clipboardHasText();
                }
            }
            if (!z) {
                findViewById4.setVisibility(View.GONE);
            }
        }
        findViewById.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                int i = PDFFormTextEditor.$r8$clinit;
                pDFFormTextEditor.hideMenu();
                PDFFormTextEditor.this.mEditText.selectAll();
                PDFFormTextEditor pDFFormTextEditor2 = PDFFormTextEditor.this;
                int selectionStart = pDFFormTextEditor2.mEditText.getSelectionStart();
                int selectionEnd = PDFFormTextEditor.this.mEditText.getSelectionEnd();
                pDFFormTextEditor2.mEditText.setSelection(selectionStart, selectionEnd);
                pDFFormTextEditor2.onSelectionChanged(selectionStart, selectionEnd);
                PDFFormTextEditor.this.invalidate();
                PDFFormTextEditor.this.showMenu();
            }
        });
        findViewById2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                int i = PDFFormTextEditor.$r8$clinit;
                pDFFormTextEditor.hideMenu();
                MuPDFDoc muPDFDoc = PDFFormTextEditor.this.mDoc;
                boolean isExtClipboardOutEnabled = docConfigOptions.isExtClipboardOutEnabled();
                String access$1500 = PDFFormTextEditor.this.getSelectedText();
                if (access$1500 != null) {
                    muPDFDoc.mInternalClipData = access$1500;
                    if (isExtClipboardOutEnabled) {
                        ArDkLib.putTextToClipboard(access$1500);
                    }
                    muPDFDoc.mExternalClipDataHash = ArDkLib.getClipboardText().hashCode();
                }
                PDFFormTextEditor.access$1600(PDFFormTextEditor.this);
            }
        });
        findViewById3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                int i = PDFFormTextEditor.$r8$clinit;
                pDFFormTextEditor.hideMenu();
                MuPDFDoc muPDFDoc = PDFFormTextEditor.this.mDoc;
                boolean isExtClipboardOutEnabled = docConfigOptions.isExtClipboardOutEnabled();
                String access$1500 = PDFFormTextEditor.this.getSelectedText();
                if (access$1500 != null) {
                    muPDFDoc.mInternalClipData = access$1500;
                    if (isExtClipboardOutEnabled) {
                        ArDkLib.putTextToClipboard(access$1500);
                    }
                    muPDFDoc.mExternalClipDataHash = ArDkLib.getClipboardText().hashCode();
                }
            }
        });
        findViewById4.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                int i = PDFFormTextEditor.$r8$clinit;
                pDFFormTextEditor.hideMenu();
                PDFFormTextEditor.access$1600(PDFFormTextEditor.this);
                MuPDFDoc muPDFDoc = PDFFormTextEditor.this.mDoc;
                boolean isExtClipboardInEnabled = docConfigOptions.isExtClipboardInEnabled();
                Objects.requireNonNull(muPDFDoc);
                if (isExtClipboardInEnabled && ArDkLib.clipboardHasText()) {
                    String clipboardText = ArDkLib.getClipboardText();
                    if (clipboardText.hashCode() != muPDFDoc.mExternalClipDataHash) {
                        PDFFormTextEditor.this.mEditText.getText().insert(PDFFormTextEditor.this.mEditText.getSelectionStart(), clipboardText);
                        return;
                    }
                }
                String str = muPDFDoc.mInternalClipData;
                if (str != null) {
                    PDFFormTextEditor.this.mEditText.getText().insert(PDFFormTextEditor.this.mEditText.getSelectionStart(), str);
                    return;
                }
                PDFFormTextEditor.this.mEditText.getText().insert(PDFFormTextEditor.this.mEditText.getSelectionStart(), "");
            }
        });
    }

    public void singleTap(float f, float f2) {
        int selectionFromTap = selectionFromTap(f, f2);
        if (selectionFromTap >= 0 && selectionFromTap <= this.mEditText.getText().length()) {
            setEditTextSelection(selectionFromTap, selectionFromTap);
            invalidate();
            showMenu();
        }
        Utilities.showKeyboard(getContext());
    }

    public void start(DocMuPdfPageView docMuPdfPageView, int i, MuPDFDoc muPDFDoc, DocView docView, final MuPDFWidget muPDFWidget, Rect rect, EditorListener editorListener) {
        this.mSelectionHandleUpper = (SelectionHandle) ((Activity) getContext()).findViewById(R.id.pdf_form_text_editor_handle_upper);
        this.mSelectionHandleLower = (SelectionHandle) ((Activity) getContext()).findViewById(R.id.pdf_form_text_editor_handle_lower);
        super.start(docMuPdfPageView, i, muPDFDoc, docView, muPDFWidget, rect, editorListener);
        Utilities.showKeyboard(getContext());
        AnonymousClass1 r3 = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                PDFFormTextEditor.this.setWidgetText(charSequence.toString());
            }
        };
        this.mWatcher = r3;
        this.mEditText.addTextChangedListener(r3);
        this.mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 5) {
                    return false;
                }
                if (!PDFFormTextEditor.this.stop()) {
                    return true;
                }
                PDFFormTextEditor.this.mEditorListener.onStopped();
                return false;
            }
        });
        this.mEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == 0) {
                    boolean z = i == 61;
                    PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                    if (!((pDFFormTextEditor.mWidget.mFieldFlags & 4096) != 0) && i == 66) {
                        z = true;
                    }
                    if (z && pDFFormTextEditor.stop()) {
                        PDFFormTextEditor.this.mEditorListener.onStopped();
                        return true;
                    }
                }
                if (keyEvent.getAction() == 1) {
                    int selectionStart = PDFFormTextEditor.this.mEditText.getSelectionStart();
                    int selectionEnd = PDFFormTextEditor.this.mEditText.getSelectionEnd();
                    PDFFormTextEditor pDFFormTextEditor2 = PDFFormTextEditor.this;
                    int i2 = PDFFormTextEditor.$r8$clinit;
                    pDFFormTextEditor2.onSelectionChanged(selectionStart, selectionEnd);
                    if (i != 4) {
                        return true;
                    }
                }
                return false;
            }
        });
        this.mDoc.jsEventListener2 = new MuPDFDoc.JsEventListener() {
            public AlertResult onAlert(String str, String str2, int i, int i2, boolean z, String str3, boolean z2) {
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                if (!pDFFormTextEditor.messageDisplayed) {
                    pDFFormTextEditor.messageDisplayed = true;
                    Utilities.showMessageAndWait((Activity) pDFFormTextEditor.getContext(), "", str2, new Runnable() {
                        public void run() {
                            PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                            pDFFormTextEditor.messageDisplayed = false;
                            Utilities.showKeyboard(pDFFormTextEditor.getContext());
                        }
                    });
                }
                PDFFormTextEditor.this.mEditText.requestFocus();
                return null;
            }
        };
        this.mEditText.requestFocus();
        this.mDoc.mWorker.add(new Worker.Task(this) {
            public void run() {
            }

            public void work() {
                MuPDFWidget muPDFWidget = muPDFWidget;
                muPDFWidget.mDoc.checkForWorkerThread();
                PDFWidget pDFWidget = muPDFWidget.mWidget;
                if (pDFWidget != null) {
                    pDFWidget.eventFocus();
                    muPDFWidget.mWidget.eventDown();
                    muPDFWidget.mWidget.eventUp();
                }
            }
        });
        this.mSelectionHandleUpper.setSelectionHandleListener(new SelectionHandle.SelectionHandleListener() {
            public void onDrag(SelectionHandle selectionHandle) {
                Point point = selectionHandle.getPoint();
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                PDFFormTextEditor.access$600(pDFFormTextEditor, pDFFormTextEditor.mSelectionHandleUpper.getKind(), point);
            }

            public void onEndDrag(SelectionHandle selectionHandle) {
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                pDFFormTextEditor.mDragging = false;
                pDFFormTextEditor.onSelectionChanged(pDFFormTextEditor.mSelStart, pDFFormTextEditor.mSelEnd);
                PDFFormTextEditor.this.showMenu();
            }

            public void onStartDrag(SelectionHandle selectionHandle) {
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                pDFFormTextEditor.mDragging = true;
                pDFFormTextEditor.hideMenu();
            }
        });
        this.mSelectionHandleLower.setSelectionHandleListener(new SelectionHandle.SelectionHandleListener() {
            public void onDrag(SelectionHandle selectionHandle) {
                Point point = selectionHandle.getPoint();
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                PDFFormTextEditor.access$600(pDFFormTextEditor, pDFFormTextEditor.mSelectionHandleLower.getKind(), point);
            }

            public void onEndDrag(SelectionHandle selectionHandle) {
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                pDFFormTextEditor.mDragging = false;
                pDFFormTextEditor.onSelectionChanged(pDFFormTextEditor.mSelStart, pDFFormTextEditor.mSelEnd);
                PDFFormTextEditor.this.showMenu();
            }

            public void onStartDrag(SelectionHandle selectionHandle) {
                PDFFormTextEditor pDFFormTextEditor = PDFFormTextEditor.this;
                pDFFormTextEditor.mDragging = true;
                pDFFormTextEditor.hideMenu();
            }
        });
        invalidate();
        this.first = true;
    }

    public boolean stop() {
        if (this.mStopped) {
            return true;
        }
        hideMenu();
        this.mWidget.setEditingState(false);
        boolean value = this.mWidget.setValue(this.mEditText.getText().toString(), this.first);
        if (!value) {
            this.mWidget.setEditingState(true);
        } else {
            MuPDFDoc muPDFDoc = this.mDoc;
            int i = this.mPageNumber;
            Objects.requireNonNull(muPDFDoc);
            Waiter waiter = new Waiter();
            muPDFDoc.mWorker.add(new Worker.Task(i, true, waiter) {
                public void run(
/*
Method generation error in method: com.artifex.solib.MuPDFDoc.25.run():void, dex: classes.dex
                jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFDoc.25.run():void, class status: UNLOADED
                	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
                	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.util.ArrayList.forEach(ArrayList.java:1259)
                	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:156)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.util.ArrayList.forEach(ArrayList.java:1259)
                	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                
*/

                public void work(
/*
Method generation error in method: com.artifex.solib.MuPDFDoc.25.work():void, dex: classes.dex
                jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFDoc.25.work():void, class status: UNLOADED
                	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
                	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.util.ArrayList.forEach(ArrayList.java:1259)
                	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:156)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.util.ArrayList.forEach(ArrayList.java:1259)
                	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                
*/
            });
            waiter.doWait();
        }
        if (!value) {
            return false;
        }
        super.stop();
        SelectionHandle selectionHandle = this.mSelectionHandleUpper;
        if (selectionHandle != null) {
            selectionHandle.hide();
        }
        SelectionHandle selectionHandle2 = this.mSelectionHandleLower;
        if (selectionHandle2 != null) {
            selectionHandle2.hide();
        }
        SOEditText sOEditText = this.mEditText;
        if (sOEditText != null) {
            TextWatcher textWatcher = this.mWatcher;
            if (textWatcher != null) {
                sOEditText.removeTextChangedListener(textWatcher);
            }
            this.mEditText.setOnEditorActionListener((TextView.OnEditorActionListener) null);
            this.mEditText.setOnKeyListener((OnKeyListener) null);
        }
        return true;
    }
}
