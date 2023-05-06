package com.android.beyikyol2.core.util

import android.content.Context
import android.content.res.Configuration
import com.yariksoffice.lingver.Lingver
import java.util.*

object LocaleUtils {
    fun setLocale(c: Context, language: String?) = updateResources(c, language ?: Locales.TM) //use locale codes

    private fun updateResources(context: Context, language: String) {
        Lingver.getInstance().setLocale(context, language)
        context.resources.apply {
//            val locale = Locale(language)
//            val configuration = Configuration(configuration)
//            configuration.setLocale(locale)
//            context.createConfigurationContext(configuration)
//            val config = Configuration(configuration)
//
//            context.createConfigurationContext(configuration)
//            Locale.setDefault(locale)
//            config.setLocale(locale)
//            context.resources.updateConfiguration(config, displayMetrics)
        }
    }


}

object Locales {
    val TM: String = "tm"
    val RU: String = "ru"
}