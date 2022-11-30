package me.benhunter.accelerate.ui.task

import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private var taskId: String = ""

    fun setTask(taskId: String) {
        this.taskId = taskId
    }
}