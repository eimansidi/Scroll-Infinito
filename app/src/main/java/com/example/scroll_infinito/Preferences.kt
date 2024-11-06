package com.example.scroll_infinito

import android.content.Context
import android.content.SharedPreferences

/**
 * Clase Preferences para gestionar el almacenamiento y recuperación de tareas
 * utilizando SharedPreferences.
 *
 * @param context contexto de la aplicación para acceder a SharedPreferences.
 */
class Preferences(context: Context) {

    companion object {
        // Nombre de la base de datos de preferencias
        const val PREFS_NAME = "myDatabase"
        // Clave para el conjunto de tareas almacenadas
        const val TASKS = "tasks_value"
    }

    // SharedPreferences para almacenar datos de usuario
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    /**
     * Guarda la lista de tareas en las preferencias.
     *
     * @param tasks lista de tareas que se guardará en las preferencias.
     */
    fun saveTasks(tasks: List<String>) {
        prefs.edit().putStringSet(TASKS, tasks.toSet()).apply() // Almacena las tareas como un Set
    }

    /**
     * Recupera la lista de tareas almacenadas en las preferencias.
     *
     * @return lista mutable de tareas.
     */
    fun getTasks(): MutableList<String> {
        return prefs.getStringSet(TASKS, emptySet<String>())?.toMutableList() ?: mutableListOf() // Obtiene las tareas como una lista mutable
    }
}
