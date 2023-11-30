package com.artifex.source;

import a.a.a.a.b.d;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import androidx.cardview.R$color;
import com.android.billingclient.api.zzay;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.google.android.gms.internal.play_billing.zzb;
import com.google.android.gms.internal.play_billing.zzfb;
import com.google.android.gms.internal.play_billing.zzff;
import com.google.android.gms.internal.play_billing.zzfm;
import com.google.android.gms.internal.play_billing.zzfu;
import com.google.android.gms.internal.play_billing.zzfv;
import com.google.android.play.core.assetpacks.zzcr;
import com.google.android.play.core.assetpacks.zzcz;
import com.google.android.play.core.assetpacks.zzda;
import com.google.android.play.core.assetpacks.zzdb;
import com.google.android.play.core.assetpacks.zzdc;
import com.google.android.play.core.assetpacks.zzdd;
import com.google.android.play.core.assetpacks.zzde;
import com.google.android.play.core.assetpacks.zzy;
import com.google.android.play.core.integrity.zzt;
import com.google.android.play.integrity.internal.zzaa;
import com.google.android.play.integrity.internal.zzi;
import com.google.firebase.firestore.local.BundleCache;
import com.google.firebase.inject.Provider;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.GenericGFPoly;
import com.vungle.warren.model.ReportDBAdapter;
import com.zoho.desk.asap.R$string;
import com.zoho.livechat.android.ui.listener.FileDownloadingListener;
import com.zoho.livechat.android.utils.FileDownload;
import com.zoho.universalimageloader.cache.memory.MemoryCache;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import org.spongycastle.asn1.x500.style.AbstractX500NameStyle;

public class c implements ResourceEncoder, zzdd, zzaa, BundleCache, MemoryCache {
    public static c fileDownloader$com$zoho$livechat$android$utils$FileDownloader;
    public final /* synthetic */ int $r8$classId;

    /* renamed from: a  reason: collision with root package name */
    public Object f399a;
    public Object b;

    public c(int i) {
        this.$r8$classId = i;
        if (i == 6) {
            this.f399a = new HashMap();
            this.b = new HashMap();
        } else if (i != 9) {
            this.f399a = new f();
        } else {
            this.f399a = Collections.synchronizedMap(new WeakHashMap());
            this.b = new ArrayList();
        }
    }

    public static c getInstance() {
        if (fileDownloader$com$zoho$livechat$android$utils$FileDownloader == null) {
            fileDownloader$com$zoho$livechat$android$utils$FileDownloader = new c(9);
        }
        return fileDownloader$com$zoho$livechat$android$utils$FileDownloader;
    }

    public d b() {
        if (((d) this.b) == null) {
            synchronized (c.class) {
                try {
                    if (((d) this.b) == null) {
                        d dVar = (d) ((e) ((BlockingQueue) ((f) this.f399a).f401a).poll());
                        if (dVar != null) {
                            dVar.f400a = new WeakReference((Object) null);
                            HandlerThread handlerThread = dVar.b;
                            if (handlerThread != null) {
                                handlerThread.setName("ssdk_io_handler");
                            }
                        } else {
                            HandlerThread handlerThread2 = new HandlerThread("ssdk_io_handler");
                            handlerThread2.start();
                            dVar = new d(handlerThread2, (d) null);
                        }
                        this.b = dVar;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return (d) this.b;
    }

    public GenericGFPoly buildGenerator(int i) {
        int i2 = i;
        if (i2 >= ((List) this.b).size()) {
            Object obj = this.b;
            int i3 = 1;
            GenericGFPoly genericGFPoly = (GenericGFPoly) ((List) obj).get(((List) obj).size() - 1);
            int size = ((List) this.b).size();
            while (size <= i2) {
                Object obj2 = this.f399a;
                GenericGF genericGF = (GenericGF) obj2;
                int[] iArr = new int[2];
                iArr[0] = i3;
                iArr[i3] = ((GenericGF) obj2).expTable[(size - 1) + ((GenericGF) obj2).generatorBase];
                GenericGFPoly genericGFPoly2 = new GenericGFPoly(genericGF, iArr);
                if (genericGFPoly.field.equals(genericGF)) {
                    if (genericGFPoly.isZero() || genericGFPoly2.isZero()) {
                        genericGFPoly = genericGFPoly.field.zero;
                    } else {
                        int[] iArr2 = genericGFPoly.coefficients;
                        int length = iArr2.length;
                        int[] iArr3 = genericGFPoly2.coefficients;
                        int length2 = iArr3.length;
                        int[] iArr4 = new int[((length + length2) - 1)];
                        for (int i4 = 0; i4 < length; i4++) {
                            int i5 = iArr2[i4];
                            for (int i6 = 0; i6 < length2; i6++) {
                                int i7 = i4 + i6;
                                iArr4[i7] = genericGFPoly.field.multiply(i5, iArr3[i6]) ^ iArr4[i7];
                            }
                        }
                        genericGFPoly = new GenericGFPoly(genericGFPoly.field, iArr4);
                    }
                    ((List) this.b).add(genericGFPoly);
                    size++;
                    i3 = 1;
                } else {
                    throw new IllegalArgumentException("GenericGFPolys do not have same GenericGF field");
                }
            }
        }
        return (GenericGFPoly) ((List) this.b).get(i2);
    }

    public void downloadFile(String str, String str2, long j, FileDownloadingListener fileDownloadingListener) {
        if (fileDownloadingListener != null) {
            ((Map) this.f399a).put(str, fileDownloadingListener);
        }
        if (!((ArrayList) this.b).contains(str)) {
            ((ArrayList) this.b).add(str);
            new FileDownload(str, str2, j).start();
            if (fileDownloadingListener != null) {
                fileDownloadingListener.onDownloadStarted();
            }
        }
    }

    public Bitmap get(String str) {
        return ((MemoryCache) this.f399a).get(str);
    }

    public EncodeStrategy getEncodeStrategy(Options options) {
        return ((ResourceEncoder) this.b).getEncodeStrategy(options);
    }

    public FileDownloadingListener getListener(String str) {
        return (FileDownloadingListener) ((Map) this.f399a).get(str);
    }

    public void grantUriPermissionToProvider(Context context) {
        for (Uri grantUriPermission : (List) this.b) {
            context.grantUriPermission(((Intent) this.f399a).getPackage(), grantUriPermission, 1);
        }
    }

    public boolean isAlreadyDownloading(String str) {
        return ((ArrayList) this.b).contains(str);
    }

    public Collection<String> keys() {
        return ((MemoryCache) this.f399a).keys();
    }

    public boolean put(String str, Bitmap bitmap) {
        synchronized (((MemoryCache) this.f399a)) {
            String str2 = null;
            Iterator<String> it = ((MemoryCache) this.f399a).keys().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String next = it.next();
                if (((Comparator) this.b).compare(str, next) == 0) {
                    str2 = next;
                    break;
                }
            }
            if (str2 != null) {
                ((MemoryCache) this.f399a).remove(str2);
            }
        }
        return ((MemoryCache) this.f399a).put(str, bitmap);
    }

    public Bitmap remove(String str) {
        return ((MemoryCache) this.f399a).remove(str);
    }

    public void removeListener(String str) {
        ((Map) this.f399a).remove(str);
    }

    public void setListener(String str, FileDownloadingListener fileDownloadingListener) {
        ((Map) this.f399a).put(str, fileDownloadingListener);
    }

    public Object zza() {
        zzdc zzdc;
        switch (this.$r8$classId) {
            case 4:
                zzde zzde = (zzde) this.f399a;
                Bundle bundle = (Bundle) this.b;
                Objects.requireNonNull(zzde);
                int i = bundle.getInt("session_id");
                if (i == 0) {
                    return Boolean.FALSE;
                }
                Map map = zzde.zzf;
                Integer valueOf = Integer.valueOf(i);
                boolean z = false;
                if (map.containsKey(valueOf)) {
                    zzdb zzq = zzde.zzq(i);
                    int i2 = bundle.getInt(R$string.zza(ReportDBAdapter.ReportColumns.COLUMN_REPORT_STATUS, zzq.zzc.zza));
                    zzda zzda = zzq.zzc;
                    int i3 = zzda.zzd;
                    if (R$color.zzc(i3, i2)) {
                        zzde.zza.zza("Found stale update for session %s with status %d.", valueOf, Integer.valueOf(i3));
                        zzda zzda2 = zzq.zzc;
                        String str = zzda2.zza;
                        int i4 = zzda2.zzd;
                        if (i4 == 4) {
                            ((zzy) zzde.zzc.zza()).zzh(i, str);
                        } else if (i4 == 5) {
                            ((zzy) zzde.zzc.zza()).zzi(i);
                        } else if (i4 == 6) {
                            ((zzy) zzde.zzc.zza()).zze(Arrays.asList(new String[]{str}));
                        }
                    } else {
                        zzda.zzd = i2;
                        if (R$color.zzd(i2)) {
                            zzde.zzr(new zzcr(zzde, i, 0));
                            zzde.zzd.zzc(zzq.zzc.zza);
                        } else {
                            for (zzdc zzdc2 : zzda.zzf) {
                                ArrayList parcelableArrayList = bundle.getParcelableArrayList(R$string.zzb("chunk_intents", zzq.zzc.zza, zzdc2.zza));
                                if (parcelableArrayList != null) {
                                    for (int i5 = 0; i5 < parcelableArrayList.size(); i5++) {
                                        if (!(parcelableArrayList.get(i5) == null || ((Intent) parcelableArrayList.get(i5)).getData() == null)) {
                                            ((zzcz) zzdc2.zzd.get(i5)).zza = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    String zzs = zzde.zzs(bundle);
                    long j = bundle.getLong(R$string.zza("pack_version", zzs));
                    String string = bundle.getString(R$string.zza("pack_version_tag", zzs), "");
                    int i6 = bundle.getInt(R$string.zza(ReportDBAdapter.ReportColumns.COLUMN_REPORT_STATUS, zzs));
                    long j2 = bundle.getLong(R$string.zza("total_bytes_to_download", zzs));
                    List stringArrayList = bundle.getStringArrayList(R$string.zza("slice_ids", zzs));
                    ArrayList arrayList = new ArrayList();
                    if (stringArrayList == null) {
                        stringArrayList = Collections.emptyList();
                    }
                    Iterator it = stringArrayList.iterator();
                    while (it.hasNext()) {
                        String str2 = (String) it.next();
                        List<Intent> parcelableArrayList2 = bundle.getParcelableArrayList(R$string.zzb("chunk_intents", zzs, str2));
                        ArrayList arrayList2 = new ArrayList();
                        if (parcelableArrayList2 == null) {
                            parcelableArrayList2 = Collections.emptyList();
                        }
                        for (Intent intent : parcelableArrayList2) {
                            Iterator it2 = it;
                            if (intent != null) {
                                z = true;
                            }
                            arrayList2.add(new zzcz(z));
                            it = it2;
                            z = false;
                        }
                        Iterator it3 = it;
                        String string2 = bundle.getString(R$string.zzb("uncompressed_hash_sha256", zzs, str2));
                        long j3 = bundle.getLong(R$string.zzb("uncompressed_size", zzs, str2));
                        int i7 = bundle.getInt(R$string.zzb("patch_format", zzs, str2), 0);
                        if (i7 != 0) {
                            zzdc = new zzdc(str2, string2, j3, arrayList2, 0, i7);
                            z = false;
                        } else {
                            z = false;
                            zzdc = new zzdc(str2, string2, j3, arrayList2, bundle.getInt(R$string.zzb("compression_format", zzs, str2), 0), 0);
                        }
                        arrayList.add(zzdc);
                        it = it3;
                    }
                    zzde.zzf.put(Integer.valueOf(i), new zzdb(i, bundle.getInt("app_version_code"), new zzda(zzs, j, i6, j2, arrayList, string)));
                }
                return Boolean.TRUE;
            default:
                return new zzt((Context) ((zzaa) this.f399a).zza(), (zzi) ((zzaa) this.b).zza());
        }
    }

    public void zzb(zzff zzff) {
        try {
            zzfu zzv = zzfv.zzv();
            zzfm zzfm = (zzfm) this.f399a;
            if (zzfm != null) {
                zzv.zzk(zzfm);
            }
            zzv.zzj(zzff);
            ((zzay) this.b).zza((zzfv) zzv.zzc());
        } catch (Throwable unused) {
            zzb.zzj("BillingLogger", "Unable to log.");
        }
    }

    public boolean encode(Resource<BitmapDrawable> resource, File file, Options options) {
        return ((ResourceEncoder) this.b).encode(new BitmapResource(resource.get().getBitmap(), (BitmapPool) this.f399a), file, options);
    }

    /* renamed from: remove  reason: collision with other method in class */
    public void m0remove(String str) {
        ((ArrayList) this.b).remove(str);
    }

    public void encode(int[] iArr, int i) {
        GenericGFPoly genericGFPoly;
        if (i != 0) {
            int length = iArr.length - i;
            if (length > 0) {
                GenericGFPoly buildGenerator = buildGenerator(i);
                int[] iArr2 = new int[length];
                System.arraycopy(iArr, 0, iArr2, 0, length);
                GenericGFPoly multiplyByMonomial = new GenericGFPoly((GenericGF) this.f399a, iArr2).multiplyByMonomial(i, 1);
                if (!multiplyByMonomial.field.equals(buildGenerator.field)) {
                    throw new IllegalArgumentException("GenericGFPolys do not have same GenericGF field");
                } else if (!buildGenerator.isZero()) {
                    GenericGFPoly genericGFPoly2 = multiplyByMonomial.field.zero;
                    int coefficient = buildGenerator.getCoefficient(buildGenerator.getDegree());
                    GenericGF genericGF = multiplyByMonomial.field;
                    Objects.requireNonNull(genericGF);
                    if (coefficient != 0) {
                        int i2 = genericGF.expTable[(genericGF.size - genericGF.logTable[coefficient]) - 1];
                        GenericGFPoly genericGFPoly3 = multiplyByMonomial;
                        while (genericGFPoly3.getDegree() >= buildGenerator.getDegree() && !genericGFPoly3.isZero()) {
                            int degree = genericGFPoly3.getDegree() - buildGenerator.getDegree();
                            int multiply = multiplyByMonomial.field.multiply(genericGFPoly3.getCoefficient(genericGFPoly3.getDegree()), i2);
                            GenericGFPoly multiplyByMonomial2 = buildGenerator.multiplyByMonomial(degree, multiply);
                            GenericGF genericGF2 = multiplyByMonomial.field;
                            Objects.requireNonNull(genericGF2);
                            if (degree >= 0) {
                                if (multiply == 0) {
                                    genericGFPoly = genericGF2.zero;
                                } else {
                                    int[] iArr3 = new int[(degree + 1)];
                                    iArr3[0] = multiply;
                                    genericGFPoly = new GenericGFPoly(genericGF2, iArr3);
                                }
                                genericGFPoly2 = genericGFPoly2.addOrSubtract(genericGFPoly);
                                genericGFPoly3 = genericGFPoly3.addOrSubtract(multiplyByMonomial2);
                            } else {
                                throw new IllegalArgumentException();
                            }
                        }
                        int[] iArr4 = new GenericGFPoly[]{genericGFPoly2, genericGFPoly3}[1].coefficients;
                        int length2 = i - iArr4.length;
                        for (int i3 = 0; i3 < length2; i3++) {
                            iArr[length + i3] = 0;
                        }
                        System.arraycopy(iArr4, 0, iArr, length + length2, iArr4.length);
                        return;
                    }
                    throw new ArithmeticException();
                } else {
                    throw new IllegalArgumentException("Divide by 0");
                }
            } else {
                throw new IllegalArgumentException("No data bytes provided");
            }
        } else {
            throw new IllegalArgumentException("No error correction bytes");
        }
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ c(a aVar) {
        this(0);
        this.$r8$classId = 0;
    }

    public /* synthetic */ c(zzde zzde, Bundle bundle) {
        this.$r8$classId = 4;
        this.f399a = zzde;
        this.b = bundle;
    }

    public c(zzaa zzaa, zzaa zzaa2) {
        this.$r8$classId = 5;
        this.f399a = zzaa;
        this.b = zzaa2;
    }

    public c(Context context, zzfm zzfm) {
        this.$r8$classId = 2;
        this.b = new zzay(context);
        this.f399a = zzfm;
    }

    public c(BitmapPool bitmapPool, ResourceEncoder resourceEncoder) {
        this.$r8$classId = 3;
        this.f399a = bitmapPool;
        this.b = resourceEncoder;
    }

    public c(AbstractX500NameStyle abstractX500NameStyle) {
        this.$r8$classId = 11;
        this.b = new Vector();
        this.f399a = abstractX500NameStyle;
    }

    public c(GenericGF genericGF) {
        this.$r8$classId = 8;
        this.f399a = genericGF;
        ArrayList arrayList = new ArrayList();
        this.b = arrayList;
        arrayList.add(new GenericGFPoly(genericGF, new int[]{1}));
    }

    public c(Intent intent, List list) {
        this.$r8$classId = 1;
        this.f399a = intent;
        this.b = list;
    }

    public c(Provider provider) {
        this.$r8$classId = 7;
        this.b = c$$ExternalSyntheticOutline1.m();
        this.f399a = provider;
    }

    public void zza(zzfb zzfb) {
        try {
            zzfu zzv = zzfv.zzv();
            zzfm zzfm = (zzfm) this.f399a;
            if (zzfm != null) {
                zzv.zzk(zzfm);
            }
            zzv.zzi(zzfb);
            ((zzay) this.b).zza((zzfv) zzv.zzc());
        } catch (Throwable unused) {
            zzb.zzj("BillingLogger", "Unable to log.");
        }
    }
}
