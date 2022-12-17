package com.example.technews.newssettings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.technews.R
import com.example.technews.utils.LocalizationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {

    private val prefLang: String = LocalizationUtil.getLanguage(context)

    private val langOptions: Array<String>

    private val _langSelection = MutableLiveData<Int>()
    val langSelection: LiveData<Int>
        get() = _langSelection


    init {
        langOptions = context.resources.getStringArray(R.array.language_array)

        _langSelection.value = when(prefLang){
            LocalizationUtil.INDONESIA -> 0
            LocalizationUtil.ENGLISH -> 1
            else -> 0
        }
    }

    fun setLangSelection(position: Int): Boolean {
        _langSelection.value = position
        val language = when(position) {
            0 -> LocalizationUtil.INDONESIA
            1 -> LocalizationUtil.ENGLISH
            else -> LocalizationUtil.INDONESIA
        }

        if(prefLang == language){
            return false
        }else {
            LocalizationUtil.setNewLocale(context, language)
            return true
        }
    }
}