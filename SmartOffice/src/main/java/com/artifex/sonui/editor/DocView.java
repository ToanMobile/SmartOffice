package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import androidx.core.content.ContextCompat;
import com.applovin.exoplayer2.b.g$a$$ExternalSyntheticLambda1;
import com.artifex.solib.ArDkBitmap;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.ConfigOptions;
import com.artifex.solib.SODoc;
import com.artifex.solib.SORenderListener;
import com.artifex.sonui.editor.DocPageView;
import com.artifex.sonui.editor.History;
import com.artifex.sonui.editor.NoteEditor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.logging.type.LogSeverity;
import com.vungle.warren.downloader.DownloadRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.KotlinVersion;

public class DocView extends AdapterView<Adapter> implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener, Runnable, DragHandleListener {
    public static final int UNSCALED_GAP = 20;
    public int bitmapIndex = 0;
    public ArDkBitmap[] bitmaps;
    public int dropY = -1;
    public boolean flinging = false;
    public int goToThisPage = -1;
    public boolean goToThisPageLast;
    public int lastMostVisibleChild = -1;
    public float lastTapX;
    public float lastTapY;
    public PageAdapter mAdapter;
    public boolean mAddComment = false;
    public final Rect mAllPagesRect = new Rect();
    public final Rect mChildRect = new Rect();
    public final SparseArray<View> mChildViews = new SparseArray<>(3);
    public boolean mConstrained = true;
    public int mCurrentInkLineColor = 0;
    public float mCurrentInkLineThickness = BitmapDescriptorFactory.HUE_RED;
    public SODataLeakHandlers mDataLeakHandlers = null;
    public ArDkDoc mDoc;
    public ConfigOptions mDocCfgOptions = null;
    public DragHandle mDragHandle = null;
    public Point mDragOrigLocation = new Point();
    public boolean mDragging = false;
    public RectF mDraggingObjectPageBounds;
    public boolean mDrawMode = false;
    public DocPageView mDrawingPage = null;
    public ArrayList<DocPageView> mDrawingPageList = new ArrayList<>();
    public int mDropPageAbove = -1;
    public int mDropPageBelow = -1;
    public boolean mFinished = false;
    public int mForceColumnCount = -1;
    public boolean mForceLayout = false;
    public GestureDetector mGestureDetector;
    public History mHistory;
    public DocViewHost mHostActivity = null;
    public final Rect mLastAllPagesRect = new Rect();
    public int mLastLayoutColumns = 1;
    public float mLastLayoutScale = BitmapDescriptorFactory.HUE_RED;
    public float mLastReflowWidth = BitmapDescriptorFactory.HUE_RED;
    public float mLastScale = BitmapDescriptorFactory.HUE_RED;
    public int mLastScrollX = 0;
    public int mLastScrollY = 0;
    public Boolean mLastSelIsAltText = null;
    public long mLastTapTime = 0;
    public int mLastViewPortWidth = 0;
    public float mNAdditionalAngle = BitmapDescriptorFactory.HUE_RED;
    public PointF mNatDim;
    public NoteEditor mNoteEditor = null;
    public boolean mPagesShowing = false;
    public boolean mPressing = false;
    public boolean mPreviousReflowMode = false;
    public boolean mReflowMode = false;
    public int mReflowWidth = -1;
    public DragHandle mResizeHandleBottomLeft = null;
    public DragHandle mResizeHandleBottomRight = null;
    public DragHandle mResizeHandleTopLeft = null;
    public DragHandle mResizeHandleTopRight = null;
    public Point mResizeOrigBottomRight;
    public Rect mResizeOrigRect = new Rect();
    public Point mResizeOrigTopLeft;
    public Rect mResizeRect = new Rect();
    public ArDkBitmap mResizingBitmap = null;
    public ImageView mResizingView;
    public float mRotateAngle = BitmapDescriptorFactory.HUE_RED;
    public DragHandle mRotateHandle = null;
    public float mScale = 1.0f;
    public ScaleGestureDetector mScaleGestureDetector;
    public boolean mScaling;
    public boolean mScrollRequested = false;
    public Scroller mScroller;
    public int mScrollerLastX;
    public int mScrollerLastY;
    public boolean mScrollingStopped = false;
    public DocPageView mSelectionEndPage;
    public DragHandle mSelectionHandleBottomRight = null;
    public DragHandle mSelectionHandleTopLeft = null;
    public ArDkSelectionLimits mSelectionLimits = null;
    public DocPageView mSelectionStartPage;
    public Smoother mSmoother;
    public int mStartPage = -1;
    public int mTapStatus = 0;
    public boolean mTouching = false;
    public boolean mUpdatesPaused = false;
    public Integer mViewSmoothScrollStartX = null;
    public Integer mViewSmoothScrollStartY = null;
    public ViewingState mViewingState;
    public final Rect mViewport = new Rect();
    public final Point mViewportOrigin = new Point();
    public float mX;
    public int mXScroll;
    public float mY;
    public int mYScroll;
    public int mostVisibleChild = -1;
    public final Rect mostVisibleRect = new Rect();
    public boolean once = true;
    public int renderCount = 0;
    public boolean renderRequested = false;
    public Point scrollToHere;
    public DocPageView scrollToHerePage;
    public boolean scrollToSelection = false;
    public boolean scrollingByKeyboard = false;
    public ShowKeyboardListener showKeyboardListener = null;
    public int unscaledMaxw = 0;

    public interface ShowKeyboardListener {
        void onShow(boolean z);
    }

    public class Smoother {
        public int MAX;
        public ArrayList<Integer> values = new ArrayList<>();

        public Smoother(DocView docView, int i) {
            this.MAX = i;
        }

        public void addValue(int i) {
            if (this.values.size() == this.MAX) {
                this.values.remove(0);
            }
            this.values.add(new Integer(i));
        }

        public void clear() {
            this.values.clear();
        }

        public int getAverage() {
            if (this.values.size() == 0) {
                return 0;
            }
            int i = 0;
            for (int i2 = 0; i2 < this.values.size(); i2++) {
                i += this.values.get(i2).intValue();
            }
            return i / this.values.size();
        }
    }

    public DocView(Context context) {
        super(context);
        initialize(context);
    }

    private View getCached() {
        return null;
    }

    public void addChildToLayout(View view) {
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-2, -2);
        }
        addViewInLayout(view, -1, layoutParams, true);
    }

    public void addComment() {
        this.mAddComment = true;
        getDoc().addHighlightAnnotation();
    }

    public void addHistory(int i, int i2, float f, boolean z) {
        if (!z || shouldAddHistory(i, i2, f)) {
            getHistory().add(i, i2, f);
        }
        this.mHostActivity.updateUI();
    }

    public void addPageHistory(int i) {
        addHistory(getScrollX(), getScrollY(), getScale(), true);
        Point scrollToPageAmounts = scrollToPageAmounts(i);
        if (scrollToPageAmounts.y != 0) {
            addHistory(getScrollX(), getScrollY() - scrollToPageAmounts.y, getScale(), false);
        }
    }

    public Point adjustDragHandle(DragHandle dragHandle, Point point) {
        return new Point(point);
    }

    public void afterRedo() {
    }

    public void afterUndo() {
    }

    public boolean allowTouchWithoutChildren() {
        return false;
    }

    public boolean allowXScroll() {
        return !this.mReflowMode;
    }

    public void beforeRedo() {
        if (getDrawMode()) {
            onDrawMode();
            onDrawMode();
        }
    }

    public void beforeUndo() {
        if (getDrawMode()) {
            onDrawMode();
            onDrawMode();
        }
    }

    public boolean canEditText() {
        ConfigOptions configOptions = this.mDocCfgOptions;
        if (configOptions != null) {
            return configOptions.isEditingEnabled();
        }
        return true;
    }

    public boolean canSelectionSpanPages() {
        return true;
    }

    public boolean centerPagesHorizontally() {
        return false;
    }

    public boolean clearAreaSelection() {
        ArDkSelectionLimits selectionLimits = getSelectionLimits();
        if (selectionLimits == null || selectionLimits.getIsCaret() || (!selectionLimits.getHasSelectionStart() && !selectionLimits.getHasSelectionEnd())) {
            return false;
        }
        this.mDoc.clearSelection();
        return true;
    }

    public void clearChildViews() {
        this.mChildViews.clear();
    }

    public void clearInk() {
        Iterator<DocPageView> it = this.mDrawingPageList.iterator();
        while (it.hasNext()) {
            it.next().clearInk();
        }
        this.mDrawingPageList.clear();
    }

    public void computeSelectionLimits() {
        this.mSelectionStartPage = null;
        this.mSelectionEndPage = null;
        this.mSelectionLimits = null;
        int i = getDoc().mSelectionStartPage;
        int i2 = getDoc().mSelectionEndPage;
        if (i <= i2 && getDoc().getNumPages() > 0) {
            this.mSelectionStartPage = (DocPageView) getOrCreateChild(i);
            this.mSelectionEndPage = (DocPageView) getOrCreateChild(i2);
            while (i < i2 + 1) {
                ArDkSelectionLimits selectionLimits = ((DocPageView) getOrCreateChild(i)).getSelectionLimits();
                if (selectionLimits != null) {
                    ArDkSelectionLimits arDkSelectionLimits = this.mSelectionLimits;
                    if (arDkSelectionLimits == null) {
                        this.mSelectionLimits = selectionLimits;
                    } else {
                        arDkSelectionLimits.combine(selectionLimits);
                    }
                }
                i++;
            }
        }
    }

    public Point constrainScrollBy(int i, int i2) {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        int height = rect.height();
        int width = rect.width();
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        if (this.mAllPagesRect.width() <= width) {
            if ((this.mAllPagesRect.width() - scrollX) - i > width) {
                i = 0;
            }
            if (scrollX + i > 0) {
                i = -scrollX;
            }
        } else {
            if (this.mAllPagesRect.width() < scrollX + width + i) {
                i = 0;
            }
            if (scrollX + i < 0) {
                i = -scrollX;
            }
            int width2 = (this.mAllPagesRect.width() - scrollX) + i;
            if (width2 < width) {
                i = width2 - width;
            }
        }
        if (this.mAllPagesRect.height() <= height) {
            if ((this.mAllPagesRect.height() - scrollY) - i2 > height) {
                i2 = 0;
            }
            if (scrollY + i2 > 0) {
                i2 = -scrollY;
            }
        } else {
            if (scrollY + i2 < 0) {
                i2 = -scrollY;
            }
            int i3 = -scrollY;
            if ((this.mAllPagesRect.height() + i3) - i2 < height) {
                i2 = -(height - (this.mAllPagesRect.height() + i3));
            }
        }
        return new Point(i, i2);
    }

    public void doDoubleTap(float f, float f2) {
        if (!((NUIDocView) this.mHostActivity).isFullScreen()) {
            doDoubleTap2(f, f2);
        }
    }

    public void doDoubleTap2(float f, float f2) {
        if (!(this.mHostActivity instanceof NUIDocViewOther)) {
            Point eventToScreen = eventToScreen(f, f2);
            DocPageView findPageViewContainingPoint = findPageViewContainingPoint(eventToScreen.x, eventToScreen.y, false);
            if (findPageViewContainingPoint != null && findPageViewContainingPoint.canDoubleTap(eventToScreen.x, eventToScreen.y)) {
                findPageViewContainingPoint.onDoubleTap(eventToScreen.x, eventToScreen.y);
                if (canEditText()) {
                    focusInputView();
                    updateInputView();
                    showKeyboardAfterDoubleTap(eventToScreen);
                }
            }
        }
    }

    public void doPageMenu(int i) {
    }

    public boolean doPreclearCheck() {
        if (!shouldPreclearSelection() || !clearAreaSelection()) {
            return false;
        }
        Utilities.hideKeyboard(getContext());
        return true;
    }

    public void doSingleTap(float f, float f2) {
        if (!((NUIDocView) this.mHostActivity).isFullScreen() || handleFullscreenTap(f, f2)) {
            if (!(this.mHostActivity instanceof NUIDocViewOther)) {
                getDoc().closeSearch();
                NoteEditor noteEditor = this.mNoteEditor;
                if (noteEditor != null && noteEditor.isVisible()) {
                    Utilities.hideKeyboard(getContext());
                    this.mNoteEditor.hide();
                } else if (!doPreclearCheck()) {
                    Point eventToScreen = eventToScreen(f, f2);
                    DocPageView findPageViewContainingPoint = findPageViewContainingPoint(eventToScreen.x, eventToScreen.y, false);
                    if (findPageViewContainingPoint != null) {
                        this.mUpdatesPaused = true;
                        if (!onSingleTap((float) eventToScreen.x, (float) eventToScreen.y, findPageViewContainingPoint)) {
                            if (canEditText() && tapToFocus()) {
                                focusInputView();
                            }
                            if (findPageViewContainingPoint.onSingleTap(eventToScreen.x, eventToScreen.y, canEditText(), new DocPageView.ExternalLinkListener() {
                                public void handleExternalLink(int i, Rect rect) {
                                    DocView docView = DocView.this;
                                    docView.addHistory(docView.getScrollX(), DocView.this.getScrollY(), DocView.this.mScale, true);
                                    int scrollBoxToTopAmount = DocView.this.scrollBoxToTopAmount(i, new RectF((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom));
                                    DocView docView2 = DocView.this;
                                    docView2.addHistory(docView2.getScrollX(), DocView.this.getScrollY() - scrollBoxToTopAmount, DocView.this.mScale, false);
                                    DocView.this.scrollBoxToTop(i, new RectF((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom));
                                }
                            })) {
                                this.mUpdatesPaused = false;
                                onSingleTapHandled(findPageViewContainingPoint);
                            } else if (!canEditText()) {
                                this.mUpdatesPaused = false;
                            } else {
                                Handler handler = new Handler();
                                long j = 500;
                                if (((NUIDocView) this.mHostActivity).isKeyboardVisible()) {
                                    j = 0;
                                }
                                handler.postDelayed(new g$a$$ExternalSyntheticLambda1(this, findPageViewContainingPoint, eventToScreen), j);
                            }
                        }
                    }
                }
            }
        } else if (this.mDocCfgOptions.showUI()) {
            ((NUIDocView) this.mHostActivity).showUI(true);
        }
    }

    public void dropMovingPage(boolean z) {
        final int movingPageNumber = getMovingPageNumber();
        if (isValidPage(movingPageNumber)) {
            if (getNewPos(movingPageNumber) == movingPageNumber) {
                z = false;
            }
            if (!z) {
                if (canEditText()) {
                    final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            viewTreeObserver.removeOnGlobalLayoutListener(this);
                            DocView.this.doPageMenu(movingPageNumber);
                            DocView docView = DocView.this;
                            docView.mHostActivity.setCurrentPage(docView, movingPageNumber);
                        }
                    });
                }
                finishDrop();
                return;
            }
            movePage(movingPageNumber, getNewPos(movingPageNumber));
            finishDrop();
        }
    }

    public Point eventToScreen(float f, float f2) {
        int round = Math.round(f);
        int round2 = Math.round(f2);
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        return new Point(round + rect.left, round2 + rect.top);
    }

    public int findPageContainingPoint(Point point) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            Rect rect = new Rect();
            childAt.getGlobalVisibleRect(rect);
            if (rect.contains(point.x, point.y)) {
                return i;
            }
        }
        return -1;
    }

    public DocPageView findPageContainingSelection() {
        ArDkSelectionLimits selectionLimits;
        for (int i = 0; i < getPageCount(); i++) {
            DocPageView docPageView = (DocPageView) getOrCreateChild(i);
            if (docPageView != null && (selectionLimits = docPageView.selectionLimits()) != null && selectionLimits.getIsActive() && selectionLimits.getHasSelectionStart()) {
                return docPageView;
            }
        }
        return null;
    }

    public DocPageView findPageViewContainingPoint(int i, int i2, boolean z) {
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            Rect rect = new Rect();
            childAt.getGlobalVisibleRect(rect);
            if (z) {
                float f = this.mScale;
                rect.left = (int) (((float) rect.left) - ((f * 20.0f) / 2.0f));
                rect.right = (int) (((f * 20.0f) / 2.0f) + ((float) rect.right));
                rect.top = (int) (((float) rect.top) - ((f * 20.0f) / 2.0f));
                rect.bottom = (int) (((f * 20.0f) / 2.0f) + ((float) rect.bottom));
            }
            if (rect.contains(i, i2)) {
                return (DocPageView) childAt;
            }
        }
        return null;
    }

    public void finish() {
        this.mFinished = true;
        for (int i = 0; i < getChildCount(); i++) {
            ((DocPageView) getChildAt(i)).finish();
        }
        clearChildViews();
        this.mDoc = null;
    }

    public void finishDrop() {
        this.dropY = -1;
        this.mForceLayout = true;
        requestLayout();
    }

    public boolean finished() {
        return this.mFinished;
    }

    public void focusInputView() {
        if (inputView() != null) {
            inputView().setFocus();
        }
    }

    public void forceLayout() {
        if (!finished()) {
            this.mForceLayout = true;
            requestLayout();
        }
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    public float getAngle(int i, int i2, int i3, int i4) {
        float degrees = ((float) Math.toDegrees(Math.atan2((double) (i4 - i2), (double) (i3 - i)))) - 90.0f;
        return degrees < BitmapDescriptorFactory.HUE_RED ? degrees + 360.0f : degrees;
    }

    public int getBorderColor() {
        return this.mHostActivity.getBorderColor();
    }

    public SODataLeakHandlers getDataLeakHandlers() {
        return this.mDataLeakHandlers;
    }

    public ArDkDoc getDoc() {
        return this.mDoc;
    }

    public ConfigOptions getDocConfigOptions() {
        return this.mDocCfgOptions;
    }

    public boolean getDrawMode() {
        return this.mDrawMode;
    }

    public History getHistory() {
        return this.mHistory;
    }

    public int getInkLineColor() {
        ConfigOptions configOptions;
        if (this.mCurrentInkLineColor == 0 && (configOptions = this.mDocCfgOptions) != null) {
            this.mCurrentInkLineColor = configOptions.mSettingsBundle.getInt("DefaultInkAnnotationLineColorKey", 0);
        }
        if (this.mCurrentInkLineColor == 0) {
            this.mCurrentInkLineColor = -65536;
        }
        return this.mCurrentInkLineColor;
    }

    public float getInkLineThickness() {
        ConfigOptions configOptions;
        if (this.mCurrentInkLineThickness == BitmapDescriptorFactory.HUE_RED && (configOptions = this.mDocCfgOptions) != null) {
            this.mCurrentInkLineThickness = configOptions.mSettingsBundle.getFloat("DefaultInkAnnotationLineThicknessKey", BitmapDescriptorFactory.HUE_RED);
        }
        if (this.mCurrentInkLineThickness == BitmapDescriptorFactory.HUE_RED) {
            this.mCurrentInkLineThickness = 4.5f;
        }
        return this.mCurrentInkLineThickness;
    }

    public int getMostVisiblePage() {
        return this.mostVisibleChild;
    }

    public int getMovingPageNumber() {
        return -1;
    }

    public final int getNewPos(int i) {
        int i2 = this.mDropPageAbove;
        if (i2 != -1) {
            return i > i2 ? i2 : i2 - 1;
        }
        int i3 = this.mDropPageBelow;
        if (i3 == -1) {
            return getDoc().getNumPages() - 1;
        }
        return i <= i3 ? i3 : i3 + 1;
    }

    public View getOrCreateChild(int i) {
        View view = this.mChildViews.get(i);
        if (view != null) {
            return view;
        }
        View viewFromAdapter = getViewFromAdapter(i);
        this.mChildViews.append(i, viewFromAdapter);
        onScaleChild(viewFromAdapter, Float.valueOf(this.mScale));
        return viewFromAdapter;
    }

    public int getPageCount() {
        if (getAdapter() == null) {
            return 0;
        }
        return getAdapter().getCount();
    }

    public int getReflowHeight() {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        return (int) (((double) rect.height()) / ((DocPageView) getOrCreateChild(0)).getFactor());
    }

    public boolean getReflowMode() {
        return this.mReflowMode;
    }

    public int getReflowWidth() {
        return ((DocPageView) getOrCreateChild(0)).getReflowWidth();
    }

    public float getScale() {
        return this.mScale;
    }

    public float getScaleFactor() {
        return this.mScale;
    }

    public int getScrollPositionX() {
        return getScrollX();
    }

    public int getScrollPositionY() {
        return getScrollY();
    }

    public View getSelectedView() {
        return null;
    }

    public DocPageView getSelectionEndPage() {
        return this.mSelectionEndPage;
    }

    public ArDkSelectionLimits getSelectionLimits() {
        return this.mSelectionLimits;
    }

    public DocPageView getSelectionStartPage() {
        return this.mSelectionStartPage;
    }

    public int getStartPage() {
        return this.mStartPage;
    }

    public boolean getUpdatesPaused() {
        return this.mUpdatesPaused;
    }

    public View getViewFromAdapter(int i) {
        return getAdapter().getView(i, getCached(), this);
    }

    public void goToFirstPage() {
        this.goToThisPage = 0;
        this.goToThisPageLast = false;
        forceLayout();
    }

    public void goToLastPage() {
        this.goToThisPage = getPageCount() - 1;
        this.goToThisPageLast = true;
        forceLayout();
    }

    public boolean handleFullscreenTap(float f, float f2) {
        return false;
    }

    public void handleStartPage() {
        if (getStartPage() >= 0) {
            setStartPage(-1);
            ViewingState viewingState = this.mViewingState;
            if (viewingState != null) {
                setScale(viewingState.scale);
                scaleChildren();
                new Handler().post(new Runnable() {
                    public void run() {
                        DocView docView = DocView.this;
                        ViewingState viewingState = docView.mViewingState;
                        docView.scrollTo(viewingState.scrollX, viewingState.scrollY);
                        DocView.this.forceLayout();
                        final ViewTreeObserver viewTreeObserver = DocView.this.getViewTreeObserver();
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                viewTreeObserver.removeOnGlobalLayoutListener(this);
                                DocView.this.invalidate();
                                DocView.this.onEndFling();
                            }
                        });
                    }
                });
            }
        }
    }

    public boolean hasNotSavedInk() {
        ArrayList<DocPageView> arrayList = this.mDrawingPageList;
        return arrayList != null && arrayList.size() > 0;
    }

    public void hideHandles() {
        showHandle(this.mSelectionHandleTopLeft, false);
        showHandle(this.mSelectionHandleBottomRight, false);
        showResizeHandles(false);
        if (canEditText()) {
            showHandle(this.mDragHandle, false);
        }
        if (canEditText()) {
            showHandle(this.mRotateHandle, false);
        }
    }

    public void initialize(Context context) {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sodk_editor_doc_background));
        this.mGestureDetector = new GestureDetector(context, this);
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.mScroller = new Scroller(context);
        this.mSmoother = new Smoother(this, 3);
        this.mHistory = new History();
        setScrollContainer(false);
    }

    public final InputView inputView() {
        return ((NUIDocView) this.mHostActivity).getInputView();
    }

    public boolean isAtRest() {
        if (!this.mTouching && !this.mScaling && this.mScroller.isFinished()) {
            return true;
        }
        return false;
    }

    public boolean isDocModified() {
        ArDkDoc arDkDoc = this.mDoc;
        if (arDkDoc != null) {
            return arDkDoc.getHasBeenModified();
        }
        return false;
    }

    public boolean isMovingPage() {
        return false;
    }

    public final boolean isValid() {
        if (this.mFinished || this.mDoc == null || this.bitmaps == null) {
            return false;
        }
        int i = 0;
        while (true) {
            ArDkBitmap[] arDkBitmapArr = this.bitmaps;
            if (i >= arDkBitmapArr.length) {
                return true;
            }
            if (arDkBitmapArr[i] == null || arDkBitmapArr[i].bitmap == null || arDkBitmapArr[i].bitmap.isRecycled()) {
                return false;
            }
            i++;
        }
    }

    public boolean isValidPage(int i) {
        return i >= 0 && i < getPageCount();
    }

    public void layoutNow() {
        this.mForceLayout = true;
        requestLayout();
    }

    public void lineDown() {
        smoothScrollBy(0, ((-getHeight()) * 1) / 20, 100);
    }

    public void lineUp() {
        this.scrollingByKeyboard = true;
        smoothScrollBy(0, (getHeight() * 1) / 20, 100);
    }

    public float maxScale() {
        return 5.0f;
    }

    public float minScale() {
        return 0.15f;
    }

    public void moveHandlesToCorners() {
        ArDkSelectionLimits selectionLimits;
        if (!finished() && (selectionLimits = getSelectionLimits()) != null) {
            boolean z = true;
            boolean z2 = selectionLimits.getIsActive() && !selectionLimits.getIsCaret();
            boolean z3 = selectionLimits.getIsActive() && getDoc().getSelectionCanBeResized() && !selectionLimits.getIsCaret();
            boolean z4 = selectionLimits.getIsActive() && getDoc().getSelectionCanBeAbsolutelyPositioned();
            if (!selectionLimits.getIsActive() || !getDoc().getSelectionCanBeRotated()) {
                z = false;
            }
            if (z3) {
                positionHandle(this.mResizeHandleTopLeft, this.mSelectionStartPage, (int) selectionLimits.getBox().left, (int) selectionLimits.getBox().top);
                positionHandle(this.mResizeHandleTopRight, this.mSelectionStartPage, (int) selectionLimits.getBox().right, (int) selectionLimits.getBox().top);
                positionHandle(this.mResizeHandleBottomLeft, this.mSelectionStartPage, (int) selectionLimits.getBox().left, (int) selectionLimits.getBox().bottom);
                positionHandle(this.mResizeHandleBottomRight, this.mSelectionStartPage, (int) selectionLimits.getBox().right, (int) selectionLimits.getBox().bottom);
            }
            if (z2 && !this.mDragging) {
                positionHandle(this.mSelectionHandleTopLeft, this.mSelectionStartPage, (int) selectionLimits.getStart().x, (int) selectionLimits.getStart().y);
                positionHandle(this.mSelectionHandleBottomRight, this.mSelectionEndPage, (int) selectionLimits.getEnd().x, (int) selectionLimits.getEnd().y);
            }
            if (z4) {
                positionHandle(this.mDragHandle, this.mSelectionStartPage, ((int) (selectionLimits.getBox().left + selectionLimits.getBox().right)) / 2, (int) selectionLimits.getBox().bottom);
            }
            if (z) {
                positionHandle(this.mRotateHandle, this.mSelectionStartPage, ((int) (selectionLimits.getBox().left + selectionLimits.getBox().right)) / 2, (int) selectionLimits.getBox().top);
            }
        }
    }

    public void movePage(int i, int i2) {
        if (isValidPage(i) && isValidPage(i2)) {
            ((SODoc) getDoc()).movePage(i, i2);
            onPageMoved(i2);
        }
    }

    public void moveResizingView(int i, int i2, int i3, int i4) {
        Rect screenRect = this.mSelectionStartPage.screenRect();
        Rect rect = new Rect(i, i2, i + i3, i2 + i4);
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        rect.offset(iArr[0], iArr[1]);
        int i5 = rect.left;
        int i6 = screenRect.left;
        if (i5 < i6) {
            i += i6 - i5;
        }
        int i7 = rect.right;
        int i8 = screenRect.right;
        if (i7 > i8) {
            i -= i7 - i8;
        }
        int i9 = rect.top;
        int i10 = screenRect.top;
        if (i9 < i10) {
            i2 += i10 - i9;
        }
        int i11 = rect.bottom;
        int i12 = screenRect.bottom;
        if (i11 > i12) {
            i2 -= i11 - i12;
        }
        int i13 = i + i3;
        int i14 = i2 + i4;
        this.mResizeRect.set(i, i2, i13, i14);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mResizingView.getLayoutParams();
        int height = i14 - getHeight();
        int i15 = height > 0 ? -height : 0;
        int width = i13 - getWidth();
        layoutParams.setMargins(i, i2, width > 0 ? -width : 0, i15);
        layoutParams.width = i3;
        layoutParams.height = i4;
        this.mResizingView.setLayoutParams(layoutParams);
        this.mResizingView.invalidate();
        this.mResizingView.setVisibility(View.VISIBLE);
    }

    public void onConfigurationChange() {
        for (int i = 0; i < getPageCount(); i++) {
            DocPageView docPageView = (DocPageView) getOrCreateChild(i);
            if (docPageView.getParent() != null && docPageView.isShown()) {
                docPageView.resetBackground();
            }
        }
        this.mForceColumnCount = this.mLastLayoutColumns;
        ((NUIDocView) this.mHostActivity).triggerOrientationChange();
    }

    public boolean onDown(MotionEvent motionEvent) {
        if (!this.mScroller.isFinished()) {
            this.mScrollingStopped = true;
            this.mScroller.forceFinished(true);
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x012c  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x013b  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x015f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDrag(DragHandle r14) {
        /*
            r13 = this;
            android.graphics.Point r0 = r14.getPosition()
            android.graphics.Point r0 = r13.viewToScreen(r0)
            int r1 = r0.x
            int r2 = r0.y
            r3 = 0
            com.artifex.sonui.editor.DocPageView r1 = r13.findPageViewContainingPoint(r1, r2, r3)
            if (r1 != 0) goto L_0x0014
            return
        L_0x0014:
            boolean r2 = r14.isSelectionKind()
            r4 = 2
            if (r2 == 0) goto L_0x0064
            boolean r2 = r13.canSelectionSpanPages()
            if (r2 != 0) goto L_0x0037
            int r2 = r1.getPageNumber()
            com.artifex.sonui.editor.DocPageView r3 = r13.mSelectionStartPage
            int r3 = r3.getPageNumber()
            if (r2 == r3) goto L_0x002e
            return
        L_0x002e:
            com.artifex.sonui.editor.DocPageView r3 = r13.mSelectionEndPage
            int r3 = r3.getPageNumber()
            if (r2 == r3) goto L_0x0037
            return
        L_0x0037:
            android.graphics.Point r0 = r13.adjustDragHandle(r14, r0)
            int r2 = r14.getKind()
            r3 = 1
            if (r2 != r3) goto L_0x0059
            int r2 = r0.x
            int r3 = r14.getWidth()
            int r3 = r3 + r2
            r0.x = r3
            int r2 = r0.y
            int r14 = r14.getHeight()
            int r14 = r14 + r2
            r0.y = r14
            r1.setSelectionStart(r0)
            goto L_0x01eb
        L_0x0059:
            int r14 = r14.getKind()
            if (r14 != r4) goto L_0x01eb
            r1.setSelectionEnd(r0)
            goto L_0x01eb
        L_0x0064:
            boolean r0 = r14.isResizeKind()
            if (r0 == 0) goto L_0x016f
            android.graphics.Point r0 = r13.mResizeOrigTopLeft
            int r1 = r0.y
            int r0 = r0.x
            android.graphics.Point r2 = r13.mResizeOrigBottomRight
            int r5 = r2.y
            int r2 = r2.x
            int r6 = r14.getKind()
            r7 = 6
            r8 = 5
            r9 = 4
            r10 = 3
            if (r6 == r10) goto L_0x00d0
            if (r6 == r9) goto L_0x00b7
            if (r6 == r8) goto L_0x009f
            if (r6 == r7) goto L_0x0087
            goto L_0x00e8
        L_0x0087:
            android.graphics.Point r2 = r14.getPosition()
            int r2 = r2.y
            int r5 = r14.getMeasuredHeight()
            int r5 = r5 / r4
            int r5 = r5 + r2
            android.graphics.Point r2 = r14.getPosition()
            int r2 = r2.x
            int r6 = r14.getMeasuredWidth()
            int r6 = r6 / r4
            goto L_0x00ce
        L_0x009f:
            android.graphics.Point r0 = r14.getPosition()
            int r0 = r0.y
            int r5 = r14.getMeasuredHeight()
            int r5 = r5 / r4
            int r5 = r5 + r0
            android.graphics.Point r0 = r14.getPosition()
            int r0 = r0.x
            int r6 = r14.getMeasuredWidth()
            int r6 = r6 / r4
            goto L_0x00e7
        L_0x00b7:
            android.graphics.Point r1 = r14.getPosition()
            int r1 = r1.y
            int r2 = r14.getMeasuredHeight()
            int r2 = r2 / r4
            int r1 = r1 + r2
            android.graphics.Point r2 = r14.getPosition()
            int r2 = r2.x
            int r6 = r14.getMeasuredWidth()
            int r6 = r6 / r4
        L_0x00ce:
            int r2 = r2 + r6
            goto L_0x00e8
        L_0x00d0:
            android.graphics.Point r0 = r14.getPosition()
            int r0 = r0.y
            int r1 = r14.getMeasuredHeight()
            int r1 = r1 / r4
            int r1 = r1 + r0
            android.graphics.Point r0 = r14.getPosition()
            int r0 = r0.x
            int r6 = r14.getMeasuredWidth()
            int r6 = r6 / r4
        L_0x00e7:
            int r0 = r0 + r6
        L_0x00e8:
            int r2 = r2 - r0
            int r5 = r5 - r1
            android.graphics.PointF r0 = r13.mNatDim
            float r1 = r0.x
            r4 = 0
            r6 = 100
            int r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r1 <= 0) goto L_0x012c
            float r0 = r0.y
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x012c
            android.graphics.Point r0 = r13.mResizeOrigBottomRight
            int r1 = r0.x
            android.graphics.Point r4 = r13.mResizeOrigTopLeft
            int r11 = r4.x
            int r1 = r1 - r11
            int r0 = r0.y
            int r4 = r4.y
            int r0 = r0 - r4
            int r4 = r1 * r1
            int r11 = r0 * r0
            int r11 = r11 + r4
            double r11 = (double) r11
            double r11 = java.lang.Math.sqrt(r11)
            int r2 = r2 * r2
            int r5 = r5 * r5
            int r5 = r5 + r2
            double r4 = (double) r5
            double r4 = java.lang.Math.sqrt(r4)
            double r4 = r4 / r11
            double r1 = (double) r1
            double r1 = r1 * r4
            int r1 = (int) r1
            double r11 = (double) r0
            double r11 = r11 * r4
            int r5 = (int) r11
            if (r1 < r6) goto L_0x01eb
            if (r5 >= r6) goto L_0x0135
            goto L_0x01eb
        L_0x012c:
            if (r2 >= r6) goto L_0x0130
            r2 = 100
        L_0x0130:
            r1 = r2
            if (r5 >= r6) goto L_0x0135
            r5 = 100
        L_0x0135:
            int r14 = r14.getKind()
            if (r14 == r10) goto L_0x015f
            if (r14 == r9) goto L_0x0155
            if (r14 == r8) goto L_0x014a
            if (r14 == r7) goto L_0x0143
            r14 = 0
            goto L_0x016a
        L_0x0143:
            android.graphics.Point r14 = r13.mResizeOrigTopLeft
            int r3 = r14.x
            int r14 = r14.y
            goto L_0x016a
        L_0x014a:
            android.graphics.Point r14 = r13.mResizeOrigBottomRight
            int r14 = r14.x
            int r3 = r14 - r1
            android.graphics.Point r14 = r13.mResizeOrigTopLeft
            int r14 = r14.y
            goto L_0x016a
        L_0x0155:
            android.graphics.Point r14 = r13.mResizeOrigTopLeft
            int r14 = r14.x
            android.graphics.Point r0 = r13.mResizeOrigBottomRight
            int r0 = r0.y
            r3 = r14
            goto L_0x0168
        L_0x015f:
            android.graphics.Point r14 = r13.mResizeOrigBottomRight
            int r0 = r14.x
            int r0 = r0 - r1
            int r14 = r14.y
            r3 = r0
            r0 = r14
        L_0x0168:
            int r14 = r0 - r5
        L_0x016a:
            r13.moveResizingView(r3, r14, r1, r5)
            goto L_0x01eb
        L_0x016f:
            boolean r0 = r14.isDragKind()
            if (r0 == 0) goto L_0x01a7
            android.graphics.Point r0 = r13.mResizeOrigTopLeft
            int r0 = r0.x
            android.graphics.Point r1 = r14.getPosition()
            int r1 = r1.x
            android.graphics.Point r2 = r13.mDragOrigLocation
            int r2 = r2.x
            int r1 = r1 - r2
            int r1 = r1 + r0
            android.graphics.Point r0 = r13.mResizeOrigTopLeft
            int r0 = r0.y
            android.graphics.Point r14 = r14.getPosition()
            int r14 = r14.y
            android.graphics.Point r2 = r13.mDragOrigLocation
            int r2 = r2.y
            int r14 = r14 - r2
            int r14 = r14 + r0
            android.graphics.Point r0 = r13.mResizeOrigBottomRight
            int r2 = r0.x
            android.graphics.Point r3 = r13.mResizeOrigTopLeft
            int r4 = r3.x
            int r2 = r2 - r4
            int r0 = r0.y
            int r3 = r3.y
            int r0 = r0 - r3
            r13.moveResizingView(r1, r14, r2, r0)
            goto L_0x01eb
        L_0x01a7:
            boolean r0 = r14.isRotateKind()
            if (r0 == 0) goto L_0x01eb
            android.graphics.Point r0 = r14.getPosition()
            int r0 = r0.x
            android.graphics.Point r14 = r14.getPosition()
            int r14 = r14.y
            android.graphics.Point r1 = r13.mResizeOrigTopLeft
            int r2 = r1.x
            android.graphics.Point r3 = r13.mResizeOrigBottomRight
            int r5 = r3.x
            int r2 = r2 + r5
            int r2 = r2 / r4
            int r1 = r1.y
            int r3 = r3.y
            int r1 = r1 + r3
            int r1 = r1 / r4
            float r14 = r13.getAngle(r0, r14, r2, r1)
            float r0 = r13.mNAdditionalAngle
            float r0 = r14 - r0
            float r0 = java.lang.Math.abs(r0)
            r1 = 1073741824(0x40000000, float:2.0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x01eb
            r13.mNAdditionalAngle = r14
            android.widget.ImageView r0 = r13.mResizingView
            r0.setRotation(r14)
            android.widget.ImageView r14 = r13.mResizingView
            android.view.ViewGroup$LayoutParams r0 = r14.getLayoutParams()
            r14.setLayoutParams(r0)
        L_0x01eb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.DocView.onDrag(com.artifex.sonui.editor.DragHandle):void");
    }

    public void onDrawMode() {
        boolean z = !this.mDrawMode;
        this.mDrawMode = z;
        if (!z) {
            saveInk();
        }
        getDoc().clearSelection();
        onSelectionChanged();
    }

    public void onEndDrag(DragHandle dragHandle) {
        if (dragHandle.isResizeKind() && this.mDraggingObjectPageBounds != null) {
            Rect rect = this.mResizeRect;
            int i = rect.left;
            Rect rect2 = this.mResizeOrigRect;
            int i2 = i - rect2.left;
            int i3 = rect.top - rect2.top;
            int i4 = rect.bottom - rect2.bottom;
            Point viewToPage = this.mSelectionStartPage.viewToPage(i2, i3);
            Point viewToPage2 = this.mSelectionStartPage.viewToPage(rect.right - rect2.right, i4);
            RectF rectF = this.mDraggingObjectPageBounds;
            rectF.left += (float) viewToPage.x;
            rectF.top += (float) viewToPage.y;
            rectF.right += (float) viewToPage2.x;
            rectF.bottom += (float) viewToPage2.y;
            setSelectionBoxBounds(rectF);
        } else if (dragHandle.isDragKind() && this.mDraggingObjectPageBounds != null && this.mDragging) {
            Rect rect3 = this.mResizeRect;
            int i5 = rect3.left;
            Rect rect4 = this.mResizeOrigRect;
            Point viewToPage3 = this.mSelectionStartPage.viewToPage(i5 - rect4.left, rect3.top - rect4.top);
            this.mDraggingObjectPageBounds.offset((float) viewToPage3.x, (float) viewToPage3.y);
            setSelectionBoxBounds(this.mDraggingObjectPageBounds);
        } else if (dragHandle.isRotateKind()) {
            ((SODoc) getDoc()).setSelectionRotation(this.mRotateAngle + this.mNAdditionalAngle);
        } else if (dragHandle.isSelectionKind()) {
            this.mDragging = false;
            moveHandlesToCorners();
            updateInputView();
        }
        this.mDraggingObjectPageBounds = null;
        this.mDragging = false;
    }

    public void onEndFling() {
        int i;
        if (!finished() && this.flinging && (i = this.mostVisibleChild) >= 0) {
            this.mHostActivity.setCurrentPage(this, i);
        }
    }

    public void onEndRenderPass() {
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.mScaling || !this.mScroller.isFinished()) {
            return true;
        }
        this.flinging = true;
        int i = (int) f2;
        int i2 = !allowXScroll() ? 0 : (int) f;
        this.mSmoother.clear();
        this.mScroller.forceFinished(true);
        this.mScrollerLastY = 0;
        this.mScrollerLastX = 0;
        this.mScroller.fling(0, 0, i2, i, DownloadRequest.Priority.CRITICAL, Integer.MAX_VALUE, DownloadRequest.Priority.CRITICAL, Integer.MAX_VALUE);
        post(this);
        return true;
    }

    public void onFoundText(int i, RectF rectF) {
        scrollBoxIntoView(i, rectF, true);
    }

    public void onFullscreen(boolean z) {
        if (z) {
            getDoc().clearSelection();
        }
        for (int i = 0; i < getPageCount(); i++) {
            ((DocPageView) getOrCreateChild(i)).onFullscreen(z);
        }
    }

    public void onHidePages() {
        int integer = getContext().getResources().getInteger(R.integer.sodk_editor_page_width_percentage);
        onSizeChange(((float) (getContext().getResources().getInteger(R.integer.sodk_editor_pagelist_width_percentage) + integer)) / ((float) integer));
        this.mPagesShowing = false;
    }

    public void onHistoryItem(History.HistoryItem historyItem) {
        this.mXScroll = 0;
        this.mYScroll = 0;
        setScrollX(historyItem.getScrollX());
        setScrollY(historyItem.getScrollY());
        this.mScale = historyItem.getScale();
        forceLayout();
        new Handler().post(new Runnable() {
            public void run() {
                DocView docView = DocView.this;
                int i = docView.mostVisibleChild;
                if (i >= 0) {
                    docView.mHostActivity.setCurrentPage(docView, i);
                }
            }
        });
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        boolean z2;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int height;
        boolean z3;
        int i12;
        if (!finished() && isShown()) {
            int pageCount = getPageCount();
            if (this.mReflowMode) {
                pageCount = this.mDoc.getNumPages();
            }
            if (getPageCount() != 0) {
                if (this.mViewSmoothScrollStartX != null) {
                    int currX = this.mScroller.getCurrX();
                    int intValue = this.mViewSmoothScrollStartX.intValue() - getScrollX();
                    int i13 = this.mXScroll;
                    this.mXScroll = (currX - (intValue + i13)) + i13;
                    int currY = this.mScroller.getCurrY();
                    int intValue2 = this.mViewSmoothScrollStartY.intValue() - getScrollY();
                    int i14 = this.mYScroll;
                    this.mYScroll = (currY - (intValue2 + i14)) + i14;
                }
                scrollBy(-this.mXScroll, -this.mYScroll);
                this.mYScroll = 0;
                this.mXScroll = 0;
                if (shouldLayout()) {
                    this.mViewportOrigin.set(getScrollX(), getScrollY());
                    getGlobalVisibleRect(this.mViewport);
                    Rect rect = this.mViewport;
                    Point point = this.mViewportOrigin;
                    rect.offsetTo(point.x, point.y);
                    this.unscaledMaxw = 0;
                    for (int i15 = 0; i15 < getPageCount(); i15++) {
                        DocPageView docPageView = (DocPageView) getOrCreateChild(i15);
                        if (!(docPageView == null || docPageView.getPage() == null)) {
                            this.unscaledMaxw = Math.max(this.unscaledMaxw, docPageView.getUnscaledWidth());
                        }
                    }
                    float f = this.mScale;
                    int i16 = (int) (((float) this.unscaledMaxw) * f);
                    if (this.mPressing) {
                        i5 = this.mLastLayoutColumns;
                    } else {
                        int i17 = this.mForceColumnCount;
                        if (i17 != -1) {
                            i5 = i17;
                        } else {
                            int i18 = this.mLastLayoutColumns;
                            if (this.mLastLayoutScale != f) {
                                this.mLastLayoutScale = f;
                                int i19 = (int) (f * 20.0f);
                                i5 = (int) Math.round(((double) (this.mViewport.width() + i19)) / ((double) (i16 + i19)));
                            } else {
                                i5 = i18;
                            }
                            if (this.mReflowMode) {
                                i5 = 1;
                            }
                        }
                    }
                    if (i5 > pageCount) {
                        i5 = pageCount;
                    }
                    this.mostVisibleChild = -1;
                    this.mAllPagesRect.setEmpty();
                    if (isMovingPage()) {
                        DocPageView docPageView2 = (DocPageView) getOrCreateChild(getMovingPageNumber());
                        i6 = (docPageView2 == null || docPageView2.getPage() == null) ? 0 : docPageView2.getUnscaledHeight();
                        this.mDropPageAbove = -1;
                        this.mDropPageBelow = -1;
                    } else {
                        i6 = 0;
                    }
                    int max = Math.max(getPageCount(), getChildCount());
                    int i20 = 0;
                    int i21 = 0;
                    int i22 = -1;
                    int i23 = 0;
                    int i24 = 0;
                    int i25 = -1;
                    while (i20 < max) {
                        DocPageView docPageView3 = (DocPageView) getOrCreateChild(i20);
                        if (docPageView3 == null || docPageView3.getPage() == null) {
                            i8 = max;
                            ArDkDoc arDkDoc = this.mDoc;
                            Objects.requireNonNull(arDkDoc);
                            if (i20 > 0 && i20 < arDkDoc.mPages.size()) {
                                arDkDoc.mPages.remove(i20);
                            }
                        } else {
                            docPageView3.setDocView(this);
                            if (!isMovingPage() || docPageView3.getPageNumber() != getMovingPageNumber()) {
                                int unscaledWidth = docPageView3.getUnscaledWidth();
                                int unscaledHeight = docPageView3.getUnscaledHeight();
                                int i26 = (this.unscaledMaxw + 20) * i23;
                                int i27 = unscaledWidth + i26;
                                int i28 = i24 + unscaledHeight;
                                int max2 = Math.max(i21, unscaledHeight);
                                i8 = max;
                                if (isMovingPage()) {
                                    i10 = max2;
                                    float f2 = this.mScale;
                                    int i29 = (int) (((float) i24) * f2);
                                    i9 = unscaledHeight;
                                    int i30 = (int) (((float) i28) * f2);
                                    if (i22 == -1) {
                                        i12 = i20;
                                        z3 = this.dropY <= i29;
                                    } else {
                                        i12 = i22;
                                        z3 = false;
                                    }
                                    int i31 = this.dropY;
                                    if ((i31 < i29 || i31 > (i29 + i30) / 2) ? z3 : true) {
                                        this.mDropPageAbove = i20;
                                        i24 += i6;
                                        i28 += i6;
                                    }
                                    i22 = i12;
                                } else {
                                    i10 = max2;
                                    i9 = unscaledHeight;
                                }
                                float f3 = (float) i26;
                                float f4 = this.mScale;
                                int i32 = (int) (f3 * f4);
                                int i33 = (int) (((float) i24) * f4);
                                int i34 = (int) (((float) i27) * f4);
                                int i35 = (int) (((float) i28) * f4);
                                this.mChildRect.set(i32, i33, i34, i35);
                                docPageView3.setChildRect(this.mChildRect);
                                if (this.mAllPagesRect.isEmpty()) {
                                    this.mAllPagesRect.set(this.mChildRect);
                                } else {
                                    this.mAllPagesRect.union(this.mChildRect);
                                }
                                if (!this.mChildRect.intersect(this.mViewport) || i20 >= pageCount) {
                                    removeViewInLayout(docPageView3);
                                } else {
                                    if (docPageView3.getParent() == null) {
                                        docPageView3.clearContent();
                                        addChildToLayout(docPageView3);
                                    }
                                    int width = centerPagesHorizontally() ? (getWidth() - this.mChildRect.width()) / 2 : 0;
                                    docPageView3.layout(i32 + width, i33, i34 + width, i35);
                                    docPageView3.invalidate();
                                    if (docPageView3.getGlobalVisibleRect(this.mostVisibleRect) && (height = this.mostVisibleRect.height()) > i25) {
                                        this.mostVisibleChild = i20;
                                        i25 = height;
                                    }
                                }
                                i23++;
                                if (i23 >= i5) {
                                    i11 = 20;
                                    i24 = (getReflowMode() ? i24 + i9 : i24 + i10) + 20;
                                    i23 = 0;
                                    i10 = 0;
                                } else {
                                    i11 = 20;
                                }
                                if (isMovingPage()) {
                                    int i36 = (int) (((float) i11) * this.mScale);
                                    int i37 = this.dropY;
                                    if (i37 >= (i33 + i35) / 2 && i37 <= i35 + i36) {
                                        this.mDropPageBelow = i20;
                                        i24 += i6;
                                    }
                                }
                                i21 = i10;
                            } else {
                                removeViewInLayout(docPageView3);
                                i8 = max;
                            }
                        }
                        i20++;
                        max = i8;
                    }
                    setMostVisiblePage();
                    if (!this.mScaling || i5 < 1 || (i7 = this.mLastLayoutColumns) < 1 || i7 == i5) {
                        z2 = false;
                    } else {
                        scrollBy(this.mAllPagesRect.centerX() - this.mViewport.centerX(), 0);
                        int i38 = this.lastMostVisibleChild;
                        if (i38 != -1) {
                            scrollToPage(i38, true);
                        }
                        z2 = true;
                    }
                    this.mLastLayoutColumns = i5;
                    this.mLastAllPagesRect.set(this.mAllPagesRect);
                    this.lastMostVisibleChild = this.mostVisibleChild;
                    moveHandlesToCorners();
                    NoteEditor noteEditor = this.mNoteEditor;
                    if (noteEditor != null) {
                        noteEditor.move();
                    }
                    if (this.once) {
                        DocPageView docPageView4 = (DocPageView) getOrCreateChild(0);
                        this.once = false;
                    }
                    handleStartPage();
                    triggerRender();
                    if (z2) {
                        new Handler().post(new Runnable() {
                            public void run() {
                                DocView.this.layoutNow();
                            }
                        });
                    }
                    final int i39 = this.goToThisPage;
                    if (i39 != -1) {
                        final boolean z4 = this.goToThisPageLast;
                        this.goToThisPage = -1;
                        new Handler().post(new Runnable() {
                            public void run() {
                                DocView.this.scrollToPage(i39, z4, false);
                            }
                        });
                    }
                    reportViewChanges();
                }
            }
        }
    }

    public void onLayoutChanged() {
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public void onLongPressMoving(MotionEvent motionEvent) {
    }

    public void onLongPressRelease() {
    }

    public void onMovePage(int i, int i2) {
        if (isMovingPage()) {
            this.dropY = getScrollY() + i2;
            this.mForceLayout = true;
            requestLayout();
        }
    }

    public void onNextPrevTrackedChange() {
        this.mScrollRequested = true;
        Utilities.hideKeyboard(getContext());
    }

    public void onOrientationChange() {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        int width = rect.width();
        int width2 = this.mAllPagesRect.width();
        if (width2 <= 0 || width <= 0) {
            requestLayout();
            final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    DocView.this.onOrientationChange();
                }
            });
        } else if (this.mLastViewPortWidth != width) {
            this.mLastViewPortWidth = width;
            if (this.mReflowMode || this.mLastLayoutColumns != 0 || width2 < width) {
                final float f = ((float) width) / ((float) width2);
                final int scrollY = getScrollY();
                this.mScale *= f;
                scaleChildren();
                requestLayout();
                final ViewTreeObserver viewTreeObserver2 = getViewTreeObserver();
                viewTreeObserver2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        viewTreeObserver2.removeOnGlobalLayoutListener(this);
                        int i = scrollY;
                        DocView.this.scrollBy(0, -(i - ((int) (f * ((float) i)))));
                        DocView docView = DocView.this;
                        docView.mForceColumnCount = -1;
                        docView.layoutNow();
                    }
                });
                return;
            }
            this.mForceColumnCount = -1;
            requestLayout();
        }
    }

    public void onPageMoved(int i) {
    }

    public void onPreLayout() {
    }

    public void onReflowScale() {
        if (this.mReflowMode) {
            DocPageView docPageView = (DocPageView) getOrCreateChild(0);
            int numPages = this.mDoc.getNumPages();
            for (int i = 1; i < numPages; i++) {
                DocPageView docPageView2 = (DocPageView) getOrCreateChild(i);
                if (!(docPageView2 == null || docPageView2.getPage() == null)) {
                    docPageView2.onReflowScale(docPageView);
                }
            }
        }
    }

    public void onReloadFile() {
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        onScaleInternal(scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY(), Boolean.FALSE);
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        onScaleBeginInternal();
        return true;
    }

    public final boolean onScaleBeginInternal() {
        this.mScaling = true;
        hideHandles();
        if (this.mReflowWidth == -1) {
            this.mReflowWidth = getReflowWidth();
        }
        this.mYScroll = 0;
        this.mXScroll = 0;
        return true;
    }

    public void onScaleChild(View view, Float f) {
        ((DocPageView) view).setNewScale(f.floatValue());
    }

    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        onScaleEndInternal();
    }

    public final void onScaleEndInternal() {
        showHandles();
        Rect rect = this.mAllPagesRect;
        if (rect == null || rect.width() == 0 || this.mAllPagesRect.height() == 0) {
            this.mScaling = false;
            return;
        }
        Rect rect2 = new Rect();
        getGlobalVisibleRect(rect2);
        if (this.mReflowMode || this.mLastLayoutColumns > 1 || this.mAllPagesRect.width() < rect2.width()) {
            ArDkDoc doc = getDoc();
            getPageCount();
            if (this.mReflowMode) {
                this.mDoc.getNumPages();
            }
            float width = ((float) rect2.width()) / ((float) this.mAllPagesRect.width());
            float height = ((float) rect2.height()) / ((float) this.mAllPagesRect.height());
            if (this.mReflowMode) {
                NUIDocView.currentNUIDocView().onReflowScale();
                final int scrollX = getScrollX();
                final int scrollY = getScrollY();
                float f = 1.0f;
                if (NUIDocView.currentNUIDocView().isPageListVisible()) {
                    f = ((float) getContext().getResources().getInteger(R.integer.sodk_editor_page_width_percentage)) / 100.0f;
                }
                final float f2 = (f * ((float) this.mReflowWidth)) / this.mScale;
                SODoc sODoc = (SODoc) doc;
                int i = sODoc.mFlowMode;
                sODoc.mFlowMode = i;
                sODoc.setFlowModeInternal(i, f2, (float) getReflowHeight());
                ViewTreeObserver viewTreeObserver = getViewTreeObserver();
                final ViewTreeObserver viewTreeObserver2 = viewTreeObserver;
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        viewTreeObserver2.removeOnGlobalLayoutListener(this);
                        DocView docView = DocView.this;
                        float f = docView.mLastReflowWidth / f2;
                        int i = scrollY;
                        docView.scrollBy(-scrollX, -(i - ((int) (f * ((float) i)))));
                        DocView.this.mLastReflowWidth = f2;
                    }
                });
                this.mScaling = false;
                return;
            }
            float min = Math.min(width, height);
            int i2 = this.unscaledMaxw;
            int i3 = this.mLastLayoutColumns;
            this.mScale = ((float) rect2.width()) / ((float) (((i3 - 1) * 20) + (i2 * i3)));
            scaleChildren();
            this.mXScroll = 0;
            this.mYScroll = 0;
            final int height2 = (int) ((((float) ((rect2.height() / 2) + getScrollY())) * width) - ((float) (rect2.height() / 2)));
            if (((int) (min * ((float) this.mLastAllPagesRect.height()))) < rect2.height()) {
                height2 = 0;
            }
            final ViewTreeObserver viewTreeObserver3 = getViewTreeObserver();
            viewTreeObserver3.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    viewTreeObserver3.removeOnGlobalLayoutListener(this);
                    DocView.this.scrollAfterScaleEnd(0, height2);
                    DocView.this.requestLayout();
                }
            });
            requestLayout();
            this.mScaling = false;
            return;
        }
        this.mScaling = false;
    }

    public final boolean onScaleInternal(float f, float f2, float f3, Boolean bool) {
        if (this.mPressing) {
            return true;
        }
        float f4 = this.mScale;
        if (bool.booleanValue()) {
            this.mScale = Math.min(Math.max(f, minScale()), maxScale());
        } else {
            this.mScale = Math.min(Math.max(this.mScale * f, minScale()), maxScale());
        }
        if (this.mScale == f4) {
            return true;
        }
        scaleChildren();
        if (!bool.booleanValue()) {
            int scrollX = getScrollX() + ((int) f2);
            int scrollY = getScrollY() + ((int) f3);
            float f5 = (float) scrollX;
            this.mXScroll = (int) ((f5 - (f5 * f)) + ((float) this.mXScroll));
            float f6 = (float) scrollY;
            this.mYScroll = (int) ((f6 - (f * f6)) + ((float) this.mYScroll));
        }
        requestLayout();
        return true;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.mScaling || this.mPressing || this.mDragging || !this.mScroller.isFinished()) {
            return true;
        }
        if (this.mReflowMode) {
            f = BitmapDescriptorFactory.HUE_RED;
        }
        this.mXScroll = (int) (((float) this.mXScroll) - f);
        this.mYScroll = (int) (((float) this.mYScroll) - f2);
        requestLayout();
        return true;
    }

    public void onSelectionChanged() {
        if (getDoc() != null) {
            computeSelectionLimits();
            NoteEditor noteEditor = this.mNoteEditor;
            if (noteEditor == null || !noteEditor.isVisible()) {
                scrollSelectionIntoView();
            }
            boolean z = false;
            for (int i = 0; i < getPageCount(); i++) {
                DocPageView docPageView = (DocPageView) getOrCreateChild(i);
                if (docPageView.sizeViewToPage()) {
                    z = true;
                }
                docPageView.setNewScale(this.mScale);
            }
            if (z) {
                forceLayout();
            }
            if (getSelectionLimits() != null) {
                boolean selectionIsAlterableTextSelection = getDoc().getSelectionIsAlterableTextSelection();
                Boolean bool = this.mLastSelIsAltText;
                if (bool == null) {
                    this.mLastSelIsAltText = new Boolean(selectionIsAlterableTextSelection);
                } else if (selectionIsAlterableTextSelection != bool.booleanValue()) {
                    this.mLastSelIsAltText = new Boolean(selectionIsAlterableTextSelection);
                    updateInputView();
                }
            } else {
                this.mLastSelIsAltText = null;
            }
            updateDragHandles();
            updateReview();
        }
    }

    public void onSelectionDelete() {
    }

    public void onShowKeyboard(boolean z) {
        if (z) {
            if (!(this.scrollToHere == null || this.scrollToHerePage == null)) {
                Point point = this.scrollToHere;
                int i = point.x;
                int i2 = point.y;
                scrollBoxIntoView(this.scrollToHerePage.getPageNumber(), new RectF((float) i, (float) i2, (float) (i + 1), (float) (i2 + 1)));
                this.scrollToHere = null;
                this.scrollToHerePage = null;
            }
            if (this.scrollToSelection) {
                scrollSelectionIntoView();
                this.scrollToSelection = false;
            }
        } else {
            NoteEditor noteEditor = this.mNoteEditor;
            if (noteEditor != null) {
                noteEditor.preMoving();
            }
            this.mForceLayout = true;
            requestLayout();
        }
        ShowKeyboardListener showKeyboardListener2 = this.showKeyboardListener;
        if (showKeyboardListener2 != null) {
            showKeyboardListener2.onShow(z);
        }
    }

    public void onShowPages() {
        int integer = getContext().getResources().getInteger(R.integer.sodk_editor_page_width_percentage);
        onSizeChange(((float) integer) / ((float) (integer + getContext().getResources().getInteger(R.integer.sodk_editor_pagelist_width_percentage))));
        this.mPagesShowing = true;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTap(float f, float f2, DocPageView docPageView) {
        return false;
    }

    public void onSingleTapHandled(DocPageView docPageView) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.mLastTapTime;
        if (j == 0 || currentTimeMillis - j >= 300) {
            this.mLastTapTime = currentTimeMillis;
            this.lastTapX = motionEvent.getX();
            float y = motionEvent.getY();
            this.lastTapY = y;
            if (!this.mScrollingStopped) {
                doSingleTap(this.lastTapX, y);
            }
            this.mTapStatus = 1;
        } else {
            this.mTapStatus = 2;
            if (!this.mScrollingStopped) {
                doDoubleTap(this.lastTapX, this.lastTapY);
            }
            this.mLastTapTime = 0;
        }
        this.mScrollingStopped = false;
        return false;
    }

    public final void onSizeChange(float f) {
        this.mScale *= f;
        scaleChildren();
        scrollTo(getScrollX(), (int) (((float) getScrollY()) * f));
        requestLayout();
    }

    public void onStartDrag(DragHandle dragHandle) {
        this.mDragging = true;
        if (!dragHandle.isSelectionKind()) {
            DocPageView docPageView = this.mSelectionStartPage;
            if (docPageView == null || docPageView.getSelectionLimits() == null) {
                this.mDragging = false;
                return;
            }
            this.mDraggingObjectPageBounds = this.mSelectionStartPage.getSelectionLimits().getBox();
            ArDkBitmap selectionAsBitmap = ((SODoc) getDoc()).getSelectionAsBitmap();
            this.mResizingBitmap = selectionAsBitmap;
            this.mResizingView.setImageBitmap(selectionAsBitmap != null ? selectionAsBitmap.bitmap : null);
            ArDkSelectionLimits selectionLimits = getSelectionLimits();
            Point pageToView = this.mSelectionStartPage.pageToView((int) selectionLimits.getBox().left, (int) selectionLimits.getBox().top);
            this.mResizeOrigTopLeft = pageToView;
            pageToView.offset(this.mSelectionStartPage.getLeft(), this.mSelectionStartPage.getTop());
            this.mResizeOrigTopLeft.offset(-getScrollX(), -getScrollY());
            Point pageToView2 = this.mSelectionStartPage.pageToView((int) selectionLimits.getBox().right, (int) selectionLimits.getBox().bottom);
            this.mResizeOrigBottomRight = pageToView2;
            pageToView2.offset(this.mSelectionStartPage.getLeft(), this.mSelectionStartPage.getTop());
            this.mResizeOrigBottomRight.offset(-getScrollX(), -getScrollY());
            this.mNatDim = ((SODoc) getDoc()).getSelectionNaturalDimensions();
            Point point = this.mResizeOrigBottomRight;
            int i = point.y;
            Point point2 = this.mResizeOrigTopLeft;
            int i2 = point2.y;
            int i3 = point.x;
            int i4 = point2.x;
            this.mDragOrigLocation.set(dragHandle.getPosition().x, dragHandle.getPosition().y);
            this.mRotateAngle = ((SODoc) getDoc()).getSelectionRotation();
            this.mNAdditionalAngle = BitmapDescriptorFactory.HUE_RED;
            this.mResizingView.setRotation(BitmapDescriptorFactory.HUE_RED);
            Point point3 = this.mResizeOrigTopLeft;
            int i5 = point3.x;
            int i6 = point3.y;
            Point point4 = this.mResizeOrigBottomRight;
            moveResizingView(i5, i6, point4.x - i5, point4.y - i6);
            this.mResizeOrigRect.set(this.mResizeRect);
            hideHandles();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (finished()) {
            return true;
        }
        if (!allowTouchWithoutChildren() && getChildCount() <= 0) {
            return true;
        }
        if (getDrawMode()) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int action = motionEvent.getAction();
            if (action == 0) {
                touchStart(x, y);
            } else if (action == 1) {
                touchUp();
            } else if (action == 2) {
                touchMove(x, y);
            }
            return true;
        }
        this.mConstrained = true;
        if ((motionEvent.getAction() & KotlinVersion.MAX_COMPONENT_VALUE) == 0) {
            this.mTouching = true;
        }
        if ((motionEvent.getAction() & KotlinVersion.MAX_COMPONENT_VALUE) == 1) {
            if (this.mPressing) {
                onLongPressRelease();
            } else {
                this.mTouching = false;
            }
            onUp(motionEvent);
        }
        if ((motionEvent.getAction() & KotlinVersion.MAX_COMPONENT_VALUE) == 2 && this.mPressing) {
            onLongPressMoving(motionEvent);
        }
        this.mScaleGestureDetector.onTouchEvent(motionEvent);
        this.mGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    public void onUp(MotionEvent motionEvent) {
    }

    public void pageDown() {
        this.scrollingByKeyboard = true;
        smoothScrollBy(0, ((-getHeight()) * 9) / 10, LogSeverity.WARNING_VALUE);
    }

    public void pageUp() {
        this.scrollingByKeyboard = true;
        smoothScrollBy(0, (getHeight() * 9) / 10, LogSeverity.WARNING_VALUE);
    }

    public boolean pagesShowing() {
        return this.mPagesShowing;
    }

    public void pauseChildren() {
        for (int i = 0; i < getPageCount(); i++) {
            ((DocPageView) getOrCreateChild(i)).onPause();
        }
    }

    public void positionHandle(DragHandle dragHandle, DocPageView docPageView, int i, int i2) {
        if (dragHandle != null && docPageView != null) {
            Point pageToView = docPageView.pageToView(i, i2);
            pageToView.offset(docPageView.getChildRect().left, docPageView.getChildRect().top);
            pageToView.offset(-getScrollX(), -getScrollY());
            dragHandle.measure(0, 0);
            pageToView.offset((-dragHandle.getMeasuredWidth()) / 2, (-dragHandle.getMeasuredHeight()) / 2);
            Point offsetCircleToEdge = dragHandle.offsetCircleToEdge();
            pageToView.offset(offsetCircleToEdge.x, offsetCircleToEdge.y);
            dragHandle.moveTo(pageToView.x, pageToView.y);
        }
    }

    public void postRun() {
        if (this.scrollingByKeyboard) {
            this.mHostActivity.setCurrentPage(this, getMostVisiblePage());
        }
    }

    public void preNextPrevTrackedChange() {
        NoteEditor noteEditor = this.mNoteEditor;
        if (noteEditor != null) {
            noteEditor.preMoving();
        }
    }

    public void releaseBitmaps() {
        this.bitmaps = null;
    }

    public final void renderPages() {
        this.renderRequested = false;
        if (isValid()) {
            int i = this.bitmapIndex + 1;
            this.bitmapIndex = i;
            if (i >= this.bitmaps.length) {
                this.bitmapIndex = 0;
            }
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < getPageCount(); i2++) {
                DocPageView docPageView = (DocPageView) getOrCreateChild(i2);
                if (docPageView.getParent() != null && docPageView.isShown()) {
                    arrayList.add(docPageView);
                    docPageView.startRenderPass();
                }
            }
            long currentTimeMillis = System.currentTimeMillis();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                DocPageView docPageView2 = (DocPageView) it.next();
                if (isValid()) {
                    this.renderCount++;
                    docPageView2.render(this.bitmaps[this.bitmapIndex], new SORenderListener(arrayList, currentTimeMillis) {
                        public final /* synthetic */ ArrayList val$pages;

                        {
                            this.val$pages = r2;
                        }

                        public void progress(int i) {
                            DocView docView = DocView.this;
                            int i2 = docView.renderCount - 1;
                            docView.renderCount = i2;
                            if (i2 == 0) {
                                Iterator it = this.val$pages.iterator();
                                while (it.hasNext()) {
                                    DocPageView docPageView = (DocPageView) it.next();
                                    docPageView.endRenderPass();
                                    docPageView.invalidate();
                                }
                                DocView.this.onEndRenderPass();
                                DocView docView2 = DocView.this;
                                if (docView2.renderRequested) {
                                    docView2.renderPages();
                                }
                            }
                        }
                    });
                } else {
                    return;
                }
            }
        }
    }

    public void reportViewChanges() {
        this.mHostActivity.reportViewChanges();
    }

    public void requestLayout() {
        if (!finished()) {
            onPreLayout();
            super.requestLayout();
        }
    }

    public void resetDrawMode() {
        this.mDrawMode = false;
        saveInk();
    }

    public void resetInputView() {
        if (inputView() != null) {
            inputView().resetEditable();
        }
    }

    public void resetModes() {
        resetDrawMode();
        clearAreaSelection();
        onSelectionChanged();
    }

    public void run() {
        int i;
        if (this.mFinished) {
            this.scrollingByKeyboard = false;
            return;
        }
        if (!this.mScroller.isFinished()) {
            this.mScroller.computeScrollOffset();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            int i2 = currX - this.mScrollerLastX;
            int i3 = currY - this.mScrollerLastY;
            if (this.flinging) {
                this.mSmoother.addValue(i3);
                i = this.mSmoother.getAverage();
            } else {
                i = i3;
            }
            this.mXScroll += i2;
            this.mYScroll += i;
            requestLayout();
            this.mScrollerLastX += i2;
            this.mScrollerLastY += i3;
            post(this);
        } else {
            this.mViewSmoothScrollStartX = null;
            this.mViewSmoothScrollStartY = null;
            requestLayout();
            onEndFling();
            this.flinging = false;
            this.scrollingByKeyboard = false;
        }
        postRun();
    }

    public void saveComment() {
        NoteEditor noteEditor = this.mNoteEditor;
        if (noteEditor != null) {
            noteEditor.saveComment();
        }
    }

    public void saveInk() {
        Iterator<DocPageView> it = this.mDrawingPageList.iterator();
        while (it.hasNext()) {
            it.next().saveInk();
            getDoc().mIsModified = true;
        }
        this.mDrawingPageList.clear();
        this.mDrawingPage = null;
    }

    public void scaleChildren() {
        for (int i = 0; i < getPageCount(); i++) {
            ((DocPageView) getOrCreateChild(i)).setNewScale(this.mScale);
        }
    }

    public Point screenToPage(int i, Point point) {
        DocPageView docPageView = (DocPageView) getChildAt(i);
        if (docPageView == null) {
            return null;
        }
        return docPageView.screenToPage(point);
    }

    public Point screenToView(Point point) {
        Point point2 = new Point(point);
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        point2.offset(-rect.left, -rect.top);
        return point2;
    }

    public void scrollAfterScaleEnd(int i, int i2) {
        scrollTo(i, i2);
    }

    public void scrollBoxIntoView(int i, RectF rectF) {
        scrollBoxIntoView(i, rectF, false);
    }

    public void scrollBoxToTop(int i, RectF rectF) {
        smoothScrollBy(0, scrollBoxToTopAmount(i, rectF));
    }

    public int scrollBoxToTopAmount(int i, RectF rectF) {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        rect.offset(0, -rect.top);
        DocPageView docPageView = (DocPageView) getOrCreateChild(i);
        Point pageToView = docPageView.pageToView((int) rectF.left, (int) rectF.top);
        int i2 = pageToView.y + docPageView.getChildRect().top;
        pageToView.y = i2;
        int scrollY = i2 - getScrollY();
        pageToView.y = scrollY;
        return rect.top - scrollY;
    }

    public void scrollBy(int i, int i2) {
        Point point;
        if (this.mConstrained) {
            point = constrainScrollBy(i, i2);
        } else {
            point = new Point(i, i2);
        }
        super.scrollBy(point.x, point.y);
    }

    public void scrollEditorIntoView() {
        NoteEditor noteEditor = this.mNoteEditor;
        if (noteEditor != null && noteEditor.isVisible()) {
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.offset(-rect.left, -rect.top);
            Rect rect2 = this.mNoteEditor.getRect();
            int i = rect2.top;
            int i2 = rect.top;
            int i3 = 0;
            int i4 = i < i2 ? i2 - i : 0;
            int i5 = rect2.bottom;
            int i6 = rect.bottom;
            if (i5 > i6) {
                i4 = i6 - i5;
            }
            int i7 = rect2.left;
            int i8 = rect.left;
            if (i7 < i8) {
                i3 = i8 - i7;
            }
            int i9 = rect2.right;
            int i10 = rect.right;
            if (i9 > i10) {
                i3 = i10 - i9;
            }
            smoothScrollBy(i3, i4);
        }
    }

    public void scrollPointVisible(Point point) {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        int i = point.y;
        int i2 = rect.top;
        if (i < i2 || i > (rect.bottom + i2) / 2) {
            smoothScrollBy(0, ((i2 + rect.bottom) / 2) - i);
        }
    }

    public void scrollSelectionIntoView() {
        DocPageView docPageView;
        ArDkSelectionLimits selectionLimits;
        RectF box;
        RectF rectF;
        if (this.mSelectionLimits != null && (docPageView = this.mSelectionStartPage) != null && (selectionLimits = docPageView.getSelectionLimits()) != null && (box = selectionLimits.getBox()) != null && !box.isEmpty()) {
            int pageNumber = this.mSelectionStartPage.getPageNumber();
            if (((NUIDocView) this.mHostActivity).wasTyping()) {
                if (Utilities.isRTL(getContext())) {
                    float f = box.left;
                    rectF = new RectF(f, box.top, 1.0f + f, box.bottom);
                } else {
                    float f2 = box.right;
                    rectF = new RectF(f2 - 1.0f, box.top, f2, box.bottom);
                }
                scrollBoxIntoView(pageNumber, rectF, true);
                return;
            }
            scrollBoxIntoView(pageNumber, box);
        }
    }

    public void scrollToPage(int i, boolean z) {
        if (isValidPage(i)) {
            boolean z2 = true;
            if (i != getPageCount() - 1) {
                z2 = false;
            }
            scrollToPage(i, z2, z);
        }
    }

    public Point scrollToPageAmounts(int i) {
        boolean z = true;
        if (i != getPageCount() - 1) {
            z = false;
        }
        return scrollToPageAmounts(i, z);
    }

    public boolean select(Point point, Point point2) {
        DocPageView findPageViewContainingPoint;
        if ((this.mHostActivity instanceof NUIDocViewOther) || (findPageViewContainingPoint = findPageViewContainingPoint(point.x, point.y, false)) == null) {
            return false;
        }
        getDoc().closeSearch();
        getDoc().clearSelection();
        if (point2 == null) {
            Point screenToPage = findPageViewContainingPoint.screenToPage(point.x, point.y);
            if (findPageViewContainingPoint.getPage().select(3, (double) screenToPage.x, (double) screenToPage.y) != 1) {
                return false;
            }
        } else {
            Point screenToPage2 = findPageViewContainingPoint.screenToPage(point.x, point.y);
            Point screenToPage3 = findPageViewContainingPoint.screenToPage(point2.x, point2.y);
            if (findPageViewContainingPoint.getPage().select(2, (double) screenToPage2.x, (double) screenToPage2.y) == 1 && findPageViewContainingPoint.getPage().select(1, (double) screenToPage3.x, (double) screenToPage3.y) == 1 && findPageViewContainingPoint.getPage().select(0, (double) screenToPage2.x, (double) screenToPage2.y) == 1) {
                return true;
            }
            return false;
        }
        return true;
    }

    public void selectTopLeft() {
        ((DocPageView) getChildAt(0)).selectTopLeft();
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = (PageAdapter) adapter;
        requestLayout();
    }

    public void setBitmaps(ArDkBitmap[] arDkBitmapArr) {
        if (arDkBitmapArr[0] == null) {
            setValid(false);
        } else {
            setValid(true);
        }
        this.bitmaps = arDkBitmapArr;
    }

    public void setDoc(ArDkDoc arDkDoc) {
        this.mDoc = arDkDoc;
    }

    public void setDocSpecifics(ConfigOptions configOptions, SODataLeakHandlers sODataLeakHandlers) {
        this.mDocCfgOptions = configOptions;
        this.mDataLeakHandlers = sODataLeakHandlers;
    }

    public void setDrawModeOff() {
        this.mDrawMode = false;
        saveInk();
        clearAreaSelection();
        onSelectionChanged();
    }

    public void setDrawModeOn() {
        this.mDrawMode = true;
        clearAreaSelection();
        onSelectionChanged();
    }

    public void setHost(DocViewHost docViewHost) {
        this.mHostActivity = docViewHost;
    }

    public void setInkLineColor(int i) {
        this.mCurrentInkLineColor = i;
        Iterator<DocPageView> it = this.mDrawingPageList.iterator();
        while (it.hasNext()) {
            it.next().setInkLineColor(i);
        }
    }

    public void setInkLineThickness(float f) {
        this.mCurrentInkLineThickness = f;
        Iterator<DocPageView> it = this.mDrawingPageList.iterator();
        while (it.hasNext()) {
            it.next().setInkLineThickness(f);
        }
    }

    public void setMostVisiblePage() {
        int i;
        if (this.mTouching && (i = this.mostVisibleChild) >= 0 && !this.mScaling) {
            this.mHostActivity.setCurrentPage(this, i);
        }
    }

    public void setReflowMode(boolean z) {
        this.mPreviousReflowMode = this.mReflowMode;
        this.mReflowMode = z;
        this.mReflowWidth = -1;
    }

    public void setReflowWidth() {
        this.mReflowWidth = getReflowWidth();
    }

    public void setScale(float f) {
        this.mScale = f;
    }

    public void setScaleAndScroll(float f, int i, int i2) {
        onScaleBeginInternal();
        requestLayout();
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        final ViewTreeObserver viewTreeObserver2 = viewTreeObserver;
        final float f2 = f;
        final int i3 = i;
        final int i4 = i2;
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                viewTreeObserver2.removeOnGlobalLayoutListener(this);
                DocView docView = DocView.this;
                float f = f2;
                Boolean bool = Boolean.TRUE;
                int i = DocView.UNSCALED_GAP;
                docView.onScaleInternal(f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, bool);
                DocView.this.requestLayout();
                final ViewTreeObserver viewTreeObserver = DocView.this.getViewTreeObserver();
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);
                        DocView docView = DocView.this;
                        int i = DocView.UNSCALED_GAP;
                        docView.onScaleEndInternal();
                        DocView.this.requestLayout();
                        final ViewTreeObserver viewTreeObserver = DocView.this.getViewTreeObserver();
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                viewTreeObserver.removeOnGlobalLayoutListener(this);
                                AnonymousClass14 r0 = AnonymousClass14.this;
                                DocView.this.scrollTo(i3, i4);
                            }
                        });
                    }
                });
            }
        });
    }

    public void setSelection(int i) {
        throw new UnsupportedOperationException(getContext().getString(R.string.sodk_editor_not_supported));
    }

    public void setSelectionBoxBounds(RectF rectF) {
    }

    public void setShowKeyboardListener(ShowKeyboardListener showKeyboardListener2) {
        this.showKeyboardListener = showKeyboardListener2;
    }

    public void setStartPage(int i) {
        this.mStartPage = i;
    }

    public void setUpdatesPaused(boolean z) {
        this.mUpdatesPaused = z;
    }

    public void setValid(boolean z) {
        for (int i = 0; i < getPageCount(); i++) {
            ((DocPageView) getOrCreateChild(i)).setValid(z);
        }
    }

    public void setViewingState(ViewingState viewingState) {
        this.mViewingState = viewingState;
    }

    public void setup(RelativeLayout relativeLayout) {
        setupHandles(relativeLayout);
        setupNoteEditor();
    }

    public DragHandle setupHandle(RelativeLayout relativeLayout, int i) {
        DragHandle dragHandle;
        if (i == 7) {
            dragHandle = new DragHandle(getContext(), R.layout.sodk_editor_drag_handle, i);
        } else if (i == 8) {
            dragHandle = new DragHandle(getContext(), R.layout.sodk_editor_rotate_handle, i);
        } else {
            dragHandle = new DragHandle(getContext(), R.layout.sodk_editor_resize_handle, i);
        }
        relativeLayout.addView(dragHandle);
        dragHandle.show(false);
        dragHandle.setDragHandleListener(this);
        return dragHandle;
    }

    public void setupHandles(RelativeLayout relativeLayout) {
        this.mSelectionHandleTopLeft = setupHandle(relativeLayout, 1);
        this.mSelectionHandleBottomRight = setupHandle(relativeLayout, 2);
        ImageView imageView = new ImageView(getContext());
        this.mResizingView = imageView;
        imageView.setAlpha(0.5f);
        relativeLayout.addView(this.mResizingView);
        this.mResizingView.setVisibility(View.GONE);
        this.mResizingView.setAdjustViewBounds(false);
        this.mResizingView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.mResizeHandleTopLeft = setupHandle(relativeLayout, 3);
        this.mResizeHandleTopRight = setupHandle(relativeLayout, 4);
        this.mResizeHandleBottomLeft = setupHandle(relativeLayout, 5);
        this.mResizeHandleBottomRight = setupHandle(relativeLayout, 6);
        this.mDragHandle = setupHandle(relativeLayout, 7);
        this.mRotateHandle = setupHandle(relativeLayout, 8);
    }

    public void setupNoteEditor() {
        this.mNoteEditor = new NoteEditor((Activity) getContext(), this, this.mHostActivity, new NoteEditor.NoteDataHandler() {
            public String getAuthor() {
                return ((SODoc) DocView.this.getDoc()).getSelectedTrackedChangeAuthor();
            }

            public String getComment() {
                String selectedTrackedChangeComment = ((SODoc) DocView.this.getDoc()).getSelectedTrackedChangeComment();
                int selectedTrackedChangeType = ((SODoc) DocView.this.getDoc()).getSelectedTrackedChangeType();
                boolean z = false;
                if (selectedTrackedChangeType == 5) {
                    selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_deleted_text);
                } else if (selectedTrackedChangeType == 24) {
                    selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_changed_paragraph_properties);
                } else if (selectedTrackedChangeType == 26) {
                    selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_changed_run_properties);
                } else if (selectedTrackedChangeType == 28) {
                    selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_changed_section_properties);
                } else if (selectedTrackedChangeType != 36) {
                    switch (selectedTrackedChangeType) {
                        case 15:
                            selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_inserted_text);
                            break;
                        case 16:
                            selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_inserted_paragraph);
                            break;
                        case 17:
                            selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_inserted_table_cell);
                            break;
                        case 18:
                            selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_inserted_table_row);
                            break;
                        default:
                            switch (selectedTrackedChangeType) {
                                case 31:
                                    selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_changed_table_cell_properties);
                                    break;
                                case 32:
                                    selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_changed_table_grid);
                                    break;
                                case 33:
                                    selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_changed_table_properties);
                                    break;
                                default:
                                    z = DocView.this.mDocCfgOptions.isEditingEnabled();
                                    break;
                            }
                    }
                } else {
                    selectedTrackedChangeComment = DocView.this.getContext().getString(R.string.sodk_editor_changed_table_row_properties);
                }
                DocView.this.mNoteEditor.setCommentEditable(z);
                return selectedTrackedChangeComment;
            }

            public String getDate() {
                return ((SODoc) DocView.this.getDoc()).getSelectedTrackedChangeDate();
            }

            public void setComment(String str) {
                ArDkDoc doc = DocView.this.getDoc();
                if (doc != null) {
                    doc.setSelectionAnnotationComment(str);
                }
            }
        });
    }

    public boolean shouldAddHistory(int i, int i2, float f) {
        History.HistoryItem current = getHistory().current();
        if (current == null) {
            return true;
        }
        Rect rect = new Rect();
        getLocalVisibleRect(rect);
        rect.offset(current.getScrollX(), current.getScrollY());
        Rect rect2 = new Rect();
        getLocalVisibleRect(rect2);
        rect2.offset(i, i2);
        if (new Rect(rect).intersect(rect2)) {
            return false;
        }
        return true;
    }

    public boolean shouldLayout() {
        boolean z = true;
        boolean z2 = this.mPreviousReflowMode != this.mReflowMode;
        if (!(this.mScale == this.mLastScale && this.mLastScrollX == getScrollX() && this.mLastScrollY == getScrollY())) {
            z2 = true;
        }
        if (this.mForceLayout) {
            this.mForceLayout = false;
        } else {
            z = z2;
        }
        if (z) {
            this.mLastScale = this.mScale;
            this.mLastScrollX = getScrollX();
            this.mLastScrollY = getScrollY();
        }
        return z;
    }

    public boolean shouldPreclearSelection() {
        return true;
    }

    public void showHandle(DragHandle dragHandle, boolean z) {
        if (dragHandle != null) {
            dragHandle.show(z);
        }
    }

    public void showHandles() {
        hideHandles();
        ArDkSelectionLimits selectionLimits = getSelectionLimits();
        if (selectionLimits != null) {
            boolean z = false;
            boolean z2 = selectionLimits.getIsActive() && !selectionLimits.getIsCaret();
            boolean z3 = selectionLimits.getIsActive() && getDoc().getSelectionCanBeResized() && !selectionLimits.getIsCaret();
            boolean z4 = selectionLimits.getIsActive() && getDoc().getSelectionCanBeAbsolutelyPositioned();
            if (selectionLimits.getIsActive() && getDoc().getSelectionCanBeRotated()) {
                z = true;
            }
            showHandle(this.mSelectionHandleTopLeft, z2);
            showHandle(this.mSelectionHandleBottomRight, z2);
            showResizeHandles(z3);
            if (canEditText()) {
                showHandle(this.mDragHandle, z4);
            }
            if (canEditText()) {
                showHandle(this.mRotateHandle, z);
            }
        }
    }

    public void showKeyboardAfterDoubleTap(Point point) {
        if (!Utilities.isLandscapePhone(getContext())) {
            showKeyboardAndScroll(point);
        }
    }

    public void showKeyboardAndScroll(Point point) {
        DocPageView findPageViewContainingPoint = findPageViewContainingPoint(point.x, point.y, false);
        if (findPageViewContainingPoint != null) {
            this.scrollToHerePage = null;
            this.scrollToHere = null;
            if (this.mHostActivity.showKeyboard()) {
                this.scrollToHerePage = findPageViewContainingPoint;
                this.scrollToHere = findPageViewContainingPoint.screenToPage(point);
            }
        }
    }

    public final void showResizeHandles(boolean z) {
        if (canEditText()) {
            showHandle(this.mResizeHandleTopLeft, z);
            showHandle(this.mResizeHandleTopRight, z);
            showHandle(this.mResizeHandleBottomLeft, z);
            showHandle(this.mResizeHandleBottomRight, z);
        }
    }

    public void smoothScrollBy(int i, int i2) {
        smoothScrollBy(i, i2, LogSeverity.WARNING_VALUE);
    }

    public void startMovingPage(int i) {
    }

    public boolean tapToFocus() {
        return true;
    }

    public void touchMove(float f, float f2) {
        float abs = Math.abs(f - this.mX);
        float abs2 = Math.abs(f2 - this.mY);
        if (abs >= 2.0f || abs2 >= 2.0f) {
            Point eventToScreen = eventToScreen(f, f2);
            DocPageView docPageView = this.mDrawingPage;
            if (docPageView != null) {
                docPageView.continueDrawInk((float) eventToScreen.x, (float) eventToScreen.y);
            }
            this.mX = f;
            this.mY = f2;
        }
    }

    public void touchStart(float f, float f2) {
        Point eventToScreen = eventToScreen(f, f2);
        DocPageView findPageViewContainingPoint = findPageViewContainingPoint(eventToScreen.x, eventToScreen.y, false);
        this.mDrawingPage = findPageViewContainingPoint;
        if (findPageViewContainingPoint != null) {
            findPageViewContainingPoint.startDrawInk((float) eventToScreen.x, (float) eventToScreen.y, getInkLineColor(), getInkLineThickness());
            if (!this.mDrawingPageList.contains(this.mDrawingPage)) {
                this.mDrawingPageList.add(this.mDrawingPage);
            }
            this.mHostActivity.selectionupdated();
        }
        this.mX = f;
        this.mY = f2;
    }

    public void touchUp() {
        DocPageView docPageView = this.mDrawingPage;
        if (docPageView != null) {
            docPageView.endDrawInk();
        }
    }

    public void triggerRender() {
        if (this.bitmaps[0] != null) {
            this.renderRequested = true;
            if (this.renderCount == 0) {
                renderPages();
            }
        }
    }

    public void updateDragHandles() {
        Bitmap bitmap;
        this.mResizingView.setImageBitmap((Bitmap) null);
        this.mResizingView.setVisibility(View.GONE);
        ArDkBitmap arDkBitmap = this.mResizingBitmap;
        if (!(arDkBitmap == null || (bitmap = arDkBitmap.bitmap) == null)) {
            bitmap.recycle();
            this.mResizingBitmap = null;
        }
        showHandles();
        moveHandlesToCorners();
    }

    public void updateInputView() {
        if (inputView() != null) {
            inputView().updateEditable();
        }
    }

    public void updateReview() {
        boolean z = this.mScrollRequested;
        this.mScrollRequested = false;
        boolean z2 = this.mAddComment;
        this.mAddComment = false;
        if (((SODoc) getDoc()).getSelectionHasAssociatedPopup() || ((SODoc) getDoc()).selectionIsReviewable()) {
            this.mTapStatus = 0;
            this.mNoteEditor.show(getSelectionLimits(), this.mSelectionStartPage);
            this.mNoteEditor.move();
            if (z) {
                scrollEditorIntoView();
            }
            if (z2) {
                this.mNoteEditor.focus();
                this.mHostActivity.showKeyboard();
                return;
            }
            return;
        }
        NoteEditor noteEditor = this.mNoteEditor;
        if (noteEditor != null && noteEditor.isVisible()) {
            Utilities.hideKeyboard(getContext());
            this.mNoteEditor.hide();
            if (z) {
                scrollSelectionIntoView();
            }
        }
    }

    public Point viewToScreen(Point point) {
        Point point2 = new Point(point);
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        point2.offset(rect.left, rect.top);
        return point2;
    }

    public void waitForRest(final Runnable runnable) {
        if (isAtRest()) {
            runnable.run();
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    DocView.this.waitForRest(runnable);
                }
            }, 100);
        }
    }

    public void scrollBoxIntoView(int i, RectF rectF, boolean z) {
        scrollBoxIntoView(i, rectF, z, 0);
    }

    public void smoothScrollBy(int i, int i2, int i3) {
        if (i != 0 || i2 != 0) {
            this.mScrollerLastY = 0;
            this.mScrollerLastX = 0;
            this.mScroller.startScroll(0, 0, i, i2, i3);
            this.mViewSmoothScrollStartX = new Integer(getScrollX());
            this.mViewSmoothScrollStartY = new Integer(getScrollY());
            post(this);
        }
    }

    public void scrollBoxIntoView(int i, RectF rectF, boolean z, int i2) {
        int i3;
        int i4;
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        int i5 = 0;
        rect.offset(0, -rect.top);
        rect.inset(i2, i2);
        DocPageView docPageView = (DocPageView) getOrCreateChild(i);
        Point pageToView = docPageView.pageToView((int) rectF.left, (int) rectF.bottom);
        Rect childRect = docPageView.getChildRect();
        int i6 = pageToView.y + childRect.top;
        pageToView.y = i6;
        pageToView.y = i6 - getScrollY();
        int i7 = pageToView.x + childRect.left;
        pageToView.x = i7;
        int scrollX = i7 - getScrollX();
        pageToView.x = scrollX;
        int i8 = pageToView.y;
        int i9 = rect.top;
        if (i8 < i9 || i8 > rect.bottom) {
            i3 = ((i9 + rect.bottom) / 2) - i8;
        } else {
            i3 = 0;
        }
        if (z && (scrollX < (i4 = rect.left) || scrollX > rect.right)) {
            i5 = ((i4 + rect.right) / 2) - scrollX;
        }
        Point point = new Point(i5, i3);
        smoothScrollBy(point.x, point.y);
    }

    public Point scrollToPageAmounts(int i, boolean z) {
        int i2;
        int i3;
        int i4;
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        Point point = new Point();
        point.set(getScrollX(), getScrollY());
        rect.offsetTo(point.x, point.y);
        Rect childRect = ((DocPageView) getOrCreateChild(i)).getChildRect();
        if (childRect.height() > rect.height()) {
            if (z) {
                i3 = getScrollY() - childRect.top;
                i4 = childRect.height() - rect.height();
            } else {
                i3 = getScrollY();
                i4 = childRect.top;
            }
            i2 = i3 - i4;
        } else {
            int i5 = childRect.top;
            if (i5 >= rect.top && childRect.bottom <= rect.bottom) {
                i2 = 0;
            } else if (i5 == 0) {
                i2 = getScrollY();
            } else {
                i2 = ((rect.height() / 2) + getScrollY()) - ((childRect.bottom + childRect.top) / 2);
            }
        }
        return new Point(0, i2);
    }

    public void scrollToPage(int i, boolean z, boolean z2) {
        int i2 = scrollToPageAmounts(i, z).y;
        if (i2 == 0) {
            forceLayout();
        } else if (z2) {
            smoothScrollBy(0, i2, 0);
        } else {
            smoothScrollBy(0, i2);
        }
    }

    public DocView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
    }

    public DocView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context);
    }
}
