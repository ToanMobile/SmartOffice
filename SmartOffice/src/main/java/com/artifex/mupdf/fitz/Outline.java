package com.artifex.mupdf.fitz;

public class Outline {
    public Outline[] down;
    public String title;
    public String uri;

    public Outline(String str, String str2, Outline[] outlineArr) {
        this.title = str;
        this.uri = str2;
        this.down = outlineArr;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.title);
        stringBuffer.append(' ');
        stringBuffer.append(this.uri);
        stringBuffer.append(10);
        if (this.down != null) {
            for (Outline append : this.down) {
                stringBuffer.append(9);
                stringBuffer.append(append);
                stringBuffer.append(10);
            }
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }
}
