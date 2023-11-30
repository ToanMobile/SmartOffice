package com.artifex.source.util.a.util_a.b;

import com.artifex.source.util.a.util_a.b.f.a;
import android.content.Context;
import android.os.Bundle;
import com.android.billingclient.api.BillingClientImpl;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.play.core.assetpacks.AssetPackState;
import com.google.android.play.core.assetpacks.zzbb;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.internal.ExponentialBackoffSender;
import com.zoho.desk.conversation.database.ZDChatDaoInterface;
import java.util.ArrayList;

public class d implements Runnable {
    public final /* synthetic */ int $r8$classId = 0;

    /* renamed from: a  reason: collision with root package name */
    public Object f405a;
    public Object b;
    public Object c;

    public d(Context context, a aVar) {
        this.f405a = aVar;
        this.b = context;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        com.artifex.source.util.a.util_a.b.a.f402a = 501;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x0173 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r7 = this;
            int r0 = r7.$r8$classId
            r1 = 0
            r2 = 1
            switch(r0) {
                case 0: goto L_0x00cf;
                case 1: goto L_0x0082;
                case 2: goto L_0x0062;
                case 3: goto L_0x002d;
                case 4: goto L_0x0009;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x0185
        L_0x0009:
            com.google.firebase.storage.network.DeleteNetworkRequest r0 = new com.google.firebase.storage.network.DeleteNetworkRequest
            java.lang.Object r3 = r7.f405a
            com.google.firebase.storage.StorageReference r3 = (com.google.firebase.storage.StorageReference) r3
            com.google.firebase.storage.internal.StorageReferenceUri r3 = r3.getStorageReferenceUri()
            java.lang.Object r4 = r7.f405a
            com.google.firebase.storage.StorageReference r4 = (com.google.firebase.storage.StorageReference) r4
            com.google.firebase.storage.FirebaseStorage r4 = r4.mFirebaseStorage
            com.google.firebase.FirebaseApp r4 = r4.mApp
            r0.<init>(r3, r4)
            java.lang.Object r3 = r7.c
            com.google.firebase.storage.internal.ExponentialBackoffSender r3 = (com.google.firebase.storage.internal.ExponentialBackoffSender) r3
            r3.sendWithExponentialBackoff(r0, r2)
            java.lang.Object r2 = r7.b
            com.google.android.gms.tasks.TaskCompletionSource r2 = (com.google.android.gms.tasks.TaskCompletionSource) r2
            r0.completeTask(r2, r1)
            return
        L_0x002d:
            java.lang.Object r0 = r7.f405a
            com.google.android.play.core.assetpacks.zzbb r0 = (com.google.android.play.core.assetpacks.zzbb) r0
            java.lang.Object r1 = r7.b
            android.os.Bundle r1 = (android.os.Bundle) r1
            java.lang.Object r2 = r7.c
            com.google.android.play.core.assetpacks.AssetPackState r2 = (com.google.android.play.core.assetpacks.AssetPackState) r2
            com.google.android.play.core.assetpacks.zzde r3 = r0.zzc
            com.yandex.metrica.c r4 = new com.yandex.metrica.c
            r4.<init>((com.google.android.play.core.assetpacks.zzde) r3, (android.os.Bundle) r1)
            java.lang.Object r1 = r3.zzr(r4)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x0061
            android.os.Handler r1 = r0.zzk
            com.android.billingclient.api.zzo r3 = new com.android.billingclient.api.zzo
            r3.<init>((com.google.android.play.core.assetpacks.zzbb) r0, (com.google.android.play.core.assetpacks.AssetPackState) r2)
            r1.post(r3)
            com.google.android.play.core.internal.zzco r0 = r0.zze
            java.lang.Object r0 = r0.zza()
            com.google.android.play.core.assetpacks.zzy r0 = (com.google.android.play.core.assetpacks.zzy) r0
            r0.zzf()
        L_0x0061:
            return
        L_0x0062:
            java.lang.Object r0 = r7.f405a
            com.android.billingclient.api.BillingClientImpl r0 = (com.android.billingclient.api.BillingClientImpl) r0
            java.lang.Object r1 = r7.b
            com.android.billingclient.api.ConsumeResponseListener r1 = (com.android.billingclient.api.ConsumeResponseListener) r1
            java.lang.Object r2 = r7.c
            com.android.billingclient.api.ConsumeParams r2 = (com.android.billingclient.api.ConsumeParams) r2
            com.artifex.source.util.a.util_a.a.b.f.c r0 = r0.zzf
            com.android.billingclient.api.BillingResult r3 = com.android.billingclient.api.zzat.zzn
            r4 = 24
            r5 = 4
            com.google.android.gms.internal.play_billing.zzfb r4 = androidx.appcompat.R$bool.zza(r4, r5, r3)
            r0.zza(r4)
            java.lang.String r0 = r2.zza
            r1.onConsumeResponse(r3, r0)
            return
        L_0x0082:
            java.lang.Object r0 = r7.b
            android.content.Context r0 = (android.content.Context) r0
            java.lang.Object r1 = r7.f405a
            byte[] r1 = (byte[]) r1
            r3 = 302(0x12e, float:4.23E-43)
            java.lang.Object r0 = com.pgl.ssdk.ces.a.meta(r3, r0, r1)
            boolean r1 = r0 instanceof java.lang.Integer
            r3 = 200(0xc8, float:2.8E-43)
            r4 = 0
            if (r1 == 0) goto L_0x00aa
            r1 = r0
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            if (r1 != 0) goto L_0x00aa
            java.lang.Object r0 = r7.c
            com.artifex.source.util.a.util_a.b.c r0 = (com.artifex.source.util.a.util_a.b.c) r0
            r0.b = r4
            r0.quit()
            goto L_0x00bb
        L_0x00aa:
            boolean r1 = r0 instanceof java.lang.String
            if (r1 == 0) goto L_0x00be
            java.lang.Object r1 = r7.c
            com.artifex.source.util.a.util_a.b.c r1 = (com.artifex.source.util.a.util_a.b.c) r1
            r1.b = r4
            r1.quit()
            java.lang.String r0 = (java.lang.String) r0
            com.artifex.source.util.a.util_a.b.a.c = r0
        L_0x00bb:
            com.artifex.source.util.a.util_a.b.a.f402a = r3
            goto L_0x00ce
        L_0x00be:
            java.lang.Object r0 = r7.c
            com.artifex.source.util.a.util_a.b.c r0 = (com.artifex.source.util.a.util_a.b.c) r0
            int r1 = r0.b
            int r1 = r1 + r2
            r0.b = r1
            android.os.Handler r0 = r0.g
            r3 = 10000(0x2710, double:4.9407E-320)
            r0.sendEmptyMessageDelayed(r2, r3)
        L_0x00ce:
            return
        L_0x00cf:
            long r3 = java.lang.System.currentTimeMillis()
            java.lang.String.valueOf(r3)
            int r0 = com.artifex.source.util.a.util_a.com.artifex.source.util.a.util_a.f393a
            java.lang.String r3 = ""
            if (r0 == 0) goto L_0x00e3
            if (r0 == r2) goto L_0x00e0
            r0 = r3
            goto L_0x00e5
        L_0x00e0:
            java.lang.String r0 = "https://ssdk-va.pangle.io"
            goto L_0x00e5
        L_0x00e3:
            java.lang.String r0 = "https://ssdk-sg.pangle.io"
        L_0x00e5:
            r4 = 301(0x12d, float:4.22E-43)
            r5 = 501(0x1f5, float:7.02E-43)
            java.lang.Object r6 = r7.b     // Catch:{ all -> 0x0173 }
            android.content.Context r6 = (android.content.Context) r6     // Catch:{ all -> 0x0173 }
            java.lang.Object r1 = com.pgl.ssdk.ces.a.meta(r4, r6, r1)     // Catch:{ all -> 0x0173 }
            byte[] r1 = (byte[]) r1     // Catch:{ all -> 0x0173 }
            if (r1 == 0) goto L_0x0170
            int r4 = r1.length     // Catch:{ all -> 0x0173 }
            if (r4 > 0) goto L_0x00f9
            goto L_0x0170
        L_0x00f9:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0173 }
            r4.<init>()     // Catch:{ all -> 0x0173 }
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            java.lang.String r0 = "/ssdk/sd/token?os=android&app_id="
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            r4.append(r3)     // Catch:{ all -> 0x0173 }
            java.lang.String r0 = com.artifex.source.util.a.util_a.b.a.b     // Catch:{ all -> 0x0173 }
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            java.lang.String r0 = "&did="
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            java.lang.String r0 = com.pgl.ssdk.ces.d.b()     // Catch:{ all -> 0x0173 }
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            java.lang.String r0 = "&app_ver="
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            java.lang.Object r0 = r7.b     // Catch:{ all -> 0x0173 }
            android.content.Context r0 = (android.content.Context) r0     // Catch:{ all -> 0x0173 }
            int r0 = com.pgl.ssdk.ces.e.a.a((android.content.Context) r0)     // Catch:{ all -> 0x0173 }
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            java.lang.String r0 = "&platform=android&ver="
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            java.lang.String r0 = "1.0.0"
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            java.lang.String r0 = "&mode=1"
            r4.append(r0)     // Catch:{ all -> 0x0173 }
            java.lang.String r0 = r4.toString()     // Catch:{ all -> 0x0173 }
            com.artifex.source.util.a.util_a.b.c r3 = new com.artifex.source.util.a.util_a.b.c     // Catch:{ all -> 0x0173 }
            java.lang.String r4 = "request"
            java.lang.Object r6 = r7.b     // Catch:{ all -> 0x0173 }
            android.content.Context r6 = (android.content.Context) r6     // Catch:{ all -> 0x0173 }
            r3.<init>(r4, r6, r0, r1)     // Catch:{ all -> 0x0173 }
            r7.c = r3     // Catch:{ all -> 0x0173 }
            r3.start()     // Catch:{ all -> 0x0173 }
            java.lang.Object r0 = r7.c     // Catch:{ all -> 0x0173 }
            com.artifex.source.util.a.util_a.b.c r0 = (com.artifex.source.util.a.util_a.b.c) r0     // Catch:{ all -> 0x0173 }
            android.os.Handler r1 = new android.os.Handler     // Catch:{ all -> 0x0173 }
            java.lang.Object r3 = r7.c     // Catch:{ all -> 0x0173 }
            com.artifex.source.util.a.util_a.b.c r3 = (com.artifex.source.util.a.util_a.b.c) r3     // Catch:{ all -> 0x0173 }
            android.os.Looper r3 = r3.getLooper()     // Catch:{ all -> 0x0173 }
            java.lang.Object r4 = r7.c     // Catch:{ all -> 0x0173 }
            com.artifex.source.util.a.util_a.b.c r4 = (com.artifex.source.util.a.util_a.b.c) r4     // Catch:{ all -> 0x0173 }
            android.os.Handler$Callback r4 = r4.h     // Catch:{ all -> 0x0173 }
            r1.<init>(r3, r4)     // Catch:{ all -> 0x0173 }
            r0.g = r1     // Catch:{ all -> 0x0173 }
            java.lang.Object r0 = r7.c     // Catch:{ all -> 0x0173 }
            com.artifex.source.util.a.util_a.b.c r0 = (com.artifex.source.util.a.util_a.b.c) r0     // Catch:{ all -> 0x0173 }
            android.os.Handler r0 = r0.g     // Catch:{ all -> 0x0173 }
            r0.sendEmptyMessage(r2)     // Catch:{ all -> 0x0173 }
            goto L_0x0175
        L_0x0170:
            com.artifex.source.util.a.util_a.b.a.f402a = r5     // Catch:{ all -> 0x0173 }
            goto L_0x0182
        L_0x0173:
            com.artifex.source.util.a.util_a.b.a.f402a = r5     // Catch:{ all -> 0x0183 }
        L_0x0175:
            java.lang.Object r0 = r7.f405a
            com.artifex.source.util.a.util_a.b.f.a r0 = (com.artifex.source.util.a.util_a.b.f.a) r0
            if (r0 == 0) goto L_0x0182
            java.lang.String r1 = com.artifex.source.util.a.util_a.b.a.a()
            r0.a(r1)
        L_0x0182:
            return
        L_0x0183:
            r0 = move-exception
            throw r0
        L_0x0185:
            java.lang.Object r0 = r7.f405a
            com.zoho.desk.conversation.database.ZDChatDaoInterface r0 = (com.zoho.desk.conversation.database.ZDChatDaoInterface) r0
            java.lang.Object r1 = r7.b
            java.util.ArrayList r1 = (java.util.ArrayList) r1
            java.lang.Object r2 = r7.c
            java.util.ArrayList r2 = (java.util.ArrayList) r2
            r0.insertMessage(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.source.util.a.util_a.b.d.run():void");
    }

    public /* synthetic */ d(BillingClientImpl billingClientImpl, ConsumeResponseListener consumeResponseListener, ConsumeParams consumeParams) {
        this.f405a = billingClientImpl;
        this.b = consumeResponseListener;
        this.c = consumeParams;
    }

    public /* synthetic */ d(zzbb zzbb, Bundle bundle, AssetPackState assetPackState) {
        this.f405a = zzbb;
        this.b = bundle;
        this.c = assetPackState;
    }

    public d(ZDChatDaoInterface zDChatDaoInterface, ArrayList arrayList, ArrayList arrayList2) {
        this.f405a = zDChatDaoInterface;
        this.b = arrayList;
        this.c = arrayList2;
    }

    public d(byte[] bArr, Context context, c cVar) {
        this.f405a = bArr;
        this.c = cVar;
        this.b = context;
    }

    public d(StorageReference storageReference, TaskCompletionSource taskCompletionSource) {
        Preconditions.checkNotNull(storageReference);
        Preconditions.checkNotNull(taskCompletionSource);
        this.f405a = storageReference;
        this.b = taskCompletionSource;
        FirebaseStorage firebaseStorage = storageReference.mFirebaseStorage;
        FirebaseApp firebaseApp = firebaseStorage.mApp;
        firebaseApp.checkNotDeleted();
        this.c = new ExponentialBackoffSender(firebaseApp.applicationContext, firebaseStorage.getAuthProvider(), firebaseStorage.getAppCheckProvider(), TTAdConstant.AD_MAX_EVENT_TIME);
    }
}
