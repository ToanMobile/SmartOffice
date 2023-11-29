package com.artifex.sonui.editor;

import android.util.Log;
import androidx.transition.ViewUtilsBase$$ExternalSyntheticOutline1;
import com.artifex.mupdf.fitz.PKCS7DistinguishedName;
import com.google.android.gms.common.Scopes;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class NUICertificate {
    public static final int cRLSign = 6;
    public static final int dataEncipherment = 3;
    public static final int decipherOnly = 8;
    public static final int digitalSignature = 0;
    public static final int encipherOnly = 7;
    public static final int keyAgreement = 4;
    public static final int keyCertSign = 5;
    public static final int keyEncipherment = 2;
    public static final int nonRepudiation = 1;
    public String SHA1;
    public String SHA256;
    public String alias;
    public HashMap<String, String> details = null;
    public List<String> extKeyUsage;
    public String extendedUsage;
    public boolean isValid;
    public String issuer;
    public String notAfter;
    public String notBefore;
    public PrivateKey privKey = null;
    public X509Certificate publicCert = null;
    public String serial;
    public String subject;
    public String subjectAlt;
    public String usage;

    public NUICertificate() {
        init();
    }

    public static HashMap<String, String> defaultDetails() {
        HashMap<String, String> m = ViewUtilsBase$$ExternalSyntheticOutline1.m("EMAIL", " - ", "CN", " - ");
        m.put("O", " - ");
        m.put("OU", " - ");
        m.put("C", " - ");
        m.put("S", " - ");
        m.put("L", " - ");
        m.put("EMAIL", " - ");
        m.put("I_CN", " - ");
        m.put("I_O", " - ");
        m.put("I_OU", " - ");
        m.put("KEYUSAGE", " - ");
        m.put("EXTENDEDKEYUSAGE", " - ");
        m.put("SERIAL", " - ");
        m.put("SHA1", " - ");
        m.put("SHA256", " - ");
        m.put("NOTBEFORE", " - ");
        m.put("NOTAFTER", " - ");
        return m;
    }

    public HashMap<String, String> distinguishedName() {
        HashMap<String, String> hashMap = this.details;
        if (hashMap != null) {
            return hashMap;
        }
        HashMap<String, String> parseLDAPfield = parseLDAPfield(this.subject, "=");
        HashMap<String, String> parseLDAPfield2 = parseLDAPfield(this.subjectAlt, ":");
        this.details = new HashMap<>();
        String str = parseLDAPfield2.get(Scopes.EMAIL);
        if (str != null) {
            this.details.put("EMAIL", str);
        }
        String str2 = parseLDAPfield.get("CN");
        if (str2 != null) {
            this.details.put("CN", str2);
        }
        String str3 = parseLDAPfield.get("O");
        if (str3 != null) {
            this.details.put("O", str3);
        }
        String str4 = parseLDAPfield.get("OU");
        if (str4 != null) {
            this.details.put("OU", str4);
        }
        String str5 = parseLDAPfield.get("C");
        if (str5 != null) {
            this.details.put("C", str5);
        }
        String str6 = parseLDAPfield.get("emailAddress");
        if (str6 != null) {
            this.details.put("EMAIL", str6);
        }
        return this.details;
    }

    public boolean fromCertificate(X509Certificate x509Certificate) {
        String str;
        try {
            this.subject = x509Certificate.getSubjectDN().getName();
            Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames == null) {
                str = "";
            } else {
                str = subjectAlternativeNames.toString();
            }
            this.subjectAlt = str;
            x509Certificate.getKeyUsage();
            this.extKeyUsage = x509Certificate.getExtendedKeyUsage();
            this.publicCert = x509Certificate;
            return true;
        } catch (CertificateException unused) {
            Log.e("NUICertificate", "Failed to parse certificate");
            init();
            return false;
        }
    }

    public HashMap<String, String> info() {
        HashMap<String, String> hashMap = new HashMap<>();
        String str = this.serial;
        if (str != null) {
            hashMap.put("SERIAL", str);
        }
        String str2 = this.SHA1;
        if (str2 != null) {
            hashMap.put("SHA1", str2);
        }
        String str3 = this.SHA256;
        if (str3 != null) {
            hashMap.put("SHA1", str3);
        }
        return hashMap;
    }

    public void init() {
        this.alias = null;
        this.issuer = null;
        this.subject = null;
        this.subjectAlt = null;
        this.serial = null;
        this.isValid = false;
        this.notBefore = null;
        this.notAfter = null;
        this.usage = null;
        this.extendedUsage = null;
        this.details = null;
    }

    public HashMap<String, String> parseLDAPfield(String str, String str2) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (str != null) {
            for (String replaceAll : str.replaceAll("([^\\\\])([+])", "$1,").split(",")) {
                String replaceAll2 = replaceAll.replaceAll("[\\n\\t ]", "");
                if (replaceAll2.length() != 0) {
                    String[] split = replaceAll2.split(str2);
                    if (split.length > 1) {
                        hashMap.put(split[0].trim(), split[1].trim());
                    }
                }
            }
        }
        return hashMap;
    }

    public PKCS7DistinguishedName pkcs7DistinguishedName() {
        PKCS7DistinguishedName pKCS7DistinguishedName = new PKCS7DistinguishedName();
        HashMap<String, String> distinguishedName = distinguishedName();
        pKCS7DistinguishedName.c = distinguishedName.get("C");
        pKCS7DistinguishedName.cn = distinguishedName.get("CN");
        pKCS7DistinguishedName.o = distinguishedName.get("O");
        pKCS7DistinguishedName.ou = distinguishedName.get("OU");
        pKCS7DistinguishedName.email = distinguishedName.get("EMAIL");
        return pKCS7DistinguishedName;
    }

    public HashMap<String, String> v3Extensions() {
        HashMap<String, String> hashMap = new HashMap<>();
        String str = this.usage;
        if (str != null) {
            hashMap.put("KEYUSAGE", str);
        }
        String str2 = this.extendedUsage;
        if (str2 != null) {
            hashMap.put("EXTENDEDKEYUSAGE", str2);
        }
        return hashMap;
    }

    public HashMap<String, String> validity() {
        HashMap<String, String> hashMap = new HashMap<>();
        String str = this.notAfter;
        if (str != null) {
            hashMap.put("NOTAFTER", str);
        }
        String str2 = this.notBefore;
        if (str2 != null) {
            hashMap.put("NOTBEFORE", str2);
        }
        return hashMap;
    }
}
