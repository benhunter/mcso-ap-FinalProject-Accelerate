package me.benhunter.accelerate.ui.myboards

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import me.benhunter.accelerate.model.Board
import me.benhunter.accelerate.model.Category
import me.benhunter.accelerate.model.Task

class MyBoardsViewModel : ViewModel() {

    private val TAG = javaClass.simpleName
    private val firestoreCollection = "myBoards"

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val board = generateBoard() // TODO replace with database!

    // TODO use in My Boards Fragment!
    val boards = List(4) { boardNumber ->
        Board("Board $boardNumber", generateBoard())
    }

    private fun generateBoard(): List<Category> {
        return List(5) { categoryNumber ->
            val tasks = List(5) { taskNumber -> Task("task $taskNumber") }
            Category("category $categoryNumber", tasks)
        }
    }

    fun getBoardById(boardId: String): Board? {
        return boards.find { it.name == boardId }
    }

    fun createBoard(){
        // add board record to firebase DB
        Log.d(TAG, "createBoard")

        val board = Board("TEST", listOf())
        board.firestoreId = db.collection(firestoreCollection).document().id // generate a new document ID
        db.collection(firestoreCollection).document(board.firestoreId).set(board)

        // TODO updateMyBoards
    }


}