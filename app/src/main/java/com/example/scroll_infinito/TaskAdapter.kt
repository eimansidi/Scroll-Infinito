package com.example.scroll_infinito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView

/**
 * TaskAdapter es un adaptador de RecyclerView para mostrar una lista de tareas.
 * Maneja la creación, enlace y animación de los elementos de tarea en la lista.
 *
 * @property tasks Lista de tareas a mostrar.
 * @property onItemDone Función de callback que se activa cuando se marca una tarea como completada.
 */
class TaskAdapter(
    private val tasks: List<String>,
    private val onItemDone: (Int) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {

    /**
     * Crea y retorna un TaskViewHolder para contener la vista de un elemento de tarea.
     *
     * @param parent El ViewGroup padre en el cual se añadirá la nueva vista.
     * @param viewType El tipo de vista del nuevo elemento.
     * @return Un nuevo TaskViewHolder que contiene la vista de un elemento de tarea.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Infla el diseño para la vista de un elemento de tarea
        val layoutInflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
    }

    /**
     * Retorna el número total de elementos de tarea en la lista.
     *
     * @return El número de tareas en la lista.
     */
    override fun getItemCount() = tasks.size

    /**
     * Vincula datos al TaskViewHolder para una posición específica.
     *
     * @param holder El ViewHolder que debe actualizarse para representar la tarea.
     * @param position La posición de la tarea dentro de la lista.
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Renderiza el elemento de tarea y establece el callback onItemDone para marcar la tarea como completada
        holder.render(tasks[position], onItemDone)

        // Aplica una animación de desvanecimiento a la vista del elemento
        setFadeAnimation(holder.itemView)
    }

    /**
     * Aplica un efecto de animación de desvanecimiento a la vista proporcionada.
     *
     * @param view La vista a la que se le aplicará la animación.
     */
    private fun setFadeAnimation(view: View) {
        // Inicializa y configura una animación de desvanecimiento
        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = 500 // Duración de la animación en milisegundos
        view.startAnimation(fadeIn) // Inicia la animación en la vista
    }
}
