package com.artifex.source.library.wheel.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import com.rpdev.document.manager.reader.allfiles.R;
import com.yandex.metrica.d;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import com.artifex.source.library.wheel.widget.WheelScroller;
import com.artifex.source.library.wheel.widget.adapters.WheelViewAdapter;

public class WheelView extends View {
    public static final int[] SHADOWS_COLORS = {-15658735, 11184810, 11184810};
    public GradientDrawable bottomShadow;
    public Drawable centerDrawable;
    public List<OnWheelChangedListener> changingListeners = new LinkedList();
    public List<OnWheelClickedListener> clickingListeners = new LinkedList();
    public int currentItem = 0;
    public DataSetObserver dataObserver = new DataSetObserver() {
        public void onChanged() {
            WheelView.this.invalidateWheel(false);
        }

        public void onInvalidated() {
            WheelView.this.invalidateWheel(true);
        }
    };
    public int firstItem;
    public boolean isCyclic = false;
    public boolean isScrollingPerformed;
    public int itemHeight = 0;
    public LinearLayout itemsLayout;
    public d recycle = new d(this);
    public WheelScroller scroller = new WheelScroller(getContext(), this.scrollingListener);
    public WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
        public void onScroll(int i) {
            WheelView.access$100(WheelView.this, i);
            int height = WheelView.this.getHeight();
            WheelView wheelView = WheelView.this;
            int i2 = wheelView.scrollingOffset;
            if (i2 > height) {
                wheelView.scrollingOffset = height;
                wheelView.scroller.scroller.forceFinished(true);
                return;
            }
            int i3 = -height;
            if (i2 < i3) {
                wheelView.scrollingOffset = i3;
                wheelView.scroller.scroller.forceFinished(true);
            }
        }
    };
    public List<OnWheelScrollListener> scrollingListeners = new LinkedList();
    public int scrollingOffset;
    public GradientDrawable topShadow;
    public WheelViewAdapter viewAdapter;
    public int visibleItems = 5;

    public WheelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public static void access$100(WheelView wheelView, int i) {
        wheelView.scrollingOffset += i;
        int itemHeight2 = wheelView.getItemHeight();
        int i2 = wheelView.scrollingOffset / itemHeight2;
        int i3 = wheelView.currentItem - i2;
        int itemsCount = wheelView.viewAdapter.getItemsCount();
        int i4 = wheelView.scrollingOffset % itemHeight2;
        if (Math.abs(i4) <= itemHeight2 / 2) {
            i4 = 0;
        }
        if (wheelView.isCyclic && itemsCount > 0) {
            if (i4 > 0) {
                i3--;
                i2++;
            } else if (i4 < 0) {
                i3++;
                i2--;
            }
            while (i3 < 0) {
                i3 += itemsCount;
            }
            i3 %= itemsCount;
        } else if (i3 < 0) {
            i2 = wheelView.currentItem;
            i3 = 0;
        } else if (i3 >= itemsCount) {
            i2 = (wheelView.currentItem - itemsCount) + 1;
            i3 = itemsCount - 1;
        } else if (i3 > 0 && i4 > 0) {
            i3--;
            i2++;
        } else if (i3 < itemsCount - 1 && i4 < 0) {
            i3++;
            i2--;
        }
        int i5 = wheelView.scrollingOffset;
        if (i3 != wheelView.currentItem) {
            wheelView.setCurrentItem(i3, false);
        } else {
            wheelView.invalidate();
        }
        int i6 = i5 - (i2 * itemHeight2);
        wheelView.scrollingOffset = i6;
        if (i6 > wheelView.getHeight()) {
            wheelView.scrollingOffset = wheelView.getHeight() + (wheelView.scrollingOffset % wheelView.getHeight());
        }
    }

    private int getItemHeight() {
        int i = this.itemHeight;
        if (i != 0) {
            return i;
        }
        LinearLayout linearLayout = this.itemsLayout;
        if (linearLayout == null || linearLayout.getChildAt(0) == null) {
            return getHeight() / this.visibleItems;
        }
        int height = this.itemsLayout.getChildAt(0).getHeight();
        this.itemHeight = height;
        return height;
    }

    private ItemsRange getItemsRange() {
        if (getItemHeight() == 0) {
            return null;
        }
        int i = this.currentItem;
        int i2 = 1;
        while (getItemHeight() * i2 < getHeight()) {
            i--;
            i2 += 2;
        }
        int i3 = this.scrollingOffset;
        if (i3 != 0) {
            if (i3 > 0) {
                i--;
            }
            int itemHeight2 = i3 / getItemHeight();
            i -= itemHeight2;
            i2 = (int) (Math.asin((double) itemHeight2) + ((double) (i2 + 1)));
        }
        return new ItemsRange(i, i2);
    }

    public final boolean addViewItem(int i, boolean z) {
        View view;
        WheelViewAdapter wheelViewAdapter = this.viewAdapter;
        if (wheelViewAdapter == null || wheelViewAdapter.getItemsCount() == 0) {
            view = null;
        } else {
            int itemsCount = this.viewAdapter.getItemsCount();
            if (!isValidItemIndex(i)) {
                WheelViewAdapter wheelViewAdapter2 = this.viewAdapter;
                d dVar = this.recycle;
                view = wheelViewAdapter2.getEmptyItem(dVar.getCachedView((List) dVar.b), this.itemsLayout);
            } else {
                while (i < 0) {
                    i += itemsCount;
                }
                int i2 = i % itemsCount;
                WheelViewAdapter wheelViewAdapter3 = this.viewAdapter;
                d dVar2 = this.recycle;
                view = wheelViewAdapter3.getItem(i2, dVar2.getCachedView((List) dVar2.f4128a), this.itemsLayout);
            }
        }
        if (view == null) {
            return false;
        }
        if (z) {
            this.itemsLayout.addView(view, 0);
            return true;
        }
        this.itemsLayout.addView(view);
        return true;
    }

    public final int calculateLayoutWidth(int i, int i2) {
        if (this.centerDrawable == null) {
            this.centerDrawable = getContext().getResources().getDrawable(R.drawable.sodk_wheel_wheel_val);
        }
        if (this.topShadow == null) {
            this.topShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        }
        if (this.bottomShadow == null) {
            this.bottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, SHADOWS_COLORS);
        }
        setBackgroundResource(R.drawable.sodk_wheel_wheel_bg);
        this.itemsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.itemsLayout.measure(MeasureSpec.makeMeasureSpec(i, 0), MeasureSpec.makeMeasureSpec(0, 0));
        int measuredWidth = this.itemsLayout.getMeasuredWidth();
        if (i2 != 1073741824) {
            int max = Math.max(measuredWidth + 20, getSuggestedMinimumWidth());
            if (i2 != Integer.MIN_VALUE || i >= max) {
                i = max;
            }
        }
        this.itemsLayout.measure(MeasureSpec.makeMeasureSpec(i - 20, 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
        return i;
    }

    public int getCurrentItem() {
        return this.currentItem;
    }

    public WheelViewAdapter getViewAdapter() {
        return this.viewAdapter;
    }

    public int getVisibleItems() {
        return this.visibleItems;
    }

    public void invalidateWheel(boolean z) {
        if (z) {
            d dVar = this.recycle;
            List list = (List) dVar.f4128a;
            if (list != null) {
                list.clear();
            }
            List list2 = (List) dVar.b;
            if (list2 != null) {
                list2.clear();
            }
            LinearLayout linearLayout = this.itemsLayout;
            if (linearLayout != null) {
                linearLayout.removeAllViews();
            }
            this.scrollingOffset = 0;
        } else {
            LinearLayout linearLayout2 = this.itemsLayout;
            if (linearLayout2 != null) {
                d dVar2 = this.recycle;
                int i = this.firstItem;
                int i2 = 0;
                while (i2 < linearLayout2.getChildCount()) {
                    if (!(i >= 0 && i <= -1)) {
                        dVar2.recycleView(linearLayout2.getChildAt(i2), i);
                        linearLayout2.removeViewAt(i2);
                    } else {
                        i2++;
                    }
                    i++;
                }
            }
        }
        invalidate();
    }

    public final boolean isValidItemIndex(int i) {
        WheelViewAdapter wheelViewAdapter = this.viewAdapter;
        return wheelViewAdapter != null && wheelViewAdapter.getItemsCount() > 0 && (this.isCyclic || (i >= 0 && i < this.viewAdapter.getItemsCount()));
    }

    public void onDraw(Canvas canvas) {
        boolean z;
        super.onDraw(canvas);
        WheelViewAdapter wheelViewAdapter = this.viewAdapter;
        if (wheelViewAdapter != null && wheelViewAdapter.getItemsCount() > 0) {
            ItemsRange itemsRange = getItemsRange();
            LinearLayout linearLayout = this.itemsLayout;
            if (linearLayout != null) {
                int recycleItems = this.recycle.recycleItems(linearLayout, this.firstItem, itemsRange);
                z = this.firstItem != recycleItems;
                this.firstItem = recycleItems;
            } else {
                if (linearLayout == null) {
                    LinearLayout linearLayout2 = new LinearLayout(getContext());
                    this.itemsLayout = linearLayout2;
                    linearLayout2.setOrientation(1);
                }
                z = true;
            }
            if (!z) {
                z = (this.firstItem == itemsRange.first && this.itemsLayout.getChildCount() == itemsRange.count) ? false : true;
            }
            int i = this.firstItem;
            int i2 = itemsRange.first;
            if (i <= i2 || i > (itemsRange.count + i2) - 1) {
                this.firstItem = i2;
            } else {
                int i3 = i - 1;
                while (i3 >= itemsRange.first && addViewItem(i3, true)) {
                    this.firstItem = i3;
                    i3--;
                }
            }
            int i4 = this.firstItem;
            for (int childCount = this.itemsLayout.getChildCount(); childCount < itemsRange.count; childCount++) {
                if (!addViewItem(this.firstItem + childCount, false) && this.itemsLayout.getChildCount() == 0) {
                    i4++;
                }
            }
            this.firstItem = i4;
            if (z) {
                calculateLayoutWidth(getWidth(), 1073741824);
                this.itemsLayout.layout(0, 0, getWidth() - 20, getHeight());
            }
            canvas.save();
            canvas.translate(10.0f, (float) ((-(((getItemHeight() - getHeight()) / 2) + ((this.currentItem - this.firstItem) * getItemHeight()))) + this.scrollingOffset));
            this.itemsLayout.draw(canvas);
            canvas.restore();
            int height = getHeight() / 2;
            int itemHeight2 = (int) (((double) (getItemHeight() / 2)) * 1.2d);
            this.centerDrawable.setBounds(0, height - itemHeight2, getWidth(), height + itemHeight2);
            this.centerDrawable.draw(canvas);
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        LinearLayout linearLayout = this.itemsLayout;
        linearLayout.layout(0, 0, (i3 - i) - 20, i4 - i2);
    }

    public void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        LinearLayout linearLayout = this.itemsLayout;
        if (linearLayout != null) {
            d dVar = this.recycle;
            int i3 = this.firstItem;
            int i4 = 0;
            while (i4 < linearLayout.getChildCount()) {
                if (!(i3 >= 0 && i3 <= -1)) {
                    dVar.recycleView(linearLayout.getChildAt(i4), i3);
                    linearLayout.removeViewAt(i4);
                } else {
                    i4++;
                }
                i3++;
            }
        } else if (linearLayout == null) {
            LinearLayout linearLayout2 = new LinearLayout(getContext());
            this.itemsLayout = linearLayout2;
            linearLayout2.setOrientation(1);
        }
        int i5 = this.visibleItems / 2;
        for (int i6 = this.currentItem + i5; i6 >= this.currentItem - i5; i6--) {
            if (addViewItem(i6, true)) {
                this.firstItem = i6;
            }
        }
        int calculateLayoutWidth = calculateLayoutWidth(size, mode);
        if (mode2 != 1073741824) {
            LinearLayout linearLayout3 = this.itemsLayout;
            if (!(linearLayout3 == null || linearLayout3.getChildAt(0) == null)) {
                this.itemHeight = linearLayout3.getChildAt(0).getMeasuredHeight();
            }
            int i7 = this.itemHeight;
            int max = Math.max((this.visibleItems * i7) - ((i7 * 10) / 50), getSuggestedMinimumHeight());
            size2 = mode2 == Integer.MIN_VALUE ? Math.min(max, size2) : max;
        }
        setMeasuredDimension(calculateLayoutWidth, size2);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int y;
        int i;
        if (isEnabled() && getViewAdapter() != null) {
            int action = motionEvent.getAction();
            if (action != 1) {
                if (action == 2 && getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            } else if (!this.isScrollingPerformed) {
                int y2 = ((int) motionEvent.getY()) - (getHeight() / 2);
                if (y2 > 0) {
                    i = (getItemHeight() / 2) + y2;
                } else {
                    i = y2 - (getItemHeight() / 2);
                }
                int itemHeight2 = i / getItemHeight();
                if (itemHeight2 != 0 && isValidItemIndex(this.currentItem + itemHeight2)) {
                    int i2 = this.currentItem + itemHeight2;
                    for (OnWheelClickedListener onItemClicked : this.clickingListeners) {
                        onItemClicked.onItemClicked(this, i2);
                    }
                }
            }
            WheelScroller wheelScroller = this.scroller;
            Objects.requireNonNull(wheelScroller);
            int action2 = motionEvent.getAction();
            if (action2 == 0) {
                wheelScroller.lastTouchedY = motionEvent.getY();
                wheelScroller.scroller.forceFinished(true);
                wheelScroller.animationHandler.removeMessages(0);
                wheelScroller.animationHandler.removeMessages(1);
            } else if (action2 == 2 && (y = (int) (motionEvent.getY() - wheelScroller.lastTouchedY)) != 0) {
                wheelScroller.startScrolling();
                ((AnonymousClass1) wheelScroller.listener).onScroll(y);
                wheelScroller.lastTouchedY = motionEvent.getY();
            }
            if (!wheelScroller.gestureDetector.onTouchEvent(motionEvent) && motionEvent.getAction() == 1) {
                wheelScroller.justify();
            }
        }
        return true;
    }

    public void setCurrentItem(int i, boolean z) {
        int min;
        WheelViewAdapter wheelViewAdapter = this.viewAdapter;
        if (wheelViewAdapter != null && wheelViewAdapter.getItemsCount() != 0) {
            int itemsCount = this.viewAdapter.getItemsCount();
            if (i < 0 || i >= itemsCount) {
                if (this.isCyclic) {
                    while (i < 0) {
                        i += itemsCount;
                    }
                    i %= itemsCount;
                } else {
                    return;
                }
            }
            int i2 = this.currentItem;
            if (i == i2) {
                return;
            }
            if (z) {
                int i3 = i - i2;
                if (this.isCyclic && (min = (Math.min(i, i2) + itemsCount) - Math.max(i, this.currentItem)) < Math.abs(i3)) {
                    i3 = i3 < 0 ? min : -min;
                }
                this.scroller.scroll((i3 * getItemHeight()) - this.scrollingOffset, 0);
                return;
            }
            this.scrollingOffset = 0;
            this.currentItem = i;
            for (OnWheelChangedListener onChanged : this.changingListeners) {
                onChanged.onChanged(this, i2, i);
            }
            playSoundEffect(0);
            invalidate();
        }
    }

    public void setCyclic(boolean z) {
        this.isCyclic = z;
        invalidateWheel(false);
    }

    public void setInterpolator(Interpolator interpolator) {
        WheelScroller wheelScroller = this.scroller;
        wheelScroller.scroller.forceFinished(true);
        wheelScroller.scroller = new Scroller(wheelScroller.context, interpolator);
    }

    public void setViewAdapter(WheelViewAdapter wheelViewAdapter) {
        WheelViewAdapter wheelViewAdapter2 = this.viewAdapter;
        if (wheelViewAdapter2 != null) {
            wheelViewAdapter2.unregisterDataSetObserver(this.dataObserver);
        }
        this.viewAdapter = wheelViewAdapter;
        if (wheelViewAdapter != null) {
            wheelViewAdapter.registerDataSetObserver(this.dataObserver);
        }
        invalidateWheel(true);
    }

    public void setVisibleItems(int i) {
        this.visibleItems = i;
    }

    public void setCurrentItem(int i) {
        setCurrentItem(i, false);
    }
}
