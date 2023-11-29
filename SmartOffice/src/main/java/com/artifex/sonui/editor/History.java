package com.artifex.sonui.editor;

import java.util.ArrayList;

public class History {
    public int mItemIndex = -1;
    public ArrayList<HistoryItem> mItems = new ArrayList<>();

    public class HistoryItem {
        public float scale;
        public int scrollX;
        public int scrollY;

        public HistoryItem(History history, int i, int i2, float f) {
            this.scrollX = i;
            this.scrollY = i2;
            this.scale = f;
        }

        public float getScale() {
            return this.scale;
        }

        public int getScrollX() {
            return this.scrollX;
        }

        public int getScrollY() {
            return this.scrollY;
        }
    }

    public void add(int i, int i2, float f) {
        HistoryItem historyItem = new HistoryItem(this, i, i2, f);
        if (this.mItemIndex + 1 != this.mItems.size()) {
            this.mItems = new ArrayList<>(this.mItems.subList(0, this.mItemIndex + 1));
        }
        this.mItems.add(historyItem);
        this.mItemIndex = this.mItems.size() - 1;
    }

    public boolean canNext() {
        return this.mItemIndex < this.mItems.size() - 1;
    }

    public boolean canPrevious() {
        return this.mItemIndex > 0;
    }

    public HistoryItem current() {
        int i = this.mItemIndex;
        if (i < 0) {
            return null;
        }
        return this.mItems.get(i);
    }

    public HistoryItem next() {
        if (!canNext()) {
            return null;
        }
        int i = this.mItemIndex + 1;
        this.mItemIndex = i;
        return this.mItems.get(i);
    }

    public HistoryItem previous() {
        if (!canPrevious()) {
            return null;
        }
        int i = this.mItemIndex - 1;
        this.mItemIndex = i;
        return this.mItems.get(i);
    }
}
