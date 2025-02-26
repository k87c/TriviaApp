package com.example.triviaapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.triviaapp.data.AppContainer
import com.example.triviaapp.data.DefaultAppcontainer
import com.example.triviaapp.data.GameRepository
import com.example.triviaapp.data.TriviaPreferencesRepository

/**
 * Application class for the Trivia app
 *
 * Initializes the AppContainer
 *
 * Esta clase es la encargada de inicializar el contenedor de dependencias
 *
 * @see AppContainer
 *
 * OJO: No olvides declarar esta clase en el AndroidManifest.xml para ello agrega la siguiente linea:
 *
 * <application
 *    android:name=".TriviaApplication"
 *    ...
 *    >
 *    ...
 *    </application>
 *
 *    Esta linea debe ir dentro del tag <application> en el archivo AndroidManifest.xml
 */

class TriviaApplication : Application() {
    lateinit var container: AppContainer
    lateinit var triviaPreferencesRepository: TriviaPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        triviaPreferencesRepository = TriviaPreferencesRepository(dataStore)
        container = DefaultAppcontainer(this)
    }
}

private const val RECORD_PREFERENCES_NAME = "record_preferences"

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = RECORD_PREFERENCES_NAME
)