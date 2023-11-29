package com.artifex.sonui.editor;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ConfigOptions;
import com.artifex.sonui.editor.SODocSession;

public class NUIActivity extends BaseActivity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Intent mChildIntent = null;
    public SODataLeakHandlers mDataLeakHandlers;
    public ConfigOptions mDocConfigOpts;
    public boolean mIsBeingRecreated = false;
    public Configuration mLastConfiguration;
    public int mLastKeyCode = -1;
    public long mLastKeyTime = 0;
    public NUIView mNUIView;
    public ViewingState mViewingState = null;
    public SODocSession session = null;

    public void checkIAP() {
    }

    public final void checkUIMode(Configuration configuration) {
        if (Build.VERSION.SDK_INT >= 28 && configuration.uiMode != this.mLastConfiguration.uiMode) {
            if (BaseActivity.getCurrentActivity() != null) {
                doPause(new Runnable() {
                    public void run() {
                        NUIActivity.super.finish();
                        Intent intent = NUIActivity.this.getIntent();
                        intent.putExtra("ACTIVITY_RESTARTED", true);
                        NUIActivity.this.startActivity(intent);
                    }
                });
            } else {
                super.finish();
                Intent intent = getIntent();
                intent.putExtra("ACTIVITY_RESTARTED", true);
                startActivity(intent);
            }
        }
        this.mLastConfiguration = getResources().getConfiguration();
    }

    public Intent childIntent() {
        return this.mChildIntent;
    }

    public final void doPause(final Runnable runnable) {
        NUIView nUIView = this.mNUIView;
        if (nUIView != null) {
            nUIView.onPause(new Runnable() {
                public void run() {
                    NUIActivity.this.mNUIView.releaseBitmaps();
                    Runnable runnable = runnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
        } else if (runnable != null) {
            ((AnonymousClass8) runnable).run();
        }
        if (Utilities.isChromebook(this)) {
            PrintHelperPdf.setPrinting(false);
        }
    }

    public void doResumeActions() {
        NUIView nUIView = this.mNUIView;
        if (nUIView != null) {
            nUIView.onResume();
        }
        checkUIMode(getResources().getConfiguration());
    }

    public void finish() {
        if (this.mNUIView == null) {
            super.finish();
            return;
        }
        Utilities.dismissCurrentAlert();
        onBackPressed();
    }

    public ConfigOptions getDocConfigOptions() {
        return this.mDocConfigOpts;
    }

    public void initialise() {
        final Intent intent = getIntent();
        if (this.mIsBeingRecreated || intent.hasExtra("ACTIVITY_RESTARTED")) {
            start(intent);
            return;
        }
        SOFileState autoOpen = SOFileState.getAutoOpen(this);
        if (autoOpen == null || !autoOpen.hasChanges()) {
            start(intent);
            return;
        }
        Utilities.yesNoMessage(this, getString(R.string.sodk_editor_previous_doc_has_been_modified), getString(R.string.sodk_editor_open_prev_or_requested_doc), getString(R.string.sodk_editor_open_req_doc), getString(R.string.sodk_editor_open_prev_doc), new Runnable() {
            public void run() {
                SOFileState.clearAutoOpen(BaseActivity.getCurrentActivity());
                NUIActivity nUIActivity = NUIActivity.this;
                Intent intent = intent;
                int i = NUIActivity.$r8$clinit;
                nUIActivity.start(intent);
            }
        }, new Runnable() {
            public void run() {
                Intent launchIntentForPackage = BaseActivity.getCurrentActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(NUIActivity.this.getBaseContext().getPackageName());
                launchIntentForPackage.addFlags(67108864);
                launchIntentForPackage.addFlags(32768);
                NUIActivity.this.startActivity(launchIntentForPackage);
            }
        });
    }

    public boolean isDocModified() {
        NUIView nUIView = this.mNUIView;
        if (nUIView != null) {
            return nUIView.isDocModified();
        }
        return false;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        NUIView nUIView = this.mNUIView;
        if (nUIView != null) {
            nUIView.onActivityResult(i, i2, intent);
        }
    }

    public void onBackPressed() {
        NUIView nUIView = this.mNUIView;
        if (nUIView != null) {
            nUIView.onBackPressed((Runnable) null);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        checkUIMode(configuration);
        NUIView nUIView = this.mNUIView;
        if (nUIView != null) {
            nUIView.onConfigurationChange(configuration);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mIsBeingRecreated = true;
        }
        this.mLastConfiguration = getResources().getConfiguration();
        initialise();
    }

    public void onDestroy() {
        super.onDestroy();
        NUIView nUIView = this.mNUIView;
        if (nUIView != null) {
            nUIView.onDestroy();
        }
    }

    public void onDocLoaded() {
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.mNUIView == null) {
            return true;
        }
        long eventTime = keyEvent.getEventTime();
        if (this.mLastKeyCode == i && eventTime - this.mLastKeyTime <= 100) {
            return true;
        }
        this.mLastKeyTime = eventTime;
        this.mLastKeyCode = i;
        return this.mNUIView.doKeyDown(i, keyEvent);
    }

    public void onNewIntent(final Intent intent) {
        if (isDocModified()) {
            Utilities.yesNoMessage(this, getString(R.string.sodk_editor_new_intent_title), getString(R.string.sodk_editor_new_intent_body), getString(R.string.sodk_editor_new_intent_yes_button), getString(R.string.sodk_editor_new_intent_no_button), new Runnable() {
                public void run() {
                    NUIView nUIView = NUIActivity.this.mNUIView;
                    if (nUIView != null) {
                        nUIView.endDocSession(true);
                    }
                    NUIActivity.this.setIntent(intent);
                    NUIActivity.this.start(intent);
                }
            }, new Runnable(this) {
                public void run() {
                    SODocSession.SODocSessionLoadListenerCustom sessionLoadListener = Utilities.getSessionLoadListener();
                    if (sessionLoadListener != null) {
                        sessionLoadListener.onSessionReject();
                    }
                    Utilities.setSessionLoadListener((SODocSession.SODocSessionLoadListenerCustom) null);
                }
            });
            return;
        }
        NUIView nUIView = this.mNUIView;
        if (nUIView != null) {
            nUIView.endDocSession(true);
        }
        setIntent(intent);
        start(intent);
    }

    public void onPause() {
        doPause((Runnable) null);
        super.onPause();
    }

    public void onResume() {
        this.mChildIntent = null;
        super.onResume();
        doResumeActions();
    }

    public void setConfigurableButtons() {
        if (this.mDocConfigOpts != null) {
            ConfigOptions appConfigOptions = ArDkLib.getAppConfigOptions();
            ConfigOptions configOptions = this.mDocConfigOpts;
            configOptions.mSettingsBundle.putBoolean("ExtClipboardOutEnabledKey", appConfigOptions.isExtClipboardOutEnabled());
            configOptions.mSettingsBundle.putBoolean("OpenInEnabledKey", appConfigOptions.isOpenInEnabled());
            configOptions.mSettingsBundle.putBoolean("OpenPdfInEnabledKey", appConfigOptions.isOpenPdfInEnabled());
            configOptions.mSettingsBundle.putBoolean("ExtClipboardInEnabledKey", appConfigOptions.isExtClipboardInEnabled());
            configOptions.mSettingsBundle.putBoolean("ImageInsertEnabledKey", appConfigOptions.isImageInsertEnabled());
            configOptions.mSettingsBundle.putBoolean("PhotoInsertEnabledKey", appConfigOptions.isPhotoInsertEnabled());
            configOptions.mSettingsBundle.putBoolean("PrintingEnabledKey", appConfigOptions.isPrintingEnabled());
            configOptions.mSettingsBundle.putBoolean("SecurePrintingEnabledKey", appConfigOptions.isSecurePrintingEnabled());
            configOptions.mSettingsBundle.putBoolean("NonRepudiationCertsOnlyKey", appConfigOptions.mSettingsBundle.getBoolean("NonRepudiationCertsOnlyKey", false));
            configOptions.mSettingsBundle.putBoolean("AppAuthEnabledKey", appConfigOptions.mSettingsBundle.getBoolean("AppAuthEnabledKey", false));
            configOptions.mSettingsBundle.putInt("AppAuthTimeoutKey", appConfigOptions.mSettingsBundle.getInt("AppAuthTimeoutKey", 30));
        }
        NUIView nUIView = this.mNUIView;
        if (nUIView != null) {
            nUIView.setConfigurableButtons();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0157, code lost:
        if (com.artifex.solib.FileUtils.fileExists(r9) == false) goto L_0x0159;
     */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x025b  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x0265  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0153  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x015c  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0162  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x016a  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01c5  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01ee  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01f6  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x01f9  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x020c  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0214  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0219  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0222  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void start(Intent r20) {
        /*
            r19 = this;
            r1 = r19
            r2 = r20
            r20.getExtras()     // Catch:{ IllegalArgumentException -> 0x0008 }
            goto L_0x000b
        L_0x0008:
            r19.finish()
        L_0x000b:
            android.os.Bundle r3 = r20.getExtras()
            java.lang.String r0 = "CONFIG_OPTS"
            boolean r4 = r2.hasExtra(r0)
            r5 = 1
            r6 = 0
            r7 = 0
            if (r4 == 0) goto L_0x0046
            android.os.Bundle r0 = r2.getBundleExtra(r0)     // Catch:{ Exception -> 0x003f }
            java.lang.String r4 = "ClassNameKey"
            java.lang.String r4 = r0.getString(r4)     // Catch:{ Exception -> 0x003f }
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ Exception -> 0x003f }
            java.lang.Class[] r8 = new java.lang.Class[r5]     // Catch:{ Exception -> 0x003f }
            java.lang.Class<android.os.Bundle> r9 = android.os.Bundle.class
            r8[r7] = r9     // Catch:{ Exception -> 0x003f }
            java.lang.reflect.Constructor r4 = r4.getConstructor(r8)     // Catch:{ Exception -> 0x003f }
            java.lang.Object[] r8 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x003f }
            r8[r7] = r0     // Catch:{ Exception -> 0x003f }
            java.lang.Object r0 = r4.newInstance(r8)     // Catch:{ Exception -> 0x003f }
            com.artifex.solib.ConfigOptions r0 = (com.artifex.solib.ConfigOptions) r0     // Catch:{ Exception -> 0x003f }
            r1.mDocConfigOpts = r0     // Catch:{ Exception -> 0x003f }
            goto L_0x0051
        L_0x003f:
            r0 = move-exception
            r0.printStackTrace()
            r1.mDocConfigOpts = r6
            goto L_0x006f
        L_0x0046:
            com.artifex.solib.ConfigOptions r0 = new com.artifex.solib.ConfigOptions
            com.artifex.solib.ConfigOptions r4 = com.artifex.solib.ArDkLib.getAppConfigOptions()
            r0.<init>(r4)
            r1.mDocConfigOpts = r0
        L_0x0051:
            com.artifex.sonui.editor.SODataLeakHandlers r0 = com.artifex.sonui.editor.Utilities.getDataLeakHandlers()     // Catch:{ InstantiationException -> 0x0069, IllegalAccessException -> 0x0062 }
            java.lang.Class r0 = r0.getClass()     // Catch:{ InstantiationException -> 0x0069, IllegalAccessException -> 0x0062 }
            java.lang.Object r0 = r0.newInstance()     // Catch:{ InstantiationException -> 0x0069, IllegalAccessException -> 0x0062 }
            com.artifex.sonui.editor.SODataLeakHandlers r0 = (com.artifex.sonui.editor.SODataLeakHandlers) r0     // Catch:{ InstantiationException -> 0x0069, IllegalAccessException -> 0x0062 }
            r1.mDataLeakHandlers = r0     // Catch:{ InstantiationException -> 0x0069, IllegalAccessException -> 0x0062 }
            goto L_0x006f
        L_0x0062:
            r0 = move-exception
            r0.printStackTrace()
            r1.mDataLeakHandlers = r6
            goto L_0x006f
        L_0x0069:
            r0 = move-exception
            r0.printStackTrace()
            r1.mDataLeakHandlers = r6
        L_0x006f:
            com.artifex.solib.ConfigOptions r0 = r1.mDocConfigOpts
            if (r0 != 0) goto L_0x0074
            return
        L_0x0074:
            java.util.Objects.requireNonNull(r0)
            com.artifex.solib.ConfigOptions r0 = r1.mDocConfigOpts
            boolean r0 = r0.usePersistentFileState()
            r4 = -1
            if (r0 == 0) goto L_0x015e
            com.artifex.solib.ConfigOptions r0 = r1.mDocConfigOpts
            android.os.Bundle r0 = r0.mSettingsBundle
            java.lang.String r8 = "AddExternalFilesToRecentsStateKey"
            boolean r0 = r0.getBoolean(r8, r5)
            if (r0 == 0) goto L_0x015e
            android.net.Uri r0 = r20.getData()
            if (r0 == 0) goto L_0x0159
            java.lang.String r8 = "_data"
            java.lang.String[] r14 = new java.lang.String[]{r8}
            android.content.ContentResolver r8 = r19.getContentResolver()
            java.lang.String r9 = r0.getScheme()
            if (r9 == 0) goto L_0x00ac
            java.lang.String r10 = "content"
            boolean r9 = r9.equalsIgnoreCase(r10)
            if (r9 != 0) goto L_0x00ac
            goto L_0x0159
        L_0x00ac:
            r11 = 0
            r12 = 0
            r13 = 0
            r9 = r0
            r10 = r14
            android.database.Cursor r8 = r8.query(r9, r10, r11, r12, r13)
            if (r8 == 0) goto L_0x00d3
            r8.moveToFirst()
            r9 = r14[r7]     // Catch:{ CursorIndexOutOfBoundsException -> 0x00ca }
            int r9 = r8.getColumnIndex(r9)     // Catch:{ CursorIndexOutOfBoundsException -> 0x00ca }
            if (r9 == r4) goto L_0x00d3
            java.lang.String r9 = r8.getString(r9)     // Catch:{ CursorIndexOutOfBoundsException -> 0x00ca }
            r8.close()     // Catch:{ CursorIndexOutOfBoundsException -> 0x00cb }
            goto L_0x00d4
        L_0x00ca:
            r9 = r6
        L_0x00cb:
            java.lang.String r8 = "FileUtils"
            java.lang.String r10 = "getFilePathFromContentUri - invalid column index"
            android.util.Log.e(r8, r10)
            goto L_0x00d4
        L_0x00d3:
            r9 = r6
        L_0x00d4:
            if (r9 != 0) goto L_0x0151
            boolean r8 = android.provider.DocumentsContract.isDocumentUri(r1, r0)
            if (r8 == 0) goto L_0x0151
            java.lang.String r8 = r0.getAuthority()
            java.lang.String r10 = "com.android.externalstorage.documents"
            boolean r8 = r10.equals(r8)
            if (r8 == 0) goto L_0x0151
            java.lang.String r0 = android.provider.DocumentsContract.getDocumentId(r0)
            java.lang.String r8 = ":"
            java.lang.String[] r10 = r0.split(r8)
            r11 = r10[r7]
            java.lang.String r12 = "primary"
            boolean r12 = r12.equalsIgnoreCase(r11)
            java.lang.String r13 = "/"
            if (r12 == 0) goto L_0x011a
            int r0 = r10.length
            if (r0 <= r5) goto L_0x0151
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.io.File r8 = android.os.Environment.getExternalStorageDirectory()
            r0.append(r8)
            r0.append(r13)
            r8 = r10[r5]
            r0.append(r8)
            java.lang.String r9 = r0.toString()
            goto L_0x0151
        L_0x011a:
            java.lang.String r12 = "home"
            boolean r11 = r12.equalsIgnoreCase(r11)
            if (r11 == 0) goto L_0x0140
            int r0 = r10.length
            if (r0 <= r5) goto L_0x0151
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.io.File r8 = android.os.Environment.getExternalStorageDirectory()
            r0.append(r8)
            java.lang.String r8 = "/Documents/"
            r0.append(r8)
            r8 = r10[r5]
            r0.append(r8)
            java.lang.String r9 = r0.toString()
            goto L_0x0151
        L_0x0140:
            java.lang.String r9 = "/storage/"
            java.lang.StringBuilder r9 = a.a.a.a.a.c$$ExternalSyntheticOutline0.m(r9)
            java.lang.String r0 = r0.replace(r8, r13)
            r9.append(r0)
            java.lang.String r9 = r9.toString()
        L_0x0151:
            if (r9 == 0) goto L_0x015a
            boolean r0 = com.artifex.solib.FileUtils.fileExists(r9)
            if (r0 != 0) goto L_0x015a
        L_0x0159:
            r9 = r6
        L_0x015a:
            if (r9 == 0) goto L_0x015f
            r0 = 1
            goto L_0x0160
        L_0x015e:
            r9 = r6
        L_0x015f:
            r0 = 0
        L_0x0160:
            if (r3 == 0) goto L_0x0168
            java.lang.String r8 = "CREATE_SESSION"
            boolean r0 = r3.getBoolean(r8, r0)
        L_0x0168:
            if (r0 == 0) goto L_0x019e
            if (r9 == 0) goto L_0x016f
            r8 = 1
            r10 = r9
            goto L_0x017d
        L_0x016f:
            java.lang.String r8 = "CREATE_THUMBNAIL"
            boolean r8 = r3.getBoolean(r8, r7)
            java.lang.String r10 = "FILE_URL"
            java.lang.String r11 = ""
            java.lang.String r10 = r3.getString(r10, r11)
        L_0x017d:
            com.artifex.solib.ArDkLib r11 = com.artifex.solib.ArDkUtils.getLibraryForPath(r1, r10)
            com.artifex.sonui.editor.SODocSession r12 = new com.artifex.sonui.editor.SODocSession
            r12.<init>(r1, r11)
            r1.session = r12
            r12.setCanCreateThumbnail(r8)
            com.artifex.sonui.editor.SODocSession r8 = r1.session
            com.artifex.sonui.editor.NUIActivity$5 r11 = new com.artifex.sonui.editor.NUIActivity$5
            r11.<init>()
            r8.addLoadListener(r11)
            com.artifex.sonui.editor.SODocSession r8 = r1.session
            com.artifex.solib.ConfigOptions r11 = r19.getDocConfigOptions()
            r8.open(r10, r11)
        L_0x019e:
            int r8 = com.artifex.sonui.editor.R.layout.sodk_editor_doc_view_activity
            r1.setContentView((int) r8)
            int r8 = com.artifex.sonui.editor.R.id.doc_view
            android.view.View r8 = r1.findViewById(r8)
            com.artifex.sonui.editor.NUIView r8 = (com.artifex.sonui.editor.NUIView) r8
            r1.mNUIView = r8
            com.artifex.solib.ConfigOptions r10 = r1.mDocConfigOpts
            r8.setDocConfigOptions(r10)
            com.artifex.sonui.editor.NUIView r8 = r1.mNUIView
            com.artifex.sonui.editor.SODataLeakHandlers r10 = r1.mDataLeakHandlers
            r8.setDocDataLeakHandler(r10)
            com.artifex.sonui.editor.NUIView r8 = r1.mNUIView
            com.artifex.sonui.editor.NUIActivity$6 r10 = new com.artifex.sonui.editor.NUIActivity$6
            r10.<init>()
            r8.setDocStateListener(r10)
            if (r3 == 0) goto L_0x01ee
            java.lang.String r8 = "START_PAGE"
            int r8 = r3.getInt(r8, r4)
            java.lang.String r10 = "STATE"
            java.lang.String r10 = r3.getString(r10)
            com.artifex.sonui.editor.SOFileDatabase r11 = com.artifex.sonui.editor.SOFileDatabase.getDatabase()
            com.artifex.sonui.editor.SOFileState r10 = com.artifex.sonui.editor.SOFileState.fromString(r10, r11)
            java.lang.String r11 = "FOREIGN_DATA"
            java.lang.String r11 = r3.getString(r11, r6)
            java.lang.String r12 = "IS_TEMPLATE"
            boolean r12 = r3.getBoolean(r12, r5)
            java.lang.String r13 = "CUSTOM_DOC_DATA"
            java.lang.String r3 = r3.getString(r13)
            r17 = r3
            goto L_0x01f4
        L_0x01ee:
            r8 = -1
            r12 = 1
            r10 = r6
            r11 = r10
            r17 = r11
        L_0x01f4:
            if (r9 == 0) goto L_0x01f9
            r3 = 0
            r15 = 0
            goto L_0x01fa
        L_0x01f9:
            r15 = r12
        L_0x01fa:
            boolean r3 = r1.mIsBeingRecreated
            java.lang.String r9 = "ACTIVITY_RESTARTED"
            if (r3 != 0) goto L_0x0206
            boolean r3 = r2.hasExtra(r9)
            if (r3 == 0) goto L_0x0212
        L_0x0206:
            com.artifex.sonui.editor.SOFileState r10 = com.artifex.sonui.editor.SOFileState.getAutoOpen(r19)
            if (r10 == 0) goto L_0x020d
            r0 = 0
        L_0x020d:
            r1.mIsBeingRecreated = r7
            r2.removeExtra(r9)
        L_0x0212:
            if (r17 != 0) goto L_0x0217
            com.artifex.sonui.editor.Utilities.setSessionLoadListener(r6)
        L_0x0217:
            if (r8 == r4) goto L_0x0222
            com.artifex.sonui.editor.ViewingState r3 = new com.artifex.sonui.editor.ViewingState
            int r8 = r8 - r5
            r3.<init>((int) r8)
            r1.mViewingState = r3
            goto L_0x0259
        L_0x0222:
            if (r10 == 0) goto L_0x022c
            com.artifex.sonui.editor.ViewingState r3 = new com.artifex.sonui.editor.ViewingState
            r3.<init>((com.artifex.sonui.editor.SOFileState) r10)
            r1.mViewingState = r3
            goto L_0x0259
        L_0x022c:
            if (r0 == 0) goto L_0x0252
            com.artifex.sonui.editor.SODocSession r3 = r1.session
            if (r3 == 0) goto L_0x0252
            com.artifex.sonui.editor.SOFileDatabase r3 = com.artifex.sonui.editor.SOFileDatabase.getDatabase()
            if (r3 == 0) goto L_0x024a
            com.artifex.sonui.editor.ViewingState r4 = new com.artifex.sonui.editor.ViewingState
            com.artifex.sonui.editor.SODocSession r5 = r1.session
            java.lang.String r5 = r5.getUserPath()
            com.artifex.sonui.editor.SOFileState r3 = r3.stateForPath(r5, r15)
            r4.<init>((com.artifex.sonui.editor.SOFileState) r3)
            r1.mViewingState = r4
            goto L_0x0259
        L_0x024a:
            com.artifex.sonui.editor.ViewingState r3 = new com.artifex.sonui.editor.ViewingState
            r3.<init>((int) r7)
            r1.mViewingState = r3
            goto L_0x0259
        L_0x0252:
            com.artifex.sonui.editor.ViewingState r3 = new com.artifex.sonui.editor.ViewingState
            r3.<init>((int) r7)
            r1.mViewingState = r3
        L_0x0259:
            if (r0 == 0) goto L_0x0265
            com.artifex.sonui.editor.NUIView r0 = r1.mNUIView
            com.artifex.sonui.editor.SODocSession r2 = r1.session
            com.artifex.sonui.editor.ViewingState r3 = r1.mViewingState
            r0.start(r2, r3, r11)
            goto L_0x0280
        L_0x0265:
            if (r10 == 0) goto L_0x026f
            com.artifex.sonui.editor.NUIView r0 = r1.mNUIView
            com.artifex.sonui.editor.ViewingState r2 = r1.mViewingState
            r0.start(r10, r2)
            goto L_0x0280
        L_0x026f:
            android.net.Uri r14 = r20.getData()
            java.lang.String r18 = r20.getType()
            com.artifex.sonui.editor.NUIView r13 = r1.mNUIView
            com.artifex.sonui.editor.ViewingState r0 = r1.mViewingState
            r16 = r0
            r13.start(r14, r15, r16, r17, r18)
        L_0x0280:
            r19.checkIAP()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.NUIActivity.start(android.content.Intent):void");
    }

    public void startActivity(Intent intent) {
        this.mChildIntent = intent;
        super.startActivity(intent, (Bundle) null);
    }

    public void startActivityForResult(Intent intent, int i) {
        this.mChildIntent = intent;
        super.startActivityForResult(intent, i);
    }

    public void startActivityForResult(Intent intent, int i, Bundle bundle) {
        this.mChildIntent = intent;
        super.startActivityForResult(intent, i, bundle);
    }
}
