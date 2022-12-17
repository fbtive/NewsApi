package com.example.technews.newsactivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.technews.utils.LocalizationUtil

open class BaseActivity: AppCompatActivity() {

    val currentLocale: String by lazy {
        LocalizationUtil.getLanguage(applicationContext)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalizationUtil.setLocale(newBase))
    }

    override fun onResume() {
        val checkLocale = LocalizationUtil.getLanguage(applicationContext)
        if(currentLocale != checkLocale){
            recreate()
        }
        super.onResume()
    }
}