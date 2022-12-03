package me.benhunter.accelerate.task

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

    fun saveTask(name: String, categoryId: String, dueDate: String, dueTime: String) {
        task.value?.let { thisTask ->

            val isCategoryChanged = thisTask.categoryId != categoryId
            var position = thisTask.position

            if (isCategoryChanged) {
                // update position and other positions
                position = 0

                // every task in new category, position++
                db.collection(taskCollection).whereEqualTo("categoryId", categoryId).get()
                    .addOnSuccessListener { querySnapshot ->
                        val batch = db.batch()
                        querySnapshot.forEach {
                            val task = it.toObject(Task::class.java)
                            // increment and update
                            task.position += 1
                            batch.set(it.reference, task)
                        }
                        batch.commit()
                    }
            }

            val updatedTask = Task(name, thisTask.firestoreId, categoryId, position, dueDate, dueTime)

            if (thisTask == updatedTask) return

            db.collection(taskCollection).document(taskId).set(updatedTask)
                .addOnSuccessListener {
                    task.postValue(updatedTask)
                }
        }
    }

    fun delete() {
        val currentTask = task.value ?: return

        db.collection(taskCollection).document(taskId).delete()
            .addOnSuccessListener {
                task.postValue(null)
            }

        // Update position for other tasks
        db.collection(taskCollection).whereEqualTo("categoryId", currentTask.categoryId).get().addOnSuccessListener { querySnapshot ->
            val batch = db.batch()
            querySnapshot.forEach {
                val task = it.toObject(Task::class.java)
                if (task.position > currentTask.position) {
                    // decrement and update
                    task.position -= 1
                    batch.set(it.reference, task)
                }
            }
            batch.commit()
        }
    }
}