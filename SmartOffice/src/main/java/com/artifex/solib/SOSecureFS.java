package com.artifex.solib;

public interface SOSecureFS {

    public static class FileAttributes {
        public boolean isDirectory;
        public boolean isHidden;
        public boolean isSystem;
        public boolean isWriteable;
        public long lastModified;
        public long length;
    }

    boolean closeFile(Object obj);

    boolean copyFile(String str, String str2);

    boolean createDirectory(String str);

    boolean createFile(String str);

    boolean deleteFile(String str);

    boolean fileExists(String str);

    FileAttributes getFileAttributes(String str);

    Object getFileHandleForReading(String str);

    Object getFileHandleForUpdating(String str);

    Object getFileHandleForWriting(String str);

    long getFileLength(Object obj);

    long getFileOffset(Object obj);

    String getSecurePath();

    String getSecurePrefix();

    String getTempPath();

    boolean isSecurePath(String str);

    int readFromFile(Object obj, byte[] bArr);

    boolean recursivelyRemoveDirectory(String str);

    boolean renameFile(String str, String str2);

    boolean seekToFileOffset(Object obj, long j);

    boolean setFileLength(Object obj, long j);

    boolean syncFile(Object obj);

    int writeToFile(Object obj, byte[] bArr);
}
