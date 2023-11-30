package com.artifex.source.util.a.util_a.b.g;

import com.artifex.source.util.a.util_a.a.a.c$$ExternalSyntheticOutline0;
import com.bytedance.sdk.openadsdk.api.PAGErrorCode;
import com.yandex.metrica.YandexMetricaDefaultValues;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.spongycastle.crypto.Digest;
import org.spongycastle.crypto.digests.SHA3Digest;
import org.spongycastle.crypto.digests.SHA512tDigest;

public class a {
    public static byte[] a(String str, byte[] bArr) {
        URL url;
        try {
            url = new URL(str);
        } catch (Throwable unused) {
            url = null;
        }
        try {
            "https".equals(url.getProtocol().toLowerCase());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
            httpURLConnection.setConnectTimeout(YandexMetricaDefaultValues.DEFAULT_MAX_REPORTS_COUNT_UPPER_BOUND);
            httpURLConnection.setReadTimeout(PAGErrorCode.LOAD_FACTORY_NULL_CODE);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(bArr);
            outputStream.flush();
            if (httpURLConnection.getResponseCode() != 200) {
                return null;
            }
            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr2, 0, 1024);
                if (read == -1) {
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr2, 0, read);
            }
        } catch (Throwable unused2) {
            return null;
        }
    }

    public static int ceil(float f) {
        return ((int) (((double) f) + 16384.999999999996d)) - 16384;
    }

    public static Digest createSHA3_224() {
        return new SHA3Digest(224);
    }

    public static Digest createSHA3_256() {
        return new SHA3Digest(256);
    }

    public static Digest createSHA3_384() {
        return new SHA3Digest(384);
    }

    public static Digest createSHA3_512() {
        return new SHA3Digest(512);
    }

    public static Digest createSHA512_224() {
        return new SHA512tDigest(224);
    }

    public static Digest createSHA512_256() {
        return new SHA512tDigest(256);
    }

    public static AssertionError fail(String str, Object... objArr) {
        StringBuilder m = c$$ExternalSyntheticOutline0.m("INTERNAL ASSERTION FAILED: ");
        m.append(String.format(str, objArr));
        throw new AssertionError(m.toString());
    }

    public static int floor(float f) {
        return ((int) (((double) f) + 16384.0d)) - 16384;
    }

    public static void hardAssert(boolean z, String str, Object... objArr) {
        if (!z) {
            fail(str, objArr);
            throw null;
        }
    }

    public static <T> T hardAssertNonNull(T t, String str, Object... objArr) {
        if (t != null) {
            return t;
        }
        fail(str, objArr);
        throw null;
    }

    public static float max(float f, float f2) {
        return f > f2 ? f2 : f;
    }

    public static float min(float f, float f2) {
        return f < f2 ? f2 : f;
    }

    public static AssertionError fail(Throwable th, String str, Object... objArr) {
        StringBuilder m = c$$ExternalSyntheticOutline0.m("INTERNAL ASSERTION FAILED: ");
        m.append(String.format(str, objArr));
        AssertionError assertionError = new AssertionError(m.toString());
        assertionError.initCause(th);
        throw assertionError;
    }
}
