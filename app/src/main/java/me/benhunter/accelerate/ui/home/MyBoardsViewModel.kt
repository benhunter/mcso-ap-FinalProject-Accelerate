package me.benhunter.accelerate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.benhunter.accelerate.model.Board
import me.benhunter.accelerate.model.Task
import me.benhunter.accelerate.model.Category

class MyBoardsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val board = generateBoard() // TODO remove!

    // TODO use in My Boards Fragment!
    val boards = List(4) {boardNumber ->
        Board("Board $boardNumber", generateBoard())
    }

    private fun generateBoard(): List<Category> {
        return List(5) { categoryNumber ->
            val tasks = List(5) { taskNumber -> Task("task $taskNumber") }
            Category("category $categoryNumber", tasks)
        }
    }
}