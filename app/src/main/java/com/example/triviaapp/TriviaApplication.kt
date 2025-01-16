package com.example.triviaapp

import android.app.Application
import com.example.triviaapp.data.AppContainer
import com.example.triviaapp.data.DefaultAppcontainer

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

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppcontainer()
    }
}