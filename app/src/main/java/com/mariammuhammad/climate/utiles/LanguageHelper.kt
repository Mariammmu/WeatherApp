package com.mariammuhammad.climate.utiles

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.Locale

object LanguageUtils {
    const val ENGLISH = Constants.LANGUAGE_EN
    const val ARABIC = Constants.LANGUAGE_AR

    fun changeLanguage(activity: Activity, languageCode: String) {
        val locale = when (languageCode) {
            ENGLISH -> Locale(ENGLISH)
            ARABIC -> Locale(ARABIC)
            else -> Locale.getDefault()
        }

        Locale.setDefault(locale)

        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setLocaleForApi33(activity, languageCode)
        } else {
            activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
        }

        activity.recreate()
    }

    //(Android 13+)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setLocaleForApi33(activity: Activity, languageCode: String) {
        val locale = Locale.forLanguageTag(languageCode)
        val localeManager = activity.getSystemService(Context.LOCALE_SERVICE) as LocaleManager
        localeManager.applicationLocales = LocaleList(locale)
    }
}