package com.artifex.sonui.editor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.view.WindowManager;

import com.artifex.R;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SODocSaveListener;
import java.util.UUID;

public class PrintHelperPdf {
    public static boolean printing = false;
    public String printName = "Printed File.pdf";

    public static void setPrinting(boolean z) {
        printing = z;
    }

    public void print(Context context, ArDkDoc arDkDoc, Runnable runnable) {
        if (!printing) {
            printing = true;
            String str = FileUtils.getTempPathRoot(context) + "/print/" + UUID.randomUUID() + ".pdf";
            FileUtils.createDirectory(str);
            FileUtils.deleteFile(str);
            final ProgressDialog progressDialog = new ProgressDialog(context);
            try {
                progressDialog.show();
            } catch (WindowManager.BadTokenException unused) {
            }
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            progressDialog.setContentView(R.layout.sodk_editor_wait_spinner);
            final Context context2 = context;
            final String str2 = str;
            final Runnable runnable2 = runnable;
            arDkDoc.saveToPDF(str, false, (i, i2) -> {
                progressDialog.dismiss();
                if (i == 0) {
                    PrintHelperPdf.this.printPath(context2, str2, true, new Runnable() {
                        public void run() {
                            PrintHelperPdf.printing = false;
                            Runnable runnable1 = runnable2;
                            if (runnable1 != null) {
                                runnable1.run();
                            }
                        }
                    });
                    return;
                }
                String format = String.format(context2.getString(R.string.sodk_editor_error_saving_document_code), new Object[]{Integer.valueOf(i2)});
                Context context1 = context2;
                Utilities.showMessage((Activity) context1, context1.getString(R.string.sodk_editor_error), format);
                PrintHelperPdf.printing = false;
                Runnable runnable1 = runnable2;
                if (runnable1 != null) {
                    runnable1.run();
                }
            });
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public void printPDF(Context context, String str, final Runnable runnable) {
        if (printing) {
            runnable.run();
            return;
        }
        printing = true;
        printPath(context, str, false, () -> {
            PrintHelperPdf.printing = false;
            Runnable runnableNew;
            runnableNew = runnable;
            if (runnableNew != null) {
                runnableNew.run();
            }
        });
    }

    public final void printPath(Context context, final String str, final boolean z, final Runnable runnable) {
        PrintDocumentAdapter r0 = new PrintDocumentAdapter() {
            public void onFinish() {
                if (z) {
                    FileUtils.deleteFile(str);
                }
                PrintHelperPdf.printing = false;
                Runnable runnableNew;
                runnableNew = runnable;
                if (runnableNew != null) {
                    runnableNew.run();
                }
            }

            public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
                if (cancellationSignal.isCanceled()) {
                    layoutResultCallback.onLayoutCancelled();
                } else {
                    layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(PrintHelperPdf.this.printName).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build(), true);
                }
            }

            /* JADX WARNING: Removed duplicated region for block: B:31:0x0043 A[SYNTHETIC, Splitter:B:31:0x0043] */
            /* JADX WARNING: Removed duplicated region for block: B:36:0x004b A[Catch:{ IOException -> 0x0047 }] */
            /* JADX WARNING: Removed duplicated region for block: B:42:0x0056 A[SYNTHETIC, Splitter:B:42:0x0056] */
            /* JADX WARNING: Removed duplicated region for block: B:45:0x005b A[Catch:{ IOException -> 0x0066 }] */
            /* JADX WARNING: Removed duplicated region for block: B:49:0x0062 A[Catch:{ IOException -> 0x0066 }] */
            /* JADX WARNING: Removed duplicated region for block: B:53:0x006a A[Catch:{ IOException -> 0x0066 }] */
            /* JADX WARNING: Removed duplicated region for block: B:57:? A[RETURN, SYNTHETIC] */
            /* JADX WARNING: Removed duplicated region for block: B:59:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onWrite(android.print.PageRange[] r3, android.os.ParcelFileDescriptor r4, CancellationSignal r5, WriteResultCallback r6) {
                /*
                    r2 = this;
                    r3 = 0
                    java.lang.String r5 = r3     // Catch:{ FileNotFoundException -> 0x005f, Exception -> 0x0053, all -> 0x003d }
                    java.lang.Object r5 = com.artifex.solib.FileUtils.getFileHandleForReading(r5)     // Catch:{ FileNotFoundException -> 0x005f, Exception -> 0x0053, all -> 0x003d }
                    java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x003a, Exception -> 0x0037, all -> 0x0034 }
                    java.io.FileDescriptor r4 = r4.getFileDescriptor()     // Catch:{ FileNotFoundException -> 0x003a, Exception -> 0x0037, all -> 0x0034 }
                    r0.<init>(r4)     // Catch:{ FileNotFoundException -> 0x003a, Exception -> 0x0037, all -> 0x0034 }
                    r3 = 1024(0x400, float:1.435E-42)
                    byte[] r3 = new byte[r3]     // Catch:{ FileNotFoundException -> 0x003b, Exception -> 0x0038, all -> 0x0032 }
                L_0x0014:
                    int r4 = com.artifex.solib.FileUtils.readFromFile(r5, r3)     // Catch:{ FileNotFoundException -> 0x003b, Exception -> 0x0038, all -> 0x0032 }
                    r1 = 0
                    if (r4 <= 0) goto L_0x001f
                    r0.write(r3, r1, r4)     // Catch:{ FileNotFoundException -> 0x003b, Exception -> 0x0038, all -> 0x0032 }
                    goto L_0x0014
                L_0x001f:
                    r3 = 1
                    android.print.PageRange[] r3 = new android.print.PageRange[r3]     // Catch:{ FileNotFoundException -> 0x003b, Exception -> 0x0038, all -> 0x0032 }
                    android.print.PageRange r4 = android.print.PageRange.ALL_PAGES     // Catch:{ FileNotFoundException -> 0x003b, Exception -> 0x0038, all -> 0x0032 }
                    r3[r1] = r4     // Catch:{ FileNotFoundException -> 0x003b, Exception -> 0x0038, all -> 0x0032 }
                    r6.onWriteFinished(r3)     // Catch:{ FileNotFoundException -> 0x003b, Exception -> 0x0038, all -> 0x0032 }
                    if (r5 == 0) goto L_0x002e
                    com.artifex.solib.FileUtils.closeFile(r5)     // Catch:{ IOException -> 0x0066 }
                L_0x002e:
                    r0.close()     // Catch:{ IOException -> 0x0066 }
                    goto L_0x0071
                L_0x0032:
                    r3 = move-exception
                    goto L_0x0041
                L_0x0034:
                    r4 = move-exception
                    r0 = r3
                    goto L_0x0040
                L_0x0037:
                    r0 = r3
                L_0x0038:
                    r3 = r5
                    goto L_0x0054
                L_0x003a:
                    r0 = r3
                L_0x003b:
                    r3 = r5
                    goto L_0x0060
                L_0x003d:
                    r4 = move-exception
                    r5 = r3
                    r0 = r5
                L_0x0040:
                    r3 = r4
                L_0x0041:
                    if (r5 == 0) goto L_0x0049
                    com.artifex.solib.FileUtils.closeFile(r5)     // Catch:{ IOException -> 0x0047 }
                    goto L_0x0049
                L_0x0047:
                    r4 = move-exception
                    goto L_0x004f
                L_0x0049:
                    if (r0 == 0) goto L_0x0052
                    r0.close()     // Catch:{ IOException -> 0x0047 }
                    goto L_0x0052
                L_0x004f:
                    r4.printStackTrace()
                L_0x0052:
                    throw r3
                L_0x0053:
                    r0 = r3
                L_0x0054:
                    if (r3 == 0) goto L_0x0059
                    com.artifex.solib.FileUtils.closeFile(r3)     // Catch:{ IOException -> 0x0066 }
                L_0x0059:
                    if (r0 == 0) goto L_0x0071
                    r0.close()     // Catch:{ IOException -> 0x0066 }
                    goto L_0x0071
                L_0x005f:
                    r0 = r3
                L_0x0060:
                    if (r3 == 0) goto L_0x0068
                    com.artifex.solib.FileUtils.closeFile(r3)     // Catch:{ IOException -> 0x0066 }
                    goto L_0x0068
                L_0x0066:
                    r3 = move-exception
                    goto L_0x006e
                L_0x0068:
                    if (r0 == 0) goto L_0x0071
                    r0.close()     // Catch:{ IOException -> 0x0066 }
                    goto L_0x0071
                L_0x006e:
                    r3.printStackTrace()
                L_0x0071:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.PrintHelperPdf.AnonymousClass3.onWrite(android.print.PageRange[], android.os.ParcelFileDescriptor, android.os.CancellationSignal, android.print.PrintDocumentAdapter$WriteResultCallback):void");
            }
        };
        ((PrintManager) context.getSystemService(Context.PRINT_SERVICE)).print(Utilities.getApplicationName(context) + " Document", r0, (PrintAttributes) null);
    }

    public void setPrintName(String str) {
        this.printName = str;
    }
}
