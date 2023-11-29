package com.artifex.sonui.editor;

import android.view.View;
import com.artifex.sonui.editor.NUICertificateAdapter;

public final /* synthetic */ class NUICertificateAdapter$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ NUICertificateAdapter f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ NUICertificateAdapter$$ExternalSyntheticLambda0(NUICertificateAdapter nUICertificateAdapter, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = nUICertificateAdapter;
        this.f$1 = i;
    }

    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                NUICertificateAdapter nUICertificateAdapter = this.f$0;
                int i = this.f$1;
                NUICertificateAdapter.ItemClickListener itemClickListener = nUICertificateAdapter.mClickListener;
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(view, i);
                }
                nUICertificateAdapter.selectItem(i);
                nUICertificateAdapter.notifyDataSetChanged();
                return;
            default:
                NUICertificateAdapter nUICertificateAdapter2 = this.f$0;
                int i2 = this.f$1;
                NUICertificateAdapter.ItemClickListener itemClickListener2 = nUICertificateAdapter2.mClickListener;
                if (itemClickListener2 != null) {
                    itemClickListener2.onDetailsClick(view, i2);
                    return;
                }
                return;
        }
    }
}
