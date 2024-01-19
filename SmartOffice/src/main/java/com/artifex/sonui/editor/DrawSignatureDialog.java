package com.artifex.sonui.editor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.artifex.R;

public class DrawSignatureDialog extends Dialog {
    public static final /* synthetic */ int $r8$clinit = 0;
    public DrawSignatureView drawView;
    public DrawSignatureListener listener;

    public interface DrawSignatureListener {
        void onCancel();

        void onSave(Bitmap bitmap);
    }

    public DrawSignatureDialog(Context context, DrawSignatureListener drawSignatureListener) {
        super(context, 16974407);
        this.listener = drawSignatureListener;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.sodk_editor_draw_signature);
        DrawSignatureView drawSignatureView = (DrawSignatureView) findViewById(R.id.drawView);
        this.drawView = drawSignatureView;
        drawSignatureView.post(new Runnable() {
            public void run() {
                DrawSignatureDialog drawSignatureDialog = DrawSignatureDialog.this;
                int i = DrawSignatureDialog.$r8$clinit;
                ((Button) drawSignatureDialog.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        DrawSignatureDialog.this.dismiss();
                        DrawSignatureDialog.this.listener.onSave(DrawSignatureDialog.this.drawView.toBitmap());
                    }
                });
                ((Button) drawSignatureDialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        DrawSignatureDialog.this.dismiss();
                        DrawSignatureDialog.this.listener.onCancel();
                    }
                });
                ((Button) drawSignatureDialog.findViewById(R.id.erase)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        DrawSignatureDialog.this.drawView.erase();
                    }
                });
            }
        });
    }
}
