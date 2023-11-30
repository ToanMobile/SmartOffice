package com.artifex.sonui.editor;

import com.artifex.source.util.a.util_a.a.a.c$$ExternalSyntheticOutline0;
import com.artifex.source.util.a.util_a.a.b.f.a$$ExternalSyntheticOutline0;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticOutline0;
import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompatWrapper$$ExternalSyntheticOutline0;
import androidx.navigation.NavDeepLink$$ExternalSyntheticOutline0;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkUtils;
import com.artifex.solib.FileUtils;
import com.artifex.solib.MuPDFDoc;
import com.artifex.solib.SODoc;
import com.artifex.solib.Waiter;
import com.artifex.sonui.editor.SODocSession;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Utilities {
    public static final Set<String> RTL;
    public static AlertDialog currentMessageDialog;
    public static SODataLeakHandlers mDataLeakHandlers;
    public static String mFileStateForPrint;
    public static MessageHandler mMessageHandler;
    public static OCRDataLoader mOCRDataLoader = null;
    public static SODocSession.SODocSessionLoadListenerCustom mSessionLoadListener;
    public static SigningFactoryListener mSigningFactory;

    public interface MessageHandler {
        void showMessage(String str, String str2, String str3, Runnable runnable);

        void yesNoMessage(String str, String str2, String str3, String str4, Runnable runnable, Runnable runnable2);
    }

    public interface OCRDataLoadListener {
        void done(boolean z, String str, String str2, String str3);
    }

    public interface OCRDataLoader {
        void load(Context context, OCRDataLoadListener oCRDataLoadListener);
    }

    public interface SigningFactoryListener {
        NUIPKCS7Signer getSigner(Activity activity);

        NUIPKCS7Verifier getVerifier(Activity activity);
    }

    public interface passwordDialogListener {
        void onCancel();

        void onOK(String str);
    }

    static {
        HashSet hashSet = new HashSet();
        hashSet.add("ar");
        hashSet.add("dv");
        hashSet.add("fa");
        hashSet.add("ha");
        hashSet.add("he");
        hashSet.add("iw");
        hashSet.add("ji");
        hashSet.add("ps");
        hashSet.add("ur");
        hashSet.add("yi");
        RTL = Collections.unmodifiableSet(hashSet);
    }

    public static int colorForDocExt(Context context, String str) {
        if (FileUtils.matchFileExtension(str, ArDkUtils.DOC_TYPES) || FileUtils.matchFileExtension(str, ArDkUtils.DOCX_TYPES)) {
            return ContextCompat.getColor(context, R.color.sodk_editor_header_doc_color);
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.XLS_TYPES) || FileUtils.matchFileExtension(str, ArDkUtils.XLSX_TYPES)) {
            return ContextCompat.getColor(context, R.color.sodk_editor_header_xls_color);
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.PPT_TYPES) || FileUtils.matchFileExtension(str, ArDkUtils.PPTX_TYPES)) {
            return ContextCompat.getColor(context, R.color.sodk_editor_header_ppt_color);
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.IMG_TYPES) || FileUtils.matchFileExtension(str, ArDkUtils.SO_IMG_TYPES)) {
            return ContextCompat.getColor(context, R.color.sodk_editor_header_image_color);
        }
        Objects.requireNonNull(str);
        char c = 65535;
        switch (str.hashCode()) {
            case 98299:
                if (str.equals("cbz")) {
                    c = 0;
                    break;
                }
                break;
            case 98822:
                if (str.equals("csv")) {
                    c = 1;
                    break;
                }
                break;
            case 101110:
                if (str.equals("fb2")) {
                    c = 2;
                    break;
                }
                break;
            case 103745:
                if (str.equals("hwp")) {
                    c = 3;
                    break;
                }
                break;
            case 110834:
                if (str.equals("pdf")) {
                    c = 4;
                    break;
                }
                break;
            case 114276:
                if (str.equals("svg")) {
                    c = 5;
                    break;
                }
                break;
            case 115312:
                if (str.equals("txt")) {
                    c = 6;
                    break;
                }
                break;
            case 118907:
                if (str.equals("xps")) {
                    c = 7;
                    break;
                }
                break;
            case 3120248:
                if (str.equals("epub")) {
                    c = 8;
                    break;
                }
                break;
            case 114035747:
                if (str.equals("xhtml")) {
                    c = 9;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return ContextCompat.getColor(context, R.color.sodk_editor_header_cbz_color);
            case 1:
            case 6:
                return ContextCompat.getColor(context, R.color.sodk_editor_header_txt_color);
            case 2:
            case 9:
                return ContextCompat.getColor(context, R.color.sodk_editor_header_fb2_color);
            case 3:
                return ContextCompat.getColor(context, R.color.sodk_editor_header_hwp_color);
            case 4:
                return ContextCompat.getColor(context, R.color.sodk_editor_header_pdf_color);
            case 5:
                return ContextCompat.getColor(context, R.color.sodk_editor_header_svg_color);
            case 7:
                return ContextCompat.getColor(context, R.color.sodk_editor_header_xps_color);
            case 8:
                return ContextCompat.getColor(context, R.color.sodk_editor_header_epub_color);
            default:
                return ContextCompat.getColor(context, R.color.sodk_editor_header_unknown_color);
        }
    }

    public static int colorForDocType(Context context, String str) {
        return colorForDocExt(context, FileUtils.getExtension(str));
    }

    public static boolean compareRects(Rect rect, Rect rect2) {
        if (rect == null) {
            return rect2 == null;
        }
        if (rect2 == null) {
            return false;
        }
        return rect.equals(rect2);
    }

    public static int convertDpToPixel(float f) {
        return Math.round((((float) Resources.getSystem().getDisplayMetrics().densityDpi) / 160.0f) * f);
    }

    public static ProgressDialog createAndShowWaitSpinner(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException unused) {
        }
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        progressDialog.setContentView(R.layout.sodk_editor_wait_spinner);
        return progressDialog;
    }

    public static void didCrashOnPreviousExecution(final Activity activity, final String str, final int i, final Runnable runnable) {
        Class<FirebaseCrashlytics> cls = FirebaseCrashlytics.class;
        try {
            Object invoke = cls.getMethod("didCrashOnPreviousExecution", (Class[]) null).invoke(cls.getMethod("getInstance", (Class[]) null).invoke((Object) null, (Object[]) null), (Object[]) null);
            Class<?> cls2 = Class.forName(str + ".MainAppRegular");
            Object invoke2 = cls2.getMethod("ignoringCrashes", (Class[]) null).invoke(activity.getApplication(), (Object[]) null);
            if (((Boolean) invoke).booleanValue() && !((Boolean) invoke2).booleanValue()) {
                cls2.getMethod("setIgnoreDidCrash", (Class[]) null).invoke(activity.getApplication(), (Object[]) null);
                yesNoMessage(activity, activity.getResources().getString(R.string.sodk_editor_crash_report_title), activity.getResources().getString(R.string.sodk_editor_crash_report), activity.getResources().getString(R.string.sodk_editor_report), activity.getResources().getString(R.string.sodk_editor_cancel), new Runnable() {
                    public void run() {
                        Utilities.showSupportActivity(activity, str, i);
                    }
                }, new Runnable() {
                    public void run() {
                        Runnable runnable = runnable;
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
            } else if (runnable != null) {
                runnable.run();
            }
        } catch (Exception unused) {
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public static void dismissCurrentAlert() {
        AlertDialog alertDialog = currentMessageDialog;
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (Exception unused) {
            }
            currentMessageDialog = null;
        }
    }

    public static ProgressDialog displayPleaseWaitWithCancel(Context context, final Runnable runnable) {
        ProgressDialogDelayed progressDialogDelayed = new ProgressDialogDelayed(context, 1000);
        progressDialogDelayed.setIndeterminate(true);
        progressDialogDelayed.setCancelable(false);
        progressDialogDelayed.setTitle(context.getString(R.string.sodk_editor_please_wait));
        progressDialogDelayed.setMessage((CharSequence) null);
        if (runnable != null) {
            progressDialogDelayed.setButton(-2, context.getString(R.string.sodk_editor_cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    runnable.run();
                }
            });
        }
        progressDialogDelayed.show();
        return progressDialogDelayed;
    }

    public static String formatDateForLocale(Context context, String str, String str2) {
        DateFormat dateFormat;
        try {
            Date parse = new SimpleDateFormat(str2).parse(str);
            String string = Settings.System.getString(context.getContentResolver(), "date_format");
            if (TextUtils.isEmpty(string)) {
                dateFormat = android.text.format.DateFormat.getDateFormat(context);
            } else {
                dateFormat = new SimpleDateFormat(string);
            }
            String format = dateFormat.format(parse);
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
            return format + " " + timeFormat.format(parse);
        } catch (Exception unused) {
            return str;
        }
    }

    public static String formatFloat(float f) {
        if (f % 1.0f == BitmapDescriptorFactory.HUE_RED) {
            return String.format("%.0f", new Object[]{Float.valueOf(f)});
        }
        String d = Double.toString(Math.abs((double) f));
        return String.format(LocaleListCompatWrapper$$ExternalSyntheticOutline0.m("%.", (d.length() - d.indexOf(46)) - 1, "f"), new Object[]{Float.valueOf(f)});
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int i = applicationInfo.labelRes;
        if (i == 0) {
            return applicationInfo.nonLocalizedLabel.toString();
        }
        return context.getString(i);
    }

    public static SODataLeakHandlers getDataLeakHandlers() {
        return mDataLeakHandlers;
    }

    public static File getDownloadDirectory(Context context) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    public static String getFileStateForPrint() {
        return mFileStateForPrint;
    }

    public static String getHtmlColorStringFromResource(int i, Context context) {
        String hexString = Integer.toHexString(context.getResources().getColor(i));
        int length = hexString.length();
        return length > 6 ? a$$ExternalSyntheticOutline0.m("#", hexString.substring(length - 6)) : "rgba(0, 0, 0, 0);";
    }

    public static int getListViewWidth(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < adapter.getCount(); i2++) {
            View view = adapter.getView(i2, (View) null, listView);
            view.measure(0, 0);
            i = Math.max(i, view.getMeasuredWidth());
        }
        return i;
    }

    public static String getMimeType(String str) {
        String extension = FileUtils.getExtension(str);
        String str2 = null;
        String mimeTypeFromExtension = extension != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) : null;
        if (mimeTypeFromExtension != null) {
            return mimeTypeFromExtension;
        }
        String extension2 = FileUtils.getExtension(str);
        if (extension2.compareToIgnoreCase("xps") == 0) {
            str2 = "application/vnd.ms-xpsdocument";
        }
        if (extension2.compareToIgnoreCase("cbz") == 0) {
            str2 = "application/x-cbz";
        }
        return extension2.compareToIgnoreCase("svg") == 0 ? "image/svg+xml" : str2;
    }

    public static OCRDataLoader getOCRLoader() {
        return mOCRDataLoader;
    }

    public static String getOpenErrorDescription(Context context, int i) {
        if (i == 5) {
            return context.getString(R.string.sodk_editor_doc_uses_unsupported_enc);
        }
        return String.format(context.getString(R.string.sodk_editor_doc_open_error), new Object[]{Integer.valueOf(i)});
    }

    public static Point getRealScreenSize(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static File getRootDirectory(Context context) {
        return Environment.getExternalStorageDirectory();
    }

    public static String getSDCardPath(Context context) {
        File[] externalFilesDirs = context.getExternalFilesDirs((String) null);
        Log.d("sdcard", String.format("getSDCardPath: there are %d external locations", new Object[]{Integer.valueOf(externalFilesDirs.length)}));
        if (externalFilesDirs.length <= 1 || externalFilesDirs[1] == null) {
            Log.d("sdcard", String.format("getSDCardPath: too few external locations", new Object[0]));
        } else {
            String absolutePath = externalFilesDirs[1].getAbsolutePath();
            if (absolutePath != null) {
                Log.d("sdcard", String.format("getSDCardPath: possible sd card path is %s", new Object[]{absolutePath}));
                int indexOf = absolutePath.toLowerCase().indexOf("/android/");
                if (indexOf > 0) {
                    String substring = absolutePath.substring(0, indexOf);
                    Log.d("sdcard", String.format("getSDCardPath: SD card is at %s", new Object[]{substring}));
                    return substring;
                }
                Log.d("sdcard", String.format("getSDCardPath: did not find /Android/ in %s", new Object[]{absolutePath}));
            } else {
                Log.d("sdcard", String.format("getSDCardPath: 2nd path is null", new Object[0]));
            }
        }
        return null;
    }

    public static Point getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(VisionController.WINDOW)).getDefaultDisplay().getMetrics(displayMetrics);
        return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static String getSelectionFontName(ArDkDoc arDkDoc) {
        String selectionFontName = ((SODoc) arDkDoc).getSelectionFontName();
        if (selectionFontName != null) {
            return (selectionFontName.startsWith("-") || selectionFontName.startsWith("+")) ? "" : selectionFontName;
        }
        return selectionFontName;
    }

    public static SODocSession.SODocSessionLoadListenerCustom getSessionLoadListener() {
        return mSessionLoadListener;
    }

    public static NUIPKCS7Signer getSigner(Activity activity) {
        SigningFactoryListener signingFactoryListener = mSigningFactory;
        if (signingFactoryListener != null) {
            return signingFactoryListener.getSigner(activity);
        }
        return null;
    }

    public static NUIPKCS7Verifier getVerifier(Activity activity) {
        SigningFactoryListener signingFactoryListener = mSigningFactory;
        if (signingFactoryListener != null) {
            return signingFactoryListener.getVerifier(activity);
        }
        return null;
    }

    public static void hideKeyboard(Context context) {
        if (Activity.class.isInstance(context)) {
            Activity activity = (Activity) context;
            ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.findViewById(16908290).getRootView().getWindowToken(), 0);
        }
    }

    public static void hideWaitDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (IllegalArgumentException unused) {
            }
        }
    }

    public static int iconForDocExt(String str) {
        if (FileUtils.matchFileExtension(str, ArDkUtils.DOC_TYPES)) {
            return R.drawable.sodk_editor_icon_doc;
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.DOCX_TYPES)) {
            return R.drawable.sodk_editor_icon_docx;
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.XLS_TYPES)) {
            return R.drawable.sodk_editor_icon_xls;
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.XLSX_TYPES)) {
            return R.drawable.sodk_editor_icon_xlsx;
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.PPT_TYPES)) {
            return R.drawable.sodk_editor_icon_ppt;
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.PPTX_TYPES)) {
            return R.drawable.sodk_editor_icon_pptx;
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.ODT_TYPES)) {
            return R.drawable.sodk_editor_icon_odt;
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.IMG_TYPES)) {
            return R.drawable.sodk_editor_icon_image;
        }
        if (FileUtils.matchFileExtension(str, ArDkUtils.SO_IMG_TYPES)) {
            return R.drawable.sodk_editor_icon_image;
        }
        Objects.requireNonNull(str);
        char c = 65535;
        switch (str.hashCode()) {
            case 98299:
                if (str.equals("cbz")) {
                    c = 0;
                    break;
                }
                break;
            case 98822:
                if (str.equals("csv")) {
                    c = 1;
                    break;
                }
                break;
            case 101110:
                if (str.equals("fb2")) {
                    c = 2;
                    break;
                }
                break;
            case 103745:
                if (str.equals("hwp")) {
                    c = 3;
                    break;
                }
                break;
            case 110834:
                if (str.equals("pdf")) {
                    c = 4;
                    break;
                }
                break;
            case 114276:
                if (str.equals("svg")) {
                    c = 5;
                    break;
                }
                break;
            case 115312:
                if (str.equals("txt")) {
                    c = 6;
                    break;
                }
                break;
            case 118907:
                if (str.equals("xps")) {
                    c = 7;
                    break;
                }
                break;
            case 3120248:
                if (str.equals("epub")) {
                    c = 8;
                    break;
                }
                break;
            case 114035747:
                if (str.equals("xhtml")) {
                    c = 9;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return R.drawable.sodk_editor_icon_cbz;
            case 1:
            case 6:
                return R.drawable.sodk_editor_icon_txt;
            case 2:
            case 9:
                return R.drawable.sodk_editor_icon_fb2;
            case 3:
                return R.drawable.sodk_editor_icon_hangul;
            case 4:
                return R.drawable.sodk_editor_icon_pdf;
            case 5:
                return R.drawable.sodk_editor_icon_svg;
            case 7:
                return R.drawable.sodk_editor_icon_xps;
            case 8:
                return R.drawable.sodk_editor_icon_epub;
            default:
                return R.drawable.sodk_editor_icon_any;
        }
    }

    public static int iconForDocType(String str) {
        return iconForDocExt(FileUtils.getExtension(str));
    }

    public static int inchesToPixels(Context context, float f) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        return (int) (((float) displayMetrics.densityDpi) * f);
    }

    public static boolean isChromebook(Context context) {
        return context.getPackageManager().hasSystemFeature("org.chromium.arc.device_management");
    }

    public static boolean isDocTypeSupported(String str) {
        String extension = FileUtils.getExtension(str);
        if (!FileUtils.matchFileExtension(extension, ArDkUtils.DOC_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.DOCX_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.ODT_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.XLS_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.XLSX_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.PPT_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.PPTX_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.MUPDF_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.IMG_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.SO_IMG_TYPES) && !FileUtils.matchFileExtension(extension, ArDkUtils.SO_OTHER_TYPES)) {
            return false;
        }
        return true;
    }

    public static boolean isEmulator() {
        String str = Build.HARDWARE;
        if (!"goldfish".equals(str) && !"ranchu".equals(str) && !Build.FINGERPRINT.contains("generic")) {
            return false;
        }
        return true;
    }

    public static boolean isLandscapePhone(Context context) {
        if (!isPhoneDevice(context)) {
            return false;
        }
        Point realScreenSize = getRealScreenSize(context);
        return realScreenSize.x > realScreenSize.y;
    }

    public static boolean isPermissionRequested(Context context, String str) {
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr != null) {
                for (String equals : strArr) {
                    if (equals.equals(str)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPhoneDevice(Context context) {
        if (isChromebook(context)) {
            return false;
        }
        Configuration configuration = context.getResources().getConfiguration();
        int i = configuration.screenWidthDp;
        if (configuration.smallestScreenWidthDp < context.getResources().getInteger(R.integer.sodk_editor_minimum_tablet_width)) {
            return true;
        }
        return false;
    }

    public static boolean isRTL(Context context) {
        String str;
        InputMethodSubtype currentInputMethodSubtype = ((InputMethodManager) context.getSystemService("input_method")).getCurrentInputMethodSubtype();
        if (currentInputMethodSubtype == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            str = currentInputMethodSubtype.getLanguageTag();
        } else {
            str = currentInputMethodSubtype.getLocale();
        }
        return RTL.contains(str);
    }

    public static Date iso8601ToDate(String str) {
        String replaceFirst = str.replaceFirst("[Zz]", "+00:00");
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(replaceFirst.substring(0, 22) + replaceFirst.substring(23));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static void launchUrl(Context context, String str) {
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = a$$ExternalSyntheticOutline0.m("http://", str);
        }
        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }

    public static void passwordDialog(final Activity activity, final passwordDialogListener passworddialoglistener) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Utilities.dismissCurrentAlert();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.sodk_editor_alert_dialog_style);
                View inflate = LayoutInflater.from(activity).inflate(R.layout.sodk_editor_password_prompt, (ViewGroup) null);
                builder.setCancelable(false);
                final SOEditText sOEditText = (SOEditText) inflate.findViewById(R.id.editTextDialogUserInput);
                builder.setView(inflate);
                builder.setTitle("");
                builder.setPositiveButton(activity.getResources().getString(R.string.sodk_editor_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utilities.hideKeyboard(activity, sOEditText);
                        dialogInterface.dismiss();
                        Utilities.currentMessageDialog = null;
                        if (passworddialoglistener != null) {
                            passworddialoglistener.onOK(sOEditText.getText().toString());
                        }
                    }
                });
                builder.setNegativeButton(activity.getResources().getString(R.string.sodk_editor_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utilities.hideKeyboard(activity, sOEditText);
                        dialogInterface.dismiss();
                        Utilities.currentMessageDialog = null;
                        passwordDialogListener passworddialoglistener = passworddialoglistener;
                        if (passworddialoglistener != null) {
                            passworddialoglistener.onCancel();
                        }
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() != 1 || i != 66) {
                            return false;
                        }
                        Utilities.hideKeyboard(activity, sOEditText);
                        Utilities.dismissCurrentAlert();
                        if (passworddialoglistener != null) {
                            passworddialoglistener.onOK(sOEditText.getText().toString());
                        }
                        return true;
                    }
                });
                AlertDialog create = builder.create();
                Utilities.currentMessageDialog = create;
                create.show();
            }
        });
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x005d A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0080 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:58:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String preInsertImage(Context r13, String r14) {
        /*
            com.artifex.solib.SOInputStream r0 = new com.artifex.solib.SOInputStream
            r0.<init>(r14)
            r1 = 0
            r2 = 1
            r3 = 0
            android.graphics.BitmapFactory$Options r4 = new android.graphics.BitmapFactory$Options     // Catch:{ Exception -> 0x001f }
            r4.<init>()     // Catch:{ Exception -> 0x001f }
            r4.inJustDecodeBounds = r2     // Catch:{ Exception -> 0x001f }
            android.graphics.BitmapFactory.decodeStream(r0, r1, r4)     // Catch:{ Exception -> 0x001f }
            int r5 = r4.outHeight     // Catch:{ Exception -> 0x001f }
            int r4 = r4.outWidth     // Catch:{ Exception -> 0x001a }
            r0.close()
            goto L_0x0028
        L_0x001a:
            r4 = move-exception
            goto L_0x0021
        L_0x001c:
            r13 = move-exception
            goto L_0x00ea
        L_0x001f:
            r4 = move-exception
            r5 = 0
        L_0x0021:
            r4.printStackTrace()     // Catch:{ all -> 0x001c }
            r0.close()
            r4 = 0
        L_0x0028:
            com.artifex.solib.SOInputStream r0 = new com.artifex.solib.SOInputStream
            r0.<init>(r14)
            androidx.exifinterface.media.ExifInterface r6 = new androidx.exifinterface.media.ExifInterface     // Catch:{ IOException -> 0x0055, all -> 0x0050 }
            r6.<init>((java.io.InputStream) r0)     // Catch:{ IOException -> 0x0055, all -> 0x0050 }
            java.lang.String r7 = "Orientation"
            int r2 = r6.getAttributeInt(r7, r2)     // Catch:{ IOException -> 0x0055, all -> 0x0050 }
            r6 = 3
            if (r2 == r6) goto L_0x004a
            r6 = 6
            if (r2 == r6) goto L_0x0047
            r6 = 8
            if (r2 == r6) goto L_0x0044
            r2 = 0
            goto L_0x004c
        L_0x0044:
            r2 = 270(0x10e, float:3.78E-43)
            goto L_0x004c
        L_0x0047:
            r2 = 90
            goto L_0x004c
        L_0x004a:
            r2 = 180(0xb4, float:2.52E-43)
        L_0x004c:
            r0.close()
            goto L_0x0059
        L_0x0050:
            r13 = move-exception
            r0.close()
            throw r13
        L_0x0055:
            r0.close()
            r2 = 0
        L_0x0059:
            r0 = 1536(0x600, float:2.152E-42)
            if (r2 != 0) goto L_0x0061
            if (r4 > r0) goto L_0x0061
            if (r5 <= r0) goto L_0x00e5
        L_0x0061:
            com.artifex.solib.SOInputStream r6 = new com.artifex.solib.SOInputStream
            r6.<init>(r14)
            android.graphics.BitmapFactory$Options r7 = new android.graphics.BitmapFactory$Options     // Catch:{ Exception -> 0x0079 }
            r7.<init>()     // Catch:{ Exception -> 0x0079 }
            android.graphics.Bitmap$Config r8 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ Exception -> 0x0079 }
            r7.inPreferredConfig = r8     // Catch:{ Exception -> 0x0079 }
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeStream(r6)     // Catch:{ Exception -> 0x0079 }
        L_0x0073:
            r6.close()
            goto L_0x007e
        L_0x0077:
            r13 = move-exception
            goto L_0x00e6
        L_0x0079:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ all -> 0x0077 }
            goto L_0x0073
        L_0x007e:
            if (r1 == 0) goto L_0x00e5
            if (r4 > r0) goto L_0x0084
            if (r5 <= r0) goto L_0x0098
        L_0x0084:
            float r14 = (float) r0
            float r0 = (float) r4
            float r4 = r14 / r0
            float r5 = (float) r5
            float r14 = r14 / r5
            float r14 = java.lang.Math.min(r4, r14)
            float r0 = r0 * r14
            int r4 = (int) r0
            float r14 = r14 * r5
            int r5 = (int) r14
            android.graphics.Bitmap r1 = android.graphics.Bitmap.createScaledBitmap(r1, r4, r5, r3)
        L_0x0098:
            r6 = r1
            r9 = r4
            r10 = r5
            if (r2 == 0) goto L_0x00ad
            android.graphics.Matrix r11 = new android.graphics.Matrix
            r11.<init>()
            float r14 = (float) r2
            r11.preRotate(r14)
            r7 = 0
            r8 = 0
            r12 = 1
            android.graphics.Bitmap r6 = android.graphics.Bitmap.createBitmap(r6, r7, r8, r9, r10, r11, r12)
        L_0x00ad:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r13 = com.artifex.solib.FileUtils.getTempPathRoot(r13)
            r14.append(r13)
            java.lang.String r13 = java.io.File.separator
            r14.append(r13)
            java.lang.String r13 = "scaled_or_rotated_"
            r14.append(r13)
            java.util.UUID r13 = java.util.UUID.randomUUID()
            r14.append(r13)
            java.lang.String r13 = ".png"
            r14.append(r13)
            java.lang.String r14 = r14.toString()
            com.artifex.solib.SOOutputStream r13 = new com.artifex.solib.SOOutputStream
            r13.<init>(r14)
            android.graphics.Bitmap$CompressFormat r0 = android.graphics.Bitmap.CompressFormat.PNG
            r1 = 100
            r6.compress(r0, r1, r13)
            r13.flush()
            r13.close()
        L_0x00e5:
            return r14
        L_0x00e6:
            r6.close()
            throw r13
        L_0x00ea:
            r0.close()
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.Utilities.preInsertImage(android.content.Context, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000c, code lost:
        r1 = r0.getName();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String removeExtension(String r4) {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r4)
            boolean r1 = r0.isDirectory()
            if (r1 == 0) goto L_0x000c
            return r4
        L_0x000c:
            java.lang.String r1 = r0.getName()
            r2 = 46
            int r2 = r1.lastIndexOf(r2)
            if (r2 > 0) goto L_0x0019
            return r4
        L_0x0019:
            java.io.File r4 = new java.io.File
            java.lang.String r0 = r0.getParent()
            r3 = 0
            java.lang.String r1 = r1.substring(r3, r2)
            r4.<init>(r0, r1)
            java.lang.String r4 = r4.getPath()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.Utilities.removeExtension(java.lang.String):java.lang.String");
    }

    public static int[] screenToWindow(int[] iArr, Context context) {
        int[] iArr2 = new int[2];
        ((Activity) context).getWindow().getDecorView().getLocationInWindow(iArr2);
        return new int[]{iArr[0] - iArr2[0], iArr[1] - iArr2[1]};
    }

    public static void setDataLeakHandlers(SODataLeakHandlers sODataLeakHandlers) {
        mDataLeakHandlers = sODataLeakHandlers;
    }

    public static void setFileStateForPrint(String str) {
        mFileStateForPrint = str;
    }

    public static void setFilenameText(SOTextView sOTextView, String str) {
        String str2;
        Context context = sOTextView.getContext();
        int lastIndexOf = str.lastIndexOf(".");
        String htmlColorStringFromResource = getHtmlColorStringFromResource(R.color.sodk_editor_filename_textcolor, context);
        String htmlColorStringFromResource2 = getHtmlColorStringFromResource(R.color.sodk_editor_extension_textcolor, context);
        if (lastIndexOf >= 0) {
            StringBuilder m = ActivityResultRegistry$$ExternalSyntheticOutline0.m("<font color='", htmlColorStringFromResource, "'>");
            m.append(str.substring(0, lastIndexOf));
            m.append("</font><font color='");
            m.append(htmlColorStringFromResource2);
            m.append("'>");
            m.append(str.substring(lastIndexOf));
            m.append("</font>");
            str2 = m.toString();
        } else {
            str2 = NavDeepLink$$ExternalSyntheticOutline0.m("<font color='", htmlColorStringFromResource, "'>", str, "</font>");
        }
        if (Build.VERSION.SDK_INT >= 24) {
            sOTextView.setText(Html.fromHtml(str2, 0), TextView.BufferType.SPANNABLE);
        } else {
            sOTextView.setText(Html.fromHtml(str2), TextView.BufferType.SPANNABLE);
        }
    }

    public static void setMessageHandler(MessageHandler messageHandler) {
        mMessageHandler = messageHandler;
    }

    public static void setOCRDataLoader(OCRDataLoader oCRDataLoader) {
        mOCRDataLoader = oCRDataLoader;
    }

    public static void setSessionLoadListener(SODocSession.SODocSessionLoadListenerCustom sODocSessionLoadListenerCustom) {
        mSessionLoadListener = sODocSessionLoadListenerCustom;
    }

    public static void setSigningFactoryListener(SigningFactoryListener signingFactoryListener) {
        mSigningFactory = signingFactoryListener;
    }

    public static void showKeyboard(final Context context) {
        new Handler().post(new Runnable() {
            public void run() {
                ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(2, 1);
            }
        });
    }

    public static void showMessage(Activity activity, String str, String str2) {
        showMessage(activity, str, str2, activity.getResources().getString(R.string.sodk_editor_ok));
    }

    public static void showMessageAndFinish(final Activity activity, final String str, final String str2) {
        if (mMessageHandler != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Utilities.mMessageHandler.showMessage(str, str2, activity.getResources().getString(R.string.sodk_editor_ok), new Runnable() {
                        public void run() {
                            activity.finish();
                        }
                    });
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Utilities.dismissCurrentAlert();
                    AlertDialog create = new AlertDialog.Builder(activity, R.style.sodk_editor_alert_dialog_style).setTitle(str).setMessage(str2).setCancelable(false).setPositiveButton(R.string.sodk_editor_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Utilities.currentMessageDialog = null;
                            activity.finish();
                        }
                    }).create();
                    Utilities.currentMessageDialog = create;
                    create.show();
                }
            });
        }
    }

    public static void showMessageAndWait(final Activity activity, final String str, final String str2, int i, final Runnable runnable) {
        if (mMessageHandler != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Utilities.mMessageHandler.showMessage(str, str2, activity.getResources().getString(R.string.sodk_editor_ok), runnable);
                }
            });
            return;
        }
        final Activity activity2 = activity;
        final int i2 = i;
        final String str3 = str;
        final String str4 = str2;
        final Runnable runnable2 = runnable;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Utilities.dismissCurrentAlert();
                AlertDialog create = new AlertDialog.Builder(activity2, i2).setTitle(str3).setMessage(str4).setCancelable(false).setPositiveButton(R.string.sodk_editor_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Utilities.currentMessageDialog = null;
                        runnable2.run();
                    }
                }).create();
                Utilities.currentMessageDialog = create;
                create.show();
            }
        });
    }

    public static MuPDFDoc.JsEventListener.AlertResult showPDFAlert(Activity activity, String str, String str2, int i, int i2, boolean z, String str3, boolean z2) {
        MuPDFDoc.JsEventListener.AlertResult alertResult = new MuPDFDoc.JsEventListener.AlertResult();
        alertResult.buttonPressed = 0;
        final boolean z3 = z2;
        alertResult.checkboxChecked = z3;
        Waiter waiter = new Waiter(0);
        final Activity activity2 = activity;
        final String str4 = str;
        final String str5 = str2;
        final int i3 = i;
        final int i4 = i2;
        final MuPDFDoc.JsEventListener.AlertResult alertResult2 = alertResult;
        final Waiter waiter2 = waiter;
        final boolean z4 = z;
        final String str6 = str3;
        Activity activity3 = activity;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Utilities.dismissCurrentAlert();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity2, R.style.sodk_editor_alert_dialog_style);
                builder.setTitle(str4);
                builder.setMessage(str5);
                builder.setCancelable(false);
                int i = i3;
                if (i == 0) {
                    builder.setIcon(R.drawable.sodk_editor_icon_pdf_alert_error);
                } else if (i == 1) {
                    builder.setIcon(R.drawable.sodk_editor_icon_pdf_alert_warning);
                } else if (i == 2) {
                    builder.setIcon(R.drawable.sodk_editor_icon_pdf_alert_question);
                } else if (i == 3) {
                    builder.setIcon(R.drawable.sodk_editor_icon_pdf_alert_information);
                }
                int i2 = i4;
                if (i2 == 0 || i2 == 1) {
                    builder.setPositiveButton(R.string.sodk_editor_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Utilities.currentMessageDialog = null;
                            AnonymousClass3 r2 = AnonymousClass3.this;
                            alertResult2.buttonPressed = 1;
                            waiter2.done();
                        }
                    });
                }
                int i3 = i4;
                if (i3 == 2 || i3 == 3) {
                    builder.setPositiveButton(R.string.sodk_editor_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Utilities.currentMessageDialog = null;
                            AnonymousClass3 r2 = AnonymousClass3.this;
                            alertResult2.buttonPressed = 4;
                            waiter2.done();
                        }
                    });
                }
                int i4 = i4;
                if (i4 == 2 || i4 == 3) {
                    builder.setNegativeButton(R.string.sodk_editor_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Utilities.currentMessageDialog = null;
                            AnonymousClass3 r2 = AnonymousClass3.this;
                            alertResult2.buttonPressed = 3;
                            waiter2.done();
                        }
                    });
                }
                int i5 = i4;
                if (i5 == 1 || i5 == 3) {
                    builder.setNeutralButton(R.string.sodk_editor_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Utilities.currentMessageDialog = null;
                            AnonymousClass3 r2 = AnonymousClass3.this;
                            alertResult2.buttonPressed = 2;
                            waiter2.done();
                        }
                    });
                }
                if (z4) {
                    View inflate = View.inflate(builder.getContext(), R.layout.sodk_editor_pdf_alert_checkbox, (ViewGroup) null);
                    CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.checkbox);
                    checkBox.setText(str6);
                    checkBox.setChecked(z3);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                            alertResult2.checkboxChecked = z;
                        }
                    });
                    builder.setView(inflate);
                }
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        Utilities.currentMessageDialog = null;
                        waiter2.done();
                    }
                });
                AlertDialog create = builder.create();
                Utilities.currentMessageDialog = create;
                create.show();
            }
        });
        waiter.doWait();
        return alertResult;
    }

    public static void showSupportActivity(Activity activity, String str, int i) {
        try {
            Class.forName(str + ".DoSupportActivity").getMethod("show", new Class[]{Activity.class, Integer.TYPE}).invoke((Object) null, new Object[]{activity, Integer.valueOf(i)});
        } catch (Exception e) {
            StringBuilder m = c$$ExternalSyntheticOutline0.m("Failed to show Support activity: ");
            m.append(e.toString());
            Log.e("Utilities", m.toString());
        }
    }

    public static ProgressDialog showWaitDialog(Context context, String str, boolean z) {
        dismissCurrentAlert();
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.sodk_editor_alert_dialog_style);
        progressDialog.setMessage(str);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        if (z) {
            Window window = progressDialog.getWindow();
            window.setFlags(8, 8);
            window.clearFlags(2);
        }
        progressDialog.show();
        return progressDialog;
    }

    public static void yesNoMessage(Activity activity, String str, String str2, String str3, String str4, Runnable runnable, Runnable runnable2) {
        if (mMessageHandler != null) {
            final String str5 = str;
            final String str6 = str2;
            final String str7 = str3;
            final String str8 = str4;
            final Runnable runnable3 = runnable;
            final Runnable runnable4 = runnable2;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Utilities.mMessageHandler.yesNoMessage(str5, str6, str7, str8, runnable3, runnable4);
                }
            });
            return;
        }
        final Activity activity2 = activity;
        final String str9 = str;
        final String str10 = str2;
        final String str11 = str3;
        final Runnable runnable5 = runnable;
        final String str12 = str4;
        final Runnable runnable6 = runnable2;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Utilities.dismissCurrentAlert();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity2, R.style.sodk_editor_alert_dialog_style);
                builder.setTitle(str9);
                builder.setMessage(str10);
                builder.setCancelable(false);
                builder.setPositiveButton(str11, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Utilities.currentMessageDialog = null;
                        Runnable runnable = runnable5;
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
                builder.setNegativeButton(str12, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Utilities.currentMessageDialog = null;
                        Runnable runnable = runnable6;
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
                AlertDialog create = builder.create();
                Utilities.currentMessageDialog = create;
                create.show();
            }
        });
    }

    public static void showMessage(final Activity activity, final String str, final String str2, final String str3) {
        if (mMessageHandler != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        Utilities.mMessageHandler.showMessage(str, str2, str3, (Runnable) null);
                    } catch (Exception unused) {
                        Log.w("sonui", "The message handler registered with Utilities.setMessageHandler encountered a problem. Falling back to the default message handler.");
                        Activity activity = activity;
                        String str = str;
                        String str2 = str2;
                        String str3 = str3;
                        String str4 = Utilities.mFileStateForPrint;
                        activity.runOnUiThread(new Runnable(activity, str, str2, str3) {
                            public void run() {
                                Utilities.dismissCurrentAlert();
                                AlertDialog create = new AlertDialog.Builder(activity, R.style.sodk_editor_alert_dialog_style).setTitle(str).setMessage(str2).setCancelable(false).setPositiveButton(str3, new DialogInterface.OnClickListener(this) {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Utilities.currentMessageDialog = null;
                                    }
                                }).create();
                                Utilities.currentMessageDialog = create;
                                create.show();
                            }
                        });
                    }
                }
            });
        } else {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Utilities.dismissCurrentAlert();
                    AlertDialog create = new AlertDialog.Builder(activity, R.style.sodk_editor_alert_dialog_style).setTitle(str).setMessage(str2).setCancelable(false).setPositiveButton(str3, new DialogInterface.OnClickListener(this) {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Utilities.currentMessageDialog = null;
                        }
                    }).create();
                    Utilities.currentMessageDialog = create;
                    create.show();
                }
            });
        }
    }

    public static void showMessageAndWait(Activity activity, String str, String str2, Runnable runnable) {
        showMessageAndWait(activity, str, str2, R.style.sodk_editor_alert_dialog_style, runnable);
    }

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Point getRealScreenSize(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(VisionController.WINDOW)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }
}
