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

    lateinit var btnAddTask:Button
    lateinit var etTask:EditText
    lateinit var rvTasks:RecyclerView

    lateinit var mediaPlayer: MediaPlayer // Declarar el MediaPlayer para reproducir sonidos

    lateinit var adapter:TaskAdapter

    var tasks = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
        attachSwipeToDelete()
        mediaPlayer = MediaPlayer.create(this, R.raw.delete_sound)
    }

    private fun initUi() {
        initView()
        initListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        tasks = prefs.getTasks()
        rvTasks.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(tasks) { deleteTask(it) }
        rvTasks.adapter = adapter
    }

    private fun deleteTask(position:Int) {
        tasks.removeAt(position)
        adapter.notifyDataSetChanged()
        prefs.saveTasks(tasks)
        mediaPlayer.start()
    }

    private fun initListeners() {
        btnAddTask.setOnClickListener{
            addTask()
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
     * Muestra un error y reproduce un sonido si el campo está vacío.
     * Si se añade exitosamente, limpia el campo de texto y reproduce un sonido de confirmación.
     */
    private fun addTask() {
        val newTask = etTask.text.toString()
        if (newTask.isEmpty()) {
            etTask.error = "Escribe una tarea!"
        } else {
            tasks.add(newTask)
            prefs.saveTasks(tasks)
            adapter.notifyDataSetChanged()
            etTask.setText("")
        }
    }

    private fun initView() {
        btnAddTask = findViewById(R.id.btnAddTask)
        etTask = findViewById(R.id.etTask)
        rvTasks = findViewById(R.id.rvTasks)
    }

    private fun attachSwipeToDelete() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            /**
             * Metodo que maneja el movimiento de un elemento dentro del RecyclerView.
             * En este caso, no se utiliza, así que devuelve false.
             *
             * @param recyclerView El RecyclerView donde ocurre el movimiento.
             * @param viewHolder El ViewHolder del elemento que se está moviendo.
             * @param target El ViewHolder del elemento objetivo donde se está moviendo.
             * @return false porque no se usa el movimiento.
             */
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No se utiliza el movimiento vertical
            }

            /**
             * Metodo que se llama para dibujar el ítem mientras se desliza.
             * Cambia el color del fondo del ítem a rojo mientras se desliza.
             *
             * @param c Canvas donde se dibuja el RecyclerView.
             * @param recyclerView El RecyclerView.
             * @param viewHolder El ViewHolder del ítem que se desliza.
             * @param dX Desplazamiento en X durante el deslizamiento.
             * @param dY Desplazamiento en Y durante el deslizamiento.
             * @param actionState Estado actual del gesto (deslizar, arrastrar, etc.).
             * @param isCurrentlyActive Indica si el gesto está activo.
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
                    val itemView = viewHolder.itemView // Obtiene la vista del ítem

                    // Cambia el color del fondo del ítem a rojo mientras se desliza
                    itemView.setBackgroundColor(Color.RED)

                    // Aplica la traducción en X para el desplazamiento
                    itemView.translationX = dX
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            /**
             * Metodo que se llama cuando un ítem ha sido deslizado.
             * Llama a la función de eliminación después del deslizamiento.
             *
             * @param viewHolder El ViewHolder que fue deslizado.
             * @param direction La dirección en la que se deslizó (izquierda o derecha).
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition // Obtiene la posición del ítem deslizado
                deleteTask(position) // Llama a la función de eliminación
            }

            /**
             * Metodo que se llama para limpiar la vista del ítem después de que se ha deslizado.
             * Restablece el color de fondo al original.
             *
             * @param recyclerView El RecyclerView donde se encuentra el ítem.
             * @param viewHolder El ViewHolder del ítem que fue deslizado.
             */
            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                // Restablece el color de fondo al original después de que el usuario deja de deslizar
                viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }

        // Asocia el ItemTouchHelper al RecyclerView
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rvTasks)
    }

    /**
     * Se llama cuando la actividad se destruye.
     * Libera los recursos utilizados por el MediaPlayer.
     */
    override fun onDestroy() {
        super.onDestroy()
        // Liberar el MediaPlayer cuando la actividad se destruya
        mediaPlayer.release()
    }

}