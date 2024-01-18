package com.artifex.solib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.artifex.mupdf.fitz.ColorParams;
import com.artifex.mupdf.fitz.DisplayList;
import com.artifex.mupdf.fitz.DisplayListDevice;
import com.artifex.mupdf.fitz.Document;
import com.artifex.mupdf.fitz.Image;
import com.artifex.mupdf.fitz.LinkDestination;
import com.artifex.mupdf.fitz.Matrix;
import com.artifex.mupdf.fitz.OutlineIterator;
import com.artifex.mupdf.fitz.PDFAnnotation;
import com.artifex.mupdf.fitz.PDFDocument;
import com.artifex.mupdf.fitz.PDFObject;
import com.artifex.mupdf.fitz.PDFPage;
import com.artifex.mupdf.fitz.PDFWidget;
import com.artifex.mupdf.fitz.Page;
import com.artifex.mupdf.fitz.Point;
import com.artifex.mupdf.fitz.Quad;
import com.artifex.mupdf.fitz.Rect;
import com.artifex.mupdf.fitz.SeekableInputOutputStream;
import com.artifex.mupdf.fitz.SeekableInputStream;
import com.artifex.sonui.editor.NUIDocView;
import com.artifex.sonui.editor.SODocSession;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MuPDFDoc extends ArDkDoc {
    public int handleCounter = 0;
    public PDFDocument.JsEventListener jsEventListener = (pDFDocument, str, str2, i, i2, z, str3, z2) -> {
        JsEventListener jsEventListener;
        MuPDFDoc muPDFDoc = MuPDFDoc.this;
        if (!muPDFDoc.showJsError || (jsEventListener = muPDFDoc.jsEventListener2) == null) {
            return null;
        }
        return jsEventListener.onAlert(str, str2, i, i2, z, str3, z2);
    };
    public JsEventListener jsEventListener2 = null;
    public PDFDocument.JsEventListener jsNullEventListener = (pDFDocument, str, str2, i, i2, z, str3, z2) -> null;
    public String lastSavedPath = null;
    public String mAuthor = null;
    public Context mContext;
    public boolean mDestroyed = false;
    public ConfigOptions mDocCfgOpts = null;
    public Document mDocument = null;
    public int mExternalClipDataHash = 0;
    public boolean mForceReload;
    public boolean mForceReloadAtResume;
    public String mInternalClipData = null;
    public long mLastSaveTime = 0;
    public boolean mLastSaveWasIncremental;
    public SODocLoadListener mListener = null;
    public boolean mLoadAborted = false;
    public long mLoadTime;
    public String mOpenedPath = null;
    public int mPageCount = 0;
    public int mPageNumber = 0;
    public ArrayList<MuPDFPage> mPages = new ArrayList<>();
    public boolean mShowXFAWarning = false;
    public String mValidPassword = null;
    public Worker mWorker = null;
    public int numBadPasswords = 0;
    public List<Integer> pagesWithRedactions = new ArrayList();
    public boolean searchBackwards;
    public boolean searchCancelled;
    public int searchIndex = 0;
    public SOSearchListener searchListener;
    public boolean searchMatchFound;
    public boolean searchNewPage = true;
    public int searchPage = 0;
    public boolean searchRunning;
    public String searchText;
    public int selectedAnnotIndex = -1;
    public int selectedAnnotPagenum = -1;
    public boolean showJsError = true;

    public interface JsEventListener {

        class AlertResult extends PDFDocument.JsEventListener.AlertResult {
        }

        AlertResult onAlert(String str, String str2, int i, int i2, boolean z, String str3, boolean z2);
    }

    public interface MuPDFEnumerateTocListener {
    }

    public interface ReloadListener {
    }

    public interface SaveSecureProgress {
    }

    public MuPDFDoc(Looper looper, SODocLoadListener sODocLoadListener, Context context, ConfigOptions configOptions) {
        this.mListener = sODocLoadListener;
        this.mContext = context;
        this.mDocCfgOpts = configOptions;
        this.mPageCount = 0;
        this.mPageNumber = 0;
        long currentTimeMillis = System.currentTimeMillis();
        this.mLoadTime = currentTimeMillis;
        this.mLastSaveTime = currentTimeMillis;
        this.mWorker = new Worker(looper);
    }

    public static void access$1900(MuPDFDoc muPDFDoc) {
        MuPDFPage muPDFPage = muPDFDoc.mPages.get(muPDFDoc.searchPage);
        muPDFPage.searchIndex = -1;
        muPDFPage.updatePageRect(muPDFPage.pageBounds);
        if (muPDFDoc.searchBackwards) {
            muPDFDoc.searchPage--;
        } else {
            muPDFDoc.searchPage++;
        }
        if (muPDFDoc.searchPage < 0) {
            muPDFDoc.searchPage = muPDFDoc.mNumPages - 1;
        }
        if (muPDFDoc.searchPage >= muPDFDoc.mNumPages) {
            muPDFDoc.searchPage = 0;
        }
        muPDFDoc.searchNewPage = true;
        ((Activity) muPDFDoc.mContext).runOnUiThread(new Runnable() {
            public void run() {
                //TODO TOAN
                /*MuPDFDoc muPDFDoc = MuPDFDoc.this;
                SOSearchListener sOSearchListener = muPDFDoc.searchListener;
                if (sOSearchListener != null) {
                    int i = muPDFDoc.searchPage;
                    Objects.requireNonNull(sOSearchListener);
                    sOSearchListener
                }*/
            }
        });
    }

    public static void access$2500(MuPDFDoc muPDFDoc, int i) {
        Objects.requireNonNull(muPDFDoc);
        Integer num = i;
        if (!muPDFDoc.pagesWithRedactions.contains(num)) {
            muPDFDoc.pagesWithRedactions.add(num);
        }
    }

    public static void access$3200(MuPDFDoc muPDFDoc) {
        muPDFDoc.pagesWithRedactions.clear();
        int size = muPDFDoc.mPages.size();
        for (int i = 0; i < size; i++) {
            if (muPDFDoc.mPages.get(i).countAnnotations(12) > 0) {
                muPDFDoc.pagesWithRedactions.add(i);
            }
        }
    }

    public static Document openFile(String str) {
        Document document = null;
        try {
            final SOSecureFS sOSecureFS = ArDkLib.mSecureFS;
            if (sOSecureFS == null || !sOSecureFS.isSecurePath(str)) {
                document = Document.openDocument(str);
                if (document != null && (document instanceof PDFDocument)) {
                    ((PDFDocument) document).enableJournal();
                }
                return document;
            }
            final Object fileHandleForReading = sOSecureFS.getFileHandleForReading(str);
            document = Document.openDocument(new SeekableInputStream() {
                public long position() throws IOException {
                    return sOSecureFS.getFileOffset(fileHandleForReading);
                }

                public int read(byte[] bArr) throws IOException {
                    int readFromFile = sOSecureFS.readFromFile(fileHandleForReading, bArr);
                    if (readFromFile == 0) {
                        return -1;
                    }
                    return readFromFile;
                }

                public long seek(long j, int i) throws IOException {
                    long fileOffset = sOSecureFS.getFileOffset(fileHandleForReading);
                    long fileLength = sOSecureFS.getFileLength(fileHandleForReading);
                    if (i != 0) {
                        j = i != 1 ? i != 2 ? 0 : j + fileLength : j + fileOffset;
                    }
                    sOSecureFS.seekToFileOffset(fileHandleForReading, j);
                    return j;
                }
            }, FileUtils.getExtension(str));
            ((PDFDocument) document).enableJournal();
            return document;
        } catch (Exception unused) {
        }
        return null;
    }

    public void abortLoad() {
        destroyDoc();
    }

    public void addHighlightAnnotation() {
        final int i = MuPDFPage.mTextSelPageNum;
        if (i != -1) {
            this.mWorker.add(new Worker.Task() {
                public void run() {
                }

                public void work() {
                    PDFDocument pDFDocument = MuPDFDoc.getPDFDocument(MuPDFDoc.this.mDocument);
                    pDFDocument.beginOperation("highlight");
                    MuPDFDoc.this.mPages.get(i).addAnnotation(8, MuPDFDoc.this.mAuthor);
                    pDFDocument.endOperation();
                    MuPDFDoc.this.update(i);
                    MuPDFDoc.this.clearSelection();
                }
            });
        }
    }

    public void afterValidation() {
        PDFDocument pDFDocument;
        this.mPageCount = this.mDocument.countPages();
        if (this.mDocCfgOpts.isFormFillingEnabled() && (pDFDocument = getPDFDocument(this.mDocument)) != null) {
            pDFDocument.enableJs();
            pDFDocument.setJsEventListener(this.jsEventListener);
        }
    }

    public boolean canPrint() {
        if (FileUtils.getExtension(this.mOpenedPath).equalsIgnoreCase("pdf")) {
            return canSave();
        }
        return true;
    }

    public boolean canRedo() {
        Document document = this.mDocument;
        if (document instanceof PDFDocument) {
            return ((PDFDocument) document).canRedo();
        }
        return false;
    }

    public boolean canSave() {
        return getPDFDocument(this.mDocument) != null;
    }

    public boolean canUndo() {
        Document document = this.mDocument;
        if (document instanceof PDFDocument) {
            return ((PDFDocument) document).canUndo();
        }
        return false;
    }

    public void cancelSearch() {
        this.searchRunning = false;
    }

    public void checkForWorkerThread() {
        String str;
        String str2;
        int i = 0;
        if (this.mDocCfgOpts.mSettingsBundle.getBoolean("MuPDFWorkerThreadCheckEnabledKey", false)) {
            Worker worker = this.mWorker;
            Objects.requireNonNull(worker);
            if (!Thread.currentThread().equals(worker.mThread)) {
                StackTraceElement[] stackTrace = new Throwable().getStackTrace();
                String methodName = stackTrace[0].getMethodName();
                String className = stackTrace[0].getClassName();
                StackTraceElement[] stackTrace2 = Thread.currentThread().getStackTrace();
                while (true) {
                    str = null;
                    if (i < stackTrace2.length) {
                        if (stackTrace2[i].getMethodName().equals(methodName) && stackTrace2[i].getClassName().equals(className)) {
                            int i2 = i + 1;
                            String methodName2 = stackTrace2[i2].getMethodName();
                            str = stackTrace2[i2].getClassName();
                            str2 = methodName2;
                            break;
                        }
                        i++;
                    } else {
                        str2 = null;
                        break;
                    }
                }
                Log.e("MuPDFDoc", str + "." + str2 + " must be called from the worker thread.");
                Thread.dumpStack();
            }
        }
    }

    public void clearSelection() {
        int i = this.selectedAnnotPagenum;
        this.selectedAnnotPagenum = -1;
        this.selectedAnnotIndex = -1;
        if (i >= 0 && i < this.mPages.size() && this.mPages.get(i) != null) {
            this.mPages.get(i).clearSelection();
        }
        int i2 = MuPDFPage.mTextSelPageNum;
        if (i2 >= 0 && i2 < this.mPages.size() && this.mPages.get(i2) != null) {
            MuPDFPage muPDFPage = this.mPages.get(i2);
            if (muPDFPage.mPageNumber == MuPDFPage.mTextSelPageNum) {
                muPDFPage.clearSelection();
            }
        }
    }

    public void closeSearch() {
        this.searchRunning = false;
    }

    public void createESignatureAt(final PointF pointF, final int i) {
        this.mWorker.add(new Worker.Task() {
            public void run() {
            }

            public void work() {
                PDFDocument pDFDocument = MuPDFDoc.getPDFDocument(MuPDFDoc.this.mDocument);
                pDFDocument.beginOperation("createSignatureAt");
                MuPDFPage muPDFPage = MuPDFDoc.this.mPages.get(i);
                Context context = MuPDFDoc.this.mContext;
                muPDFPage.mDoc.checkForWorkerThread();
                PDFPage pDFPage = MuPDFPage.getPDFPage(muPDFPage.mPage);
                if (pDFPage != null) {
                    byte[] readFileBytes = FileUtils.readFileBytes(SOPreferences.getStringPreference(SOPreferences.getPreferencesObject(context, "general"), "eSignaturePath", "path"));
                    if (readFileBytes.length > 0) {
                        Image image = new Image(readFileBytes);
                        PDFAnnotation createAnnotation = pDFPage.createAnnotation(13);
                        Rect bounds = createAnnotation.getBounds();
                        float f = bounds.x0;
                        float f2 = bounds.y0;
                        float f3 = 0.0f;
                        bounds.x0 = f3;
                        float f4 = bounds.x1 - f;
                        bounds.x1 = f4;
                        bounds.y0 = 0.0f;
                        bounds.y1 -= f2;
                        bounds.y1 = bounds.y0 + (((f4 - f3) * ((float) image.getHeight())) / ((float) image.getWidth()));
                        DisplayList displayList = new DisplayList(bounds);
                        DisplayListDevice displayListDevice = new DisplayListDevice(displayList);
                        Point point = new Point(pointF.x, pointF.y);
                        float f5 = bounds.x1 - bounds.x0;
                        float f6 = bounds.y1 - bounds.y0;
                        Matrix Identity = Matrix.Identity();
                        float f7 = f5 / 2.0f;
                        float f8 = f6 / 2.0f;
                        Identity.translate(f7, f8);
                        Identity.scale(f5, f6);
                        Identity.translate(-0.5f, -0.5f);
                        displayListDevice.fillImage(image, Identity, 1.0f, ColorParams.pack(ColorParams.RenderingIntent.RELATIVE_COLORIMETRIC, true, false, false));
                        float f9 = point.x;
                        float f10 = point.y;
                        Rect rect = new Rect(f9 - f7, f10 - f8, f9 + f7, f10 + f8);
                        muPDFPage.constrainToPage(rect);
                        createAnnotation.setRect(rect);
                        createAnnotation.setAppearance(displayList);
                        createAnnotation.update();
                        muPDFPage.mDoc.mIsModified = true;
                    }
                }
                pDFDocument.endOperation();
                MuPDFDoc.this.update(i);
            }
        });
    }

    public void createInkAnnotation(int i, SOPoint[] sOPointArr, float f, int i2) {
        final int i3 = i;
        final SOPoint[] sOPointArr2 = sOPointArr;
        final float f2 = f;
        final int i4 = i2;
        this.mWorker.add(new Worker.Task() {
            public void run() {
            }

            public void work() {
                PDFDocument pDFDocument = MuPDFDoc.getPDFDocument(MuPDFDoc.this.mDocument);
                pDFDocument.beginOperation("createInkAnnotation");
                MuPDFPage muPDFPage = MuPDFDoc.this.mPages.get(i3);
                SOPoint[] sOPointArr = sOPointArr2;
                float f = f2;
                int i = i4;
                muPDFPage.mDoc.checkForWorkerThread();
                PDFPage pDFPage = MuPDFPage.getPDFPage(muPDFPage.mPage);
                if (pDFPage != null) {
                    PDFAnnotation createAnnotation = pDFPage.createAnnotation(15);
                    createAnnotation.setBorder(f);
                    createAnnotation.setColor(MuPDFPage.colorToArray(i));
                    createAnnotation.setOpacity(((float) Color.alpha(i)) / 255.0f);
                    int length = sOPointArr.length;
                    int[] iArr = new int[2];
                    iArr[1] = length;
                    iArr[0] = 1;
                    Point[][] pointArr = (Point[][]) Array.newInstance(Point.class, iArr);
                    for (int i2 = 0; i2 < length; i2++) {
                        pointArr[0][i2] = new Point(sOPointArr[i2].x, sOPointArr[i2].y);
                    }
                    createAnnotation.setInkList(pointArr);
                    createAnnotation.update();
                    muPDFPage.mDoc.mIsModified = true;
                }
                pDFDocument.endOperation();
                MuPDFDoc.this.update(i3);
            }
        });
    }

    public void createSignatureAt(final PointF pointF, final int i) {
        this.mWorker.add(new Worker.Task() {
            public void run() {
            }

            public void work() {
                PDFDocument pDFDocument = MuPDFDoc.getPDFDocument(MuPDFDoc.this.mDocument);
                pDFDocument.beginOperation("createSignatureAt");
                MuPDFPage muPDFPage = MuPDFDoc.this.mPages.get(i);
                muPDFPage.mDoc.checkForWorkerThread();
                PDFPage pDFPage = MuPDFPage.getPDFPage(muPDFPage.mPage);
                if (pDFPage != null) {
                    float f = pointF.x;
                    float f2 = pointF.y;
                    PDFWidget createSignature = pDFPage.createSignature();
                    if (createSignature != null) {
                        Rect rect = createSignature.getRect();
                        float f3 = rect.x0;
                        float f4 = f - f3;
                        float f5 = rect.y0;
                        float f6 = f2 - f5;
                        rect.x0 = f3 + f4;
                        rect.x1 += f4;
                        rect.y0 = f5 + f6;
                        rect.y1 += f6;
                        muPDFPage.constrainToPage(rect);
                        createSignature.setRect(rect);
                        createSignature.update();
                        muPDFPage.mDoc.mIsModified = true;
                    }
                }
                pDFDocument.endOperation();
                MuPDFDoc.this.update(i);
            }
        });
    }

    public void createTextAnnotationAt(final PointF pointF, final int i) {
        this.mWorker.add(new Worker.Task() {
            public void run() {
            }

            public void work() {
                MuPDFDoc muPDFDoc = MuPDFDoc.this;
                String str = muPDFDoc.mAuthor;
                PDFDocument pDFDocument = MuPDFDoc.getPDFDocument(muPDFDoc.mDocument);
                pDFDocument.beginOperation("createTextAnnotationAt");
                MuPDFPage muPDFPage = MuPDFDoc.this.mPages.get(i);
                muPDFPage.mDoc.checkForWorkerThread();
                PDFPage pDFPage = MuPDFPage.getPDFPage(muPDFPage.mPage);
                if (pDFPage != null) {
                    PDFAnnotation createAnnotation = pDFPage.createAnnotation(0);
                    float f = pointF.x;
                    float f2 = pointF.y;
                    float f3 = (float) 24;
                    createAnnotation.setRect(new Rect(f, f2 - f3, f3 + f, f2));
                    createAnnotation.setAuthor(str);
                    createAnnotation.setModificationDate(new Date());
                    createAnnotation.update();
                    muPDFPage.mDoc.mIsModified = true;
                }
                MuPDFDoc.this.update(i);
                pDFDocument.endOperation();
            }
        });
    }

    public void deleteHighlightAnnotation() {
    }

    public void destroyDoc() {
        if (!this.mDestroyed) {
            this.mDestroyed = true;
            this.mLoadAborted = true;
            this.mWorker.add(new Worker.Task() {
                public void run() {
                    Worker worker = MuPDFDoc.this.mWorker;
                    if (worker != null) {
                        worker.alive = false;
                        worker.mThread.interrupt();
                        worker.mQueue.clear();
                        MuPDFDoc.this.mWorker = null;
                    }
                }

                public void work() {
                    MuPDFDoc muPDFDoc = MuPDFDoc.this;
                    Document document = muPDFDoc.mDocument;
                    if (document != null) {
                        PDFDocument pDFDocument = MuPDFDoc.getPDFDocument(document);
                        if (pDFDocument != null) {
                            pDFDocument.setJsEventListener(muPDFDoc.jsNullEventListener);
                            pDFDocument.disableJs();
                        }
                        MuPDFDoc.this.mDocument.destroy();
                        MuPDFDoc.this.mDocument = null;
                    }
                    ArrayList<MuPDFPage> arrayList = MuPDFDoc.this.mPages;
                    if (arrayList != null) {
                        Iterator<MuPDFPage> it = arrayList.iterator();
                        while (it.hasNext()) {
                            it.next().destroyPage();
                        }
                        MuPDFDoc.this.mPages.clear();
                        MuPDFDoc.this.mPages = null;
                    }
                    MuPDFDoc muPDFDoc2 = MuPDFDoc.this;
                    muPDFDoc2.mPageCount = 0;
                    muPDFDoc2.mPageNumber = 0;
                }
            });
        }
    }

    public void doRedo(final Runnable runnable) {
        this.mWorker.add(new Worker.Task() {
            public void run() {
                MuPDFDoc.this.clearSelection();
                MuPDFDoc.access$3200(MuPDFDoc.this);
                if (runnable != null) {
                    runnable.run();
                }
            }

            public void work() {
                ((PDFDocument) MuPDFDoc.this.mDocument).redo();
                MuPDFDoc.this.updatePages();
            }
        });
    }

    public void doUndo(final Runnable runnable) {
        this.mWorker.add(new Worker.Task() {
            public void run() {
                MuPDFDoc.this.clearSelection();
                MuPDFDoc.access$3200(MuPDFDoc.this);
                if (runnable != null) {
                    runnable.run();
                }
            }

            public void work() {
                ((PDFDocument) MuPDFDoc.this.mDocument).undo();
                MuPDFDoc.this.updatePages();
            }
        });
    }

    public void exportTo(String str, SODocSaveListener sODocSaveListener, String str2) {
        saveToExportToInternal(str, sODocSaveListener, str2);
    }

    public String getAuthor() {
        return this.mAuthor;
    }

    public String getDateFormatPattern() {
        return "yyyy-MM-dd HH:mm";
    }

    public int getNumPages() {
        return this.mNumPages;
    }

    public PDFDocument getPDFDocument() {
        return getPDFDocument(this.mDocument);
    }

    public ArDkPage getPage(int i, SOPageListener sOPageListener) {
        ArrayList<MuPDFPage> arrayList = this.mPages;
        if (arrayList == null || i >= arrayList.size()) {
            return null;
        }
        MuPDFPage muPDFPage = this.mPages.get(i);
        if (!muPDFPage.mPageListeners.contains(sOPageListener)) {
            muPDFPage.mPageListeners.add(sOPageListener);
        }
        return muPDFPage;
    }

    public MuPDFAnnotation getSelectedAnnotation() {
        int i = this.selectedAnnotPagenum;
        if (i == -1 || this.selectedAnnotIndex == -1 || i >= this.mPages.size()) {
            return null;
        }
        return this.mPages.get(this.selectedAnnotPagenum).getAnnotation(this.selectedAnnotIndex);
    }

    public int getSelectedAnnotationIndex() {
        int i = this.selectedAnnotPagenum;
        if (i == -1 || this.selectedAnnotIndex == -1 || i >= this.mPages.size()) {
            return -1;
        }
        return this.selectedAnnotIndex;
    }

    public String getSelectionAnnotationAuthor() {
        MuPDFAnnotation selectedAnnotation = getSelectedAnnotation();
        if (selectedAnnotation != null) {
            return selectedAnnotation.mAuthor;
        }
        return null;
    }

    public String getSelectionAnnotationComment() {
        MuPDFAnnotation selectedAnnotation = getSelectedAnnotation();
        if (selectedAnnotation != null) {
            return selectedAnnotation.mContents;
        }
        return null;
    }

    public String getSelectionAnnotationDate() {
        MuPDFAnnotation selectedAnnotation = getSelectedAnnotation();
        if (selectedAnnotation == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(selectedAnnotation.mModDate);
        } catch (Exception unused) {
            return null;
        }
    }

    public String getSelectionAsText() {
        int i = MuPDFPage.mTextSelPageNum;
        if (i == -1) {
            return null;
        }
        MuPDFPage muPDFPage = this.mPages.get(i);
        muPDFPage.updateText();
        android.graphics.Point point = muPDFPage.selectionStart;
        if (point == null || muPDFPage.selectionEnd == null || muPDFPage.mStructuredText == null) {
            return null;
        }
        Point point2 = new Point((float) point.x, (float) point.y);
        android.graphics.Point point3 = muPDFPage.selectionEnd;
        return muPDFPage.mStructuredText.copy(point2, new Point((float) point3.x, (float) point3.y));
    }

    public boolean getSelectionCanBeAbsolutelyPositioned() {
        return false;
    }

    public boolean getSelectionCanBeDeleted() {
        return this.selectedAnnotPagenum != -1 && this.selectedAnnotIndex != -1;
    }

    public boolean getSelectionCanBeResized() {
        return false;
    }

    public boolean getSelectionCanBeRotated() {
        return false;
    }

    public boolean getSelectionHasAssociatedPopup() {
        MuPDFAnnotation selectedAnnotation = getSelectedAnnotation();
        if (selectedAnnotation != null && selectedAnnotation.type == 0) {
            return true;
        }
        return selectedAnnotation != null && selectedAnnotation.type == 8;
    }

    public boolean getSelectionIsAlterableTextSelection() {
        return MuPDFPage.mTextSelPageNum != -1;
    }

    public boolean hasAcroForm() {
        Document document = this.mDocument;
        if (!(document instanceof PDFDocument)) {
            return false;
        }
        PDFObject trailer = ((PDFDocument) document).getTrailer();
        if (!isNull(trailer)) {
            trailer = trailer.get("Root");
        }
        if (!isNull(trailer)) {
            trailer = trailer.get("AcroForm");
        }
        if (!isNull(trailer)) {
            trailer = trailer.get("Fields");
        }
        return !isNull(trailer) && trailer.size() > 0;
    }

    public boolean hasRedactionsToApply() {
        return !this.pagesWithRedactions.isEmpty();
    }

    public boolean hasXFAForm() {
        Document document = this.mDocument;
        if (!(document instanceof PDFDocument)) {
            return false;
        }
        PDFObject trailer = ((PDFDocument) document).getTrailer();
        if (!isNull(trailer)) {
            trailer = trailer.get("Root");
        }
        if (!isNull(trailer)) {
            trailer = trailer.get("AcroForm");
        }
        if (!isNull(trailer)) {
            trailer = trailer.get("XFA");
        }
        return !isNull(trailer);
    }

    public final boolean isNull(PDFObject pDFObject) {
        return pDFObject == null || pDFObject.equals(PDFObject.Null);
    }

    public boolean isSearchRunning() {
        return this.searchRunning;
    }

    public boolean isSelectionInkAnnotation() {
        MuPDFAnnotation selectedAnnotation = getSelectedAnnotation();
        return selectedAnnotation != null && selectedAnnotation.isInk();
    }

    public void loadNextPage() {
        if (this.mLoadAborted) {
            SODocLoadListener sODocLoadListener = this.mListener;
            if (sODocLoadListener != null) {
                ((SODocSession.SODocSessionLoadListener) sODocLoadListener).onError(6, 0);
                return;
            }
            return;
        }
        this.mWorker.add(new Worker.Task() {
            public boolean done = false;
            public boolean error = false;

            public void run() {
                if (!this.done) {
                    MuPDFDoc muPDFDoc = MuPDFDoc.this;
                    int i = muPDFDoc.mPageNumber + 1;
                    muPDFDoc.mPageNumber = i;
                    SODocLoadListener sODocLoadListener = muPDFDoc.mListener;
                    if (sODocLoadListener != null) {
                        ((SODocSession.SODocSessionLoadListener) sODocLoadListener).onPageLoad(i);
                    }
                    MuPDFDoc.this.loadNextPage();
                } else if (this.error) {
                    SODocLoadListener sODocLoadListener2 = MuPDFDoc.this.mListener;
                    if (sODocLoadListener2 != null) {
                        ((SODocSession.SODocSessionLoadListener) sODocLoadListener2).onError(6, 0);
                    }
                } else {
                    SODocLoadListener sODocLoadListener3 = MuPDFDoc.this.mListener;
                    if (sODocLoadListener3 != null) {
                        ((SODocSession.SODocSessionLoadListener) sODocLoadListener3).onDocComplete();
                    }
                }
            }

            public void work() {
                try {
                    MuPDFDoc muPDFDoc = MuPDFDoc.this;
                    int i = muPDFDoc.mPageNumber;
                    if (i < muPDFDoc.mPageCount) {
                        Page loadPage = muPDFDoc.mDocument.loadPage(i);
                        MuPDFDoc muPDFDoc2 = MuPDFDoc.this;
                        Objects.requireNonNull(muPDFDoc2);
                        muPDFDoc2.mPages.add(new MuPDFPage(muPDFDoc2, loadPage, muPDFDoc2.mNumPages));
                        muPDFDoc2.mNumPages = muPDFDoc2.mPages.size();
                        MuPDFDoc muPDFDoc3 = MuPDFDoc.this;
                        if (muPDFDoc3.mPages.get(muPDFDoc3.mPageNumber).countAnnotations(12) > 0) {
                            MuPDFDoc muPDFDoc4 = MuPDFDoc.this;
                            MuPDFDoc.access$2500(muPDFDoc4, muPDFDoc4.mPageNumber);
                            return;
                        }
                        return;
                    }
                    this.done = true;
                } catch (Exception unused) {
                    this.error = true;
                    this.done = true;
                }
            }
        });
    }

    public void onSelectionUpdate(final int i) {
        new Handler().post(new Runnable() {
            public void run() {
                MuPDFDoc muPDFDoc = MuPDFDoc.this;
                muPDFDoc.mSelectionStartPage = i;
                muPDFDoc.mSelectionEndPage = i;
                SODocLoadListener sODocLoadListener = muPDFDoc.mListener;
                if (sODocLoadListener != null) {
                    ((SODocSession.SODocSessionLoadListener) sODocLoadListener).onSelectionChanged(i, i);
                }
            }
        });
    }

    public void processKeyCommand(int i) {
    }

    public final void processOutline(OutlineIterator outlineIterator, boolean z, int i, MuPDFEnumerateTocListener muPDFEnumerateTocListener) {
        if (!z || outlineIterator.down() >= 0) {
            OutlineIterator.OutlineItem item = outlineIterator.item();
            while (true) {
                if (item == null) {
                    OutlineIterator outlineIterator2 = outlineIterator;
                    break;
                }
                final int i2 = this.handleCounter + 1;
                this.handleCounter = i2;
                final String str = item.title;
                final String str2 = item.uri;
                LinkDestination resolveLinkDestination = this.mDocument.resolveLinkDestination(str2);
                int i3 = resolveLinkDestination.chapter;
                int i4 = resolveLinkDestination.page;
                int i5 = 0;
                for (int i6 = 0; i6 < i3; i6++) {
                    i5 += this.mDocument.countPages(i6);
                }
                final int i7 = i5 + i4;
                final int i8 = (int) resolveLinkDestination.x;
                final int i9 = (int) resolveLinkDestination.y;
                final int i10 = i;
                ArDkLib.runOnUiThread(() -> {
                   //  muPDFEnumerateTocListener.nextTocEntry(i2, i10, i7, str, str2, (float) i8, (float) i9); // TODO
                });
                OutlineIterator outlineIterator3 = outlineIterator;
                processOutline(outlineIterator, true, this.handleCounter, muPDFEnumerateTocListener);
                if (outlineIterator.next() != 0) {
                    break;
                }
                item = outlineIterator.item();
            }
            if (z) {
                outlineIterator.up();
            }
        }
    }

    public boolean providePassword(String str) {
        if (!this.mDocument.authenticatePassword(str)) {
            int i = this.numBadPasswords + 1;
            this.numBadPasswords = i;
            if (i >= 5) {
                SODocLoadListener sODocLoadListener = this.mListener;
                if (sODocLoadListener != null) {
                    ((SODocSession.SODocSessionLoadListener) sODocLoadListener).onError(6, 0);
                }
                return false;
            }
            SODocLoadListener sODocLoadListener2 = this.mListener;
            if (sODocLoadListener2 != null) {
                ((SODocSession.SODocSessionLoadListener) sODocLoadListener2).onError(4096, 0);
            }
            return false;
        }
        this.mValidPassword = str;
        afterValidation();
        loadNextPage();
        return true;
    }

    public void saveTo(String str, final SODocSaveListener sODocSaveListener) {
        if (canSave()) {
            saveToInternal(str, sODocSaveListener);
        } else if (str.compareToIgnoreCase(this.mOpenedPath) == 0) {
            ArDkLib.runOnUiThread(() -> {
                sODocSaveListener.onComplete(0, 0);
                MuPDFDoc.this.mLastSaveTime = System.currentTimeMillis();
            });
        } else if (FileUtils.copyFile(this.mOpenedPath, str, true)) {
            ArDkLib.runOnUiThread(() -> {
                sODocSaveListener.onComplete(0, 0);
                MuPDFDoc.this.mLastSaveTime = System.currentTimeMillis();
            });
        } else {
            ArDkLib.runOnUiThread(() -> sODocSaveListener.onComplete(1, 795));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x005f A[LOOP:0: B:22:0x005b->B:24:0x005f, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0087 A[SYNTHETIC, Splitter:B:30:0x0087] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void saveToExportToInternal(String r10, final SODocSaveListener r11, String r12) {
        /*
            r9 = this;
            java.lang.String r0 = r9.mOpenedPath
            java.lang.String r0 = com.artifex.solib.FileUtils.getExtension(r0)
            java.lang.String r1 = "pdf"
            if (r12 != 0) goto L_0x001c
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L_0x001c
            boolean r12 = r9.mIsModified
            com.artifex.solib.MuPDFDoc$8 r0 = new com.artifex.solib.MuPDFDoc$8
            r0.<init>(r12, r11)
            r9.saveToInternal(r10, r0)
            goto L_0x0096
        L_0x001c:
            if (r12 != 0) goto L_0x001f
            r12 = r1
        L_0x001f:
            com.artifex.solib.SOSecureFS r0 = com.artifex.solib.ArDkLib.mSecureFS
            java.lang.String r1 = ""
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x003e
            boolean r4 = r0.isSecurePath(r10)
            if (r4 == 0) goto L_0x003e
            java.lang.Object r10 = r0.getFileHandleForWriting(r10)
            com.artifex.solib.MuPDFDoc$7 r4 = new com.artifex.solib.MuPDFDoc$7
            r4.<init>(r10)
            com.artifex.mupdf.fitz.DocumentWriter r5 = new com.artifex.mupdf.fitz.DocumentWriter
            r5.<init>((com.artifex.mupdf.fitz.SeekableOutputStream) r4, (java.lang.String) r12, (java.lang.String) r1)
            r4 = r2
            r2 = r5
            goto L_0x0059
        L_0x003e:
            com.artifex.mupdf.fitz.FileStream r4 = new com.artifex.mupdf.fitz.FileStream     // Catch:{ IOException -> 0x0050 }
            java.lang.String r5 = "rw"
            r4.<init>((java.lang.String) r10, (java.lang.String) r5)     // Catch:{ IOException -> 0x0050 }
            com.artifex.mupdf.fitz.DocumentWriter r10 = new com.artifex.mupdf.fitz.DocumentWriter     // Catch:{ IOException -> 0x004e }
            r10.<init>((com.artifex.mupdf.fitz.SeekableOutputStream) r4, (java.lang.String) r12, (java.lang.String) r1)     // Catch:{ IOException -> 0x004e }
            r8 = r2
            r2 = r10
            r10 = r8
            goto L_0x0059
        L_0x004e:
            r10 = move-exception
            goto L_0x0052
        L_0x0050:
            r10 = move-exception
            r4 = r2
        L_0x0052:
            r10.printStackTrace()
            r11.onComplete(r3, r3)
            r10 = r2
        L_0x0059:
            r12 = 0
            r1 = 0
        L_0x005b:
            int r5 = r9.mNumPages
            if (r1 >= r5) goto L_0x007b
            com.artifex.mupdf.fitz.Document r5 = r9.mDocument
            com.artifex.mupdf.fitz.Page r5 = r5.loadPage((int) r1)
            com.artifex.mupdf.fitz.Rect r6 = r5.getBounds()
            com.artifex.mupdf.fitz.Device r6 = r2.beginPage(r6)
            com.artifex.mupdf.fitz.Matrix r7 = new com.artifex.mupdf.fitz.Matrix
            r7.<init>()
            r5.run(r6, r7)
            r2.endPage()
            int r1 = r1 + 1
            goto L_0x005b
        L_0x007b:
            r2.close()
            if (r0 == 0) goto L_0x0085
            if (r10 == 0) goto L_0x0085
            r0.closeFile(r10)
        L_0x0085:
            if (r4 == 0) goto L_0x0093
            r4.close()     // Catch:{ IOException -> 0x008b }
            goto L_0x0093
        L_0x008b:
            r10 = move-exception
            r10.printStackTrace()
            r11.onComplete(r3, r3)
            return
        L_0x0093:
            r11.onComplete(r12, r12)
        L_0x0096:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.MuPDFDoc.saveToExportToInternal(java.lang.String, com.artifex.solib.SODocSaveListener, java.lang.String):void");
    }

    public void saveToInternal(final String str, final SODocSaveListener sODocSaveListener) {
        final PDFDocument pDFDocument = getPDFDocument(this.mDocument);
        if (pDFDocument == null) {
            sODocSaveListener.onComplete(1, 0);
        } else {
            this.mWorker.add(new Worker.Task() {
                public int saveResult;

                public void run() {
                    if (sODocSaveListener != null) {
                        int i = this.saveResult;
                        if (i == 0) {
                            sODocSaveListener.onComplete(0, i);
                            MuPDFDoc.this.mLastSaveTime = System.currentTimeMillis();
                            return;
                        }
                        sODocSaveListener.onComplete(1, i);
                    }
                }

                public void work() {
                    String str;
                    int i;
                    MuPDFDoc.this.mLastSaveWasIncremental = false;
                    if (pDFDocument.canBeSavedIncrementally()) {
                        MuPDFDoc.this.mLastSaveWasIncremental = true;
                        str = "incremental";
                    } else {
                        str = "";
                    }
                    String str2 = FileUtils.getTempPathRoot(MuPDFDoc.this.mContext) + File.separator + UUID.randomUUID() + ".pdf";
                    if (str.equals("incremental")) {
                        MuPDFDoc muPDFDoc = MuPDFDoc.this;
                        String str3 = muPDFDoc.lastSavedPath;
                        if (str3 == null) {
                            str3 = muPDFDoc.mOpenedPath;
                        }
                        FileUtils.copyFile(str3, str2, true);
                    }
                    SOSecureFS sOSecureFS = ArDkLib.mSecureFS;
                    if (sOSecureFS == null || !sOSecureFS.isSecurePath(str)) {
                        try {
                            pDFDocument.save(str2, str);
                            this.saveResult = 0;
                        } catch (Exception unused) {
                            this.saveResult = 1;
                        }
                    } else {
                        Object fileHandleForWriting = sOSecureFS.getFileHandleForWriting(str2);
                        try {
                            pDFDocument.save(new SeekableInputOutputStream(fileHandleForWriting) {
                                public long position() throws IOException {
                                    return sOSecureFS.getFileOffset(r10);
                                }

                                public int read(byte[] bArr) throws IOException {
                                    int readFromFile = sOSecureFS.readFromFile(r10, bArr);
                                    if (readFromFile == 0) {
                                        return -1;
                                    }
                                    return readFromFile;
                                }

                                public long seek(long j, int i) throws IOException {
                                    long fileOffset = sOSecureFS.getFileOffset(r10);
                                    long fileLength = sOSecureFS.getFileLength(r10);
                                    if (i != 0) {
                                        j = i != 1 ? i != 2 ? 0 : j + fileLength : j + fileOffset;
                                    }
                                    sOSecureFS.seekToFileOffset(r10, j);
                                    return j;
                                }

                                public void truncate() throws IOException {
                                    if (!sOSecureFS.setFileLength(r10, sOSecureFS.getFileOffset(r10))) {
                                        throw new RuntimeException("MuPDFDoc.saveSecure - error in call to secureFS.setFileLength");
                                    }
                                }

                                public void write(byte[] bArr, int i, int i2) throws IOException {
                                    if (i == 0 && i2 == bArr.length) {
                                        sOSecureFS.writeToFile(r10, bArr);
                                    } else {
                                        sOSecureFS.writeToFile(r10, Arrays.copyOfRange(bArr, i, i2 + i));
                                    }
                                }
                            }, str);
                            sOSecureFS.closeFile(fileHandleForWriting);
                            i = 0;
                        } catch (Exception unused2) {
                            sOSecureFS.closeFile(fileHandleForWriting);
                            i = 1;
                        } catch (Throwable th) {
                            sOSecureFS.closeFile(fileHandleForWriting);
                            throw th;
                        }
                        this.saveResult = i;
                    }
                    if (this.saveResult == 0) {
                        MuPDFDoc.this.mIsModified = false;
                        if (FileUtils.copyFile(str2, str, true)) {
                            FileUtils.deleteFile(str2);
                            MuPDFDoc.this.lastSavedPath = str;
                        }
                    }
                }
            });
        }
    }

    public void saveToPDF(String str, boolean z, SODocSaveListener sODocSaveListener) {
        saveToExportToInternal(str, sODocSaveListener, null);
    }

    public int search() {
        this.searchRunning = true;
        this.searchCancelled = false;
        this.searchMatchFound = false;
        this.mWorker.add(new Worker.Task() {
            public void run() {
                android.graphics.Rect[] rectArr;
                int i;
                MuPDFDoc muPDFDoc = MuPDFDoc.this;
                if (muPDFDoc.searchCancelled) {
                    SOSearchListener sOSearchListener = muPDFDoc.searchListener;
                    if (sOSearchListener != null) {
                        NUIDocView.access$2600(NUIDocView.this);
                        NUIDocView.this.mIsSearching = false;
                    }
                } else if (!muPDFDoc.searchMatchFound) {
                    SOSearchListener sOSearchListener2 = muPDFDoc.searchListener;
                    if (sOSearchListener2 != null) {
                        sOSearchListener2.notFound();
                    }
                } else {
                    MuPDFPage muPDFPage = muPDFDoc.mPages.get(muPDFDoc.searchPage);
                    muPDFPage.searchIndex = MuPDFDoc.this.searchIndex;
                    muPDFPage.updatePageRect(muPDFPage.pageBounds);
                    MuPDFDoc muPDFDoc2 = MuPDFDoc.this;
                    if (muPDFDoc2.searchListener != null) {
                        MuPDFPage muPDFPage2 = muPDFDoc2.mPages.get(muPDFDoc2.searchPage);
                        Quad[][] quadArr = muPDFPage2.searchResults;
                        if (quadArr == null || quadArr.length <= 0 || (i = muPDFPage2.searchIndex) < 0 || i >= quadArr.length) {
                            rectArr = null;
                        } else {
                            ArrayList arrayList = new ArrayList();
                            for (Quad rect : muPDFPage2.searchResults[muPDFPage2.searchIndex]) {
                                arrayList.add(muPDFPage2.toRect(rect.toRect()));
                            }
                            rectArr = (android.graphics.Rect[]) arrayList.toArray(new android.graphics.Rect[arrayList.size()]);
                        }
                        android.graphics.Rect rect2 = rectArr[0];
                        MuPDFDoc muPDFDoc3 = MuPDFDoc.this;
                        Objects.requireNonNull(muPDFDoc3.mPages.get(muPDFDoc3.searchPage));
                        RectF rectF = new RectF((float) rect2.left, (float) rect2.top, (float) rect2.right, (float) rect2.bottom);
                        MuPDFDoc muPDFDoc4 = MuPDFDoc.this;
                        SOSearchListener sOSearchListener3 = muPDFDoc4.searchListener;
                        if (sOSearchListener3 != null) {
                            sOSearchListener3.found(muPDFDoc4.searchPage, rectF);
                        }
                    }
                }
                MuPDFDoc.this.searchRunning = false;
            }

            /* JADX WARNING: Removed duplicated region for block: B:15:0x003b  */
            /* JADX WARNING: Removed duplicated region for block: B:18:0x0048  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void work() {
                /*
                    r7 = this;
                    r0 = 0
                    r1 = 0
                L_0x0002:
                    com.artifex.solib.MuPDFDoc r2 = com.artifex.solib.MuPDFDoc.this
                    boolean r3 = r2.searchRunning
                    r4 = 1
                    if (r3 != 0) goto L_0x000d
                    r2.searchCancelled = r4
                    goto L_0x0089
                L_0x000d:
                    java.util.ArrayList<com.artifex.solib.MuPDFPage> r3 = r2.mPages
                    int r2 = r2.searchPage
                    java.lang.Object r2 = r3.get(r2)
                    com.artifex.solib.MuPDFPage r2 = (com.artifex.solib.MuPDFPage) r2
                    com.artifex.solib.MuPDFDoc r3 = com.artifex.solib.MuPDFDoc.this
                    java.lang.String r3 = r3.searchText
                    com.artifex.solib.MuPDFDoc r5 = r2.mDoc
                    r5.checkForWorkerThread()
                    java.lang.String r5 = r2.lastSearch     // Catch:{ Exception -> 0x0038 }
                    boolean r5 = r3.equalsIgnoreCase(r5)     // Catch:{ Exception -> 0x0038 }
                    if (r5 != 0) goto L_0x0030
                    com.artifex.mupdf.fitz.Page r5 = r2.mPage     // Catch:{ Exception -> 0x0038 }
                    com.artifex.mupdf.fitz.Quad[][] r5 = r5.search(r3)     // Catch:{ Exception -> 0x0038 }
                    r2.searchResults = r5     // Catch:{ Exception -> 0x0038 }
                L_0x0030:
                    r2.lastSearch = r3     // Catch:{ Exception -> 0x0038 }
                    com.artifex.mupdf.fitz.Quad[][] r2 = r2.searchResults     // Catch:{ Exception -> 0x0038 }
                    if (r2 == 0) goto L_0x0038
                    int r2 = r2.length     // Catch:{ Exception -> 0x0038 }
                    goto L_0x0039
                L_0x0038:
                    r2 = 0
                L_0x0039:
                    if (r2 != 0) goto L_0x0048
                    int r1 = r1 + 1
                    com.artifex.solib.MuPDFDoc r2 = com.artifex.solib.MuPDFDoc.this
                    int r3 = r2.mNumPages
                    if (r1 != r3) goto L_0x0044
                    goto L_0x0089
                L_0x0044:
                    com.artifex.solib.MuPDFDoc.access$1900(r2)
                    goto L_0x0002
                L_0x0048:
                    com.artifex.solib.MuPDFDoc r3 = com.artifex.solib.MuPDFDoc.this
                    boolean r5 = r3.searchNewPage
                    if (r5 == 0) goto L_0x005b
                    boolean r1 = r3.searchBackwards
                    if (r1 == 0) goto L_0x0056
                    int r2 = r2 - r4
                    r3.searchIndex = r2
                    goto L_0x0058
                L_0x0056:
                    r3.searchIndex = r0
                L_0x0058:
                    r3.searchNewPage = r0
                    goto L_0x0072
                L_0x005b:
                    boolean r5 = r3.searchBackwards
                    if (r5 == 0) goto L_0x0066
                    int r6 = r3.searchIndex
                    int r6 = r6 + -1
                    r3.searchIndex = r6
                    goto L_0x006b
                L_0x0066:
                    int r6 = r3.searchIndex
                    int r6 = r6 + r4
                    r3.searchIndex = r6
                L_0x006b:
                    int r6 = r3.searchIndex
                    if (r6 < 0) goto L_0x0075
                    if (r6 < r2) goto L_0x0072
                    goto L_0x0075
                L_0x0072:
                    r3.searchMatchFound = r4
                    goto L_0x0089
                L_0x0075:
                    if (r5 == 0) goto L_0x007f
                    int r2 = r3.searchPage
                    if (r2 != 0) goto L_0x008a
                    com.artifex.solib.MuPDFDoc.access$1900(r3)
                    goto L_0x0089
                L_0x007f:
                    int r2 = r3.searchPage
                    int r5 = r3.mNumPages
                    int r5 = r5 - r4
                    if (r2 != r5) goto L_0x008a
                    com.artifex.solib.MuPDFDoc.access$1900(r3)
                L_0x0089:
                    return
                L_0x008a:
                    com.artifex.solib.MuPDFDoc.access$1900(r3)
                    goto L_0x0002
                */
                throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.MuPDFDoc.SODocSessionLoadListener3.work():void");
            }
        });
        return 0;
    }

    public void selectionDelete() {
        if (this.selectedAnnotPagenum != -1 && this.selectedAnnotIndex != -1) {
            this.mWorker.add(new Worker.Task() {
                public void run() {
                    MuPDFDoc.this.clearSelection();
                }

                public void work() {
                    MuPDFDoc muPDFDoc = MuPDFDoc.this;
                    int countAnnotations = muPDFDoc.mPages.get(muPDFDoc.selectedAnnotPagenum).countAnnotations(12);
                    MuPDFDoc muPDFDoc2 = MuPDFDoc.this;
                    MuPDFPage muPDFPage = muPDFDoc2.mPages.get(muPDFDoc2.selectedAnnotPagenum);
                    MuPDFAnnotation annotation = muPDFPage.getAnnotation(MuPDFDoc.this.selectedAnnotIndex);
                    boolean z = annotation.type == 12;
                    PDFDocument pDFDocument = (PDFDocument) MuPDFDoc.this.mDocument;
                    pDFDocument.beginOperation("selectionDelete");
                    muPDFPage.mDoc.checkForWorkerThread();
                    PDFPage pDFPage = MuPDFPage.getPDFPage(muPDFPage.mPage);
                    if (pDFPage != null) {
                        pDFPage.deleteAnnotation(annotation.getPDFAnnotation());
                        muPDFPage.mDoc.mIsModified = true;
                    }
                    pDFDocument.endOperation();
                    MuPDFDoc muPDFDoc3 = MuPDFDoc.this;
                    muPDFDoc3.update(muPDFDoc3.selectedAnnotPagenum);
                    if (countAnnotations <= 1 && z) {
                        MuPDFDoc muPDFDoc4 = MuPDFDoc.this;
                        Integer num = Integer.valueOf(muPDFDoc4.selectedAnnotPagenum);
                        muPDFDoc4.pagesWithRedactions.remove(num);
                    }
                }
            });
        }
    }

    public boolean selectionIsRedaction() {
        return selectionIsType(12);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0012, code lost:
        r0 = (r0 = r2.mPages.get(r2.selectedAnnotPagenum)).getAnnotation(r2.selectedAnnotIndex);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean selectionIsTextRedaction() {
        /*
            r2 = this;
            boolean r0 = r2.selectionIsRedaction()
            if (r0 == 0) goto L_0x0020
            java.util.ArrayList<com.artifex.solib.MuPDFPage> r0 = r2.mPages
            int r1 = r2.selectedAnnotPagenum
            java.lang.Object r0 = r0.get(r1)
            com.artifex.solib.MuPDFPage r0 = (com.artifex.solib.MuPDFPage) r0
            if (r0 == 0) goto L_0x0020
            int r1 = r2.selectedAnnotIndex
            com.artifex.solib.MuPDFAnnotation r0 = r0.getAnnotation(r1)
            if (r0 == 0) goto L_0x0020
            int r0 = r0.mQuadPointCount
            if (r0 <= 0) goto L_0x0020
            r0 = 1
            return r0
        L_0x0020:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.MuPDFDoc.selectionIsTextRedaction():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0005, code lost:
        r0 = r2.mPages.get(r0).getAnnotation(r2.selectedAnnotIndex);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean selectionIsType(int r3) {
        /*
            r2 = this;
            int r0 = r2.selectedAnnotPagenum
            r1 = -1
            if (r0 == r1) goto L_0x001b
            java.util.ArrayList<com.artifex.solib.MuPDFPage> r1 = r2.mPages
            java.lang.Object r0 = r1.get(r0)
            com.artifex.solib.MuPDFPage r0 = (com.artifex.solib.MuPDFPage) r0
            int r1 = r2.selectedAnnotIndex
            com.artifex.solib.MuPDFAnnotation r0 = r0.getAnnotation(r1)
            if (r0 == 0) goto L_0x001b
            int r0 = r0.type
            if (r0 != r3) goto L_0x001b
            r3 = 1
            return r3
        L_0x001b:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.MuPDFDoc.selectionIsType(int):boolean");
    }

    public boolean setAuthor(String str) {
        this.mAuthor = str;
        return true;
    }

    public void setForceReload(boolean z) {
        this.mForceReload = z;
    }

    public void setForceReloadAtResume(boolean z) {
        this.mForceReloadAtResume = z;
    }

    public void setSearchBackwards(boolean z) {
        if (!this.searchRunning) {
            this.searchBackwards = z;
            return;
        }
        throw new IllegalArgumentException("Search already in progess");
    }

    public void setSearchListener(SOSearchListener sOSearchListener) {
        if (sOSearchListener == null) {
            this.searchRunning = false;
            this.searchListener = sOSearchListener;
        } else if (!this.searchRunning) {
            this.searchListener = sOSearchListener;
        } else {
            throw new IllegalArgumentException("Search already in progess");
        }
    }

    public void setSearchMatchCase(boolean z) {
        if (this.searchRunning) {
            throw new IllegalArgumentException("Search already in progess");
        }
    }

    public void setSearchStart(int i, float f, float f2) {
        if (this.searchRunning) {
            throw new IllegalArgumentException("Search already in progess");
        }
    }

    public void setSearchString(String str) {
        if (!this.searchRunning) {
            if (!str.equalsIgnoreCase(this.searchText)) {
                this.searchIndex = 0;
                this.searchPage = 0;
                this.searchNewPage = true;
            }
            this.searchText = str;
            return;
        }
        throw new IllegalArgumentException("Search already in progess");
    }

    public void setSelectionAnnotationComment(final String str) {
        final MuPDFAnnotation selectedAnnotation = getSelectedAnnotation();
        if (selectedAnnotation != null && str != null) {
            String str2 = selectedAnnotation.mContents;
            boolean z = str2 == null || str2.compareTo(str) != 0;
            if (z) {
                this.mWorker.add(new Worker.Task() {
                    public void run() {
                    }

                    public void work() {
                        MuPDFAnnotation muPDFAnnotation = selectedAnnotation;
                        String str = str;
                        muPDFAnnotation.mDoc.checkForWorkerThread();
                        muPDFAnnotation.mContents = str;
                        muPDFAnnotation.mAnnotation.setContents(str);
                        Date date = new Date();
                        MuPDFAnnotation muPDFAnnotation2 = selectedAnnotation;
                        muPDFAnnotation2.mDoc.checkForWorkerThread();
                        muPDFAnnotation2.mModDate = date;
                        muPDFAnnotation2.mAnnotation.setModificationDate(date);
                        MuPDFDoc muPDFDoc = MuPDFDoc.this;
                        muPDFDoc.update(muPDFDoc.selectedAnnotIndex);
                        MuPDFDoc.this.mIsModified = true;
                    }
                });
            }
        }
    }

    public void setSelectionInkColor(final int i) {
        final MuPDFAnnotation selectedAnnotation = getSelectedAnnotation();
        if (selectedAnnotation != null && selectedAnnotation.isInk()) {
            this.mWorker.add(new Worker.Task() {
                public void run() {
                }

                public void work() {
                    PDFDocument pDFDocument = (PDFDocument) MuPDFDoc.this.mDocument;
                    pDFDocument.beginOperation("setSelectionInkColor");
                    PDFAnnotation pDFAnnotation = selectedAnnotation.getPDFAnnotation();
                    pDFAnnotation.setColor(MuPDFPage.colorToArray(i));
                    pDFAnnotation.setOpacity(((float) Color.alpha(i)) / 255.0f);
                    pDFDocument.endOperation();
                    MuPDFDoc muPDFDoc = MuPDFDoc.this;
                    muPDFDoc.update(muPDFDoc.selectedAnnotPagenum);
                }
            });
        }
    }

    public void setSelectionInkThickness(final float f) {
        final MuPDFAnnotation selectedAnnotation = getSelectedAnnotation();
        if (selectedAnnotation != null && selectedAnnotation.isInk()) {
            this.mWorker.add(new Worker.Task() {
                public void run() {
                }

                public void work() {
                    PDFDocument pDFDocument = (PDFDocument) MuPDFDoc.this.mDocument;
                    pDFDocument.beginOperation("setSelectionInkWidth");
                    selectedAnnotation.getPDFAnnotation().setBorder(f);
                    pDFDocument.endOperation();
                    MuPDFDoc muPDFDoc = MuPDFDoc.this;
                    muPDFDoc.update(muPDFDoc.selectedAnnotPagenum);
                }
            });
        }
    }

    public void update(final int i) {
        final Waiter waiter = new Waiter();
        this.mWorker.add(new Worker.Task(false) {
            public void run() {
                SODocLoadListener sODocLoadListener = MuPDFDoc.this.mListener;
                if (sODocLoadListener != null) {
                    ((SODocSession.SODocSessionLoadListener) sODocLoadListener).onDocComplete();
                    SODocLoadListener sODocLoadListener2 = MuPDFDoc.this.mListener;
                    ((SODocSession.SODocSessionLoadListener) sODocLoadListener2).onSelectionChanged(i, i);
                }
            }

            public void work() {
                MuPDFPage muPDFPage = MuPDFDoc.this.mPages.get(i);
                if (muPDFPage != null) {
                    muPDFPage.update();
                    if (false) {
                        waiter.done();
                    }
                }
            }
        });
    }

    public void updatePages() {
        ((PDFDocument) this.mDocument).calculate();
        ArrayList<MuPDFPage> arrayList = this.mPages;
        if (arrayList != null) {
            Iterator<MuPDFPage> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().needsUpdate = true;
            }
        }
    }

    public static PDFDocument getPDFDocument(Document document) {
        try {
            return (PDFDocument) document;
        } catch (Exception unused) {
            return null;
        }
    }
}
