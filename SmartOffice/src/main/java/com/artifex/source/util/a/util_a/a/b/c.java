package com.artifex.source.util.a.util_a.a.b;

import java.util.ArrayList;
import java.util.List;

public class c {

    /* renamed from: a  reason: collision with root package name */
    public static List f397a = new ArrayList();

    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0160, code lost:
        return true;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x001f */
    /* JADX WARNING: Missing exception handler attribute for start block: B:60:0x0128 */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002a A[Catch:{ all -> 0x0163 }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x002c A[Catch:{ all -> 0x0163 }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004b A[Catch:{ all -> 0x0163 }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0054 A[DONT_GENERATE] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0056 A[SYNTHETIC, Splitter:B:27:0x0056] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x014f A[DONT_GENERATE] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0151 A[SYNTHETIC, Splitter:B:77:0x0151] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized boolean a(android.content.Context r12, String r13) {
        /*
            java.lang.Class<com.artifex.source.util.a.util_a.a.b.c> r0 = com.artifex.source.util.a.util_a.a.b.c.class
            monitor-enter(r0)
            java.util.List r1 = f397a     // Catch:{ all -> 0x016e }
            java.util.ArrayList r1 = (java.util.ArrayList) r1     // Catch:{ all -> 0x016e }
            boolean r1 = r1.contains(r13)     // Catch:{ all -> 0x016e }
            r2 = 1
            if (r1 == 0) goto L_0x0010
            monitor-exit(r0)
            return r2
        L_0x0010:
            r1 = 0
            java.lang.System.loadLibrary(r13)     // Catch:{ UnsatisfiedLinkError -> 0x001f, all -> 0x001d }
            java.util.List r3 = f397a     // Catch:{ UnsatisfiedLinkError -> 0x001f, all -> 0x001d }
            java.util.ArrayList r3 = (java.util.ArrayList) r3     // Catch:{ UnsatisfiedLinkError -> 0x001f, all -> 0x001d }
            r3.add(r13)     // Catch:{ UnsatisfiedLinkError -> 0x001f, all -> 0x001d }
            goto L_0x015f
        L_0x001d:
            monitor-exit(r0)
            return r1
        L_0x001f:
            java.lang.String r3 = java.lang.System.mapLibraryName(r13)     // Catch:{ all -> 0x016e }
            r4 = 0
            java.io.File r5 = r12.getFilesDir()     // Catch:{ all -> 0x016e }
            if (r5 != 0) goto L_0x002c
            r5 = r4
            goto L_0x0049
        L_0x002c:
            java.io.File r5 = new java.io.File     // Catch:{ all -> 0x016e }
            java.io.File r6 = r12.getFilesDir()     // Catch:{ all -> 0x016e }
            java.lang.String r7 = "libso"
            r5.<init>(r6, r7)     // Catch:{ all -> 0x016e }
            boolean r6 = r5.exists()     // Catch:{ all -> 0x016e }
            if (r6 != 0) goto L_0x0049
            java.lang.String r6 = r5.getAbsolutePath()     // Catch:{ all -> 0x016e }
            java.io.File r7 = new java.io.File     // Catch:{ all -> 0x016e }
            r7.<init>(r6)     // Catch:{ all -> 0x016e }
            r7.mkdirs()     // Catch:{ all -> 0x016e }
        L_0x0049:
            if (r5 == 0) goto L_0x0051
            java.io.File r6 = new java.io.File     // Catch:{ all -> 0x016e }
            r6.<init>(r5, r3)     // Catch:{ all -> 0x016e }
            goto L_0x0052
        L_0x0051:
            r6 = r4
        L_0x0052:
            if (r6 != 0) goto L_0x0056
            monitor-exit(r0)
            return r1
        L_0x0056:
            boolean r3 = r6.exists()     // Catch:{ all -> 0x016e }
            if (r3 == 0) goto L_0x005f
            r6.delete()     // Catch:{ all -> 0x016e }
        L_0x005f:
            android.content.pm.ApplicationInfo r12 = r12.getApplicationInfo()     // Catch:{ all -> 0x013d }
            java.util.zip.ZipFile r3 = new java.util.zip.ZipFile     // Catch:{ all -> 0x013d }
            java.io.File r5 = new java.io.File     // Catch:{ all -> 0x013d }
            java.lang.String r12 = r12.sourceDir     // Catch:{ all -> 0x013d }
            r5.<init>(r12)     // Catch:{ all -> 0x013d }
            r3.<init>(r5, r2)     // Catch:{ all -> 0x013d }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0136 }
            r12.<init>()     // Catch:{ all -> 0x0136 }
            java.lang.String r5 = "lib/"
            r12.append(r5)     // Catch:{ all -> 0x0136 }
            java.lang.String r5 = android.os.Build.CPU_ABI     // Catch:{ all -> 0x0136 }
            r12.append(r5)     // Catch:{ all -> 0x0136 }
            java.lang.String r7 = "/"
            r12.append(r7)     // Catch:{ all -> 0x0136 }
            java.lang.String r7 = java.lang.System.mapLibraryName(r13)     // Catch:{ all -> 0x0136 }
            r12.append(r7)     // Catch:{ all -> 0x0136 }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x0136 }
            java.util.zip.ZipEntry r12 = r3.getEntry(r12)     // Catch:{ all -> 0x0136 }
            if (r12 != 0) goto L_0x00e6
            r12 = 45
            int r12 = r5.indexOf(r12)     // Catch:{ all -> 0x0136 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0136 }
            r7.<init>()     // Catch:{ all -> 0x0136 }
            java.lang.String r8 = "lib/"
            r7.append(r8)     // Catch:{ all -> 0x0136 }
            if (r12 <= 0) goto L_0x00a7
            goto L_0x00ab
        L_0x00a7:
            int r12 = r5.length()     // Catch:{ all -> 0x0136 }
        L_0x00ab:
            java.lang.String r12 = r5.substring(r1, r12)     // Catch:{ all -> 0x0136 }
            r7.append(r12)     // Catch:{ all -> 0x0136 }
            java.lang.String r12 = "/"
            r7.append(r12)     // Catch:{ all -> 0x0136 }
            java.lang.String r12 = java.lang.System.mapLibraryName(r13)     // Catch:{ all -> 0x0136 }
            r7.append(r12)     // Catch:{ all -> 0x0136 }
            java.lang.String r12 = r7.toString()     // Catch:{ all -> 0x0136 }
            java.util.zip.ZipEntry r5 = r3.getEntry(r12)     // Catch:{ all -> 0x0136 }
            if (r5 != 0) goto L_0x00e5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0136 }
            r5.<init>()     // Catch:{ all -> 0x0136 }
            java.lang.String r7 = "Library entry not found:"
            r5.append(r7)     // Catch:{ all -> 0x0136 }
            r5.append(r12)     // Catch:{ all -> 0x0136 }
            java.lang.String r12 = r5.toString()     // Catch:{ all -> 0x0136 }
            com.pgl.ssdk.ces.e.a.a((java.io.Closeable) r4)     // Catch:{ all -> 0x016e }
            com.pgl.ssdk.ces.e.a.a((java.io.Closeable) r4)     // Catch:{ all -> 0x016e }
            com.pgl.ssdk.ces.e.a.a((java.util.zip.ZipFile) r3)     // Catch:{ all -> 0x016e }
            r4 = r12
            goto L_0x014d
        L_0x00e5:
            r12 = r5
        L_0x00e6:
            r6.createNewFile()     // Catch:{ all -> 0x0136 }
            java.io.InputStream r12 = r3.getInputStream(r12)     // Catch:{ all -> 0x0136 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ all -> 0x0134 }
            r5.<init>(r6)     // Catch:{ all -> 0x0134 }
            r7 = 16384(0x4000, float:2.2959E-41)
            byte[] r7 = new byte[r7]     // Catch:{ all -> 0x0132 }
        L_0x00f6:
            int r8 = r12.read(r7)     // Catch:{ all -> 0x0132 }
            if (r8 <= 0) goto L_0x0100
            r5.write(r7, r1, r8)     // Catch:{ all -> 0x0132 }
            goto L_0x00f6
        L_0x0100:
            java.lang.String r7 = r6.getAbsolutePath()     // Catch:{ all -> 0x0132 }
            r8 = 493(0x1ed, float:6.91E-43)
            r9 = 3
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ all -> 0x0132 }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x0132 }
            r9[r1] = r8     // Catch:{ all -> 0x0132 }
            r8 = -1
            java.lang.Integer r10 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x0132 }
            r9[r2] = r10     // Catch:{ all -> 0x0132 }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x0132 }
            r10 = 2
            r9[r10] = r8     // Catch:{ all -> 0x0132 }
            java.lang.String r8 = "android.os.FileUtils"
            java.util.Map r10 = com.artifex.source.util.a.util_a.a.b.b.f396a     // Catch:{ all -> 0x0132 }
            java.lang.Class r8 = java.lang.Class.forName(r8)     // Catch:{ Exception -> 0x0128 }
            com.artifex.source.util.a.util_a.a.b.b.a((java.lang.Class) r8, (java.lang.String) r7, (java.lang.Object[]) r9)     // Catch:{ Exception -> 0x0128 }
        L_0x0128:
            com.pgl.ssdk.ces.e.a.a((java.io.Closeable) r5)     // Catch:{ all -> 0x016e }
            com.pgl.ssdk.ces.e.a.a((java.io.Closeable) r12)     // Catch:{ all -> 0x016e }
        L_0x012e:
            com.pgl.ssdk.ces.e.a.a((java.util.zip.ZipFile) r3)     // Catch:{ all -> 0x016e }
            goto L_0x014d
        L_0x0132:
            r4 = move-exception
            goto L_0x0142
        L_0x0134:
            r5 = move-exception
            goto L_0x0139
        L_0x0136:
            r12 = move-exception
            r5 = r12
            r12 = r4
        L_0x0139:
            r11 = r5
            r5 = r4
            r4 = r11
            goto L_0x0142
        L_0x013d:
            r12 = move-exception
            r3 = r4
            r5 = r3
            r4 = r12
            r12 = r5
        L_0x0142:
            java.lang.String r4 = r4.getMessage()     // Catch:{ all -> 0x0163 }
            com.pgl.ssdk.ces.e.a.a((java.io.Closeable) r5)     // Catch:{ all -> 0x016e }
            com.pgl.ssdk.ces.e.a.a((java.io.Closeable) r12)     // Catch:{ all -> 0x016e }
            goto L_0x012e
        L_0x014d:
            if (r4 == 0) goto L_0x0151
            monitor-exit(r0)
            return r1
        L_0x0151:
            java.lang.String r12 = r6.getAbsolutePath()     // Catch:{ all -> 0x0161 }
            java.lang.System.load(r12)     // Catch:{ all -> 0x0161 }
            java.util.List r12 = f397a     // Catch:{ all -> 0x0161 }
            java.util.ArrayList r12 = (java.util.ArrayList) r12     // Catch:{ all -> 0x0161 }
            r12.add(r13)     // Catch:{ all -> 0x0161 }
        L_0x015f:
            monitor-exit(r0)
            return r2
        L_0x0161:
            monitor-exit(r0)
            return r1
        L_0x0163:
            r13 = move-exception
            com.pgl.ssdk.ces.e.a.a((java.io.Closeable) r5)     // Catch:{ all -> 0x016e }
            com.pgl.ssdk.ces.e.a.a((java.io.Closeable) r12)     // Catch:{ all -> 0x016e }
            com.pgl.ssdk.ces.e.a.a((java.util.zip.ZipFile) r3)     // Catch:{ all -> 0x016e }
            throw r13     // Catch:{ all -> 0x016e }
        L_0x016e:
            r12 = move-exception
            monitor-exit(r0)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.source.util.a.util_a.a.b.c.a(android.content.Context, java.lang.String):boolean");
    }
}
