package com.artifex.sonui

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.artifex.solib.ArDkLib
import com.artifex.sonui.editor.DocumentView
import com.artifex.sonui.editor.NUIActivity
import com.artifex.sonui.editor.SODocumentView
import com.artifex.sonui.editor.Utilities
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.UriUtils
import java.io.File

class MainActivity : AppCompatActivity() {
    private var mDocumentView: SODocumentView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Default UI Demo
        val pathWelcome = PathUtils.getExternalDownloadsPath() + File.separator + "welcome.pdf"
        val documentUri = UriUtils.file2Uri(FileUtils.getFileByPath(pathWelcome))
        val defaultUI = Intent(this, NUIActivity::class.java).apply {
            this.action = Intent.ACTION_VIEW
            this.data = documentUri
        }
        //startActivity(defaultUI)
        //Custom UI
        DocumentView.initialize(this)
        val filename = FileUtils.getFileName(pathWelcome)
        val documentView: DocumentView = DocumentView(this)
        mDocumentView = documentView.mDocView
        val parent = findViewById<ViewGroup>(R.id.documentViewHolder)
        parent.addView(
            documentView,
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        documentView.setDocConfigOptions(ArDkLib.getAppConfigOptions())
        documentView.setDocDataLeakHandler(Utilities.getDataLeakHandlers())
        documentView.start(documentUri, 0, false)
    }
}
