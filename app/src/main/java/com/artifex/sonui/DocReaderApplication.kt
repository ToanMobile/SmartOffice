package com.artifex.sonui

import androidx.multidex.MultiDexApplication
import com.artifex.solib.ArDkLib
import com.artifex.solib.ConfigOptions
import com.artifex.sonui.editor.NUIDefaultSignerFactory
import com.artifex.sonui.editor.Utilities


class DocReaderApplication : MultiDexApplication() {
    private var mAppCfgOptions: ConfigOptions? = null

    override fun onCreate() {
        super.onCreate()
        //System.loadLibrary("jniPdfium")
        if (mAppCfgOptions == null) {
            val configOptions = ConfigOptions()
            mAppCfgOptions = configOptions
            ArDkLib.mAppConfigOptions = configOptions
        }
        val instance = NUIDefaultSignerFactory.getInstance()
        if (instance != null) {
            Utilities.setSigningFactoryListener(instance)
        }
    }
}