package com.example.karsanusa.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")

class ThemePreference private constructor(private val themeDataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey(THEME_KEY)

    fun getThemeSetting(): Flow<Boolean> {
        return themeDataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        themeDataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }

    companion object {

        private const val THEME_KEY = "theme_setting"

        @Volatile
        private var INSTANCE: ThemePreference? = null

        fun getInstance(
            themeDataStore: DataStore<Preferences>
        ): ThemePreference {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemePreference(themeDataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}