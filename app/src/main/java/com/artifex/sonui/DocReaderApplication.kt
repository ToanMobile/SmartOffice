package com.artifex.sonui

import androidx.multidex.MultiDexApplication
import com.artifex.solib.ArDkLib
import com.artifex.solib.ConfigOptions


class DocReaderApplication : MultiDexApplication() {
    private var mAppCfgOptions: ConfigOptions? = null

    override fun onCreate() {
        super.onCreate()
        if (mAppCfgOptions == null) {
            val configOptions = ConfigOptions()
            mAppCfgOptions = configOptions
            ArDkLib.mAppConfigOptions = configOptions
        }
    }
}