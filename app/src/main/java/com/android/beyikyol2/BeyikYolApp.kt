package com.android.beyikyol2

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.android.beyikyol2.core.util.Locales
import com.android.beyikyol2.core.util.Utils
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BeyikYolApp: Application(){

    val isDark = mutableStateOf(false)

    fun toggleMode(context: Context){
        Log.e("Dark","${isDark.value}")
        if(!isDark.value){
            Utils.setPreference("mode","dark",context)
        } else {
            Utils.setPreference("mode","light",context)
        }
        isDark.value=!isDark.value
    }

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this, Locales.TM)
    }
}