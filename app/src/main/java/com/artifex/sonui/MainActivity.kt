package com.artifex.sonui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.artifex.solib.ArDkLib
import com.artifex.solib.ConfigOptions
import com.artifex.sonui.editor.NUIActivity
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.UriUtils
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pathWelcome = PathUtils.getExternalDownloadsPath() + File.separator + "welcome.pdf"
        val defaultUI = Intent(this, NUIActivity::class.java).apply {
            this.action = Intent.ACTION_VIEW
            this.data = UriUtils.file2Uri(FileUtils.getFileByPath(pathWelcome))
        }
        startActivity(defaultUI)
//        val appCfgOpts = Configuration()
//        NUIActivity().checkUIMode(appCfgOpts)
    }
}
