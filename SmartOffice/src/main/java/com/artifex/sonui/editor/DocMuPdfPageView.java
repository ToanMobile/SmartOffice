package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import androidx.core.content.ContextCompat;

import com.artifex.R;
import com.artifex.mupdf.fitz.Image;
import com.artifex.mupdf.fitz.PDFAnnotation;
import com.artifex.mupdf.fitz.PDFDocument;
import com.artifex.mupdf.fitz.PDFPage;
import com.artifex.mupdf.fitz.PDFWidget;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ConfigOptions;
import com.artifex.solib.MuPDFAnnotation;
import com.artifex.solib.MuPDFDoc;
import com.artifex.solib.MuPDFLib;
import com.artifex.solib.MuPDFPage;
import com.artifex.solib.MuPDFWidget;
import com.artifex.solib.SOPageListener;
import com.artifex.solib.SignatureAppearance;
import com.artifex.solib.Waiter;
import com.artifex.solib.Worker;
import com.artifex.sonui.editor.DocPageView;
import com.artifex.sonui.editor.NUIPKCS7Signer;
import com.artifex.sonui.editor.SignatureDialog;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class DocMuPdfPageView extends DocPdfPageView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean continueSigning = false;
    public int highlightedSigIndex = -1;
    public MuPDFWidget mEditingWidget = null;
    public int mEditingWidgetIndex = -1;
    public PDFFormEditor mFormEditor = null;
    public Rect[] mFormFieldBounds;
    public Paint mFormFieldPainter = null;
    public MuPDFWidget[] mFormFields;
    public boolean mFullscreen = false;
    public Paint mHighlightSignaturePainter = null;
    public Rect mHighlightingRect = new Rect();
    public Paint mSelectionHighlightPainter = null;
    public int numInitialFormFields = -1;

    public DocMuPdfPageView(Context context, ArDkDoc arDkDoc) {
        super(context, arDkDoc);
        Paint paint = new Paint();
        this.mSelectionHighlightPainter = paint;
        paint.setColor(ContextCompat.getColor(context, R.color.sodk_editor_text_highlight_color));
        this.mSelectionHighlightPainter.setStyle(Paint.Style.FILL);
        this.mSelectionHighlightPainter.setAlpha(getContext().getResources().getInteger(R.integer.sodk_editor_text_highlight_alpha));
        Paint paint2 = new Paint();
        this.mFormFieldPainter = paint2;
        paint2.setColor(ContextCompat.getColor(context, R.color.sodk_editor_form_field_color));
        this.mFormFieldPainter.setStyle(Paint.Style.FILL);
        this.mFormFieldPainter.setAlpha(getContext().getResources().getInteger(R.integer.sodk_editor_form_field_alpha));
        Paint paint3 = new Paint();
        this.mHighlightSignaturePainter = paint3;
        paint3.setColor(ContextCompat.getColor(context, R.color.sodk_editor_palette_green));
        this.mHighlightSignaturePainter.setStyle(Paint.Style.STROKE);
        this.mHighlightSignaturePainter.setStrokeWidth((float) Utilities.convertDpToPixel(3.0f));
    }

    public void access$200(DocMuPdfPageView docMuPdfPageView) {
        MuPDFWidget[] muPDFWidgetArr;
        int i;
        int i2 = docMuPdfPageView.mEditingWidgetIndex;
        if (i2 >= 0) {
            do {
                int i3 = docMuPdfPageView.mEditingWidgetIndex + 1;
                docMuPdfPageView.mEditingWidgetIndex = i3;
                muPDFWidgetArr = docMuPdfPageView.mFormFields;
                if (i3 >= muPDFWidgetArr.length) {
                    docMuPdfPageView.mEditingWidgetIndex = 0;
                }
                i = docMuPdfPageView.mEditingWidgetIndex;
                if (i == i2) {
                    return;
                }
            } while (muPDFWidgetArr[i].mKind == 1);
            docMuPdfPageView.mEditingWidget = muPDFWidgetArr[i];
            new Handler().post(new Runnable() {
                public void run() {
                    docMuPdfPageView.editWidget(docMuPdfPageView.mEditingWidget, false, (Point) null);
                }
            });
        }
    }

    public static void access$700(DocMuPdfPageView docMuPdfPageView, SignatureAppearance signatureAppearance, MuPDFWidget muPDFWidget, NUIPKCS7Signer nUIPKCS7Signer) {
        NUIDocView currentNUIDocView;
        final MuPDFDoc muPDFDoc = (MuPDFDoc) docMuPdfPageView.getDoc();
        muPDFDoc.mForceReload = true;
        if (docMuPdfPageView.mPage != null && (currentNUIDocView = NUIDocView.currentNUIDocView()) != null) {
            final SignatureAppearance signatureAppearance2 = signatureAppearance;
            currentNUIDocView.doSaveAs(false, new SOSaveAsComplete() {
                public void onComplete(int i, String str) {
                    ((NUIDocViewPdf) NUIDocView.currentNUIDocView()).setNeedsReload();
                    docMuPdfPageView.setSigningFlow(false);
                    muPDFDoc.update(docMuPdfPageView.getPageNumber());
                }

                public boolean onFilenameSelected(String str) {
                    final Waiter waiter = new Waiter();
                    muPDFDoc.mWorker.add(new Worker.Task() {
                        public void run() {
                        }

                        public void work() {
                            SignatureAppearance signatureAppearance = signatureAppearance2;
                            boolean z = false;
                            muPDFWidget.mDoc.checkForWorkerThread();
                            if (signatureAppearance != null) {
                                if (muPDFWidget.mWidget != null) {
                                    Image image = muPDFWidget.getImage(signatureAppearance);
                                    z = muPDFWidget.mWidget.sign(nUIPKCS7Signer, muPDFWidget.getFlags(signatureAppearance), image, signatureAppearance.reason, signatureAppearance.location);
                                }
                            } else {
                                PDFWidget pDFWidget = muPDFWidget.mWidget;
                                if (pDFWidget != null) {
                                    z = pDFWidget.sign(nUIPKCS7Signer);
                                }
                            }
                            if (z) {
                                muPDFWidget.mTimeSigned = System.currentTimeMillis();
                                muPDFWidget.mDoc.mIsModified = true;
                            }
                            waiter.value = z;
                            waiter.done();
                        }
                    });
                    waiter.doWait();
                    return waiter.value;
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void setSigningFlow(boolean z) {
        ((NUIDocViewPdf) NUIDocView.currentNUIDocView()).setSigningInProgress(z);
    }

    public int addRedactAnnotation(Rect rect) {
        CopyOnWriteArrayList<MuPDFAnnotation> copyOnWriteArrayList = ((MuPDFPage) this.mPage).mAnnotations;
        int size = copyOnWriteArrayList != null ? copyOnWriteArrayList.size() : 0;
        Point screenToPage = screenToPage(rect.left, rect.top);
        Point screenToPage2 = screenToPage(rect.right, rect.bottom);
        Rect rect2 = new Rect(screenToPage.x, screenToPage.y, screenToPage2.x, screenToPage2.y);
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        int pageNumber = getPageNumber();
        Objects.requireNonNull(muPDFDoc);
        if (pageNumber != -1) {
            muPDFDoc.mWorker.add(new Worker.Task() {
                public final /* synthetic */ int val$pageNum;
                public final /* synthetic */ Rect val$r;

                {
                    this.val$pageNum = pageNumber;
                    this.val$r = rect2;
                }

                public void run() {
                }

                public void work() {
                    PDFDocument pDFDocument = MuPDFDoc.getPDFDocument(muPDFDoc.mDocument);
                    pDFDocument.beginOperation("addRedactAnnotation");
                    MuPDFPage muPDFPage = muPDFDoc.mPages.get(this.val$pageNum);
                    Rect rect = this.val$r;
                    String str = muPDFDoc.mAuthor;
                    muPDFPage.mDoc.checkForWorkerThread();
                    PDFPage pDFPage = MuPDFPage.getPDFPage(muPDFPage.mPage);
                    if (pDFPage != null) {
                        PDFAnnotation createAnnotation = pDFPage.createAnnotation(12);
                        createAnnotation.setRect(new com.artifex.mupdf.fitz.Rect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom));
                        createAnnotation.setAuthor(str);
                        createAnnotation.setModificationDate(new Date());
                        createAnnotation.update();
                        muPDFPage.mDoc.mIsModified = true;
                    }
                    pDFDocument.endOperation();
                    muPDFDoc.update(this.val$pageNum);
                    muPDFDoc.access$2500(muPDFDoc, this.val$pageNum);
                }
            });
        }
        return size;
    }

    public void afterUndoRedo() {
        if (getDocView().getDocConfigOptions().isFormFillingEnabled()) {
            stopPreviousEditor();
            collectFormFields();
            invalidate();
        }
    }

    public void beforeUndoRedo() {
        closeCurrentEditor();
    }

    public boolean canDoubleTap(int i, int i2) {
        return ((MuPDFPage) this.mPage).findSelectableAnnotAtPoint(screenToPage(new Point(i, i2)), 12) == -1;
    }

    public void changePage(int i) {
        super.changePage(i);
        collectFormFields();
    }

    public final void closeCurrentEditor() {
        PDFFormEditor pDFFormEditor;
        if (this.mEditingWidgetIndex >= 0 && (pDFFormEditor = this.mFormEditor) != null && (pDFFormEditor instanceof PDFFormTextEditor)) {
            ((MuPDFDoc) getDoc()).showJsError = false;
            if (!stopPreviousEditor()) {
                this.mFormEditor.setNewValue(this.mFormEditor.getOriginalValue());
                if (!stopPreviousEditor()) {
                    Log.e("DocPdfPageView", "DocMuPdfPageView.onPause: problem resetting open field.");
                }
            }
            ((MuPDFDoc) getDoc()).showJsError = true;
            this.mEditingWidget = null;
            this.mEditingWidgetIndex = -1;
            invalidate();
        }
    }

    public void collectFormFields() {
        MuPDFWidget[] muPDFWidgetArr;
        MuPDFPage muPDFPage = (MuPDFPage) getPage();
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        if (muPDFPage != null && muPDFDoc != null) {
            ArrayList<MuPDFWidget> arrayList = muPDFPage.mupdfWidgets;
            if (arrayList == null || arrayList.size() <= 0) {
                muPDFWidgetArr = null;
            } else {
                ArrayList<MuPDFWidget> arrayList2 = muPDFPage.mupdfWidgets;
                muPDFWidgetArr = (MuPDFWidget[]) arrayList2.toArray(new MuPDFWidget[arrayList2.size()]);
            }
            this.mFormFields = muPDFWidgetArr;
            if (this.numInitialFormFields == -1) {
                if (muPDFWidgetArr != null) {
                    this.numInitialFormFields = muPDFWidgetArr.length;
                } else {
                    this.numInitialFormFields = 0;
                }
            }
            if (muPDFWidgetArr != null && muPDFWidgetArr.length > 0) {
                this.mFormFieldBounds = new Rect[muPDFWidgetArr.length];
                int i = 0;
                for (MuPDFWidget muPDFWidget : muPDFWidgetArr) {
                    int i2 = this.numInitialFormFields;
                    if (i2 != -1 && i > i2 - 1) {
                        this.mFormFields[i].mCreatedInThisSession = true;
                    }
                    this.mFormFieldBounds[i] = muPDFWidget.getBounds();
                    i++;
                }
            }
        }
    }

    public final Point coordsToPoint(float f, float f2) {
        return new Point(pageToView((int) f), pageToView((int) f2));
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        PDFFormEditor pDFFormEditor;
        if (!(getDocView() instanceof DocPdfView) || (pDFFormEditor = this.mFormEditor) == null || !pDFFormEditor.dispatchTouchEvent(motionEvent)) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return true;
    }

//    public final void doSign(final MuPDFWidget muPDFWidget) {
//        final Handler handler = new Handler();
//        final NUIPKCS7Signer signer = Utilities.getSigner((Activity) getContext());
//        if (signer != null) {
//            setSigningFlow(true);
//            signer.doSign(new NUIPKCS7Signer.NUIPKCS7SignerListener() {
//                public void onCancel() {
//                    ArDkLib.runOnUiThread(new Runnable() {
//                        public void run() {
//                            DocMuPdfPageView.this.setSigningFlow(false);
//                        }
//                    });
//                }
//
//                public void onSignatureReady() {
//                    if (DocMuPdfPageView.this.getDocView().getDocConfigOptions().isDigitalSignaturesEnabled()) {
//                        handler.post(new Runnable() {
//                            public void run() {
//                                DocMuPdfPageView docMuPdfPageView = DocMuPdfPageView.this;
//                                NUIPKCS7Signer nUIPKCS7Signer = signer;
//                                int i = DocMuPdfPageView.$r8$clinit;
//                                new SignatureDialog(docMuPdfPageView.getContext(), docMuPdfPageView.getDocView().getDocConfigOptions(), muPDFWidget, nUIPKCS7Signer, new SignatureDialog.SignatureListener(muPDFWidget, nUIPKCS7Signer) {
//                                    public final /* synthetic */ NUIPKCS7Signer val$signer;
//                                    public final /* synthetic */ MuPDFWidget val$widget;
//
//                                    {
//                                        this.val$widget = r2;
//                                        this.val$signer = r3;
//                                    }
//
//                                    public void onCancel() {
//                                        DocMuPdfPageView.this.setSigningFlow(false);
//                                    }
//
//                                    public void onSign(SignatureAppearance signatureAppearance) {
//                                        DocMuPdfPageView.access$700(DocMuPdfPageView.this, signatureAppearance, this.val$widget, this.val$signer);
//                                    }
//                                }).show();
//                            }
//                        });
//                    } else {
//                        handler.post(new Runnable() {
//                            public void run() {
//                                DocMuPdfPageView.access$700(DocMuPdfPageView.this, (SignatureAppearance) null, muPDFWidget, signer);
//                            }
//                        });
//                    }
//                }
//            });
//        }
//    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0010, code lost:
        r0 = r0.searchIndex;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drawSearchHighlight(Canvas r10) {
        /*
            r9 = this;
            com.artifex.solib.ArDkPage r0 = r9.getPage()
            com.artifex.solib.MuPDFPage r0 = (com.artifex.solib.MuPDFPage) r0
            if (r0 != 0) goto L_0x0009
            return
        L_0x0009:
            com.artifex.mupdf.fitz.Quad[][] r1 = r0.searchResults
            if (r1 == 0) goto L_0x001a
            int r2 = r1.length
            if (r2 <= 0) goto L_0x001a
            int r0 = r0.searchIndex
            if (r0 < 0) goto L_0x001a
            int r2 = r1.length
            if (r0 >= r2) goto L_0x001a
            r0 = r1[r0]
            goto L_0x001b
        L_0x001a:
            r0 = 0
        L_0x001b:
            if (r0 == 0) goto L_0x00e6
            int r1 = r0.length
            if (r1 <= 0) goto L_0x00e6
            android.graphics.Path r1 = new android.graphics.Path
            r1.<init>()
            int r2 = r0.length
            r3 = 0
        L_0x0027:
            if (r3 >= r2) goto L_0x0085
            r4 = r0[r3]
            android.graphics.Path r5 = new android.graphics.Path
            r5.<init>()
            float r6 = r4.ul_x
            float r7 = r4.ul_y
            android.graphics.Point r6 = r9.coordsToPoint(r6, r7)
            int r7 = r6.x
            float r7 = (float) r7
            int r8 = r6.y
            float r8 = (float) r8
            r5.moveTo(r7, r8)
            float r7 = r4.ur_x
            float r8 = r4.ur_y
            android.graphics.Point r7 = r9.coordsToPoint(r7, r8)
            int r8 = r7.x
            float r8 = (float) r8
            int r7 = r7.y
            float r7 = (float) r7
            r5.lineTo(r8, r7)
            float r7 = r4.lr_x
            float r8 = r4.lr_y
            android.graphics.Point r7 = r9.coordsToPoint(r7, r8)
            int r8 = r7.x
            float r8 = (float) r8
            int r7 = r7.y
            float r7 = (float) r7
            r5.lineTo(r8, r7)
            float r7 = r4.ll_x
            float r4 = r4.ll_y
            android.graphics.Point r4 = r9.coordsToPoint(r7, r4)
            int r7 = r4.x
            float r7 = (float) r7
            int r4 = r4.y
            float r4 = (float) r4
            r5.lineTo(r7, r4)
            int r4 = r6.x
            float r4 = (float) r4
            int r6 = r6.y
            float r6 = (float) r6
            r5.lineTo(r4, r6)
            android.graphics.Path$Op r4 = android.graphics.Path.Op.UNION
            r1.op(r5, r4)
            int r3 = r3 + 1
            goto L_0x0027
        L_0x0085:
            android.graphics.Paint r0 = new android.graphics.Paint
            r0.<init>()
            android.graphics.Paint$Style r2 = android.graphics.Paint.Style.FILL
            r0.setStyle(r2)
            android.content.Context r2 = r9.getContext()
            int r3 = com.artifex.sonui.editor.R.color.sodk_editor_text_highlight_color
            int r2 = androidx.core.content.ContextCompat.getColor(r2, r3)
            r0.setColor(r2)
            android.content.Context r2 = r9.getContext()
            android.content.res.Resources r2 = r2.getResources()
            int r3 = com.artifex.sonui.editor.R.integer.sodk_editor_text_highlight_alpha
            int r2 = r2.getInteger(r3)
            r0.setAlpha(r2)
            r10.drawPath(r1, r0)
            r2 = 1
            r0.setFlags(r2)
            android.graphics.Paint$Style r2 = android.graphics.Paint.Style.STROKE
            r0.setStyle(r2)
            android.content.Context r2 = r9.getContext()
            int r3 = com.artifex.sonui.editor.R.color.sodk_editor_search_highlight_border_color
            int r2 = androidx.core.content.ContextCompat.getColor(r2, r3)
            r0.setColor(r2)
            float r2 = r9.mScale
            android.content.Context r3 = r9.getContext()
            android.content.res.Resources r3 = r3.getResources()
            int r4 = com.artifex.sonui.editor.R.integer.sodk_editor_search_highlight_border_width
            int r3 = r3.getInteger(r4)
            float r3 = (float) r3
            int r3 = com.artifex.sonui.editor.Utilities.convertDpToPixel(r3)
            float r3 = (float) r3
            float r2 = r2 * r3
            int r2 = (int) r2
            float r2 = (float) r2
            r0.setStrokeWidth(r2)
            r10.drawPath(r1, r0)
        L_0x00e6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.DocMuPdfPageView.drawSearchHighlight(android.graphics.Canvas):void");
    }

    public void drawSelection(Canvas canvas) {
        MuPDFPage muPDFPage;
        MuPDFWidget[] signatures;
        int i;
        MuPDFWidget[] muPDFWidgetArr;
        if (this.mPage != null && (muPDFPage = (MuPDFPage) getPage()) != null) {
            MuPDFDoc muPDFDoc = muPDFPage.mDoc;
            int i2 = muPDFDoc.selectedAnnotPagenum;
            int selectedAnnotationIndex = muPDFDoc.getSelectedAnnotationIndex();
            Rect[] rectArr = null;
            Rect rect = (i2 != muPDFPage.mPageNumber || selectedAnnotationIndex < 0 || selectedAnnotationIndex >= muPDFPage.mAnnotations.size()) ? null : muPDFPage.toRect(muPDFPage.mAnnotations.get(selectedAnnotationIndex).rect);
            if (rect != null && !((MuPDFDoc) getDoc()).selectionIsTextRedaction()) {
                Rect pageToView = pageToView(new RectF((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom));
                this.mHighlightingRect.set(pageToView.left, pageToView.top, pageToView.right, pageToView.bottom);
                canvas.drawRect(this.mHighlightingRect, this.mSelectionHighlightPainter);
            }
            com.artifex.mupdf.fitz.Rect[] rectArr2 = muPDFPage.mSelectionRects;
            if (rectArr2 != null) {
                rectArr = new Rect[rectArr2.length];
                int i3 = 0;
                while (true) {
                    com.artifex.mupdf.fitz.Rect[] rectArr3 = muPDFPage.mSelectionRects;
                    if (i3 >= rectArr3.length) {
                        break;
                    }
                    rectArr[i3] = muPDFPage.toRect(rectArr3[i3]);
                    i3++;
                }
            }
            if (rectArr != null && rectArr.length > 0) {
                for (Rect rect2 : rectArr) {
                    this.mHighlightingRect.set(rect2);
                    Rect rect3 = this.mHighlightingRect;
                    pageToView(rect3, rect3);
                    canvas.drawRect(this.mHighlightingRect, this.mSelectionHighlightPainter);
                }
            }
            ConfigOptions docConfigOptions = getDocView().getDocConfigOptions();
            if (docConfigOptions != null && docConfigOptions.isFormFillingEnabled() && !this.mFullscreen && (muPDFWidgetArr = this.mFormFields) != null && muPDFWidgetArr.length > 0) {
                int i4 = 0;
                while (true) {
                    MuPDFWidget[] muPDFWidgetArr2 = this.mFormFields;
                    if (i4 >= muPDFWidgetArr2.length) {
                        break;
                    }
                    MuPDFWidget muPDFWidget = muPDFWidgetArr2[i4];
                    if ((muPDFWidget.mKind == 6) && !muPDFWidget.mIsSigned) {
                        if ((muPDFWidget.mFieldFlags & 1) != 0) {
                            i4++;
                        }
                    }
                    MuPDFWidget muPDFWidget2 = this.mEditingWidget;
                    if ((muPDFWidget2 == null || !muPDFWidget.mWidget.equals(muPDFWidget2.mWidget)) && muPDFWidget.mKind != 1) {
                        this.mHighlightingRect.set(this.mFormFieldBounds[i4]);
                        Rect rect4 = this.mHighlightingRect;
                        pageToView(rect4, rect4);
                        canvas.drawRect(this.mHighlightingRect, this.mFormFieldPainter);
                    }
                    i4++;
                }
            }
            if (this.highlightedSigIndex >= 0 && (signatures = getSignatures()) != null && (i = this.highlightedSigIndex) < signatures.length) {
                Rect bounds = signatures[i].getBounds();
                pageToView(bounds, bounds);
                canvas.drawRect(bounds, this.mHighlightSignaturePainter);
            }
        }
    }

    public void drawWatermark(Canvas canvas) {
        Activity activity = (Activity) getContext();
        Objects.requireNonNull(MuPDFLib.getLib());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x008f, code lost:
        if (r12 != false) goto L_0x0224;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void editWidget(final MuPDFWidget r10, boolean r11, Point r12) {
        /*
            r9 = this;
            com.artifex.solib.ArDkDoc r0 = r9.getDoc()
            com.artifex.solib.MuPDFDoc r0 = (com.artifex.solib.MuPDFDoc) r0
            int r1 = r10.mKind
            r2 = 0
            switch(r1) {
                case 1: goto L_0x01fc;
                case 2: goto L_0x01a4;
                case 3: goto L_0x016b;
                case 4: goto L_0x016b;
                case 5: goto L_0x01a4;
                case 6: goto L_0x0081;
                case 7: goto L_0x0027;
                default: goto L_0x000c;
            }
        L_0x000c:
            r9.stopPreviousEditor()
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "editWidget() unsupported widget type: "
            r10.append(r11)
            r10.append(r1)
            java.lang.String r10 = r10.toString()
            java.lang.String r11 = "DocPdfPageView"
            android.util.Log.i(r11, r10)
            goto L_0x0224
        L_0x0027:
            boolean r11 = r9.stopPreviousEditor()
            if (r11 != 0) goto L_0x002f
            goto L_0x0224
        L_0x002f:
            com.artifex.sonui.editor.DocView r5 = r9.getDocView()
            com.artifex.sonui.editor.DocMuPdfPageView$17 r11 = new com.artifex.sonui.editor.DocMuPdfPageView$17
            r11.<init>()
            r5.setShowKeyboardListener(r11)
            com.artifex.sonui.editor.DocView r11 = r9.getDocView()
            com.artifex.sonui.editor.DocPdfView r11 = (com.artifex.sonui.editor.DocPdfView) r11
            r11.mFormEditorPage = r9
            android.content.Context r11 = r9.getContext()
            android.app.Activity r11 = (android.app.Activity) r11
            int r12 = com.artifex.sonui.editor.R.id.pdf_form_text_editor_layout
            android.view.View r11 = r11.findViewById(r12)
            r1 = r11
            com.artifex.sonui.editor.PDFFormEditor r1 = (com.artifex.sonui.editor.PDFFormEditor) r1
            r9.mFormEditor = r1
            int r3 = r9.getPageNumber()
            com.artifex.solib.ArDkDoc r11 = r9.getDoc()
            r4 = r11
            com.artifex.solib.MuPDFDoc r4 = (com.artifex.solib.MuPDFDoc) r4
            android.graphics.Rect[] r11 = r9.mFormFieldBounds
            int r12 = r9.mEditingWidgetIndex
            r7 = r11[r12]
            com.artifex.sonui.editor.DocMuPdfPageView$18 r8 = new com.artifex.sonui.editor.DocMuPdfPageView$18
            r8.<init>(r5)
            r2 = r9
            r6 = r10
            r1.start(r2, r3, r4, r5, r6, r7, r8)
            com.artifex.sonui.editor.NUIDocView r10 = com.artifex.sonui.editor.NUIDocView.currentNUIDocView()
            boolean r10 = r10.isKeyboardVisible()
            if (r10 == 0) goto L_0x007c
            r9.scrollCurrentWidgetIntoView()
        L_0x007c:
            r9.invalidate()
            goto L_0x0224
        L_0x0081:
            boolean r12 = r10.mIsSigned
            r1 = 1
            r3 = 0
            if (r12 != 0) goto L_0x0093
            int r12 = r10.mFieldFlags
            r12 = r12 & r1
            if (r12 == 0) goto L_0x008e
            r12 = 1
            goto L_0x008f
        L_0x008e:
            r12 = 0
        L_0x008f:
            if (r12 == 0) goto L_0x0093
            goto L_0x0224
        L_0x0093:
            boolean r12 = r9.stopPreviousEditor()
            if (r12 != 0) goto L_0x009b
            goto L_0x0224
        L_0x009b:
            if (r11 == 0) goto L_0x0163
            com.artifex.sonui.editor.DocView r11 = r9.getDocView()
            com.artifex.solib.ConfigOptions r11 = r11.getDocConfigOptions()
            boolean r11 = r11.isFormSigningFeatureEnabled()
            if (r11 == 0) goto L_0x0163
            boolean r11 = r10.mIsSigned
            if (r11 != 0) goto L_0x012c
            r9.continueSigning = r3
            r9.setSigningFlow(r1)
            boolean r11 = r10.mCreatedInThisSession
            if (r11 == 0) goto L_0x0126
            androidx.appcompat.view.ContextThemeWrapper r11 = new androidx.appcompat.view.ContextThemeWrapper
            android.content.Context r12 = r9.getContext()
            int r1 = com.artifex.sonui.editor.R.style.sodk_editor_alert_dialog_style
            r11.<init>((android.content.Context) r12, (int) r1)
            androidx.appcompat.app.AlertDialog$Builder r12 = new androidx.appcompat.app.AlertDialog$Builder
            r12.<init>(r11)
            androidx.appcompat.app.AlertDialog r11 = r12.create()
            android.content.Context r12 = r9.getContext()
            android.view.LayoutInflater r12 = android.view.LayoutInflater.from(r12)
            int r1 = com.artifex.sonui.editor.R.layout.sodk_editor_signature_dialog
            android.view.View r12 = r12.inflate(r1, r2)
            androidx.appcompat.app.AlertController r1 = r11.mAlert
            r1.mView = r12
            r1.mViewLayoutResId = r3
            r1.mViewSpacingSpecified = r3
            int r1 = com.artifex.sonui.editor.R.id.sign_button
            android.view.View r1 = r12.findViewById(r1)
            com.artifex.sonui.editor.DocMuPdfPageView$5 r2 = new com.artifex.sonui.editor.DocMuPdfPageView$5
            r2.<init>(r11, r10)
            r1.setOnClickListener(r2)
            int r1 = com.artifex.sonui.editor.R.id.reposition_button
            android.view.View r1 = r12.findViewById(r1)
            com.artifex.sonui.editor.DocMuPdfPageView$6 r2 = new com.artifex.sonui.editor.DocMuPdfPageView$6
            r2.<init>(r11, r9, r10)
            r1.setOnClickListener(r2)
            int r1 = com.artifex.sonui.editor.R.id.delete_button
            android.view.View r1 = r12.findViewById(r1)
            com.artifex.sonui.editor.DocMuPdfPageView$7 r2 = new com.artifex.sonui.editor.DocMuPdfPageView$7
            r2.<init>(r11, r10)
            r1.setOnClickListener(r2)
            int r10 = com.artifex.sonui.editor.R.id.cancel_button
            android.view.View r10 = r12.findViewById(r10)
            com.artifex.sonui.editor.DocMuPdfPageView$8 r12 = new com.artifex.sonui.editor.DocMuPdfPageView$8
            r12.<init>(r9, r11)
            r10.setOnClickListener(r12)
            com.artifex.sonui.editor.DocMuPdfPageView$9 r10 = new com.artifex.sonui.editor.DocMuPdfPageView$9
            r10.<init>()
            r11.setOnDismissListener(r10)
            r11.show()
            goto L_0x0163
        L_0x0126:
            r9.continueSigning = r1
            r9.doSign(r10)
            goto L_0x0163
        L_0x012c:
            android.content.Context r11 = r9.getContext()
            android.app.Activity r11 = (android.app.Activity) r11
            com.artifex.sonui.editor.NUIPKCS7Verifier r11 = com.artifex.sonui.editor.Utilities.getVerifier(r11)
            com.artifex.solib.ArDkDoc r12 = r9.getDoc()
            com.artifex.solib.MuPDFDoc r12 = (com.artifex.solib.MuPDFDoc) r12
            if (r11 == 0) goto L_0x0163
            com.artifex.sonui.editor.DocMuPdfPageView$10 r2 = new com.artifex.sonui.editor.DocMuPdfPageView$10
            r2.<init>(r9, r12, r10, r11)
            long r3 = r12.mLastSaveTime
            long r5 = r10.mTimeSigned
            int r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r7 <= 0) goto L_0x0159
            android.content.Context r10 = r9.getContext()
            int r11 = com.artifex.sonui.editor.R.string.sodk_editor_unsaved_signatures
            android.widget.Toast r10 = android.widget.Toast.makeText(r10, r11, r1)
            r10.show()
            goto L_0x0163
        L_0x0159:
            com.artifex.solib.Worker r12 = r12.mWorker
            com.artifex.sonui.editor.DocMuPdfPageView$11 r1 = new com.artifex.sonui.editor.DocMuPdfPageView$11
            r1.<init>(r9, r10, r11, r2)
            r12.add(r1)
        L_0x0163:
            r9.scrollCurrentWidgetIntoView()
            r9.invalidate()
            goto L_0x0224
        L_0x016b:
            com.artifex.solib.MuPDFDoc r11 = r10.mDoc
            r11.checkForWorkerThread()
            com.artifex.mupdf.fitz.PDFWidget r11 = r10.mWidget
            if (r11 == 0) goto L_0x0178
            java.lang.String[] r2 = r11.getOptions()
        L_0x0178:
            r5 = r2
            java.lang.String r6 = r10.mValue
            if (r5 == 0) goto L_0x0224
            int r11 = r5.length
            if (r11 <= 0) goto L_0x0224
            com.artifex.sonui.editor.ListWheelDialog r3 = new com.artifex.sonui.editor.ListWheelDialog
            r3.<init>()
            android.content.Context r4 = r9.getContext()
            com.artifex.sonui.editor.DocMuPdfPageView$4 r7 = new com.artifex.sonui.editor.DocMuPdfPageView$4
            r7.<init>(r6, r10)
            r8 = 0
            r3.show(r4, r5, r6, r7, r8)
            com.artifex.sonui.editor.NUIDocView r10 = com.artifex.sonui.editor.NUIDocView.currentNUIDocView()
            boolean r10 = r10.isKeyboardVisible()
            if (r10 == 0) goto L_0x019f
            r9.scrollCurrentWidgetIntoView()
        L_0x019f:
            r9.invalidate()
            goto L_0x0224
        L_0x01a4:
            boolean r12 = r9.stopPreviousEditor()
            if (r12 != 0) goto L_0x01ac
            goto L_0x0224
        L_0x01ac:
            com.artifex.sonui.editor.DocView r5 = r9.getDocView()
            com.artifex.sonui.editor.DocMuPdfPageView$15 r12 = new com.artifex.sonui.editor.DocMuPdfPageView$15
            r12.<init>()
            r5.setShowKeyboardListener(r12)
            com.artifex.sonui.editor.DocView r12 = r9.getDocView()
            com.artifex.sonui.editor.DocPdfView r12 = (com.artifex.sonui.editor.DocPdfView) r12
            r12.mFormEditorPage = r9
            android.content.Context r12 = r9.getContext()
            android.app.Activity r12 = (android.app.Activity) r12
            int r1 = com.artifex.sonui.editor.R.id.pdf_form_checkbox_editor_layout
            android.view.View r12 = r12.findViewById(r1)
            r1 = r12
            com.artifex.sonui.editor.PDFFormEditor r1 = (com.artifex.sonui.editor.PDFFormEditor) r1
            r9.mFormEditor = r1
            int r3 = r9.getPageNumber()
            com.artifex.solib.ArDkDoc r12 = r9.getDoc()
            r4 = r12
            com.artifex.solib.MuPDFDoc r4 = (com.artifex.solib.MuPDFDoc) r4
            android.graphics.Rect[] r12 = r9.mFormFieldBounds
            int r2 = r9.mEditingWidgetIndex
            r7 = r12[r2]
            com.artifex.sonui.editor.DocMuPdfPageView$16 r8 = new com.artifex.sonui.editor.DocMuPdfPageView$16
            r8.<init>(r5)
            r2 = r9
            r6 = r10
            r1.start(r2, r3, r4, r5, r6, r7, r8)
            if (r11 == 0) goto L_0x01f5
            com.artifex.sonui.editor.PDFFormEditor r10 = r9.mFormEditor
            com.artifex.sonui.editor.PDFFormCheckboxEditor r10 = (com.artifex.sonui.editor.PDFFormCheckboxEditor) r10
            r10.toggle()
        L_0x01f5:
            r9.scrollCurrentWidgetIntoView()
            r9.invalidate()
            goto L_0x0224
        L_0x01fc:
            boolean r1 = r9.stopPreviousEditor()
            if (r1 == 0) goto L_0x0224
            if (r11 == 0) goto L_0x0224
            if (r12 == 0) goto L_0x0224
            com.artifex.sonui.editor.DocMuPdfPageView$1 r11 = new com.artifex.sonui.editor.DocMuPdfPageView$1
            r11.<init>()
            com.artifex.solib.ArDkDoc r12 = r9.getDoc()
            com.artifex.solib.MuPDFDoc r12 = (com.artifex.solib.MuPDFDoc) r12
            r12.jsEventListener2 = r11
            com.artifex.sonui.editor.DocMuPdfPageView$2 r11 = new com.artifex.sonui.editor.DocMuPdfPageView$2
            r11.<init>()
            com.artifex.solib.MuPDFDoc r12 = r10.mDoc
            com.artifex.solib.Worker r12 = r12.mWorker
            com.artifex.solib.MuPDFWidget$7 r1 = new com.artifex.solib.MuPDFWidget$7
            r1.<init>(r11)
            r12.add(r1)
        L_0x0224:
            int r10 = r9.getPageNumber()
            r0.update(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.DocMuPdfPageView.editWidget(com.artifex.solib.MuPDFWidget, boolean, android.graphics.Point):void");
    }

    public void endRenderPass() {
        super.endRenderPass();
        PDFFormEditor pDFFormEditor = this.mFormEditor;
        if (pDFFormEditor != null) {
            pDFFormEditor.onRenderComplete();
        }
    }

    public final MuPDFWidget findTappedWidget(int i, int i2) {
        PDFFormEditor pDFFormEditor = this.mFormEditor;
        if (pDFFormEditor != null && !pDFFormEditor.isStopped()) {
            return this.mEditingWidget;
        }
        MuPDFWidget[] muPDFWidgetArr = this.mFormFields;
        if (muPDFWidgetArr == null || muPDFWidgetArr.length <= 0) {
            return null;
        }
        int i3 = 0;
        while (true) {
            MuPDFWidget[] muPDFWidgetArr2 = this.mFormFields;
            if (i3 >= muPDFWidgetArr2.length) {
                return null;
            }
            int length = (muPDFWidgetArr2.length - i3) - 1;
            MuPDFWidget muPDFWidget = muPDFWidgetArr2[length];
            if (this.mFormFieldBounds[length].contains(i, i2)) {
                if (muPDFWidget.mKind != 1) {
                    this.mEditingWidget = muPDFWidget;
                    this.mEditingWidgetIndex = length;
                }
                return muPDFWidget;
            }
            i3++;
        }
    }

    public void finish() {
        stopPreviousEditor(true);
        super.finish();
    }

    public MuPDFWidget getNewestWidget() {
        MuPDFWidget[] muPDFWidgetArr = this.mFormFields;
        if (muPDFWidgetArr == null || muPDFWidgetArr.length == 0) {
            return null;
        }
        return muPDFWidgetArr[muPDFWidgetArr.length - 1];
    }

    public MuPDFWidget[] getSignatures() {
        MuPDFWidget[] muPDFWidgetArr = this.mFormFields;
        MuPDFWidget[] muPDFWidgetArr2 = null;
        if (!(muPDFWidgetArr == null || muPDFWidgetArr.length == 0)) {
            ArrayList arrayList = new ArrayList();
            int i = 0;
            int i2 = 0;
            while (true) {
                MuPDFWidget[] muPDFWidgetArr3 = this.mFormFields;
                if (i2 >= muPDFWidgetArr3.length) {
                    break;
                }
                MuPDFWidget muPDFWidget = muPDFWidgetArr3[i2];
                if (muPDFWidget.mKind == 6) {
                    arrayList.add(muPDFWidget);
                }
                i2++;
            }
            if (arrayList.size() == 0) {
                return null;
            }
            muPDFWidgetArr2 = new MuPDFWidget[arrayList.size()];
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                muPDFWidgetArr2[i] = (MuPDFWidget) it.next();
                i++;
            }
        }
        return muPDFWidgetArr2;
    }

    public void onFullscreen(boolean z) {
        this.mFullscreen = z;
        if (z && getDocView() != null) {
            ConfigOptions docConfigOptions = getDocView().getDocConfigOptions();
            if (docConfigOptions.isFormFillingEnabled() && docConfigOptions.isEditingEnabled()) {
                stopPreviousEditor();
                invalidate();
            }
        }
    }

    public void onPause() {
        closeCurrentEditor();
    }

    public void onReloadFile() {
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        dropPage();
        changePage(getPageNumber());
        if (getDocView().getDocConfigOptions().isFormFillingEnabled()) {
            stopPreviousEditor();
            collectFormFields();
            invalidate();
        }
        muPDFDoc.update(getPageNumber());
    }

    public boolean onSingleTap(int i, int i2, boolean z, ExternalLinkListener externalLinkListener) {
        boolean z2;
        MuPDFWidget findTappedWidget;
        ConfigOptions docConfigOptions = getDocView().getDocConfigOptions();
        MuPDFDoc muPDFDoc = (MuPDFDoc) getDoc();
        muPDFDoc.selectedAnnotPagenum = -1;
        muPDFDoc.selectedAnnotIndex = -1;
        Point screenToPage = screenToPage(i, i2);
        boolean z3 = true;
        if (!docConfigOptions.isFormFillingEnabled() || !docConfigOptions.isEditingEnabled()) {
            z2 = true;
        } else {
            z2 = stopPreviousEditor();
            this.mEditingWidget = null;
            this.mEditingWidgetIndex = -1;
            invalidate();
        }
        if (!tryHyperlink(screenToPage, externalLinkListener)) {
            if (docConfigOptions.isFormFillingEnabled() && docConfigOptions.isEditingEnabled() && z2) {
                MuPDFWidget findTappedWidget2 = findTappedWidget(screenToPage.x, screenToPage.y);
                if (findTappedWidget2 == null || findTappedWidget2.mKind == 1) {
                    Utilities.hideKeyboard(getContext());
                } else {
                    MuPDFDoc muPDFDoc2 = (MuPDFDoc) getDoc();
                    if (muPDFDoc2.mShowXFAWarning) {
                        muPDFDoc2.mShowXFAWarning = false;
                        Utilities.showMessage((Activity) getContext(), getContext().getString(R.string.sodk_editor_xfa_warning_title), String.format(getContext().getString(R.string.sodk_editor_xfa_warning_body), new Object[]{Utilities.getApplicationName(getContext())}));
                        this.mEditingWidget = null;
                        this.mEditingWidgetIndex = -1;
                    } else {
                        editWidget(findTappedWidget2, true, screenToPage);
                    }
                }
            }
            if (!selectAnnotation(i, i2)) {
                if (!docConfigOptions.isFormFillingEnabled() || (findTappedWidget = findTappedWidget(screenToPage.x, screenToPage.y)) == null || findTappedWidget.mKind != 1) {
                    MuPDFWidget findTappedWidget3 = findTappedWidget(screenToPage.x, screenToPage.y);
                    if (!docConfigOptions.isEditingEnabled() || findTappedWidget3 == null || findTappedWidget3.mKind != 6) {
                        if (!docConfigOptions.isFormFillingEnabled()) {
                            findTappedWidget(screenToPage.x, screenToPage.y);
                        }
                    } else if (docConfigOptions.isFormSigningFeatureEnabled()) {
                        editWidget(findTappedWidget3, true, screenToPage);
                    }
                    z3 = false;
                } else {
                    editWidget(findTappedWidget, true, screenToPage);
                }
            }
        }
        if (this.mEditingWidget == null) {
            Utilities.hideKeyboard(getContext());
        }
        return z3;
    }

    public final void scrollCurrentWidgetIntoView() {
        PDFFormEditor pDFFormEditor = this.mFormEditor;
        if (pDFFormEditor != null) {
            pDFFormEditor.scrollIntoView();
        }
    }

    public boolean selectAnnotation(int i, int i2) {
        int i3;
        CopyOnWriteArrayList<MuPDFAnnotation> copyOnWriteArrayList;
        MuPDFAnnotation muPDFAnnotation;
        ConfigOptions docConfigOptions = getDocView().getDocConfigOptions();
        Point screenToPage = screenToPage(i, i2);
        if (docConfigOptions.isEditingEnabled()) {
            MuPDFPage muPDFPage = (MuPDFPage) this.mPage;
            if (!NUIDocView.currentNUIDocView().isRedactionMode() || !docConfigOptions.isRedactionsEnabled()) {
                int findSelectableAnnotAtPoint = muPDFPage.findSelectableAnnotAtPoint(screenToPage, 0);
                if (findSelectableAnnotAtPoint < 0) {
                    findSelectableAnnotAtPoint = muPDFPage.findSelectableAnnotAtPoint(screenToPage, 8);
                }
                i3 = findSelectableAnnotAtPoint < 0 ? muPDFPage.findSelectableAnnotAtPoint(screenToPage, -1) : findSelectableAnnotAtPoint;
            } else {
                i3 = muPDFPage.findSelectableAnnotAtPoint(screenToPage, 12);
            }
            if (i3 >= 0) {
                if (MuPDFPage.getPDFPage(muPDFPage.mPage) == null || (copyOnWriteArrayList = muPDFPage.mAnnotations) == null || copyOnWriteArrayList.size() == 0 || (muPDFAnnotation = muPDFPage.mAnnotations.get(i3)) == null) {
                    return true;
                }
                MuPDFDoc muPDFDoc = muPDFPage.mDoc;
                muPDFDoc.selectedAnnotPagenum = muPDFPage.mPageNumber;
                muPDFDoc.selectedAnnotIndex = i3;
                com.artifex.mupdf.fitz.Rect rect = muPDFAnnotation.rect;
                Iterator<SOPageListener> it = muPDFPage.mPageListeners.iterator();
                while (it.hasNext()) {
                    it.next().update(muPDFPage.toRectF(rect));
                }
                muPDFPage.updatePageRect(rect);
                muPDFPage.mDoc.onSelectionUpdate(muPDFPage.mPageNumber);
                return true;
            }
        }
        return false;
    }

    public void setHighlightedSig(int i) {
        this.highlightedSigIndex = i;
        invalidate();
    }

    public void setOrigin() {
        Rect rect = new Rect();
        rect.set(this.mPageRect);
        int[] iArr = new int[2];
        ((Activity) getContext()).getWindow().getDecorView().getLocationInWindow(iArr);
        rect.offset(-iArr[0], -iArr[1]);
        this.mRenderOrigin.set((float) rect.left, (float) rect.top);
    }

    public void stopCurrentEditor() {
        stopPreviousEditor();
        this.mEditingWidget = null;
        this.mEditingWidgetIndex = -1;
        invalidate();
    }

    public boolean stopPreviousEditor() {
        return stopPreviousEditor(false);
    }

    public void update(RectF rectF) {
        if (!isFinished() && isShown()) {
            invalidate();
        }
    }

    public void updateSelectionRects(Point point, Point point2) {
        ((MuPDFPage) getPage()).updateSelectionRects(screenToPage(point), screenToPage(point2));
        invalidate();
    }

    public boolean stopPreviousEditor(boolean z) {
        PDFFormEditor pDFFormEditor;
        boolean z2;
        if (getDocView() instanceof DocListPagesView) {
            return true;
        }
        DocPdfView docPdfView = (DocPdfView) getDocView();
        DocMuPdfPageView docMuPdfPageView = docPdfView != null ? docPdfView.mFormEditorPage : null;
        if (docMuPdfPageView == null || (pDFFormEditor = docMuPdfPageView.mFormEditor) == null) {
            return true;
        }
        if (z) {
            z2 = pDFFormEditor.cancel();
        } else {
            z2 = pDFFormEditor.stop();
        }
        if (z2) {
            docMuPdfPageView.mFormEditor = null;
            docMuPdfPageView.invalidate();
            if (docMuPdfPageView != this) {
                docMuPdfPageView.mEditingWidgetIndex = -1;
                docMuPdfPageView.mEditingWidget = null;
            }
        }
        return z2;
    }
}
