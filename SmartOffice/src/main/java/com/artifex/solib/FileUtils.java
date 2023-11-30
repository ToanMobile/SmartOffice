package com.artifex.solib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.artifex.solib.SOSecureFS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class FileUtils {
    public static SOSecureFS mSecureFs;

    public static boolean closeFile(Object obj) {
        SOSecureFS sOSecureFS = mSecureFs;
        if (sOSecureFS != null) {
            return sOSecureFS.closeFile(obj);
        }
        try {
            ((FileInputStream) obj).close();
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    /* JADX WARNING: type inference failed for: r5v2, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r7v1, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r7v2 */
    /* JADX WARNING: type inference failed for: r5v7 */
    /* JADX WARNING: type inference failed for: r7v4 */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.io.OutputStream, java.io.BufferedOutputStream] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00c6 A[Catch:{ FileNotFoundException | IOException -> 0x00da }] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x00cb A[Catch:{ FileNotFoundException | IOException -> 0x00da }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00d2 A[Catch:{ FileNotFoundException | IOException -> 0x00da }] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x00d7 A[Catch:{ FileNotFoundException | IOException -> 0x00da }] */
    /* JADX WARNING: Removed duplicated region for block: B:88:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyFile(String r5, String r6, boolean r7) {
        /*
            r0 = 0
            if (r5 == 0) goto L_0x00da
            if (r6 != 0) goto L_0x0007
            goto L_0x00da
        L_0x0007:
            com.artifex.solib.SOSecureFS r1 = mSecureFs
            if (r1 == 0) goto L_0x0063
            boolean r1 = r1.isSecurePath(r5)
            if (r1 != 0) goto L_0x0019
            com.artifex.solib.SOSecureFS r1 = mSecureFs
            boolean r1 = r1.isSecurePath(r6)
            if (r1 == 0) goto L_0x0063
        L_0x0019:
            com.artifex.solib.SOSecureFS r1 = mSecureFs
            boolean r1 = r1.isSecurePath(r6)
            if (r1 == 0) goto L_0x003f
            if (r7 != 0) goto L_0x002c
            com.artifex.solib.SOSecureFS r1 = mSecureFs
            boolean r1 = r1.fileExists(r6)
            if (r1 == 0) goto L_0x002c
            return r0
        L_0x002c:
            if (r7 == 0) goto L_0x005c
            com.artifex.solib.SOSecureFS r7 = mSecureFs
            boolean r7 = r7.fileExists(r6)
            if (r7 == 0) goto L_0x005c
            com.artifex.solib.SOSecureFS r7 = mSecureFs
            boolean r7 = r7.deleteFile(r6)
            if (r7 != 0) goto L_0x005c
            return r0
        L_0x003f:
            java.io.File r1 = new java.io.File
            r1.<init>(r6)
            if (r7 != 0) goto L_0x004d
            boolean r2 = r1.exists()
            if (r2 == 0) goto L_0x004d
            return r0
        L_0x004d:
            if (r7 == 0) goto L_0x005c
            boolean r7 = r1.exists()
            if (r7 == 0) goto L_0x005c
            boolean r7 = deleteFile(r6)
            if (r7 != 0) goto L_0x005c
            return r0
        L_0x005c:
            com.artifex.solib.SOSecureFS r7 = mSecureFs
            boolean r5 = r7.copyFile(r5, r6)
            return r5
        L_0x0063:
            java.io.File r1 = new java.io.File
            r1.<init>(r6)
            java.io.File r2 = new java.io.File
            r2.<init>(r5)
            if (r7 != 0) goto L_0x0076
            boolean r5 = r1.exists()
            if (r5 == 0) goto L_0x0076
            return r0
        L_0x0076:
            if (r7 == 0) goto L_0x0085
            boolean r5 = r1.exists()
            if (r5 == 0) goto L_0x0085
            boolean r5 = deleteFile(r6)
            if (r5 != 0) goto L_0x0085
            return r0
        L_0x0085:
            r5 = 32768(0x8000, float:4.5918E-41)
            r6 = 0
            byte[] r5 = new byte[r5]     // Catch:{ Exception -> 0x00cf, all -> 0x00c2 }
            java.io.BufferedInputStream r7 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x00cf, all -> 0x00c2 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00cf, all -> 0x00c2 }
            r3.<init>(r2)     // Catch:{ Exception -> 0x00cf, all -> 0x00c2 }
            r7.<init>(r3)     // Catch:{ Exception -> 0x00cf, all -> 0x00c2 }
            java.io.BufferedOutputStream r2 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x00bf, all -> 0x00ba }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00bf, all -> 0x00ba }
            r3.<init>(r1, r0)     // Catch:{ Exception -> 0x00bf, all -> 0x00ba }
            r2.<init>(r3)     // Catch:{ Exception -> 0x00bf, all -> 0x00ba }
            r6 = 0
        L_0x00a0:
            r1 = -1
            if (r6 == r1) goto L_0x00b2
            int r6 = r7.read(r5)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            if (r6 <= 0) goto L_0x00a0
            r2.write(r5, r0, r6)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            goto L_0x00a0
        L_0x00ad:
            r5 = move-exception
            r6 = r2
            goto L_0x00bb
        L_0x00b0:
            r6 = r2
            goto L_0x00bf
        L_0x00b2:
            r7.close()     // Catch:{ FileNotFoundException | IOException -> 0x00da }
            r2.close()     // Catch:{ FileNotFoundException | IOException -> 0x00da }
            r0 = 1
            goto L_0x00da
        L_0x00ba:
            r5 = move-exception
        L_0x00bb:
            r4 = r7
            r7 = r6
            r6 = r4
            goto L_0x00c4
        L_0x00bf:
            r5 = r6
            r6 = r7
            goto L_0x00d0
        L_0x00c2:
            r5 = move-exception
            r7 = r6
        L_0x00c4:
            if (r6 == 0) goto L_0x00c9
            r6.close()     // Catch:{ FileNotFoundException | IOException -> 0x00da }
        L_0x00c9:
            if (r7 == 0) goto L_0x00ce
            r7.close()     // Catch:{ FileNotFoundException | IOException -> 0x00da }
        L_0x00ce:
            throw r5     // Catch:{ FileNotFoundException | IOException -> 0x00da }
        L_0x00cf:
            r5 = r6
        L_0x00d0:
            if (r6 == 0) goto L_0x00d5
            r6.close()     // Catch:{ FileNotFoundException | IOException -> 0x00da }
        L_0x00d5:
            if (r5 == 0) goto L_0x00da
            r5.close()     // Catch:{ FileNotFoundException | IOException -> 0x00da }
        L_0x00da:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.FileUtils.copyFile(java.lang.String, java.lang.String, boolean):boolean");
    }

    public static boolean createDirectory(String str) {
        if (str == null) {
            return false;
        }
        SOSecureFS sOSecureFS = mSecureFs;
        if (sOSecureFS != null && sOSecureFS.isSecurePath(str)) {
            return mSecureFs.createDirectory(str);
        }
        try {
            return new File(str).mkdirs();
        } catch (SecurityException unused) {
            return false;
        }
    }

    public static boolean deleteFile(String str) {
        if (str == null) {
            return false;
        }
        SOSecureFS sOSecureFS = mSecureFs;
        if (sOSecureFS != null && sOSecureFS.isSecurePath(str)) {
            return mSecureFs.deleteFile(str);
        }
        try {
            File file = new File(new File(str).getAbsolutePath());
            if (!file.exists()) {
                return true;
            }
            file.delete();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean deleteRecursive(String str) {
        File file = new File(str);
        try {
            if (file.isDirectory()) {
                for (File path : file.listFiles()) {
                    deleteRecursive(path.getPath());
                }
            }
            file.delete();
            return true;
        } catch (SecurityException unused) {
            return false;
        } catch (NullPointerException unused2) {
            Log.e("FileUtils", "deleteRecursive() failed  [" + str + "]: Have storage permissions been granted");
            return false;
        }
    }

    public static String displayNameFromUri(Context context, Uri uri) {
        String scheme = uri.getScheme();
        if (scheme == null) {
            return null;
        }
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_display_name"}, (String) null, (String[]) null, (String) null);
            if (query == null || !query.moveToFirst()) {
                return null;
            }
            @SuppressLint("Range") String string = query.getString(query.getColumnIndex("_display_name"));
            query.close();
            return string;
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x005c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String exportContentUri(Context r5, Uri r6) {
        /*
            java.lang.String r0 = r6.getScheme()
            java.lang.String r1 = r6.getPath()
            java.lang.String r2 = r6.toString()
            java.lang.String r3 = "."
            if (r0 == 0) goto L_0x0042
            java.lang.String r4 = "content"
            boolean r0 = r0.equalsIgnoreCase(r4)
            if (r0 == 0) goto L_0x0042
            java.lang.String r0 = displayNameFromUri(r5, r6)
            if (r0 != 0) goto L_0x0050
            java.lang.String r0 = "attachment"
            boolean r1 = r2.contains(r0)
            if (r1 != 0) goto L_0x002e
            java.lang.String r1 = "mail"
            boolean r1 = r2.contains(r1)
            if (r1 == 0) goto L_0x002f
        L_0x002e:
            r4 = r0
        L_0x002f:
            java.lang.String r0 = getFileTypeExtension(r5, r6)
            if (r0 == 0) goto L_0x0040
            int r1 = r0.length()
            if (r1 <= 0) goto L_0x0040
            java.lang.String r0 = androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline1.m(r4, r3, r0)
            goto L_0x0050
        L_0x0040:
            r0 = r4
            goto L_0x0050
        L_0x0042:
            if (r1 == 0) goto L_0x004e
            java.io.File r0 = new java.io.File
            r0.<init>(r1)
            java.lang.String r0 = r0.getName()
            goto L_0x0050
        L_0x004e:
            java.lang.String r0 = "file"
        L_0x0050:
            java.lang.String r1 = getExtension(r0)
            java.lang.String r2 = ""
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0064
            java.lang.String r1 = getFileTypeExtension(r5, r6)
            java.lang.String r0 = androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline1.m(r0, r3, r1)
        L_0x0064:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = getTempPathRoot(r5)
            r1.append(r2)
            java.lang.String r2 = "/shared/"
            r1.append(r2)
            java.util.UUID r2 = java.util.UUID.randomUUID()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            createDirectory(r1)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            java.lang.String r1 = "/"
            r2.append(r1)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            java.lang.String r5 = exportContentUri(r5, r6, r0)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.FileUtils.exportContentUri(android.content.Context, android.net.Uri):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0005, code lost:
        r2 = new java.io.File(r2.getPath()).getName();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String extensionFromUriFilename(Uri r2) {
        /*
            java.lang.String r0 = ""
            if (r2 != 0) goto L_0x0005
            return r0
        L_0x0005:
            java.io.File r1 = new java.io.File
            java.lang.String r2 = r2.getPath()
            r1.<init>(r2)
            java.lang.String r2 = r1.getName()
            r1 = 46
            int r1 = r2.lastIndexOf(r1)
            if (r1 <= 0) goto L_0x0020
            int r1 = r1 + 1
            java.lang.String r0 = r2.substring(r1)
        L_0x0020:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.FileUtils.extensionFromUriFilename(android.net.Uri):java.lang.String");
    }

    public static String extractAssetToString(Context context, String str) {
        try {
            InputStream open = context.getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean fileExists(String str) {
        if (str == null) {
            return false;
        }
        SOSecureFS sOSecureFS = mSecureFs;
        if (sOSecureFS != null && sOSecureFS.isSecurePath(str)) {
            return mSecureFs.fileExists(str);
        }
        if (new File(str).exists()) {
            return true;
        }
        return false;
    }

    public static long fileLastModified(String str) {
        if (str == null) {
            return 0;
        }
        SOSecureFS sOSecureFS = mSecureFs;
        if (sOSecureFS == null) {
            return new File(str).lastModified();
        }
        SOSecureFS.FileAttributes fileAttributes = sOSecureFS.getFileAttributes(str);
        if (fileAttributes != null) {
            return fileAttributes.lastModified;
        }
        return 0;
    }

    public static String getExtension(String str) {
        if (str == null) {
            return "";
        }
        String[] split = str.split("\\.");
        if (split.length <= 1) {
            return "";
        }
        return split[split.length - 1].toLowerCase();
    }

    public static Object getFileHandleForReading(String str) {
        if (str == null) {
            return null;
        }
        SOSecureFS sOSecureFS = mSecureFs;
        if (sOSecureFS != null) {
            return sOSecureFS.getFileHandleForReading(str);
        }
        try {
            return new FileInputStream(str);
        } catch (FileNotFoundException | SecurityException unused) {
            return null;
        }
    }

    public static String getFileTypeExtension(Context context, Uri uri) {
        return getFileTypeExtension(context, uri, (String) null);
    }

    public static String getTempPathRoot(Context context) {
        SOSecureFS sOSecureFS = mSecureFs;
        if (sOSecureFS != null) {
            return sOSecureFS.getTempPath();
        }
        return context.getFilesDir().toString();
    }

    public static void init() {
        try {
            SOSecureFS sOSecureFS = ArDkLib.mSecureFS;
            mSecureFs = sOSecureFS;
            if (sOSecureFS == null) {
                throw new ClassNotFoundException();
            }
        } catch (ExceptionInInitializerError unused) {
            Log.e("FileUtils", String.format("init() experienced unexpected exception [%s]", new Object[]{"ExceptionInInitializerError"}));
        } catch (LinkageError unused2) {
            Log.e("FileUtils", String.format("init() experienced unexpected exception [%s]", new Object[]{"LinkageError"}));
        } catch (SecurityException unused3) {
            Log.e("FileUtils", String.format("init() experienced unexpected exception [%s]", new Object[]{"SecurityException"}));
        } catch (ClassNotFoundException unused4) {
            Log.i("FileUtils", "SecureFS implementation unavailable");
        }
    }

    public static boolean isDocSupportedByMupdf(String str) {
        String extension = getExtension(str);
        return matchFileExtension(extension, ArDkUtils.MUPDF_TYPES) || matchFileExtension(extension, ArDkUtils.IMG_TYPES);
    }

    public static boolean matchFileExtension(String str, String[] strArr) {
        for (String lowerCase : strArr) {
            if (str.toLowerCase().equals(lowerCase.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static byte[] readFileBytes(String str) {
        Object fileHandleForReading = getFileHandleForReading(str);
        if (fileHandleForReading == null) {
            return null;
        }
        long j = 0;
        if (str != null) {
            SOSecureFS sOSecureFS = mSecureFs;
            if (sOSecureFS != null) {
                SOSecureFS.FileAttributes fileAttributes = sOSecureFS.getFileAttributes(str);
                if (fileAttributes != null) {
                    j = fileAttributes.length;
                }
            } else {
                j = new File(str).length();
            }
        }
        int i = (int) j;
        if (i <= 0) {
            return null;
        }
        byte[] bArr = new byte[i];
        if (readFromFile(fileHandleForReading, bArr) > 0) {
            return bArr;
        }
        return null;
    }

    public static int readFromFile(Object obj, byte[] bArr) {
        if (obj == null) {
            return -1;
        }
        SOSecureFS sOSecureFS = mSecureFs;
        if (sOSecureFS != null) {
            return sOSecureFS.readFromFile(obj, bArr);
        }
        try {
            return ((FileInputStream) obj).read(bArr);
        } catch (IOException unused) {
            return -1;
        }
    }

    public static boolean renameFile(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        SOSecureFS sOSecureFS = mSecureFs;
        if (sOSecureFS != null) {
            return sOSecureFS.renameFile(str, str2);
        }
        return new File(str).renameTo(new File(str2));
    }

    public static String getFileTypeExtension(Context context, Uri uri, String str) {
        String scheme = uri != null ? uri.getScheme() : null;
        if (scheme == null) {
            return extensionFromUriFilename(uri);
        }
        MimeTypeMap singleton = MimeTypeMap.getSingleton();
        String type = context.getContentResolver().getType(uri);
        String str2 = "";
        if (type != null) {
            str = type;
        } else if (str == null) {
            str = str2;
        }
        if (str.equalsIgnoreCase("application/vnd.ms-xpsdocument") || str.equalsIgnoreCase("application/oxps")) {
            str2 = "xps";
        } else if (str.equalsIgnoreCase("application/octet-stream")) {
            String displayNameFromUri = displayNameFromUri(context, uri);
            if (displayNameFromUri != null) {
                str2 = getExtension(displayNameFromUri);
            }
        } else {
            str2 = singleton.getExtensionFromMimeType(str);
        }
        if (str2 == null) {
            return extensionFromUriFilename(uri);
        }
        return str2;
    }

    public static String exportContentUri(Context context, Uri uri, String str) {
        Object obj;
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            try {
                if (fileExists(str)) {
                    deleteFile(str);
                }
                SOSecureFS sOSecureFS = mSecureFs;
                if (sOSecureFS == null || !sOSecureFS.isSecurePath(str)) {
                    obj = new FileOutputStream(str);
                } else {
                    obj = mSecureFs.getFileHandleForWriting(str);
                }
                if (obj == null) {
                    return "---";
                }
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = openInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    } else if (obj instanceof FileOutputStream) {
                        ((FileOutputStream) obj).write(bArr, 0, read);
                    } else {
                        mSecureFs.writeToFile(obj, Arrays.copyOf(bArr, read));
                    }
                }
                if (obj instanceof FileOutputStream) {
                    ((FileOutputStream) obj).close();
                    return str;
                }
                mSecureFs.closeFile(obj);
                return str;
            } catch (IOException e) {
                e.printStackTrace();
                StringBuilder sb = new StringBuilder();
                sb.append("---IOException ");
                return FileUtils$$ExternalSyntheticOutline0.m(e, sb);
            } catch (SecurityException e2) {
                e2.printStackTrace();
                return "---SecurityException " + e2.getMessage();
            } catch (Exception e3) {
                e3.printStackTrace();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("---Exception ");
                return FileUtils$$ExternalSyntheticOutline1.m(e3, sb2);
            }
        } catch (Exception unused) {
            return "---fileOpen";
        }
    }
}
