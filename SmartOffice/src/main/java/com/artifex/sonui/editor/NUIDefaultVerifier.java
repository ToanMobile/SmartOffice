package com.artifex.sonui.editor;

import a.a.a.a.a.c$$ExternalSyntheticOutline0;
import android.app.Activity;
import android.content.Intent;
import androidx.transition.ViewOverlayApi18;
import com.artifex.mupdf.fitz.FitzInputStream;
import com.artifex.mupdf.fitz.PDFWidget;
import com.artifex.solib.FileUtils$$ExternalSyntheticOutline0;
import com.artifex.sonui.editor.NUIPKCS7Verifier;
import com.yandex.metrica.c;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Objects;
import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1GeneralizedTime;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1Set;
import org.spongycastle.asn1.ASN1UTCTime;
import org.spongycastle.asn1.DERNull;
import org.spongycastle.asn1.cms.Attribute;
import org.spongycastle.asn1.cms.CMSAlgorithmProtection;
import org.spongycastle.asn1.cms.CMSAttributes;
import org.spongycastle.asn1.cms.ContentInfo;
import org.spongycastle.asn1.cms.SignerInfo;
import org.spongycastle.asn1.cms.Time;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.Certificate;
import org.spongycastle.asn1.x509.DigestInfo;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spongycastle.cert.jcajce.JcaX509CertificateHolder;
import org.spongycastle.cms.CMSException;
import org.spongycastle.cms.CMSProcessableByteArray;
import org.spongycastle.cms.CMSSignedData;
import org.spongycastle.cms.CMSSignedHelper;
import org.spongycastle.cms.CMSSignerDigestMismatchException;
import org.spongycastle.cms.CMSTypedData;
import org.spongycastle.cms.CMSUtils;
import org.spongycastle.cms.CMSVerifierCertificateNotValidException;
import org.spongycastle.cms.SignerInformation;
import org.spongycastle.cms.SignerInformationStore;
import org.spongycastle.cms.SignerInformationVerifier;
import org.spongycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.operator.ContentVerifier;
import org.spongycastle.operator.ContentVerifierProvider;
import org.spongycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.spongycastle.operator.DigestCalculator;
import org.spongycastle.operator.DigestCalculatorProvider;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.RawContentVerifier;
import org.spongycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.spongycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.spongycastle.operator.jcajce.OperatorHelper;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.CollectionStore;
import org.spongycastle.util.io.TeeOutputStream;

public class NUIDefaultVerifier extends NUIPKCS7Verifier {
    public Activity mActivity;
    public NUICertificate mCertificate;
    public NUIPKCS7VerifierListener mListener;
    public int mResult = -1;
    public Class mResultViewerClass;

    public NUIDefaultVerifier(Activity activity, Class cls) {
        this.mActivity = activity;
        this.mResultViewerClass = cls;
        Security.addProvider(new BouncyCastleProvider());
    }

    public void certificateUpdated() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x006e, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x009a, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x009d, code lost:
        r7.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00a1, code lost:
        r7 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00a3, code lost:
        r7 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00a4, code lost:
        r7.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        return 6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return 6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0070 */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x009a A[ExcHandler: IOException | NoSuchAlgorithmException (r7v8 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:5:0x000e] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00a1 A[ExcHandler: KeyStoreException (e java.security.KeyStoreException), Splitter:B:5:0x000e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int checkCertificate(byte[] r7) {
        /*
            r6 = this;
            java.lang.String r7 = "sign"
            com.artifex.sonui.editor.NUICertificate r0 = r6.mCertificate
            if (r0 == 0) goto L_0x00a9
            java.security.cert.X509Certificate r0 = r0.publicCert
            if (r0 != 0) goto L_0x000c
            goto L_0x00a9
        L_0x000c:
            java.lang.String r0 = "AndroidCAStore"
            java.security.KeyStore r0 = java.security.KeyStore.getInstance(r0)     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r1 = 0
            r0.load(r1, r1)     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.util.Enumeration r1 = r0.aliases()     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
        L_0x001a:
            boolean r2 = r1.hasMoreElements()     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            if (r2 == 0) goto L_0x00a7
            java.lang.Object r2 = r1.nextElement()     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.security.cert.Certificate r3 = r0.getCertificate(r2)     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.security.cert.X509Certificate r3 = (java.security.cert.X509Certificate) r3     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r3.checkValidity()     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r4.<init>()     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r5 = "Issuer: "
            r4.append(r5)     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.security.Principal r5 = r3.getIssuerDN()     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r5 = r5.getName()     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r4.append(r5)     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r4 = r4.toString()     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            android.util.Log.v(r7, r4)     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r4.<init>()     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r5 = "Cert: "
            r4.append(r5)     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r5 = r3.toString()     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r4.append(r5)     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r4 = r4.toString()     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            android.util.Log.v(r7, r4)     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            com.artifex.sonui.editor.NUICertificate r4 = r6.mCertificate     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.security.cert.X509Certificate r4 = r4.publicCert     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.security.PublicKey r3 = r3.getPublicKey()     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r4.verify(r3)     // Catch:{ InvalidKeyException | NoSuchProviderException | CertificateException -> 0x0085, SignatureException -> 0x0070, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r7 = 0
            return r7
        L_0x0070:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r3.<init>()     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r4 = "Invalid signature: "
            r3.append(r4)     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r3.append(r2)     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r2 = r3.toString()     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            android.util.Log.v(r7, r2)     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            goto L_0x001a
        L_0x0085:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r3.<init>()     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r4 = "No certificate chain found for: "
            r3.append(r4)     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            r3.append(r2)     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            java.lang.String r2 = r3.toString()     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            android.util.Log.v(r7, r2)     // Catch:{ CertificateException -> 0x00a3, KeyStoreException -> 0x00a1, IOException -> 0x009c, NoSuchAlgorithmException -> 0x009a }
            goto L_0x001a
        L_0x009a:
            r7 = move-exception
            goto L_0x009d
        L_0x009c:
            r7 = move-exception
        L_0x009d:
            r7.printStackTrace()
            goto L_0x00a7
        L_0x00a1:
            r7 = move-exception
            goto L_0x00a4
        L_0x00a3:
            r7 = move-exception
        L_0x00a4:
            r7.printStackTrace()
        L_0x00a7:
            r7 = 6
            return r7
        L_0x00a9:
            r7 = 2
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.NUIDefaultVerifier.checkCertificate(byte[]):int");
    }

    public int checkDigest(FitzInputStream fitzInputStream, byte[] bArr) {
        HashMap<String, String> hashMap;
        HashMap<String, String> validity;
        HashMap<String, String> v3Extensions;
        byte[] bArr2 = new byte[PDFWidget.PDF_BTN_FIELD_IS_NO_TOGGLE_TO_OFF];
        this.mCertificate = new NUICertificate();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = fitzInputStream.read(bArr2, 0, PDFWidget.PDF_BTN_FIELD_IS_NO_TOGGLE_TO_OFF);
            if (read <= 0) {
                break;
            }
            byteArrayOutputStream.write(bArr2, 0, read);
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            this.mResult = 3;
            if (verifySignedData(byteArray, bArr)) {
                this.mResult = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.mResult == 0) {
            this.mResult = checkCertificate(bArr);
        }
        NUICertificate nUICertificate = this.mCertificate;
        if (nUICertificate != null) {
            hashMap = nUICertificate.distinguishedName();
        } else {
            hashMap = NUICertificate.defaultDetails();
        }
        NUICertificate nUICertificate2 = this.mCertificate;
        if (!(nUICertificate2 == null || (v3Extensions = nUICertificate2.v3Extensions()) == null)) {
            hashMap.putAll(v3Extensions);
        }
        NUICertificate nUICertificate3 = this.mCertificate;
        if (!(nUICertificate3 == null || (validity = nUICertificate3.validity()) == null)) {
            hashMap.putAll(validity);
        }
        NUIPKCS7VerifierListener nUIPKCS7VerifierListener = this.mListener;
        if (nUIPKCS7VerifierListener != null) {
            nUIPKCS7VerifierListener.onVerifyResult(hashMap, this.mResult);
        }
        presentResults(hashMap, this.mResult);
        return this.mResult;
    }

    public void doVerify(NUIPKCS7VerifierListener nUIPKCS7VerifierListener, int i) {
        this.mListener = nUIPKCS7VerifierListener;
        this.mSignatureValidity = i;
        nUIPKCS7VerifierListener.onInitComplete();
    }

    public void presentResults(HashMap<String, String> hashMap, int i) {
        Intent intent = new Intent(this.mActivity, this.mResultViewerClass);
        intent.putExtra("certificateDetails", hashMap);
        intent.putExtra("verifyResult", i);
        intent.putExtra("updatedSinceSigning", this.mSignatureValidity);
        this.mActivity.startActivity(intent);
    }

    public boolean verifySignedData(byte[] bArr, byte[] bArr2) throws Exception {
        CollectionStore collectionStore;
        Time time;
        Date date;
        CMSProcessableByteArray cMSProcessableByteArray = new CMSProcessableByteArray(bArr);
        int i = CMSUtils.$r8$clinit;
        try {
            ContentInfo instance = ContentInfo.getInstance(new ASN1InputStream(bArr2).readObject());
            if (instance != null) {
                CMSSignedData cMSSignedData = new CMSSignedData(cMSProcessableByteArray, instance);
                CMSSignedHelper cMSSignedHelper = CMSSignedData.HELPER;
                ASN1Set aSN1Set = cMSSignedData.signedData.certificates;
                Objects.requireNonNull(cMSSignedHelper);
                if (aSN1Set != null) {
                    ArrayList arrayList = new ArrayList(aSN1Set.size());
                    Enumeration objects = aSN1Set.getObjects();
                    while (objects.hasMoreElements()) {
                        ASN1Primitive aSN1Primitive = ((ASN1Encodable) objects.nextElement()).toASN1Primitive();
                        if (aSN1Primitive instanceof ASN1Sequence) {
                            arrayList.add(new X509CertificateHolder(Certificate.getInstance(aSN1Primitive)));
                        }
                    }
                    collectionStore = new CollectionStore(arrayList);
                } else {
                    collectionStore = new CollectionStore(new ArrayList());
                }
                CMSAlgorithmProtection cMSAlgorithmProtection = null;
                int i2 = 0;
                if (cMSSignedData.signerInfoStore == null) {
                    ASN1Set aSN1Set2 = cMSSignedData.signedData.signerInfos;
                    ArrayList arrayList2 = new ArrayList();
                    for (int i3 = 0; i3 != aSN1Set2.size(); i3++) {
                        arrayList2.add(new SignerInformation(SignerInfo.getInstance(aSN1Set2.getObjectAt(i3)), cMSSignedData.signedData.contentInfo.contentType, cMSSignedData.signedContent, (byte[]) null));
                    }
                    cMSSignedData.signerInfoStore = new SignerInformationStore(arrayList2);
                }
                SignerInformationStore signerInformationStore = cMSSignedData.signerInfoStore;
                Objects.requireNonNull(signerInformationStore);
                SignerInformation signerInformation = (SignerInformation) new ArrayList(signerInformationStore.all).iterator().next();
                X509CertificateHolder x509CertificateHolder = (X509CertificateHolder) ((ArrayList) collectionStore.getMatches(signerInformation.sid)).iterator().next();
                JcaX509CertificateConverter jcaX509CertificateConverter = new JcaX509CertificateConverter();
                try {
                    Objects.requireNonNull(jcaX509CertificateConverter.helper);
                    this.mCertificate.fromCertificate((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(x509CertificateHolder.x509Certificate.getEncoded())));
                    JcaSimpleSignerInfoVerifierBuilder jcaSimpleSignerInfoVerifierBuilder = new JcaSimpleSignerInfoVerifierBuilder();
                    c cVar = new c(10);
                    DefaultSignatureAlgorithmIdentifierFinder defaultSignatureAlgorithmIdentifierFinder = new DefaultSignatureAlgorithmIdentifierFinder();
                    Objects.requireNonNull(jcaSimpleSignerInfoVerifierBuilder.helper);
                    JcaContentVerifierProviderBuilder jcaContentVerifierProviderBuilder = new JcaContentVerifierProviderBuilder();
                    OperatorHelper operatorHelper = jcaContentVerifierProviderBuilder.helper;
                    Objects.requireNonNull(operatorHelper);
                    try {
                        X509Certificate x509Certificate = (X509Certificate) operatorHelper.helper.createCertificateFactory("X.509").generateCertificate(new ByteArrayInputStream(x509CertificateHolder.x509Certificate.getEncoded()));
                        try {
                            JcaContentVerifierProviderBuilder.AnonymousClass1 r8 = new ContentVerifierProvider(new JcaX509CertificateHolder(x509Certificate), x509Certificate) {
                                public SignatureOutputStream stream;
                                public final /* synthetic */ X509CertificateHolder val$certHolder;
                                public final /* synthetic */ X509Certificate val$certificate;

                                {
                                    this.val$certHolder = r2;
                                    this.val$certificate = r3;
                                }
                            };
                            Objects.requireNonNull(jcaSimpleSignerInfoVerifierBuilder.helper);
                            SignerInformationVerifier signerInformationVerifier = new SignerInformationVerifier(cVar, defaultSignatureAlgorithmIdentifierFinder, r8, new DigestCalculatorProvider() {
                                public DigestCalculator get(final AlgorithmIdentifier algorithmIdentifier) throws OperatorCreationException {
                                    try {
                                        final DigestOutputStream digestOutputStream = new DigestOutputStream(JcaDigestCalculatorProviderBuilder.this, JcaDigestCalculatorProviderBuilder.this.helper.createDigest(algorithmIdentifier));
                                        return new DigestCalculator(this) {
                                            public byte[] getDigest() {
                                                return digestOutputStream.dig.digest();
                                            }
                                        };
                                    } catch (GeneralSecurityException e) {
                                        throw new OperatorCreationException("exception on setup: " + e, e);
                                    }
                                }
                            });
                            ASN1Primitive singleValuedSignedAttribute = signerInformation.getSingleValuedSignedAttribute(CMSAttributes.signingTime, "signing-time");
                            if (singleValuedSignedAttribute == null) {
                                time = null;
                            } else {
                                try {
                                    time = Time.getInstance(singleValuedSignedAttribute);
                                } catch (IllegalArgumentException unused) {
                                    throw new CMSException("signing-time attribute value not a valid 'Time' structure");
                                }
                            }
                            Objects.requireNonNull(signerInformationVerifier.verifierProvider);
                            if (time != null) {
                                X509CertificateHolder x509CertificateHolder2 = ((JcaContentVerifierProviderBuilder.AnonymousClass1) signerInformationVerifier.verifierProvider).val$certHolder;
                                try {
                                    ASN1Primitive aSN1Primitive2 = time.time;
                                    if (aSN1Primitive2 instanceof ASN1UTCTime) {
                                        date = ((ASN1UTCTime) aSN1Primitive2).getAdjustedDate();
                                    } else {
                                        date = ((ASN1GeneralizedTime) aSN1Primitive2).getDate();
                                    }
                                    if (!(!date.before(x509CertificateHolder2.x509Certificate.tbsCert.startDate.getDate()) && !date.after(x509CertificateHolder2.x509Certificate.tbsCert.endDate.getDate()))) {
                                        throw new CMSVerifierCertificateNotValidException("verifier not valid at signingTime");
                                    }
                                } catch (ParseException e) {
                                    StringBuilder m = c$$ExternalSyntheticOutline0.m("invalid date string: ");
                                    m.append(e.getMessage());
                                    throw new IllegalStateException(m.toString());
                                }
                            }
                            CMSSignedHelper cMSSignedHelper2 = CMSSignedHelper.INSTANCE;
                            String str = signerInformation.encryptionAlgorithm.algorithm.identifier;
                            String str2 = (String) ((HashMap) CMSSignedHelper.encryptionAlgs).get(str);
                            if (str2 != null) {
                                str = str2;
                            }
                            try {
                                ContentVerifier contentVerifier = signerInformationVerifier.getContentVerifier(signerInformation.encryptionAlgorithm, signerInformation.info.digAlgorithm);
                                try {
                                    JcaContentVerifierProviderBuilder.SignatureOutputStream signatureOutputStream = ((JcaContentVerifierProviderBuilder.SigVerifier) contentVerifier).stream;
                                    if (signatureOutputStream != null) {
                                        if (signerInformation.resultDigest == null) {
                                            DigestCalculator digestCalculator = ((JcaDigestCalculatorProviderBuilder.AnonymousClass1) signerInformationVerifier.digestProvider).get(signerInformation.digestAlgorithm);
                                            CMSTypedData cMSTypedData = signerInformation.content;
                                            if (cMSTypedData != null) {
                                                JcaDigestCalculatorProviderBuilder.DigestOutputStream digestOutputStream = digestOutputStream;
                                                if (signerInformation.signedAttributeSet != null) {
                                                    cMSTypedData.write(digestOutputStream);
                                                    signatureOutputStream.write(signerInformation.getEncodedSignedAttributes());
                                                } else if (contentVerifier instanceof RawContentVerifier) {
                                                    cMSTypedData.write(digestOutputStream);
                                                } else {
                                                    TeeOutputStream teeOutputStream = new TeeOutputStream(digestOutputStream, signatureOutputStream);
                                                    signerInformation.content.write(teeOutputStream);
                                                    teeOutputStream.close();
                                                }
                                                digestOutputStream.close();
                                            } else if (signerInformation.signedAttributeSet != null) {
                                                signatureOutputStream.write(signerInformation.getEncodedSignedAttributes());
                                            } else {
                                                throw new CMSException("data not encapsulated in signature - use detached constructor.");
                                            }
                                            signerInformation.resultDigest = ((JcaDigestCalculatorProviderBuilder.AnonymousClass1.AnonymousClass1) digestCalculator).getDigest();
                                        } else if (signerInformation.signedAttributeSet == null) {
                                            CMSTypedData cMSTypedData2 = signerInformation.content;
                                            if (cMSTypedData2 != null) {
                                                cMSTypedData2.write(signatureOutputStream);
                                            }
                                        } else {
                                            signatureOutputStream.write(signerInformation.getEncodedSignedAttributes());
                                        }
                                        signatureOutputStream.close();
                                        ASN1Primitive singleValuedSignedAttribute2 = signerInformation.getSingleValuedSignedAttribute(CMSAttributes.contentType, "content-type");
                                        if (singleValuedSignedAttribute2 == null) {
                                            if (!signerInformation.isCounterSignature && signerInformation.signedAttributeSet != null) {
                                                throw new CMSException("The content-type attribute type MUST be present whenever signed attributes are present in signed-data");
                                            }
                                        } else if (signerInformation.isCounterSignature) {
                                            throw new CMSException("[For counter signatures,] the signedAttributes field MUST NOT contain a content-type attribute");
                                        } else if (!(singleValuedSignedAttribute2 instanceof ASN1ObjectIdentifier)) {
                                            throw new CMSException("content-type attribute value not of ASN.1 type 'OBJECT IDENTIFIER'");
                                        } else if (!((ASN1ObjectIdentifier) singleValuedSignedAttribute2).equals(signerInformation.contentType)) {
                                            throw new CMSException("content-type attribute value does not match eContentType");
                                        }
                                        ASN1Set aSN1Set3 = signerInformation.signedAttributeSet;
                                        if (aSN1Set3 != null && signerInformation.signedAttributeValues == null) {
                                            signerInformation.signedAttributeValues = new ViewOverlayApi18(aSN1Set3);
                                        }
                                        ViewOverlayApi18 viewOverlayApi18 = signerInformation.signedAttributeValues;
                                        ViewOverlayApi18 unsignedAttributes = signerInformation.getUnsignedAttributes();
                                        if (unsignedAttributes == null || unsignedAttributes.getAll(CMSAttributes.cmsAlgorithmProtect).size() <= 0) {
                                            if (viewOverlayApi18 != null) {
                                                ViewOverlayApi18 all = viewOverlayApi18.getAll(CMSAttributes.cmsAlgorithmProtect);
                                                if (all.size() > 1) {
                                                    throw new CMSException("Only one instance of a cmsAlgorithmProtect attribute can be present");
                                                } else if (all.size() > 0) {
                                                    Attribute instance2 = Attribute.getInstance(all.get(0));
                                                    if (instance2.attrValues.size() == 1) {
                                                        Object obj = instance2.attrValues.toArray()[0];
                                                        if (obj instanceof CMSAlgorithmProtection) {
                                                            cMSAlgorithmProtection = (CMSAlgorithmProtection) obj;
                                                        } else if (obj != null) {
                                                            cMSAlgorithmProtection = new CMSAlgorithmProtection(ASN1Sequence.getInstance(obj));
                                                        }
                                                        if (!CMSUtils.isEquivalent(cMSAlgorithmProtection.digestAlgorithm, signerInformation.info.digAlgorithm)) {
                                                            throw new CMSException("CMS Algorithm Identifier Protection check failed for digestAlgorithm");
                                                        } else if (!CMSUtils.isEquivalent(cMSAlgorithmProtection.signatureAlgorithm, signerInformation.info.digEncryptionAlgorithm)) {
                                                            throw new CMSException("CMS Algorithm Identifier Protection check failed for signatureAlgorithm");
                                                        }
                                                    } else {
                                                        throw new CMSException("A cmsAlgorithmProtect attribute MUST contain exactly one value");
                                                    }
                                                }
                                            }
                                            ASN1Primitive singleValuedSignedAttribute3 = signerInformation.getSingleValuedSignedAttribute(CMSAttributes.messageDigest, "message-digest");
                                            if (singleValuedSignedAttribute3 == null) {
                                                if (signerInformation.signedAttributeSet != null) {
                                                    throw new CMSException("the message-digest signed attribute type MUST be present when there are any signed attributes present");
                                                }
                                            } else if (!(singleValuedSignedAttribute3 instanceof ASN1OctetString)) {
                                                throw new CMSException("message-digest attribute value not of ASN.1 type 'OCTET STRING'");
                                            } else if (!Arrays.constantTimeAreEqual(signerInformation.resultDigest, ((ASN1OctetString) singleValuedSignedAttribute3).getOctets())) {
                                                throw new CMSSignerDigestMismatchException("message-digest attribute value does not match calculated value");
                                            }
                                            if (viewOverlayApi18 == null || viewOverlayApi18.getAll(CMSAttributes.counterSignature).size() <= 0) {
                                                ViewOverlayApi18 unsignedAttributes2 = signerInformation.getUnsignedAttributes();
                                                if (unsignedAttributes2 != null) {
                                                    ViewOverlayApi18 all2 = unsignedAttributes2.getAll(CMSAttributes.counterSignature);
                                                    while (i2 < all2.size()) {
                                                        if (Attribute.getInstance(all2.get(i2)).attrValues.size() >= 1) {
                                                            i2++;
                                                        } else {
                                                            throw new CMSException("A countersignature attribute MUST contain at least one AttributeValue");
                                                        }
                                                    }
                                                }
                                                try {
                                                    if (signerInformation.signedAttributeSet != null || signerInformation.resultDigest == null || !(contentVerifier instanceof RawContentVerifier)) {
                                                        return contentVerifier.verify(signerInformation.getSignature());
                                                    }
                                                    RawContentVerifier rawContentVerifier = (RawContentVerifier) contentVerifier;
                                                    if (str.equals("RSA")) {
                                                        return rawContentVerifier.verify(new DigestInfo(new AlgorithmIdentifier(signerInformation.digestAlgorithm.algorithm, DERNull.INSTANCE), signerInformation.resultDigest).getEncoded("DER"), signerInformation.getSignature());
                                                    }
                                                    return rawContentVerifier.verify(signerInformation.resultDigest, signerInformation.getSignature());
                                                } catch (IOException e2) {
                                                    throw new CMSException("can't process mime object to create signature.", e2);
                                                }
                                            } else {
                                                throw new CMSException("A countersignature attribute MUST NOT be a signed attribute");
                                            }
                                        } else {
                                            throw new CMSException("A cmsAlgorithmProtect attribute MUST be a signed attribute");
                                        }
                                    } else {
                                        throw new IllegalStateException("verifier not initialised");
                                    }
                                } catch (IOException e3) {
                                    throw new CMSException("can't process mime object to create signature.", e3);
                                } catch (OperatorCreationException e4) {
                                    StringBuilder m2 = c$$ExternalSyntheticOutline0.m("can't create digest calculator: ");
                                    m2.append(e4.getMessage());
                                    throw new CMSException(m2.toString(), e4);
                                }
                            } catch (OperatorCreationException e5) {
                                StringBuilder m3 = c$$ExternalSyntheticOutline0.m("can't create content verifier: ");
                                m3.append(e5.getMessage());
                                throw new CMSException(m3.toString(), e5);
                            }
                        } catch (CertificateEncodingException e6) {
                            StringBuilder m4 = c$$ExternalSyntheticOutline0.m("cannot process certificate: ");
                            m4.append(e6.getMessage());
                            throw new OperatorCreationException(m4.toString(), e6);
                        }
                    } catch (IOException e7) {
                        throw new OperatorHelper.OpCertificateException(FileUtils$$ExternalSyntheticOutline0.m(e7, c$$ExternalSyntheticOutline0.m("cannot get encoded form of certificate: ")), e7);
                    } catch (NoSuchProviderException e8) {
                        StringBuilder m5 = c$$ExternalSyntheticOutline0.m("cannot find factory provider: ");
                        m5.append(e8.getMessage());
                        throw new OperatorHelper.OpCertificateException(m5.toString(), e8);
                    }
                } catch (IOException e9) {
                    throw new JcaX509CertificateConverter.ExCertificateParsingException(jcaX509CertificateConverter, FileUtils$$ExternalSyntheticOutline0.m(e9, c$$ExternalSyntheticOutline0.m("exception parsing certificate: ")), e9);
                } catch (NoSuchProviderException e10) {
                    StringBuilder m6 = c$$ExternalSyntheticOutline0.m("cannot find required provider:");
                    m6.append(e10.getMessage());
                    throw new JcaX509CertificateConverter.ExCertificateException(jcaX509CertificateConverter, m6.toString(), e10);
                }
            } else {
                throw new CMSException("No content found.");
            }
        } catch (IOException e11) {
            throw new CMSException("IOException reading content.", e11);
        } catch (ClassCastException e12) {
            throw new CMSException("Malformed content.", e12);
        } catch (IllegalArgumentException e13) {
            throw new CMSException("Malformed content.", e13);
        }
    }
}
