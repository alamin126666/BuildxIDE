package com.buildxide

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BuildxApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Application initialization
    }
}
