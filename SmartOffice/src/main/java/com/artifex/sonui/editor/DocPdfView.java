package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.core.content.ContextCompat;
import com.artifex.mupdf.fitz.PDFDocument;
import com.artifex.mupdf.fitz.PDFWidget;
import com.artifex.mupdf.fitz.Quad;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.ConfigOptions;
import com.artifex.solib.MuPDFAnnotation;
import com.artifex.solib.MuPDFDoc;
import com.artifex.solib.MuPDFPage;
import com.artifex.solib.MuPDFWidget;
import com.artifex.solib.Worker;
import com.artifex.sonui.editor.NoteEditor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class DocPdfView extends DocView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int currentSignatureIndex = -1;
    public DragHandleListener dragListener = new DragHandleListener() {
        public void onDrag(DragHandle dragHandle) {
            if (dragHandle == DocPdfView.this.mDragHandleMove) {
                Point point = new Point(dragHandle.getPosition());
                int i = point.x;
                Point point2 = DocPdfView.this.mMovingPoint;
                int i2 = i - point2.x;
                int i3 = point.y - point2.y;
                Rect rect = DocPdfView.this.mMovingRectAtStart;
                Point point3 = new Point(rect.left + i2, rect.top + i3);
                Rect rect2 = DocPdfView.this.mMovingRectAtStart;
                Point point4 = new Point(rect2.right + i2, rect2.bottom + i3);
                DocPdfView.access$2000(DocPdfView.this, point3, point4, true);
                DocPdfView.this.moveResizingView(point3, point4);
            } else {
                Point point5 = new Point(dragHandle.getPosition());
                int i4 = point5.x;
                DocPdfView docPdfView = DocPdfView.this;
                Point point6 = docPdfView.mDragHandlePointAtStart;
                int i5 = i4 - point6.x;
                int i6 = point5.y - point6.y;
                Point point7 = docPdfView.mResizingMovingPoint;
                Point point8 = docPdfView.mResizingMovingPointAtStart;
                point7.x = point8.x + i5;
                point7.y = point8.y + i6;
                DocPdfView.access$2000(docPdfView, docPdfView.mResizingFixedPoint, point7, false);
                DocPdfView docPdfView2 = DocPdfView.this;
                docPdfView2.moveResizingView(docPdfView2.mResizingFixedPoint, docPdfView2.mResizingMovingPoint);
            }
            DocPdfView docPdfView3 = DocPdfView.this;
            if (!docPdfView3.resizingTextRedaction) {
                DocPageView docPageView = docPdfView3.mResizingPage;
                Rect rect3 = DocPdfView.this.mResizingRect;
                Point screenToPage = docPageView.screenToPage(new Point(rect3.left, rect3.top));
                DocPageView docPageView2 = DocPdfView.this.mResizingPage;
                Rect rect4 = DocPdfView.this.mResizingRect;
                Point screenToPage2 = docPageView2.screenToPage(new Point(rect4.right, rect4.bottom));
                DocPageView docPageView3 = DocPdfView.this.mResizingPage;
                Rect rect5 = DocPdfView.this.mResizingRect;
                Point screenToPage3 = docPageView3.screenToPage(new Point((rect5.right + rect5.left) / 2, rect5.bottom));
                DocPdfView docPdfView4 = DocPdfView.this;
                docPdfView4.positionHandle(docPdfView4.mDragHandleTopLeft, docPdfView4.mResizingPage, screenToPage.x, screenToPage.y);
                DocPdfView docPdfView5 = DocPdfView.this;
                docPdfView5.positionHandle(docPdfView5.mDragHandleBottomRight, docPdfView5.mResizingPage, screenToPage2.x, screenToPage2.y);
                DocPdfView docPdfView6 = DocPdfView.this;
                docPdfView6.positionHandle(docPdfView6.mDragHandleMove, docPdfView6.mResizingPage, screenToPage3.x, screenToPage3.y);
            }
            MuPDFDoc muPDFDoc = (MuPDFDoc) DocPdfView.this.getDoc();
            DocPdfView docPdfView7 = DocPdfView.this;
            docPdfView7.mResizingPage.screenToPage(docPdfView7.mResizingRect);
            DocPdfView docPdfView8 = DocPdfView.this;
            if (docPdfView8.resizingRedaction && docPdfView8.resizingTextRedaction) {
                docPdfView8.mResizingView.setVisibility(4);
                DocPdfView docPdfView9 = DocPdfView.this;
                docPdfView9.updateTextSelectionRects(docPdfView9.mResizingPage, dragHandle);
            }
        }

        public void onEndDrag(DragHandle dragHandle) {
            MuPDFDoc muPDFDoc = (MuPDFDoc) DocPdfView.this.getDoc();
            DocPdfView docPdfView = DocPdfView.this;
            int i = DocPdfView.$r8$clinit;
            docPdfView.enforceMinResizeDimension();
            DocPdfView docPdfView2 = DocPdfView.this;
            Rect screenToPage = docPdfView2.mResizingPage.screenToPage(docPdfView2.mResizingRect);
            DocPdfView docPdfView3 = DocPdfView.this;
            MuPDFWidget muPDFWidget = docPdfView3.sigEditingWidget;
            if (muPDFWidget != null) {
                muPDFWidget.safeBounds = screenToPage;
                muPDFWidget.mDoc.mWorker.add(new Worker.Task(screenToPage) {
                    public final /* synthetic */ Rect val$r;

                    {
                        this.val$r = r2;
                    }

                    public void run() {
                    }

                    public void work() {
                        PDFWidget pDFWidget = MuPDFWidget.this.mWidget;
                        Rect rect = this.val$r;
                        pDFWidget.setRect(new com.artifex.mupdf.fitz.Rect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom));
                        MuPDFWidget.this.mWidget.update();
                    }
                });
                muPDFDoc.update(DocPdfView.this.mResizingPage.getPageNumber());
            } else if (docPdfView3.sigEditingAnnot != null) {
                MuPDFPage muPDFPage = (MuPDFPage) docPdfView3.sigEditingPage.mPage;
                int i2 = docPdfView3.sigEditingAnnotIndex;
                if (i2 < muPDFPage.mAnnotations.size()) {
                    muPDFPage.mDoc.mWorker.add(new Worker.Task(muPDFPage.mAnnotations.get(i2), new com.artifex.mupdf.fitz.Rect((float) screenToPage.left, (float) screenToPage.top, (float) screenToPage.right, (float) screenToPage.bottom)) {
                        public final /* synthetic */ com.artifex.mupdf.fitz.Rect val$fr;
                        public final /* synthetic */ MuPDFAnnotation val$mAnnot;

                        public void run(
/*
Method generation error in method: com.artifex.solib.MuPDFPage.4.run():void, dex: classes.dex
                        jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFPage.4.run():void, class status: UNLOADED
                        	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
                        	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.util.ArrayList.forEach(ArrayList.java:1259)
                        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                        	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                        	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                        	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                        	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                        	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                        	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                        	at jadx.core.codegen.RegionGen.connectElseIf(RegionGen.java:175)
                        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:152)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.util.ArrayList.forEach(ArrayList.java:1259)
                        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                        	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                        	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                        	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                        	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                        	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                        	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:98)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:480)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                        	at jadx.core.codegen.ClassGen.addInsnBody(ClassGen.java:437)
                        	at jadx.core.codegen.ClassGen.addField(ClassGen.java:378)
                        	at jadx.core.codegen.ClassGen.addFields(ClassGen.java:348)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:226)
                        	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                        	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                        	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                        	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                        	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                        
*/

                        public void work(
/*
Method generation error in method: com.artifex.solib.MuPDFPage.4.work():void, dex: classes.dex
                        jadx.core.utils.exceptions.JadxRuntimeException: Method args not loaded: com.artifex.solib.MuPDFPage.4.work():void, class status: UNLOADED
                        	at jadx.core.dex.nodes.MethodNode.getArgRegs(MethodNode.java:278)
                        	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:116)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:313)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.util.ArrayList.forEach(ArrayList.java:1259)
                        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                        	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                        	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                        	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                        	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                        	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                        	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                        	at jadx.core.codegen.RegionGen.connectElseIf(RegionGen.java:175)
                        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:152)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.util.ArrayList.forEach(ArrayList.java:1259)
                        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                        	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                        	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                        	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                        	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                        	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                        	at jadx.core.codegen.InsnGen.inlineAnonymousConstructor(InsnGen.java:676)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:607)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:98)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:480)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                        	at jadx.core.codegen.ClassGen.addInsnBody(ClassGen.java:437)
                        	at jadx.core.codegen.ClassGen.addField(ClassGen.java:378)
                        	at jadx.core.codegen.ClassGen.addFields(ClassGen.java:348)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:226)
                        	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                        	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                        	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                        	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                        	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                        
*/
                    });
                }
                muPDFDoc.update(DocPdfView.this.mResizingPage.getPageNumber());
            } else {
                if (docPdfView3.resizingRedaction) {
                    MuPDFPage muPDFPage2 = (MuPDFPage) docPdfView3.mResizingPage.getPage();
                    if (DocPdfView.this.resizingTextRedaction) {
                        muPDFPage2.updateSelectedRedaction((Rect) null);
                    } else {
                        muPDFPage2.updateSelectedRedaction(screenToPage);
                    }
                } else {
                    muPDFDoc.mWorker.add(new Worker.Task(new RectF(screenToPage)) {
                        public final /* synthetic */ RectF val$bounds;

                        {
                            this.val$bounds = r2;
                        }

                        public void run() {
                        }

                        public void work() {
                            MuPDFAnnotation selectedAnnotation = MuPDFDoc.this.getSelectedAnnotation();
                            if (selectedAnnotation != null) {
                                RectF rectF = this.val$bounds;
                                com.artifex.mupdf.fitz.Rect rect = new com.artifex.mupdf.fitz.Rect(rectF.left, rectF.top, rectF.right, rectF.bottom);
                                selectedAnnotation.mDoc.checkForWorkerThread();
                                selectedAnnotation.rect = rect;
                                selectedAnnotation.mAnnotation.setRect(rect);
                                Quad[] rectsToQuads = MuPDFPage.rectsToQuads(new com.artifex.mupdf.fitz.Rect[]{rect});
                                selectedAnnotation.mDoc.checkForWorkerThread();
                                selectedAnnotation.mQuadPoints = rectsToQuads;
                                selectedAnnotation.mAnnotation.setQuadPoints(rectsToQuads);
                                Date date = new Date();
                                selectedAnnotation.mDoc.checkForWorkerThread();
                                selectedAnnotation.mModDate = date;
                                selectedAnnotation.mAnnotation.setModificationDate(date);
                                MuPDFDoc muPDFDoc = MuPDFDoc.this;
                                muPDFDoc.update(muPDFDoc.selectedAnnotPagenum);
                            }
                        }
                    });
                }
                DocPdfView.this.mResizingView.setVisibility(View.GONE);
                DocPdfView.this.mResizingPage = null;
            }
            DocPdfView docPdfView4 = DocPdfView.this;
            docPdfView4.resizingRedaction = false;
            docPdfView4.resizingTextRedaction = false;
            docPdfView4.dragging = false;
        }

        public void onStartDrag(DragHandle dragHandle) {
            DocPdfView docPdfView = DocPdfView.this;
            docPdfView.dragging = true;
            if (docPdfView.sigEditingWidget != null) {
                docPdfView.matchResizeRectToSig();
            } else if (docPdfView.sigEditingAnnot != null) {
                docPdfView.matchResizeRectToESig();
            } else {
                DocPageView docPageView = docPdfView.mSelectionStartPage;
                if (docPageView == null || docPageView.getSelectionLimits() == null) {
                    DocPdfView.this.dragging = false;
                } else {
                    DocPdfView docPdfView2 = DocPdfView.this;
                    DocPageView docPageView2 = docPdfView2.mSelectionStartPage;
                    docPdfView2.mResizingPage = docPageView2;
                    RectF box = docPageView2.getSelectionLimits().getBox();
                    DocPdfView.this.mResizingRect = new Rect(DocPdfView.this.mSelectionStartPage.pageToView((int) box.left), DocPdfView.this.mSelectionStartPage.pageToView((int) box.top), DocPdfView.this.mSelectionStartPage.pageToView((int) box.right), DocPdfView.this.mSelectionStartPage.pageToView((int) box.bottom));
                    DocPdfView docPdfView3 = DocPdfView.this;
                    docPdfView3.mResizingRect.offset(docPdfView3.mSelectionStartPage.getLeft(), DocPdfView.this.mSelectionStartPage.getTop());
                    DocPdfView docPdfView4 = DocPdfView.this;
                    docPdfView4.mResizingRect.offset(-docPdfView4.getScrollX(), -DocPdfView.this.getScrollY());
                }
            }
            MuPDFDoc muPDFDoc = (MuPDFDoc) DocPdfView.this.getDoc();
            DocPdfView.this.resizingRedaction = muPDFDoc.selectionIsRedaction();
            DocPdfView.this.resizingTextRedaction = muPDFDoc.selectionIsTextRedaction();
            DocPdfView docPdfView5 = DocPdfView.this;
            if (dragHandle == docPdfView5.mDragHandleMove) {
                docPdfView5.mMovingPoint = new Point(dragHandle.getPosition());
                DocPdfView.this.mMovingRectAtStart = new Rect(DocPdfView.this.mResizingRect);
                DocPdfView.this.mResizingView.setVisibility(View.VISIBLE);
                return;
            }
            if (docPdfView5.resizingTextRedaction) {
                MuPDFPage muPDFPage = (MuPDFPage) docPdfView5.mResizingPage.mPage;
                Point pageToScreen = DocPdfView.this.mResizingPage.pageToScreen(muPDFPage.getSelectedAnnotStart());
                Point pageToScreen2 = DocPdfView.this.mResizingPage.pageToScreen(muPDFPage.getSelectedAnnotEnd());
                DocPdfView docPdfView6 = DocPdfView.this;
                if (dragHandle == docPdfView6.mDragHandleTopLeft) {
                    docPdfView6.mResizingFixedPoint = pageToScreen2;
                    docPdfView6.mResizingMovingPoint = pageToScreen;
                } else {
                    docPdfView6.mResizingFixedPoint = pageToScreen;
                    docPdfView6.mResizingMovingPoint = pageToScreen2;
                }
            } else if (dragHandle == docPdfView5.mDragHandleTopLeft) {
                Rect rect = DocPdfView.this.mResizingRect;
                docPdfView5.mResizingFixedPoint = new Point(rect.right, rect.bottom);
                DocPdfView docPdfView7 = DocPdfView.this;
                Rect rect2 = DocPdfView.this.mResizingRect;
                docPdfView7.mResizingMovingPoint = new Point(rect2.left, rect2.top);
            } else {
                Rect rect3 = DocPdfView.this.mResizingRect;
                docPdfView5.mResizingMovingPoint = new Point(rect3.right, rect3.bottom);
                DocPdfView docPdfView8 = DocPdfView.this;
                Rect rect4 = DocPdfView.this.mResizingRect;
                docPdfView8.mResizingFixedPoint = new Point(rect4.left, rect4.top);
            }
            DocPdfView.this.mDragHandlePointAtStart = new Point(dragHandle.getPosition());
            DocPdfView.this.mResizingMovingPointAtStart = new Point(DocPdfView.this.mResizingMovingPoint);
            DocPdfView docPdfView9 = DocPdfView.this;
            if (docPdfView9.resizingTextRedaction) {
                docPdfView9.mResizingView.setVisibility(4);
                DocPdfView docPdfView10 = DocPdfView.this;
                docPdfView10.updateTextSelectionRects(docPdfView10.mResizingPage, dragHandle);
                return;
            }
            docPdfView9.moveResizingView(docPdfView9.mResizingFixedPoint, docPdfView9.mResizingMovingPoint);
            DocPdfView.this.mResizingView.setVisibility(View.VISIBLE);
        }
    };
    public boolean dragging = false;
    public DocPdfPageView esigCreatedPage = null;
    public DragHandle mDragHandleBottomRight = null;
    public DragHandle mDragHandleMove = null;
    public Point mDragHandlePointAtStart;
    public DragHandle mDragHandleTopLeft = null;
    public boolean mESignatureMode = false;
    public DocMuPdfPageView mFormEditorPage = null;
    public boolean mMarkAreaMode = false;
    public boolean mMarkTextDragging = false;
    public boolean mMarkTextMode = false;
    public Point mMovingPoint;
    public Rect mMovingRectAtStart = new Rect();
    public NoteEditor mNoteEditor = null;
    public boolean mNoteMode = false;
    public Point mResizingFixedPoint;
    public Point mResizingMovingPoint;
    public Point mResizingMovingPointAtStart;
    public DocPageView mResizingPage = null;
    public Rect mResizingRect = new Rect();
    public View mResizingView;
    public boolean mSignatureMode = false;
    public ArrayList<Signature> mSignatures = null;
    public int minResizeDimension = 0;
    public boolean resizingRedaction = false;
    public boolean resizingTextRedaction = false;
    public DocPdfPageView sigCreatedPage = null;
    public DocPdfPageView sigDeletingPage = null;
    public MuPDFAnnotation sigEditingAnnot = null;
    public int sigEditingAnnotIndex = -1;
    public DocPdfPageView sigEditingPage = null;
    public MuPDFWidget sigEditingWidget = null;

    public class Signature {
        public int indexOnPage;
        public DocMuPdfPageView page;
        public MuPDFWidget signature;

        public Signature(DocPdfView docPdfView, AnonymousClass1 r2) {
        }
    }

    public DocPdfView(Context context) {
        super(context);
        init();
    }

    public static void access$2000(DocPdfView docPdfView, Point point, Point point2, boolean z) {
        Objects.requireNonNull(docPdfView);
        Rect rect = new Rect(point.x, point.y, point2.x, point2.y);
        int[] iArr = new int[2];
        docPdfView.getLocationInWindow(iArr);
        rect.offset(iArr[0], iArr[1]);
        Rect screenRect = docPdfView.mResizingPage.screenRect();
        if (z) {
            int i = rect.left;
            int i2 = screenRect.left;
            if (i < i2) {
                int i3 = i2 - i;
                point.x += i3;
                point2.x = i3 + point2.x;
            }
            int i4 = rect.right;
            int i5 = screenRect.right;
            if (i4 > i5) {
                int i6 = i4 - i5;
                point.x -= i6;
                point2.x -= i6;
            }
            int i7 = rect.top;
            int i8 = screenRect.top;
            if (i7 < i8) {
                int i9 = i8 - i7;
                point.y += i9;
                point2.y = i9 + point2.y;
            }
            int i10 = rect.bottom;
            int i11 = screenRect.bottom;
            if (i10 > i11) {
                int i12 = i10 - i11;
                point.y -= i12;
                point2.y -= i12;
                return;
            }
            return;
        }
        int i13 = rect.left;
        int i14 = screenRect.left;
        if (i13 < i14) {
            point.x = (i14 - i13) + point.x;
        }
        int i15 = rect.right;
        int i16 = screenRect.right;
        if (i15 > i16) {
            point2.x -= i15 - i16;
        }
        int i17 = rect.top;
        int i18 = screenRect.top;
        if (i17 < i18) {
            point.y = (i18 - i17) + point.y;
        }
        int i19 = rect.bottom;
        int i20 = screenRect.bottom;
        if (i19 > i20) {
            point2.y -= i19 - i20;
        }
    }

    private DragHandle setupHandle(RelativeLayout relativeLayout, int i) {
        DragHandle dragHandle;
        if (i == 7) {
            dragHandle = new DragHandle(getContext(), R.layout.sodk_editor_drag_handle, i);
        } else {
            dragHandle = new DragHandle(getContext(), R.layout.sodk_editor_resize_handle, i);
        }
        relativeLayout.addView(dragHandle);
        dragHandle.show(false);
        dragHandle.setDragHandleListener(this.dragListener);
        return dragHandle;
    }

    public void afterRedo() {
        afterUndoRedo();
    }

    public void afterUndo() {
        afterUndoRedo();
    }

    public final void afterUndoRedo() {
        ((MuPDFDoc) getDoc()).updatePages();
        for (int i = 0; i < getPageCount(); i++) {
            ((DocMuPdfPageView) getOrCreateChild(i)).afterUndoRedo();
        }
        ((NUIDocView) this.mHostActivity).onDocCompleted();
        ((NUIDocView) this.mHostActivity).onSelectionChanged();
        resetModes();
        triggerRender();
    }

    public void beforeRedo() {
        for (int i = 0; i < getPageCount(); i++) {
            ((DocMuPdfPageView) getOrCreateChild(i)).beforeUndoRedo();
        }
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        muPDFDoc.selectedAnnotPagenum = -1;
        muPDFDoc.selectedAnnotIndex = -1;
        if (getDrawMode()) {
            onDrawMode();
            onDrawMode();
        }
        this.mFormEditorPage = null;
    }

    public void beforeUndo() {
        for (int i = 0; i < getPageCount(); i++) {
            ((DocMuPdfPageView) getOrCreateChild(i)).beforeUndoRedo();
        }
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        muPDFDoc.selectedAnnotPagenum = -1;
        muPDFDoc.selectedAnnotIndex = -1;
        if (getDrawMode()) {
            onDrawMode();
            onDrawMode();
        }
        this.mFormEditorPage = null;
    }

    public boolean canEditText() {
        return false;
    }

    public boolean canSelectionSpanPages() {
        return false;
    }

    public final boolean clearSignatureEditing() {
        boolean z = (this.sigEditingWidget == null && this.sigEditingPage == null && this.sigEditingAnnot == null && this.sigEditingAnnotIndex == -1) ? false : true;
        this.sigEditingWidget = null;
        this.sigEditingPage = null;
        this.sigEditingAnnot = null;
        this.sigEditingAnnotIndex = -1;
        this.mResizingView.setVisibility(View.GONE);
        hideHandles();
        return z;
    }

    public void collectSignatures() {
        collectSignatures(false);
    }

    public int countSignatures() {
        collectSignatures();
        return this.mSignatures.size();
    }

    public void doDoubleTap(float f, float f2) {
        if (!((NUIDocView) this.mHostActivity).isFullScreen()) {
            doDoubleTap2(f, f2);
        }
    }

    public boolean doPreclearCheck() {
        if (!shouldPreclearSelection()) {
            return false;
        }
        boolean clearAreaSelection = clearAreaSelection();
        boolean clearSignatureEditing = clearSignatureEditing();
        if (!clearAreaSelection && !clearSignatureEditing) {
            return false;
        }
        Utilities.hideKeyboard(getContext());
        return true;
    }

    public void doReposition(DocPdfPageView docPdfPageView, MuPDFWidget muPDFWidget) {
        if (muPDFWidget != null && docPdfPageView != null) {
            this.sigEditingWidget = muPDFWidget;
            this.sigEditingPage = docPdfPageView;
            this.mResizingPage = docPdfPageView;
            this.mSelectionEndPage = docPdfPageView;
            this.mSelectionStartPage = docPdfPageView;
            this.mResizingView.setVisibility(View.VISIBLE);
            matchResizeRectToSig();
            Rect rect = this.mResizingRect;
            moveResizingView(rect.left, rect.top, rect.width(), this.mResizingRect.height());
            showHandles();
        }
    }

    public final void enforceMinResizeDimension() {
        int width = this.mResizingRect.width();
        int i = this.minResizeDimension;
        if (width < i) {
            int width2 = i - this.mResizingRect.width();
            Rect rect = this.mResizingRect;
            int i2 = width2 / 2;
            rect.left -= i2;
            rect.right += i2;
        }
        int height = this.mResizingRect.height();
        int i3 = this.minResizeDimension;
        if (height < i3) {
            int height2 = i3 - this.mResizingRect.height();
            Rect rect2 = this.mResizingRect;
            int i4 = height2 / 2;
            rect2.top -= i4;
            rect2.bottom += i4;
        }
    }

    public boolean getESignatureMode() {
        return this.mESignatureMode;
    }

    public boolean getMarkAreaMode() {
        return this.mMarkAreaMode;
    }

    public boolean getMarkTextMode() {
        return this.mMarkTextMode;
    }

    public boolean getNoteMode() {
        return this.mNoteMode;
    }

    public int getSignatureCount() {
        ArrayList<Signature> arrayList = this.mSignatures;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public boolean getSignatureMode() {
        return this.mSignatureMode;
    }

    public boolean handleFullscreenTap(float f, float f2) {
        Point eventToScreen = eventToScreen(f, f2);
        DocPageView findPageViewContainingPoint = findPageViewContainingPoint(eventToScreen.x, eventToScreen.y, false);
        if (findPageViewContainingPoint == null) {
            return false;
        }
        Point screenToPage = findPageViewContainingPoint.screenToPage(eventToScreen);
        return findPageViewContainingPoint.handleFullscreenTap(screenToPage.x, screenToPage.y);
    }

    public void hideHandles() {
        super.hideHandles();
        showHandle(this.mDragHandleTopLeft, false);
        showHandle(this.mDragHandleBottomRight, false);
        showHandle(this.mDragHandleMove, false);
    }

    public final void highlightSignature(int i) {
        Signature signature = this.mSignatures.get(i);
        signature.page.setHighlightedSig(signature.indexOnPage);
        scrollBoxIntoView(signature.page.getPageNumber(), new RectF(signature.signature.getBounds()), true);
    }

    public final void init() {
        this.minResizeDimension = Utilities.inchesToPixels(getContext(), 0.2f);
    }

    public final void matchResizeRectToESig() {
        Rect annotationRect = ((MuPDFPage) this.sigEditingPage.mPage).getAnnotationRect(this.sigEditingAnnotIndex);
        if (annotationRect != null) {
            Rect rect = new Rect(this.sigEditingPage.pageToView(annotationRect.left), this.sigEditingPage.pageToView(annotationRect.top), this.sigEditingPage.pageToView(annotationRect.right), this.sigEditingPage.pageToView(annotationRect.bottom));
            this.mResizingRect = rect;
            rect.offset(this.sigEditingPage.getLeft(), this.sigEditingPage.getTop());
            this.mResizingRect.offset(-getScrollX(), -getScrollY());
        }
    }

    public final void matchResizeRectToSig() {
        Rect bounds = this.sigEditingWidget.getBounds();
        Rect rect = new Rect(this.sigEditingPage.pageToView(bounds.left), this.sigEditingPage.pageToView(bounds.top), this.sigEditingPage.pageToView(bounds.right), this.sigEditingPage.pageToView(bounds.bottom));
        this.mResizingRect = rect;
        rect.offset(this.sigEditingPage.getLeft(), this.sigEditingPage.getTop());
        this.mResizingRect.offset(-getScrollX(), -getScrollY());
    }

    public void moveHandlesToCorners() {
        ArDkDoc doc = getDoc();
        if (doc != null && (doc instanceof MuPDFDoc)) {
            MuPDFDoc muPDFDoc = (MuPDFDoc) doc;
            boolean selectionIsRedaction = muPDFDoc.selectionIsRedaction();
            if (muPDFDoc.selectionIsTextRedaction()) {
                if (!this.dragging && (this.mSelectionStartPage.getPage() instanceof MuPDFPage)) {
                    MuPDFPage muPDFPage = (MuPDFPage) this.mSelectionStartPage.getPage();
                    Point selectedAnnotStart = muPDFPage.getSelectedAnnotStart();
                    Point selectedAnnotEnd = muPDFPage.getSelectedAnnotEnd();
                    if (selectedAnnotStart != null && selectedAnnotEnd != null) {
                        positionHandle(this.mDragHandleTopLeft, this.mSelectionStartPage, selectedAnnotStart.x, selectedAnnotStart.y);
                        positionHandle(this.mDragHandleBottomRight, this.mSelectionStartPage, selectedAnnotEnd.x, selectedAnnotEnd.y);
                    }
                }
            } else if (getMarkTextMode()) {
                if (this.mMarkTextDragging) {
                    Point screenToPage = this.mSelectionStartPage.screenToPage(this.mResizingFixedPoint);
                    Point screenToPage2 = this.mSelectionEndPage.screenToPage(this.mResizingMovingPoint);
                    positionHandle(this.mDragHandleTopLeft, this.mSelectionStartPage, screenToPage.x, screenToPage.y);
                    positionHandle(this.mDragHandleBottomRight, this.mSelectionEndPage, screenToPage2.x, screenToPage2.y);
                }
            } else if (selectionIsRedaction) {
                ArDkSelectionLimits selectionLimits = getSelectionLimits();
                if (selectionLimits != null) {
                    positionHandle(this.mDragHandleTopLeft, this.mSelectionStartPage, (int) selectionLimits.getStart().x, (int) selectionLimits.getStart().y);
                    positionHandle(this.mDragHandleBottomRight, this.mSelectionEndPage, (int) selectionLimits.getEnd().x, (int) selectionLimits.getEnd().y);
                    positionHandle(this.mDragHandleMove, this.mSelectionEndPage, (((int) selectionLimits.getStart().x) + ((int) selectionLimits.getEnd().x)) / 2, (int) selectionLimits.getEnd().y);
                }
            } else {
                MuPDFWidget muPDFWidget = this.sigEditingWidget;
                if (muPDFWidget != null) {
                    Rect bounds = muPDFWidget.getBounds();
                    positionHandle(this.mDragHandleTopLeft, this.sigEditingPage, bounds.left, bounds.top);
                    positionHandle(this.mDragHandleBottomRight, this.sigEditingPage, bounds.right, bounds.bottom);
                    positionHandle(this.mDragHandleMove, this.sigEditingPage, (bounds.left + bounds.right) / 2, bounds.bottom);
                } else if (this.sigEditingAnnot != null) {
                    Rect annotationRect = ((MuPDFPage) this.sigEditingPage.mPage).getAnnotationRect(this.sigEditingAnnotIndex);
                    positionHandle(this.mDragHandleTopLeft, this.sigEditingPage, annotationRect.left, annotationRect.top);
                    positionHandle(this.mDragHandleBottomRight, this.sigEditingPage, annotationRect.right, annotationRect.bottom);
                    positionHandle(this.mDragHandleMove, this.sigEditingPage, (annotationRect.left + annotationRect.right) / 2, annotationRect.bottom);
                } else if (!this.mDragging) {
                    super.moveHandlesToCorners();
                }
            }
        }
    }

    public final void moveResizingView(Point point, Point point2) {
        moveResizingView(Math.min(point.x, point2.x), Math.min(point.y, point2.y), Math.abs(point.x - point2.x), Math.abs(point.y - point2.y));
    }

    public void onDrawMode() {
        super.onDrawMode();
        this.mNoteMode = false;
        this.mSignatureMode = false;
    }

    public void onESignatureMode() {
        setESignatureMode(!this.mESignatureMode);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (!finished()) {
            this.mNoteEditor.move();
            if (this.sigEditingWidget != null && !this.dragging) {
                matchResizeRectToSig();
                Rect rect = this.mResizingRect;
                moveResizingView(rect.left, rect.top, rect.width(), this.mResizingRect.height());
            }
            if (this.sigEditingAnnot != null && !this.dragging) {
                matchResizeRectToESig();
                Rect rect2 = this.mResizingRect;
                moveResizingView(rect2.left, rect2.top, rect2.width(), this.mResizingRect.height());
            }
        }
    }

    public void onNextSignature() {
        clearSignatureEditing();
        collectSignatures();
        int i = this.currentSignatureIndex;
        if (i >= 0) {
            this.mSignatures.get(i).page.setHighlightedSig(-1);
        }
        if (this.currentSignatureIndex + 1 < this.mSignatures.size()) {
            this.currentSignatureIndex++;
        } else {
            this.currentSignatureIndex = 0;
        }
        highlightSignature(this.currentSignatureIndex);
    }

    public void onNoteMode() {
        setNoteMode(!this.mNoteMode);
    }

    public void onPreviousSignature() {
        clearSignatureEditing();
        collectSignatures();
        int i = this.currentSignatureIndex;
        if (i >= 0) {
            this.mSignatures.get(i).page.setHighlightedSig(-1);
        }
        int i2 = this.currentSignatureIndex;
        if (i2 > 0) {
            this.currentSignatureIndex = i2 - 1;
        } else {
            this.currentSignatureIndex = this.mSignatures.size() - 1;
        }
        highlightSignature(this.currentSignatureIndex);
    }

    public void onReloadFile() {
        for (int i = 0; i < getPageCount(); i++) {
            ((DocMuPdfPageView) getOrCreateChild(i)).onReloadFile();
        }
    }

    public void onSelectionChanged() {
        super.onSelectionChanged();
        if (this.sigEditingWidget != null) {
            this.sigEditingPage.collectFormFields();
        }
        DocPdfPageView docPdfPageView = this.sigDeletingPage;
        if (docPdfPageView != null) {
            docPdfPageView.collectFormFields();
            this.sigDeletingPage = null;
            this.mSignatures = null;
            collectSignatures();
        }
        DocPdfPageView docPdfPageView2 = this.sigCreatedPage;
        if (docPdfPageView2 != null && this.sigEditingWidget == null) {
            docPdfPageView2.collectFormFields();
            this.mSignatures = null;
            this.currentSignatureIndex = -1;
            collectSignatures();
            MuPDFWidget newestWidget = this.sigCreatedPage.getNewestWidget();
            this.sigEditingWidget = newestWidget;
            if (newestWidget != null) {
                DocPdfPageView docPdfPageView3 = this.sigCreatedPage;
                this.sigEditingPage = docPdfPageView3;
                this.mResizingPage = docPdfPageView3;
                this.mResizingView.setVisibility(View.VISIBLE);
                matchResizeRectToSig();
                Rect rect = this.mResizingRect;
                moveResizingView(rect.left, rect.top, rect.width(), this.mResizingRect.height());
                showHandles();
            }
            this.sigCreatedPage = null;
        }
        DocPdfPageView docPdfPageView4 = this.esigCreatedPage;
        if (docPdfPageView4 != null) {
            MuPDFPage muPDFPage = (MuPDFPage) docPdfPageView4.mPage;
            CopyOnWriteArrayList<MuPDFAnnotation> copyOnWriteArrayList = muPDFPage.mAnnotations;
            int size = (copyOnWriteArrayList != null ? copyOnWriteArrayList.size() : 0) - 1;
            this.sigEditingAnnot = muPDFPage.getAnnotation(size);
            this.sigEditingAnnotIndex = size;
            DocPdfPageView docPdfPageView5 = this.esigCreatedPage;
            this.sigEditingPage = docPdfPageView5;
            this.mResizingPage = docPdfPageView5;
            this.mResizingView.setVisibility(View.VISIBLE);
            matchResizeRectToESig();
            Rect rect2 = this.mResizingRect;
            moveResizingView(rect2.left, rect2.top, rect2.width(), this.mResizingRect.height());
            showHandles();
            this.esigCreatedPage = null;
        }
        if (this.sigEditingAnnot != null) {
            DocPdfPageView docPdfPageView6 = this.sigEditingPage;
            this.mResizingPage = docPdfPageView6;
            this.mSelectionEndPage = docPdfPageView6;
            this.mSelectionStartPage = docPdfPageView6;
            this.mResizingView.setVisibility(View.VISIBLE);
            matchResizeRectToESig();
            Rect rect3 = this.mResizingRect;
            moveResizingView(rect3.left, rect3.top, rect3.width(), this.mResizingRect.height());
            showHandles();
        }
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        if (muPDFDoc != null && muPDFDoc.selectionIsTextRedaction()) {
            updateTextSelectionRects(this.mSelectionStartPage, (DragHandle) null);
        }
    }

    public void onSelectionDelete() {
        clearSignatureEditing();
    }

    public void onSignatureMode() {
        setSignatureMode(!this.mSignatureMode);
    }

    public boolean onSingleTap(float f, float f2, DocPageView docPageView) {
        DocPdfPageView docPdfPageView = (DocPdfPageView) docPageView;
        if (this.mNoteMode && docPdfPageView != null) {
            docPdfPageView.createNote(f, f2);
            this.mNoteMode = false;
            return true;
        } else if (this.mSignatureMode && docPdfPageView != null && this.sigEditingWidget == null) {
            this.sigCreatedPage = docPdfPageView;
            docPdfPageView.createSignatureAt(f, f2);
            this.mSignatureMode = false;
            return true;
        } else if (this.mESignatureMode) {
            this.esigCreatedPage = docPdfPageView;
            docPdfPageView.createESignatureAt(f, f2);
            this.mESignatureMode = false;
            return true;
        } else if (this.sigEditingWidget != null) {
            clearSignatureEditing();
            this.mSignatureMode = false;
            return true;
        } else if (this.sigEditingAnnot == null) {
            return false;
        } else {
            clearSignatureEditing();
            getDoc().clearSelection();
            this.mESignatureMode = false;
            return true;
        }
    }

    public void onSingleTapHandled(DocPageView docPageView) {
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        MuPDFAnnotation selectedAnnotation = muPDFDoc.getSelectedAnnotation();
        if (selectedAnnotation != null) {
            if (selectedAnnotation.type == 13) {
                this.sigEditingAnnot = selectedAnnotation;
                this.sigEditingAnnotIndex = muPDFDoc.getSelectedAnnotationIndex();
                this.sigEditingPage = (DocPdfPageView) docPageView;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (finished()) {
            return true;
        }
        if (getMarkAreaMode()) {
            return onTouchEventMarkArea(motionEvent);
        }
        if (getMarkTextMode()) {
            return onTouchEventMarkText(motionEvent);
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean onTouchEventMarkArea(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            Point eventToScreen = eventToScreen(motionEvent.getX(), motionEvent.getY());
            DocPageView findPageViewContainingPoint = findPageViewContainingPoint(eventToScreen.x, eventToScreen.y, false);
            this.mResizingPage = findPageViewContainingPoint;
            if (findPageViewContainingPoint != null) {
                this.mResizingMovingPoint = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                Point point = new Point(((int) motionEvent.getX()) - this.minResizeDimension, ((int) motionEvent.getY()) - this.minResizeDimension);
                this.mResizingFixedPoint = point;
                moveResizingView(point, this.mResizingMovingPoint);
                this.mResizingView.setVisibility(View.VISIBLE);
            }
        } else if (action != 1) {
            if (action == 2 && this.mResizingPage != null) {
                Point point2 = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                this.mResizingMovingPoint = point2;
                moveResizingView(this.mResizingFixedPoint, point2);
            }
        } else if (this.mResizingPage != null) {
            enforceMinResizeDimension();
            int addRedactAnnotation = ((DocMuPdfPageView) this.mResizingPage).addRedactAnnotation(this.mResizingRect);
            if (addRedactAnnotation != -1) {
                MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
                muPDFDoc.selectedAnnotPagenum = this.mResizingPage.getPageNumber();
                muPDFDoc.selectedAnnotIndex = addRedactAnnotation;
                muPDFDoc.mSelectionStartPage = this.mResizingPage.getPageNumber();
                muPDFDoc.mSelectionEndPage = this.mResizingPage.getPageNumber();
            }
            this.mResizingView.setVisibility(View.GONE);
            this.mMarkAreaMode = false;
            this.mResizingPage = null;
        }
        return true;
    }

    public boolean onTouchEventMarkText(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            Point eventToScreen = eventToScreen(motionEvent.getX(), motionEvent.getY());
            DocPageView findPageViewContainingPoint = findPageViewContainingPoint(eventToScreen.x, eventToScreen.y, false);
            this.mResizingPage = findPageViewContainingPoint;
            if (findPageViewContainingPoint != null) {
                this.mResizingMovingPoint = eventToScreen;
                this.mResizingFixedPoint = eventToScreen;
                findPageViewContainingPoint.setSelectionStart(eventToScreen);
                this.mResizingPage.setSelectionEnd(this.mResizingMovingPoint);
            }
        } else if (action != 1) {
            if (action == 2 && this.mResizingPage != null) {
                this.mResizingMovingPoint = eventToScreen(motionEvent.getX(), motionEvent.getY());
                this.mResizingPage.setSelectionStart(this.mResizingFixedPoint);
                this.mResizingPage.setSelectionEnd(this.mResizingMovingPoint);
                this.mMarkTextDragging = true;
                showHandle(this.mDragHandleTopLeft, true);
                showHandle(this.mDragHandleBottomRight, true);
                moveHandlesToCorners();
            }
        } else if (this.mResizingPage != null) {
            if (MuPDFPage.mTextSelPageNum != -1) {
                MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
                Objects.requireNonNull(muPDFDoc);
                int i = MuPDFPage.mTextSelPageNum;
                if (i != -1) {
                    muPDFDoc.mWorker.add(new Worker.Task(i, true) {
                        public final /* synthetic */ int val$pageNum;
                        public final /* synthetic */ boolean val$select;

                        {
                            this.val$pageNum = r2;
                            this.val$select = r3;
                        }

                        /* JADX WARNING: Code restructure failed: missing block: B:8:0x002e, code lost:
                            r2 = r0.mAnnotations.size() - 1;
                         */
                        /* Code decompiled incorrectly, please refer to instructions dump. */
                        public void run() {
                            /*
                                r5 = this;
                                com.artifex.solib.MuPDFDoc r0 = com.artifex.solib.MuPDFDoc.this
                                r0.clearSelection()
                                boolean r0 = r5.val$select
                                if (r0 == 0) goto L_0x006d
                                com.artifex.solib.MuPDFDoc r0 = com.artifex.solib.MuPDFDoc.this
                                r1 = -1
                                r0.selectedAnnotPagenum = r1
                                r0.selectedAnnotIndex = r1
                                java.util.ArrayList<com.artifex.solib.MuPDFPage> r0 = r0.mPages
                                int r2 = r5.val$pageNum
                                java.lang.Object r0 = r0.get(r2)
                                com.artifex.solib.MuPDFPage r0 = (com.artifex.solib.MuPDFPage) r0
                                com.artifex.mupdf.fitz.Page r2 = r0.mPage
                                com.artifex.mupdf.fitz.PDFPage r2 = com.artifex.solib.MuPDFPage.getPDFPage(r2)
                                if (r2 != 0) goto L_0x0023
                                goto L_0x006d
                            L_0x0023:
                                java.util.concurrent.CopyOnWriteArrayList<com.artifex.solib.MuPDFAnnotation> r2 = r0.mAnnotations
                                if (r2 == 0) goto L_0x006d
                                int r2 = r2.size()
                                if (r2 != 0) goto L_0x002e
                                goto L_0x006d
                            L_0x002e:
                                java.util.concurrent.CopyOnWriteArrayList<com.artifex.solib.MuPDFAnnotation> r2 = r0.mAnnotations
                                int r2 = r2.size()
                                int r2 = r2 + r1
                                java.util.concurrent.CopyOnWriteArrayList<com.artifex.solib.MuPDFAnnotation> r1 = r0.mAnnotations
                                java.lang.Object r1 = r1.get(r2)
                                com.artifex.solib.MuPDFAnnotation r1 = (com.artifex.solib.MuPDFAnnotation) r1
                                if (r1 == 0) goto L_0x006d
                                com.artifex.solib.MuPDFDoc r3 = r0.mDoc
                                int r4 = r0.mPageNumber
                                r3.selectedAnnotPagenum = r4
                                r3.selectedAnnotIndex = r2
                                com.artifex.mupdf.fitz.Rect r1 = r1.rect
                                java.util.ArrayList<com.artifex.solib.SOPageListener> r2 = r0.mPageListeners
                                java.util.Iterator r2 = r2.iterator()
                            L_0x004f:
                                boolean r3 = r2.hasNext()
                                if (r3 == 0) goto L_0x0063
                                java.lang.Object r3 = r2.next()
                                com.artifex.solib.SOPageListener r3 = (com.artifex.solib.SOPageListener) r3
                                android.graphics.RectF r4 = r0.toRectF(r1)
                                r3.update(r4)
                                goto L_0x004f
                            L_0x0063:
                                r0.updatePageRect(r1)
                                com.artifex.solib.MuPDFDoc r1 = r0.mDoc
                                int r0 = r0.mPageNumber
                                r1.onSelectionUpdate(r0)
                            L_0x006d:
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.MuPDFDoc.AnonymousClass16.run():void");
                        }

                        public void work() {
                            PDFDocument pDFDocument = MuPDFDoc.getPDFDocument(MuPDFDoc.this.mDocument);
                            pDFDocument.beginOperation("addRedactAnnotation");
                            MuPDFDoc.this.mPages.get(this.val$pageNum).addAnnotation(12, MuPDFDoc.this.mAuthor);
                            pDFDocument.endOperation();
                            MuPDFDoc.this.update(this.val$pageNum);
                            MuPDFDoc.access$2500(MuPDFDoc.this, this.val$pageNum);
                        }
                    });
                }
            }
            this.mResizingView.setVisibility(View.GONE);
            this.mMarkTextMode = false;
            this.mResizingPage = null;
            this.mMarkTextDragging = false;
            NUIDocView.currentNUIDocView().updateUIAppearance();
            hideHandles();
        }
        return true;
    }

    public void resetModes() {
        resetDrawMode();
        this.mNoteMode = false;
        NoteEditor noteEditor = this.mNoteEditor;
        if (noteEditor != null && noteEditor.isVisible()) {
            this.mNoteEditor.saveData();
            Utilities.hideKeyboard(getContext());
            this.mNoteEditor.hide();
        }
        clearAreaSelection();
        clearSignatureEditing();
        this.mSignatureMode = false;
        onSelectionChanged();
    }

    public void saveComment() {
    }

    public void saveNoteData() {
        if (this.mNoteEditor != null && ((MuPDFDoc) getDoc()).selectionIsType(0)) {
            this.mNoteEditor.saveData();
        }
    }

    public void setDeletingPage(DocPdfPageView docPdfPageView) {
        this.sigDeletingPage = docPdfPageView;
    }

    public void setDrawModeOff() {
        super.setDrawModeOff();
        clearSignatureEditing();
    }

    public void setESignatureMode(boolean z) {
        DocMuPdfPageView docMuPdfPageView = this.mFormEditorPage;
        if (docMuPdfPageView != null) {
            docMuPdfPageView.stopCurrentEditor();
            Utilities.hideKeyboard(getContext());
        }
        this.mFormEditorPage = null;
        this.mESignatureMode = z;
        this.mSignatureMode = false;
        this.mNoteMode = false;
        this.mDrawMode = false;
        clearAreaSelection();
        clearSignatureEditing();
        onSelectionChanged();
    }

    public void setMarkTextMode(boolean z) {
        this.mMarkTextMode = z;
        if (z) {
            this.mMarkAreaMode = false;
        }
        onSelectionChanged();
    }

    public void setNoteMode(boolean z) {
        this.mNoteMode = z;
        this.mDrawMode = false;
        this.mSignatureMode = false;
        clearAreaSelection();
        clearSignatureEditing();
        onSelectionChanged();
    }

    public void setSignatureMode(boolean z) {
        DocMuPdfPageView docMuPdfPageView = this.mFormEditorPage;
        if (docMuPdfPageView != null) {
            docMuPdfPageView.stopCurrentEditor();
            Utilities.hideKeyboard(getContext());
        }
        this.mFormEditorPage = null;
        this.mSignatureMode = z;
        this.mNoteMode = false;
        this.mDrawMode = false;
        clearAreaSelection();
        clearSignatureEditing();
        onSelectionChanged();
    }

    public void setup(RelativeLayout relativeLayout) {
        super.setup(relativeLayout);
    }

    public void setupHandles(RelativeLayout relativeLayout) {
        super.setupHandles(relativeLayout);
        this.mDragHandleMove = setupHandle(relativeLayout, 7);
        this.mDragHandleTopLeft = setupHandle(relativeLayout, 3);
        this.mDragHandleBottomRight = setupHandle(relativeLayout, 6);
        View view = new View(getContext());
        this.mResizingView = view;
        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sodk_editor_redact_resize_color));
        relativeLayout.addView(this.mResizingView);
        this.mResizingView.setVisibility(View.GONE);
    }

    public void setupNoteEditor() {
        this.mNoteEditor = new NoteEditor((Activity) getContext(), this, this.mHostActivity, new NoteEditor.NoteDataHandler() {
            public String getAuthor() {
                return DocPdfView.this.getDoc().getSelectionAnnotationAuthor();
            }

            public String getComment() {
                return DocPdfView.this.getDoc().getSelectionAnnotationComment();
            }

            public String getDate() {
                return DocPdfView.this.getDoc().getSelectionAnnotationDate();
            }

            public void setComment(String str) {
                DocPdfView.this.getDoc().setSelectionAnnotationComment(str);
                DocPdfView.this.mHostActivity.selectionupdated();
            }
        });
    }

    public void showHandles() {
        if (!getMarkTextMode()) {
            hideHandles();
        }
        MuPDFDoc muPDFDoc = getDoc() instanceof MuPDFDoc ? (MuPDFDoc) getDoc() : null;
        if (muPDFDoc == null) {
            super.showHandles();
            return;
        }
        boolean selectionIsRedaction = muPDFDoc.selectionIsRedaction();
        boolean selectionIsTextRedaction = muPDFDoc.selectionIsTextRedaction();
        boolean selectionIsType = muPDFDoc.selectionIsType(15);
        if (selectionIsRedaction || selectionIsTextRedaction || this.sigEditingWidget != null || this.sigEditingAnnot != null) {
            showHandle(this.mDragHandleTopLeft, true);
            showHandle(this.mDragHandleBottomRight, true);
            if (!selectionIsTextRedaction) {
                showHandle(this.mDragHandleMove, true);
            }
            moveHandlesToCorners();
        } else if (!selectionIsType && !getMarkTextMode()) {
            super.showHandles();
        }
    }

    public void showKeyboardAfterDoubleTap(Point point) {
    }

    public void toggleMarkAreaMode() {
        clearAreaSelection();
        clearSignatureEditing();
        boolean z = !this.mMarkAreaMode;
        this.mMarkAreaMode = z;
        if (z) {
            this.mMarkTextMode = false;
        }
        onSelectionChanged();
    }

    public void updateReview() {
        if (getDoc() == null) {
            Log.e("DocPdfView", "getDoc() returned NULL in updateReview");
        } else if (getDoc().getSelectionHasAssociatedPopup()) {
            this.mNoteEditor.show(getSelectionLimits(), this.mSelectionStartPage);
            this.mNoteEditor.move();
            ConfigOptions configOptions = this.mDocCfgOptions;
            if (configOptions == null || !configOptions.isPDFAnnotationEnabled()) {
                this.mNoteEditor.setCommentEditable(false);
            } else {
                this.mNoteEditor.setCommentEditable(true);
            }
            requestLayout();
        } else {
            NoteEditor noteEditor = this.mNoteEditor;
            if (noteEditor != null && noteEditor.isVisible()) {
                Utilities.hideKeyboard(getContext());
                this.mNoteEditor.hide();
            }
        }
    }

    public final void updateTextSelectionRects(DocPageView docPageView, DragHandle dragHandle) {
        Point point;
        Point point2;
        MuPDFPage muPDFPage = (MuPDFPage) docPageView.getPage();
        Point selectedAnnotStart = muPDFPage.getSelectedAnnotStart();
        selectedAnnotStart.offset(2, 2);
        Point selectedAnnotEnd = muPDFPage.getSelectedAnnotEnd();
        DragHandle dragHandle2 = this.mDragHandleTopLeft;
        if (dragHandle == dragHandle2) {
            Point position = dragHandle2.getPosition();
            position.x = this.mDragHandleTopLeft.getWidth() + position.x;
            position.y = this.mDragHandleTopLeft.getHeight() + position.y;
            point = viewToScreen(position);
            point2 = docPageView.pageToScreen(selectedAnnotEnd);
        } else if (dragHandle == this.mDragHandleBottomRight) {
            point = docPageView.pageToScreen(selectedAnnotStart);
            point2 = viewToScreen(this.mDragHandleBottomRight.getPosition());
        } else {
            point = docPageView.pageToScreen(selectedAnnotStart);
            point2 = docPageView.pageToScreen(selectedAnnotEnd);
        }
        ((DocMuPdfPageView) docPageView).updateSelectionRects(point, point2);
    }

    public void collectSignatures(boolean z) {
        if (this.mSignatures == null || z) {
            this.mSignatures = new ArrayList<>();
            for (int i = 0; i < getDoc().getNumPages(); i++) {
                DocMuPdfPageView docMuPdfPageView = (DocMuPdfPageView) getOrCreateChild(i);
                docMuPdfPageView.setHighlightedSig(-1);
                MuPDFWidget[] signatures = docMuPdfPageView.getSignatures();
                if (signatures != null && signatures.length > 0) {
                    for (int i2 = 0; i2 < signatures.length; i2++) {
                        Signature signature = new Signature(this, (AnonymousClass1) null);
                        signature.page = docMuPdfPageView;
                        signature.signature = signatures[i2];
                        signature.indexOnPage = i2;
                        this.mSignatures.add(signature);
                    }
                }
            }
        }
    }

    private void moveResizingView(int i, int i2, int i3, int i4) {
        int i5 = i + i3;
        int i6 = i2 + i4;
        Rect rect = new Rect(i, i2, i5, i6);
        int[] iArr = new int[2];
        getLocationInWindow(iArr);
        rect.offset(iArr[0], iArr[1]);
        this.mResizingRect.set(rect);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mResizingView.getLayoutParams();
        int height = i6 - getHeight();
        int i7 = height > 0 ? -height : 0;
        int width = i5 - getWidth();
        layoutParams.setMargins(i, i2, width > 0 ? -width : 0, i7);
        layoutParams.width = i3;
        layoutParams.height = i4;
        this.mResizingView.setLayoutParams(layoutParams);
        this.mResizingView.invalidate();
        if (!this.resizingTextRedaction) {
            this.mResizingView.setVisibility(View.VISIBLE);
        }
    }

    public DocPdfView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public DocPdfView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }
}
