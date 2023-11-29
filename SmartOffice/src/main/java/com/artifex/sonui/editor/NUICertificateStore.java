package com.artifex.sonui.editor;

public abstract class NUICertificateStore {
    public abstract NUICertificate[] getAllCertificates();

    public abstract NUICertificate[] getAuxCertificates(NUICertificate nUICertificate);

    public abstract NUICertificate[] getSigningCertificates();

    public abstract void initialise();
}
