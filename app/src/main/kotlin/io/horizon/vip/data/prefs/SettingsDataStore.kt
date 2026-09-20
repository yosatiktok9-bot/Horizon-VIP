package io.horizon.vip.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("horizon_settings")

class SettingsDataStore(private val context: Context) {
    private object Keys {
        val accent = stringPreferencesKey("accent")
        val scanLine = booleanPreferencesKey("scan_line")
        val reduceMotion = booleanPreferencesKey("reduce_motion")
        val concurrency = intPreferencesKey("concurrency")
        val timeout = floatPreferencesKey("timeout")
        val passive = booleanPreferencesKey("passive")
        val cppHotPath = booleanPreferencesKey("cpp_hot_path")
    }

    val accent: Flow<String> = context.dataStore.data.map { it[Keys.accent] ?: "neon_mint" }
    val scanLineEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.scanLine] ?: true }
    val reduceMotion: Flow<Boolean> = context.dataStore.data.map { it[Keys.reduceMotion] ?: false }
    val concurrency: Flow<Int> = context.dataStore.data.map { it[Keys.concurrency] ?: 64 }
    val timeout: Flow<Float> = context.dataStore.data.map { it[Keys.timeout] ?: 2.0f }
    val passive: Flow<Boolean> = context.dataStore.data.map { it[Keys.passive] ?: true }
    val cppHotPath: Flow<Boolean> = context.dataStore.data.map { it[Keys.cppHotPath] ?: true }

    suspend fun setAccent(value: String) {
        context.dataStore.edit { it[Keys.accent] = value }
    }

    suspend fun setScanLine(value: Boolean) {
        context.dataStore.edit { it[Keys.scanLine] = value }
    }

    suspend fun setReduceMotion(value: Boolean) {
        context.dataStore.edit { it[Keys.reduceMotion] = value }
    }

    suspend fun setConcurrency(value: Int) {
        context.dataStore.edit { it[Keys.concurrency] = value }
    }

    suspend fun setTimeout(value: Float) {
        context.dataStore.edit { it[Keys.timeout] = value }
    }

    suspend fun setPassive(value: Boolean) {
        context.dataStore.edit { it[Keys.passive] = value }
    }

    suspend fun setCppHotPath(value: Boolean) {
        context.dataStore.edit { it[Keys.cppHotPath] = value }
    }
}
