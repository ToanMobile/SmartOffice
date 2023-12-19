package com.artifex.sonui.editor;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.artifex.solib.SODoc;
import com.artifex.sonui.editor.PageMenu;

public class DocListPagesView extends DocView {
    public int AUTOSCROLL_AMOUNT = 0;
    public int deletingPageNum = -1;
    public MotionEvent lastEvent;
    public boolean mCanManipulatePages = false;
    public boolean mIsMovingPage = false;
    public DocView mMainView;
    public boolean mMoved = false;
    public DocPageView mMovingPage = null;
    public Bitmap mMovingPageBitmap = null;
    public int mMovingPageNumber = -1;
    public ImageView mMovingPageView;
    public ProgressDialog spinner = null;
    public float startPageX;
    public float startPageY;
    public float startPressX;
    public float startPressY;

    public DocListPagesView(Context context) {
        super(context);
        init();
    }

    public boolean allowTouchWithoutChildren() {
        return true;
    }

    public boolean allowXScroll() {
        return false;
    }

    public boolean centerPagesHorizontally() {
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0037, code lost:
        if (r0 < r4) goto L_0x0039;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003c, code lost:
        r4 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0029, code lost:
        if ((r1 - r5) < r4) goto L_0x0039;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Point constrainScrollBy(int r4, int r5) {
        /*
            r3 = this;
            android.graphics.Rect r4 = new android.graphics.Rect
            r4.<init>()
            r3.getGlobalVisibleRect(r4)
            int r4 = r4.height()
            android.graphics.Rect r0 = r3.mAllPagesRect
            int r0 = r0.height()
            int r1 = r3.getScrollY()
            if (r0 > r4) goto L_0x001a
            int r4 = -r1
            goto L_0x003d
        L_0x001a:
            int r2 = r1 + r5
            if (r2 >= 0) goto L_0x001f
            int r5 = -r1
        L_0x001f:
            boolean r2 = r3.isMovingPage()
            if (r2 != 0) goto L_0x002c
            int r1 = -r1
            int r1 = r1 + r0
            int r0 = r1 - r5
            if (r0 >= r4) goto L_0x003c
            goto L_0x0039
        L_0x002c:
            android.widget.ImageView r2 = r3.mMovingPageView
            int r2 = r2.getHeight()
            int r1 = -r1
            int r1 = r1 + r0
            int r0 = r1 - r5
            int r4 = r4 - r2
            if (r0 >= r4) goto L_0x003c
        L_0x0039:
            int r4 = r4 - r1
            int r4 = -r4
            goto L_0x003d
        L_0x003c:
            r4 = r5
        L_0x003d:
            android.graphics.Point r5 = new android.graphics.Point
            r0 = 0
            r5.<init>(r0, r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.DocListPagesView.constrainScrollBy(int, int):android.graphics.Point");
    }

    public void doDoubleTap(float f, float f2) {
    }

    public void doPageMenu(final int i) {
        DocPageView docPageView = (DocPageView) getOrCreateChild(i);
        Context context = getContext();
        boolean z = true;
        if (getPageCount() <= 1) {
            z = false;
        }
        new PageMenu(context, docPageView, z, new PageMenu.ActionListener() {
            public void onDelete() {
                DocListPagesView docListPagesView = DocListPagesView.this;
                docListPagesView.spinner = Utilities.createAndShowWaitSpinner(docListPagesView.getContext());
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ((SODoc) DocListPagesView.this.getDoc()).deletePage(i);
                        AnonymousClass3 r0 = AnonymousClass3.this;
                        DocListPagesView.this.deletingPageNum = i;
                    }
                }, 50);
            }

            public void onDuplicate() {
                DocListPagesView docListPagesView = DocListPagesView.this;
                docListPagesView.spinner = Utilities.createAndShowWaitSpinner(docListPagesView.getContext());
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ((SODoc) DocListPagesView.this.getDoc()).duplicatePage(i);
                        AnonymousClass3 r0 = AnonymousClass3.this;
                        DocListPagesView.this.onPageMoved(i);
                    }
                }, 50);
            }
        }).show();
    }

    public void doSingleTap(float f, float f2) {
        Point eventToScreen = eventToScreen(f, f2);
        DocPageView findPageViewContainingPoint = findPageViewContainingPoint(eventToScreen.x, eventToScreen.y, false);
        if (findPageViewContainingPoint != null) {
            DocView docView = this.mMainView;
            docView.addHistory(docView.getScrollX(), this.mMainView.getScrollY(), this.mMainView.getScale(), true);
            int pageNumber = findPageViewContainingPoint.getPageNumber();
            Point scrollToPageAmounts = this.mMainView.scrollToPageAmounts(pageNumber);
            if (scrollToPageAmounts.y != 0) {
                DocView docView2 = this.mMainView;
                docView2.addHistory(docView2.getScrollX(), this.mMainView.getScrollY() - scrollToPageAmounts.y, this.mMainView.getScale(), false);
            }
            setCurrentPage(pageNumber);
            this.mHostActivity.setCurrentPage(this, pageNumber);
            this.mMainView.scrollToPage(pageNumber, false);
        }
    }

    public void finishDrop() {
        this.mMovingPageView.setVisibility(View.GONE);
        super.finishDrop();
    }

    public void fitToColumns() {
        this.mLastLayoutColumns = 1;
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        int i = 0;
        for (int i2 = 0; i2 < getPageCount(); i2++) {
            DocPageView docPageView = (DocPageView) getOrCreateChild(i2);
            if (!(docPageView == null || docPageView.getPage() == null)) {
                i = Math.max(i, docPageView.getUnscaledWidth());
            }
        }
        int i3 = this.mLastLayoutColumns;
        this.mScale = ((float) rect.width()) / ((float) (((i3 - 1) * 20) + (i * i3)));
        scaleChildren();
        requestLayout();
    }

    public int getCurrentPage() {
        for (int i = 0; i < getPageCount(); i++) {
            if (((DocPageView) getOrCreateChild(i)).isCurrent()) {
                return i;
            }
        }
        return -1;
    }

    public int getMovingPageNumber() {
        return this.mMovingPageNumber;
    }

    public final void init() {
        this.AUTOSCROLL_AMOUNT = Utilities.convertDpToPixel(8.0f);
    }

    public final boolean isEventInside(MotionEvent motionEvent) {
        Rect rect = new Rect();
        getHitRect(rect);
        return rect.contains((int) motionEvent.getX(), (int) motionEvent.getY());
    }

    public boolean isMovingPage() {
        return this.mIsMovingPage;
    }

    public void onConfigurationChange() {
    }

    public void onEndFling() {
    }

    public void onHidePages() {
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (this.mCanManipulatePages) {
            this.mMoved = false;
            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.setDuration(200);
            layoutTransition.enableTransitionType(4);
            setLayoutTransition(layoutTransition);
            Point eventToScreen = eventToScreen(motionEvent.getX(), motionEvent.getY());
            DocPageView findPageViewContainingPoint = findPageViewContainingPoint(eventToScreen.x, eventToScreen.y, false);
            this.mMovingPage = findPageViewContainingPoint;
            if (findPageViewContainingPoint != null) {
                setCurrentPage(-1);
                this.mMovingPage.setCurrent(true);
                this.mPressing = true;
                this.mIsMovingPage = true;
                this.mMovingPageNumber = this.mMovingPage.getPageNumber();
                this.startPressX = motionEvent.getX();
                this.startPressY = motionEvent.getY();
                this.startPageX = this.mMovingPage.getX() - ((float) getScrollX());
                this.startPageY = this.mMovingPage.getY() - ((float) getScrollY());
                startMovingPage(this.mMovingPageNumber);
                this.lastEvent = motionEvent;
            }
        }
    }

    public void onLongPressMoving(MotionEvent motionEvent) {
        if (this.mCanManipulatePages && this.mMovingPageView != null) {
            float x = motionEvent.getX() - this.startPressX;
            float y = motionEvent.getY() - this.startPressY;
            float dimension = (float) ((int) getContext().getResources().getDimension(R.dimen.sodk_editor_drag_slop));
            if (Math.abs(x) > dimension || Math.abs(y) > dimension) {
                this.mMoved = true;
            }
            float f = this.startPageX + x;
            float f2 = this.startPageY + y;
            int convertDpToPixel = Utilities.convertDpToPixel(10.0f);
            float f3 = (float) convertDpToPixel;
            float f4 = f + f3;
            float f5 = f2 - f3;
            final int i = 0;
            if (this.mMovingPageView.getVisibility() == 8) {
                DocPageView docPageView = this.mMovingPage;
                Bitmap createBitmap = Bitmap.createBitmap(docPageView.getWidth(), docPageView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                docPageView.layout(docPageView.getLeft(), docPageView.getTop(), docPageView.getRight(), docPageView.getBottom());
                docPageView.draw(canvas);
                this.mMovingPageBitmap = createBitmap;
                this.mMovingPageView.setImageBitmap(createBitmap);
                this.mMovingPageView.setVisibility(View.VISIBLE);
            }
            this.mMovingPageView.setX(f4);
            this.mMovingPageView.setY(f5);
            this.mMovingPageView.invalidate();
            if (isEventInside(motionEvent)) {
                View view = (View) getParent();
                float y2 = (motionEvent.getY() - ((float) view.getTop())) / ((float) view.getHeight());
                if (y2 <= 0.15f && getScrollY() > 0) {
                    i = -this.AUTOSCROLL_AMOUNT;
                } else if (y2 >= 0.85f) {
                    i = this.AUTOSCROLL_AMOUNT;
                }
                if (i != 0) {
                    Handler handler = new Handler();
                    super.forceLayout();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            DocListPagesView.this.scrollBy(0, i);
                        }
                    }, 5);
                } else {
                    onMovePage(this.mMovingPageNumber, (this.mMovingPageView.getHeight() / 2) + (((int) f5) - convertDpToPixel));
                }
            }
            this.lastEvent = motionEvent;
        }
    }

    public void onLongPressRelease() {
        if (this.mCanManipulatePages) {
            setLayoutTransition((LayoutTransition) null);
            ImageView imageView = this.mMovingPageView;
            if (imageView != null) {
                imageView.setVisibility(View.GONE);
                this.mMovingPageView.setImageBitmap((Bitmap) null);
                Bitmap bitmap = this.mMovingPageBitmap;
                if (bitmap != null) {
                    bitmap.recycle();
                    this.mMovingPageBitmap = null;
                }
            }
            if (isEventInside(this.lastEvent)) {
                dropMovingPage(this.mMoved);
            } else {
                finishDrop();
            }
            this.mPressing = false;
            this.mIsMovingPage = false;
            this.mMovingPageNumber = -1;
            this.mMovingPage = null;
        }
    }

    public void onOrientationChange() {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        int width = rect.width();
        rect.height();
        int width2 = this.mAllPagesRect.width();
        getScrollY();
        if (width2 > 0) {
            this.mScale *= ((float) width) / ((float) width2);
            scaleChildren();
            requestLayout();
            final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    final int currentPage = DocListPagesView.this.getCurrentPage();
                    if (currentPage >= 0) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                DocListPagesView.this.scrollToPage(currentPage, false);
                            }
                        }, 100);
                    }
                    DocListPagesView.this.mForceColumnCount = -1;
                }
            });
        }
    }

    public void onPageMoved(int i) {
        setCurrentPage(i);
        this.mMainView.scrollToPage(i, false);
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        return true;
    }

    public void onSelectionChanged() {
        if (this.deletingPageNum != -1) {
            int pageCount = getPageCount();
            int i = this.deletingPageNum;
            if (i < 0) {
                i = 0;
            } else if (i >= pageCount) {
                i = pageCount - 1;
            }
            this.mMainView.scrollToPage(i, true);
            setCurrentPage(i);
            this.mHostActivity.setCurrentPage(this, i);
            this.deletingPageNum = -1;
        }
        forceLayout();
        ProgressDialog progressDialog = this.spinner;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.spinner = null;
        }
    }

    public void onShowKeyboard(boolean z) {
        if (isShown() && !z) {
            forceLayout();
        }
    }

    public void onShowPages() {
    }

    public void postRun() {
    }

    public void reportViewChanges() {
    }

    public void setBorderColor(int i) {
    }

    public void setCanManipulatePages(boolean z) {
        this.mCanManipulatePages = z;
    }

    public void setCurrentPage(int i) {
        int i2 = 0;
        while (i2 < getPageCount()) {
            DocPageView docPageView = (DocPageView) getOrCreateChild(i2);
            if (i == -1) {
                docPageView.setCurrent(false);
            } else {
                docPageView.setCurrent(i2 == i);
            }
            docPageView.setSelectedBorderColor(getBorderColor());
            i2++;
        }
    }

    public void setMainView(DocView docView) {
        this.mMainView = docView;
    }

    public void setMostVisiblePage() {
    }

    public void setup(RelativeLayout relativeLayout) {
        if (this.mMovingPageView == null) {
            ImageView imageView = new ImageView(getContext());
            this.mMovingPageView = imageView;
            relativeLayout.addView(imageView);
            this.mMovingPageView.setVisibility(View.GONE);
        }
    }

    public void showHandles() {
    }

    public DocListPagesView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public DocListPagesView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }
}
