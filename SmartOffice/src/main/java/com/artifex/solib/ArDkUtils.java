package com.artifex.solib;

import android.app.Activity;

public class ArDkUtils {
    public static final String[] DOCX_TYPES = {"docx", "docm", "dotx", "dotm"};
    public static final String[] DOC_TYPES = {"doc"};
    public static final String[] IMG_TYPES = {"bmp", "jpg", "jpeg", "png"};
    public static final String[] MUPDF_TYPES = {"pdf", "epub", "svg", "xps", "fb2", "cbz", "xhtml"};
    public static final String[] ODT_TYPES = {"odt"};
    public static final String[] PPTX_TYPES = {"pps", "pptx", "pptm", "potx", "potm", "ppsx", "ppsm"};
    public static final String[] PPT_TYPES = {"ppt", "ppts"};
    public static final String[] SO_IMG_TYPES = {"wmf", "emf", "tif", "tiff", "gif"};
    public static final String[] SO_OTHER_TYPES = {"hwp", "txt", "csv"};
    public static final String[] XLSX_TYPES = {"xlsm", "xlsx", "xltm", "xltx"};
    public static final String[] XLS_TYPES = {"xls"};

    public static ArDkLib getLibraryForPath(Activity activity, String str) {
        if (FileUtils.isDocSupportedByMupdf(str)) {
            return MuPDFLib.getLib();
        }
        return SOLib.getLib(activity);
    }
}
