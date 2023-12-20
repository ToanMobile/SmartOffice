package com.artifex.sonui.editor;

import com.artifex.source.util.a.util_a.a.a.c$$ExternalSyntheticOutline0;
import android.app.Activity;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.security.KeyChainException;
import androidx.transition.ViewOverlayApi18;
import com.artifex.mupdf.fitz.FitzInputStream;
import com.artifex.mupdf.fitz.PDFWidget;
import com.artifex.mupdf.fitz.PKCS7DistinguishedName;
import com.artifex.solib.FileUtilsExternalSyntheticOutline0;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1Set;
import org.spongycastle.asn1.DERNull;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DEROutputStream;
import org.spongycastle.asn1.DLSet;
import org.spongycastle.asn1.cms.CMSObjectIdentifiers;
import org.spongycastle.asn1.cms.ContentInfo;
import org.spongycastle.asn1.cms.IssuerAndSerialNumber;
import org.spongycastle.asn1.cms.SignedData;
import org.spongycastle.asn1.cms.SignerIdentifier;
import org.spongycastle.asn1.cms.SignerInfo;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.Certificate;
import org.spongycastle.asn1.x509.Extensions;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.jcajce.JcaCertStore;
import org.spongycastle.cms.CMSException;
import org.spongycastle.cms.CMSProcessableByteArray;
import org.spongycastle.cms.CMSSignatureEncryptionAlgorithmFinder;
import org.spongycastle.cms.CMSSignedData;
import org.spongycastle.cms.CMSSignedDataGenerator;
import org.spongycastle.cms.CMSSignedHelper;
import org.spongycastle.cms.CMSUtils;
import org.spongycastle.cms.DefaultCMSSignatureEncryptionAlgorithmFinder;
import org.spongycastle.cms.SignerInfoGenerator;
import org.spongycastle.cms.SignerInformation;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.spongycastle.operator.DigestCalculator;
import org.spongycastle.operator.DigestCalculatorProvider;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.RuntimeOperatorException;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.Selector;
import org.spongycastle.util.io.TeeOutputStream;

public class NUIDefaultSigner extends NUIPKCS7Signer {
    public Activity mActivity;
    public String mAlias;
    public NUICertificate mCert;
    public PKCS7DistinguishedName mDistinguishedName;

    public NUIDefaultSigner(Activity activity) {
        this.mActivity = activity;
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] signData(byte[] bArr, X509Certificate x509Certificate, PrivateKey privateKey) throws Exception {
        ASN1Set aSN1Set;
        AlgorithmIdentifier algorithmIdentifier;
        ASN1Set aSN1Set2;
        OutputStream outputStream;
        ArrayList arrayList = new ArrayList();
        CMSProcessableByteArray cMSProcessableByteArray = new CMSProcessableByteArray(bArr);
        arrayList.add(x509Certificate);
        JcaCertStore jcaCertStore = new JcaCertStore(arrayList);
        CMSSignedDataGenerator cMSSignedDataGenerator = new CMSSignedDataGenerator();
        JcaContentSignerBuilder jcaContentSignerBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        try {
            Signature createSignature = jcaContentSignerBuilder.helper.createSignature(jcaContentSignerBuilder.sigAlgId);
            AlgorithmIdentifier algorithmIdentifier2 = jcaContentSignerBuilder.sigAlgId;
            createSignature.initSign(privateKey);
            JcaContentSignerBuilder.AnonymousClass1 r7 = new ContentSigner(jcaContentSignerBuilder, createSignature, algorithmIdentifier2) {
                public SignatureOutputStream stream;
                public final /* synthetic */ AlgorithmIdentifier val$signatureAlgId;

                {
                    this.val$signatureAlgId = r3;
                    this.stream = new SignatureOutputStream(r1, r2);
                }
            };
            JcaDigestCalculatorProviderBuilder.AnonymousClass1 r8 = new DigestCalculatorProvider() {
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
            };
            DefaultCMSSignatureEncryptionAlgorithmFinder defaultCMSSignatureEncryptionAlgorithmFinder = new DefaultCMSSignatureEncryptionAlgorithmFinder();
            Certificate instance = Certificate.getInstance(x509Certificate.getEncoded());
            Extensions extensions = instance.tbsCert.extensions;
            cMSSignedDataGenerator.signerGens.add(new SignerInfoGenerator(new SignerIdentifier(new IssuerAndSerialNumber(instance)), r7, r8, defaultCMSSignatureEncryptionAlgorithmFinder, true));
            List list = cMSSignedDataGenerator.certs;
            int i = CMSUtils.$r8$clinit;
            ArrayList arrayList2 = new ArrayList();
            try {
                Iterator it = ((ArrayList) jcaCertStore.getMatches((Selector) null)).iterator();
                while (it.hasNext()) {
                    arrayList2.add(((X509CertificateHolder) it.next()).x509Certificate);
                }
                list.addAll(arrayList2);
                if (cMSSignedDataGenerator.signerInfs.isEmpty()) {
                    ViewOverlayApi18 viewOverlayApi18 = new ViewOverlayApi18();
                    ViewOverlayApi18 viewOverlayApi182 = new ViewOverlayApi18();
                    cMSSignedDataGenerator.digests.clear();
                    for (SignerInformation signerInformation : cMSSignedDataGenerator._signers) {
                        CMSSignedHelper cMSSignedHelper = CMSSignedHelper.INSTANCE;
                        AlgorithmIdentifier algorithmIdentifier3 = signerInformation.digestAlgorithm;
                        if (algorithmIdentifier3.parameters == null) {
                            algorithmIdentifier3 = new AlgorithmIdentifier(algorithmIdentifier3.algorithm, DERNull.INSTANCE);
                        }
                        ((Vector) viewOverlayApi18.mViewOverlay).addElement(algorithmIdentifier3);
                        viewOverlayApi182.add(signerInformation.info);
                    }
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = cMSProcessableByteArray.type;
                    if (cMSProcessableByteArray.getContent() != null) {
                        List<SignerInfoGenerator> list2 = cMSSignedDataGenerator.signerGens;
                        int i2 = CMSUtils.$r8$clinit;
                        OutputStream outputStream2 = null;
                        for (SignerInfoGenerator signerInfoGenerator : list2) {
                            DigestCalculator digestCalculator = signerInfoGenerator.digester;
                            if (digestCalculator == null) {
                                outputStream = ((JcaContentSignerBuilder.AnonymousClass1) signerInfoGenerator.signer).stream;
                            } else if (signerInfoGenerator.sAttrGen == null) {
                                outputStream = new TeeOutputStream(digestOutputStream, ((JcaContentSignerBuilder.AnonymousClass1) signerInfoGenerator.signer).stream);
                            } else {
                                outputStream = digestOutputStream;
                            }
                            if (outputStream2 == null) {
                                outputStream2 = CMSUtils.getSafeOutputStream(outputStream);
                            } else if (outputStream != null) {
                                outputStream2 = new TeeOutputStream(outputStream2, outputStream);
                            }
                        }
                        OutputStream safeOutputStream = CMSUtils.getSafeOutputStream(outputStream2);
                        try {
                            safeOutputStream.write(cMSProcessableByteArray.bytes);
                            safeOutputStream.close();
                        } catch (IOException e) {
                            throw new CMSException(FileUtilsExternalSyntheticOutline0.m(e, c$$ExternalSyntheticOutline0.m("data processing exception: ")), e);
                        }
                    }
                    for (SignerInfoGenerator signerInfoGenerator2 : cMSSignedDataGenerator.signerGens) {
                        Objects.requireNonNull(signerInfoGenerator2);
                        try {
                            CMSSignatureEncryptionAlgorithmFinder cMSSignatureEncryptionAlgorithmFinder = signerInfoGenerator2.sigEncAlgFinder;
                            AlgorithmIdentifier algorithmIdentifier4 = ((JcaContentSignerBuilder.AnonymousClass1) signerInfoGenerator2.signer).val$signatureAlgId;
                            Objects.requireNonNull((DefaultCMSSignatureEncryptionAlgorithmFinder) cMSSignatureEncryptionAlgorithmFinder);
                            if (((HashSet) DefaultCMSSignatureEncryptionAlgorithmFinder.RSA_PKCS1d5).contains(algorithmIdentifier4.algorithm)) {
                                algorithmIdentifier4 = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
                            }
                            AlgorithmIdentifier algorithmIdentifier5 = algorithmIdentifier4;
                            if (signerInfoGenerator2.sAttrGen != null) {
                                DigestCalculator digestCalculator2 = signerInfoGenerator2.digester;
                                algorithmIdentifier = algorithmIdentifier;
                                byte[] digest = ((JcaDigestCalculatorProviderBuilder.AnonymousClass1.AnonymousClass1) digestCalculator2).getDigest();
                                signerInfoGenerator2.calculatedDigest = digest;
                                ASN1Set attributeSet = signerInfoGenerator2.getAttributeSet(signerInfoGenerator2.sAttrGen.getAttributes(Collections.unmodifiableMap(signerInfoGenerator2.getBaseParameters(aSN1ObjectIdentifier, algorithmIdentifier, algorithmIdentifier5, digest))));
                                JcaContentSignerBuilder.SignatureOutputStream signatureOutputStream = ((JcaContentSignerBuilder.AnonymousClass1) signerInfoGenerator2.signer).stream;
                                signatureOutputStream.write(attributeSet.getEncoded("DER"));
                                signatureOutputStream.close();
                                aSN1Set = attributeSet;
                            } else {
                                DigestCalculator digestCalculator3 = signerInfoGenerator2.digester;
                                if (digestCalculator3 != null) {
                                    algorithmIdentifier = algorithmIdentifier;
                                    signerInfoGenerator2.calculatedDigest = ((JcaDigestCalculatorProviderBuilder.AnonymousClass1.AnonymousClass1) digestCalculator3).getDigest();
                                } else {
                                    algorithmIdentifier = ((DefaultDigestAlgorithmIdentifierFinder) signerInfoGenerator2.digAlgFinder).find(((JcaContentSignerBuilder.AnonymousClass1) signerInfoGenerator2.signer).val$signatureAlgId);
                                    signerInfoGenerator2.calculatedDigest = null;
                                }
                                aSN1Set = null;
                            }
                            AlgorithmIdentifier algorithmIdentifier6 = algorithmIdentifier;
                            JcaContentSignerBuilder.AnonymousClass1 r5 = (JcaContentSignerBuilder.AnonymousClass1) signerInfoGenerator2.signer;
                            Objects.requireNonNull(r5);
                            byte[] sign = r5.stream.sig.sign();
                            if (signerInfoGenerator2.unsAttrGen != null) {
                                Map baseParameters = signerInfoGenerator2.getBaseParameters(aSN1ObjectIdentifier, algorithmIdentifier6, algorithmIdentifier5, signerInfoGenerator2.calculatedDigest);
                                ((HashMap) baseParameters).put("encryptedDigest", Arrays.clone(sign));
                                aSN1Set2 = signerInfoGenerator2.getAttributeSet(signerInfoGenerator2.unsAttrGen.getAttributes(Collections.unmodifiableMap(baseParameters)));
                            } else {
                                aSN1Set2 = null;
                            }
                            SignerInfo signerInfo = new SignerInfo(signerInfoGenerator2.signerIdentifier, algorithmIdentifier6, aSN1Set, algorithmIdentifier5, new DEROctetString(sign), aSN1Set2);
                            viewOverlayApi18.add(signerInfo.digAlgorithm);
                            ((Vector) viewOverlayApi182.mViewOverlay).addElement(signerInfo);
                            byte[] bArr2 = signerInfoGenerator2.calculatedDigest;
                            byte[] clone = bArr2 != null ? Arrays.clone(bArr2) : null;
                            if (clone != null) {
                                cMSSignedDataGenerator.digests.put(signerInfo.digAlgorithm.algorithm.identifier, clone);
                            }
                        } catch (SignatureException e2) {
                            throw new RuntimeOperatorException("exception obtaining signature: " + e2.getMessage(), e2);
                        } catch (IOException e3) {
                            throw new CMSException("encoding error.", e3);
                        }
                    }
                    CMSSignedData cMSSignedData = new CMSSignedData(cMSProcessableByteArray, new ContentInfo(CMSObjectIdentifiers.signedData, new SignedData(new DLSet(viewOverlayApi18, 1), new ContentInfo(aSN1ObjectIdentifier, (ASN1Encodable) null), cMSSignedDataGenerator.certs.size() != 0 ? CMSUtils.createBerSetFromList(cMSSignedDataGenerator.certs) : null, cMSSignedDataGenerator.crls.size() != 0 ? CMSUtils.createBerSetFromList(cMSSignedDataGenerator.crls) : null, new DLSet(viewOverlayApi182, 1))));
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    new DEROutputStream(byteArrayOutputStream).writeObject(cMSSignedData.contentInfo);
                    return byteArrayOutputStream.toByteArray();
                }
                throw new IllegalStateException("this method can only be used with SignerInfoGenerator");
            } catch (ClassCastException e4) {
                throw new CMSException("error processing certs", e4);
            }
        } catch (GeneralSecurityException e5) {
            StringBuilder m = c$$ExternalSyntheticOutline0.m("cannot create signer: ");
            m.append(e5.getMessage());
            throw new OperatorCreationException(m.toString(), e5);
        }
    }

    public void doSign(final NUIPKCS7SignerListener nUIPKCS7SignerListener) {
        if (this.mAlias == null) {
            KeyChain.choosePrivateKeyAlias(this.mActivity, new KeyChainAliasCallback() {
                public void alias(String str) {
                    X509Certificate[] x509CertificateArr;
                    NUIDefaultSigner.this.mCert = new NUICertificate();
                    if (str != null) {
                        NUIDefaultSigner nUIDefaultSigner = NUIDefaultSigner.this;
                        NUICertificate nUICertificate = nUIDefaultSigner.mCert;
                        Activity activity = nUIDefaultSigner.mActivity;
                        nUICertificate.init();
                        try {
                            x509CertificateArr = KeyChain.getCertificateChain(activity, str);
                        } catch (KeyChainException | InterruptedException unused) {
                            x509CertificateArr = null;
                        }
                        boolean z = false;
                        if (!(x509CertificateArr == null || x509CertificateArr.length == 0)) {
                            z = nUICertificate.fromCertificate(x509CertificateArr[0]);
                        }
                        try {
                            nUICertificate.privKey = KeyChain.getPrivateKey(activity, str);
                        } catch (KeyChainException | InterruptedException unused2) {
                        }
                        if (z) {
                            NUIDefaultSigner nUIDefaultSigner2 = NUIDefaultSigner.this;
                            nUIDefaultSigner2.mAlias = str;
                            nUIDefaultSigner2.mDistinguishedName = nUIDefaultSigner2.mCert.pkcs7DistinguishedName();
                            nUIPKCS7SignerListener.onSignatureReady();
                            return;
                        }
                    }
                    NUIDefaultSigner nUIDefaultSigner3 = NUIDefaultSigner.this;
                    nUIDefaultSigner3.mCert = null;
                    nUIDefaultSigner3.mAlias = null;
                    nUIPKCS7SignerListener.onCancel();
                    Activity activity2 = NUIDefaultSigner.this.mActivity;
                    Utilities.showMessage(activity2, activity2.getString(R.string.sodk_editor_certificate_sign), NUIDefaultSigner.this.mActivity.getString(R.string.sodk_editor_certificate_no_identities));
                }
            }, (String[]) null, (Principal[]) null, (String) null, -1, "");
        } else {
            nUIPKCS7SignerListener.onSignatureReady();
        }
    }

    public int maxDigest() {
        return 7168;
    }

    public PKCS7DistinguishedName name() {
        return this.mDistinguishedName;
    }

    public byte[] sign(FitzInputStream fitzInputStream) {
        byte[] bArr = new byte[PDFWidget.PDF_BTN_FIELD_IS_NO_TOGGLE_TO_OFF];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = fitzInputStream.read(bArr, 0, PDFWidget.PDF_BTN_FIELD_IS_NO_TOGGLE_TO_OFF);
            if (read > 0) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                NUICertificate nUICertificate = this.mCert;
                try {
                    return signData(byteArray, nUICertificate.publicCert, nUICertificate.privKey);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
