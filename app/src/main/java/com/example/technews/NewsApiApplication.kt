package com.example.technews

import android.app.Application
import android.content.Context
import com.example.technews.utils.LocalizationUtil
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NewsApiApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocalizationUtil.setLocale(base))
    }
}