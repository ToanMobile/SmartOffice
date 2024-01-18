package com.artifex.sonui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.security.Security

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Security.insertProviderAt(org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }
}
