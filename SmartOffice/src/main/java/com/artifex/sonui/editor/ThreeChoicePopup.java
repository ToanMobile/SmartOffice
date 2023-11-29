package com.artifex.sonui.editor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ThreeChoicePopup {
    public boolean choice1Enabled = true;
    public boolean choice2Enabled = true;
    public boolean choice3Enabled = true;
    public NUIPopupWindow popup;

    public interface ResultListener {
        void onChoice(int i);
    }

    public void setChoice1Enabled(boolean z) {
        this.choice1Enabled = z;
    }

    public void setChoice2Enabled(boolean z) {
        this.choice2Enabled = z;
    }

    public void setChoice3Enabled(boolean z) {
        this.choice3Enabled = z;
    }

    public void show(Context context, View view, final ResultListener resultListener, String str, String str2, String str3) {
        View inflate = View.inflate(context, R.layout.sodk_editor_three_choise, (ViewGroup) null);
        Button button = (Button) inflate.findViewById(R.id.button1);
        button.setText(str);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ThreeChoicePopup.this.popup.dismiss();
                resultListener.onChoice(1);
            }
        });
        button.setEnabled(this.choice1Enabled);
        Button button2 = (Button) inflate.findViewById(R.id.button2);
        button2.setText(str2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ThreeChoicePopup.this.popup.dismiss();
                resultListener.onChoice(2);
            }
        });
        button2.setEnabled(this.choice2Enabled);
        Button button3 = (Button) inflate.findViewById(R.id.button3);
        button3.setText(str3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ThreeChoicePopup.this.popup.dismiss();
                resultListener.onChoice(3);
            }
        });
        button3.setEnabled(this.choice3Enabled);
        NUIPopupWindow nUIPopupWindow = new NUIPopupWindow(inflate, -2, -2);
        this.popup = nUIPopupWindow;
        nUIPopupWindow.setFocusable(true);
        this.popup.showAsDropDown(view, 30, 30);
    }
}
