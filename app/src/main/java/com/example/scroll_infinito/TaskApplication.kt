package com.example.scroll_infinito

import android.app.Application

/**
 * TaskApplication es la clase de aplicación principal que inicializa componentes
 * globales para la aplicación, como las preferencias compartidas.
 */
class TaskApplication : Application() {

    companion object {
        // Variable para almacenar una instancia de Preferences de manera global en la app
        lateinit var prefs: Preferences
    }

    /**
     * Método llamado al crear la aplicación.
     * Inicializa el objeto de preferencias compartidas para el acceso global.
     */
    override fun onCreate() {
        super.onCreate()
        // Inicializa el objeto Preferences con el contexto de la aplicación
        prefs = Preferences(baseContext)
    }
}
