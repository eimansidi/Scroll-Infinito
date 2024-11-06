package com.example.scroll_infinito

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * TaskViewHolder es un ViewHolder personalizado para mostrar tareas individuales
 * en el RecyclerView. Vincula una tarea y maneja el evento de completado.
 *
 * @param view La vista del elemento de tarea en el RecyclerView.
 */
class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvTask: TextView = view.findViewById(R.id.tvTask) // TextView para mostrar la tarea
    private val ivTaskDone: ImageView = view.findViewById(R.id.ivTaskDone) // ImageView para marcar como completada

    /**
     * Asigna el texto de la tarea a la vista y configura el listener para
     * marcar la tarea como completada cuando se hace clic en el icono.
     *
     * @param task La tarea a mostrar en el TextView.
     * @param onItemDone Callback que se llama cuando se marca la tarea como completada.
     */
    fun render(task: String, onItemDone: (Int) -> Unit) {
        tvTask.text = task // Asigna el texto de la tarea al TextView

        ivTaskDone.setOnClickListener {
            onItemDone(adapterPosition) // Llama al callback con la posición del elemento
        }
    }
}
