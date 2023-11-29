package com.artifex.solib;

import android.os.Bundle;

public class ConfigOptions {
    public Bundle mSettingsBundle;

    public ConfigOptions() {
        Bundle bundle = new Bundle();
        this.mSettingsBundle = bundle;
        bundle.putString("ClassNameKey", ConfigOptions.class.getName());
    }

    public boolean isDigitalSignaturesEnabled() {
        return this.mSettingsBundle.getBoolean("DigitalSignaturesEnabledKey", true);
    }

    public boolean isEditingEnabled() {
        return this.mSettingsBundle.getBoolean("EditingEnabledKey", true);
    }

    public boolean isExtClipboardInEnabled() {
        return this.mSettingsBundle.getBoolean("ExtClipboardInEnabledKey", true);
    }

    public boolean isExtClipboardOutEnabled() {
        return this.mSettingsBundle.getBoolean("ExtClipboardOutEnabledKey", true);
    }

    public boolean isFormFillingEnabled() {
        return this.mSettingsBundle.getBoolean("FormFillingEnabledKey", false);
    }

    public boolean isFormSigningFeatureEnabled() {
        return this.mSettingsBundle.getBoolean("FormSigningFeatureEnabledKey", false);
    }

    public boolean isFullscreenEnabled() {
        return this.mSettingsBundle.getBoolean("FullscreenEnabledKey", false);
    }

    public boolean isImageInsertEnabled() {
        return this.mSettingsBundle.getBoolean("ImageInsertEnabledKey", true);
    }

    public boolean isOpenInEnabled() {
        return this.mSettingsBundle.getBoolean("OpenInEnabledKey", true);
    }

    public boolean isOpenPdfInEnabled() {
        return this.mSettingsBundle.getBoolean("OpenPdfInEnabledKey", true);
    }

    public boolean isPDFAnnotationEnabled() {
        return this.mSettingsBundle.getBoolean("PDFAnnotationEnabledKey", true);
    }

    public boolean isPhotoInsertEnabled() {
        return this.mSettingsBundle.getBoolean("PhotoInsertEnabledKey", true);
    }

    public boolean isPrintingEnabled() {
        return this.mSettingsBundle.getBoolean("PrintingEnabledKey", true);
    }

    public boolean isRedactionsEnabled() {
        return this.mSettingsBundle.getBoolean("RedactionsEnabledKey", false);
    }

    public boolean isSaveAsEnabled() {
        return this.mSettingsBundle.getBoolean("SaveAsEnabledKey", true);
    }

    public boolean isSaveAsPdfEnabled() {
        return this.mSettingsBundle.getBoolean("SaveAsPdfEnabledKey", true);
    }

    public boolean isSecurePrintingEnabled() {
        return this.mSettingsBundle.getBoolean("SecurePrintingEnabledKey", false);
    }

    public boolean isSecureRedactionsEnabled() {
        return this.mSettingsBundle.getBoolean("SecureRedactionsEnabledKey", false);
    }

    public boolean isShareEnabled() {
        return this.mSettingsBundle.getBoolean("ShareEnabledKey", true);
    }

    public boolean isTrackChangesFeatureEnabled() {
        return this.mSettingsBundle.getBoolean("TrackChangesFeatureEnabledKey", false);
    }

    public void setEditingEnabled(boolean z) {
        this.mSettingsBundle.putBoolean("EditingEnabledKey", z);
    }

    public boolean showUI() {
        return this.mSettingsBundle.getBoolean("ShowUIKey", true);
    }

    public boolean usePersistentFileState() {
        return this.mSettingsBundle.getBoolean("UsePersistentFileStateKey", true);
    }

    public ConfigOptions(ConfigOptions configOptions) {
        Bundle bundle = new Bundle(configOptions.mSettingsBundle);
        this.mSettingsBundle = bundle;
        bundle.putString("ClassNameKey", ConfigOptions.class.getName());
    }
}
