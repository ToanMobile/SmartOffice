package com.artifex.mupdf.fitz;

import java.util.ArrayList;

public class StructuredText {
    public static final int SELECT_CHARS = 0;
    public static final int SELECT_LINES = 2;
    public static final int SELECT_WORDS = 1;
    private long pointer;

    public class BlockWalker implements StructuredTextWalker {
        public Rect blockBbox;
        public ArrayList<TextBlock> blocks = new ArrayList<>();
        public ArrayList<TextChar> chrs;
        public Rect lineBbox;
        public ArrayList<TextLine> lines;

        public BlockWalker() {
        }

        public void beginLine(Rect rect, int i) {
            this.chrs = new ArrayList<>();
            this.lineBbox = rect;
        }

        public void beginTextBlock(Rect rect) {
            this.lines = new ArrayList<>();
            this.blockBbox = rect;
        }

        public void endLine() {
            TextLine textLine = new TextLine();
            textLine.bbox = this.lineBbox;
            textLine.chars = (TextChar[]) this.chrs.toArray(new TextChar[0]);
            this.lines.add(textLine);
        }

        public void endTextBlock() {
            TextBlock textBlock = new TextBlock();
            textBlock.bbox = this.blockBbox;
            textBlock.lines = (TextLine[]) this.lines.toArray(new TextLine[0]);
            this.blocks.add(textBlock);
        }

        public void onChar(int i, Point point, Font font, float f, Quad quad) {
            TextChar textChar = new TextChar();
            textChar.c = i;
            textChar.quad = quad;
            this.chrs.add(textChar);
        }

        public void onImageBlock(Rect rect, Matrix matrix, Image image) {
        }
    }

    public static class TextBlock {
        public Rect bbox;
        public TextLine[] lines;
    }

    public static class TextChar {
        public int c;
        public Quad quad;

        public boolean isWhitespace() {
            return Character.isWhitespace(this.c);
        }
    }

    public static class TextLine {
        public Rect bbox;
        public TextChar[] chars;
    }

    static {
        Context.init();
    }

    private StructuredText(long j) {
        this.pointer = j;
    }

    public native String copy(Point point, Point point2);

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public TextBlock[] getBlocks() {
        BlockWalker blockWalker = new BlockWalker();
        walk(blockWalker);
        return (TextBlock[]) blockWalker.blocks.toArray(new TextBlock[0]);
    }

    public native Quad[] highlight(Point point, Point point2);

    public native Quad[][] search(String str);

    public native Quad snapSelection(Point point, Point point2, int i);

    public native void walk(StructuredTextWalker structuredTextWalker);
}
