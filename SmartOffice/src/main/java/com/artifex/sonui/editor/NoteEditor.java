package com.artifex.sonui.editor;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.artifex.R;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkSelectionLimits;

public class NoteEditor {
    public SOTextView mAuthorView;
    public SOEditText mCommentView;
    public View mCover;
    public NoteDataHandler mDataHandler = null;
    public SOTextView mDateView;
    public View mEditor;
    public boolean mEditorDismissed = false;
    public boolean mEditorLostFocus = false;
    public int mEditorScrollDiffX = 0;
    public int mEditorScrollDiffY = 0;
    public DocPageView mPageView;
    public DocView mScrollView;
    public ArDkSelectionLimits mSelLimits;

    public interface NoteDataHandler {
        String getAuthor();

        String getComment();

        String getDate();

        void setComment(String str);
    }

    public NoteEditor(final Activity activity, DocView docView, DocViewHost docViewHost, NoteDataHandler noteDataHandler) {
        this.mScrollView = docView;
        this.mDataHandler = noteDataHandler;
        this.mEditor = activity.findViewById(R.id.doc_note_editor);
        this.mCover = activity.findViewById(R.id.doc_cover);
        ViewGroup viewGroup = (ViewGroup) this.mEditor.getParent();
        this.mCommentView = (SOEditText) activity.findViewById(R.id.doc_note_editor_text);
        this.mDateView = (SOTextView) activity.findViewById(R.id.doc_note_editor_date);
        this.mAuthorView = (SOTextView) activity.findViewById(R.id.doc_note_editor_author);
        this.mCommentView.setEnabled(false);
        this.mCover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NoteEditor.this.mEditorDismissed = true;
                Utilities.hideKeyboard(activity);
                NoteEditor.this.mCommentView.clearFocus();
                NoteEditor.this.mCover.setVisibility(View.GONE);
            }
        });
        this.mCommentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    NoteEditor.this.mCover.setVisibility(View.VISIBLE);
                    if (!NoteEditor.this.mEditorLostFocus) {
                        Rect rect = new Rect();
                        NoteEditor.this.mScrollView.getGlobalVisibleRect(rect);
                        rect.offset(0, -rect.top);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) NoteEditor.this.mEditor.getLayoutParams();
                        NoteEditor noteEditor = NoteEditor.this;
                        noteEditor.mEditorScrollDiffY = rect.top - layoutParams.topMargin;
                        noteEditor.mEditorScrollDiffX = 0;
                        noteEditor.mEditor.getLocationInWindow(new int[2]);
                        int i = layoutParams.leftMargin;
                        if (i < rect.left) {
                            NoteEditor.this.mEditorScrollDiffX = Math.abs(i);
                        } else {
                            int i2 = i + layoutParams.width;
                            int i3 = rect.right;
                            if (i2 > i3) {
                                NoteEditor.this.mEditorScrollDiffX = i3 - i2;
                            }
                        }
                        NoteEditor noteEditor2 = NoteEditor.this;
                        noteEditor2.mScrollView.smoothScrollBy(noteEditor2.mEditorScrollDiffX, noteEditor2.mEditorScrollDiffY);
                    }
                    NoteEditor.this.mEditorLostFocus = false;
                    return;
                }
                NoteEditor.this.mCover.setVisibility(View.GONE);
                NoteEditor.this.saveData();
                NoteEditor noteEditor3 = NoteEditor.this;
                if (noteEditor3.mEditorDismissed) {
                    noteEditor3.mEditorDismissed = false;
                    noteEditor3.mScrollView.smoothScrollBy(-noteEditor3.mEditorScrollDiffX, -noteEditor3.mEditorScrollDiffY);
                    NoteEditor noteEditor4 = NoteEditor.this;
                    noteEditor4.mEditorScrollDiffX = 0;
                    noteEditor4.mEditorScrollDiffY = 0;
                    return;
                }
                noteEditor3.mEditorLostFocus = true;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        NoteEditor.this.mCommentView.requestFocus();
                    }
                }, 100);
            }
        });
    }

    public void focus() {
        this.mCommentView.requestFocus();
    }

    public Rect getRect() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mEditor.getLayoutParams();
        Rect rect = new Rect();
        int i = layoutParams.leftMargin;
        rect.left = i;
        rect.top = layoutParams.topMargin;
        rect.right = this.mEditor.getMeasuredWidth() + i;
        rect.bottom = this.mEditor.getMeasuredHeight() + rect.top;
        return rect;
    }

    public void hide() {
        this.mEditor.setVisibility(View.GONE);
        this.mCover.setVisibility(View.GONE);
    }

    public boolean isVisible() {
        return this.mEditor.getVisibility() == View.VISIBLE;
    }

    public void move() {
        View view = this.mEditor;
        if (view != null && this.mSelLimits != null && view.getVisibility() == View.VISIBLE && this.mPageView != null) {
            RectF box = this.mSelLimits.getBox();
            Point pageToView = this.mPageView.pageToView((int) box.left, (int) box.bottom);
            pageToView.offset(this.mPageView.getLeft(), this.mPageView.getTop());
            pageToView.offset(-this.mScrollView.getScrollX(), -this.mScrollView.getScrollY());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mEditor.getLayoutParams();
            if (box.left > ((float) (this.mPageView.getPage().sizeAtZoom(1.0d).x / 2))) {
                pageToView.x -= layoutParams.width;
            }
            layoutParams.leftMargin = pageToView.x;
            layoutParams.topMargin = pageToView.y;
            this.mEditor.setLayoutParams(layoutParams);
        }
    }

    public void preMoving() {
        if (this.mCommentView.isEnabled()) {
            if (this.mCommentView.hasFocus()) {
                this.mEditorDismissed = true;
            }
            saveData();
            this.mCommentView.setEnabled(false);
        }
    }

    public void saveComment() {
        saveData();
    }

    public void saveData() {
        this.mDataHandler.setComment(this.mCommentView.getText().toString());
    }

    public void setCommentEditable(boolean z) {
        this.mCommentView.setEnabled(z);
    }

    public void show(ArDkSelectionLimits arDkSelectionLimits, DocPageView docPageView) {
        this.mSelLimits = arDkSelectionLimits;
        this.mPageView = docPageView;
        ArDkDoc doc = this.mScrollView.getDoc();
        String author = this.mDataHandler.getAuthor();
        String date = this.mDataHandler.getDate();
        String comment = this.mDataHandler.getComment();
        if (author == null || author.isEmpty()) {
            this.mAuthorView.setVisibility(View.GONE);
        } else {
            this.mAuthorView.setVisibility(View.VISIBLE);
            this.mAuthorView.setText((CharSequence) author);
        }
        if (date == null || date.isEmpty()) {
            this.mDateView.setVisibility(View.GONE);
        } else {
            this.mDateView.setVisibility(View.VISIBLE);
            this.mDateView.setText((CharSequence) Utilities.formatDateForLocale(this.mScrollView.getContext(), date, doc.getDateFormatPattern()));
        }
        this.mCommentView.setText(comment);
        this.mEditor.setVisibility(View.VISIBLE);
    }
}
