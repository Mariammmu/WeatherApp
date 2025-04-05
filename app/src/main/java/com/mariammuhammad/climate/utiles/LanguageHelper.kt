package com.mariammuhammad.climate.utiles

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.mariammuhammad.climate.settings.SettingsPrefs
import java.text.NumberFormat
import java.util.Locale

class LanguageHelper(private val settingsPrefs: SettingsPrefs) {

    companion object {
        const val ENGLISH = "en"
        const val ARABIC = "ar"
        const val SYSTEM_DEFAULT = "system"

        // For displaying in your settings UI
        fun getLanguageDisplayName(code: String): String {
            return when (code) {
                ARABIC -> "Arabic"
                ENGLISH -> "English"
                else -> "System Default"
            }
        }
    }

    fun setAppLocale(activity: Activity, languageCode: String) {

        settingsPrefs.saveLanguage(languageCode)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                setLocaleForApi33(activity, languageCode)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
                setLocaleWithAppCompat(languageCode)
            }
            else -> {
                setLegacyLocale(activity, languageCode)
            }
        }

        activity.recreate()
    }

    fun getCurrentLanguage(): String {
        return settingsPrefs.getLanguage()
    }

    fun isArabic(): Boolean {
        return getCurrentLanguage() == ARABIC ||
                (getCurrentLanguage() == SYSTEM_DEFAULT && Locale.getDefault().language == ARABIC)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setLocaleForApi33(activity: Activity, languageCode: String) {
        if (languageCode == SYSTEM_DEFAULT) {
            val localeManager = activity.getSystemService(Context.LOCALE_SERVICE) as LocaleManager
            localeManager.applicationLocales = LocaleList.getEmptyLocaleList()
            return
        }

        val locale = Locale.forLanguageTag(languageCode)
        val localeManager = activity.getSystemService(Context.LOCALE_SERVICE) as LocaleManager
        localeManager.applicationLocales = LocaleList(locale)
    }

    private fun setLocaleWithAppCompat(languageCode: String) {
        if (languageCode == SYSTEM_DEFAULT) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
            return
        }

        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }

    @Suppress("DEPRECATION")
    private fun setLegacyLocale(activity: Activity, languageCode: String) {
        val locale = when (languageCode) {
            ARABIC -> Locale(ARABIC)
            ENGLISH -> Locale.ENGLISH
            else -> Locale.getDefault()
        }

        Locale.setDefault(locale)
        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)
        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
    }
}