package com.example.triviaapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class TriviaPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        const val RECORD_KEY = "record" // Key for the record
        val record_key = intPreferencesKey(RECORD_KEY) // Key for the record value
        const val TAG = "TriviaPreferencesRepository" // Tag for logging
    }

    val recordFlow: Flow<Int> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[record_key] ?: 0
        }

    suspend fun writeTriviaPreferences(record: Int) {
        dataStore.edit { preferences ->
            preferences[record_key] = record
        }
    }
}