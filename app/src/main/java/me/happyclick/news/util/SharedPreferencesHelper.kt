package me.happyclick.news.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class SharedPreferencesHelper {

    companion object {

        private const val PREF_TIME = PREF_TIME_KEY
        private const val PREF_TIME_SOURCE = PREF_TIME_KEY

        private const val PREF_SOURCE = KEY_SOURCES

        private var prefs: SharedPreferences? = null

        @Volatile private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper = instance
            ?: synchronized(LOCK) {
            instance
                ?: buildHelper(
                    context
                ).also {
                instance = it
            }
        }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }
    }

    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) {
            putLong(PREF_TIME, time)
        }
    }

    fun getUpdateTime() = prefs?.getLong(
        PREF_TIME, 0)

    fun saveUpdateTimeSources(time: Long) {
        prefs?.edit(commit = true) {
            putLong(PREF_TIME_SOURCE, time)
        }
    }

    fun getUpdateTimeSources() = prefs?.getLong(
        PREF_TIME_SOURCE, 0)

    fun saveSource(source: String) {
        prefs?.edit(commit = true) {
            putString(PREF_SOURCE, source)
        }
    }

    fun saveSource(source: String, sourceName: String) {
        prefs?.edit(commit = true) {
            putString(PREF_SOURCE, source)
            putString(PREF_SOURCES_NAME_KEY, sourceName)
        }
    }

    fun getSource() = prefs?.getString(
        PREF_SOURCE, SOURCE_ID)

    fun getSourceName() = prefs?.getString(
        PREF_SOURCES_NAME_KEY, SOURCE_NAME)
}