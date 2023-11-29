package com.artifex.sonui.editor;

public interface DragHandleListener {
    void onDrag(DragHandle dragHandle);

    void onEndDrag(DragHandle dragHandle);

    void onStartDrag(DragHandle dragHandle);
}
