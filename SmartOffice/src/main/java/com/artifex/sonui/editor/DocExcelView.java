package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.artifex.solib.ArDkPage;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.SODoc;
import com.artifex.solib.SOPage;
import com.artifex.solib.SOSelectionTableRange;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DocExcelView extends DocView implements TextView.OnEditorActionListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public FreezePageView bottomLeft;
    public FreezePageView bottomRight;
    public boolean computingSelectionLimits = false;
    public boolean freezeShown = true;
    public boolean frozen = false;
    public HorizontalRuler hRuler1;
    public HorizontalRuler hRuler2;
    public Context mContext;
    public int mCurrentSheet = 0;
    public SOTextView mFxButton;
    public boolean mHandlingViewingState = false;
    public boolean mTapped = false;
    public SOEditText mTextInput;
    public boolean mUseSavedScroll = true;
    public FreezePageView[] pagePanes = new FreezePageView[4];
    public Runnable postLayoutRunnable = null;
    public boolean sawFirstSheet = false;
    public Map<String, Float> scaleMap = new HashMap();
    public DocPageView scrollingView = null;
    public Map<String, Integer> scrollxMap = new HashMap();
    public Map<String, Integer> scrollyMap = new HashMap();
    public FreezePageView topLeft;
    public FreezePageView topRight;
    public VerticalRuler vRuler1;
    public VerticalRuler vRuler2;

    public DocExcelView(Context context) {
        super(context);
    }

    public static void access$100(DocExcelView docExcelView, float f, float f2) {
        super.doSingleTap(f, f2);
        docExcelView.setEditText(((SODoc) docExcelView.getDoc()).getSelectionAsText());
    }

    public void addColumnsLeft() {
        ((SODoc) getDoc()).addColumnsLeft();
        if (this.frozen && this.freezeShown) {
            onFreezeChange();
        }
    }

    public void addColumnsRight() {
        ((SODoc) getDoc()).addColumnsRight();
        if (this.frozen && this.freezeShown) {
            onFreezeChange();
        }
    }

    public void addRowsAbove() {
        ((SODoc) getDoc()).addRowsAbove();
        if (this.frozen && this.freezeShown) {
            onFreezeChange();
        }
    }

    public void addRowsBelow() {
        ((SODoc) getDoc()).addRowsBelow();
        if (this.frozen && this.freezeShown) {
            onFreezeChange();
        }
    }

    public boolean clearAreaSelection() {
        return false;
    }

    public void computeSelectionLimits() {
        this.computingSelectionLimits = true;
        super.computeSelectionLimits();
        this.computingSelectionLimits = false;
    }

    public Point constrainScrollBy(int i, int i2) {
        Point constrainScrollBy = super.constrainScrollBy(i, i2);
        DocExcelPageView sheetPage = getSheetPage(getCurrentSheet());
        float[] horizontalRuler = sheetPage.getHorizontalRuler();
        float[] verticalRuler = sheetPage.getVerticalRuler();
        if (!(horizontalRuler == null || verticalRuler == null)) {
            int pageToView = sheetPage.pageToView((int) horizontalRuler[0]);
            int pageToView2 = sheetPage.pageToView((int) verticalRuler[0]);
            if (getScrollX() + constrainScrollBy.x < pageToView) {
                constrainScrollBy.x = pageToView - getScrollX();
            }
            if (getScrollY() + constrainScrollBy.y < pageToView2) {
                constrainScrollBy.y = pageToView2 - getScrollY();
            }
        }
        return constrainScrollBy;
    }

    public boolean copyEditTextToCell() {
        if (!this.mTextInput.isEnabled()) {
            return false;
        }
        String selectionAsText = ((SODoc) getDoc()).getSelectionAsText();
        String str = "";
        if (selectionAsText == null) {
            selectionAsText = str;
        }
        String editText = getEditText();
        if (editText != null) {
            str = editText;
        }
        if (str.equals(selectionAsText)) {
            return false;
        }
        ((SODoc) getDoc()).setSelectionText(str);
        return true;
    }

    public void copySelectionToEdit() {
        SODoc sODoc = (SODoc) getDoc();
        if (sODoc.selectionTableRange() != null) {
            setEditText(sODoc.getSelectionAsText());
        }
    }

    public int countColumns() {
        return getSheetPage(getCurrentSheet()).getHorizontalRuler().length - 1;
    }

    public int countRows() {
        return getSheetPage(getCurrentSheet()).getVerticalRuler().length - 1;
    }

    public void deleteSelectedColumns() {
        SODoc sODoc = (SODoc) getDoc();
        SOSelectionTableRange selectionTableRange = sODoc.selectionTableRange();
        int columnCount = (selectionTableRange.columnCount() + (selectionTableRange.firstColumn() + 1)) - 1;
        int countColumns = countColumns();
        sODoc.deleteColumns();
        if (columnCount >= countColumns) {
            sODoc.clearSelection();
        }
    }

    public void deleteSelectedRows() {
        SODoc sODoc = (SODoc) getDoc();
        SOSelectionTableRange selectionTableRange = sODoc.selectionTableRange();
        int rowCount = (selectionTableRange.rowCount() + (selectionTableRange.firstRow() + 1)) - 1;
        int countRows = countRows();
        sODoc.deleteRows();
        if (rowCount >= countRows) {
            sODoc.clearSelection();
        }
    }

    public void doSingleTap(final float f, final float f2) {
        this.mTapped = true;
        if (copyEditTextToCell()) {
            this.postLayoutRunnable = new Runnable() {
                public void run() {
                    DocExcelView.access$100(DocExcelView.this, f, f2);
                }
            };
            return;
        }
        super.doSingleTap(f, f2);
        setEditText(((SODoc) getDoc()).getSelectionAsText());
    }

    public DocPageView findPageViewContainingPoint(int i, int i2, boolean z) {
        if (!this.frozen || !this.freezeShown) {
            return super.findPageViewContainingPoint(i, i2, z);
        }
        int i3 = 0;
        while (true) {
            FreezePageView[] freezePageViewArr = this.pagePanes;
            if (i3 >= freezePageViewArr.length) {
                return null;
            }
            FreezePageView freezePageView = freezePageViewArr[i3];
            Rect rect = new Rect();
            freezePageView.getGlobalVisibleRect(rect);
            if (rect.contains(i, i2)) {
                return freezePageView;
            }
            i3++;
        }
    }

    public void focusInputView() {
    }

    public int getCurrentSheet() {
        return this.mCurrentSheet;
    }

    public String getEditText() {
        return this.mTextInput.getText().toString();
    }

    public Point getFirstCellLocation(int i) {
        DocExcelPageView sheetPage = getSheetPage(i);
        sheetPage.getPage().getTopLeftCell();
        return new Point(sheetPage.pageToView((int) sheetPage.getHorizontalRuler()[0]), sheetPage.pageToView((int) sheetPage.getVerticalRuler()[0]));
    }

    public Point getFreezeCellLocation(int i) {
        DocExcelPageView sheetPage = getSheetPage(i);
        Point topLeftCell = sheetPage.getPage().getTopLeftCell();
        return new Point(sheetPage.pageToView((int) sheetPage.getHorizontalRuler()[topLeftCell.x]), sheetPage.pageToView((int) sheetPage.getVerticalRuler()[topLeftCell.y]));
    }

    public Point getLastCellLocation(int i) {
        DocExcelPageView sheetPage = getSheetPage(i);
        sheetPage.getPage().getTopLeftCell();
        float[] horizontalRuler = sheetPage.getHorizontalRuler();
        float[] verticalRuler = sheetPage.getVerticalRuler();
        return new Point(sheetPage.pageToView((int) horizontalRuler[horizontalRuler.length - 1]), sheetPage.pageToView((int) verticalRuler[verticalRuler.length - 1]));
    }

    public View getOrCreateChild(int i) {
        if (!this.frozen || !this.freezeShown || this.computingSelectionLimits) {
            return getSheetPage(getCurrentSheet());
        }
        FreezePageView freezePageView = this.pagePanes[i];
        onScaleChild(freezePageView, Float.valueOf(this.mScale));
        return freezePageView;
    }

    public int getPageCount() {
        if (!this.frozen || !this.freezeShown) {
            return 1;
        }
        return this.pagePanes.length;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = r1.getPage();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String getPageTitle(int r1) {
        /*
            r0 = this;
            com.artifex.sonui.editor.DocExcelPageView r1 = r0.getSheetPage(r1)
            if (r1 == 0) goto L_0x0013
            com.artifex.solib.ArDkPage r1 = r1.getPage()
            if (r1 == 0) goto L_0x0013
            com.artifex.solib.SOPage r1 = (com.artifex.solib.SOPage) r1
            java.lang.String r1 = r1.getPageTitle()
            return r1
        L_0x0013:
            java.lang.String r1 = ""
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.DocExcelView.getPageTitle(int):java.lang.String");
    }

    public Point getSelectedCell() {
        SOSelectionTableRange selectionTableRange;
        if (getSelectionLimits() == null || (selectionTableRange = ((SODoc) getDoc()).selectionTableRange()) == null) {
            return null;
        }
        return new Point(selectionTableRange.firstColumn(), selectionTableRange.firstRow());
    }

    public final DocExcelPageView getSheetPage(int i) {
        DocExcelPageView docExcelPageView = (DocExcelPageView) super.getOrCreateChild(i);
        onScaleChild(docExcelPageView, Float.valueOf(this.mScale));
        return docExcelPageView;
    }

    public Point getTopLeftCell(int i) {
        return getSheetPage(i).getPage().getTopLeftCell();
    }

    public View getViewFromAdapter(int i) {
        DocExcelPageView docExcelPageView = new DocExcelPageView(this.mContext, getDoc());
        docExcelPageView.setupPage(i, getWidth(), 1);
        return docExcelPageView;
    }

    public void handleStartPage() {
    }

    public void handleViewingState() {
        ViewingState viewingState = this.mViewingState;
        if (viewingState != null) {
            if (viewingState.pageNumber >= super.getPageCount() || this.mViewingState.pageNumber < 0) {
                this.mViewingState.pageNumber = 0;
            }
            this.mHandlingViewingState = true;
            setCurrentSheet(this.mViewingState.pageNumber);
            this.mHandlingViewingState = false;
        }
    }

    public final float initialScale(int i) {
        DocExcelPageView sheetPage = getSheetPage(i);
        float[] verticalRuler = sheetPage.getVerticalRuler();
        if (verticalRuler == null || verticalRuler.length < 2) {
            float max = Math.max(((float) sheetPage.getPage().sizeAtZoom(1.0d).x) / 786.0f, 1.0f);
            return Utilities.isPhoneDevice(getContext()) ? max * 2.0f : max;
        }
        int min = Math.min(verticalRuler.length, 101);
        float f = BitmapDescriptorFactory.HUE_RED;
        for (int i2 = 1; i2 < min; i2++) {
            float f2 = verticalRuler[i2] - verticalRuler[i2 - 1];
            if (f == BitmapDescriptorFactory.HUE_RED) {
                f = f2;
            } else {
                f = Math.min(f, f2);
            }
        }
        return (float) (((double) (((float) Utilities.inchesToPixels(getContext(), 0.15f)) / f)) / sheetPage.getZoom());
    }

    public void initialize(Context context) {
        super.initialize(context);
        this.mContext = context;
        SOEditText sOEditText = (SOEditText) ((Activity) context).findViewById(R.id.text_input);
        this.mTextInput = sOEditText;
        sOEditText.setOnEditorActionListener(this);
        this.mTextInput.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                SODoc sODoc = (SODoc) DocExcelView.this.getDoc();
                if (keyEvent.getAction() != 1) {
                    return false;
                }
                boolean isShiftPressed = keyEvent.isShiftPressed();
                int keyCode = keyEvent.getKeyCode();
                if (keyCode == 61) {
                    DocExcelView.this.copyEditTextToCell();
                    if (isShiftPressed) {
                        sODoc.moveTableSelectionLeft();
                        return false;
                    }
                    sODoc.moveTableSelectionRight();
                    return false;
                } else if (keyCode == 66) {
                    return true;
                } else {
                    switch (keyCode) {
                        case 19:
                            DocExcelView.this.copyEditTextToCell();
                            sODoc.moveTableSelectionUp();
                            return false;
                        case 20:
                            DocExcelView.this.copyEditTextToCell();
                            sODoc.moveTableSelectionDown();
                            return false;
                        case 21:
                            DocExcelView.this.copyEditTextToCell();
                            sODoc.moveTableSelectionLeft();
                            return false;
                        case 22:
                            DocExcelView.this.copyEditTextToCell();
                            sODoc.moveTableSelectionRight();
                            return false;
                        default:
                            return false;
                    }
                }
            }
        });
        this.mFxButton = (SOTextView) ((Activity) this.mContext).findViewById(R.id.fx_button);
        AnonymousClass2 r2 = new ActionMode.Callback() {
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
                    if (DocExcelView.this.mDocCfgOptions.isShareEnabled() && itemId == 16908341) {
                        z = false;
                    }
                    if (DocExcelView.this.mDocCfgOptions.isExtClipboardOutEnabled() && (itemId == 16908320 || itemId == 16908321)) {
                        z = false;
                    }
                    if (DocExcelView.this.mDocCfgOptions.isExtClipboardInEnabled() && itemId == 16908322) {
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
        this.mTextInput.setCustomSelectionActionModeCallback(r2);
        this.mTextInput.setCustomInsertionActionModeCallback(r2);
        this.hRuler1 = (HorizontalRuler) ((Activity) this.mContext).findViewById(R.id.horizontal_ruler);
        this.vRuler1 = (VerticalRuler) ((Activity) this.mContext).findViewById(R.id.vertical_ruler);
        this.hRuler2 = (HorizontalRuler) ((Activity) this.mContext).findViewById(R.id.horizontal_ruler2);
        this.vRuler2 = (VerticalRuler) ((Activity) this.mContext).findViewById(R.id.vertical_ruler2);
    }

    public void insertEditText(String str) {
        int max = Math.max(this.mTextInput.getSelectionStart(), 0);
        int max2 = Math.max(this.mTextInput.getSelectionEnd(), 0);
        this.mTextInput.getText().replace(Math.min(max, max2), Math.max(max, max2), str, 0, str.length());
    }

    public boolean isFreezeShown() {
        return this.freezeShown;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public float maxScale() {
        float initialScale = initialScale(getCurrentSheet());
        return (initialScale > 1000.0f ? 1.0f : 1.5f) * initialScale;
    }

    public float minScale() {
        return initialScale(getCurrentSheet()) * 0.1f;
    }

    public final void moveHandle(DragHandle dragHandle, int i, int i2) {
        int i3 = 0;
        boolean z = false;
        while (true) {
            FreezePageView[] freezePageViewArr = this.pagePanes;
            if (i3 < freezePageViewArr.length) {
                FreezePageView freezePageView = freezePageViewArr[i3];
                Point pageToView = freezePageView.pageToView(i, i2);
                Rect rect = new Rect(freezePageView.getChildRect());
                rect.offset(-rect.left, -rect.top);
                if (rect.contains(pageToView.x, pageToView.y)) {
                    positionHandle(dragHandle, freezePageView, i, i2);
                    z = true;
                }
                i3++;
            } else {
                dragHandle.show(z);
                return;
            }
        }
    }

    public void moveHandlesToCorners() {
        if (!finished()) {
            if (!this.frozen || !this.freezeShown) {
                super.moveHandlesToCorners();
                return;
            }
            ArDkSelectionLimits selectionLimits = getSelectionLimits();
            if (selectionLimits != null) {
                moveHandle(this.mSelectionHandleTopLeft, (int) selectionLimits.getStart().x, (int) selectionLimits.getStart().y);
                moveHandle(this.mSelectionHandleBottomRight, (int) selectionLimits.getEnd().x, (int) selectionLimits.getEnd().y);
            }
        }
    }

    public boolean onDown(MotionEvent motionEvent) {
        boolean onDown = super.onDown(motionEvent);
        if (this.frozen && this.freezeShown) {
            Point viewToScreen = viewToScreen(new Point((int) motionEvent.getX(), (int) motionEvent.getY()));
            this.scrollingView = findPageViewContainingPoint(viewToScreen.x, viewToScreen.y, false);
        }
        return onDown;
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6 && i != 0) {
            return true;
        }
        copyEditTextToCell();
        ((SODoc) getDoc()).moveTableSelectionDown();
        return true;
    }

    public void onEndFling() {
        reportViewChanges();
    }

    public void onEndRenderPass() {
        updateRulers(false);
        moveHandlesToCorners();
    }

    public void onFoundText(int i, RectF rectF) {
        this.mUseSavedScroll = false;
        this.mHostActivity.clickSheetButton(i, true);
        this.mUseSavedScroll = true;
        if (this.frozen && this.freezeShown) {
            i = 0;
        }
        scrollBoxIntoView(i, rectF, true);
    }

    public void onFreezeCell() {
        SOPage sOPage;
        DocExcelPageView sheetPage = getSheetPage(getCurrentSheet());
        if (sheetPage != null && (sOPage = (SOPage) sheetPage.getPage()) != null) {
            if (this.frozen) {
                sOPage.setTopLeftCell(0, 0);
                onFreezeChange();
                return;
            }
            Point selectedCell = getSelectedCell();
            if (selectedCell != null) {
                sOPage.setTopLeftCell(selectedCell.x, selectedCell.y);
                onFreezeChange();
            }
        }
    }

    public final void onFreezeChange() {
        scrollTo(0, 0);
        this.sawFirstSheet = false;
        setCurrentSheet(getCurrentSheet());
        triggerRender();
        this.mHostActivity.updateUI();
    }

    public void onFreezeFirstCol() {
        SOPage sOPage;
        DocExcelPageView sheetPage = getSheetPage(getCurrentSheet());
        if (sheetPage != null && (sOPage = (SOPage) sheetPage.getPage()) != null) {
            sOPage.setTopLeftCell(1, 0);
            onFreezeChange();
        }
    }

    public void onFreezeFirstRow() {
        SOPage sOPage;
        DocExcelPageView sheetPage = getSheetPage(getCurrentSheet());
        if (sheetPage != null && (sOPage = (SOPage) sheetPage.getPage()) != null) {
            sOPage.setTopLeftCell(0, 1);
            onFreezeChange();
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (!this.frozen || !this.freezeShown) {
            super.onLayout(z, i, i2, i3, i4);
            if (!finished()) {
                handleStartPage();
                if (canEditText() && !NUIDocView.currentNUIDocView().isFullScreen()) {
                    ((LinearLayout) ((Activity) this.mContext).findViewById(R.id.fx_bar)).setVisibility(0);
                }
            }
        } else if (!finished() && isShown() && shouldLayout()) {
            Point freezeCellLocation = getFreezeCellLocation(getCurrentSheet());
            Point firstCellLocation = getFirstCellLocation(getCurrentSheet());
            Point lastCellLocation = getLastCellLocation(getCurrentSheet());
            Rect rect = new Rect();
            getLocalVisibleRect(rect);
            this.topLeft.layout(0, 0, freezeCellLocation.x - firstCellLocation.x, freezeCellLocation.y - firstCellLocation.y);
            this.topRight.layout(freezeCellLocation.x - firstCellLocation.x, 0, rect.width(), freezeCellLocation.y - firstCellLocation.y);
            this.bottomLeft.layout(0, freezeCellLocation.y - firstCellLocation.y, freezeCellLocation.x - firstCellLocation.x, rect.height());
            this.bottomRight.layout(freezeCellLocation.x - firstCellLocation.x, freezeCellLocation.y - firstCellLocation.y, rect.width(), rect.height());
            this.topLeft.setScrollingLimits(firstCellLocation.x, firstCellLocation.y, freezeCellLocation.x, freezeCellLocation.y);
            this.topRight.setScrollingLimits(freezeCellLocation.x, firstCellLocation.y, lastCellLocation.x, freezeCellLocation.y);
            this.bottomLeft.setScrollingLimits(firstCellLocation.x, freezeCellLocation.y, freezeCellLocation.x, lastCellLocation.y);
            this.bottomRight.setScrollingLimits(freezeCellLocation.x, freezeCellLocation.y, lastCellLocation.x, lastCellLocation.y);
            DocPageView docPageView = this.scrollingView;
            FreezePageView freezePageView = this.topLeft;
            if (docPageView != freezePageView) {
                FreezePageView freezePageView2 = this.topRight;
                if (docPageView == freezePageView2) {
                    freezePageView2.scrollBy(-this.mXScroll, 0);
                    this.bottomRight.scrollBy(-this.mXScroll, 0);
                } else {
                    FreezePageView freezePageView3 = this.bottomLeft;
                    if (docPageView == freezePageView3) {
                        freezePageView3.scrollBy(0, -this.mYScroll);
                        this.bottomRight.scrollBy(0, -this.mYScroll);
                    } else if (docPageView == this.bottomRight) {
                        freezePageView2.scrollBy(-this.mXScroll, 0);
                        this.bottomLeft.scrollBy(0, -this.mYScroll);
                        this.bottomRight.scrollBy(-this.mXScroll, -this.mYScroll);
                    } else {
                        freezePageView.scrollBy(0, 0);
                        this.topRight.scrollBy(-this.mXScroll, 0);
                        this.bottomLeft.scrollBy(0, -this.mYScroll);
                        this.bottomRight.scrollBy(-this.mXScroll, -this.mYScroll);
                    }
                }
            }
            this.mYScroll = 0;
            this.mXScroll = 0;
            triggerRender();
        }
    }

    public void onLayoutChanged() {
        super.onLayoutChanged();
        Runnable runnable = this.postLayoutRunnable;
        if (runnable != null) {
            runnable.run();
            this.postLayoutRunnable = null;
        }
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public void onLongPressMoving(MotionEvent motionEvent) {
    }

    public void onLongPressRelease() {
    }

    public void onOrientationChange() {
        if (!this.frozen || !this.freezeShown) {
            super.onOrientationChange();
            return;
        }
        scaleChildren();
        updateRulers(false);
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        boolean onScale = super.onScale(scaleGestureDetector);
        if (this.frozen && this.freezeShown) {
            this.mXScroll = 0;
            this.mYScroll = 0;
        }
        return onScale;
    }

    public void onSelectionChanged() {
        super.onSelectionChanged();
        if (!canEditText()) {
            this.mTextInput.setEnabled(false);
            this.mFxButton.setEnabled(false);
        } else {
            boolean selectionCanHaveTextAltered = ((SODoc) getDoc()).getSelectionCanHaveTextAltered();
            this.mTextInput.setEnabled(selectionCanHaveTextAltered);
            this.mFxButton.setEnabled(selectionCanHaveTextAltered);
            if (selectionCanHaveTextAltered) {
                this.mTextInput.setFocusableInTouchMode(true);
                if (!NUIDocView.currentNUIDocView().isLandscapePhone() && this.mTapped) {
                    this.mTextInput.requestFocus();
                    Utilities.showKeyboard(getContext());
                }
                copySelectionToEdit();
            } else {
                this.mTextInput.clearFocus();
                Utilities.hideKeyboard(getContext());
                this.mTextInput.setText("");
            }
        }
        this.mTapped = false;
        computeSelectionLimits();
        scrollSelectedCellIntoView();
    }

    public void onSheetDeleted() {
        this.sawFirstSheet = false;
    }

    public void onShowKeyboard(boolean z) {
        if (!z && getContext().getResources().getConfiguration().keyboardHidden == 2) {
            SODoc sODoc = (SODoc) getDoc();
            SOSelectionTableRange sOSelectionTableRange = null;
            if (sODoc != null) {
                sOSelectionTableRange = sODoc.selectionTableRange();
            }
            if (sOSelectionTableRange != null && sOSelectionTableRange.rowCount() == 1 && sOSelectionTableRange.columnCount() == 1) {
                copyEditTextToCell();
            }
        }
        new Handler().post(new Runnable() {
            public void run() {
                DocExcelView docExcelView = DocExcelView.this;
                int i = DocExcelView.$r8$clinit;
                docExcelView.updateRulers(true);
            }
        });
        super.onShowKeyboard(z);
    }

    public void onUp(MotionEvent motionEvent) {
        super.onUp(motionEvent);
        if (this.frozen && this.freezeShown) {
            this.scrollingView = null;
        }
    }

    public void resetInputView() {
    }

    public void scrollBoxIntoView(int i, RectF rectF) {
        DocExcelPageView docExcelPageView;
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        rect.offset(0, -rect.top);
        if (!this.frozen || !this.freezeShown) {
            docExcelPageView = getSheetPage(i);
        } else {
            docExcelPageView = this.pagePanes[i];
        }
        Point pageToView = docExcelPageView.pageToView((int) rectF.left, (int) rectF.bottom);
        int scrollY = pageToView.y - getScrollY();
        pageToView.y = scrollY;
        int i2 = rect.top;
        if (scrollY < i2 || scrollY > rect.bottom) {
            smoothScrollBy(0, ((i2 + rect.bottom) / 2) - scrollY);
        }
    }

    public void scrollSelectedCellAboveKeyboard() {
        if (!this.frozen || !this.freezeShown) {
            DocExcelPageView sheetPage = getSheetPage(getCurrentSheet());
            Rect pageToScreen = sheetPage.pageToScreen(sheetPage.selectionLimits().getBox());
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            int keyboardHeight = getResources().getDisplayMetrics().heightPixels - this.mHostActivity.getKeyboardHeight();
            rect.bottom = keyboardHeight;
            int i = pageToScreen.top;
            int i2 = rect.top;
            if (i < i2 || pageToScreen.bottom > keyboardHeight) {
                smoothScrollBy(0, (((i2 + keyboardHeight) / 2) - (pageToScreen.height() / 2)) - ((i + pageToScreen.bottom) / 2));
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x007f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scrollSelectedCellIntoView() {
        /*
            r7 = this;
            boolean r0 = r7.frozen
            if (r0 == 0) goto L_0x0009
            boolean r0 = r7.freezeShown
            if (r0 == 0) goto L_0x0009
            return
        L_0x0009:
            com.artifex.solib.ArDkSelectionLimits r0 = r7.getSelectionLimits()
            if (r0 != 0) goto L_0x0010
            return
        L_0x0010:
            android.graphics.RectF r0 = r0.getBox()
            if (r0 != 0) goto L_0x0017
            return
        L_0x0017:
            android.graphics.Rect r1 = new android.graphics.Rect
            r1.<init>()
            r7.getGlobalVisibleRect(r1)
            int r2 = r1.left
            int r2 = -r2
            int r3 = r1.top
            int r3 = -r3
            r1.offset(r2, r3)
            int r2 = r7.getCurrentSheet()
            com.artifex.sonui.editor.DocExcelPageView r2 = r7.getSheetPage(r2)
            android.graphics.Rect r3 = r2.getChildRect()
            android.graphics.Rect r0 = r2.pageToView((android.graphics.RectF) r0)
            int r2 = r3.left
            int r4 = r7.getScrollX()
            int r2 = r2 - r4
            int r3 = r3.top
            int r4 = r7.getScrollY()
            int r3 = r3 - r4
            r0.offset(r2, r3)
            int r2 = r0.top
            int r3 = r1.top
            r4 = 0
            if (r2 < r3) goto L_0x0062
            int r5 = r1.bottom
            if (r2 <= r5) goto L_0x0055
            goto L_0x0062
        L_0x0055:
            int r2 = r0.bottom
            if (r2 < r3) goto L_0x005e
            if (r2 <= r5) goto L_0x005c
            goto L_0x005e
        L_0x005c:
            r3 = 0
            goto L_0x0068
        L_0x005e:
            int r3 = r3 + r5
            int r3 = r3 / 2
            goto L_0x0067
        L_0x0062:
            int r5 = r1.bottom
            int r3 = r3 + r5
            int r3 = r3 / 2
        L_0x0067:
            int r3 = r3 - r2
        L_0x0068:
            int r2 = r0.left
            int r5 = r1.left
            if (r2 < r5) goto L_0x007f
            int r6 = r1.right
            if (r2 <= r6) goto L_0x0073
            goto L_0x007f
        L_0x0073:
            int r0 = r0.right
            if (r0 < r5) goto L_0x0079
            if (r0 <= r6) goto L_0x0086
        L_0x0079:
            int r5 = r5 + r6
            int r5 = r5 / 2
            int r4 = r5 - r0
            goto L_0x0086
        L_0x007f:
            int r0 = r1.right
            int r5 = r5 + r0
            int r5 = r5 / 2
            int r4 = r5 - r2
        L_0x0086:
            r7.smoothScrollBy(r4, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.DocExcelView.scrollSelectedCellIntoView():void");
    }

    public void scrollSelectionIntoView() {
        if (!this.frozen || !this.freezeShown) {
            super.scrollSelectionIntoView();
        }
    }

    public void scrollToPage(int i, boolean z) {
    }

    public void setCurrentSheet(int i) {
        FreezePageView[] freezePageViewArr;
        ArDkPage page;
        if (getCurrentSheet() < super.getPageCount() && !this.mHandlingViewingState) {
            String pageTitle = getPageTitle(getCurrentSheet());
            if (this.scaleMap.containsKey(pageTitle)) {
                this.scrollxMap.put(pageTitle, Integer.valueOf(getScrollX()));
                this.scrollyMap.put(pageTitle, Integer.valueOf(getScrollY()));
                this.scaleMap.put(pageTitle, Float.valueOf(this.mScale));
            }
        }
        if (i != getCurrentSheet() || !this.sawFirstSheet) {
            this.sawFirstSheet = true;
            if (this.frozen && this.freezeShown) {
                int i2 = 0;
                while (true) {
                    FreezePageView[] freezePageViewArr2 = this.pagePanes;
                    if (i2 >= freezePageViewArr2.length) {
                        break;
                    }
                    FreezePageView freezePageView = freezePageViewArr2[i2];
                    if (freezePageView != null) {
                        freezePageView.stopRender();
                    }
                    i2++;
                }
            } else {
                DocExcelPageView sheetPage = getSheetPage(getCurrentSheet());
                if (sheetPage != null) {
                    sheetPage.stopRender();
                }
            }
            this.mCurrentSheet = i;
            clearChildViews();
            removeAllViewsInLayout();
            this.frozen = false;
            DocExcelPageView sheetPage2 = getSheetPage(getCurrentSheet());
            if (!(sheetPage2 == null || (page = sheetPage2.getPage()) == null)) {
                Point topLeftCell = ((SOPage) page).getTopLeftCell();
                if (!(topLeftCell.x == 0 && topLeftCell.y == 0)) {
                    this.frozen = true;
                }
            }
            int i3 = 8;
            if (!this.frozen || !this.freezeShown) {
                this.hRuler2.setVisibility(8);
                this.vRuler2.setVisibility(8);
                clearChildViews();
                removeAllViewsInLayout();
            } else {
                this.hRuler2.setVisibility(0);
                this.vRuler2.setVisibility(0);
                int i4 = 0;
                while (true) {
                    freezePageViewArr = this.pagePanes;
                    if (i4 >= freezePageViewArr.length) {
                        break;
                    }
                    FreezePageView freezePageView2 = new FreezePageView(this.mContext, getDoc());
                    freezePageView2.setupPage(getCurrentSheet(), getWidth(), 1);
                    freezePageView2.setValid(true);
                    this.pagePanes[i4] = freezePageView2;
                    addChildToLayout(freezePageView2);
                    i4++;
                }
                this.topLeft = freezePageViewArr[0];
                this.topRight = freezePageViewArr[1];
                this.bottomLeft = freezePageViewArr[2];
                this.bottomRight = freezePageViewArr[3];
            }
            updateRulers(false);
            String pageTitle2 = getPageTitle(getCurrentSheet());
            final int i5 = this.scrollxMap.get(pageTitle2);
            if (i5 == null) {
                i5 = 0;
            }
            final int i6 = this.scrollyMap.get(pageTitle2);
            if (i6 == null) {
                i6 = 0;
            }
            Float f = this.scaleMap.get(pageTitle2);
            if (f == null) {
                f = Float.valueOf(initialScale(i));
            }
            this.scaleMap.put(pageTitle2, f);
            DocExcelPageView sheetPage3 = getSheetPage(getCurrentSheet());
            if (sheetPage3 != null) {
                LinearLayout linearLayout = (LinearLayout) ((Activity) getContext()).findViewById(R.id.hruler_holder);
                float[] horizontalRuler = sheetPage3.getHorizontalRuler();
                linearLayout.setVisibility((horizontalRuler == null || horizontalRuler.length <= 0) ? 8 : 0);
                VerticalRuler verticalRuler = (VerticalRuler) ((Activity) getContext()).findViewById(R.id.vertical_ruler);
                float[] verticalRuler2 = sheetPage3.getVerticalRuler();
                if (verticalRuler2 != null && verticalRuler2.length > 0) {
                    i3 = 0;
                }
                verticalRuler.setVisibility(i3);
            }
            updateDragHandles();
            this.mScale = f.floatValue();
            scaleChildren();
            if (!this.frozen || !this.freezeShown) {
                final boolean z = this.mUseSavedScroll;
                post(new Runnable() {
                    public void run() {
                        if (z) {
                            DocExcelView.this.setScrollX(i5.intValue());
                            DocExcelView.this.setScrollY(i6.intValue());
                        }
                        DocExcelView.this.requestLayout();
                    }
                });
                return;
            }
            scrollTo(0, 0);
        }
    }

    public void setEditText(String str) {
        this.mTextInput.setText(str);
        this.mTextInput.selectAll();
    }

    public void setMostVisiblePage() {
    }

    public void setupNoteEditor() {
    }

    public boolean shouldLayout() {
        return getDoc() != null;
    }

    public void showKeyboardAndScroll(Point point) {
    }

    public boolean tapToFocus() {
        return false;
    }

    public void toggleFreezeShown() {
        this.freezeShown = !this.freezeShown;
        onFreezeChange();
    }

    public final void updateFreezeRuler(Ruler ruler, FreezePageView freezePageView, boolean z, boolean z2) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ruler.getLayoutParams();
        if (z) {
            layoutParams.width = freezePageView.getWidth();
            ruler.setScale((float) freezePageView.getZoomScale());
            ruler.setOffsetX(freezePageView.scrollX);
            ruler.setGraduations(freezePageView.getHorizontalRuler());
        } else {
            layoutParams.height = freezePageView.getHeight();
            ruler.setScale((float) freezePageView.getZoomScale());
            ruler.setOffsetY(freezePageView.scrollY);
            ruler.setGraduations(freezePageView.getVerticalRuler());
        }
        ruler.update(z2);
    }

    public void updateInputView() {
    }

    public void updateReview() {
    }

    public final void updateRulers(boolean z) {
        if (getPageCount() != 0) {
            if (!this.frozen || !this.freezeShown) {
                DocExcelPageView sheetPage = getSheetPage(getCurrentSheet());
                if (sheetPage != null) {
                    ((LinearLayout.LayoutParams) this.hRuler1.getLayoutParams()).width = getWidth();
                    ((LinearLayout.LayoutParams) this.vRuler1.getLayoutParams()).height = getHeight();
                    this.hRuler1.setScale((float) sheetPage.getZoomScale());
                    this.hRuler1.setOffsetX(getScrollX());
                    this.hRuler1.setGraduations(sheetPage.getHorizontalRuler());
                    this.hRuler1.update(z);
                    this.vRuler1.setScale((float) sheetPage.getZoomScale());
                    this.vRuler1.setOffsetY(getScrollY());
                    this.vRuler1.setGraduations(sheetPage.getVerticalRuler());
                    this.vRuler1.update(z);
                    return;
                }
                return;
            }
            updateFreezeRuler(this.hRuler1, this.topLeft, true, z);
            updateFreezeRuler(this.hRuler2, this.bottomRight, true, z);
            updateFreezeRuler(this.vRuler1, this.topLeft, false, z);
            updateFreezeRuler(this.vRuler2, this.bottomRight, false, z);
        }
    }

    public void scrollBoxIntoView(int i, RectF rectF, boolean z) {
        super.scrollBoxIntoView(i, rectF, z);
    }

    public DocExcelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DocExcelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
