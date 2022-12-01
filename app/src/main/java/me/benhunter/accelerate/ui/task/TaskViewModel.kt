package me.benhunter.accelerate.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import me.benhunter.accelerate.model.Task

class TaskViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val taskCollection = "tasks"

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var taskId: String = ""
    private val task = MutableLiveData<Task?>()

    fun setTask(taskId: String) {
        this.taskId = taskId

        db.collection(taskCollection).document(taskId).get()
            .addOnSuccessListener {
                val task = it.toObject(Task::class.java)
                this.task.postValue(task)
            }
    }

    fun observeTask(): LiveData<Task?> {
        return task
    }

    fun saveNameAndCategory(name: String, categoryId: String) {
        task.value?.let {
            val updatedTask = Task(name, it.firestoreId, categoryId, it.position)
            db.collection(taskCollection).document(taskId).set(updatedTask)
                .addOnSuccessListener {
                    task.postValue(updatedTask)
                }
        }
    }

    fun delete() {
        val position = task.value?.position ?: 0

        db.collection(taskCollection).document(taskId).delete()
            .addOnSuccessListener {
                task.postValue(null)
            }

        // Update position for other tasks
        db.collection(taskCollection).get().addOnSuccessListener { querySnapshot ->
            val batch = db.batch()
            querySnapshot.forEach {
                val task = it.toObject(Task::class.java)
                if (task.position > position) {
                    // decrement and update
                    task.position -= 1
                    batch.set(it.reference, task)
                }
            }
            batch.commit()
        }
    }
}