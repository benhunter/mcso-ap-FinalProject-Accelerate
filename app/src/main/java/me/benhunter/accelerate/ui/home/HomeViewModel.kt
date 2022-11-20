package me.benhunter.accelerate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.benhunter.accelerate.Task
import me.benhunter.accelerate.TaskList

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val board = List(5) { boardNumber ->
        val tasks = List(5) { taskNumber -> Task("task $taskNumber") }
        TaskList("category $boardNumber", tasks)
    }
}