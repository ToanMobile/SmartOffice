package com.artifex.mupdf.fitz;

import java.util.Objects;

public class Location {
    public final int chapter;
    public final int page;

    public Location(int i, int i2) {
        this.chapter = i;
        this.page = i2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Location)) {
            return false;
        }
        Location location = (Location) obj;
        if (this.chapter == location.chapter && this.page == location.page) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.chapter), Integer.valueOf(this.page)});
    }

    public String toString() {
        StringBuilder m = new StringBuilder("Location(chapter=");
        m.append(this.chapter);
        m.append(", page=");
        return m.toString();
        //return ConstraintWidget$$ExternalSyntheticOutline0.m(m, this.page, ")");
    }
}
