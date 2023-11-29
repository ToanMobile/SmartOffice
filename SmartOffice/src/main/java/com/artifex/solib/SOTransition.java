package com.artifex.solib;

import com.bytedance.sdk.openadsdk.api.PAGErrorCode;
import com.google.logging.type.LogSeverity;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;
import com.vungle.warren.model.CacheBustDBAdapter;
import kotlinx.coroutines.internal.ArrayQueue;

public class SOTransition {
    public final /* synthetic */ int $r8$classId;
    public Object mDirection;
    public int mDuration;
    public Object mRawValue;
    public Object mSpeed;
    public Object mType;

    public SOTransition() {
        this.$r8$classId = 1;
        this.mDuration = -1;
    }

    public String toString() {
        switch (this.$r8$classId) {
            case 1:
                StringBuilder sb = new StringBuilder(200);
                sb.append("<<\n");
                sb.append(" mode: ");
                sb.append((Mode) this.mRawValue);
                sb.append("\n ecLevel: ");
                sb.append((ErrorCorrectionLevel) this.mType);
                sb.append("\n version: ");
                sb.append((Version) this.mDirection);
                sb.append("\n maskPattern: ");
                sb.append(this.mDuration);
                if (((ArrayQueue) this.mSpeed) == null) {
                    sb.append("\n matrix: null\n");
                } else {
                    sb.append("\n matrix:\n");
                    sb.append((ArrayQueue) this.mSpeed);
                }
                sb.append(">>\n");
                return sb.toString();
            default:
                return super.toString();
        }
    }

    public SOTransition(String str) {
        this.$r8$classId = 0;
        this.mType = "";
        this.mDirection = "";
        this.mSpeed = "";
        this.mDuration = 0;
        this.mRawValue = str;
        if (str != null) {
            String[] split = str.split(CacheBustDBAdapter.DELIMITER);
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
                    this.mDuration = LogSeverity.ERROR_VALUE;
                    if (trim2.equals("veryslow")) {
                        this.mDuration = PAGErrorCode.LOAD_FACTORY_NULL_CODE;
                    }
                    if (((String) this.mSpeed).equals("slow")) {
                        this.mDuration = 1000;
                    }
                    if (((String) this.mSpeed).equals("medium")) {
                        this.mDuration = 750;
                    }
                    if (((String) this.mSpeed).equals("veryfast")) {
                        this.mDuration = LogSeverity.NOTICE_VALUE;
                    }
                    if (((String) this.mSpeed).equals("fastest")) {
                        this.mDuration = 75;
                    }
                }
            }
        }
    }
}
