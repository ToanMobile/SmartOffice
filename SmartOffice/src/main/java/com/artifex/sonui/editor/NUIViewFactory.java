package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import com.artifex.solib.ArDkUtils;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SOLib;
import com.artifex.solib.SmartOfficeDocType;

public class NUIViewFactory {

    /* renamed from: com.artifex.sonui.editor.NUIViewFactory$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$artifex$solib$SmartOfficeDocType;

        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.artifex.solib.SmartOfficeDocType[] r0 = com.artifex.solib.SmartOfficeDocType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$artifex$solib$SmartOfficeDocType = r0
                com.artifex.solib.SmartOfficeDocType r1 = com.artifex.solib.SmartOfficeDocType.SmartOfficeDocType_XLS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$artifex$solib$SmartOfficeDocType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.artifex.solib.SmartOfficeDocType r1 = com.artifex.solib.SmartOfficeDocType.SmartOfficeDocType_XLSX     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$artifex$solib$SmartOfficeDocType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.artifex.solib.SmartOfficeDocType r1 = com.artifex.solib.SmartOfficeDocType.SmartOfficeDocType_DOC     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$artifex$solib$SmartOfficeDocType     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.artifex.solib.SmartOfficeDocType r1 = com.artifex.solib.SmartOfficeDocType.SmartOfficeDocType_DOCX     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$artifex$solib$SmartOfficeDocType     // Catch:{ NoSuchFieldError -> 0x003e }
                com.artifex.solib.SmartOfficeDocType r1 = com.artifex.solib.SmartOfficeDocType.SmartOfficeDocType_ODT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$artifex$solib$SmartOfficeDocType     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.artifex.solib.SmartOfficeDocType r1 = com.artifex.solib.SmartOfficeDocType.SmartOfficeDocType_PPT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$artifex$solib$SmartOfficeDocType     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.artifex.solib.SmartOfficeDocType r1 = com.artifex.solib.SmartOfficeDocType.SmartOfficeDocType_PPTX     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$com$artifex$solib$SmartOfficeDocType     // Catch:{ NoSuchFieldError -> 0x0060 }
                com.artifex.solib.SmartOfficeDocType r1 = com.artifex.solib.SmartOfficeDocType.SmartOfficeDocType_IMG     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.NUIViewFactory.AnonymousClass1.<clinit>():void");
        }
    }

    public static Activity getActivity(Context context) {
        if (context == null || !(context instanceof ContextWrapper)) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return getActivity(((ContextWrapper) context).getBaseContext());
    }

    public static NUIDocView makeNUIView(String str, Context context) {
        NUIDocView nUIDocView;
        SOLib lib;
        Activity activity = getActivity(context);
        if (!(activity == null || (lib = SOLib.getLib(activity)) == null)) {
            switch (AnonymousClass1.$SwitchMap$com$artifex$solib$SmartOfficeDocType[SmartOfficeDocType.values()[lib.getDocTypeFromFileContents(str)].ordinal()]) {
                case 1:
                case 2:
                    nUIDocView = new NUIDocViewXls(context);
                    break;
                case 3:
                case 4:
                case 5:
                    nUIDocView = new NUIDocViewDoc(context);
                    break;
                case 6:
                case 7:
                    nUIDocView = new NUIDocViewPpt(context);
                    break;
                case 8:
                    nUIDocView = new NUIDocViewOther(context);
                    break;
            }
        }
        nUIDocView = null;
        if (nUIDocView != null) {
            return nUIDocView;
        }
        String extension = FileUtils.getExtension(str);
        boolean z = false;
        if (FileUtils.matchFileExtension(extension, ArDkUtils.XLS_TYPES) || FileUtils.matchFileExtension(extension, ArDkUtils.XLSX_TYPES)) {
            return new NUIDocViewXls(context);
        }
        String extension2 = FileUtils.getExtension(str);
        if (FileUtils.matchFileExtension(extension2, ArDkUtils.PPT_TYPES) || FileUtils.matchFileExtension(extension2, ArDkUtils.PPTX_TYPES)) {
            return new NUIDocViewPpt(context);
        }
        String extension3 = FileUtils.getExtension(str);
        if (FileUtils.matchFileExtension(extension3, ArDkUtils.DOC_TYPES) || FileUtils.matchFileExtension(extension3, ArDkUtils.DOCX_TYPES) || FileUtils.matchFileExtension(extension3, ArDkUtils.ODT_TYPES)) {
            z = true;
        }
        if (z) {
            return new NUIDocViewDoc(context);
        }
        return (nUIDocView != null || !FileUtils.getExtension(str).equalsIgnoreCase("gif")) ? nUIDocView : new NUIDocViewOther(context);
    }
}
