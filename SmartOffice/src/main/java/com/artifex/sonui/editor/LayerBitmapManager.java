package com.artifex.sonui.editor;

import com.artifex.solib.SOBitmap;
import java.util.TreeSet;

public class LayerBitmapManager {
    public TreeSet<SOBitmap> alphaBitmaps = new TreeSet<>();
    public int alphaEvicted = 0;
    public int alphaOom = 0;
    public int alphaRejected = 0;
    public int alphaReuse = 0;
    public long cacheSizeLeft = (Runtime.getRuntime().maxMemory() / 6);
    public TreeSet<SOBitmap> colourBitmaps = new TreeSet<>();
    public int colourEvicted = 0;
    public int colourOom = 0;
    public int colourRejected = 0;
    public int colourReuse = 0;
    public int evictionCount = 0;
    public TreeSet<SOBitmap> fullBitmaps = new TreeSet<>();
    public int fullEvicted = 0;
    public int fullOom = 0;
    public int fullRejected = 0;
    public int fullReuse = 0;
    public MemoryInfoProvider mip = null;
    public int oomCount = 0;
    public int rejectedBMCount = 0;
    public int reuseCount = 0;

    /* renamed from: com.artifex.sonui.editor.LayerBitmapManager$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config;
        public static final /* synthetic */ int[] $SwitchMap$com$artifex$solib$ArDkBitmap$Type;

        /* JADX WARNING: Can't wrap try/catch for region: R(15:0|(2:1|2)|3|(2:5|6)|7|9|10|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Can't wrap try/catch for region: R(17:0|1|2|3|5|6|7|9|10|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0039 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0043 */
        static {
            /*
                com.artifex.solib.ArDkBitmap$Type[] r0 = com.artifex.solib.ArDkBitmap.Type.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$artifex$solib$ArDkBitmap$Type = r0
                r1 = 1
                com.artifex.solib.ArDkBitmap$Type r2 = com.artifex.solib.ArDkBitmap.Type.A8     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$com$artifex$solib$ArDkBitmap$Type     // Catch:{ NoSuchFieldError -> 0x001d }
                com.artifex.solib.ArDkBitmap$Type r3 = com.artifex.solib.ArDkBitmap.Type.RGB565     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = $SwitchMap$com$artifex$solib$ArDkBitmap$Type     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.artifex.solib.ArDkBitmap$Type r4 = com.artifex.solib.ArDkBitmap.Type.RGBA8888     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                android.graphics.Bitmap$Config[] r3 = android.graphics.Bitmap.Config.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                $SwitchMap$android$graphics$Bitmap$Config = r3
                android.graphics.Bitmap$Config r4 = android.graphics.Bitmap.Config.ALPHA_8     // Catch:{ NoSuchFieldError -> 0x0039 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0039 }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x0039 }
            L_0x0039:
                int[] r1 = $SwitchMap$android$graphics$Bitmap$Config     // Catch:{ NoSuchFieldError -> 0x0043 }
                android.graphics.Bitmap$Config r3 = android.graphics.Bitmap.Config.RGB_565     // Catch:{ NoSuchFieldError -> 0x0043 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0043 }
                r1[r3] = r0     // Catch:{ NoSuchFieldError -> 0x0043 }
            L_0x0043:
                int[] r0 = $SwitchMap$android$graphics$Bitmap$Config     // Catch:{ NoSuchFieldError -> 0x004d }
                android.graphics.Bitmap$Config r1 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ NoSuchFieldError -> 0x004d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004d }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004d }
            L_0x004d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.LayerBitmapManager.AnonymousClass1.<clinit>():void");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0066, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0079, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void addToCache(SOBitmap r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            if (r6 != 0) goto L_0x0005
            monitor-exit(r5)
            return
        L_0x0005:
            android.graphics.Bitmap r0 = r6.bitmap     // Catch:{ all -> 0x0076 }
            if (r0 == 0) goto L_0x0078
            boolean r1 = r0.isRecycled()     // Catch:{ all -> 0x0076 }
            if (r1 == 0) goto L_0x0010
            goto L_0x0078
        L_0x0010:
            int[] r1 = com.artifex.sonui.editor.LayerBitmapManager.AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config     // Catch:{ all -> 0x0076 }
            android.graphics.Bitmap r2 = r6.bitmap     // Catch:{ all -> 0x0076 }
            android.graphics.Bitmap$Config r2 = r2.getConfig()     // Catch:{ all -> 0x0076 }
            int r2 = r2.ordinal()     // Catch:{ all -> 0x0076 }
            r1 = r1[r2]     // Catch:{ all -> 0x0076 }
            r2 = 1
            if (r1 == r2) goto L_0x002d
            r3 = 2
            if (r1 == r3) goto L_0x002a
            r3 = 3
            if (r1 == r3) goto L_0x002a
            java.util.TreeSet<com.artifex.solib.SOBitmap> r1 = r5.fullBitmaps     // Catch:{ all -> 0x0076 }
            goto L_0x002f
        L_0x002a:
            java.util.TreeSet<com.artifex.solib.SOBitmap> r1 = r5.colourBitmaps     // Catch:{ all -> 0x0076 }
            goto L_0x002f
        L_0x002d:
            java.util.TreeSet<com.artifex.solib.SOBitmap> r1 = r5.alphaBitmaps     // Catch:{ all -> 0x0076 }
        L_0x002f:
            if (r1 != 0) goto L_0x0033
            monitor-exit(r5)
            return
        L_0x0033:
            int r3 = r0.getByteCount()     // Catch:{ all -> 0x0076 }
            boolean r3 = r5.makeRoomForBitmap(r3, r1)     // Catch:{ all -> 0x0076 }
            if (r3 != 0) goto L_0x0067
            r6.dispose()     // Catch:{ all -> 0x0076 }
            java.lang.Runtime r6 = java.lang.Runtime.getRuntime()     // Catch:{ all -> 0x0076 }
            r6.gc()     // Catch:{ all -> 0x0076 }
            int r6 = r5.rejectedBMCount     // Catch:{ all -> 0x0076 }
            int r6 = r6 + r2
            r5.rejectedBMCount = r6     // Catch:{ all -> 0x0076 }
            java.util.TreeSet<com.artifex.solib.SOBitmap> r6 = r5.fullBitmaps     // Catch:{ all -> 0x0076 }
            if (r1 != r6) goto L_0x0056
            int r6 = r5.fullRejected     // Catch:{ all -> 0x0076 }
            int r6 = r6 + r2
            r5.fullRejected = r6     // Catch:{ all -> 0x0076 }
            goto L_0x0065
        L_0x0056:
            java.util.TreeSet<com.artifex.solib.SOBitmap> r6 = r5.colourBitmaps     // Catch:{ all -> 0x0076 }
            if (r1 != r6) goto L_0x0060
            int r6 = r5.colourRejected     // Catch:{ all -> 0x0076 }
            int r6 = r6 + r2
            r5.colourRejected = r6     // Catch:{ all -> 0x0076 }
            goto L_0x0065
        L_0x0060:
            int r6 = r5.alphaRejected     // Catch:{ all -> 0x0076 }
            int r6 = r6 + r2
            r5.alphaRejected = r6     // Catch:{ all -> 0x0076 }
        L_0x0065:
            monitor-exit(r5)
            return
        L_0x0067:
            r1.add(r6)     // Catch:{ all -> 0x0076 }
            long r1 = r5.cacheSizeLeft     // Catch:{ all -> 0x0076 }
            int r6 = r0.getByteCount()     // Catch:{ all -> 0x0076 }
            long r3 = (long) r6     // Catch:{ all -> 0x0076 }
            long r1 = r1 - r3
            r5.cacheSizeLeft = r1     // Catch:{ all -> 0x0076 }
            monitor-exit(r5)
            return
        L_0x0076:
            r6 = move-exception
            goto L_0x007a
        L_0x0078:
            monitor-exit(r5)
            return
        L_0x007a:
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.LayerBitmapManager.addToCache(com.artifex.solib.SOBitmap):void");
    }

    public synchronized void clearAllBitmaps() {
        TreeSet<SOBitmap> treeSet = this.fullBitmaps;
        if (treeSet != null) {
            clearList(treeSet);
        }
        TreeSet<SOBitmap> treeSet2 = this.colourBitmaps;
        if (treeSet2 != null) {
            clearList(treeSet2);
        }
        if (this.colourBitmaps != null) {
            clearList(this.alphaBitmaps);
        }
        Runtime.getRuntime().gc();
        this.reuseCount = 0;
        this.evictionCount = 0;
        this.oomCount = 0;
        this.rejectedBMCount = 0;
        this.colourOom = 0;
        this.colourReuse = 0;
        this.colourRejected = 0;
        this.colourEvicted = 0;
        this.alphaOom = 0;
        this.alphaReuse = 0;
        this.alphaRejected = 0;
        this.alphaEvicted = 0;
        this.fullOom = 0;
        this.fullReuse = 0;
        this.fullRejected = 0;
        this.fullEvicted = 0;
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0039 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x0011  */
    public final void clearList(TreeSet<SOBitmap> r7) {
        /*
            r6 = this;
            boolean r0 = r7.isEmpty()
            r1 = 0
            if (r0 != 0) goto L_0x000e
            java.lang.Object r0 = r7.first()
            com.artifex.solib.SOBitmap r0 = (com.artifex.solib.SOBitmap) r0
            goto L_0x000f
        L_0x000e:
            r0 = r1
        L_0x000f:
            if (r0 == 0) goto L_0x0039
            r7.remove(r0)
            android.graphics.Bitmap r2 = r0.bitmap
            boolean r2 = r2.isRecycled()
            if (r2 == 0) goto L_0x001d
            goto L_0x002c
        L_0x001d:
            long r2 = r6.cacheSizeLeft
            android.graphics.Bitmap r4 = r0.bitmap
            int r4 = r4.getByteCount()
            long r4 = (long) r4
            long r2 = r2 + r4
            r6.cacheSizeLeft = r2
            r0.dispose()
        L_0x002c:
            boolean r0 = r7.isEmpty()
            if (r0 != 0) goto L_0x000e
            java.lang.Object r0 = r7.first()
            com.artifex.solib.SOBitmap r0 = (com.artifex.solib.SOBitmap) r0
            goto L_0x000f
        L_0x0039:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.LayerBitmapManager.clearList(java.util.TreeSet):void");
    }

    public synchronized void dispose() {
        clearAllBitmaps();
        this.fullBitmaps = null;
        this.colourBitmaps = null;
        this.alphaBitmaps = null;
        this.mip = null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00bb, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized SOBitmap get(int r12, com.artifex.solib.ArDkBitmap.Type r13) {
        /*
            r11 = this;
            monitor-enter(r11)
            int r0 = r12 * r12
            int[] r1 = com.artifex.sonui.editor.LayerBitmapManager.AnonymousClass1.$SwitchMap$com$artifex$solib$ArDkBitmap$Type     // Catch:{ all -> 0x00c3 }
            int r2 = r13.ordinal()     // Catch:{ all -> 0x00c3 }
            r1 = r1[r2]     // Catch:{ all -> 0x00c3 }
            r2 = 2
            r3 = 1
            if (r1 == r3) goto L_0x0017
            if (r1 == r2) goto L_0x0014
            java.util.TreeSet<com.artifex.solib.SOBitmap> r1 = r11.fullBitmaps     // Catch:{ all -> 0x00c3 }
            goto L_0x0019
        L_0x0014:
            java.util.TreeSet<com.artifex.solib.SOBitmap> r1 = r11.colourBitmaps     // Catch:{ all -> 0x00c3 }
            goto L_0x0019
        L_0x0017:
            java.util.TreeSet<com.artifex.solib.SOBitmap> r1 = r11.alphaBitmaps     // Catch:{ all -> 0x00c3 }
        L_0x0019:
            r4 = 0
            if (r1 != 0) goto L_0x001e
            monitor-exit(r11)
            return r4
        L_0x001e:
            boolean r5 = r1.isEmpty()     // Catch:{ all -> 0x00c3 }
            if (r5 != 0) goto L_0x002b
            java.lang.Object r5 = r1.first()     // Catch:{ all -> 0x00c3 }
            com.artifex.solib.SOBitmap r5 = (com.artifex.solib.SOBitmap) r5     // Catch:{ all -> 0x00c3 }
            goto L_0x002c
        L_0x002b:
            r5 = r4
        L_0x002c:
            if (r5 == 0) goto L_0x007b
            android.graphics.Bitmap r6 = r5.bitmap     // Catch:{ all -> 0x00c3 }
            int r7 = r6.getWidth()     // Catch:{ all -> 0x00c3 }
            int r8 = r6.getHeight()     // Catch:{ all -> 0x00c3 }
            int r7 = r7 * r8
            if (r0 > r7) goto L_0x0074
            r1.remove(r5)     // Catch:{ all -> 0x00c3 }
            long r7 = r11.cacheSizeLeft     // Catch:{ all -> 0x00c3 }
            int r13 = r6.getByteCount()     // Catch:{ all -> 0x00c3 }
            long r9 = (long) r13     // Catch:{ all -> 0x00c3 }
            long r7 = r7 + r9
            r11.cacheSizeLeft = r7     // Catch:{ all -> 0x00c3 }
            int r13 = r11.reuseCount     // Catch:{ all -> 0x00c3 }
            int r13 = r13 + r3
            r11.reuseCount = r13     // Catch:{ all -> 0x00c3 }
            java.util.TreeSet<com.artifex.solib.SOBitmap> r13 = r11.fullBitmaps     // Catch:{ all -> 0x00c3 }
            if (r1 != r13) goto L_0x0058
            int r13 = r11.fullReuse     // Catch:{ all -> 0x00c3 }
            int r13 = r13 + r3
            r11.fullReuse = r13     // Catch:{ all -> 0x00c3 }
            goto L_0x0067
        L_0x0058:
            java.util.TreeSet<com.artifex.solib.SOBitmap> r13 = r11.colourBitmaps     // Catch:{ all -> 0x00c3 }
            if (r1 != r13) goto L_0x0062
            int r13 = r11.colourReuse     // Catch:{ all -> 0x00c3 }
            int r13 = r13 + r3
            r11.colourReuse = r13     // Catch:{ all -> 0x00c3 }
            goto L_0x0067
        L_0x0062:
            int r13 = r11.alphaReuse     // Catch:{ all -> 0x00c3 }
            int r13 = r13 + r3
            r11.alphaReuse = r13     // Catch:{ all -> 0x00c3 }
        L_0x0067:
            com.artifex.solib.SOBitmap r13 = new com.artifex.solib.SOBitmap     // Catch:{ all -> 0x00c3 }
            r2 = 0
            r3 = 0
            r0 = r13
            r1 = r5
            r4 = r12
            r5 = r12
            r0.<init>(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x00c3 }
            monitor-exit(r11)
            return r13
        L_0x0074:
            java.lang.Object r5 = r1.higher(r5)     // Catch:{ all -> 0x00c3 }
            com.artifex.solib.SOBitmap r5 = (com.artifex.solib.SOBitmap) r5     // Catch:{ all -> 0x00c3 }
            goto L_0x002c
        L_0x007b:
            r11.makeRoomForBitmap(r0, r1)     // Catch:{ all -> 0x00c3 }
            com.artifex.sonui.editor.MemoryInfoProvider r5 = r11.mip     // Catch:{ all -> 0x00c3 }
            if (r5 == 0) goto L_0x00bc
            int[] r6 = com.artifex.sonui.editor.LayerBitmapManager.AnonymousClass1.$SwitchMap$com$artifex$solib$ArDkBitmap$Type     // Catch:{ all -> 0x00c3 }
            int r7 = r13.ordinal()     // Catch:{ all -> 0x00c3 }
            r6 = r6[r7]     // Catch:{ all -> 0x00c3 }
            if (r6 == r2) goto L_0x0092
            r2 = 3
            if (r6 == r2) goto L_0x0091
            r2 = 1
            goto L_0x0092
        L_0x0091:
            r2 = 4
        L_0x0092:
            int r0 = r0 * r2
            long r6 = (long) r0     // Catch:{ all -> 0x00c3 }
            boolean r0 = r5.checkMemoryAvailable(r6)     // Catch:{ all -> 0x00c3 }
            if (r0 == 0) goto L_0x009c
            goto L_0x00bc
        L_0x009c:
            int r12 = r11.oomCount     // Catch:{ all -> 0x00c3 }
            int r12 = r12 + r3
            r11.oomCount = r12     // Catch:{ all -> 0x00c3 }
            java.util.TreeSet<com.artifex.solib.SOBitmap> r12 = r11.fullBitmaps     // Catch:{ all -> 0x00c3 }
            if (r1 != r12) goto L_0x00ab
            int r12 = r11.fullOom     // Catch:{ all -> 0x00c3 }
            int r12 = r12 + r3
            r11.fullOom = r12     // Catch:{ all -> 0x00c3 }
            goto L_0x00ba
        L_0x00ab:
            java.util.TreeSet<com.artifex.solib.SOBitmap> r12 = r11.colourBitmaps     // Catch:{ all -> 0x00c3 }
            if (r1 != r12) goto L_0x00b5
            int r12 = r11.colourOom     // Catch:{ all -> 0x00c3 }
            int r12 = r12 + r3
            r11.colourOom = r12     // Catch:{ all -> 0x00c3 }
            goto L_0x00ba
        L_0x00b5:
            int r12 = r11.alphaOom     // Catch:{ all -> 0x00c3 }
            int r12 = r12 + r3
            r11.alphaOom = r12     // Catch:{ all -> 0x00c3 }
        L_0x00ba:
            monitor-exit(r11)
            return r4
        L_0x00bc:
            com.artifex.solib.SOBitmap r0 = new com.artifex.solib.SOBitmap     // Catch:{ all -> 0x00c3 }
            r0.<init>(r12, r12, r13)     // Catch:{ all -> 0x00c3 }
            monitor-exit(r11)
            return r0
        L_0x00c3:
            r12 = move-exception
            monitor-exit(r11)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.LayerBitmapManager.get(int, com.artifex.solib.ArDkBitmap$Type):com.artifex.solib.SOBitmap");
    }

    public final boolean makeRoomForBitmap(int i, TreeSet<SOBitmap> treeSet) {
        boolean z = false;
        while (!treeSet.isEmpty() && ((long) i) > this.cacheSizeLeft) {
            SOBitmap first = treeSet.first();
            treeSet.remove(first);
            this.cacheSizeLeft += (long) first.getBitmap().getByteCount();
            first.dispose();
            this.evictionCount++;
            if (treeSet == this.fullBitmaps) {
                this.fullEvicted++;
            } else if (treeSet == this.colourBitmaps) {
                this.colourEvicted++;
            } else {
                this.alphaEvicted++;
            }
            z = true;
        }
        if (z) {
            Runtime.getRuntime().gc();
        }
        if (((long) i) <= this.cacheSizeLeft) {
            return true;
        }
        return false;
    }

    public void setMemoryInfoProvider(MemoryInfoProvider memoryInfoProvider) {
        this.mip = memoryInfoProvider;
    }
}
