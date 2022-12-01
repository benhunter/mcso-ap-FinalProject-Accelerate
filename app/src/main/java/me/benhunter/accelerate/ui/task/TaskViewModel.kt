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
                val taskFromFirestore = it.toObject(Task::class.java)
                task.postValue(taskFromFirestore)
            }
    }

    fun observeTask(): LiveData<Task?> {
        return task
    }

    fun saveName(name: String) {
        task.value?.let {
            val updatedTask = Task(name, it.firestoreId, it.categoryId, it.position)
            db.collection(taskCollection).document(taskId).set(updatedTask)
                .addOnSuccessListener {
                    task.postValue(updatedTask)
                }
        }
    }

    fun delete() {
        db.collection(taskCollection).document(taskId).delete()
            .addOnSuccessListener {
                task.postValue(null)

            }
    }
}