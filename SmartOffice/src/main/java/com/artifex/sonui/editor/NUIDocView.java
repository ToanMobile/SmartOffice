package com.artifex.sonui.editor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.artifex.solib.ArDkBitmap;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ArDkPage;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.ArDkUtils;
import com.artifex.solib.ConfigOptions;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SOClipboardHandler;
import com.artifex.solib.SODoc;
import com.artifex.solib.SODocSaveListener;
import com.artifex.solib.SOLib;
import com.artifex.solib.SOPreferences;
import com.artifex.solib.SOSearchListener;
import com.artifex.sonui.editor.AuthorDialog;
import com.artifex.sonui.editor.BaseActivity;
import com.artifex.sonui.editor.DocumentView;
import com.artifex.sonui.editor.InkColorDialog;
import com.artifex.sonui.editor.InkLineWidthDialog;
import com.artifex.sonui.editor.NUIView;
import com.artifex.sonui.editor.PdfExportAsPopup;
import com.artifex.sonui.editor.SODocSession;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.vungle.warren.model.CacheBustDBAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;

public class NUIDocView extends FrameLayout implements TabHost.OnTabChangeListener, DocViewHost, View.OnClickListener {
    public static int OVERSIZE_MARGIN = 0;
    public static final int OVERSIZE_PERCENT = 20;
    public static ActivityResultCallback callback = new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri obj) {
            Uri uri = (Uri) obj;
            NUIDocView currentNUIDocView = NUIDocView.currentNUIDocView();
            SelectImageListener selectImageListener = currentNUIDocView.onImageSelectedListener;
            if (selectImageListener != null) {
                selectImageListener.onImageSelected(uri);
            }
            currentNUIDocView.onImageSelectedListener = null;
        }
    };
    public static MyContract contract = new MyContract();
    public static NUIDocView mCurrentNUIDocView;
    public static ActivityResultLauncher<String> mGetContent;
    public ArDkBitmap[] bitmaps = {null, null};
    public int[] cutoutHeights = {0, 0, 0, 0};
    public boolean firstTabShown = false;
    public int keyboardHeight = 0;
    public boolean keyboardShown = false;
    public String lastSearchString = "";
    public long lastTypingTime = 0;
    public PageAdapter mAdapter;
    public ArrayList<String> mAllTabHostTabs = new ArrayList<>();
    public ImageButton mBackButton;
    public boolean mCanGoBack = false;
    public ChangePageListener mChangePageListener = null;
    public boolean mCompleted = false;
    public ToolbarButton mCopyButton2;
    public int mCurrentPageNum = 0;
    public String mCurrentTab = "";
    public String mCustomDocdata;
    public ToolbarButton mCustomSaveButton;
    public SODataLeakHandlers mDataLeakHandlers;
    public View mDecorView = null;
    public ToolbarButton mDeleteInkButton;
    public ArrayList<String> mDeleteOnClose = new ArrayList<>();
    public ConfigOptions mDocCfgOptions = null;
    public DocListPagesView mDocPageListView;
    public NUIView.DocStateListener mDocStateListener = null;
    public String mDocUserPath;
    public DocView mDocView;
    public ArDkLib mDocumentLib;
    public DocumentListener mDocumentListener = null;
    public ToolbarButton mDrawButton;
    public ToolbarButton mDrawLineColorButton;
    public ToolbarButton mDrawLineThicknessButton;
    public Boolean mEndSessionSilent;
    public Runnable mExitFullScreenRunnable = null;
    public ToolbarButton mExportPdfAsButton;
    public SOFileDatabase mFileDatabase;
    public SOFileState mFileState;
    public boolean mFinished = false;
    public ToolbarButton mFirstPageButton;
    public SOTextView mFooter;
    public View mFooterLead;
    public SOTextView mFooterText;
    public boolean mForceOrientationChange = false;
    public boolean mForceReload = false;
    public boolean mForceReloadAtResume = false;
    public String mForeignData = null;
    public boolean mFullscreen = false;
    public Button mFullscreenButton;
    public Toast mFullscreenToast;
    public InputView mInputView = null;
    public ToolbarButton mInsertImageButton;
    public ToolbarButton mInsertPhotoButton;
    public boolean mIsActivityActive = false;
    public boolean mIsSearching = false;
    public boolean mIsSession = false;
    public boolean mIsTemplate = false;
    public boolean mIsWaiting = false;
    public int mLastOrientation = 0;
    public ToolbarButton mLastPageButton;
    public ListPopupWindow mListPopupWindow;
    public Runnable mOnUpdateUIRunnable = null;
    public ToolbarButton mOpenInButton;
    public ToolbarButton mOpenPdfInButton;
    public int mPageCount;
    public PdfExportAsPopup mPdfExportAsPopup = null;
    public int mPrevKeyboard = -1;
    public ToolbarButton mPrintButton;
    public ProgressDialog mProgressDialog;
    public Handler mProgressHandler = null;
    public boolean mProgressIsScheduled = false;
    public boolean mProgressStarted = false;
    public ToolbarButton mProtectButton;
    public Button mRedoButton;
    public ToolbarButton mReflowButton;
    public ToolbarButton mSaveAsButton;
    public ToolbarButton mSaveButton;
    public ToolbarButton mSavePdfButton;
    public Button mSearchButton;
    public int mSearchCounter = 0;
    public Handler mSearchHandler = null;
    public SOSearchListener mSearchListener = null;
    public ToolbarButton mSearchNextButton;
    public ToolbarButton mSearchPreviousButton;
    public ProgressDialog mSearchProgressDialog = null;
    public SOEditText mSearchText;
    public SODocSession mSession;
    public ToolbarButton mShareButton;
    public Runnable mShowHideKeyboardRunnable = null;
    public boolean mShowUI = true;
    public SigningHandler mSigningHandler = null;
    public int mStartPage = -1;
    public Uri mStartUri = null;
    public boolean mStarted = false;
    public SOFileState mState = null;
    public TabData[] mTabs = null;
    public boolean mUIEnabled = false;
    public Button mUndoButton;
    public long mVersionLastTapTime = 0;
    public int mVersionTapCount = 0;
    public ViewingState mViewingState;
    public ProgressDialog mWaitDialog = null;
    public SelectImageListener onImageSelectedListener = null;
    public boolean pausing = false;
    public float scalePrev = BitmapDescriptorFactory.HUE_RED;
    public int scrollXPrev = -1;
    public int scrollYPrev = -1;
    public Rect selectionPrev = null;
    public TabHost tabHost = null;
    public Map<String, View> tabMap = new HashMap();

    public interface ChangePageListener {
        void onPage(int i);
    }

    public static class MyContract extends ActivityResultContracts.GetContent {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, @NonNull String input) {
            super.createIntent(context, input);
            Intrinsics.checkNotNullParameter(context, "context");
            Intent type = new Intent("android.intent.action.GET_CONTENT").addCategory("android.intent.category.OPENABLE").setType("*/*");
            Intrinsics.checkNotNullExpressionValue(type, "Intent(Intent.ACTION_GET…          .setType(input)");
            type.putExtra("android.intent.extra.MIME_TYPES", ((String) input).split(","));
            return type;
        }
    }

    public interface SelectImageListener {
        void onImageSelected(Uri uri);
    }

    public interface SigningHandler {
        void saveAsHandler(String str, SOSaveAsComplete sOSaveAsComplete);
    }

    public class TabData {
        public int contentId;
        public int layoutId;
        public String name;
        public int visibility;

        public TabData(NUIDocView nUIDocView, String str, int i, int i2, int i3) {
            this.name = str;
            this.contentId = i;
            this.layoutId = i2;
            this.visibility = i3;
        }
    }

    public NUIDocView(Context context) {
        super(context);
        initialize(context);
    }

    public static void access$1800(NUIDocView nUIDocView) {
        if (!nUIDocView.firstTabShown) {
            nUIDocView.firstTabShown = true;
            nUIDocView.onNewTabShown(nUIDocView.mCurrentTab);
        }
    }

    public static void access$1900(NUIDocView nUIDocView) {
        if (!nUIDocView.mProgressStarted) {
            nUIDocView.mProgressStarted = true;
            nUIDocView.mProgressDialog = Utilities.showWaitDialog(nUIDocView.getContext(), nUIDocView.getContext().getString(R.string.sodk_editor_loading_please_wait), true);
        }
    }

    public static void access$2600(NUIDocView nUIDocView) {
        ProgressDialog progressDialog = nUIDocView.mSearchProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void access$2800(NUIDocView nUIDocView) {
        Utilities.hideKeyboard(nUIDocView.getContext());
        if (!nUIDocView.mProgressIsScheduled) {
            nUIDocView.mProgressIsScheduled = true;
            final int i = nUIDocView.mSearchCounter;
            if (nUIDocView.mProgressHandler == null) {
                nUIDocView.mProgressHandler = new Handler();
            }
            nUIDocView.mProgressHandler.postDelayed(new Runnable() {
                public void run() {
                    NUIDocView nUIDocView = NUIDocView.this;
                    nUIDocView.mProgressIsScheduled = false;
                    if (nUIDocView.mIsSearching && i == nUIDocView.mSearchCounter && nUIDocView.getDoc() != null) {
                        NUIDocView nUIDocView2 = NUIDocView.this;
                        if (nUIDocView2.mSearchProgressDialog == null) {
                            nUIDocView2.mSearchProgressDialog = new ProgressDialog(NUIDocView.this.getContext(), R.style.sodk_editor_alert_dialog_style);
                        }
                        ProgressDialog progressDialog = NUIDocView.this.mSearchProgressDialog;
                        progressDialog.setMessage(NUIDocView.this.getResources().getString(R.string.sodk_editor_searching) + "...");
                        NUIDocView.this.mSearchProgressDialog.setCancelable(false);
                        NUIDocView nUIDocView3 = NUIDocView.this;
                        nUIDocView3.mSearchProgressDialog.setButton(-2, nUIDocView3.getResources().getString(R.string.sodk_editor_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NUIDocView.this.getDoc().cancelSearch();
                            }
                        });
                        NUIDocView.this.mSearchProgressDialog.show();
                    }
                }
            }, 1000);
        }
        ArDkDoc doc = nUIDocView.getDoc();
        if (nUIDocView.mShowUI) {
            doc.setSearchString(nUIDocView.mSearchText.getText().toString());
        }
        doc.search();
    }

    public static void access$3100(NUIDocView nUIDocView) {
        nUIDocView.saveState();
        nUIDocView.mFileState.closeFile();
    }

    public static NUIDocView currentNUIDocView() {
        return mCurrentNUIDocView;
    }

    public static void registerGetContentLauncher(Context context) {
        try {
            if (mGetContent == null && (context instanceof ComponentActivity)) {
                mGetContent = ((ComponentActivity) context).registerForActivityResult(contract, callback);
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public void setFooterText(String str) {
        if (str != null && !str.isEmpty()) {
            String name = new File(str).getName();
            if (name == null || name.isEmpty()) {
                this.mFooter.setText((CharSequence) str);
            } else {
                this.mFooter.setText((CharSequence) name);
            }
        }
    }

    private void setValid(boolean z) {
        DocListPagesView docListPagesView;
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.setValid(z);
        }
        if (usePagesView() && (docListPagesView = this.mDocPageListView) != null) {
            docListPagesView.setValid(z);
        }
    }

    public Activity activity() {
        return (Activity) getContext();
    }

    public void addDeleteOnClose(String str) {
        this.mDeleteOnClose.add(str);
    }

    public void afterCut() {
    }

    public void afterFirstLayoutComplete() {
        Button button;
        Button button2;
        Button button3;
        Button button4;
        this.mFinished = false;
        if (this.mDocCfgOptions.usePersistentFileState()) {
            SOFileDatabase.init(activity());
        }
        createEditButtons();
        createEditButtons2();
        createReviewButtons();
        createPagesButtons();
        createInsertButtons();
        createDrawButtons();
        this.mBackButton = (ImageButton) createToolbarButton(R.id.back_button);
        this.mUndoButton = (Button) createToolbarButton(R.id.undo_button);
        this.mRedoButton = (Button) createToolbarButton(R.id.redo_button);
        this.mSearchButton = (Button) createToolbarButton(R.id.search_button);
        this.mFullscreenButton = (Button) createToolbarButton(R.id.fullscreen_button);
        this.mSearchNextButton = (ToolbarButton) createToolbarButton(R.id.search_next);
        this.mSearchPreviousButton = (ToolbarButton) createToolbarButton(R.id.search_previous);
        if (!hasSearch() && (button4 = this.mSearchButton) != null) {
            button4.setVisibility(View.GONE);
        }
        if (!hasUndo() && (button3 = this.mUndoButton) != null) {
            button3.setVisibility(View.GONE);
        }
        if (!hasRedo() && (button2 = this.mRedoButton) != null) {
            button2.setVisibility(View.GONE);
        }
        if (!this.mDocCfgOptions.isFullscreenEnabled() && (button = this.mFullscreenButton) != null) {
            button.setVisibility(View.GONE);
        }
        if (!this.mDocCfgOptions.isEditingEnabled()) {
            Button button5 = this.mUndoButton;
            if (button5 != null) {
                button5.setVisibility(View.GONE);
            }
            Button button6 = this.mRedoButton;
            if (button6 != null) {
                button6.setVisibility(View.GONE);
            }
        }
        showSearchSelected(false);
        this.mSearchText = (SOEditText) findViewById(R.id.search_text_input);
        this.mFooterText = (SOTextView) findViewById(R.id.footer_page_text);
        this.mFooterLead = findViewById(R.id.footer_lead);
        this.mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6) {
                    return false;
                }
                NUIDocView.this.onSearchNext((View) null);
                return true;
            }
        });
        this.mSearchText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return i == 67 && NUIDocView.this.mSearchText.getSelectionStart() == 0 && NUIDocView.this.mSearchText.getSelectionEnd() == 0;
            }
        });
        this.mSearchNextButton.setEnabled(false);
        this.mSearchPreviousButton.setEnabled(false);
        this.mSearchText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                NUIDocView.this.setSearchStart();
                boolean z = true;
                NUIDocView.this.mSearchNextButton.setEnabled(charSequence.toString().length() > 0);
                ToolbarButton toolbarButton = NUIDocView.this.mSearchPreviousButton;
                if (charSequence.toString().length() <= 0) {
                    z = false;
                }
                toolbarButton.setEnabled(z);
            }
        });
        ActionMode.Callback r1 = new ActionMode.Callback() {
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return true;
            }

            public void onDestroyActionMode(ActionMode actionMode) {
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                ArrayList arrayList = new ArrayList();
                int size = menu.size();
                int i = 0;
                while (true) {
                    boolean z = true;
                    if (i >= size) {
                        break;
                    }
                    MenuItem item = menu.getItem(i);
                    int itemId = item.getItemId();
                    if (NUIDocView.this.mDocCfgOptions.isShareEnabled() && itemId == 16908341) {
                        z = false;
                    }
                    if (NUIDocView.this.mDocCfgOptions.isExtClipboardOutEnabled() && (itemId == 16908320 || itemId == 16908321)) {
                        z = false;
                    }
                    if (NUIDocView.this.mDocCfgOptions.isExtClipboardInEnabled() && itemId == 16908322) {
                        z = false;
                    }
                    if (item.getItemId() == 16908319) {
                        z = false;
                    }
                    if (z) {
                        arrayList.add(Integer.valueOf(itemId));
                    }
                    i++;
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    menu.removeItem(((Integer) it.next()).intValue());
                }
                return true;
            }
        };
        this.mSearchText.setCustomSelectionActionModeCallback(r1);
        this.mSearchText.setCustomInsertionActionModeCallback(r1);
        ((ImageView) findViewById(R.id.search_text_clear)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                NUIDocView nUIDocView = NUIDocView.this;
                if (!nUIDocView.mFinished) {
                    nUIDocView.mSearchText.setText("");
                }
            }
        });
        this.mSaveButton = (ToolbarButton) createToolbarButton(R.id.save_button);
        this.mSaveAsButton = (ToolbarButton) createToolbarButton(R.id.save_as_button);
        this.mSavePdfButton = (ToolbarButton) createToolbarButton(R.id.save_pdf_button);
        this.mExportPdfAsButton = (ToolbarButton) createToolbarButton(R.id.export_pdf_as_button);
        this.mPrintButton = (ToolbarButton) createToolbarButton(R.id.print_button);
        this.mShareButton = (ToolbarButton) createToolbarButton(R.id.share_button);
        this.mOpenInButton = (ToolbarButton) createToolbarButton(R.id.open_in_button);
        this.mOpenPdfInButton = (ToolbarButton) createToolbarButton(R.id.open_pdf_in_button);
        this.mProtectButton = (ToolbarButton) createToolbarButton(R.id.protect_button);
        this.mCopyButton2 = (ToolbarButton) createToolbarButton(R.id.copy_button2);
        int identifier = getContext().getResources().getIdentifier("custom_save_button", CacheBustDBAdapter.CacheBustColumns.COLUMN_EVENT_ID, getContext().getPackageName());
        if (identifier != 0) {
            this.mCustomSaveButton = (ToolbarButton) createToolbarButton(identifier);
        }
        setupTabs();
        onDeviceSizeChange();
        setConfigurableButtons();
        fixFileToolbar(R.id.file_toolbar);
        this.mAdapter = createAdapter();
        DocView createMainView = createMainView(activity());
        this.mDocView = createMainView;
        createMainView.setHost(this);
        this.mDocView.setAdapter(this.mAdapter);
        this.mDocView.setDocSpecifics(this.mDocCfgOptions, this.mDataLeakHandlers);
        if (usePagesView()) {
            DocListPagesView docListPagesView = new DocListPagesView(activity());
            this.mDocPageListView = docListPagesView;
            docListPagesView.setHost(this);
            this.mDocPageListView.setAdapter(this.mAdapter);
            this.mDocPageListView.setMainView(this.mDocView);
            this.mDocPageListView.setBorderColor(this.mDocView.getBorderColor());
            this.mDocPageListView.setDocSpecifics(this.mDocCfgOptions, this.mDataLeakHandlers);
        }
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.doc_inner_container);
        relativeLayout.addView(this.mDocView, 0);
        this.mDocView.setup(relativeLayout);
        if (usePagesView()) {
            RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.pages_container);
            relativeLayout2.addView(this.mDocPageListView);
            this.mDocPageListView.setup(relativeLayout2);
            this.mDocPageListView.setCanManipulatePages(canCanManipulatePages());
        }
        this.mFooter = (SOTextView) findViewById(R.id.footer_text);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header_top);
        if (linearLayout != null) {
            linearLayout.setBackgroundColor(getTabUnselectedColor());
        }
        View findViewById = findViewById(R.id.header_top_spacer);
        if (findViewById != null) {
            findViewById.setBackgroundColor(getTabUnselectedColor());
        }
        findViewById(R.id.footer).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str;
                String str2;
                String str3;
                if (!NUIDocView.this.mFinished) {
                    long currentTimeMillis = System.currentTimeMillis();
                    NUIDocView nUIDocView = NUIDocView.this;
                    if (currentTimeMillis - nUIDocView.mVersionLastTapTime > 500) {
                        nUIDocView.mVersionTapCount = 1;
                    } else {
                        nUIDocView.mVersionTapCount++;
                    }
                    if (nUIDocView.mVersionTapCount == 5) {
                        SOLib lib = SOLib.getLib((Activity) nUIDocView.getContext());
                        String[] versionInfo = lib != null ? lib.getVersionInfo() : null;
                        String str4 = "";
                        if (versionInfo != null) {
                            str3 = versionInfo[0];
                            str2 = versionInfo[1];
                            str = versionInfo[3];
                        } else {
                            str = str4;
                            str3 = str;
                            str2 = str3;
                        }
                        try {
                            str4 = NUIDocView.this.getContext().getPackageManager().getPackageInfo(NUIDocView.this.getContext().getApplicationInfo().packageName, 0).versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        Utilities.showMessage((Activity) NUIDocView.this.getContext(), NUIDocView.this.getContext().getString(R.string.sodk_editor_version_title), String.format(NUIDocView.this.getContext().getString(R.string.sodk_editor_version_format), new Object[]{str3, str2, str4, str}));
                        NUIDocView.this.mVersionTapCount = 0;
                    }
                    NUIDocView.this.mVersionLastTapTime = currentTimeMillis;
                }
            }
        });
        if (this.mDocCfgOptions.usePersistentFileState()) {
            this.mFileDatabase = SOFileDatabase.getDatabase();
        }
        final Activity activity = activity();
        final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int i;
                NUIDocView nUIDocView = NUIDocView.this;
                if (nUIDocView.mFinished || nUIDocView.mDocView.finished()) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    return;
                }
                NUIDocView nUIDocView2 = NUIDocView.this;
                Point realScreenSize = Utilities.getRealScreenSize(nUIDocView2.activity());
                int i2 = realScreenSize.x > realScreenSize.y ? 2 : 1;
                if (nUIDocView2.mForceOrientationChange || !(i2 == (i = nUIDocView2.mLastOrientation) || i == 0)) {
                    nUIDocView2.onOrientationChange();
                }
                nUIDocView2.mForceOrientationChange = false;
                nUIDocView2.mLastOrientation = i2;
            }
        });
        this.mDocView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                NUIDocView.this.mDocView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                NUIDocView nUIDocView = NUIDocView.this;
                nUIDocView.createBitmaps();
                nUIDocView.setBitmapsInViews();
                NUIDocView.this.disableUI();
                NUIDocView nUIDocView2 = NUIDocView.this;
                if (nUIDocView2.mIsSession) {
                    if (nUIDocView2.mSession.hasLoadError()) {
                        NUIDocView.this.disableUI();
                    }
                    NUIDocView nUIDocView3 = NUIDocView.this;
                    nUIDocView3.mDocUserPath = nUIDocView3.mSession.getUserPath();
                    if (NUIDocView.this.mDocCfgOptions.usePersistentFileState()) {
                        NUIDocView nUIDocView4 = NUIDocView.this;
                        nUIDocView4.mFileState = nUIDocView4.mFileDatabase.stateForPath(nUIDocView4.mDocUserPath, nUIDocView4.mIsTemplate);
                        NUIDocView nUIDocView5 = NUIDocView.this;
                        nUIDocView5.mFileState.setForeignData(nUIDocView5.mForeignData);
                        NUIDocView nUIDocView6 = NUIDocView.this;
                        nUIDocView6.mSession.setFileState(nUIDocView6.mFileState);
                        NUIDocView nUIDocView7 = NUIDocView.this;
                        nUIDocView7.mFileState.openFile(nUIDocView7.mIsTemplate);
                        NUIDocView.this.mFileState.setHasChanges(false);
                        NUIDocView nUIDocView8 = NUIDocView.this;
                        nUIDocView8.setFooterText(nUIDocView8.mFileState.getUserPath());
                        NUIDocView nUIDocView9 = NUIDocView.this;
                        nUIDocView9.mDocView.setDoc(nUIDocView9.mSession.getDoc());
                        if (NUIDocView.this.usePagesView()) {
                            NUIDocView nUIDocView10 = NUIDocView.this;
                            nUIDocView10.mDocPageListView.setDoc(nUIDocView10.mSession.getDoc());
                        }
                        NUIDocView nUIDocView11 = NUIDocView.this;
                        nUIDocView11.mAdapter.setDoc(nUIDocView11.mSession.getDoc());
                        NUIDocView nUIDocView12 = NUIDocView.this;
                        if (nUIDocView12.mDocumentListener != null) {
                            nUIDocView12.mSession.setPasswordHandler(new Runnable() {
                                public void run() {
                                    NUIDocView.this.mDocumentListener.onPasswordRequired();
                                }
                            });
                        }
                        NUIDocView.this.mSession.addLoadListener(new SODocSession.SODocSessionLoadListener() {
                            public boolean thumbnailCreated = false;

                            public void onCancel() {
                                NUIDocView nUIDocView = NUIDocView.this;
                                int i = NUIDocView.OVERSIZE_PERCENT;
                                nUIDocView.endProgress();
                                NUIDocView.this.disableUI();
                            }

                            public void onDocComplete() {
                                NUIDocView nUIDocView = NUIDocView.this;
                                int i = NUIDocView.OVERSIZE_PERCENT;
                                nUIDocView.endProgress();
                                NUIDocView.this.onDocCompleted();
                                NUIDocView.this.setPageNumberText();
                                NUIDocView.this.enableUI();
                            }

                            public void onError(int i, int i2) {
                                if (NUIDocView.this.mSession.isOpen()) {
                                    NUIDocView.this.disableUI();
                                    NUIDocView.this.endProgress();
                                    if (!NUIDocView.this.mSession.isCancelled() || i != 6) {
                                        String openErrorDescription = Utilities.getOpenErrorDescription(NUIDocView.this.getContext(), i);
                                        // AnonymousClass11 r4 = AnonymousClass11.this;
                                        Utilities.showMessage(activity, NUIDocView.this.getContext().getString(R.string.sodk_editor_error), openErrorDescription);
                                    }
                                }
                            }

                            public void onLayoutCompleted() {
                                NUIDocView.access$1900(NUIDocView.this);
                                NUIDocView.this.onLayoutChanged();
                            }

                            public void onPageLoad(int i) {
                                NUIDocView nUIDocView = NUIDocView.this;
                                int i2 = NUIDocView.OVERSIZE_PERCENT;
                                nUIDocView.endProgress();
                                NUIDocView.this.onPageLoaded(i);
                                NUIDocView.this.enableUI();
                                NUIDocView.access$1800(NUIDocView.this);
                                if (i >= 1 && !this.thumbnailCreated && NUIDocView.this.mSession.canCreateThumbnail()) {
                                    this.thumbnailCreated = true;
                                    NUIDocView.this.mSession.createThumbnail();
                                }
                            }

                            public void onSelectionChanged(int i, int i2) {
                                NUIDocView.this.onSelectionMonitor(i, i2);
                            }
                        });
                        if (NUIDocView.this.usePagesView()) {
                            NUIDocView.this.mDocPageListView.setScale(((float) NUIDocView.this.getResources().getInteger(R.integer.sodk_editor_pagelist_width_percentage)) / 100.0f);
                        }
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else if (nUIDocView2.mState == null) {
                    Uri uri = nUIDocView2.mStartUri;
                    if (uri == null) {
                        nUIDocView2.mCanGoBack = true;
                        Utilities.showMessageAndFinish(activity, nUIDocView2.getContext().getString(R.string.sodk_editor_error_opening_doc), NUIDocView.this.getContext().getString(R.string.sodk_editor_invalid_location));
                        return;
                    }
                    String scheme = uri.getScheme();
                    if (scheme == null) {
                        NUIDocView.this.mDocUserPath = uri.getPath();
                        NUIDocView nUIDocView13 = NUIDocView.this;
                        if (nUIDocView13.mDocUserPath == null) {
                            nUIDocView13.mCanGoBack = true;
                            Utilities.showMessageAndFinish(activity, nUIDocView13.getContext().getString(R.string.sodk_editor_invalid_file_name), NUIDocView.this.getContext().getString(R.string.sodk_editor_error_opening_from_other_app));
                            Log.e("NUIDocView", " Uri has no path: " + uri.toString());
                            return;
                        }
                    } else {
                        String exportContentUri = FileUtils.exportContentUri(NUIDocView.this.getContext(), uri);
                        if (exportContentUri.equals("---fileOpen")) {
                            NUIDocView nUIDocView14 = NUIDocView.this;
                            nUIDocView14.mCanGoBack = true;
                            Utilities.showMessageAndFinish(activity, nUIDocView14.getContext().getString(R.string.sodk_editor_content_error), NUIDocView.this.getContext().getString(R.string.sodk_editor_error_opening_from_other_app));
                            return;
                        } else if (exportContentUri.startsWith("---")) {
                            NUIDocView nUIDocView15 = NUIDocView.this;
                            nUIDocView15.mCanGoBack = true;
                            String string = nUIDocView15.getResources().getString(R.string.sodk_editor_cant_create_temp_file);
                            String string2 = NUIDocView.this.getContext().getString(R.string.sodk_editor_content_error);
                            Utilities.showMessageAndFinish(activity, string2, NUIDocView.this.getContext().getString(R.string.sodk_editor_error_opening_from_other_app) + ": \n\n" + string);
                            return;
                        } else {
                            NUIDocView nUIDocView16 = NUIDocView.this;
                            nUIDocView16.mDocUserPath = exportContentUri;
                            if (nUIDocView16.mIsTemplate) {
                                nUIDocView16.addDeleteOnClose(exportContentUri);
                            }
                        }
                    }
                    NUIDocView nUIDocView17 = NUIDocView.this;
                    nUIDocView17.setFooterText(nUIDocView17.mDocUserPath);
                    if (NUIDocView.this.mDocCfgOptions.usePersistentFileState()) {
                        NUIDocView nUIDocView18 = NUIDocView.this;
                        nUIDocView18.mFileState = nUIDocView18.mFileDatabase.stateForPath(nUIDocView18.mDocUserPath, nUIDocView18.mIsTemplate);
                    } else {
                        NUIDocView nUIDocView19 = NUIDocView.this;
                        nUIDocView19.mFileState = new SOFileStateDummy(nUIDocView19.mDocUserPath);
                    }
                    NUIDocView nUIDocView20 = NUIDocView.this;
                    nUIDocView20.mFileState.openFile(nUIDocView20.mIsTemplate);
                    NUIDocView.this.mFileState.setHasChanges(false);
                    NUIDocView nUIDocView21 = NUIDocView.this;
                    nUIDocView21.mSession = new SODocSession(activity, nUIDocView21.mDocumentLib);
                    NUIDocView nUIDocView22 = NUIDocView.this;
                    nUIDocView22.mSession.setFileState(nUIDocView22.mFileState);
                    NUIDocView nUIDocView23 = NUIDocView.this;
                    if (nUIDocView23.mDocumentListener != null) {
                        nUIDocView23.mSession.setPasswordHandler(new Runnable() {
                            public void run() {
                                NUIDocView.this.mDocumentListener.onPasswordRequired();
                            }
                        });
                    }
                    NUIDocView.this.mSession.addLoadListener(new SODocSession.SODocSessionLoadListener() {
                        public void onCancel() {
                            NUIDocView.this.disableUI();
                            NUIDocView.this.endProgress();
                        }

                        public void onDocComplete() {
                            NUIDocView nUIDocView = NUIDocView.this;
                            if (!nUIDocView.mFinished) {
                                nUIDocView.endProgress();
                                NUIDocView.this.onDocCompleted();
                                NUIDocView.this.enableUI();
                            }
                        }

                        public void onError(int i, int i2) {
                            NUIDocView nUIDocView = NUIDocView.this;
                            if (!nUIDocView.mFinished) {
                                nUIDocView.disableUI();
                                NUIDocView.this.endProgress();
                                if (!NUIDocView.this.mSession.isCancelled() || i != 6) {
                                    String openErrorDescription = Utilities.getOpenErrorDescription(NUIDocView.this.getContext(), i);
                                    Utilities.showMessage(activity, NUIDocView.this.getContext().getString(R.string.sodk_editor_error), openErrorDescription);
                                }
                            }
                        }

                        public void onLayoutCompleted() {
                            NUIDocView.access$1900(NUIDocView.this);
                            NUIDocView.this.onLayoutChanged();
                        }

                        public void onPageLoad(int i) {
                            NUIDocView nUIDocView = NUIDocView.this;
                            if (!nUIDocView.mFinished) {
                                nUIDocView.endProgress();
                                NUIDocView.this.onPageLoaded(i);
                                NUIDocView.this.enableUI();
                                NUIDocView.access$1800(NUIDocView.this);
                            }
                        }

                        public void onSelectionChanged(int i, int i2) {
                            NUIDocView.this.onSelectionMonitor(i, i2);
                        }
                    });
                    NUIDocView nUIDocView24 = NUIDocView.this;
                    nUIDocView24.mSession.open(nUIDocView24.mFileState.getInternalPath(), NUIDocView.this.mDocCfgOptions);
                    NUIDocView nUIDocView25 = NUIDocView.this;
                    nUIDocView25.mDocView.setDoc(nUIDocView25.mSession.getDoc());
                    if (NUIDocView.this.usePagesView()) {
                        NUIDocView nUIDocView26 = NUIDocView.this;
                        nUIDocView26.mDocPageListView.setDoc(nUIDocView26.mSession.getDoc());
                    }
                    NUIDocView nUIDocView27 = NUIDocView.this;
                    nUIDocView27.mAdapter.setDoc(nUIDocView27.mSession.getDoc());
                    if (NUIDocView.this.usePagesView()) {
                        NUIDocView.this.mDocPageListView.setScale(0.2f);
                    }
                } else if (nUIDocView2.mDocCfgOptions.usePersistentFileState()) {
                    NUIDocView nUIDocView28 = NUIDocView.this;
                    nUIDocView28.mDocUserPath = nUIDocView28.mState.getOpenedPath();
                    NUIDocView nUIDocView29 = NUIDocView.this;
                    nUIDocView29.setFooterText(nUIDocView29.mState.getUserPath());
                    NUIDocView nUIDocView30 = NUIDocView.this;
                    SOFileState sOFileState = nUIDocView30.mState;
                    nUIDocView30.mFileState = sOFileState;
                    sOFileState.openFile(nUIDocView30.mIsTemplate);
                    NUIDocView nUIDocView31 = NUIDocView.this;
                    nUIDocView31.mSession = new SODocSession(activity, nUIDocView31.mDocumentLib);
                    NUIDocView nUIDocView32 = NUIDocView.this;
                    nUIDocView32.mSession.setFileState(nUIDocView32.mFileState);
                    NUIDocView nUIDocView33 = NUIDocView.this;
                    if (nUIDocView33.mDocumentListener != null) {
                        nUIDocView33.mSession.setPasswordHandler(new Runnable() {
                            public void run() {
                                NUIDocView.this.mDocumentListener.onPasswordRequired();
                            }
                        });
                    }
                    NUIDocView.this.mSession.addLoadListener(new SODocSession.SODocSessionLoadListener() {
                        public void onCancel() {
                            NUIDocView nUIDocView = NUIDocView.this;
                            int i = NUIDocView.OVERSIZE_PERCENT;
                            nUIDocView.endProgress();
                            NUIDocView.this.disableUI();
                        }

                        public void onDocComplete() {
                            NUIDocView nUIDocView = NUIDocView.this;
                            if (!nUIDocView.mFinished) {
                                nUIDocView.endProgress();
                                NUIDocView.this.onDocCompleted();
                                NUIDocView.this.enableUI();
                            }
                        }

                        public void onError(int i, int i2) {
                            NUIDocView nUIDocView = NUIDocView.this;
                            if (!nUIDocView.mFinished) {
                                nUIDocView.disableUI();
                                NUIDocView.this.endProgress();
                                if (!NUIDocView.this.mSession.isCancelled() || i != 6) {
                                    String openErrorDescription = Utilities.getOpenErrorDescription(NUIDocView.this.getContext(), i);
                                    Utilities.showMessage(activity, NUIDocView.this.getContext().getString(R.string.sodk_editor_error), openErrorDescription);
                                }
                            }
                        }

                        public void onLayoutCompleted() {
                            NUIDocView.access$1900(NUIDocView.this);
                            NUIDocView.this.onLayoutChanged();
                        }

                        public void onPageLoad(int i) {
                            NUIDocView nUIDocView = NUIDocView.this;
                            if (!nUIDocView.mFinished) {
                                nUIDocView.endProgress();
                                NUIDocView.this.onPageLoaded(i);
                                NUIDocView.this.enableUI();
                                NUIDocView.access$1800(NUIDocView.this);
                            }
                        }

                        public void onSelectionChanged(int i, int i2) {
                            NUIDocView.this.onSelectionMonitor(i, i2);
                        }
                    });
                    NUIDocView nUIDocView34 = NUIDocView.this;
                    nUIDocView34.mSession.open(nUIDocView34.mFileState.getInternalPath(), NUIDocView.this.mDocCfgOptions);
                    NUIDocView nUIDocView35 = NUIDocView.this;
                    nUIDocView35.mDocView.setDoc(nUIDocView35.mSession.getDoc());
                    if (NUIDocView.this.usePagesView()) {
                        NUIDocView nUIDocView36 = NUIDocView.this;
                        nUIDocView36.mDocPageListView.setDoc(nUIDocView36.mSession.getDoc());
                    }
                    NUIDocView nUIDocView37 = NUIDocView.this;
                    nUIDocView37.mAdapter.setDoc(nUIDocView37.mSession.getDoc());
                    if (NUIDocView.this.usePagesView()) {
                        NUIDocView.this.mDocPageListView.setScale(0.2f);
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
                NUIDocView.this.createInputView();
                NUIDocView.this.mCanGoBack = true;
            }
        });
        if (Utilities.isPhoneDevice(activity())) {
            scaleHeader();
        }
        getDocView().setViewingState(this.mViewingState);
    }

    public void afterPaste() {
    }

    public void afterShowUI(boolean z) {
        if (this.mDocCfgOptions.isFullscreenEnabled() && z) {
            Toast toast = this.mFullscreenToast;
            if (toast != null) {
                toast.cancel();
            }
            findViewById(R.id.footer).setVisibility(View.VISIBLE);
            if (isPagesTab()) {
                showPages();
            }
            this.mFullscreen = false;
            if (getDocView() != null) {
                getDocView().onFullscreen(false);
            }
            layoutNow();
        }
    }

    public void applyDocumentTypeConfig(String str) {
    }

    public final void askForCameraPermission(final Runnable runnable1) {
        if (!Utilities.isPermissionRequested(getContext(), "android.permission.CAMERA")) {
            runnable1.run();
        } else if (ContextCompat.checkSelfPermission(getContext(), "android.permission.CAMERA") == 0) {
            runnable1.run();
        } else {
            final BaseActivity currentActivity = BaseActivity.getCurrentActivity();
            BaseActivity.setPermissionResultHandler(new BaseActivity.PermissionResultHandler() {
                public boolean handle(int i, String[] strArr, int[] iArr) {
                    BaseActivity.setPermissionResultHandler((BaseActivity.PermissionResultHandler) null);
                    if (i == 1) {
                        if (iArr.length > 0 && iArr[0] == 0) {
                            new Handler().post(runnable1);
                        } else if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, "android.permission.CAMERA")) {
                            BaseActivity baseActivity = currentActivity;
                            Utilities.yesNoMessage(baseActivity, baseActivity.getString(R.string.sodk_editor_permission_denied), currentActivity.getString(R.string.sodk_editor_permission_camera_why), currentActivity.getString(R.string.sodk_editor_yes), currentActivity.getString(R.string.sodk_editor_no), new Runnable() {
                                public void run() {
                                    NUIDocView nUIDocView = NUIDocView.this;
                                    Runnable runnable = runnable1;
                                    int i = NUIDocView.OVERSIZE_PERCENT;
                                    nUIDocView.askForCameraPermission(runnable);
                                }
                            }, new Runnable() {
                                public void run() {
                                    BaseActivity baseActivity = currentActivity;
                                    Utilities.showMessage(baseActivity, baseActivity.getString(R.string.sodk_editor_permission_denied), currentActivity.getString(R.string.sodk_editor_permission_camera));
                                }
                            });
                        } else {
                            BaseActivity baseActivity2 = currentActivity;
                            Utilities.showMessage(baseActivity2, baseActivity2.getString(R.string.sodk_editor_permission_denied), currentActivity.getString(R.string.sodk_editor_permission_camera));
                        }
                    }
                    return true;
                }
            });
            ActivityCompat.requestPermissions(currentActivity, new String[]{"android.permission.CAMERA"}, 1);
        }
    }

    public void author() {
        onAuthorButton((View) null);
    }

    public Boolean canApplyRedactions() {
        return Boolean.FALSE;
    }

    public boolean canCanManipulatePages() {
        return false;
    }

    public boolean canDeleteSelection() {
        boolean z;
        ArDkSelectionLimits selectionLimits = getDocView().getSelectionLimits();
        if (selectionLimits != null) {
            boolean isActive = selectionLimits.getIsActive();
            z = isActive && !selectionLimits.getIsCaret();
            if (isActive) {
                boolean isCaret = selectionLimits.getIsCaret();
            }
        } else {
            z = false;
        }
        if (!z || !this.mSession.getDoc().getSelectionCanBeDeleted()) {
            return false;
        }
        return true;
    }

    public Boolean canMarkRedaction() {
        return Boolean.FALSE;
    }

    public boolean canRedo() {
        if (!this.mDocCfgOptions.isEditingEnabled()) {
            return false;
        }
        return this.mSession.getDoc().canRedo();
    }

    public Boolean canRemoveRedaction() {
        return Boolean.FALSE;
    }

    public boolean canSelect() {
        return true;
    }

    public boolean canUndo() {
        if (!this.mDocCfgOptions.isEditingEnabled()) {
            return false;
        }
        return this.mSession.getDoc().canUndo();
    }

    public void changeTab(String str) {
        if (!this.mCurrentTab.equalsIgnoreCase(str)) {
            onTabChanging(this.mCurrentTab, str);
            this.mCurrentTab = str;
            setSingleTabTitle(str);
            onSelectionChanged();
            if (!this.mCurrentTab.equals(getContext().getString(R.string.sodk_editor_tab_find)) && !this.mCurrentTab.equals(getContext().getString(R.string.sodk_editor_tab_hidden))) {
                findViewById(R.id.searchTab).setVisibility(View.GONE);
                showSearchSelected(false);
            }
            handlePagesTab(str);
            setTabColors(str);
            this.mDocView.layoutNow();
        }
    }

    public void clearSelection() {
        getDoc().clearSelection();
    }

    public void clickSheetButton(int i, boolean z) {
    }

    public PageAdapter createAdapter() {
        return new PageAdapter(getContext(), this, 1);
    }

    public final void createBitmaps() {
        Point realScreenSize = Utilities.getRealScreenSize(activity());
        int max = Math.max(realScreenSize.x, realScreenSize.y);
        int i = (max * 120) / 100;
        OVERSIZE_MARGIN = (i - max) / 2;
        int i2 = 0;
        while (true) {
            ArDkBitmap[] arDkBitmapArr = this.bitmaps;
            if (i2 < arDkBitmapArr.length) {
                arDkBitmapArr[i2] = this.mDocumentLib.createBitmap(i, i);
                i2++;
            } else {
                return;
            }
        }
    }

    public void createDrawButtons() {
    }

    public void createEditButtons() {
    }

    public void createEditButtons2() {
    }

    public void createInputView() {
        InputView inputView = new InputView(getContext(), this.mSession.getDoc(), this);
        this.mInputView = inputView;
        ((RelativeLayout) findViewById(R.id.doc_inner_container)).addView(inputView);
    }

    public void createInsertButtons() {
        this.mInsertImageButton = (ToolbarButton) createToolbarButton(R.id.insert_image_button);
        this.mInsertPhotoButton = (ToolbarButton) createToolbarButton(R.id.insert_photo_button);
    }

    public DocView createMainView(Activity activity) {
        return new DocView(activity);
    }

    public void createPagesButtons() {
        this.mFirstPageButton = (ToolbarButton) createToolbarButton(R.id.first_page_button);
        this.mLastPageButton = (ToolbarButton) createToolbarButton(R.id.last_page_button);
        ToolbarButton toolbarButton = (ToolbarButton) createToolbarButton(R.id.reflow_button);
        this.mReflowButton = toolbarButton;
        if (toolbarButton != null) {
            toolbarButton.setEnabled(false);
        }
        ToolbarButton.setAllSameSize(new ToolbarButton[]{this.mFirstPageButton, this.mLastPageButton, this.mReflowButton});
    }

    public void createReviewButtons() {
    }

    public View createToolbarButton(int i) {
        View findViewById = findViewById(i);
        if (findViewById != null) {
            findViewById.setOnClickListener(this);
        }
        return findViewById;
    }

    public void defocusInputView() {
        InputView inputView = this.mInputView;
        if (inputView != null) {
            inputView.clearFocus();
        }
    }

    public void deleteSelectedText() {
        getDoc().selectionDelete();
        getDocView().onSelectionDelete();
    }

    public void deleteSelection() {
        getDoc().selectionDelete();
        getDocView().onSelectionDelete();
    }

    public void disableUI() {
        setViewAndChildrenEnabled(this, false);
        this.mUIEnabled = false;
    }

    public void doArrowKey(KeyEvent keyEvent) {
        boolean isShiftPressed = keyEvent.isShiftPressed();
        boolean isAltPressed = keyEvent.isAltPressed();
        switch (keyEvent.getKeyCode()) {
            case 19:
                if (!isShiftPressed && !isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(2);
                }
                if (isShiftPressed && !isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(12);
                }
                if (!isShiftPressed && isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(6);
                }
                if (isShiftPressed && isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(14);
                }
                onTyping();
                updateInputView();
                return;
            case 20:
                if (!isShiftPressed && !isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(3);
                }
                if (isShiftPressed && !isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(13);
                }
                if (!isShiftPressed && isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(7);
                }
                if (isShiftPressed && isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(15);
                }
                onTyping();
                updateInputView();
                return;
            case 21:
                if (!isShiftPressed && !isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(0);
                }
                if (isShiftPressed && !isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(8);
                }
                if (!isShiftPressed && isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(4);
                }
                if (isShiftPressed && isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(10);
                }
                onTyping();
                updateInputView();
                return;
            case 22:
                if (!isShiftPressed && !isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(1);
                }
                if (isShiftPressed && !isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(9);
                }
                if (!isShiftPressed && isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(5);
                }
                if (isShiftPressed && isAltPressed) {
                    this.mSession.getDoc().processKeyCommand(11);
                }
                onTyping();
                updateInputView();
                return;
            default:
                return;
        }
    }

    public void doBold() {
    }

    public void doCopy() {
    }

    public void doCut() {
    }

    public void doInsertImage(String str) {
    }

    public void doItalic() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0068, code lost:
        r1 = getDocView().getSelectionLimits();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0070, code lost:
        if (r1 == null) goto L_0x0086;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0076, code lost:
        if (r1.getIsActive() != false) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x007d, code lost:
        if (inputViewHasFocus() == false) goto L_0x0190;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x007f, code lost:
        onTyping();
        doArrowKey(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0085, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0086, code lost:
        if (r8 != false) goto L_0x0093;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0088, code lost:
        if (r0 == false) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x008b, code lost:
        getDocView().lineDown();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0093, code lost:
        getDocView().pageDown();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x009a, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean doKeyDown(int r8, KeyEvent r9) {
        /*
            r7 = this;
            boolean r8 = r9.isAltPressed()
            boolean r0 = r9.isCtrlPressed()
            boolean r1 = r9.isShiftPressed()
            android.content.Context r2 = r7.getContext()
            boolean r2 = r2 instanceof com.artifex.sonui.editor.BaseActivity
            r3 = 0
            if (r2 == 0) goto L_0x001c
            android.content.Context r2 = r7.getContext()
            com.artifex.sonui.editor.BaseActivity r2 = (com.artifex.sonui.editor.BaseActivity) r2
            goto L_0x001d
        L_0x001c:
            r2 = r3
        L_0x001d:
            int r4 = r9.getKeyCode()
            r5 = 4
            r6 = 1
            if (r4 == r5) goto L_0x01be
            r2 = 37
            if (r4 == r2) goto L_0x017f
            r2 = 47
            if (r4 == r2) goto L_0x0177
            r2 = 52
            if (r4 == r2) goto L_0x0166
            r2 = 54
            if (r4 == r2) goto L_0x0155
            r2 = 62
            if (r4 == r2) goto L_0x0133
            r1 = 67
            r2 = 0
            if (r4 == r1) goto L_0x0112
            r1 = 112(0x70, float:1.57E-43)
            if (r4 == r1) goto L_0x00f1
            r1 = 30
            if (r4 == r1) goto L_0x00e0
            r1 = 31
            if (r4 == r1) goto L_0x00d2
            r1 = 49
            if (r4 == r1) goto L_0x00c1
            r1 = 50
            if (r4 == r1) goto L_0x00b0
            switch(r4) {
                case 19: goto L_0x0057;
                case 20: goto L_0x0068;
                case 21: goto L_0x0079;
                case 22: goto L_0x0079;
                default: goto L_0x0055;
            }
        L_0x0055:
            goto L_0x0190
        L_0x0057:
            com.artifex.sonui.editor.DocView r1 = r7.getDocView()
            com.artifex.solib.ArDkSelectionLimits r1 = r1.getSelectionLimits()
            if (r1 == 0) goto L_0x009b
            boolean r1 = r1.getIsActive()
            if (r1 != 0) goto L_0x0068
            goto L_0x009b
        L_0x0068:
            com.artifex.sonui.editor.DocView r1 = r7.getDocView()
            com.artifex.solib.ArDkSelectionLimits r1 = r1.getSelectionLimits()
            if (r1 == 0) goto L_0x0086
            boolean r1 = r1.getIsActive()
            if (r1 != 0) goto L_0x0079
            goto L_0x0086
        L_0x0079:
            boolean r8 = r7.inputViewHasFocus()
            if (r8 == 0) goto L_0x0190
            r7.onTyping()
            r7.doArrowKey(r9)
            return r6
        L_0x0086:
            if (r8 != 0) goto L_0x0093
            if (r0 == 0) goto L_0x008b
            goto L_0x0093
        L_0x008b:
            com.artifex.sonui.editor.DocView r8 = r7.getDocView()
            r8.lineDown()
            goto L_0x009a
        L_0x0093:
            com.artifex.sonui.editor.DocView r8 = r7.getDocView()
            r8.pageDown()
        L_0x009a:
            return r6
        L_0x009b:
            if (r8 != 0) goto L_0x00a8
            if (r0 == 0) goto L_0x00a0
            goto L_0x00a8
        L_0x00a0:
            com.artifex.sonui.editor.DocView r8 = r7.getDocView()
            r8.lineUp()
            goto L_0x00af
        L_0x00a8:
            com.artifex.sonui.editor.DocView r8 = r7.getDocView()
            r8.pageUp()
        L_0x00af:
            return r6
        L_0x00b0:
            boolean r1 = r7.inputViewHasFocus()
            if (r1 == 0) goto L_0x0190
            if (r0 != 0) goto L_0x00ba
            if (r8 == 0) goto L_0x0190
        L_0x00ba:
            r7.onTyping()
            r7.doPaste()
            return r6
        L_0x00c1:
            boolean r1 = r7.inputViewHasFocus()
            if (r1 == 0) goto L_0x0190
            if (r0 != 0) goto L_0x00cb
            if (r8 == 0) goto L_0x0190
        L_0x00cb:
            r7.onTyping()
            r7.doUnderline()
            return r6
        L_0x00d2:
            boolean r1 = r7.inputViewHasFocus()
            if (r1 == 0) goto L_0x0190
            if (r0 != 0) goto L_0x00dc
            if (r8 == 0) goto L_0x0190
        L_0x00dc:
            r7.doCopy()
            return r6
        L_0x00e0:
            boolean r1 = r7.inputViewHasFocus()
            if (r1 == 0) goto L_0x0190
            if (r0 != 0) goto L_0x00ea
            if (r8 == 0) goto L_0x0190
        L_0x00ea:
            r7.onTyping()
            r7.doBold()
            return r6
        L_0x00f1:
            boolean r8 = r7.inputViewHasFocus()
            if (r8 == 0) goto L_0x0111
            r7.onTyping()
            com.artifex.solib.ArDkDoc r8 = r7.getDoc()
            com.artifex.solib.SODoc r8 = (com.artifex.solib.SODoc) r8
            boolean r9 = r8.getSelectionCanBeDeleted()
            if (r9 == 0) goto L_0x010a
            r8.selectionDelete()
            goto L_0x010d
        L_0x010a:
            r8.forwardDeleteChar()
        L_0x010d:
            r7.updateInputView()
            return r6
        L_0x0111:
            return r2
        L_0x0112:
            boolean r8 = r7.inputViewHasFocus()
            if (r8 == 0) goto L_0x0132
            r7.onTyping()
            com.artifex.solib.ArDkDoc r8 = r7.getDoc()
            com.artifex.solib.SODoc r8 = (com.artifex.solib.SODoc) r8
            boolean r9 = r8.getSelectionCanBeDeleted()
            if (r9 == 0) goto L_0x012b
            r8.selectionDelete()
            goto L_0x012e
        L_0x012b:
            r8.deleteChar()
        L_0x012e:
            r7.updateInputView()
            return r6
        L_0x0132:
            return r2
        L_0x0133:
            com.artifex.sonui.editor.DocView r8 = r7.getDocView()
            com.artifex.solib.ArDkSelectionLimits r8 = r8.getSelectionLimits()
            if (r8 == 0) goto L_0x0143
            boolean r8 = r8.getIsActive()
            if (r8 != 0) goto L_0x0190
        L_0x0143:
            if (r1 == 0) goto L_0x014d
            com.artifex.sonui.editor.DocView r8 = r7.getDocView()
            r8.pageUp()
            goto L_0x0154
        L_0x014d:
            com.artifex.sonui.editor.DocView r8 = r7.getDocView()
            r8.pageDown()
        L_0x0154:
            return r6
        L_0x0155:
            if (r0 != 0) goto L_0x0159
            if (r8 == 0) goto L_0x0190
        L_0x0159:
            r7.onTyping()
            if (r1 == 0) goto L_0x0162
            r7.doRedo()
            goto L_0x0165
        L_0x0162:
            r7.doUndo()
        L_0x0165:
            return r6
        L_0x0166:
            boolean r1 = r7.inputViewHasFocus()
            if (r1 == 0) goto L_0x0190
            if (r0 != 0) goto L_0x0170
            if (r8 == 0) goto L_0x0190
        L_0x0170:
            r7.onTyping()
            r7.doCut()
            return r6
        L_0x0177:
            if (r0 != 0) goto L_0x017b
            if (r8 == 0) goto L_0x0190
        L_0x017b:
            r7.doSave()
            return r6
        L_0x017f:
            boolean r1 = r7.inputViewHasFocus()
            if (r1 == 0) goto L_0x0190
            if (r0 != 0) goto L_0x0189
            if (r8 == 0) goto L_0x0190
        L_0x0189:
            r7.onTyping()
            r7.doItalic()
            return r6
        L_0x0190:
            boolean r8 = r7.inputViewHasFocus()
            if (r8 == 0) goto L_0x01bd
            int r8 = r9.getUnicodeChar()
            char r8 = (char) r8
            if (r8 == 0) goto L_0x01bd
            r7.onTyping()
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r0 = ""
            r9.append(r0)
            r9.append(r8)
            java.lang.String r8 = r9.toString()
            com.artifex.solib.ArDkDoc r9 = r7.getDoc()
            com.artifex.solib.SODoc r9 = (com.artifex.solib.SODoc) r9
            r9.setSelectionText(r8)
            r7.updateInputView()
        L_0x01bd:
            return r6
        L_0x01be:
            if (r2 == 0) goto L_0x01ca
            boolean r8 = r2.isSlideShow()
            if (r8 == 0) goto L_0x01ca
            r2.finish()
            goto L_0x01cd
        L_0x01ca:
            r7.onBackPressed(r3)
        L_0x01cd:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.NUIDocView.doKeyDown(int, android.view.KeyEvent):boolean");
    }

    public void doPaste() {
    }

    public void doRedo() {
        final DocView docView = getDocView();
        ArDkDoc doc = this.mSession.getDoc();
        if (doc.canRedo()) {
            updateInputView();
            docView.beforeRedo();
            doc.doRedo(() -> docView.afterRedo());
        }
    }

    public void doSave() {
        if (this.mIsTemplate) {
            doSaveAs(false, (SOSaveAsComplete) null);
        } else {
            preSaveQuestion(new Runnable() {
                public void run() {
                    final ProgressDialog displayPleaseWaitWithCancel = Utilities.displayPleaseWaitWithCancel(NUIDocView.this.getContext(), (Runnable) null);
                    new Handler().post(new Runnable() {
                        public void run() {
                            NUIDocView.this.mSession.getDoc().saveTo(NUIDocView.this.mFileState.getInternalPath(), new SODocSaveListener() {
                                public void onComplete(int i, int i2) {
                                    displayPleaseWaitWithCancel.dismiss();
                                    if (i == 0) {
                                        NUIDocView.this.mFileState.saveFile();
                                        NUIDocView.this.updateUIAppearance();
                                        SODataLeakHandlers sODataLeakHandlers = NUIDocView.this.mDataLeakHandlers;
                                        if (sODataLeakHandlers != null) {
                                            sODataLeakHandlers.postSaveHandler(new SOSaveAsComplete() {
                                                public void onComplete(int i, String str) {
                                                    NUIDocView.this.reloadFile();
                                                }

                                                public boolean onFilenameSelected(String str) {
                                                    return true;
                                                }
                                            });
                                            return;
                                        }
                                        return;
                                    }
                                    Utilities.showMessage(NUIDocView.this.activity(), NUIDocView.this.activity().getString(R.string.sodk_editor_error), String.format(NUIDocView.this.activity().getString(R.string.sodk_editor_error_saving_document_code), new Object[]{Integer.valueOf(i2)}));
                                }
                            });
                        }
                    });
                }
            }, () -> {
            });
        }
    }

    public void doSaveAs(final boolean z, final SOSaveAsComplete sOSaveAsComplete1) {
        if (this.mDataLeakHandlers == null && this.mSigningHandler == null) {
            throw new UnsupportedOperationException();
        }
        preSaveQuestion(new Runnable() {
            public void run() {
                try {
                    String userPath = NUIDocView.this.mFileState.getUserPath();
                    if (userPath == null) {
                        userPath = NUIDocView.this.mFileState.getOpenedPath();
                    }
                    File file = new File(userPath);
                    SOSaveAsComplete r0 = new SOSaveAsComplete() {
                        public void onComplete(int i, String str) {
                            if (i == 0) {
                                NUIDocView.this.setFooterText(str);
                                NUIDocView.this.mFileState.setUserPath(str);
                                if (z) {
                                    NUIDocView.this.prefinish();
                                }
                                NUIDocView nUIDocView = NUIDocView.this;
                                if (!nUIDocView.mFinished) {
                                    nUIDocView.mFileState.setHasChanges(false);
                                    NUIDocView.this.onSelectionChanged();
                                    NUIDocView.this.reloadFile();
                                    NUIDocView nUIDocView2 = NUIDocView.this;
                                    nUIDocView2.mIsTemplate = nUIDocView2.mFileState.isTemplate();
                                } else {
                                    return;
                                }
                            } else if (i == 1) {
                                NUIDocView.this.mFileState.setUserPath((String) null);
                            }
                            if (sOSaveAsComplete1 != null) {
                                sOSaveAsComplete1.onComplete(i, str);
                            }
                        }

                        public boolean onFilenameSelected(String str) {
                            if (sOSaveAsComplete1 != null) {
                                return sOSaveAsComplete1.onFilenameSelected(str);
                            }
                            return true;
                        }
                    };
                    NUIDocView nUIDocView = NUIDocView.this;
                    SigningHandler signingHandler = nUIDocView.mSigningHandler;
                    if (signingHandler != null) {
                        signingHandler.saveAsHandler(file.getName(), r0);
                        return;
                    }
                    SODataLeakHandlers sODataLeakHandlers = nUIDocView.mDataLeakHandlers;
                    if (sODataLeakHandlers != null) {
                        sODataLeakHandlers.saveAsHandler(file.getName(), NUIDocView.this.mSession.getDoc(), r0);
                    }
                } catch (UnsupportedOperationException unused) {
                }
            }
        }, () -> {
        });
    }

    public void doSelectAll() {
        getDocView().selectTopLeft();
        this.mSession.getDoc().processKeyCommand(6);
        this.mSession.getDoc().processKeyCommand(15);
    }

    public void doUnderline() {
    }

    public void doUndo() {
        final DocView docView = getDocView();
        ArDkDoc doc = this.mSession.getDoc();
        if (doc.canUndo()) {
            updateInputView();
            docView.beforeUndo();
            doc.doUndo(() -> docView.afterUndo());
        }
    }

    public void doUpdateCustomUI() {
        Runnable runnable;
        if (!this.mFinished && (runnable = this.mOnUpdateUIRunnable) != null) {
            runnable.run();
        }
    }

    public boolean documentHasBeenModified() {
        SODocSession sODocSession = this.mSession;
        return (sODocSession == null || sODocSession.getDoc() == null || this.mFileState == null || (!this.mSession.getDoc().getHasBeenModified() && !this.mFileState.hasChanges())) ? false : true;
    }

    public void enableUI() {
        if (!this.mUIEnabled) {
            setViewAndChildrenEnabled(this, true);
            updateUIAppearance();
        }
        this.mUIEnabled = true;
    }

    public void endDocSession(boolean z) {
        DocListPagesView docListPagesView;
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.finish();
        }
        if (usePagesView() && (docListPagesView = this.mDocPageListView) != null) {
            docListPagesView.finish();
        }
        SODocSession sODocSession = this.mSession;
        if (sODocSession != null) {
            sODocSession.endSession(z);
        }
        endProgress();
    }

    public final void endProgress() {
        Utilities.hideWaitDialog(this.mProgressDialog);
        this.mProgressDialog = null;
        this.mProgressStarted = true;
    }

    public void enforceInitialShowUI(View view) {
        boolean showUI = this.mDocCfgOptions.showUI();
        View findViewById = view.findViewById(R.id.tabhost);
        if (findViewById != null) {
            findViewById.setVisibility(showUI ? View.VISIBLE : View.GONE);
        }
        int i2 = R.id.footer;
        View findViewById2 = view.findViewById(i2);
        if (!showUI) {
            findViewById2.setVisibility(View.GONE);
        }
        if (!this.mShowUI) {
            if (findViewById != null) {
                findViewById.setVisibility(View.GONE);
            }
            view.findViewById(R.id.header).setVisibility(View.GONE);
            view.findViewById(i2).setVisibility(View.GONE);
        }
        this.mFullscreen = !showUI;
    }

    public void enterFullScreen(Runnable runnable) {
        this.mExitFullScreenRunnable = runnable;
        Utilities.hideKeyboard(getContext());
        onFullScreen((View) null);
    }

    public void exportTo(String str) {
        if (this.mDataLeakHandlers != null) {
            try {
                this.mDataLeakHandlers.exportPdfAsHandler(new File(this.mFileState.getOpenedPath()).getName(), str, this.mSession.getDoc());
            } catch (UnsupportedOperationException unused) {
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void findNextSignature() {
    }

    public int findPageContainingPoint(Point point) {
        DocView docView = getDocView();
        if (docView == null) {
            return -1;
        }
        return docView.findPageContainingPoint(point);
    }

    public void findPreviousSignature() {
    }

    public void firstPage() {
        onFirstPageButton((View) null);
    }

    public void fixFileToolbar(int i) {
        LinearLayout linearLayout = (LinearLayout) findViewById(i);
        if (linearLayout != null) {
            View view = null;
            boolean z = false;
            boolean z2 = false;
            for (int i2 = 0; i2 < linearLayout.getChildCount(); i2++) {
                View childAt = linearLayout.getChildAt(i2);
                if (!(childAt instanceof ToolbarButton)) {
                    if (!z2) {
                        childAt.setVisibility(View.GONE);
                    } else if (!z && view != null) {
                        view.setVisibility(View.GONE);
                    }
                    view = childAt;
                    z = false;
                } else if (childAt.getVisibility() == View.VISIBLE) {
                    z = true;
                    z2 = true;
                }
            }
            if (view != null && !z) {
                view.setVisibility(View.GONE);
            }
            if (!z2) {
                linearLayout.setVisibility(View.GONE);
                ((View) linearLayout.getParent()).setVisibility(View.GONE);
            }
        }
    }

    public void focusInputView() {
        if (!this.mDocCfgOptions.isEditingEnabled()) {
            defocusInputView();
            return;
        }
        InputView inputView = this.mInputView;
        if (inputView != null) {
            inputView.setFocus();
        }
    }

    public void forceReload() {
        this.mForceReload = true;
    }

    public void forceReloadAtResume() {
        this.mForceReloadAtResume = true;
    }

    public String getAuthor() {
        ArDkDoc doc = getDocView().getDoc();
        return doc != null ? doc.getAuthor() : "";
    }

    public int getBorderColor() {
        return ContextCompat.getColor(getContext(), R.color.sodk_editor_selected_page_border_color);
    }

    public String getCurrentTab() {
        return this.mCurrentTab;
    }

    public int getCutoutHeightForRotation() {
        Point realScreenSize = Utilities.getRealScreenSize(activity());
        if (realScreenSize.x > realScreenSize.y) {
            return 0;
        }
        int rotation = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            rotation = ((WindowManager) activity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        }
        if (rotation == 0) {
            int[] iArr = this.cutoutHeights;
            int i = iArr[0];
            return iArr[2] != 0 ? (int) (((float) i) - (((float) findViewById(R.id.footer).getHeight()) * 0.9f)) : i;
        } else if (rotation == 1) {
            int[] iArr2 = this.cutoutHeights;
            int i2 = iArr2[1];
            return iArr2[3] != 0 ? (int) (((float) i2) - (((float) findViewById(R.id.footer).getHeight()) * 0.9f)) : i2;
        } else if (rotation == 2) {
            int[] iArr3 = this.cutoutHeights;
            int i3 = iArr3[2];
            return iArr3[0] != 0 ? (int) (((float) i3) - (((float) findViewById(R.id.footer).getHeight()) * 0.9f)) : i3;
        } else if (rotation != 3) {
            return 0;
        } else {
            int[] iArr4 = this.cutoutHeights;
            int i4 = iArr4[3];
            return iArr4[1] != 0 ? (int) (((float) i4) - (((float) findViewById(R.id.footer).getHeight()) * 0.9f)) : i4;
        }
    }

    public ArDkDoc getDoc() {
        SODocSession sODocSession = this.mSession;
        if (sODocSession == null) {
            return null;
        }
        return sODocSession.getDoc();
    }

    public String getDocFileExtension() {
        SOFileState sOFileState = this.mState;
        if (sOFileState != null) {
            return FileUtils.getExtension(sOFileState.getUserPath());
        }
        SODocSession sODocSession = this.mSession;
        if (sODocSession != null) {
            return FileUtils.getExtension(sODocSession.getUserPath());
        }
        return FileUtils.getFileTypeExtension(getContext(), this.mStartUri);
    }

    public DocListPagesView getDocListPagesView() {
        return this.mDocPageListView;
    }

    public DocView getDocView() {
        return this.mDocView;
    }

    public int getFlowMode() {
        return ((SODoc) getDoc()).mFlowMode;
    }

    public int getInitialTab() {
        return 0;
    }

    public InputView getInputView() {
        return this.mInputView;
    }

    public int getKeyboardHeight() {
        return this.keyboardHeight;
    }

    public int getLayoutId() {
        return 0;
    }

    public int getLineColor() {
        return getDocView().getInkLineColor();
    }

    public float getLineThickness() {
        return getDocView().getInkLineThickness();
    }

    public int getPageCount() {
        return this.mPageCount;
    }

    public int getPageNumber() {
        return this.mCurrentPageNum + 1;
    }

    public String getPageNumberText() {
        return String.format(getResources().getConfiguration().locale, getContext().getString(R.string.sodk_editor_page_d_of_d), new Object[]{Integer.valueOf(this.mCurrentPageNum + 1), Integer.valueOf(getPageCount())});
    }

    public String getPersistedAuthor() {
        return SOPreferences.getStringPreference(SOPreferences.getPreferencesObject(activity(), "general"), "DocAuthKey", Utilities.getApplicationName(activity()));
    }

    public float getScaleFactor() {
        DocView docView = this.mDocView;
        if (docView != null) {
            return docView.getScaleFactor();
        }
        return -1.0f;
    }

    public int getScrollPositionX() {
        DocView docView = this.mDocView;
        if (docView != null) {
            return docView.getScrollPositionX();
        }
        return -1;
    }

    public int getScrollPositionY() {
        DocView docView = this.mDocView;
        if (docView != null) {
            return docView.getScrollPositionY();
        }
        return -1;
    }

    public String getSelectedText() {
        return getDoc().getSelectionAsText();
    }

    public SODocSession getSession() {
        return this.mSession;
    }

    public int getSignatureCount() {
        return 0;
    }

    public View getSingleTabView() {
        return this.tabHost.getTabWidget().getChildTabViewAt(this.tabHost.getTabWidget().getTabCount() - 1);
    }

    public int getStartPage() {
        return this.mStartPage;
    }

    public TabData[] getTabData() {
        if (this.mTabs == null) {
            this.mTabs = new TabData[6];
            if (this.mDocCfgOptions.isEditingEnabled()) {
                this.mTabs[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_left, 0);
                TabData[] tabDataArr = this.mTabs;
                String string = getContext().getString(R.string.sodk_editor_tab_edit);
                int i = R.id.editTab;
                int i2 = R.layout.sodk_editor_tab;
                tabDataArr[1] = new TabData(this, string, i, i2, 0);
                this.mTabs[2] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_insert), R.id.insertTab, i2, 0);
                this.mTabs[3] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_pages), R.id.pagesTab, i2, 0);
                this.mTabs[4] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_draw), R.id.drawTab, i2, 0);
                this.mTabs[5] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_review), R.id.reviewTab, R.layout.sodk_editor_tab_right, 0);
            } else {
                this.mTabs[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_left, 0);
                TabData[] tabDataArr2 = this.mTabs;
                String string2 = getContext().getString(R.string.sodk_editor_tab_edit);
                int i3 = R.id.editTab;
                int i4 = R.layout.sodk_editor_tab;
                int i5 = i4;
                tabDataArr2[1] = new TabData(this, string2, i3, i5, 8);
                this.mTabs[2] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_insert), R.id.insertTab, i5, 8);
                TabData[] tabDataArr3 = this.mTabs;
                String string3 = getContext().getString(R.string.sodk_editor_tab_pages);
                int i6 = R.id.pagesTab;
                int i7 = R.layout.sodk_editor_tab_right;
                tabDataArr3[3] = new TabData(this, string3, i6, i7, 0);
                this.mTabs[4] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_draw), R.id.drawTab, i4, 8);
                this.mTabs[5] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_review), R.id.reviewTab, i7, 8);
            }
        }
        if (!this.mDocCfgOptions.isTrackChangesFeatureEnabled()) {
            Objects.requireNonNull(this.mDocCfgOptions);
            TabData[] tabDataArr4 = this.mTabs;
            tabDataArr4[4].visibility = 8;
            tabDataArr4[3].layoutId = R.layout.sodk_editor_tab_right;
        }
        return this.mTabs;
    }

    public int getTabSelectedColor() {
        if (getResources().getInteger(R.integer.sodk_editor_ui_doc_tab_color_from_doctype) == 0) {
            return ContextCompat.getColor(activity(), R.color.sodk_editor_header_color_selected);
        }
        return ContextCompat.getColor(activity(), R.color.sodk_editor_header_doc_color);
    }

    public int getTabSelectedTextColor() {
        return ContextCompat.getColor(activity(), R.color.sodk_editor_header_text_color_selected);
    }

    public int getTabUnselectedColor() {
        if (getResources().getInteger(R.integer.sodk_editor_ui_doc_tabbar_color_from_doctype) == 0) {
            return ContextCompat.getColor(activity(), R.color.sodk_editor_header_color);
        }
        return ContextCompat.getColor(activity(), R.color.sodk_editor_header_doc_color);
    }

    public int getTabUnselectedTextColor() {
        return ContextCompat.getColor(activity(), R.color.sodk_editor_header_text_color);
    }

    public int getTargetPageNumber() {
        DocPageView findPageContainingSelection = getDocView().findPageContainingSelection();
        if (findPageContainingSelection != null) {
            return findPageContainingSelection.getPageNumber();
        }
        Rect rect = new Rect();
        getDocView().getGlobalVisibleRect(rect);
        DocPageView findPageViewContainingPoint = getDocView().findPageViewContainingPoint((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2, true);
        if (findPageViewContainingPoint != null) {
            return findPageViewContainingPoint.getPageNumber();
        }
        return 0;
    }

    public void goBack(final Runnable runnable1) {
        if (this.mCanGoBack) {
            prepareToGoBack();
            if (documentHasBeenModified()) {
                activity().runOnUiThread(new Runnable() {
                    public void run() {
                        int identifier;
                        int i = R.string.sodk_editor_save;
                        NUIDocView nUIDocView = NUIDocView.this;
                        if (!(nUIDocView.mCustomDocdata == null || (identifier = nUIDocView.getContext().getResources().getIdentifier("secure_save_upper", "string", NUIDocView.this.getContext().getPackageName())) == 0)) {
                            i = identifier;
                        }
                        new AlertDialog.Builder(NUIDocView.this.activity(), R.style.sodk_editor_alert_dialog_style).setTitle(R.string.sodk_editor_document_has_been_modified).setMessage(R.string.sodk_editor_would_you_like_to_save_your_changes).setCancelable(false).setPositiveButton(i, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                NUIDocView.this.preSaveQuestion(new Runnable() {
                                    public void run() {
                                        NUIDocView nUIDocView = NUIDocView.this;
                                        if (nUIDocView.mCustomDocdata != null) {
                                            nUIDocView.onCustomSaveButton((View) null);
                                        } else if (nUIDocView.mIsTemplate) {
                                            nUIDocView.doSaveAs(true, new SOSaveAsComplete() {
                                                public void onComplete(int i, String str) {
                                                    Runnable runnable = runnable1;
                                                    if (runnable != null) {
                                                        runnable.run();
                                                    }
                                                }

                                                public boolean onFilenameSelected(String str) {
                                                    return true;
                                                }
                                            });
                                        } else {
                                            final ProgressDialog displayPleaseWaitWithCancel = Utilities.displayPleaseWaitWithCancel(nUIDocView.getContext(), (Runnable) null);
                                            new Handler().post(new Runnable() {
                                                public void run() {
                                                    NUIDocView.this.mSession.getDoc().saveTo(NUIDocView.this.mFileState.getInternalPath(), new SODocSaveListener() {
                                                        public void onComplete(int i, int i2) {
                                                            displayPleaseWaitWithCancel.dismiss();
                                                            if (i == 0) {
                                                                NUIDocView.this.mFileState.saveFile();
                                                                NUIDocView nUIDocView = NUIDocView.this;
                                                                SODataLeakHandlers sODataLeakHandlers = nUIDocView.mDataLeakHandlers;
                                                                if (sODataLeakHandlers != null) {
                                                                    sODataLeakHandlers.postSaveHandler(new SOSaveAsComplete() {
                                                                        public void onComplete(int i, String str) {
                                                                            if (i == 0) {
                                                                                NUIDocView.access$3100(NUIDocView.this);
                                                                                NUIDocView.this.prefinish();
                                                                                Runnable runnable = runnable1;
                                                                                if (runnable != null) {
                                                                                    runnable.run();
                                                                                }
                                                                            }
                                                                        }

                                                                        public boolean onFilenameSelected(String str) {
                                                                            return true;
                                                                        }
                                                                    });
                                                                    return;
                                                                }
                                                                NUIDocView.access$3100(nUIDocView);
                                                                NUIDocView.this.prefinish();
                                                                Runnable runnable = runnable1;
                                                                if (runnable != null) {
                                                                    runnable.run();
                                                                    return;
                                                                }
                                                                return;
                                                            }
                                                            NUIDocView.access$3100(NUIDocView.this);
                                                            Utilities.showMessage(NUIDocView.this.activity(), NUIDocView.this.activity().getString(R.string.sodk_editor_error), String.format(NUIDocView.this.activity().getString(R.string.sodk_editor_error_saving_document_code), new Object[]{Integer.valueOf(i2)}));
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                }, new Runnable() {
                                    public void run() {
                                        Runnable runnable = runnable1;
                                        if (runnable != null) {
                                            runnable.run();
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton(R.string.sodk_editor_discard, (dialogInterface, i12) -> {
                            dialogInterface.dismiss();
                            NUIDocView.access$3100(NUIDocView.this);
                            NUIDocView.this.mEndSessionSilent = new Boolean(false);
                            NUIDocView.this.prefinish();
                            Runnable runnable = runnable1;
                            if (runnable != null) {
                                runnable.run();
                            }
                        }).setNeutralButton(R.string.sodk_editor_continue_editing, (dialogInterface, i1) -> dialogInterface.dismiss()).create().show();
                    }
                });
                return;
            }
            this.mEndSessionSilent = new Boolean(false);
            prefinish();
            if (runnable1 != null) {
                runnable1.run();
            }
        }
    }

    public void goToPage(int i) {
        goToPage(i, false);
    }

    public void gotoInternalLocation(int i, RectF rectF) {
        DocView docView = getDocView();
        docView.addHistory(docView.getScrollX(), docView.getScrollY(), docView.getScale(), true);
        docView.addHistory(docView.getScrollX(), docView.getScrollY() - docView.scrollBoxToTopAmount(i, rectF), docView.getScale(), false);
        docView.scrollBoxToTop(i, rectF);
    }

    public void handlePagesTab(String str) {
        if (str.equals(activity().getString(R.string.sodk_editor_tab_pages))) {
            showPages();
        } else {
            hidePages();
        }
    }

    public void handleStartPage() {
        int startPage = getStartPage();
        if (startPage >= 0 && getPageCount() > startPage) {
            setStartPage(-1);
            this.mDocView.setStartPage(startPage);
            this.mCurrentPageNum = startPage;
            setPageNumberText();
            ViewingState viewingState = this.mViewingState;
            if (viewingState != null) {
                if (viewingState.pageListVisible) {
                    showPages(startPage);
                    showPagesTab();
                }
                getDocView().setScale(this.mViewingState.scale);
                getDocView().forceLayout();
            }
            SODocSession sODocSession = this.mSession;
            if (sODocSession != null && this.mPrintButton != null) {
                if (!sODocSession.getDoc().canPrint() || (!this.mDocCfgOptions.isPrintingEnabled() && !this.mDocCfgOptions.isSecurePrintingEnabled())) {
                    this.mPrintButton.setVisibility(View.GONE);
                } else {
                    this.mPrintButton.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public boolean hasHistory() {
        return false;
    }

    public boolean hasIndent() {
        return false;
    }

    public boolean hasNextHistory() {
        History history = getDocView().getHistory();
        if (history != null) {
            return history.canNext();
        }
        return false;
    }

    public boolean hasPreviousHistory() {
        History history = getDocView().getHistory();
        if (history != null) {
            return history.canPrevious();
        }
        return false;
    }

    public boolean hasRedo() {
        return true;
    }

    public boolean hasReflow() {
        return false;
    }

    public boolean hasSearch() {
        return true;
    }

    public boolean hasSelectionAlignment() {
        return false;
    }

    public boolean hasUndo() {
        return true;
    }

    public final void hideMainTabs() {
        int tabCount = this.tabHost.getTabWidget().getTabCount();
        for (int i = 1; i < tabCount - 1; i++) {
            this.tabHost.getTabWidget().getChildAt(i).setVisibility(View.GONE);
        }
    }

    public void hidePages() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.pages_container);
        if (relativeLayout != null && relativeLayout.getVisibility() != View.GONE) {
            final int i = this.mCurrentPageNum;
            relativeLayout.setVisibility(View.GONE);
            this.mDocView.onHidePages();
            doUpdateCustomUI();
            new Handler().post(new Runnable() {
                public void run() {
                    NUIDocView nUIDocView = NUIDocView.this;
                    nUIDocView.setCurrentPage(nUIDocView, i);
                }
            });
        }
    }

    public void hideUnusedButtons() {
    }

    public void highlightSelection() {
    }

    public void historyNext() {
    }

    public void historyPrevious() {
    }

    public final void initClipboardHandler() {
        try {
            SOClipboardHandler clipboardHandler = ArDkLib.getClipboardHandler();
            if (clipboardHandler != null) {
                clipboardHandler.initClipboardHandler(activity());
                return;
            }
            throw new ClassNotFoundException();
        } catch (ExceptionInInitializerError unused) {
            Log.e("NUIDocView", String.format("initClipboardHandler() experienced unexpected exception [%s]", new Object[]{"ExceptionInInitializerError"}));
        } catch (LinkageError unused2) {
            Log.e("NUIDocView", String.format("initClipboardHandler() experienced unexpected exception [%s]", new Object[]{"LinkageError"}));
        } catch (SecurityException unused3) {
            Log.e("NUIDocView", String.format("initClipboardHandler() experienced unexpected exception [%s]", new Object[]{"SecurityException"}));
        } catch (ClassNotFoundException unused4) {
            Log.i("NUIDocView", "initClipboardHandler implementation unavailable");
        }
    }

    public final void initialize(Context context) {
        mCurrentNUIDocView = this;
        this.mDecorView = ((Activity) getContext()).getWindow().getDecorView();
        FileUtils.init();
        this.mPrevKeyboard = context.getResources().getConfiguration().keyboard;
        registerGetContentLauncher(context);
    }

    public boolean inputViewHasFocus() {
        InputView inputView = this.mInputView;
        if (inputView != null) {
            return inputView.hasFocus();
        }
        return false;
    }

    public boolean isActivityActive() {
        return this.mIsActivityActive;
    }

    public boolean isAlterableTextSelection() {
        ArDkDoc doc = getDoc();
        if (doc != null) {
            return doc.getSelectionIsAlterableTextSelection();
        }
        return false;
    }

    public boolean isDigitalSignatureMode() {
        return false;
    }

    public boolean isDocumentModified() {
        return documentHasBeenModified();
    }

    public boolean isDrawModeOn() {
        return this.mDocView.getDrawMode();
    }

    public boolean isESignatureMode() {
        return false;
    }

    public boolean isFullScreen() {
        if (!this.mDocCfgOptions.isFullscreenEnabled()) {
            return false;
        }
        return this.mFullscreen;
    }

    public boolean isKeyboardVisible() {
        return this.keyboardShown;
    }

    public boolean isLandscapePhone() {
        return this.mLastOrientation == 2 && Utilities.isPhoneDevice(getContext());
    }

    public boolean isNoteModeOn() {
        return false;
    }

    public boolean isPageListVisible() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.pages_container);
        return relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE;
    }

    public boolean isPagesTab() {
        return getCurrentTab().equals(activity().getString(R.string.sodk_editor_tab_pages));
    }

    public boolean isRedactionMode() {
        return false;
    }

    public boolean isSearchFocused() {
        SOEditText sOEditText = (SOEditText) findViewById(R.id.search_text_input);
        return sOEditText != null && sOEditText.getVisibility() == View.VISIBLE && sOEditText.isShown() && sOEditText.isFieldFocused();
    }

    public boolean isSearchVisible() {
        View findViewById = findViewById(R.id.search_text_input);
        return findViewById != null && findViewById.getVisibility() == View.VISIBLE && findViewById.isShown();
    }

    public boolean isTOCEnabled() {
        return false;
    }

    public void lastPage() {
        onLastPageButton((View) null);
    }

    public void launchGetImage(String str, SelectImageListener selectImageListener) {
        if (str == null) {
            throw new RuntimeException("NUIDocView.launchGetImage - input must not be null");
        } else if (selectImageListener != null) {
            this.onImageSelectedListener = selectImageListener;
            mGetContent.launch(str, (ActivityOptionsCompat) null);
        } else {
            throw new RuntimeException("NUIDocView.launchGetImage - listener must not be null");
        }
    }

    public void layoutAfterPageLoad() {
        layoutNow();
    }

    public void layoutNow() {
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.layoutNow();
        }
        if (this.mDocPageListView != null && usePagesView() && isPageListVisible()) {
            this.mDocPageListView.layoutNow();
        }
    }

    public void measureTabs() {
        int i;
        TextView textView;
        TabHost tabHost2 = (TabHost) findViewById(R.id.tabhost);
        TabWidget tabWidget = tabHost2.getTabWidget();
        if (Utilities.isPhoneDevice(activity())) {
            View childTabViewAt = tabHost2.getTabWidget().getChildTabViewAt(tabHost2.getTabWidget().getTabCount() - 1);
            childTabViewAt.measure(0, 0);
            i = childTabViewAt.getMeasuredWidth() + 0;
        } else {
            TabData[] tabData = getTabData();
            int i2 = 0;
            int i3 = 0;
            while (i3 < tabData.length) {
                i3++;
                View childAt = tabWidget.getChildAt(i3);
                if (!(childAt == null || (textView = (TextView) childAt.findViewById(R.id.tabText)) == null)) {
                    textView.measure(0, 0);
                    int paddingRight = childAt.getPaddingRight() + childAt.getPaddingLeft() + textView.getMeasuredWidth();
                    childAt.measure(0, 0);
                    childAt.setLayoutParams(new LinearLayout.LayoutParams(paddingRight, childAt.getMeasuredHeight()));
                    i2 += paddingRight;
                }
            }
            i = i2;
        }
        tabWidget.getLayoutParams().width = i;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.onImageSelectedListener == null) {
            SODataLeakHandlers sODataLeakHandlers = this.mDataLeakHandlers;
            if (sODataLeakHandlers != null) {
                sODataLeakHandlers.onActivityResult(i, i2, intent);
                return;
            }
            throw new UnsupportedOperationException();
        } else if (intent != null) {
            Uri data = intent.getData();
            SelectImageListener selectImageListener = this.onImageSelectedListener;
            if (selectImageListener != null) {
                selectImageListener.onImageSelected(data);
            }
            this.onImageSelectedListener = null;
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                int rotation = ((WindowManager) activity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
                WindowInsets rootWindowInsets = activity().getWindow().getDecorView().getRootWindowInsets();
                if (rotation == 0) {
                    this.cutoutHeights[0] = rootWindowInsets.getDisplayCutout().getSafeInsetTop();
                    this.cutoutHeights[3] = rootWindowInsets.getDisplayCutout().getSafeInsetLeft();
                    this.cutoutHeights[2] = rootWindowInsets.getDisplayCutout().getSafeInsetBottom();
                    this.cutoutHeights[1] = rootWindowInsets.getDisplayCutout().getSafeInsetRight();
                } else if (rotation == 1) {
                    this.cutoutHeights[0] = rootWindowInsets.getDisplayCutout().getSafeInsetLeft();
                    this.cutoutHeights[3] = rootWindowInsets.getDisplayCutout().getSafeInsetBottom();
                    this.cutoutHeights[2] = rootWindowInsets.getDisplayCutout().getSafeInsetRight();
                    this.cutoutHeights[1] = rootWindowInsets.getDisplayCutout().getSafeInsetTop();
                } else if (rotation == 2) {
                    this.cutoutHeights[0] = rootWindowInsets.getDisplayCutout().getSafeInsetBottom();
                    this.cutoutHeights[3] = rootWindowInsets.getDisplayCutout().getSafeInsetRight();
                    this.cutoutHeights[2] = rootWindowInsets.getDisplayCutout().getSafeInsetTop();
                    this.cutoutHeights[1] = rootWindowInsets.getDisplayCutout().getSafeInsetLeft();
                } else if (rotation == 3) {
                    this.cutoutHeights[0] = rootWindowInsets.getDisplayCutout().getSafeInsetRight();
                    this.cutoutHeights[3] = rootWindowInsets.getDisplayCutout().getSafeInsetTop();
                    this.cutoutHeights[2] = rootWindowInsets.getDisplayCutout().getSafeInsetLeft();
                    this.cutoutHeights[1] = rootWindowInsets.getDisplayCutout().getSafeInsetBottom();
                }
            } catch (NullPointerException unused) {
            }
        }
    }

    public void onAuthorButton(View view) {
        final ArDkDoc doc = getDocView().getDoc();
        AuthorDialog.show(activity(), new AuthorDialog.AuthorDialogListener() {
            public void onCancel() {
            }

            public void onOK(String str) {
                doc.setAuthor(str);
                NUIDocView.this.persistAuthor(str);
            }
        }, doc.getAuthor());
    }

    public void onBackPressed(Runnable runnable) {
        goBack(runnable);
    }

    public void onClick(View view) {
        Button button;
        if (view != null && !this.mFinished) {
            if (view == this.mSaveAsButton) {
                onSaveAsButton(view);
            }
            if (view == this.mSaveButton) {
                onSaveButton(view);
            }
            if (view == this.mCustomSaveButton) {
                onCustomSaveButton(view);
            }
            if (view == this.mSavePdfButton) {
                onSavePDFButton(view);
            }
            if (view == this.mExportPdfAsButton) {
                onPDFExportAsButton(view);
            }
            if (view == this.mPrintButton) {
                onPrintButton(view);
            }
            if (view == this.mShareButton) {
                onShareButton(view);
            }
            if (view == this.mOpenInButton) {
                onOpenInButton(view);
            }
            if (view == this.mOpenPdfInButton) {
                onOpenPDFInButton(view);
            }
            if (view == this.mProtectButton) {
                onProtectButton(view);
            }
            if (view == this.mCopyButton2) {
                doCopy();
            }
            if (view == this.mFirstPageButton) {
                onFirstPageButton(view);
            }
            if (view == this.mLastPageButton) {
                onLastPageButton(view);
            }
            if (view == this.mReflowButton) {
                onReflowButton(view);
            }
            if (view == this.mUndoButton) {
                onUndoButton(view);
            }
            if (view == this.mRedoButton) {
                onRedoButton(view);
            }
            if (view == this.mSearchButton) {
                onSearchButton(view);
            }
            if (view == this.mSearchNextButton) {
                onSearchNext(view);
            }
            if (view == this.mSearchPreviousButton) {
                onSearchPrevious(view);
            }
            if (view == this.mBackButton) {
                goBack((Runnable) null);
            }
            if (view == this.mInsertImageButton) {
                onInsertImageButton(view);
            }
            if (view == this.mInsertPhotoButton) {
                onInsertPhotoButton(view);
            }
            if (view == this.mDrawButton) {
                onDrawButton(view);
            }
            if (view == this.mDrawLineColorButton) {
                onLineColorButton(view);
            }
            if (view == this.mDrawLineThicknessButton) {
                onLineThicknessButton(view);
            }
            if (view == this.mDeleteInkButton) {
                onDeleteButton(view);
            }
            if (this.mDocCfgOptions.isFullscreenEnabled() && (button = this.mFullscreenButton) != null && view == button) {
                onFullScreen(view);
            }
        }
    }

    public void onConfigurationChange(Configuration configuration) {
        DocListPagesView docListPagesView;
        if (!(configuration == null || configuration.keyboard == this.mPrevKeyboard)) {
            resetInputView();
            this.mPrevKeyboard = configuration.keyboard;
        }
        PdfExportAsPopup pdfExportAsPopup = this.mPdfExportAsPopup;
        if (pdfExportAsPopup != null) {
            pdfExportAsPopup.onDismiss();
        }
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.onConfigurationChange();
        }
        if (usePagesView() && (docListPagesView = this.mDocPageListView) != null) {
            docListPagesView.onConfigurationChange();
        }
    }

    public void onCustomSaveButton(View view) {
        if (this.mDataLeakHandlers != null) {
            Objects.requireNonNull(this.mDocCfgOptions);
            preSaveQuestion(new Runnable() {
                public void run() {
                    String userPath = NUIDocView.this.mFileState.getUserPath();
                    if (userPath == null) {
                        userPath = NUIDocView.this.mFileState.getOpenedPath();
                    }
                    File file = new File(userPath);
                    NUIDocView.this.preSave();
                    try {
                        NUIDocView.this.mDataLeakHandlers.customSaveHandler(file.getName(), NUIDocView.this.mSession.getDoc(), NUIDocView.this.mCustomDocdata, new SOCustomSaveComplete() {
                            public void onComplete(int i, String str, boolean z) {
                                NUIDocView.this.mFileState.setHasChanges(false);
                                if (i == 0) {
                                    NUIDocView.this.mFileState.setHasChanges(false);
                                }
                                if (z) {
                                    NUIDocView.this.prefinish();
                                }
                            }
                        });
                    } catch (IOException | UnsupportedOperationException unused) {
                    }
                }
            }, new Runnable() {
                public void run() {
                }
            });
        }
    }

    public void onDeleteButton(View view) {
        DocView docView = getDocView();
        if (getDoc().getSelectionCanBeDeleted()) {
            getDoc().selectionDelete();
            docView.onSelectionDelete();
            updateUIAppearance();
        } else if (docView.getDrawMode()) {
            docView.clearInk();
            updateUIAppearance();
        }
    }

    public void onDestroy() {
        recycleBitmaps();
        if (this.mDeleteOnClose != null) {
            for (int i = 0; i < this.mDeleteOnClose.size(); i++) {
                FileUtils.deleteFile(this.mDeleteOnClose.get(i));
            }
            this.mDeleteOnClose.clear();
        }
        SODataLeakHandlers sODataLeakHandlers = this.mDataLeakHandlers;
        if (sODataLeakHandlers != null) {
            sODataLeakHandlers.finaliseDataLeakHandlers();
        }
        if (mCurrentNUIDocView == this) {
            mCurrentNUIDocView = null;
        }
    }

    public void onDeviceSizeChange() {
        Button button;
        View findViewById = findViewById(R.id.back_button_after);
        if (Utilities.isPhoneDevice(activity())) {
            scaleHeader();
            Button button2 = this.mSearchButton;
            if (button2 != null) {
                button2.setVisibility(View.GONE);
            }
            if (this.tabHost != null) {
                hideMainTabs();
                getSingleTabView().setVisibility(View.VISIBLE);
            }
            findViewById.getLayoutParams().width = Utilities.convertDpToPixel((float) ((int) getContext().getResources().getDimension(R.dimen.sodk_editor_after_back_button_phone)));
            return;
        }
        if (hasSearch() && (button = this.mSearchButton) != null) {
            button.setVisibility(View.VISIBLE);
        }
        TabHost tabHost2 = this.tabHost;
        if (tabHost2 != null) {
            int tabCount = tabHost2.getTabWidget().getTabCount();
            for (int i = 1; i < tabCount - 1; i++) {
                this.tabHost.getTabWidget().getChildAt(i).setVisibility(View.VISIBLE);
            }
            getSingleTabView().setVisibility(View.GONE);
        }
        findViewById.getLayoutParams().width = Utilities.convertDpToPixel((float) ((int) getContext().getResources().getDimension(R.dimen.sodk_editor_after_back_button)));
    }

    public void onDocCompleted() {
        if (!this.mFinished) {
            this.mCompleted = true;
            int numPages = this.mSession.getDoc().getNumPages();
            this.mPageCount = numPages;
            this.mAdapter.setCount(numPages);
            layoutNow();
            DocumentListener documentListener = this.mDocumentListener;
            if (documentListener != null) {
                documentListener.onDocCompleted();
            }
            if (SOLib.getLib(activity()).isTrackChangesEnabled()) {
                this.mSession.getDoc().setAuthor(SOPreferences.getStringPreference(SOPreferences.getPreferencesObject(activity(), "general"), "DocAuthKey", Utilities.getApplicationName(activity())));
            }
        }
    }

    public void onDrawButton(View view) {
        getDocView().onDrawMode();
        updateUIAppearance();
    }

    public void onFirstPageButton(View view) {
        this.mDocView.addPageHistory(0);
        setCurrentPage(this, 0);
    }

    public void onFullScreen(View view) {
        if (!this.mFinished && getDocView() != null && !isFullScreen()) {
            getDocView().saveInk();
            getDocView().setDrawModeOff();
            updateUIAppearance();
            if (this.mDocCfgOptions.isFullscreenEnabled()) {
                this.mFullscreen = true;
                Utilities.hideKeyboard(getContext());
                getDocView().onFullscreen(true);
                onFullScreenHide();
                if (this.mFullscreenToast == null) {
                    Toast makeText = Toast.makeText(getContext(), getContext().getString(R.string.sodk_editor_fullscreen_warning), Toast.LENGTH_SHORT);
                    this.mFullscreenToast = makeText;
                    makeText.setGravity(53, 0, 0);
                }
                this.mFullscreenToast.show();
            }
        }
    }

    public void onFullScreenHide() {
        findViewById(R.id.tabhost).setVisibility(View.GONE);
        findViewById(R.id.header).setVisibility(View.GONE);
        findViewById(R.id.footer).setVisibility(View.GONE);
        hidePages();
        layoutNow();
    }

    public void onInsertImageButton(View view) {
        setInsertButtonsClickable(false);
        showKeyboard(false, new Runnable() {
            public void run() {
                SODataLeakHandlers sODataLeakHandlers = NUIDocView.this.mDataLeakHandlers;
                if (sODataLeakHandlers != null) {
                    try {
                        sODataLeakHandlers.insertImageHandler(NUIDocView.this);
                    } catch (UnsupportedOperationException unused) {
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        });
    }

    public void onInsertPhotoButton(View view) {
        askForCameraPermission(new Runnable() {
            public void run() {
                NUIDocView.this.showKeyboard(false, new Runnable() {
                    public void run() {
                        NUIDocView nUIDocView = NUIDocView.this;
                        SODataLeakHandlers sODataLeakHandlers = nUIDocView.mDataLeakHandlers;
                        if (sODataLeakHandlers != null) {
                            try {
                                sODataLeakHandlers.insertPhotoHandler(nUIDocView);
                            } catch (UnsupportedOperationException unused) {
                            }
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    }
                });
            }
        });
    }

    public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
        View currentFocus;
        BaseActivity baseActivity = getContext() instanceof BaseActivity ? (BaseActivity) getContext() : null;
        if (baseActivity == null || baseActivity.isSlideShow() || (currentFocus = baseActivity.getCurrentFocus()) == null || keyEvent.getKeyCode() != 4) {
            return super.onKeyPreIme(i, keyEvent);
        }
        currentFocus.clearFocus();
        Utilities.hideKeyboard(getContext());
        return true;
    }

    public void onLastPageButton(View view) {
        this.mDocView.addPageHistory(getPageCount() - 1);
        setCurrentPage(this, getPageCount() - 1);
    }

    public void onLayoutChanged() {
        SODocSession sODocSession = this.mSession;
        if (sODocSession != null && sODocSession.getDoc() != null && !this.mFinished) {
            this.mDocView.onLayoutChanged();
        }
    }

    public void onLineColorButton(View view) {
        final DocView docView = getDocView();
        if (docView.getDrawMode()) {
            InkColorDialog inkColorDialog = new InkColorDialog(1, docView.getInkLineColor(), activity(), this.mDrawLineColorButton, new InkColorDialog.ColorChangedListener() {
                public void onColorChanged(String str) {
                    int parseColor = Color.parseColor(str);
                    docView.setInkLineColor(parseColor);
                    NUIDocView.this.mDrawLineColorButton.setDrawableColor(parseColor);
                }
            }, true);
            inkColorDialog.setShowTitle(false);
            inkColorDialog.show();
        }
    }

    public void onLineThicknessButton(View view) {
        final DocView docView = getDocView();
        if (docView.getDrawMode()) {
            new InkLineWidthDialog().show(activity(), this.mDrawLineThicknessButton, docView.getInkLineThickness(), docView::setInkLineThickness);
        }
    }

    public void onMeasure(int i, int i2) {
        if (!this.mFinished) {
            int i3 = this.keyboardHeight;
            int height = this.mDecorView.getHeight();
            int i4 = (height * 15) / 100;
            Rect rect = new Rect();
            this.mDecorView.getWindowVisibleDisplayFrame(rect);
            int i5 = height - rect.bottom;
            this.keyboardHeight = i5;
            this.keyboardHeight = getCutoutHeightForRotation() + i5;
            Resources resources = getContext().getResources();
            int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            if ((identifier > 0 && resources.getBoolean(identifier)) || Utilities.isEmulator()) {
                int identifier2 = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                this.keyboardHeight -= identifier2 > 0 ? resources.getDimensionPixelSize(identifier2) : 0;
            }
            int i6 = this.keyboardHeight;
            if (i6 < i4) {
                this.keyboardHeight = 0;
                if (this.keyboardShown) {
                    onShowKeyboard(false);
                }
            } else if (i3 != i6) {
                onShowKeyboard(true);
            }
        }
        super.onMeasure(i, i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        r6 = (com.artifex.sonui.editor.SOHorizontalScrollView) r6.getChildAt(0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onNewTabShown(String r6) {
        /*
            r5 = this;
            com.artifex.sonui.editor.NUIDocView$TabData[] r0 = r5.mTabs
            r1 = 0
            if (r0 == 0) goto L_0x0019
            r0 = 0
        L_0x0006:
            com.artifex.sonui.editor.NUIDocView$TabData[] r2 = r5.mTabs
            int r3 = r2.length
            if (r0 >= r3) goto L_0x0019
            r2 = r2[r0]
            java.lang.String r3 = r2.name
            boolean r3 = r6.equals(r3)
            if (r3 == 0) goto L_0x0016
            goto L_0x001a
        L_0x0016:
            int r0 = r0 + 1
            goto L_0x0006
        L_0x0019:
            r2 = 0
        L_0x001a:
            if (r2 != 0) goto L_0x001d
            return
        L_0x001d:
            int r6 = r2.contentId
            android.view.View r6 = r5.findViewById(r6)
            android.widget.LinearLayout r6 = (android.widget.LinearLayout) r6
            if (r6 == 0) goto L_0x007e
            int r0 = r6.getChildCount()
            if (r0 != 0) goto L_0x002e
            goto L_0x007e
        L_0x002e:
            android.view.View r6 = r6.getChildAt(r1)
            com.artifex.sonui.editor.SOHorizontalScrollView r6 = (com.artifex.sonui.editor.SOHorizontalScrollView) r6
            if (r6 != 0) goto L_0x0037
            return
        L_0x0037:
            boolean r0 = r6.mayAnimate()
            if (r0 != 0) goto L_0x003e
            return
        L_0x003e:
            android.content.Context r0 = r5.getContext()
            java.lang.String r2 = "general"
            java.lang.Object r0 = com.artifex.solib.SOPreferences.getPreferencesObject(r0, r2)
            java.lang.String r3 = "scroll_was_animated"
            if (r0 != 0) goto L_0x004d
            goto L_0x005c
        L_0x004d:
            java.lang.String r4 = "FALSE"
            java.lang.String r0 = com.artifex.solib.SOPreferences.getStringPreference(r0, r3, r4)
            if (r0 == 0) goto L_0x005b
            boolean r0 = r0.equals(r4)
            if (r0 == 0) goto L_0x005c
        L_0x005b:
            r1 = 1
        L_0x005c:
            if (r1 != 0) goto L_0x005f
            return
        L_0x005f:
            android.content.Context r0 = r5.getContext()
            java.lang.Object r0 = com.artifex.solib.SOPreferences.getPreferencesObject(r0, r2)
            if (r0 == 0) goto L_0x006e
            java.lang.String r1 = "TRUE"
            com.artifex.solib.SOPreferences.setStringPreference(r0, r3, r1)
        L_0x006e:
            r6.startAnimate()
            android.os.Handler r0 = new android.os.Handler
            r0.<init>()
            com.artifex.sonui.editor.NUIDocView$46 r1 = new com.artifex.sonui.editor.NUIDocView$46
            r1.<init>(r6)
            r0.post(r1)
        L_0x007e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.NUIDocView.onNewTabShown(java.lang.String):void");
    }

    public void onOpenInButton(View view) {
        preSave();
        if (this.mDataLeakHandlers != null) {
            try {
                this.mDataLeakHandlers.openInHandler(new File(this.mFileState.getOpenedPath()).getName(), this.mSession.getDoc());
            } catch (NullPointerException | UnsupportedOperationException unused) {
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void onOpenPDFInButton(View view) {
        preSave();
        if (this.mDataLeakHandlers != null) {
            try {
                this.mDataLeakHandlers.openPdfInHandler(new File(this.mFileState.getOpenedPath()).getName(), this.mSession.getDoc());
            } catch (NullPointerException | UnsupportedOperationException unused) {
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void onOrientationChange() {
        this.mDocView.onOrientationChange();
        if (usePagesView()) {
            this.mDocPageListView.onOrientationChange();
        }
        if (!isFullScreen()) {
            showUI(!this.keyboardShown);
        }
        onDeviceSizeChange();
    }

    public void onPDFExportAsButton(View view) {
        PdfExportAsPopup pdfExportAsPopup = new PdfExportAsPopup(getContext(), view, new PdfExportAsPopup.OnClickInterface() {
            public void onClick(String str) {
                if (NUIDocView.this.mDataLeakHandlers != null) {
                    try {
                        NUIDocView.this.mDataLeakHandlers.exportPdfAsHandler(new File(NUIDocView.this.mFileState.getOpenedPath()).getName(), str, NUIDocView.this.mSession.getDoc());
                    } catch (UnsupportedOperationException unused) {
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        });
        this.mPdfExportAsPopup = pdfExportAsPopup;
        pdfExportAsPopup.show();
    }

    public void onPageLoaded(int i) {
        boolean z = false;
        boolean z2 = this.mPageCount == 0;
        this.mPageCount = i;
        if (z2) {
            if (this.mSearchListener == null) {
                this.mSearchListener = new SOSearchListener() {
                    public void found(int i, RectF rectF) {
                        NUIDocView.access$2600(NUIDocView.this);
                        NUIDocView.this.getDocView().onFoundText(i, rectF);
                        NUIDocView nUIDocView = NUIDocView.this;
                        nUIDocView.setCurrentPage(nUIDocView, i);
                        NUIDocView.this.getDocView().waitForRest(new Runnable() {
                            public void run() {
                                NUIDocView.this.mIsSearching = false;
                            }
                        });
                    }

                    public void notFound() {
                        NUIDocView nUIDocView = NUIDocView.this;
                        nUIDocView.mIsSearching = false;
                        NUIDocView.access$2600(nUIDocView);
                        Utilities.yesNoMessage((Activity) NUIDocView.this.getContext(), NUIDocView.this.getResources().getString(R.string.sodk_editor_no_more_found), NUIDocView.this.getResources().getString(R.string.sodk_editor_keep_searching), NUIDocView.this.getResources().getString(R.string.sodk_editor_str_continue), NUIDocView.this.getResources().getString(R.string.sodk_editor_stop), new Runnable() {
                            public void run() {
                                new Handler().post(new Runnable() {
                                    public void run() {
                                        NUIDocView nUIDocView = NUIDocView.this;
                                        nUIDocView.mIsSearching = true;
                                        nUIDocView.access$2800(nUIDocView);
                                    }
                                });
                            }
                        }, new Runnable() {
                            public void run() {
                                NUIDocView.this.mIsSearching = false;
                            }
                        });
                    }
                };
                this.mSession.getDoc().setSearchListener(this.mSearchListener);
            }
            this.mSession.getDoc().setSearchMatchCase(false);
            updateUIAppearance();
            ToolbarButton toolbarButton = this.mReflowButton;
            if (toolbarButton != null) {
                toolbarButton.setEnabled(true);
            }
        }
        int count = this.mAdapter.getCount();
        int i2 = this.mPageCount;
        if (i2 != count) {
            z = true;
        }
        if (i2 < count) {
            this.mDocView.removeAllViewsInLayout();
            if (usePagesView()) {
                this.mDocPageListView.removeAllViewsInLayout();
            }
        }
        this.mAdapter.setCount(this.mPageCount);
        if (z) {
            final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    NUIDocView nUIDocView = NUIDocView.this;
                    if (!nUIDocView.mFinished) {
                        if (nUIDocView.mDocView.getReflowMode()) {
                            NUIDocView.this.onReflowScale();
                        } else {
                            NUIDocView.this.mDocView.scrollSelectionIntoView();
                        }
                    }
                }
            });
            layoutAfterPageLoad();
            DocumentListener documentListener = this.mDocumentListener;
            if (documentListener != null) {
                documentListener.onPageLoaded(this.mPageCount);
            }
        } else {
            requestLayouts();
        }
        handleStartPage();
        if (!this.mIsWaiting) {
            this.mIsWaiting = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    NUIDocView.this.setPageNumberText();
                    NUIDocView.this.mIsWaiting = false;
                }
            }, 1000);
        }
    }

    public void onPause(final Runnable runnable) {
        DocView docView;
        if (!this.pausing) {
            this.pausing = true;
            onPauseCommon();
            DocView docView2 = this.mDocView;
            if (docView2 != null && docView2.finished()) {
                runnable.run();
                this.pausing = false;
            } else if (this.mFileState == null || (docView = this.mDocView) == null || docView.getDoc() == null) {
                runnable.run();
                this.pausing = false;
            } else if (this.mDataLeakHandlers == null || !usePauseHandler()) {
                runnable.run();
                this.pausing = false;
            } else {
                ArDkDoc doc = this.mDocView.getDoc();
                this.mDataLeakHandlers.pauseHandler(doc, doc.getHasBeenModified(), new Runnable() {
                    public void run() {
                        runnable.run();
                        NUIDocView.this.pausing = false;
                    }
                });
            }
        }
    }

    public void onPauseCommon() {
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.resetDrawMode();
            this.mDocView.pauseChildren();
        }
        mCurrentNUIDocView = this;
        try {
            SOClipboardHandler clipboardHandler = ArDkLib.getClipboardHandler();
            if (clipboardHandler != null) {
                clipboardHandler.releaseClipboardHandler();
                saveState();
                this.mIsActivityActive = false;
                resetInputView();
                onShowKeyboardPreventPush(false);
                return;
            }
            throw new ClassNotFoundException();
        } catch (ClassNotFoundException unused) {
            Log.i("NUIDocView", "releaseClipboardHandler implementation unavailable");
        }
    }

    public void onPrintButton(View view) {
        Objects.requireNonNull(this.mDocCfgOptions);
        SODataLeakHandlers sODataLeakHandlers = this.mDataLeakHandlers;
        if (sODataLeakHandlers != null) {
            try {
                SODocSession sODocSession = this.mSession;
                if (sODocSession != null) {
                    sODataLeakHandlers.printHandler(sODocSession);
                }
            } catch (UnsupportedOperationException unused) {
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void onProtectButton(View view) {
    }

    public void onRedoButton(View view) {
        doRedo();
    }

    public void onReflowButton(View view) {
        this.mSession.addLoadListener(new SODocSession.SODocSessionLoadListener() {
            public void onCancel() {
            }

            public void onDocComplete() {
            }

            public void onError(int i, int i2) {
            }

            public void onLayoutCompleted() {
                NUIDocView.this.onLayoutChanged();
            }

            public void onPageLoad(int i) {
            }

            public void onSelectionChanged(int i, int i2) {
                NUIDocView.this.mSession.removeLoadListener(this);
                DocView docView = NUIDocView.this.mDocView;
                docView.scrollTo(docView.getScrollX(), 0);
                if (NUIDocView.this.usePagesView()) {
                    DocListPagesView docListPagesView = NUIDocView.this.mDocPageListView;
                    docListPagesView.scrollTo(docListPagesView.getScrollX(), 0);
                }
                NUIDocView nUIDocView = NUIDocView.this;
                nUIDocView.setCurrentPage(nUIDocView, 0);
                if (NUIDocView.this.mDocView.getReflowMode()) {
                    NUIDocView.this.mDocView.setReflowWidth();
                    NUIDocView.this.mDocView.onScaleEnd((ScaleGestureDetector) null);
                } else {
                    float f = 1.0f;
                    if (NUIDocView.this.usePagesView() && NUIDocView.this.isPageListVisible()) {
                        f = ((float) NUIDocView.this.getResources().getInteger(R.integer.sodk_editor_page_width_percentage)) / 100.0f;
                    }
                    NUIDocView.this.mDocView.setScale(f);
                    NUIDocView.this.mDocView.scaleChildren();
                }
                if (NUIDocView.this.usePagesView()) {
                    NUIDocView.this.mDocPageListView.fitToColumns();
                }
                NUIDocView.this.layoutNow();
            }
        });
        SODoc sODoc = (SODoc) getDoc();
        if (sODoc.mFlowMode == 1) {
            if (usePagesView()) {
                this.mDocPageListView.setReflowMode(true);
            }
            this.mDocView.setReflowMode(true);
            sODoc.mFlowMode = 2;
            sODoc.setFlowModeInternal(2, (float) getDocView().getReflowWidth(), (float) getDocView().getReflowHeight());
            this.mDocView.mLastReflowWidth = (float) getDocView().getReflowWidth();
            return;
        }
        this.mDocView.setReflowMode(false);
        if (usePagesView()) {
            this.mDocPageListView.setReflowMode(false);
        }
        sODoc.mFlowMode = 1;
        sODoc.setFlowModeInternal(1, (float) getDocView().getReflowWidth(), BitmapDescriptorFactory.HUE_RED);
    }

    public void onReflowScale() {
        this.mDocView.onReflowScale();
        if (usePagesView()) {
            this.mDocPageListView.onReflowScale();
        }
    }

    public void onResume() {
        if (this.pausing) {
            if (this.mWaitDialog == null) {
                this.mWaitDialog = Utilities.showWaitDialog(getContext(), getContext().getString(R.string.sodk_editor_please_wait), false);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    NUIDocView.this.onResume();
                }
            }, 50);
            return;
        }
        ProgressDialog progressDialog = this.mWaitDialog;
        if (progressDialog != null) {
            Utilities.hideWaitDialog(progressDialog);
            this.mWaitDialog = null;
        }
        onResumeCommon();
        this.keyboardHeight = 0;
        onShowKeyboard(false);
        SOFileState autoOpen = SOFileState.getAutoOpen(getContext());
        if (!(autoOpen == null || this.mFileState == null || autoOpen.getLastAccess() <= this.mFileState.getLastAccess())) {
            this.mFileState.setHasChanges(autoOpen.hasChanges());
        }
        SOFileState.clearAutoOpen(getContext());
        setInsertButtonsClickable(true);
        SODataLeakHandlers sODataLeakHandlers = this.mDataLeakHandlers;
        if (sODataLeakHandlers != null) {
            sODataLeakHandlers.doInsert();
        }
    }

    public void onResumeCommon() {
        DocListPagesView docListPagesView;
        mCurrentNUIDocView = this;
        initClipboardHandler();
        if (this.mDocUserPath != null) {
            createBitmaps();
            setBitmapsInViews();
        }
        if (this.mForceReloadAtResume) {
            this.mForceReloadAtResume = false;
            getDoc().setForceReloadAtResume(true);
            reloadFile();
        } else if (this.mForceReload) {
            this.mForceReload = false;
            getDoc().setForceReload(true);
            reloadFile();
        }
        this.mIsActivityActive = true;
        focusInputView();
        DocView docView = getDocView();
        if (docView != null) {
            docView.forceLayout();
        }
        if (usePagesView() && (docListPagesView = getDocListPagesView()) != null) {
            docListPagesView.forceLayout();
        }
    }

    public void onSaveAsButton(View view) {
        preSave();
        doSaveAs(false, (SOSaveAsComplete) null);
    }

    public void onSaveButton(View view) {
        preSave();
        doSave();
    }

    public void onSavePDFButton(View view) {
        if (this.mDataLeakHandlers != null) {
            try {
                this.mDataLeakHandlers.saveAsPdfHandler(new File(this.mFileState.getOpenedPath()).getName(), this.mSession.getDoc());
            } catch (UnsupportedOperationException unused) {
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void onSearch() {
        setTab(getContext().getString(R.string.sodk_editor_tab_hidden));
        if (Utilities.isPhoneDevice(getContext())) {
            hideMainTabs();
        }
        findViewById(R.id.searchTab).setVisibility(View.VISIBLE);
        showSearchSelected(true);
        this.mSearchText.getText().clear();
        this.mSearchText.requestFocus();
        Utilities.showKeyboard(getContext());
    }

    public void onSearchButton(View view) {
        onSearch();
    }

    public void onSearchNext(View view) {
        if (this.mSearchHandler == null) {
            this.mSearchHandler = new Handler();
        }
        this.mSearchHandler.post(() -> {
            ArDkDoc doc = NUIDocView.this.getDoc();
            if (doc != null) {
                NUIDocView nUIDocView = NUIDocView.this;
                if (!nUIDocView.mIsSearching) {
                    nUIDocView.mIsSearching = true;
                    if (nUIDocView.mSearchCounter > 1000) {
                        nUIDocView.mSearchCounter = 0;
                    }
                    nUIDocView.mSearchCounter++;
                    doc.setSearchBackwards(true);
                    nUIDocView.access$2800(NUIDocView.this);
                }
            }
        });
    }

    public void onSearchPrevious(View view) {
        if (this.mSearchHandler == null) {
            this.mSearchHandler = new Handler();
        }
        this.mSearchHandler.post(() -> {
            ArDkDoc doc = NUIDocView.this.getDoc();
            if (doc != null) {
                NUIDocView nUIDocView = NUIDocView.this;
                if (!nUIDocView.mIsSearching) {
                    nUIDocView.mIsSearching = true;
                    if (nUIDocView.mSearchCounter > 1000) {
                        nUIDocView.mSearchCounter = 0;
                    }
                    nUIDocView.mSearchCounter++;
                    doc.setSearchBackwards(true);
                    nUIDocView.access$2800(NUIDocView.this);
                }
            }
        });
    }

    public void onSelectionChanged() {
        SODocSession sODocSession = this.mSession;
        if (sODocSession != null && sODocSession.getDoc() != null && !this.mFinished) {
            this.mDocView.onSelectionChanged();
            if (usePagesView() && isPageListVisible()) {
                this.mDocPageListView.onSelectionChanged();
            }
            updateUIAppearance();
            reportViewChanges();
        }
    }

    public void onSelectionMonitor(int i, int i2) {
        onSelectionChanged();
    }

    public void onShareButton(View view) {
        preSave();
        if (this.mDataLeakHandlers != null) {
            try {
                this.mDataLeakHandlers.shareHandler(new File(this.mFileState.getOpenedPath()).getName(), this.mSession.getDoc());
            } catch (NullPointerException | UnsupportedOperationException unused) {
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void onShowKeyboard(final boolean z) {
        DocListPagesView docListPagesView;
        if (isActivityActive() && getPageCount() > 0) {
            this.keyboardShown = z;
            onShowKeyboardPreventPush(z);
            if (!isFullScreen()) {
                showUI(!z);
            }
            if (usePagesView() && (docListPagesView = getDocListPagesView()) != null) {
                docListPagesView.onShowKeyboard(z);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    DocView docView = NUIDocView.this.getDocView();
                    if (docView != null) {
                        docView.onShowKeyboard(z);
                    }
                    Runnable runnable = NUIDocView.this.mShowHideKeyboardRunnable;
                    if (runnable != null) {
                        runnable.run();
                        NUIDocView.this.mShowHideKeyboardRunnable = null;
                    }
                    NUIDocView.this.layoutNow();
                }
            }, 250);
        }
    }

    public void onShowKeyboardPreventPush(boolean z) {
        if ((activity().getWindow().getAttributes().flags & 1024) != 0) {
            View findViewById = ((Activity) getContext()).findViewById(NUIDocView.this.getId());
            if (z) {
                if (Build.VERSION.SDK_INT < 30) {
                    findViewById.setPadding(0, 0, 0, getKeyboardHeight());
                }
                findViewById(R.id.footer).setVisibility(View.GONE);
                return;
            }
            if (Build.VERSION.SDK_INT < 30) {
                findViewById.setPadding(0, 0, 0, 0);
            }
            findViewById(R.id.footer).setVisibility(View.VISIBLE);
        }
    }

    public void onTabChanged(String str) {
        getDocView().saveComment();
        if (str.equals(activity().getString(R.string.sodk_editor_tab_review)) && !((SODoc) getDocView().getDoc()).docSupportsReview()) {
            Utilities.showMessage(activity(), activity().getString(R.string.sodk_editor_not_supported), activity().getString(R.string.sodk_editor_cant_review_doc_body));
            setTab(this.mCurrentTab);
            if (this.mCurrentTab.equals(activity().getString(R.string.sodk_editor_tab_hidden))) {
                onSearchButton(this.mSearchButton);
            }
            onSelectionChanged();
        } else if (str.equals(activity().getString(R.string.sodk_editor_tab_draw)) && !((SODoc) getDocView().getDoc()).docSupportsDrawing()) {
            Utilities.showMessage(activity(), activity().getString(R.string.sodk_editor_not_supported), activity().getString(R.string.sodk_editor_cant_draw_doc_body));
            setTab(this.mCurrentTab);
            if (this.mCurrentTab.equals(activity().getString(R.string.sodk_editor_tab_hidden))) {
                onSearchButton(this.mSearchButton);
            }
            onSelectionChanged();
        } else if (str.equals(activity().getString(R.string.sodk_editor_tab_single))) {
            setTab(this.mCurrentTab);
            if (this.mListPopupWindow == null) {
                activity();
                ListPopupWindow listPopupWindow = new ListPopupWindow(activity());
                this.mListPopupWindow = listPopupWindow;
                listPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity(), R.drawable.sodk_editor_menu_popup));
                this.mListPopupWindow.setModal(true);
                this.mListPopupWindow.setAnchorView(getSingleTabView());
                final ArrayAdapter arrayAdapter = new ArrayAdapter(activity(), R.layout.sodk_editor_menu_popup_item);
                this.mListPopupWindow.setAdapter(arrayAdapter);
                TabData[] tabData = getTabData();
                int length = tabData.length;
                for (int i = 0; i < length; i++) {
                    if (tabData[i].visibility == 0) {
                        String str2 = tabData[i].name;
                        arrayAdapter.add(str2);
                        ((TextView) activity().getLayoutInflater().inflate(R.layout.sodk_editor_menu_popup_item, (ViewGroup) null)).setText(str2);
                    }
                }
                if (hasSearch()) {
                    Activity activity = activity();
                    int i2 = R.string.sodk_editor_tab_find;
                    arrayAdapter.add(activity.getString(i2));
                    ((TextView) activity().getLayoutInflater().inflate(R.layout.sodk_editor_menu_popup_item, (ViewGroup) null)).setText(activity().getString(i2));
                }
                this.mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        ListPopupWindow listPopupWindow = NUIDocView.this.mListPopupWindow;
                        if (listPopupWindow != null) {
                            listPopupWindow.dismiss();
                        }
                        NUIDocView nUIDocView = NUIDocView.this;
                        nUIDocView.mListPopupWindow = null;
                        Utilities.hideKeyboard(nUIDocView.getContext());
                        String str = (String) arrayAdapter.getItem(i);
                        if (str.equals(NUIDocView.this.activity().getString(R.string.sodk_editor_tab_find))) {
                            NUIDocView.this.onSearch();
                            NUIDocView.this.setSingleTabTitle(str);
                        } else if (str.equals(NUIDocView.this.activity().getString(R.string.sodk_editor_tab_review)) && !((SODoc) NUIDocView.this.getDocView().getDoc()).docSupportsReview()) {
                            Utilities.showMessage(NUIDocView.this.activity(), NUIDocView.this.activity().getString(R.string.sodk_editor_not_supported), NUIDocView.this.activity().getString(R.string.sodk_editor_cant_review_doc_body));
                        } else if (!str.equals(NUIDocView.this.activity().getString(R.string.sodk_editor_tab_draw)) || ((SODoc) NUIDocView.this.getDocView().getDoc()).docSupportsDrawing()) {
                            NUIDocView.this.changeTab(str);
                            NUIDocView.this.setSingleTabTitle(str);
                            NUIDocView.this.tabHost.setCurrentTabByTag(str);
                            NUIDocView.this.onNewTabShown(str);
                        } else {
                            Utilities.showMessage(NUIDocView.this.activity(), NUIDocView.this.activity().getString(R.string.sodk_editor_not_supported), NUIDocView.this.activity().getString(R.string.sodk_editor_cant_draw_doc_body));
                        }
                        NUIDocView.this.measureTabs();
                        NUIDocView nUIDocView2 = NUIDocView.this;
                        nUIDocView2.setOneTabColor(nUIDocView2.getSingleTabView(), true);
                    }
                });
                this.mListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    public void onDismiss() {
                        NUIDocView.this.mListPopupWindow = null;
                    }
                });
                ListPopupWindow listPopupWindow2 = this.mListPopupWindow;
                int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                int count = arrayAdapter.getCount();
                FrameLayout frameLayout = null;
                View view = null;
                int i3 = 0;
                int i4 = 0;
                for (int i5 = 0; i5 < count; i5++) {
                    int itemViewType = arrayAdapter.getItemViewType(i5);
                    if (itemViewType != i4) {
                        view = null;
                        i4 = itemViewType;
                    }
                    if (frameLayout == null) {
                        frameLayout = new FrameLayout(getContext());
                    }
                    view = arrayAdapter.getView(i5, view, frameLayout);
                    view.measure(makeMeasureSpec, makeMeasureSpec2);
                    int measuredWidth = view.getMeasuredWidth();
                    if (measuredWidth > i3) {
                        i3 = measuredWidth;
                    }
                }
                listPopupWindow2.setContentWidth(i3);
                showKeyboard(false, new Runnable() {
                    public void run() {
                        NUIDocView.this.mListPopupWindow.show();
                    }
                });
            }
        } else {
            changeTab(str);
            if (!Utilities.isPhoneDevice(getContext())) {
                onNewTabShown(str);
            }
        }
    }

    public void onTabChanging(String str, String str2) {
        if (str.equals(getContext().getString(R.string.sodk_editor_tab_draw))) {
            if (getDocView().getDrawMode()) {
                getDocView().onDrawMode();
            }
            updateUIAppearance();
        }
    }

    public void onTyping() {
        this.lastTypingTime = System.currentTimeMillis();
    }

    public void onUndoButton(View view) {
        doUndo();
    }

    public void openIn() {
        onOpenInButton((View) null);
    }

    public void persistAuthor(String str) {
        SOPreferences.setStringPreference(SOPreferences.getPreferencesObject(activity(), "general"), "DocAuthKey", str);
    }

    public void preSave() {
    }

    public void preSaveQuestion(Runnable runnable, Runnable runnable2) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public void prefinish() {
        DocListPagesView docListPagesView;
        if (!this.mFinished) {
            this.mFinished = true;
            SODocSession sODocSession = this.mSession;
            if (!(sODocSession == null || sODocSession.getDoc() == null)) {
                this.mSession.getDoc().setSearchListener((SOSearchListener) null);
            }
            this.mSearchListener = null;
            endProgress();
            if (this.mFileState != null) {
                saveState();
                this.mFileState.closeFile();
            }
            Utilities.hideKeyboard(getContext());
            DocView docView = this.mDocView;
            if (docView != null) {
                docView.finish();
                this.mDocView = null;
            }
            if (usePagesView() && (docListPagesView = this.mDocPageListView) != null) {
                docListPagesView.finish();
                this.mDocPageListView = null;
            }
            SODocSession sODocSession2 = this.mSession;
            if (sODocSession2 != null) {
                sODocSession2.abort();
            }
            final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.sodk_editor_alert_dialog_style);
            progressDialog.setMessage(getContext().getString(R.string.sodk_editor_wait));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.getWindow().clearFlags(2);
            progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                public void onShow(DialogInterface dialogInterface) {
                    new Thread(new Runnable() {
                        public void run() {
                            ArDkDoc doc = NUIDocView.this.getDoc();
                            if (doc != null) {
                                for (int i = 0; i < doc.mPages.size(); i++) {
                                    ArDkPage arDkPage = doc.mPages.get(i);
                                    if (arDkPage != null) {
                                        arDkPage.releasePage();
                                    }
                                }
                                doc.mPages.clear();
                            }
                            PageAdapter pageAdapter = NUIDocView.this.mAdapter;
                            if (pageAdapter != null) {
                                pageAdapter.setDoc((ArDkDoc) null);
                            }
                            NUIDocView nUIDocView = NUIDocView.this;
                            nUIDocView.mAdapter = null;
                            Boolean bool = nUIDocView.mEndSessionSilent;
                            if (bool != null) {
                                nUIDocView.endDocSession(bool.booleanValue());
                                NUIDocView.this.mEndSessionSilent = null;
                            }
                            SODocSession sODocSession = NUIDocView.this.mSession;
                            if (sODocSession != null) {
                                sODocSession.destroy();
                            }
                            NUIDocView.this.activity().runOnUiThread(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    NUIView.DocStateListener docStateListener = NUIDocView.this.mDocStateListener;
                                    if (docStateListener != null) {
                                        docStateListener.done();
                                    }
                                    ArDkLib arDkLib = NUIDocView.this.mDocumentLib;
                                    if (arDkLib != null) {
                                        arDkLib.reclaimMemory();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
            progressDialog.show();
            mGetContent = null;
        }
    }

    public void prepareToGoBack() {
        SODocSession sODocSession = this.mSession;
        if ((sODocSession == null || sODocSession.getDoc() != null) && this.mCompleted && getDocView() != null) {
            getDocView().resetModes();
        }
    }

    public void print() {
        if (this.mSession.isPasswordProtected()) {
            Utilities.showMessage((Activity) getContext(), getContext().getString(R.string.sodk_editor_printing_not_supported_title), getContext().getString(R.string.sodk_editor_print_password_protected_pdf));
            return;
        }
        String name = new File(getSession().getFileState().getOpenedPath()).getName();
        if (!FileUtils.getExtension(getSession().getUserPath()).equalsIgnoreCase("pdf")) {
            name = name + ".pdf";
        }
        PrintHelperPdf printHelperPdf = new PrintHelperPdf();
        printHelperPdf.setPrintName(name);
        printHelperPdf.print(getContext(), getDoc(), (Runnable) null);
    }

    public void providePassword(String str) {
        ArDkDoc doc;
        SODocSession sODocSession = this.mSession;
        if (sODocSession != null && (doc = sODocSession.getDoc()) != null) {
            doc.providePassword(str);
        }
    }

    public final void recycleBitmaps() {
        DocListPagesView docListPagesView;
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.releaseBitmaps();
        }
        if (usePagesView() && (docListPagesView = this.mDocPageListView) != null) {
            docListPagesView.releaseBitmaps();
        }
        int i = 0;
        while (true) {
            ArDkBitmap[] arDkBitmapArr = this.bitmaps;
            if (i < arDkBitmapArr.length) {
                if (arDkBitmapArr[i] != null) {
                    arDkBitmapArr[i].getBitmap().recycle();
                    this.bitmaps[i] = null;
                }
                i++;
            } else {
                return;
            }
        }
    }

    public void redactApply() {
    }

    public boolean redactGetMarkTextMode() {
        return false;
    }

    public boolean redactIsMarkingArea() {
        return false;
    }

    public void redactMarkArea() {
    }

    public void redactMarkText() {
    }

    public void redactRemove() {
    }

    public void releaseBitmaps() {
        setValid(false);
        recycleBitmaps();
        setBitmapsInViews();
    }

    public void reloadFile() {
    }

    public void reloadFile(String str) {
    }

    public void reportViewChanges() {
        DocView docView;
        RectF box;
        if (this.mDocumentListener != null && (docView = this.mDocView) != null) {
            float scale = docView.getScale();
            int scrollX = this.mDocView.getScrollX();
            int scrollY = this.mDocView.getScrollY();
            Rect rect = null;
            ArDkSelectionLimits selectionLimits = this.mDocView.getSelectionLimits();
            DocPageView selectionStartPage = this.mDocView.getSelectionStartPage();
            if (!(selectionLimits == null || selectionStartPage == null || (box = selectionLimits.getBox()) == null)) {
                rect = selectionStartPage.pageToView(box);
                rect.offset((int) selectionStartPage.getX(), (int) selectionStartPage.getY());
            }
            boolean z = false;
            boolean z2 = true;
            if (scale != this.scalePrev) {
                z = true;
            }
            if (scrollX != this.scrollXPrev) {
                z = true;
            }
            if (scrollY != this.scrollYPrev) {
                z = true;
            }
            if (Utilities.compareRects(this.selectionPrev, rect)) {
                z2 = z;
            }
            if (z2) {
                this.scalePrev = scale;
                this.scrollXPrev = scrollX;
                this.scrollYPrev = scrollY;
                this.selectionPrev = rect;
                this.mDocumentListener.onViewChanged(scale, scrollX, scrollY, rect);
            }
        }
    }

    public final void requestLayouts() {
        this.mDocView.requestLayout();
        if (usePagesView() && isPageListVisible()) {
            this.mDocPageListView.requestLayout();
        }
    }

    public void resetInputView() {
        InputView inputView = this.mInputView;
        if (inputView != null) {
            inputView.resetEditable();
        }
    }

    public void save() {
        preSave();
        doSave();
    }

    public void saveAs() {
        preSave();
        doSaveAs(false, (SOSaveAsComplete) null);
    }

    public void savePDF() {
        onSavePDFButton((View) null);
    }

    public final void saveState() {
        SOFileState fileState;
        DocView docView = getDocView();
        if (docView != null) {
            int i = this.mCurrentPageNum;
            float scale = docView.getScale();
            int scrollX = docView.getScrollX();
            int scrollY = docView.getScrollY();
            boolean z = usePagesView() && docView.pagesShowing();
            SODocSession sODocSession = this.mSession;
            if (!(sODocSession == null || (fileState = sODocSession.getFileState()) == null)) {
                fileState.setPageNumber(i);
                fileState.setScale(scale);
                fileState.setScrollX(scrollX);
                fileState.setScrollY(scrollY);
                fileState.setPageListVisible(z);
            }
            ViewingState viewingState = this.mViewingState;
            if (viewingState != null) {
                viewingState.pageNumber = i;
                viewingState.scale = scale;
                viewingState.scrollX = scrollX;
                viewingState.scrollY = scrollY;
                viewingState.pageListVisible = z;
            }
        }
    }

    public void saveTo(final String str, final SODocSaveListener sODocSaveListener) {
        preSave();
        preSaveQuestion(new Runnable() {
            public void run() {
                NUIDocView.this.getDoc().saveTo(str, new SODocSaveListener() {
                    public void onComplete(int i, int i2) {
                        if (i == 0) {
                            NUIDocView.this.setFooterText(str);
                            NUIDocView.this.mFileState.setUserPath(str);
                            NUIDocView.this.mFileState.setHasChanges(false);
                            NUIDocView.this.onSelectionChanged();
                            NUIDocView.this.reloadFile();
                            NUIDocView nUIDocView = NUIDocView.this;
                            nUIDocView.mIsTemplate = nUIDocView.mFileState.isTemplate();
                        } else if (i == 1) {
                            NUIDocView.this.mFileState.setUserPath((String) null);
                        }
                        sODocSaveListener.onComplete(i, i2);
                    }
                });
            }
        }, () -> {
        });
    }

    public void saveToPDF(String str, SODocSaveListener sODocSaveListener) {
        this.mSession.getDoc().saveToPDF(str, false, sODocSaveListener);
    }

    public final void scaleChild(View view, int i, float f) {
        View findViewById = view.findViewById(i);
        findViewById.setScaleX(f);
        findViewById.setScaleY(f);
    }

    public void scaleHeader() {
        scaleToolbar(R.id.annotate_toolbar, 0.65f);
        scaleToolbar(R.id.doc_pages_toolbar, 0.65f);
        scaleToolbar(R.id.edit_toolbar, 0.65f);
        scaleToolbar(R.id.excel_edit_toolbar, 0.65f);
        scaleToolbar(R.id.file_toolbar, 0.65f);
        scaleToolbar(R.id.format_toolbar, 0.65f);
        scaleToolbar(R.id.formulas_toolbar, 0.65f);
        scaleToolbar(R.id.insert_toolbar, 0.65f);
        scaleToolbar(R.id.pdf_pages_toolbar, 0.65f);
        scaleToolbar(R.id.ppt_format_toolbar, 0.65f);
        scaleToolbar(R.id.ppt_insert_toolbar, 0.65f);
        scaleToolbar(R.id.ppt_slides_toolbar, 0.65f);
        scaleToolbar(R.id.review_toolbar, 0.65f);
        scaleToolbar(R.id.redact_toolbar, 0.65f);
        scaleToolbar(R.id.pdf_sign_toolbar, 0.65f);
        scaleSearchToolbar(0.65f);
        scaleTabArea(0.65f);
        ((TextView) this.tabHost.getTabWidget().getChildTabViewAt(this.tabHost.getTabWidget().getTabCount() - 1).findViewById(R.id.tabText)).setTextSize((float) getContext().getResources().getInteger(R.integer.sodk_editor_single_tab_text_size));
        this.mBackButton.setScaleX(0.65f);
        this.mBackButton.setScaleY(0.65f);
        this.mUndoButton.setScaleX(0.65f);
        this.mUndoButton.setScaleY(0.65f);
        this.mRedoButton.setScaleX(0.65f);
        this.mRedoButton.setScaleY(0.65f);
        this.mSearchButton.setScaleX(0.65f);
        this.mSearchButton.setScaleY(0.65f);
    }

    public void scaleSearchToolbar(float f) {
        Drawable drawable;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search_toolbar);
        if (linearLayout != null) {
            scaleChild(linearLayout, R.id.search_icon, f);
            scaleChild(linearLayout, R.id.search_text_clear, f);
            scaleChild(linearLayout, R.id.search_next, f);
            scaleChild(linearLayout, R.id.search_previous, f);
            LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(R.id.search_input_wrapper);
            if (Utilities.isPhoneDevice(getContext())) {
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.sodk_editor_search_input_wrapper_phone);
            } else {
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.sodk_editor_search_input_wrapper);
            }
            linearLayout2.setBackground(drawable);
            this.mSearchText.setTextSize(2, f * 20.0f);
            linearLayout2.measure(0, 0);
            linearLayout2.getLayoutParams().height = (int) (((float) linearLayout2.getMeasuredHeight()) * 0.85f);
            linearLayout.setPadding(0, -15, 0, -15);
        }
    }

    public void scaleTabArea(float f) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header_top);
        if (linearLayout != null) {
            linearLayout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            linearLayout.getLayoutParams().height = (int) ((linearLayout.getMeasuredHeight()) * f);
            linearLayout.requestLayout();
            linearLayout.invalidate();
        }
    }

    public void scaleToolbar(int i, float f) {
        LinearLayout linearLayout = (LinearLayout) findViewById(i);
        if (linearLayout != null) {
            linearLayout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int measuredHeight = linearLayout.getMeasuredHeight();
            int measuredWidth = linearLayout.getMeasuredWidth();
            linearLayout.setScaleX(f);
            linearLayout.setScaleY(f);
            linearLayout.setPivotX(BitmapDescriptorFactory.HUE_RED);
            linearLayout.setPivotY(BitmapDescriptorFactory.HUE_RED);
            float f2 = (measuredHeight) * f;
            linearLayout.getLayoutParams().height = (int) (2.0f * f2);
            int i2 = (int) ((measuredWidth) * f);
            linearLayout.getLayoutParams().width = i2;
            linearLayout.requestLayout();
            linearLayout.invalidate();
            int convertDpToPixel = Utilities.convertDpToPixel(3.0f);
            int i3 = (int) f2;
            ViewParent parent = linearLayout.getParent();
            if (parent instanceof HorizontalScrollView) {
                HorizontalScrollView horizontalScrollView = (HorizontalScrollView) parent;
                horizontalScrollView.getLayoutParams().height = convertDpToPixel + i3;
                horizontalScrollView.getLayoutParams().width = i2;
                horizontalScrollView.requestLayout();
                horizontalScrollView.invalidate();
            }
            int childCount = linearLayout.getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = linearLayout.getChildAt(i4);
                if (childAt instanceof ToolbarButton) {
                    childAt.setPadding((int) ((childAt.getPaddingLeft()) * f), 0, (int) ((childAt.getPaddingRight()) * f), 0);
                }
                String str = (String) childAt.getTag();
                if (str != null && str.equals("toolbar_divider")) {
                    childAt.getLayoutParams().height = i3;
                    ((LinearLayout.LayoutParams) childAt.getLayoutParams()).gravity = 48;
                    int convertDpToPixel2 = Utilities.convertDpToPixel(7.0f);
                    int convertDpToPixel3 = Utilities.convertDpToPixel(3.0f);
                    ((LinearLayout.LayoutParams) childAt.getLayoutParams()).setMargins(convertDpToPixel3, convertDpToPixel2, convertDpToPixel3, 0);
                }
            }
        }
    }

    public Point screenToPage(int i, Point point) {
        DocView docView = getDocView();
        if (docView == null) {
            return null;
        }
        return docView.screenToPage(i, point);
    }

    public void searchBackward(String str) {
        searchCommon(str, true);
    }

    public final void searchCommon(String str, boolean z) {
        ArDkDoc doc = getDoc();
        if (doc != null && !doc.isSearchRunning()) {
            doc.setSearchBackwards(z);
            Utilities.hideKeyboard(getContext());
            if (!str.equals(this.lastSearchString)) {
                setSearchStart();
                doc.setSearchString(str);
            }
            doc.search();
        }
    }

    public void searchForward(String str) {
        searchCommon(str, false);
    }

    public void secureSave() {
    }

    public boolean select(Point point) {
        DocView docView = this.mDocView;
        if (docView != null) {
            return docView.select(point, (Point) null);
        }
        return false;
    }

    public void selectionupdated() {
        onSelectionChanged();
    }

    public void setAuthor(String str) {
        ArDkDoc doc = getDocView().getDoc();
        if (doc != null) {
            doc.setAuthor(str);
            persistAuthor(str);
        }
    }

    public final void setBitmapsInViews() {
        DocListPagesView docListPagesView;
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.setBitmaps(this.bitmaps);
        }
        if (usePagesView() && (docListPagesView = this.mDocPageListView) != null) {
            docListPagesView.setBitmaps(this.bitmaps);
        }
    }

    public void setButtonColor(Button button, int i) {
        Drawable[] compoundDrawables = button.getCompoundDrawables();
        for (Drawable compoundDrawable : compoundDrawables) {
            if (compoundDrawable != null) {
                DrawableCompat.setTint(compoundDrawable, i);
            }
        }
    }

    public final void setButtonEnabled(Button button, boolean z) {
        button.setEnabled(z);
        int color = ContextCompat.getColor(activity(), R.color.sodk_editor_header_button_enabled_tint);
        if (!z) {
            color = ContextCompat.getColor(activity(), R.color.sodk_editor_header_button_disabled_tint);
        }
        setButtonColor(button, color);
    }

    public void setConfigurableButtons() {
        ArrayList arrayList = new ArrayList();
        if (this.mSaveButton != null) {
            if (this.mDocCfgOptions.mSettingsBundle.getBoolean("SaveEnabledKey", true)) {
                this.mSaveButton.setVisibility(View.VISIBLE);
                arrayList.add(this.mSaveButton);
            } else {
                this.mSaveButton.setVisibility(View.GONE);
            }
        }
        if (this.mCustomSaveButton != null) {
            if (this.mDocCfgOptions.mSettingsBundle.getBoolean("CustomSaveEnabledKey", false)) {
                this.mCustomSaveButton.setVisibility(View.VISIBLE);
                arrayList.add(this.mCustomSaveButton);
            } else {
                this.mCustomSaveButton.setVisibility(View.GONE);
            }
        }
        if (this.mSaveAsButton != null) {
            if (this.mDocCfgOptions.isSaveAsEnabled()) {
                this.mSaveAsButton.setVisibility(View.VISIBLE);
                arrayList.add(this.mSaveAsButton);
            } else {
                this.mSaveAsButton.setVisibility(View.GONE);
            }
        }
        if (shouldConfigureSaveAsPDFButton() && this.mSavePdfButton != null) {
            if (this.mDocCfgOptions.isSaveAsPdfEnabled()) {
                this.mSavePdfButton.setVisibility(View.VISIBLE);
                arrayList.add(this.mSavePdfButton);
            } else {
                this.mSavePdfButton.setVisibility(View.GONE);
            }
        }
        if (shouldConfigureExportPdfAsButton() && this.mExportPdfAsButton != null) {
            if (this.mDocCfgOptions.mSettingsBundle.getBoolean("PdfExportAsEnabledKey", true)) {
                this.mExportPdfAsButton.setVisibility(View.VISIBLE);
                arrayList.add(this.mExportPdfAsButton);
            } else {
                this.mExportPdfAsButton.setVisibility(View.GONE);
            }
        }
        if (this.mShareButton != null) {
            if (this.mDocCfgOptions.isShareEnabled()) {
                this.mShareButton.setVisibility(View.VISIBLE);
                arrayList.add(this.mShareButton);
            } else {
                this.mShareButton.setVisibility(View.GONE);
            }
        }
        if (this.mOpenInButton != null) {
            if (this.mDocCfgOptions.isOpenInEnabled()) {
                this.mOpenInButton.setVisibility(View.VISIBLE);
                arrayList.add(this.mOpenInButton);
            } else {
                this.mOpenInButton.setVisibility(View.GONE);
            }
        }
        if (this.mOpenPdfInButton != null) {
            if (this.mDocCfgOptions.isOpenPdfInEnabled()) {
                this.mOpenPdfInButton.setVisibility(View.VISIBLE);
                arrayList.add(this.mOpenPdfInButton);
            } else {
                this.mOpenPdfInButton.setVisibility(View.GONE);
            }
        }
        if (this.mPrintButton != null) {
            if (this.mDocCfgOptions.isPrintingEnabled() || this.mDocCfgOptions.isSecurePrintingEnabled()) {
                this.mPrintButton.setVisibility(View.VISIBLE);
                arrayList.add(this.mPrintButton);
            } else {
                this.mPrintButton.setVisibility(View.GONE);
            }
        }
        ToolbarButton toolbarButton = this.mProtectButton;
        if (toolbarButton != null) {
            arrayList.add(toolbarButton);
            this.mProtectButton.setVisibility(View.GONE);
        }
        if (this.mCopyButton2 != null) {
            if (!this.mDocCfgOptions.isEditingEnabled() || getDocFileExtension().equalsIgnoreCase("pdf")) {
                this.mCopyButton2.setVisibility(View.VISIBLE);
                arrayList.add(this.mCopyButton2);
            } else {
                this.mCopyButton2.setVisibility(View.GONE);
            }
        }
        ToolbarButton.setAllSameSize((ToolbarButton[]) arrayList.toArray(new ToolbarButton[arrayList.size()]));
        if (!this.mDocCfgOptions.isEditingEnabled()) {
            Button button = this.mUndoButton;
            if (button != null) {
                button.setVisibility(View.GONE);
            }
            Button button2 = this.mRedoButton;
            if (button2 != null) {
                button2.setVisibility(View.GONE);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        if (this.mInsertImageButton != null) {
            if (!this.mDocCfgOptions.isImageInsertEnabled() || !this.mDocCfgOptions.isEditingEnabled()) {
                this.mInsertImageButton.setVisibility(View.GONE);
            } else {
                this.mInsertImageButton.setVisibility(View.VISIBLE);
                arrayList2.add(this.mInsertImageButton);
            }
        }
        if (this.mInsertPhotoButton != null) {
            if (!this.mDocCfgOptions.isPhotoInsertEnabled() || !this.mDocCfgOptions.isEditingEnabled()) {
                this.mInsertPhotoButton.setVisibility(View.GONE);
            } else {
                this.mInsertPhotoButton.setVisibility(View.VISIBLE);
                arrayList2.add(this.mInsertPhotoButton);
            }
        }
        if (arrayList2.size() > 0) {
            ToolbarButton.setAllSameSize((ToolbarButton[]) arrayList2.toArray(new ToolbarButton[arrayList2.size()]));
        }
        setInsertTabVisibility();
    }

    public void setCurrentPage(View view, int i) {
        if (view instanceof DocListPagesView) {
            this.mDocView.scrollToPage(i, false);
        } else if (view instanceof DocView) {
            if (usePagesView()) {
                this.mDocPageListView.setCurrentPage(i);
                this.mDocPageListView.scrollToPage(i, false);
            }
        } else if (view instanceof NUIDocView) {
            this.mDocView.scrollToPage(i, true);
            if (usePagesView()) {
                this.mDocPageListView.setCurrentPage(i);
                this.mDocPageListView.scrollToPage(i, true);
            }
        }
        this.mCurrentPageNum = i;
        setPageNumberText();
        this.mSession.getFileState().setPageNumber(this.mCurrentPageNum);
    }

    public final void setDataLeakHandlers() {
        try {
            SODataLeakHandlers sODataLeakHandlers = this.mDataLeakHandlers;
            if (sODataLeakHandlers != null) {
                sODataLeakHandlers.initDataLeakHandlers(activity(), this.mDocCfgOptions);
                return;
            }
            throw new ClassNotFoundException();
        } catch (ExceptionInInitializerError unused) {
            Log.e("NUIDocView", String.format("setDataLeakHandlers() experienced unexpected exception [%s]", new Object[]{"ExceptionInInitializerError"}));
        } catch (LinkageError unused2) {
            Log.e("NUIDocView", String.format("setDataLeakHandlers() experienced unexpected exception [%s]", new Object[]{"LinkageError"}));
        } catch (SecurityException unused3) {
            Log.e("NUIDocView", String.format("setDataLeakHandlers() experienced unexpected exception [%s]", new Object[]{"SecurityException"}));
        } catch (ClassNotFoundException unused4) {
            Log.i("NUIDocView", "DataLeakHandlers implementation unavailable");
        } catch (IOException unused5) {
            Log.i("NUIDocView", "DataLeakHandlers IOException");
        }
    }

    public void setDigitalSignatureModeOff() {
    }

    public void setDigitalSignatureModeOn() {
    }

    public void setDocSpecifics(ConfigOptions configOptions, SODataLeakHandlers sODataLeakHandlers) {
        this.mDocCfgOptions = configOptions;
        this.mDataLeakHandlers = sODataLeakHandlers;
    }

    public void setDocumentListener(DocumentListener documentListener) {
        this.mDocumentListener = documentListener;
    }

    public void setDrawModeOff() {
        this.mDocView.setDrawModeOff();
    }

    public void setDrawModeOn() {
        this.mDocView.setDrawModeOn();
    }

    public void setESignatureModeOff() {
    }

    public void setESignatureModeOn(View view) {
    }

    public void setFlowMode(int i) {
        SODoc sODoc = (SODoc) getDoc();
        if (i != sODoc.mFlowMode) {
            this.mSession.addLoadListener(new SODocSession.SODocSessionLoadListener() {
                public void onCancel() {
                }

                public void onDocComplete() {
                }

                public void onError(int i, int i2) {
                }

                public void onLayoutCompleted() {
                    NUIDocView.this.onLayoutChanged();
                }

                public void onPageLoad(int i) {
                }

                public void onSelectionChanged(int i, int i2) {
                    NUIDocView.this.mSession.removeLoadListener(this);
                    DocView docView = NUIDocView.this.mDocView;
                    docView.scrollTo(docView.getScrollX(), 0);
                    if (NUIDocView.this.usePagesView()) {
                        DocListPagesView docListPagesView = NUIDocView.this.mDocPageListView;
                        docListPagesView.scrollTo(docListPagesView.getScrollX(), 0);
                    }
                    NUIDocView nUIDocView = NUIDocView.this;
                    nUIDocView.setCurrentPage(nUIDocView, 0);
                    if (NUIDocView.this.mDocView.getReflowMode()) {
                        NUIDocView.this.mDocView.setReflowWidth();
                        NUIDocView.this.mDocView.onScaleEnd((ScaleGestureDetector) null);
                    } else {
                        float f = 1.0f;
                        if (NUIDocView.this.usePagesView() && NUIDocView.this.isPageListVisible()) {
                            f = ((float) NUIDocView.this.getResources().getInteger(R.integer.sodk_editor_page_width_percentage)) / 100.0f;
                        }
                        NUIDocView.this.mDocView.setScale(f);
                        NUIDocView.this.mDocView.scaleChildren();
                    }
                    if (NUIDocView.this.usePagesView()) {
                        NUIDocView.this.mDocPageListView.fitToColumns();
                    }
                    NUIDocView.this.layoutNow();
                }
            });
            if (i == 1) {
                this.mDocView.setReflowMode(false);
                if (usePagesView()) {
                    this.mDocPageListView.setReflowMode(false);
                }
                sODoc.mFlowMode = 1;
                sODoc.setFlowModeInternal(1, (float) getDocView().getReflowWidth(), BitmapDescriptorFactory.HUE_RED);
            }
            if (i == 2) {
                if (usePagesView()) {
                    this.mDocPageListView.setReflowMode(true);
                }
                this.mDocView.setReflowMode(true);
                sODoc.mFlowMode = 2;
                sODoc.setFlowModeInternal(2, (float) getDocView().getReflowWidth(), (float) getDocView().getReflowHeight());
                this.mDocView.mLastReflowWidth = (float) getDocView().getReflowWidth();
            }
            if (i == 3) {
                if (usePagesView()) {
                    this.mDocPageListView.setReflowMode(true);
                }
                this.mDocView.setReflowMode(true);
                sODoc.mFlowMode = 3;
                sODoc.setFlowModeInternal(3, (float) getDocView().getReflowWidth(), (float) getDocView().getReflowHeight());
                this.mDocView.mLastReflowWidth = (float) getDocView().getReflowWidth();
            }
        }
    }

    public void setInsertButtonsClickable(boolean z) {
        ToolbarButton toolbarButton = this.mInsertImageButton;
        if (toolbarButton != null) {
            toolbarButton.setClickable(z);
        }
        ToolbarButton toolbarButton2 = this.mInsertPhotoButton;
        if (toolbarButton2 != null) {
            toolbarButton2.setClickable(z);
        }
    }

    public void setInsertTabVisibility() {
    }

    public void setLineColor(int i) {
        getDocView().setInkLineColor(i);
    }

    public void setLineThickness(float f) {
        getDocView().setInkLineThickness(f);
    }

    public void setNoteModeOff() {
    }

    public void setNoteModeOn() {
    }

    public void setOnUpdateUI(Runnable runnable) {
        this.mOnUpdateUIRunnable = runnable;
    }

    public void setOneTabColor(View view, boolean z) {
        GradientDrawable gradientDrawable = (GradientDrawable) ((LayerDrawable) view.getBackground()).findDrawableByLayerId(R.id.tab_bg_color);
        TextView textView = (TextView) view.findViewById(R.id.tabText);
        int i = 0;
        if (z) {
            gradientDrawable.setColor(getTabSelectedColor());
            textView.setTextColor(getTabSelectedTextColor());
            Drawable[] compoundDrawables = textView.getCompoundDrawables();
            int length = compoundDrawables.length;
            while (i < length) {
                Drawable drawable = compoundDrawables[i];
                if (drawable != null) {
                    drawable.setColorFilter(getTabSelectedTextColor(), PorterDuff.Mode.SRC_IN);
                }
                i++;
            }
            return;
        }
        gradientDrawable.setColor(getTabUnselectedColor());
        textView.setTextColor(getTabUnselectedTextColor());
        Drawable[] compoundDrawables2 = textView.getCompoundDrawables();
        int length2 = compoundDrawables2.length;
        while (i < length2) {
            Drawable drawable2 = compoundDrawables2[i];
            if (drawable2 != null) {
                drawable2.setColorFilter(getTabUnselectedTextColor(), PorterDuff.Mode.SRC_IN);
            }
            i++;
        }
    }

    public void setPageChangeListener(ChangePageListener changePageListener) {
        this.mChangePageListener = changePageListener;
    }

    public void setPageCount(int i) {
        this.mPageCount = i;
        this.mAdapter.setCount(i);
        requestLayouts();
    }

    public void setPageNumberText() {
        new Handler().post(new Runnable() {
            public void run() {
                NUIDocView nUIDocView = NUIDocView.this;
                nUIDocView.mFooterText.setText((CharSequence) nUIDocView.getPageNumberText());
                NUIDocView.this.mFooterText.measure(0, 0);
                NUIDocView.this.mFooterLead.getLayoutParams().width = NUIDocView.this.mFooterText.getMeasuredWidth();
                NUIDocView.this.mFooterLead.getLayoutParams().height = NUIDocView.this.mFooterText.getMeasuredHeight();
                NUIDocView.this.doUpdateCustomUI();
                NUIDocView nUIDocView2 = NUIDocView.this;
                ChangePageListener changePageListener = nUIDocView2.mChangePageListener;
                if (changePageListener != null) {
                    changePageListener.onPage(nUIDocView2.mCurrentPageNum);
                }
            }
        });
    }

    public void setScaleAndScroll(float f, int i, int i2) {
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.setScaleAndScroll(f, i, i2);
        }
    }

    public void setSearchStart() {
    }

    public void setSelectionText(String str) {
        ((SODoc) getDoc()).setSelectionText(str);
    }

    public void setSigningHandler(SigningHandler signingHandler) {
        this.mSigningHandler = signingHandler;
    }

    public void setSingleTabTitle(String str) {
        if (!str.equalsIgnoreCase(getContext().getString(R.string.sodk_editor_tab_hidden))) {
            ((TextView) this.tabHost.getTabWidget().getChildTabViewAt(this.tabHost.getTabWidget().getTabCount() - 1).findViewById(R.id.tabText)).setText(str);
        }
    }

    public void setStartPage(int i) {
        this.mStartPage = i;
    }

    public void setTab(String str) {
        this.mCurrentTab = str;
        ((TabHost) findViewById(R.id.tabhost)).setCurrentTabByTag(this.mCurrentTab);
        setSingleTabTitle(str);
        if (Utilities.isPhoneDevice(activity())) {
            scaleHeader();
        }
    }

    public void setTabColors(String str) {
        for (Map.Entry next : this.tabMap.entrySet()) {
            setOneTabColor((View) next.getValue(), str.equals((String) next.getKey()));
        }
        setOneTabColor(getSingleTabView(), true);
    }

    public void setViewAndChildrenEnabled(View view, boolean z) {
        view.setEnabled(z);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt != this.mBackButton) {
                    setViewAndChildrenEnabled(childAt, z);
                }
            }
        }
    }

    public void setupTab(TabHost tabHost2, String str, int i, int i2, int i3) {
        if (Utilities.isPhoneDevice(activity()) || i3 != 8) {
            View inflate = LayoutInflater.from(tabHost2.getContext()).inflate(i2, (ViewGroup) null);
            ((TextView) inflate.findViewById(R.id.tabText)).setText(str);
            this.tabMap.put(str, inflate);
            TabHost.TabSpec newTabSpec = tabHost2.newTabSpec(str);
            newTabSpec.setIndicator(inflate);
            newTabSpec.setContent(i);
            tabHost2.addTab(newTabSpec);
            this.mAllTabHostTabs.add(str);
            return;
        }
        findViewById(i).setVisibility(View.GONE);
    }

    public void setupTabs() {
        TabHost tabHost2 = (TabHost) findViewById(R.id.tabhost);
        this.tabHost = tabHost2;
        tabHost2.setup();
        final TabData[] tabData = getTabData();
        setupTab(this.tabHost, getContext().getString(R.string.sodk_editor_tab_hidden), R.id.hiddenTab, R.layout.sodk_editor_tab, 0);
        this.tabHost.getTabWidget().getChildTabViewAt(0).setVisibility(View.GONE);
        for (TabData tabData2 : tabData) {
            setupTab(this.tabHost, tabData2.name, tabData2.contentId, tabData2.layoutId, tabData2.visibility);
        }
        setupTab(this.tabHost, getContext().getString(R.string.sodk_editor_tab_single), R.id.hiddenTab, R.layout.sodk_editor_tab_single, 0);
        final int initialTab = getInitialTab();
        setTab(tabData[initialTab].name);
        final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this);
                NUIDocView.this.setTabColors(tabData[initialTab].name);
            }
        });
        setSingleTabTitle(tabData[initialTab].name);
        measureTabs();
        this.tabHost.setOnTabChangedListener(this);
        TabWidget tabWidget = this.tabHost.getTabWidget();
        int tabCount = tabWidget.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            tabWidget.getChildAt(i).setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() != 1) {
                        return false;
                    }
                    NUIDocView nUIDocView = NUIDocView.this;
                    int i = NUIDocView.OVERSIZE_PERCENT;
                    Utilities.hideKeyboard(nUIDocView.getContext());
                    return false;
                }
            });
        }
    }

    public void share() {
        onShareButton((View) null);
    }

    public boolean shouldConfigureExportPdfAsButton() {
        return false;
    }

    public boolean shouldConfigureSaveAsPDFButton() {
        return true;
    }

    public boolean showKeyboard() {
        Utilities.showKeyboard(getContext());
        return true;
    }

    public void showPages() {
        showPages(-1);
    }

    public void showPagesTab(String str, int i) {
        this.tabHost.setOnTabChangedListener((TabHost.OnTabChangeListener) null);
        this.tabHost.setCurrentTabByTag(str);
        setTabColors(str);
        setSingleTabTitle(getContext().getString(i));
        this.mCurrentTab = str;
        measureTabs();
        this.tabHost.setOnTabChangedListener(this);
    }

    public void showSearchSelected(boolean z) {
        Button button = this.mSearchButton;
        if (button != null) {
            button.setSelected(z);
            if (z) {
                setButtonColor(this.mSearchButton, ContextCompat.getColor(activity(), R.color.sodk_editor_button_tint));
            } else {
                setButtonColor(this.mSearchButton, ContextCompat.getColor(activity(), R.color.sodk_editor_header_button_enabled_tint));
            }
        }
    }

    public void showUI(final boolean z) {
        if (z) {
            Runnable runnable = this.mExitFullScreenRunnable;
            if (runnable != null) {
                runnable.run();
            }
            this.mFullscreen = false;
        }
        if (this.mShowUI) {
            View findViewById = findViewById(R.id.tabhost);
            View findViewById2 = findViewById(R.id.header);
            if (isLandscapePhone()) {
                if (!z && findViewById.getVisibility() != View.GONE && !isSearchVisible()) {
                    findViewById.setVisibility(View.GONE);
                    findViewById2.setVisibility(View.GONE);
                    layoutNow();
                } else if (z && findViewById.getVisibility() != View.VISIBLE) {
                    findViewById.setVisibility(View.VISIBLE);
                    findViewById2.setVisibility(View.VISIBLE);
                    layoutNow();
                }
            } else if (findViewById.getVisibility() != View.VISIBLE) {
                findViewById.setVisibility(View.VISIBLE);
                findViewById2.setVisibility(View.VISIBLE);
                layoutNow();
            }
            final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    NUIDocView.this.afterShowUI(z);
                }
            });
        }
    }

    public void start(Uri uri, boolean z, ViewingState viewingState, String str, NUIView.DocStateListener docStateListener, boolean z2) {
        this.mIsTemplate = z;
        this.mStartUri = uri;
        this.mCustomDocdata = str;
        this.mDocStateListener = docStateListener;
        String fileTypeExtension = FileUtils.getFileTypeExtension(getContext(), uri);
        Activity activity = activity();
        this.mDocumentLib = ArDkUtils.getLibraryForPath(activity, "f." + fileTypeExtension);
        applyDocumentTypeConfig("f." + fileTypeExtension);
        this.mViewingState = viewingState;
        if (viewingState != null) {
            setStartPage(viewingState.pageNumber);
        }
        this.mShowUI = z2;
        startUI();
        setDataLeakHandlers();
        initClipboardHandler();
    }

    public final void startUI() {
        this.mStarted = false;
        final ViewGroup viewGroup = (ViewGroup) ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getLayoutId(), (ViewGroup) null);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                NUIDocView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                NUIDocView nUIDocView = NUIDocView.this;
                if (!nUIDocView.mStarted) {
                    nUIDocView.enforceInitialShowUI(viewGroup);
                    NUIDocView.this.afterFirstLayoutComplete();
                    NUIDocView.this.mStarted = true;
                }
            }
        });
        addView(viewGroup);
        SODocSession sODocSession = this.mSession;
        if (sODocSession != null) {
            sODocSession.addLoadListener(new SODocSession.SODocSessionLoadListener() {
                public void onCancel() {
                }

                public void onDocComplete() {
                }

                public void onError(int i, int i2) {
                    Utilities.showMessageAndWait((Activity) NUIDocView.this.getContext(), NUIDocView.this.getContext().getString(R.string.sodk_editor_unable_to_load_document_title), Utilities.getOpenErrorDescription(NUIDocView.this.getContext(), i), new Runnable() {
                        public void run() {
                            NUIDocView nUIDocView = NUIDocView.this;
                            nUIDocView.mCanGoBack = true;
                            nUIDocView.goBack((Runnable) null);
                        }
                    });
                }

                public void onLayoutCompleted() {
                }

                public void onPageLoad(int i) {
                }

                public void onSelectionChanged(int i, int i2) {
                }
            });
        }
    }

    public void tableOfContents() {
    }

    public void triggerOrientationChange() {
        this.mForceOrientationChange = true;
    }

    public void triggerRender() {
        DocView docView = this.mDocView;
        if (docView != null) {
            docView.triggerRender();
        }
        if (this.mDocPageListView != null && usePagesView() && isPageListVisible()) {
            this.mDocPageListView.triggerRender();
        }
    }

    public void updateDrawUIAppearance() {
        boolean selectionCanBeDeleted = getDoc().getSelectionCanBeDeleted();
        boolean drawMode = this.mDocView.getDrawMode();
        if (this.mDrawLineColorButton != null) {
            this.mDeleteInkButton.setEnabled((drawMode && getDocView().hasNotSavedInk()) || selectionCanBeDeleted);
            this.mDrawLineColorButton.setDrawableColor(getDocView().getInkLineColor());
            findViewById(R.id.draw_tools_holder).setSelected(drawMode);
        }
    }

    public void updateEditUIAppearance() {
    }

    public void updateInputView() {
        InputView inputView = this.mInputView;
        if (inputView != null) {
            inputView.updateEditable();
        }
    }

    public void updateInsertUIAppearance() {
        ArDkSelectionLimits selectionLimits = getDocView().getSelectionLimits();
        boolean z = selectionLimits != null && selectionLimits.getIsActive() && selectionLimits.getIsCaret();
        if (this.mInsertImageButton != null && this.mDocCfgOptions.isImageInsertEnabled()) {
            this.mInsertImageButton.setEnabled(z);
        }
        if (this.mInsertPhotoButton != null && this.mDocCfgOptions.isPhotoInsertEnabled()) {
            this.mInsertPhotoButton.setEnabled(z);
        }
    }

    public void updateReviewUIAppearance() {
    }

    public void updateSaveUIAppearance() {
        if (this.mSaveButton != null) {
            boolean documentHasBeenModified = documentHasBeenModified();
            boolean z = false;
            if (!this.mDocCfgOptions.isEditingEnabled()) {
                documentHasBeenModified = false;
            }
            if (getDoc() != null && !getDoc().canSave()) {
                documentHasBeenModified = false;
            }
            if (!this.mIsTemplate) {
                z = documentHasBeenModified;
            }
            this.mSaveButton.setEnabled(z);
        }
    }

    public void updateSearchUIAppearance() {
        String obj = this.mSearchText.getText().toString();
        boolean z = true;
        this.mSearchNextButton.setEnabled(!obj.isEmpty());
        ToolbarButton toolbarButton = this.mSearchPreviousButton;
        if (obj.length() <= 0) {
            z = false;
        }
        toolbarButton.setEnabled(z);
    }

    public void updateUI() {
        updateUIAppearance();
    }

    public void updateUIAppearance() {
        boolean z;
        updateSearchUIAppearance();
        updateSaveUIAppearance();
        ArDkSelectionLimits selectionLimits = getDocView().getSelectionLimits();
        boolean z2 = true;
        if (selectionLimits != null) {
            boolean isActive = selectionLimits.getIsActive();
            z = isActive && !selectionLimits.getIsCaret();
            if (isActive) {
                boolean isCaret = selectionLimits.getIsCaret();
            }
        } else {
            z = false;
        }
        if (!this.mDocCfgOptions.isEditingEnabled()) {
            ToolbarButton toolbarButton = this.mCopyButton2;
            if (toolbarButton != null) {
                if (!z || !((SODoc) this.mSession.getDoc()).getSelectionCanBeCopied()) {
                    z2 = false;
                }
                toolbarButton.setEnabled(z2);
                return;
            }
            return;
        }
        updateEditUIAppearance();
        updateUndoUIAppearance();
        updateReviewUIAppearance();
        updateInsertUIAppearance();
        updateDrawUIAppearance();
        doUpdateCustomUI();
    }

    public void updateUndoUIAppearance() {
        SODocSession sODocSession = this.mSession;
        if (sODocSession != null && sODocSession.getDoc() != null) {
            ArDkDoc doc = this.mSession.getDoc();
            setButtonEnabled(this.mUndoButton, doc.canUndo());
            setButtonEnabled(this.mRedoButton, doc.canRedo());
        }
    }

    public boolean usePagesView() {
        return true;
    }

    public boolean usePauseHandler() {
        return true;
    }

    public boolean wasTyping() {
        return System.currentTimeMillis() - this.lastTypingTime < 500;
    }

    public void goToPage(int i, boolean z) {
        this.mDocView.scrollToPage(i, z);
        if (usePagesView()) {
            this.mDocPageListView.setCurrentPage(i);
            this.mDocPageListView.scrollToPage(i, z);
        }
        this.mCurrentPageNum = i;
        setCurrentPage(this, i);
    }

    public void showKeyboard(boolean z, Runnable runnable) {
        boolean z2 = getKeyboardHeight() > 0;
        if (z) {
            Utilities.showKeyboard(getContext());
            if (z2) {
                runnable.run();
            } else {
                this.mShowHideKeyboardRunnable = runnable;
            }
        } else {
            Utilities.hideKeyboard(getContext());
            if (!z2) {
                runnable.run();
            } else {
                this.mShowHideKeyboardRunnable = runnable;
            }
        }
    }

    public void showPages(final int i1) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.pages_container);
        if (relativeLayout != null) {
            if (relativeLayout.getVisibility() != View.VISIBLE) {
                relativeLayout.setVisibility(View.VISIBLE);
                this.mDocPageListView.setVisibility(View.VISIBLE);
                this.mDocView.onShowPages();
            }
            final ViewTreeObserver viewTreeObserver = this.mDocView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    NUIDocView nUIDocView = NUIDocView.this;
                    if (!nUIDocView.mFinished) {
                        int i = i1;
                        if (i == -1) {
                            i = nUIDocView.mDocView.getMostVisiblePage();
                        }
                        NUIDocView.this.mDocPageListView.setCurrentPage(i);
                        NUIDocView.this.mDocPageListView.scrollToPage(i, false);
                        NUIDocView.this.mDocPageListView.fitToColumns();
                        NUIDocView.this.mDocPageListView.layoutNow();
                        NUIDocView.this.doUpdateCustomUI();
                    }
                }
            });
        }
    }

    public void save(final DocumentView.OnSaveListener onSaveListener) {
        preSave();
        this.mSession.getDoc().saveTo(this.mFileState.getInternalPath(), new SODocSaveListener() {
            public void onComplete(int i, int i2) {
                if (i == 0) {
                    NUIDocView.this.mFileState.saveFile();
                    onSaveListener.done(true);
                    return;
                }
                onSaveListener.done(false);
            }
        });
    }

    public boolean select(Point point, Point point2) {
        DocView docView = this.mDocView;
        if (docView != null) {
            return docView.select(point, point2);
        }
        return false;
    }

    public void exportTo(String str, String str2, SODocSaveListener sODocSaveListener) {
        this.mSession.getDoc().exportTo(str, sODocSaveListener, str2);
    }

    public void showPagesTab() {
        Context context = getContext();
        int i = R.string.sodk_editor_tab_pages;
        showPagesTab(context.getString(i), i);
    }

    public void print(String str, final Runnable runnable1) {
        final String str2 = FileUtils.getTempPathRoot(getContext()) + "/print/" + str;
        FileUtils.createDirectory(str2);
        FileUtils.deleteFile(str2);
        getDoc().saveToPDF(str2, false, new SODocSaveListener() {
            public void onComplete(int i, int i2) {
                if (i == 0) {
                    String name = new File(NUIDocView.this.getSession().getFileState().getOpenedPath()).getName();
                    if (!FileUtils.getExtension(NUIDocView.this.getSession().getUserPath()).equalsIgnoreCase("pdf")) {
                        name = name + ".pdf";
                    }
                    PrintHelperPdf printHelperPdf = new PrintHelperPdf();
                    printHelperPdf.setPrintName(name);
                    printHelperPdf.printPDF(NUIDocView.this.getContext(), str2, new Runnable() {
                        public void run() {
                            FileUtils.deleteFile(str2);
                            Runnable runnable = runnable1;
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    });
                    return;
                }
                Runnable runnable = runnable1;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    public void start(SODocSession sODocSession, ViewingState viewingState, String str, NUIView.DocStateListener docStateListener) {
        this.mIsSession = true;
        this.mSession = sODocSession;
        this.mIsTemplate = false;
        this.mViewingState = viewingState;
        applyDocumentTypeConfig(sODocSession.getUserPath());
        if (viewingState != null) {
            setStartPage(viewingState.pageNumber);
        }
        this.mDocStateListener = docStateListener;
        this.mForeignData = str;
        this.mDocumentLib = ArDkUtils.getLibraryForPath(activity(), sODocSession.getUserPath());
        startUI();
        setDataLeakHandlers();
        initClipboardHandler();
    }

    public void start(SOFileState sOFileState, ViewingState viewingState, NUIView.DocStateListener docStateListener) {
        this.mIsSession = false;
        this.mIsTemplate = sOFileState.isTemplate();
        this.mState = sOFileState;
        this.mViewingState = viewingState;
        applyDocumentTypeConfig(sOFileState.getOpenedPath());
        if (viewingState != null) {
            setStartPage(viewingState.pageNumber);
        }
        this.mDocStateListener = docStateListener;
        this.mDocumentLib = ArDkUtils.getLibraryForPath(activity(), sOFileState.getOpenedPath());
        startUI();
        setDataLeakHandlers();
        initClipboardHandler();
    }

    public NUIDocView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
    }

    public NUIDocView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context);
    }
}
