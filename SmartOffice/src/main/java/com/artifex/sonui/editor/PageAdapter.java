package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.artifex.solib.ArDkDoc;

public class PageAdapter extends BaseAdapter {
    public static final int DOC_KIND = 1;
    public static final int PDF_KIND = 2;
    public final Context mContext;
    public ArDkDoc mDoc;
    public DocViewHost mHost = null;
    public int mKind = 0;
    public int mPageCount;
    public int setupWidth = -1;

    public PageAdapter(Context context, DocViewHost docViewHost, int i) {
        this.mHost = docViewHost;
        this.mContext = context;
        this.mKind = i;
    }

    public int getCount() {
        return this.mPageCount;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        DocPageView docPageView;
        Activity activity = (Activity) this.mContext;
        if (view == null) {
            int i2 = this.mKind;
            if (i2 == 1) {
                docPageView = new DocPageView(activity, this.mDoc);
            } else {
                docPageView = i2 == 2 ? new DocMuPdfPageView(activity, this.mDoc) : null;
            }
        } else {
            docPageView = (DocPageView) view;
        }
        if (this.setupWidth == -1) {
            this.setupWidth = this.mHost.getDocView().getWidth();
        }
        docPageView.setupPage(i, this.setupWidth, 1);
        return docPageView;
    }

    public void setCount(int i) {
        this.mPageCount = i;
    }

    public void setDoc(ArDkDoc arDkDoc) {
        this.mDoc = arDkDoc;
    }
}
