package com.example.technews.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.preference.PreferenceManager
import java.util.*


abstract class LocalizationUtil {
    companion object {
        private const val SELECTED_LANGUGAGE = "Locale.Util.Selected.Language"
        const val ENGLISH = "en"
        const val INDONESIA = "in"

        fun setLocale(context: Context): Context {
            return updateResources(context, getLanguage(context))
        }

        fun setNewLocale(context: Context, language: String): Context {
            persist(context, language)

            return updateResources(context, language)
        }

        private fun persist(context: Context, language: String) {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString(SELECTED_LANGUGAGE, language)
            editor.commit()
        }

        fun getLanguage(context: Context): String {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//            val saved = preferences.getString(SELECTED_LANGUGAGE, ENGLISH)
            Log.i("preferences", "langugage: " + preferences.getString(SELECTED_LANGUGAGE, ENGLISH))
            return preferences.getString(SELECTED_LANGUGAGE, ENGLISH) ?: ENGLISH
        }

        private fun updateResources(context: Context, language: String): Context {
            val language = getLanguage(context)

            val locale: Locale = Locale(language)
            Locale.setDefault(locale)
            val resources = context.resources
            val config = resources.configuration
            config.setLocale(locale)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.createConfigurationContext(config)
            }else {
                @Suppress("deprecation")
                resources.updateConfiguration(config, resources.displayMetrics)
            }

            return context
        }
    }
}