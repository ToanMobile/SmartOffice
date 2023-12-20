package com.artifex.solib;

public class SOTransition {
    public final int type;
    public Object mDirection;
    public int mDuration;
    public Object mRawValue;
    public Object mSpeed;
    public Object mType;

    public SOTransition() {
        this.type = 1;
        this.mDuration = -1;
    }

    public String toString() {
        if (this.type == 1) {
            StringBuilder sb = new StringBuilder(200);
            sb.append("<<\n");
            sb.append(" mode: ");
            sb.append(this.mRawValue);
            sb.append("\n ecLevel: ");
            sb.append(this.mType);
            sb.append("\n version: ");
            sb.append(this.mDirection);
            sb.append("\n maskPattern: ");
            sb.append(this.mDuration);
            if (this.mSpeed == null) {
                sb.append("\n matrix: null\n");
            } else {
                sb.append("\n matrix:\n");
                sb.append(this.mSpeed);
            }
            sb.append(">>\n");
            return sb.toString();
        }
        return super.toString();
    }

    public SOTransition(String str) {
        this.type = 0;
        this.mType = "";
        this.mDirection = "";
        this.mSpeed = "";
        this.mDuration = 0;
        this.mRawValue = str;
        if (str != null) {
            String[] split = str.split(";");
            for (String trim : split) {
                String[] split2 = trim.trim().split(":");
                if (split2[0].trim().equals("type")) {
                    this.mType = split2[1].trim();
                }
                if (split2[0].trim().equals("direction")) {
                    this.mDirection = split2[1].trim();
                }
                if (split2[0].trim().equals("speed")) {
                    String trim2 = split2[1].trim();
                    this.mSpeed = trim2;
                    this.mDuration = 500;
                    if (trim2.equals("veryslow")) {
                        this.mDuration = 5000;
                    }
                    if (this.mSpeed.equals("slow")) {
                        this.mDuration = 1000;
                    }
                    if (this.mSpeed.equals("medium")) {
                        this.mDuration = 750;
                    }
                    if (this.mSpeed.equals("veryfast")) {
                        this.mDuration = 300;
                    }
                    if (this.mSpeed.equals("fastest")) {
                        this.mDuration = 75;
                    }
                }
            }
        }
    }
}
