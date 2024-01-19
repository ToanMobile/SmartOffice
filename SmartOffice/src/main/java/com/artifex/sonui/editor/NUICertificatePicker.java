package com.artifex.sonui.editor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.artifex.R;
import com.artifex.mupdf.fitz.PKCS7DistinguishedName;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ConfigOptions;
import com.artifex.sonui.editor.NUICertificateAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class NUICertificatePicker extends AppCompatActivity implements NUICertificateAdapter.ItemClickListener {
    public static final String CERTIFICATE_SERIAL = "cert_serial";
    public static NUICertificatePickerListener mListener;
    public NUICertificateAdapter adapter;
    public ConfigOptions mAppCfgOptions = ArDkLib.getAppConfigOptions();
    public HashMap<String, String> mDetails;
    public PKCS7DistinguishedName mPKCS7DistinguishedName;
    public Button mSignButton;
    public HashMap<String, String> mV3ExtensionsDetails;
    public HashMap<String, String> mValidityDetails;
    public Class mViewerClass;

    public interface NUICertificatePickerListener {
        void onCancel();

        void onOK(String str, PKCS7DistinguishedName pKCS7DistinguishedName);
    }

    public void onBackPressed() {
        NUICertificatePickerListener nUICertificatePickerListener = mListener;
        if (nUICertificatePickerListener != null) {
            nUICertificatePickerListener.onCancel();
        }
        super.onBackPressed();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.sodk_editor_certificate_pick);
        this.mDetails = null;
        this.mV3ExtensionsDetails = null;
        this.mValidityDetails = null;
        this.mViewerClass = NUICertificateViewer.class;
        Button button = (Button) findViewById(R.id.sodk_editor_choose_signature);
        this.mSignButton = button;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NUICertificatePickerListener nUICertificatePickerListener;
                int selectedPos = NUICertificatePicker.this.adapter.getSelectedPos();
                if (selectedPos != -1 && selectedPos < NUICertificatePicker.this.adapter.getItemCount()) {
                    new Intent();
                    NUICertificate item = NUICertificatePicker.this.adapter.getItem(selectedPos);
                    if (item != null && (nUICertificatePickerListener = NUICertificatePicker.mListener) != null) {
                        nUICertificatePickerListener.onOK(item.serial, NUICertificatePicker.this.mPKCS7DistinguishedName);
                        NUICertificatePicker.this.finish();
                    }
                }
            }
        });
    }

    public void onDetailsClick(View view, int i) {
        NUICertificate item;
        if (mListener != null && (item = this.adapter.getItem(i)) != null) {
            HashMap<String, String> distinguishedName = item.distinguishedName();
            distinguishedName.putAll(item.v3Extensions());
            distinguishedName.putAll(item.info());
            distinguishedName.putAll(item.validity());
            Intent intent = new Intent(this, this.mViewerClass);
            intent.putExtra("certificateDetails", distinguishedName);
            startActivity(intent);
        }
    }

    public void onItemClick(View view, int i) {
        refreshDetails(i);
    }

    public void populate(NUICertificateStore nUICertificateStore) {
        LinearLayout linearLayout;
        NUICertificate[] signingCertificates = nUICertificateStore.getSigningCertificates();
        ArrayList arrayList = new ArrayList();
        for (NUICertificate nUICertificate : signingCertificates) {
            if (this.mAppCfgOptions.mSettingsBundle.getBoolean("NonRepudiationCertsOnlyKey", false)) {
                String str = nUICertificate.v3Extensions().get("KEYUSAGE");
                if (str != null && str.contains("Non Repudiation")) {
                    arrayList.add(nUICertificate);
                }
            } else {
                arrayList.add(nUICertificate);
            }
        }
        NUICertificateAdapter nUICertificateAdapter = new NUICertificateAdapter(this, arrayList);
        this.adapter = nUICertificateAdapter;
        nUICertificateAdapter.setClickListener(this);
        ((RecyclerView) findViewById(R.id.sodk_editor_certificate_view)).setAdapter(this.adapter);
        if (this.adapter.getItemCount() == 0) {
            linearLayout = (LinearLayout) findViewById(R.id.sodk_editor_certificate_list);
        } else {
            linearLayout = (LinearLayout) findViewById(R.id.sodk_editor_certificate_none);
        }
        if (linearLayout != null) {
            linearLayout.setVisibility(View.GONE);
        }
        if (this.adapter.getItemCount() > 0) {
            refreshDetails(0);
        }
    }

    public final void refreshDetails(int i) {
        RecyclerView recyclerView;
        NUICertificate item = this.adapter.getItem(i);
        if (item != null) {
            this.mDetails = item.distinguishedName();
            this.mV3ExtensionsDetails = item.v3Extensions();
            this.mValidityDetails = item.validity();
            this.mPKCS7DistinguishedName = item.pkcs7DistinguishedName();
        }
        if (this.adapter.getItemCount() > 0 && (recyclerView = (RecyclerView) findViewById(R.id.sodk_editor_certificate_view)) != null) {
            this.adapter.selectItem(i);
            recyclerView.invalidate();
            recyclerView.requestLayout();
        }
        this.mSignButton.setEnabled(this.adapter.getItemCount() > 0 && i >= 0);
    }
}
