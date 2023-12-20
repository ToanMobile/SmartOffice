package com.artifex.sonui.editor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import billing.pro.ProActivity$$ExternalSyntheticLambda2;
import com.artifex.mupdf.fitz.PKCS7DistinguishedName;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class NUICertificateViewer extends AppCompatActivity {
    public static NUICertificateViewerListener mListener;
    public HashMap<String, String> mDetails;
    public ImageButton mDoneButton;
    public Boolean mHasVerifyResult = Boolean.FALSE;
    public PKCS7DistinguishedName mPKCS7DistinguishedName;
    public int mResult = -1;
    public int mUpdatedSinceSigning = 0;

    public interface NUICertificateViewerListener {
        void onCancel();
    }

    public void onBackPressed() {
        NUICertificateViewerListener nUICertificateViewerListener = mListener;
        if (nUICertificateViewerListener != null) {
            nUICertificateViewerListener.onCancel();
        }
        super.onBackPressed();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.sodk_editor_certificate_view);
        Intent intent = getIntent();
        this.mDetails = (HashMap) intent.getSerializableExtra("certificateDetails");
        this.mHasVerifyResult = Boolean.valueOf(intent.hasExtra("verifyResult"));
        this.mResult = intent.getIntExtra("verifyResult", -1);
        this.mUpdatedSinceSigning = intent.getIntExtra("updatedSinceSigning", 0);
        ImageButton imageButton = (ImageButton) findViewById(R.id.sodk_certificate_view_done);
        this.mDoneButton = imageButton;
        imageButton.setOnClickListener(new ProActivity$$ExternalSyntheticLambda2(this));
        populate(this.mDetails);
    }

    public void populate(HashMap<String, String> hashMap) {
        SOTextView sOTextView = (SOTextView) findViewById(R.id.sodk_editor_certificate_result);
        SOTextView sOTextView2 = (SOTextView) findViewById(R.id.sodk_editor_certificate_result_detail);
        if (hashMap != null) {
            ((SOTextView) findViewById(R.id.certificate_cn)).setText((CharSequence) hashMap.get("CN"));
            ((SOTextView) findViewById(R.id.certificate_c)).setText((CharSequence) hashMap.get("C"));
            ((SOTextView) findViewById(R.id.certificate_ou)).setText((CharSequence) hashMap.get("OU"));
            ((SOTextView) findViewById(R.id.certificate_o)).setText((CharSequence) hashMap.get("O"));
            ((SOTextView) findViewById(R.id.certificate_s)).setText((CharSequence) hashMap.get("S"));
            ((SOTextView) findViewById(R.id.certificate_l)).setText((CharSequence) hashMap.get("L"));
            ((SOTextView) findViewById(R.id.certificate_email)).setText((CharSequence) hashMap.get("EMAIL"));
            populateDateField(hashMap, "NOTBEFORE", (SOTextView) findViewById(R.id.certificate_not_before));
            populateDateField(hashMap, "NOTAFTER", (SOTextView) findViewById(R.id.certificate_expiry));
            ((SOTextView) findViewById(R.id.certificate_keyusage)).setText((CharSequence) hashMap.get("KEYUSAGE"));
            ((SOTextView) findViewById(R.id.certificate_extended_keyusage)).setText((CharSequence) hashMap.get("EXTENDEDKEYUSAGE"));
            ((SOTextView) findViewById(R.id.certificate_serial)).setText((CharSequence) hashMap.get("SERIAL"));
        }
        if (this.mHasVerifyResult.booleanValue()) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.sodk_editor_certificate_result_panel);
            if (linearLayout != null) {
                linearLayout.setVisibility(View.VISIBLE);
            }
            int i = this.mResult;
            if (i != 0) {
                if (i != 3) {
                    sOTextView.setText(R.string.sodk_editor_certificate_verify_failed);
                    sOTextView2.setText(R.string.sodk_editor_certificate_verify_not_trusted);
                } else {
                    sOTextView.setText(R.string.sodk_editor_certificate_verify_failed);
                    sOTextView2.setText(R.string.sodk_editor_certificate_verify_digest_failure);
                }
            } else if (this.mUpdatedSinceSigning > 0) {
                sOTextView.setText(R.string.sodk_editor_certificate_verify_warning);
                sOTextView2.setText(R.string.sodk_editor_certificate_verify_has_changes);
            } else {
                sOTextView.setText(R.string.sodk_editor_certificate_verify_ok);
                sOTextView2.setText(R.string.sodk_editor_certificate_verify_permitted_changes);
            }
            LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.sodk_editor_certificate_list);
            if (linearLayout2 != null) {
                linearLayout2.invalidate();
                linearLayout2.requestLayout();
            }
        }
    }

    public final void populateDateField(HashMap<String, String> hashMap, String str, SOTextView sOTextView) {
        String str2;
        if (hashMap.containsKey(str)) {
            try {
                str2 = new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm").format(new Date(((long) Integer.valueOf(hashMap.get(str)).intValue()) * 1000));
            } catch (NumberFormatException unused) {
                str2 = hashMap.get(str);
            } catch (NullPointerException unused2) {
                str2 = "";
            }
            sOTextView.setText((CharSequence) str2);
        }
    }
}
