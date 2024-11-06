package com.example.scroll_infinito

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scroll_infinito.TaskApplication.Companion.prefs

class MainActivity : AppCompatActivity() {

    lateinit var btnAddTask: Button
    lateinit var etTask: EditText
    lateinit var rvTasks: RecyclerView

    lateinit var mediaPlayer: MediaPlayer // Declarar el MediaPlayer para reproducir sonidos

    lateinit var adapter: TaskAdapter

    var tasks = mutableListOf<String>()

    /**
     * Método llamado al crear la actividad.
     * Inicializa la interfaz de usuario y el MediaPlayer para los sonidos.
     *
     * @param savedInstanceState El estado de la instancia guardada.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi() // Inicializa la UI
        attachSwipeToDelete() // Configura el swipe para eliminar
        mediaPlayer = MediaPlayer.create(this, R.raw.delete_sound) // Configura el sonido
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     * Llama a las funciones para inicializar vistas, listeners y RecyclerView.
     */
    private fun initUi() {
        initView() // Inicializa las vistas
        initListeners() // Inicializa los listeners
        initRecyclerView() // Configura el RecyclerView
    }

    /**
     * Configura el RecyclerView con el adaptador y la lista de tareas guardadas.
     */
    private fun initRecyclerView() {
        tasks = prefs.getTasks() // Obtiene las tareas guardadas
        rvTasks.layoutManager = LinearLayoutManager(this) // Establece el layout
        adapter = TaskAdapter(tasks) { deleteTask(it) } // Inicializa el adaptador con callback
        rvTasks.adapter = adapter // Asigna el adaptador al RecyclerView
    }

    /**
     * Elimina una tarea de la lista en la posición especificada.
     * Guarda la lista de tareas y reproduce un sonido de eliminación.
     *
     * @param position La posición de la tarea a eliminar.
     */
    private fun deleteTask(position: Int) {
        tasks.removeAt(position) // Elimina la tarea en la posición dada
        adapter.notifyDataSetChanged() // Notifica el cambio al adaptador
        prefs.saveTasks(tasks) // Guarda la lista actualizada
        mediaPlayer.start() // Reproduce el sonido de eliminación
    }

    /**
     * Inicializa los listeners de la interfaz de usuario.
     * Configura el botón de añadir tarea y su animación al hacer clic.
     */
    private fun initListeners() {
        btnAddTask.setOnClickListener {
            addTask() // Llama a la función para añadir tarea

            // Crea animación de "pulso" en el botón de añadir tarea
            val pulse = ObjectAnimator.ofPropertyValuesHolder(
                btnAddTask,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f)
            )
            pulse.duration = 150
            pulse.repeatCount = 1
            pulse.repeatMode = ObjectAnimator.REVERSE
            pulse.start()
        }
    }

    /**
     * Añade una nueva tarea a la lista si el campo de texto no está vacío.
     * Muestra un error si el campo está vacío y limpia el campo tras añadir la tarea.
     */
    private fun addTask() {
        val newTask = etTask.text.toString()
        if (newTask.isEmpty()) {
            etTask.error = "Escribe una tarea!" // Muestra un mensaje de error
        } else {
            tasks.add(newTask) // Añade la nueva tarea a la lista
            prefs.saveTasks(tasks) // Guarda la lista actualizada
            adapter.notifyDataSetChanged() // Actualiza el adaptador
            etTask.setText("") // Limpia el campo de texto
        }
    }

    /**
     * Asocia las vistas de la interfaz a las variables.
     */
    private fun initView() {
        btnAddTask = findViewById(R.id.btnAddTask)
        etTask = findViewById(R.id.etTask)
        rvTasks = findViewById(R.id.rvTasks)
    }

    /**
     * Configura el gesto de deslizar para eliminar elementos del RecyclerView.
     * Aplica un fondo rojo durante el deslizamiento y elimina la tarea al finalizar.
     */
    private fun attachSwipeToDelete() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            /**
             * Método que maneja el movimiento de un elemento dentro del RecyclerView.
             * En este caso, no se utiliza, así que devuelve false.
             */
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No se utiliza el movimiento vertical
            }

            /**
             * Cambia el color de fondo del ítem a rojo mientras se desliza.
             */
            override fun onChildDraw(
                c: android.graphics.Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    itemView.setBackgroundColor(Color.RED) // Fondo rojo al deslizar
                    itemView.translationX = dX // Aplica el desplazamiento en X
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            /**
             * Método que se llama cuando un ítem ha sido deslizado.
             * Elimina la tarea en la posición del ViewHolder deslizado.
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition // Obtiene la posición
                deleteTask(position) // Elimina la tarea
            }

            /**
             * Restaura el color de fondo original del ítem tras finalizar el deslizamiento.
             */
            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT) // Restablece el color original
            }
        }

        // Asocia el ItemTouchHelper al RecyclerView
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rvTasks)
    }

    /**
     * Método llamado al destruir la actividad.
     * Libera los recursos utilizados por el MediaPlayer.
     */
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Libera el MediaPlayer
    }
}
