package com.artifex.mupdf.fitz;

public class Context {
    private static boolean inited = false;
    private static final Object lock = new Object();
    private static Log log;

    public interface Log {
        void error(String str);

        void warning(String str);
    }

    public static class Version {
        public int major;
        public int minor;
        public int patch;
        public String version;
    }

    static {
        init();
    }

    public static native void disableICC();

    public static native void emptyStore();

    public static native void enableICC();

    public static native Version getVersion();

    public static void init() {
        if (!inited) {
            inited = true;
            try {
                System.loadLibrary("mupdf_java");
            } catch (UnsatisfiedLinkError unused) {
                try {
                    System.loadLibrary("mupdf_java64");
                } catch (UnsatisfiedLinkError unused2) {
                    System.loadLibrary("mupdf_java32");
                }
            }
            if (initNative() < 0) {
                throw new RuntimeException("cannot initialize mupdf library");
            }
        }
    }

    private static native int initNative();

    public static native void setAntiAliasLevel(int i);

    public static void setLog(Log log2) {
        synchronized (lock) {
            log = log2;
        }
    }

    public static native void setUserCSS(String str);

    public static native void useDocumentCSS(boolean z);
}
