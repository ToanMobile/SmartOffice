package com.artifex.mupdf.fitz;

public class PDFWidget extends PDFAnnotation {
    public static final int PDF_BTN_FIELD_IS_NO_TOGGLE_TO_OFF = 16384;
    public static final int PDF_BTN_FIELD_IS_PUSHBUTTON = 65536;
    public static final int PDF_BTN_FIELD_IS_RADIO = 32768;
    public static final int PDF_CH_FIELD_IS_COMBO = 131072;
    public static final int PDF_CH_FIELD_IS_EDIT = 262144;
    public static final int PDF_CH_FIELD_IS_MULTI_SELECT = 2097152;
    public static final int PDF_CH_FIELD_IS_SORT = 524288;
    public static final int PDF_FIELD_IS_NO_EXPORT = 4;
    public static final int PDF_FIELD_IS_READ_ONLY = 1;
    public static final int PDF_FIELD_IS_REQUIRED = 2;
    public static final int PDF_SIGNATURE_DEFAULT_APPEARANCE = 63;
    public static final int PDF_SIGNATURE_SHOW_DATE = 4;
    public static final int PDF_SIGNATURE_SHOW_DN = 2;
    public static final int PDF_SIGNATURE_SHOW_GRAPHIC_NAME = 16;
    public static final int PDF_SIGNATURE_SHOW_LABELS = 1;
    public static final int PDF_SIGNATURE_SHOW_LOGO = 32;
    public static final int PDF_SIGNATURE_SHOW_TEXT_NAME = 8;
    public static final int PDF_TX_FIELD_IS_COMB = 16777216;
    public static final int PDF_TX_FIELD_IS_MULTILINE = 4096;
    public static final int PDF_TX_FIELD_IS_PASSWORD = 8192;
    public static final int TX_FORMAT_DATE = 3;
    public static final int TX_FORMAT_NONE = 0;
    public static final int TX_FORMAT_NUMBER = 1;
    public static final int TX_FORMAT_SPECIAL = 2;
    public static final int TX_FORMAT_TIME = 4;
    public static final int TYPE_BUTTON = 1;
    public static final int TYPE_CHECKBOX = 2;
    public static final int TYPE_COMBOBOX = 3;
    public static final int TYPE_LISTBOX = 4;
    public static final int TYPE_RADIOBUTTON = 5;
    public static final int TYPE_SIGNATURE = 6;
    public static final int TYPE_TEXT = 7;
    public static final int TYPE_UNKNOWN = 0;
    private int fieldFlags;
    private int fieldType;
    private int maxLen;
    private String[] options;
    private String originalValue;
    private int textFormat;

    public static class TextWidgetCharLayout {
        public float advance;
        public int index;
        public Rect rect;
        public float x;
    }

    public static class TextWidgetLayout {
        public Matrix invMatrix;
        public TextWidgetLineLayout[] lines;
        public Matrix matrix;
    }

    public static class TextWidgetLineLayout {
        public TextWidgetCharLayout[] chars;
        public float fontSize;
        public int index;
        public Rect rect;
        public float x;
        public float y;
    }

    static {
        Context.init();
    }

    public PDFWidget(long j) {
        super(j);
    }

    public static Pixmap previewSignature(int i, int i2, int i3, PKCS7Signer pKCS7Signer, int i4, Image image, String str, String str2) {
        return previewSignatureNative(i, i2, i3, pKCS7Signer, i4, image, str, str2);
    }

    private static native Pixmap previewSignatureNative(int i, int i2, int i3, PKCS7Signer pKCS7Signer, int i4, Image image, String str, String str2);

    private native boolean signNative(PKCS7Signer pKCS7Signer, int i, Image image, String str, String str2);

    public void cancelEditing() {
        setValue(this.originalValue);
        setEditing(false);
    }

    public native int checkCertificate(PKCS7Verifier pKCS7Verifier);

    public native int checkDigest(PKCS7Verifier pKCS7Verifier);

    public native void clearSignature();

    public boolean commitEditing(String str) {
        setValue(this.originalValue);
        setEditing(false);
        if (setTextValue(str)) {
            return true;
        }
        setEditing(true);
        return false;
    }

    public native PKCS7DistinguishedName getDistinguishedName(PKCS7Verifier pKCS7Verifier);

    public int getFieldFlags() {
        return this.fieldFlags;
    }

    public int getFieldType() {
        return this.fieldType;
    }

    public native String getLabel();

    public int getMaxLen() {
        return this.maxLen;
    }

    public String[] getOptions() {
        return this.options;
    }

    public int getTextFormat() {
        return this.textFormat;
    }

    public native String getValue();

    public native boolean incrementalChangeAfterSigning();

    public boolean isButton() {
        int fieldType2 = getFieldType();
        return fieldType2 == 1 || fieldType2 == 2 || fieldType2 == 5;
    }

    public boolean isCheckbox() {
        return getFieldType() == 2;
    }

    public boolean isChoice() {
        int fieldType2 = getFieldType();
        return fieldType2 == 3 || fieldType2 == 4;
    }

    public boolean isComb() {
        return (getFieldFlags() & PDF_TX_FIELD_IS_COMB) != 0;
    }

    public boolean isComboBox() {
        return getFieldType() == 3;
    }

    public native boolean isEditing();

    public boolean isListBox() {
        return getFieldType() == 4;
    }

    public boolean isMultiline() {
        return (getFieldFlags() & 4096) != 0;
    }

    public boolean isPassword() {
        return (getFieldFlags() & 8192) != 0;
    }

    public boolean isPushButton() {
        return getFieldType() == 1;
    }

    public boolean isRadioButton() {
        return getFieldType() == 5;
    }

    public boolean isReadOnly() {
        return (getFieldFlags() & 1) != 0;
    }

    public native boolean isSigned();

    public boolean isText() {
        return getFieldType() == 7;
    }

    public native TextWidgetLayout layoutTextWidget();

    public native boolean setChoiceValue(String str);

    public native void setEditing(boolean z);

    public native boolean setTextValue(String str);

    public native boolean setValue(String str);

    public boolean sign(PKCS7Signer pKCS7Signer, int i, Image image, String str, String str2) {
        return signNative(pKCS7Signer, i, image, str, str2);
    }

    public void startEditing() {
        setEditing(true);
        this.originalValue = getValue();
    }

    public native Quad[] textQuads();

    public native boolean toggle();

    public native int validateSignature();

    public boolean verify(PKCS7Verifier pKCS7Verifier) {
        if (checkDigest(pKCS7Verifier) == 0 && checkCertificate(pKCS7Verifier) == 0) {
            return !incrementalChangeAfterSigning();
        }
        return false;
    }

    public static Pixmap previewSignature(int i, int i2, int i3, PKCS7Signer pKCS7Signer, Image image) {
        return previewSignatureNative(i, i2, i3, pKCS7Signer, 63, image, (String) null, (String) null);
    }

    public boolean sign(PKCS7Signer pKCS7Signer, Image image) {
        return signNative(pKCS7Signer, 63, image, (String) null, (String) null);
    }

    public static Pixmap previewSignature(int i, int i2, int i3, PKCS7Signer pKCS7Signer) {
        return previewSignatureNative(i, i2, i3, pKCS7Signer, 63, (Image) null, (String) null, (String) null);
    }

    public boolean sign(PKCS7Signer pKCS7Signer) {
        return signNative(pKCS7Signer, 63, (Image) null, (String) null, (String) null);
    }

    public static Pixmap previewSignature(int i, int i2, PKCS7Signer pKCS7Signer, Image image) {
        return previewSignatureNative(i, i2, 0, pKCS7Signer, 63, image, (String) null, (String) null);
    }

    public static Pixmap previewSignature(int i, int i2, PKCS7Signer pKCS7Signer) {
        return previewSignatureNative(i, i2, 0, pKCS7Signer, 63, (Image) null, (String) null, (String) null);
    }

    public Pixmap previewSignature(float f, PKCS7Signer pKCS7Signer, int i, Image image, String str, String str2) {
        Rect bounds = getBounds();
        float f2 = f / 72.0f;
        return previewSignature(Math.round((bounds.x1 - bounds.x0) * f2), Math.round((bounds.x1 - bounds.x0) * f2), getLanguage(), pKCS7Signer, i, image, str, str2);
    }

    public Pixmap previewSignature(float f, PKCS7Signer pKCS7Signer, Image image) {
        Rect bounds = getBounds();
        float f2 = f / 72.0f;
        return previewSignature(Math.round((bounds.x1 - bounds.x0) * f2), Math.round((bounds.x1 - bounds.x0) * f2), getLanguage(), pKCS7Signer, image);
    }

    public Pixmap previewSignature(float f, PKCS7Signer pKCS7Signer) {
        Rect bounds = getBounds();
        float f2 = f / 72.0f;
        return previewSignature(Math.round((bounds.x1 - bounds.x0) * f2), Math.round((bounds.x1 - bounds.x0) * f2), getLanguage(), pKCS7Signer);
    }
}
