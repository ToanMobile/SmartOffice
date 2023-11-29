package com.artifex.sonui.editor;

import android.content.Context;
import androidx.fragment.app.BackStackRecord$$ExternalSyntheticOutline0;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SOPreferences;
import com.artifex.solib.SignatureAppearance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SignatureStyle {
    public static ArrayList<SignatureStyle> styles = new ArrayList<>();
    public boolean current;
    public boolean date;
    public boolean dn;
    public boolean editable;
    public boolean labels;
    public String leftDrawing;
    public String leftImage;
    public boolean leftText;
    public boolean location;
    public boolean logo;
    public boolean name;
    public boolean reason;
    public String styleName;
    public SignatureStyleType type;

    /* renamed from: com.artifex.sonui.editor.SignatureStyle$3  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$artifex$sonui$editor$SignatureStyle$SignatureStyleType;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.artifex.sonui.editor.SignatureStyle$SignatureStyleType[] r0 = com.artifex.sonui.editor.SignatureStyle.SignatureStyleType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$artifex$sonui$editor$SignatureStyle$SignatureStyleType = r0
                com.artifex.sonui.editor.SignatureStyle$SignatureStyleType r1 = com.artifex.sonui.editor.SignatureStyle.SignatureStyleType.SignatureStyleType_Text     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$artifex$sonui$editor$SignatureStyle$SignatureStyleType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.artifex.sonui.editor.SignatureStyle$SignatureStyleType r1 = com.artifex.sonui.editor.SignatureStyle.SignatureStyleType.SignatureStyleType_Draw     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$artifex$sonui$editor$SignatureStyle$SignatureStyleType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.artifex.sonui.editor.SignatureStyle$SignatureStyleType r1 = com.artifex.sonui.editor.SignatureStyle.SignatureStyleType.SignatureStyleType_Image     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$artifex$sonui$editor$SignatureStyle$SignatureStyleType     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.artifex.sonui.editor.SignatureStyle$SignatureStyleType r1 = com.artifex.sonui.editor.SignatureStyle.SignatureStyleType.SignatureStyleType_None     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.SignatureStyle.AnonymousClass3.<clinit>():void");
        }
    }

    public enum SignatureStyleType {
        SignatureStyleType_None,
        SignatureStyleType_Text,
        SignatureStyleType_Draw,
        SignatureStyleType_Image
    }

    public SignatureStyle() {
        setToDefault();
    }

    public static void cleanup(Context context) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < styles.size(); i++) {
            SignatureStyle signatureStyle = styles.get(i);
            String str = signatureStyle.leftImage;
            if (str != null) {
                arrayList.add(str);
            }
            String str2 = signatureStyle.leftDrawing;
            if (str2 != null) {
                arrayList.add(str2);
            }
        }
        File[] listFiles = new File(getSigDirPath(context)).listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File absolutePath : listFiles) {
                String absolutePath2 = absolutePath.getAbsolutePath();
                if (!arrayList.contains(absolutePath2)) {
                    FileUtils.deleteFile(absolutePath2);
                }
            }
        }
    }

    public static SignatureStyle copy(SignatureStyle signatureStyle) {
        String json = new Gson().toJson((Object) signatureStyle);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.prettyPrinting = true;
        return (SignatureStyle) gsonBuilder.create().fromJson(json, new TypeToken<SignatureStyle>() {
        }.getType());
    }

    public static SignatureStyle copyCurrentStyle() {
        int currentStyleIndex = getCurrentStyleIndex();
        if (currentStyleIndex < 0 || currentStyleIndex >= styles.size()) {
            return null;
        }
        return copy(styles.get(currentStyleIndex));
    }

    public static SignatureStyle getCurrentStyle() {
        int currentStyleIndex = getCurrentStyleIndex();
        if (currentStyleIndex < 0 || currentStyleIndex >= styles.size()) {
            return null;
        }
        return styles.get(currentStyleIndex);
    }

    public static int getCurrentStyleIndex() {
        ArrayList<SignatureStyle> arrayList = styles;
        if (arrayList == null || arrayList.size() <= 0) {
            return -1;
        }
        for (int i = 0; i < styles.size(); i++) {
            if (styles.get(i).current) {
                return i;
            }
        }
        return -1;
    }

    public static String[] getNames(boolean z) {
        ArrayList arrayList = new ArrayList();
        ArrayList<SignatureStyle> arrayList2 = styles;
        if (arrayList2 != null && arrayList2.size() > 0) {
            for (int i = 0; i < styles.size(); i++) {
                SignatureStyle signatureStyle = styles.get(i);
                if (z || signatureStyle.type != SignatureStyleType.SignatureStyleType_Image) {
                    arrayList.add(signatureStyle.styleName);
                }
            }
        }
        if (arrayList.size() == 0) {
            return null;
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String getSigDirPath(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtils.getTempPathRoot(context));
        String str = File.separator;
        String m = BackStackRecord$$ExternalSyntheticOutline0.m(sb, str, ".signatures", str);
        if (!FileUtils.fileExists(m)) {
            FileUtils.createDirectory(m);
        }
        return m;
    }

    public static SignatureStyle getStyle(String str) {
        int styleIndex = getStyleIndex(str);
        if (styleIndex >= 0) {
            return styles.get(styleIndex);
        }
        return null;
    }

    public static int getStyleIndex(String str) {
        ArrayList<SignatureStyle> arrayList = styles;
        if (arrayList == null || arrayList.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < styles.size(); i++) {
            String str2 = styles.get(i).styleName;
            if (str2 != null && str2.compareToIgnoreCase(str) == 0) {
                return i;
            }
        }
        return -1;
    }

    public static void loadStyles(Context context) {
        String stringPreference = SOPreferences.getStringPreference(SOPreferences.getPreferencesObject(context, "general"), "signatureStyles", "");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.prettyPrinting = true;
        ArrayList<SignatureStyle> arrayList = (ArrayList) gsonBuilder.create().fromJson(stringPreference, new TypeToken<List<SignatureStyle>>() {
        }.getType());
        styles = arrayList;
        if (arrayList == null) {
            styles = new ArrayList<>();
        }
        if (styles.size() == 0) {
            SignatureStyle signatureStyle = new SignatureStyle();
            signatureStyle.current = true;
            signatureStyle.styleName = "Standard Text";
            signatureStyle.editable = false;
            styles.add(signatureStyle);
        }
    }

    public static void saveStyle(SignatureStyle signatureStyle, boolean z) {
        int styleIndex = getStyleIndex(signatureStyle.styleName);
        SignatureStyle copy = copy(signatureStyle);
        if (styleIndex < 0) {
            styles.add(copy);
        } else {
            styles.set(styleIndex, copy);
        }
        if (z) {
            setCurrentStyle(getStyleIndex(signatureStyle.styleName));
        }
    }

    public static void saveStyles(Context context) {
        SOPreferences.setStringPreference(SOPreferences.getPreferencesObject(context, "general"), "signatureStyles", new Gson().toJson((Object) styles));
    }

    public static void setCurrentStyle(int i) {
        ArrayList<SignatureStyle> arrayList = styles;
        if (arrayList != null && i >= 0 && i < arrayList.size()) {
            for (int i2 = 0; i2 < styles.size(); i2++) {
                SignatureStyle signatureStyle = styles.get(i2);
                if (i2 == i) {
                    signatureStyle.current = true;
                } else {
                    signatureStyle.current = false;
                }
            }
        }
    }

    public void setToDefault() {
        this.type = SignatureStyleType.SignatureStyleType_Text;
        this.styleName = null;
        this.leftDrawing = null;
        this.leftImage = null;
        this.leftText = true;
        this.name = true;
        this.dn = true;
        this.location = false;
        this.date = true;
        this.reason = false;
        this.labels = true;
        this.logo = true;
        this.current = false;
        this.editable = true;
    }

    public SignatureAppearance toAppearance(Context context) {
        SignatureAppearance signatureAppearance = new SignatureAppearance();
        signatureAppearance.location = this.location ? context.getString(R.string.sodk_editor_signature_unknown) : null;
        signatureAppearance.reason = this.reason ? context.getString(R.string.sodk_editor_signature_unknown) : null;
        signatureAppearance.showDate = this.date;
        signatureAppearance.showDn = this.dn;
        signatureAppearance.showLabels = this.labels;
        signatureAppearance.showLogo = this.logo;
        signatureAppearance.showName = this.name;
        int i = AnonymousClass3.$SwitchMap$com$artifex$sonui$editor$SignatureStyle$SignatureStyleType[this.type.ordinal()];
        if (i == 1) {
            signatureAppearance.showLeftText = true;
        } else if (i == 2) {
            signatureAppearance.showLeftImage = true;
            signatureAppearance.imagePath = this.leftDrawing;
        } else if (i == 3) {
            signatureAppearance.showLeftImage = true;
            signatureAppearance.imagePath = this.leftImage;
        } else if (i == 4) {
            signatureAppearance.imagePath = null;
        }
        return signatureAppearance;
    }
}
