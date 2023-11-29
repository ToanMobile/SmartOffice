package com.artifex.mupdf.fitz;

import a.a.a.a.a.c$$ExternalSyntheticOutline0;
import androidx.constraintlayout.core.widgets.Barrier$$ExternalSyntheticOutline0;

public class Link {
    public Rect bounds;
    public String uri;

    public Link(Rect rect, String str) {
        this.bounds = rect;
        this.uri = str;
    }

    public boolean isExternal() {
        char charAt = this.uri.charAt(0);
        if ((charAt >= 'a' && charAt <= 'z') || (charAt >= 'A' && charAt <= 'Z')) {
            int i = 1;
            while (i < this.uri.length()) {
                char charAt2 = this.uri.charAt(i);
                if ((charAt2 >= 'a' && charAt2 <= 'z') || ((charAt2 >= 'A' && charAt2 <= 'Z') || ((charAt2 >= '0' && charAt2 <= '9') || charAt2 == '+' || charAt2 == '-' || charAt2 == '.'))) {
                    i++;
                } else if (charAt2 == ':') {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder m = c$$ExternalSyntheticOutline0.m("Link(bounds=");
        m.append(this.bounds);
        m.append(",uri=");
        return Barrier$$ExternalSyntheticOutline0.m(m, this.uri, ")");
    }
}
