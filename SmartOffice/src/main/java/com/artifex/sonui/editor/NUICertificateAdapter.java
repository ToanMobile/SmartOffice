package com.artifex.sonui.editor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;

public class NUICertificateAdapter extends RecyclerView.Adapter<ViewHolder> {
    public ArrayList<NUICertificate> mCertificates;
    public ItemClickListener mClickListener;
    public Context mContext;
    public LayoutInflater mInflater;
    public int selectedPos = 0;

    public interface ItemClickListener {
        void onDetailsClick(View view, int i);

        void onItemClick(View view, int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SOTextView myDNView;
        public Button myDetails;
        public Switch mySwitch;
        public SOTextView myTextView;

        public ViewHolder(NUICertificateAdapter nUICertificateAdapter, View view) {
            super(view);
            this.myTextView = (SOTextView) view.findViewById(R.id.sodk_editor_certificate_name);
            this.myDNView = (SOTextView) view.findViewById(R.id.sodk_editor_certificate_dname);
            this.myDetails = (Button) view.findViewById(R.id.certificate_details_button);
            this.mySwitch = (Switch) view.findViewById(R.id.sodk_editor_certificate_switch);
            view.setOnClickListener(this);
        }

        public View getItemView() {
            return this.itemView;
        }

        public void onClick(View view) {
        }
    }

    public NUICertificateAdapter(Context context, ArrayList<NUICertificate> arrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.mCertificates = arrayList;
        this.mContext = context;
    }

    public NUICertificate getItem(int i) {
        return this.mCertificates.get(i);
    }

    public int getItemCount() {
        return this.mCertificates.size();
    }

    public int getSelectedPos() {
        return this.selectedPos;
    }

    public void selectItem(int i) {
        super.notifyItemChanged(this.selectedPos);
        this.selectedPos = i;
        super.notifyItemChanged(i);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (i < this.mCertificates.size()) {
            NUICertificate nUICertificate = this.mCertificates.get(i);
            HashMap<String, String> distinguishedName = nUICertificate.distinguishedName();
            if (distinguishedName != null) {
                viewHolder.myTextView.setText((CharSequence) distinguishedName.get("CN"));
                viewHolder.myDNView.setText((CharSequence) nUICertificate.subject);
            } else {
                viewHolder.myTextView.setText((CharSequence) "-");
                viewHolder.myDNView.setText((CharSequence) "-");
            }
            viewHolder.mySwitch.setChecked(this.selectedPos == i);
            viewHolder.mySwitch.setOnClickListener(new NUICertificateAdapter$$ExternalSyntheticLambda0(this, i, 0));
            viewHolder.myDetails.setOnClickListener(new NUICertificateAdapter$$ExternalSyntheticLambda0(this, i, 1));
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this, this.mInflater.inflate(R.layout.sodk_editor_certificate_item, viewGroup, false));
    }
}
