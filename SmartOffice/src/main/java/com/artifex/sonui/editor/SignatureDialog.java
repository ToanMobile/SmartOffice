package com.artifex.sonui.editor;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.artifex.mupdf.fitz.PKCS7Signer;
import com.artifex.solib.ConfigOptions;
import com.artifex.solib.FileUtils;
import com.artifex.solib.MuPDFWidget;
import com.artifex.solib.SOOutputStream;
import com.artifex.solib.SignatureAppearance;
import com.artifex.solib.Worker;
import com.artifex.sonui.editor.DrawSignatureDialog;
import com.artifex.sonui.editor.NUIDocView;
import com.artifex.sonui.editor.SignatureStyle;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class SignatureDialog extends Dialog {
    public static SignatureDialog currentDialog;
    public TextView appearanceNameText;
    public CheckBox cbDate;
    public CheckBox cbDistinguishedName;
    public CheckBox cbLabels;
    public CheckBox cbLocation;
    public CheckBox cbLogo;
    public CheckBox cbName;
    public CheckBox cbReason;
    public Button chooseImageButton;
    public Context context;
    public Button drawSignatureButton;
    public Button editButton;
    public SOEditText editLocation;
    public SOEditText editReason;
    public SOEditText editStyleName;
    public RelativeLayout edit_panel;
    public ImageView edit_preview;
    public SignatureListener listener;
    public boolean mayUseImages;
    public Button saveButton;
    public String savedLocation = "";
    public String savedReason = "";
    public int savedSpinnerIndex = -1;
    public NUIDocView.SelectImageListener selectImageListener = new NUIDocView.SelectImageListener() {
        public void onImageSelected(Uri uri) {
            String str = SignatureStyle.getSigDirPath(SignatureDialog.this.context) + "imported_" + UUID.randomUUID().toString() + ".png";
            if (FileUtils.exportContentUri(SignatureDialog.this.context, uri, str).equalsIgnoreCase(str)) {
                String preInsertImage = Utilities.preInsertImage(SignatureDialog.this.getContext(), str);
                if (!preInsertImage.equalsIgnoreCase(str)) {
                    FileUtils.copyFile(preInsertImage, str, true);
                    FileUtils.deleteFile(preInsertImage);
                }
                SignatureDialog signatureDialog = SignatureDialog.this;
                SignatureStyle signatureStyle = signatureDialog.styleEditing;
                signatureStyle.type = SignatureStyle.SignatureStyleType.SignatureStyleType_Image;
                signatureStyle.leftImage = str;
                SignatureDialog.access$2000(signatureDialog);
            }
        }
    };
    public boolean shouldRestore = false;
    public boolean signPressed = false;
    public boolean signShowing = true;
    public RelativeLayout sign_panel;
    public ImageView sign_preview;
    public NUIPKCS7Signer signer;
    public SignatureStyle styleEditing;
    public Spinner styleSpinner;
    public RadioButton styleTypeDrawButton;
    public RadioButton styleTypeImageButton;
    public RadioButton styleTypeNoneButton;
    public RadioGroup styleTypeRadioGroup;
    public RadioButton styleTypeTextButton;
    public MuPDFWidget widget;

    public interface SignatureListener {
        void onCancel();

        void onSign(SignatureAppearance signatureAppearance);
    }

    public SignatureDialog(Context context2, ConfigOptions configOptions, MuPDFWidget muPDFWidget, NUIPKCS7Signer nUIPKCS7Signer, SignatureListener signatureListener) {
        super(context2, 16974407);
        this.widget = muPDFWidget;
        this.signer = nUIPKCS7Signer;
        this.listener = signatureListener;
        this.context = context2;
        this.mayUseImages = true;
        if (configOptions != null) {
            this.mayUseImages = configOptions.isImageInsertEnabled();
        }
    }

    public static void access$1900(SignatureDialog signatureDialog) {
        Objects.requireNonNull(signatureDialog);
        SignatureAppearance appearance = SignatureStyle.getCurrentStyle().toAppearance(signatureDialog.getContext());
        appearance.location = signatureDialog.getFieldText(signatureDialog.editLocation);
        appearance.reason = signatureDialog.getFieldText(signatureDialog.editReason);
        signatureDialog.setPreview(appearance, signatureDialog.sign_preview);
    }

    public static void access$2000(SignatureDialog signatureDialog) {
        SignatureStyle signatureStyle = signatureDialog.styleEditing;
        signatureStyle.name = signatureDialog.cbName.isChecked();
        signatureStyle.dn = signatureDialog.cbDistinguishedName.isChecked();
        signatureStyle.date = signatureDialog.cbDate.isChecked();
        signatureStyle.location = signatureDialog.cbLocation.isChecked();
        signatureStyle.reason = signatureDialog.cbReason.isChecked();
        signatureStyle.labels = signatureDialog.cbLabels.isChecked();
        signatureStyle.logo = signatureDialog.cbLogo.isChecked();
        signatureDialog.setPreview(signatureStyle.toAppearance(signatureDialog.getContext()), signatureDialog.edit_preview);
    }

    public static void onConfigurationChange() {
        SignatureDialog signatureDialog = currentDialog;
        if (signatureDialog != null) {
            signatureDialog.signShowing = signatureDialog.sign_panel.getVisibility() == 0;
            signatureDialog.savedLocation = signatureDialog.editLocation.getText().toString().trim();
            signatureDialog.savedReason = signatureDialog.editReason.getText().toString().trim();
            signatureDialog.savedSpinnerIndex = signatureDialog.styleSpinner.getSelectedItemPosition();
            signatureDialog.shouldRestore = true;
            signatureDialog.createLayout();
        }
    }

    public final void configureStyleTypeButtons(SignatureStyle.SignatureStyleType signatureStyleType) {
        int i = AnonymousClass30.$SwitchMap$com$artifex$sonui$editor$SignatureStyle$SignatureStyleType[signatureStyleType.ordinal()];
        if (i == 1) {
            this.drawSignatureButton.setVisibility(View.VISIBLE);
            this.chooseImageButton.setVisibility(View.GONE);
            findViewById(R.id.choose_button_wrapper).setVisibility(View.INVISIBLE);
            this.styleTypeTextButton.setChecked(true);
        } else if (i == 2) {
            this.drawSignatureButton.setVisibility(View.VISIBLE);
            this.chooseImageButton.setVisibility(View.GONE);
            findViewById(R.id.choose_button_wrapper).setVisibility(View.VISIBLE);
            this.styleTypeDrawButton.setChecked(true);
        } else if (i == 3) {
            this.drawSignatureButton.setVisibility(View.GONE);
            this.chooseImageButton.setVisibility(View.VISIBLE);
            findViewById(R.id.choose_button_wrapper).setVisibility(View.VISIBLE);
            this.styleTypeImageButton.setChecked(true);
        } else if (i == 4) {
            this.drawSignatureButton.setVisibility(View.VISIBLE);
            this.chooseImageButton.setVisibility(View.GONE);
            findViewById(R.id.choose_button_wrapper).setVisibility(View.INVISIBLE);
            this.styleTypeNoneButton.setChecked(true);
        }
    }

    public final void createLayout() {
        setContentView(R.layout.sodk_editor_signature);
        final View findViewById = findViewById(R.id.sodk_editor_signature_outer);
        findViewById.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                findViewById.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                SignatureDialog signatureDialog = SignatureDialog.this;
                signatureDialog.sign_panel = (RelativeLayout) signatureDialog.findViewById(R.id.sign_panel);
                SignatureDialog signatureDialog2 = SignatureDialog.this;
                signatureDialog2.edit_panel = (RelativeLayout) signatureDialog2.findViewById(R.id.edit_panel);
                SignatureDialog signatureDialog3 = SignatureDialog.this;
                signatureDialog3.sign_preview = (ImageView) signatureDialog3.findViewById(R.id.sign_preview);
                SignatureDialog signatureDialog4 = SignatureDialog.this;
                signatureDialog4.edit_preview = (ImageView) signatureDialog4.findViewById(R.id.edit_preview);
                SignatureStyle.loadStyles(SignatureDialog.this.context);
                if (!SignatureDialog.this.mayUseImages && SignatureStyle.getCurrentStyle().type == SignatureStyle.SignatureStyleType.SignatureStyleType_Image) {
                    SignatureStyle.setCurrentStyle(0);
                }
                SignatureDialog.this.sign_panel.post(new Runnable() {
                    public void run() {
                        SignatureDialog signatureDialog = SignatureDialog.this;
                        SignatureDialog signatureDialog2 = SignatureDialog.currentDialog;
                        ((TextView) signatureDialog.findViewById(R.id.sign_as_text)).setText(signatureDialog.signer.name().cn);
                        ((ImageButton) signatureDialog.findViewById(R.id.sign_back)).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.this.dismiss();
                            }
                        });
                        signatureDialog.setOnDismissListener(new OnDismissListener() {
                            public void onDismiss(DialogInterface dialogInterface) {
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                if (!signatureDialog.signPressed) {
                                    signatureDialog.listener.onCancel();
                                }
                            }
                        });
                        ((Button) signatureDialog.findViewById(R.id.sign)).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                signatureDialog.signPressed = true;
                                signatureDialog.dismiss();
                                SignatureAppearance appearance = SignatureStyle.getCurrentStyle().toAppearance(SignatureDialog.this.getContext());
                                SignatureDialog signatureDialog2 = SignatureDialog.this;
                                appearance.location = signatureDialog2.getFieldText(signatureDialog2.editLocation);
                                SignatureDialog signatureDialog3 = SignatureDialog.this;
                                appearance.reason = signatureDialog3.getFieldText(signatureDialog3.editReason);
                                SignatureDialog.this.listener.onSign(appearance);
                            }
                        });
                        ((ImageButton) signatureDialog.findViewById(R.id.edit_back)).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                SignatureDialog signatureDialog2 = SignatureDialog.currentDialog;
                                signatureDialog.showSignPanel();
                            }
                        });
                        Button button = (Button) signatureDialog.findViewById(R.id.edit);
                        signatureDialog.editButton = button;
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.this.styleEditing = SignatureStyle.copyCurrentStyle();
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                signatureDialog.sign_panel.setVisibility(View.GONE);
                                signatureDialog.edit_panel.setVisibility(View.VISIBLE);
                                SignatureDialog signatureDialog2 = SignatureDialog.this;
                                signatureDialog2.setEditUIFromStyle(signatureDialog2.styleEditing);
                            }
                        });
                        Button button2 = (Button) signatureDialog.findViewById(R.id.save);
                        signatureDialog.saveButton = button2;
                        button2.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                signatureDialog.styleEditing.styleName = signatureDialog.editStyleName.getText().toString().trim();
                                int i = AnonymousClass30.$SwitchMap$com$artifex$sonui$editor$SignatureStyle$SignatureStyleType[SignatureDialog.this.styleEditing.type.ordinal()];
                                if (i == 1) {
                                    SignatureStyle signatureStyle = SignatureDialog.this.styleEditing;
                                    signatureStyle.leftDrawing = null;
                                    signatureStyle.leftImage = null;
                                } else if (i == 2) {
                                    SignatureDialog.this.styleEditing.leftImage = null;
                                } else if (i == 3) {
                                    SignatureDialog.this.styleEditing.leftDrawing = null;
                                } else if (i == 4) {
                                    SignatureStyle signatureStyle2 = SignatureDialog.this.styleEditing;
                                    signatureStyle2.leftDrawing = null;
                                    signatureStyle2.leftImage = null;
                                }
                                SignatureStyle.saveStyle(SignatureDialog.this.styleEditing, true);
                                SignatureStyle.saveStyles(SignatureDialog.this.context);
                                SignatureDialog.this.showSignPanel();
                            }
                        });
                        signatureDialog.editLocation = (SOEditText) signatureDialog.findViewById(R.id.sign_location);
                        signatureDialog.editReason = (SOEditText) signatureDialog.findViewById(R.id.sign_reason);
                        signatureDialog.editLocation.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable editable) {
                            }

                            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            }

                            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                                SignatureDialog.access$1900(SignatureDialog.this);
                            }
                        });
                        signatureDialog.editReason.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable editable) {
                            }

                            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            }

                            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                                SignatureDialog.access$1900(SignatureDialog.this);
                            }
                        });
                        signatureDialog.appearanceNameText = (TextView) signatureDialog.findViewById(R.id.appearance_name);
                        Spinner spinner = (Spinner) signatureDialog.findViewById(R.id.style_picker);
                        signatureDialog.styleSpinner = spinner;
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public int lastPosition = -1;

                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                                if (i != this.lastPosition) {
                                    SignatureStyle.setCurrentStyle(i);
                                    SignatureStyle.saveStyles(SignatureDialog.this.getContext());
                                    SignatureDialog.this.styleSpinner.clearFocus();
                                    SignatureDialog.this.showSignPanel();
                                }
                                this.lastPosition = i;
                            }

                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        String[] names = SignatureStyle.getNames(signatureDialog.mayUseImages);
                        Context context = signatureDialog.context;
                        int i = R.layout.sodk_editor_sigstyle_spinner_item;
                        ArrayAdapter arrayAdapter = new ArrayAdapter(context, i, names);
                        arrayAdapter.setDropDownViewResource(i);
                        signatureDialog.styleSpinner.setAdapter(arrayAdapter);
                        CheckBox checkBox = (CheckBox) signatureDialog.findViewById(R.id.cb_name);
                        signatureDialog.cbName = checkBox;
                        checkBox.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        CheckBox checkBox2 = (CheckBox) signatureDialog.findViewById(R.id.cd_distinguished_name);
                        signatureDialog.cbDistinguishedName = checkBox2;
                        checkBox2.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        CheckBox checkBox3 = (CheckBox) signatureDialog.findViewById(R.id.cb_date);
                        signatureDialog.cbDate = checkBox3;
                        checkBox3.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        CheckBox checkBox4 = (CheckBox) signatureDialog.findViewById(R.id.cb_location);
                        signatureDialog.cbLocation = checkBox4;
                        checkBox4.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        CheckBox checkBox5 = (CheckBox) signatureDialog.findViewById(R.id.cb_reason);
                        signatureDialog.cbReason = checkBox5;
                        checkBox5.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        CheckBox checkBox6 = (CheckBox) signatureDialog.findViewById(R.id.cb_labels);
                        signatureDialog.cbLabels = checkBox6;
                        checkBox6.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        CheckBox checkBox7 = (CheckBox) signatureDialog.findViewById(R.id.cb_logo);
                        signatureDialog.cbLogo = checkBox7;
                        checkBox7.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        Button button3 = (Button) signatureDialog.findViewById(R.id.graphic_draw_choose);
                        signatureDialog.drawSignatureButton = button3;
                        button3.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                new DrawSignatureDialog(SignatureDialog.this.getContext(), new DrawSignatureDialog.DrawSignatureListener() {
                                    public void onCancel() {
                                    }

                                    public void onSave(Bitmap bitmap) {
                                        String str = SignatureStyle.getSigDirPath(SignatureDialog.this.context) + "drawn_" + UUID.randomUUID().toString() + ".png";
                                        SOOutputStream sOOutputStream = new SOOutputStream(str);
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, sOOutputStream);
                                        sOOutputStream.flush();
                                        sOOutputStream.close();
                                        SignatureDialog signatureDialog = SignatureDialog.this;
                                        SignatureStyle signatureStyle = signatureDialog.styleEditing;
                                        signatureStyle.type = SignatureStyle.SignatureStyleType.SignatureStyleType_Draw;
                                        signatureStyle.leftDrawing = str;
                                        SignatureDialog.access$2000(signatureDialog);
                                    }
                                }).show();
                            }
                        });
                        Button button4 = (Button) signatureDialog.findViewById(R.id.graphic_image_choose);
                        signatureDialog.chooseImageButton = button4;
                        button4.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                SignatureDialog signatureDialog2 = SignatureDialog.currentDialog;
                                Objects.requireNonNull(signatureDialog);
                                NUIDocView.currentNUIDocView().launchGetImage("image/png,image/jpg,image/jpeg,image/tiff,image/gif,image/bmp", signatureDialog.selectImageListener);
                            }
                        });
                        RadioButton radioButton = (RadioButton) signatureDialog.findViewById(R.id.graphic_text);
                        signatureDialog.styleTypeTextButton = radioButton;
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                SignatureStyle signatureStyle = signatureDialog.styleEditing;
                                SignatureStyle.SignatureStyleType signatureStyleType = SignatureStyle.SignatureStyleType.SignatureStyleType_Text;
                                signatureStyle.type = signatureStyleType;
                                signatureDialog.configureStyleTypeButtons(signatureStyleType);
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        RadioButton radioButton2 = (RadioButton) signatureDialog.findViewById(R.id.graphic_draw);
                        signatureDialog.styleTypeDrawButton = radioButton2;
                        radioButton2.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                SignatureStyle signatureStyle = signatureDialog.styleEditing;
                                SignatureStyle.SignatureStyleType signatureStyleType = SignatureStyle.SignatureStyleType.SignatureStyleType_Draw;
                                signatureStyle.type = signatureStyleType;
                                signatureDialog.configureStyleTypeButtons(signatureStyleType);
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        RadioButton radioButton3 = (RadioButton) signatureDialog.findViewById(R.id.graphic_image);
                        signatureDialog.styleTypeImageButton = radioButton3;
                        radioButton3.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                SignatureStyle signatureStyle = signatureDialog.styleEditing;
                                SignatureStyle.SignatureStyleType signatureStyleType = SignatureStyle.SignatureStyleType.SignatureStyleType_Image;
                                signatureStyle.type = signatureStyleType;
                                signatureDialog.configureStyleTypeButtons(signatureStyleType);
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        RadioButton radioButton4 = (RadioButton) signatureDialog.findViewById(R.id.graphic_none);
                        signatureDialog.styleTypeNoneButton = radioButton4;
                        radioButton4.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                SignatureStyle signatureStyle = signatureDialog.styleEditing;
                                SignatureStyle.SignatureStyleType signatureStyleType = SignatureStyle.SignatureStyleType.SignatureStyleType_None;
                                signatureStyle.type = signatureStyleType;
                                signatureDialog.configureStyleTypeButtons(signatureStyleType);
                                SignatureDialog.access$2000(SignatureDialog.this);
                            }
                        });
                        signatureDialog.styleTypeRadioGroup = (RadioGroup) signatureDialog.findViewById(R.id.edit_radio_group);
                        ((Button) signatureDialog.findViewById(R.id.create)).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                SignatureDialog.this.styleEditing = new SignatureStyle();
                                SignatureDialog.this.styleEditing.setToDefault();
                                SignatureStyle signatureStyle = SignatureDialog.this.styleEditing;
                                StringBuilder m = new StringBuilder("Created ");
                                m.append(new Date().toString());
                                signatureStyle.styleName = m.toString();
                                SignatureDialog signatureDialog = SignatureDialog.this;
                                signatureDialog.sign_panel.setVisibility(View.GONE);
                                signatureDialog.edit_panel.setVisibility(View.VISIBLE);
                                SignatureDialog signatureDialog2 = SignatureDialog.this;
                                signatureDialog2.setEditUIFromStyle(signatureDialog2.styleEditing);
                            }
                        });
                        SOEditText sOEditText = (SOEditText) signatureDialog.findViewById(R.id.edit_style_name);
                        signatureDialog.editStyleName = sOEditText;
                        sOEditText.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable editable) {
                            }

                            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            }

                            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                                if (SignatureDialog.this.editStyleName.getText().toString().trim().length() == 0) {
                                    SignatureDialog.this.saveButton.setEnabled(false);
                                } else {
                                    SignatureDialog.this.saveButton.setEnabled(true);
                                }
                            }
                        });
                        InputFilter r1 = (charSequence, i1, i2, spanned, i3, i4) -> {
                            StringBuilder sb = new StringBuilder(i2 - i1);
                            boolean z = true;
                            for (int i5 = i1; i5 < i2; i5++) {
                                char charAt = charSequence.charAt(i5);
                                if (charAt != 10) {
                                    sb.append(charAt);
                                } else {
                                    z = false;
                                }
                            }
                            if (z) {
                                return null;
                            }
                            if (!(charSequence instanceof Spanned)) {
                                return sb;
                            }
                            SpannableString spannableString = new SpannableString(sb);
                            TextUtils.copySpansFrom((Spanned) charSequence, i1, sb.length(), (Class) null, spannableString, 0);
                            return spannableString;
                        };
                        signatureDialog.editStyleName.setFilters(new InputFilter[]{r1});
                        if (!signatureDialog.mayUseImages) {
                            signatureDialog.styleTypeImageButton.setVisibility(View.GONE);
                        }
                        if (signatureDialog.signShowing) {
                            signatureDialog.showSignPanel();
                        } else {
                            signatureDialog.sign_panel.setVisibility(View.GONE);
                            signatureDialog.edit_panel.setVisibility(View.VISIBLE);
                        }
                        signatureDialog.setOnKeyListener((dialogInterface, i12, keyEvent) -> {
                            if (i12 != 4 || SignatureDialog.this.edit_panel.getVisibility() != View.VISIBLE) {
                                return false;
                            }
                            SignatureDialog.this.showSignPanel();
                            return true;
                        });
                        SignatureDialog signatureDialog3 = SignatureDialog.this;
                        if (signatureDialog3.shouldRestore) {
                            if (signatureDialog3.signShowing) {
                                signatureDialog3.editLocation.setText(signatureDialog3.savedLocation);
                                signatureDialog3.editReason.setText(signatureDialog3.savedReason);
                                signatureDialog3.styleSpinner.setSelection(signatureDialog3.savedSpinnerIndex);
                                signatureDialog3.setPreview(SignatureStyle.getCurrentStyle(), signatureDialog3.sign_preview);
                            } else {
                                signatureDialog3.setEditUIFromStyle(signatureDialog3.styleEditing);
                                signatureDialog3.setPreview(signatureDialog3.styleEditing, signatureDialog3.edit_preview);
                            }
                            signatureDialog3.shouldRestore = false;
                        }
                    }
                });
            }
        });
    }

    public void dismiss() {
        currentDialog = null;
        super.dismiss();
    }

    public final String getFieldText(SOEditText sOEditText) {
        String trim = sOEditText.getText().toString().trim();
        return trim.isEmpty() ? getContext().getString(R.string.sodk_editor_signature_unknown) : trim;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        createLayout();
    }

    public final void setEditUIFromStyle(SignatureStyle signatureStyle) {
        this.cbName.setChecked(signatureStyle.name);
        this.cbDistinguishedName.setChecked(signatureStyle.dn);
        this.cbDate.setChecked(signatureStyle.date);
        this.cbLocation.setChecked(signatureStyle.location);
        this.cbReason.setChecked(signatureStyle.reason);
        this.cbLabels.setChecked(signatureStyle.labels);
        this.cbLogo.setChecked(signatureStyle.logo);
        setPreview(this.styleEditing, this.edit_preview);
        this.editStyleName.setText(this.styleEditing.styleName);
        this.styleTypeRadioGroup.clearCheck();
        configureStyleTypeButtons(signatureStyle.type);
    }

    public final void setPreview(SignatureStyle signatureStyle, ImageView imageView) {
        setPreview(signatureStyle.toAppearance(getContext()), imageView);
    }

    public void show() {
        super.show();
        currentDialog = this;
    }

    public final void showSignPanel() {
        this.sign_panel.setVisibility(View.VISIBLE);
        this.edit_panel.setVisibility(View.GONE);
        SignatureStyle currentStyle = SignatureStyle.getCurrentStyle();
        int currentStyleIndex = SignatureStyle.getCurrentStyleIndex();
        setPreview(currentStyle, this.sign_preview);
        if (currentStyle.editable) {
            this.editButton.setVisibility(View.VISIBLE);
        } else {
            this.editButton.setVisibility(View.GONE);
        }
        if (currentStyle.location) {
            findViewById(R.id.sign_location_row).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.sign_location_row).setVisibility(View.GONE);
        }
        if (currentStyle.reason) {
            findViewById(R.id.sign_reason_row).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.sign_reason_row).setVisibility(View.GONE);
        }
        this.appearanceNameText.setVisibility(View.GONE);
        String[] names = SignatureStyle.getNames(this.mayUseImages);
        Context context2 = this.context;
        int i = R.layout.sodk_editor_sigstyle_spinner_item;
        ArrayAdapter arrayAdapter = new ArrayAdapter(context2, i, names);
        arrayAdapter.setDropDownViewResource(i);
        this.styleSpinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        String str = SignatureStyle.getCurrentStyle().styleName;
        for (int i2 = 0; i2 < names.length; i2++) {
            if (names[i2].compareToIgnoreCase(str) == 0) {
                currentStyleIndex = i2;
            }
        }
        this.styleSpinner.setSelection(currentStyleIndex);
        SignatureStyle.cleanup(this.context);
    }

    public final void setPreview(SignatureAppearance signatureAppearance, final ImageView imageView) {
        Rect rect = new Rect(0, 0, imageView.getWidth(), imageView.getHeight());
        MuPDFWidget muPDFWidget = this.widget;
        muPDFWidget.mDoc.mWorker.add(new Worker.Task() {
            public Bitmap bmp;
            public final /* synthetic */ SignatureAppearance val$appearance = signatureAppearance;
            public final /* synthetic */ MuPDFWidget.RenderAppearanceListener val$listener = new MuPDFWidget.RenderAppearanceListener() {};
            public final /* synthetic */ Rect val$rect = rect;
            public final /* synthetic */ PKCS7Signer val$signer = signer;

            public void run() {}
/*
Method generation error in method: com.artifex.solib.MuPDFWidget.6.run():void, dex: classes.dex
            jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFWidget.6.run():void, class status: UNLOADED
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

            public void work(){}
/*
Method generation error in method: com.artifex.solib.MuPDFWidget.6.work():void, dex: classes.dex
            jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFWidget.6.work():void, class status: UNLOADED
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
    }
}
