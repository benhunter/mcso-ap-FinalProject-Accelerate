package me.benhunter.accelerate.ui.myboards

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import me.benhunter.accelerate.FirestoreAuthLiveData
import me.benhunter.accelerate.model.Board
import me.benhunter.accelerate.model.Category
import me.benhunter.accelerate.model.Task

class MyBoardsViewModel : ViewModel() {

    private val TAG = javaClass.simpleName
    private val collection = "myBoards"

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val myBoards = MutableLiveData<List<Board>>()

    init {
        // Update the boards when user logs in.
        FirestoreAuthLiveData().observeForever {
            Log.d(TAG, "FirestoreAuthLiveData $it")
            if (it != null){
                fetchMyBoards()
            }
        }
    }

    // TODO replace with myBoards from DB
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

    fun createBoard(name: String) {
        // add board record to firebase DB
        Log.d(TAG, "createBoard")

        val board = Board(name, listOf())
        board.firestoreId = db.collection(collection).document().id // generate a new document ID
        db.collection(collection).document(board.firestoreId).set(board)

        fetchMyBoards()
    }

    fun fetchMyBoards() {
        Log.d(TAG, "fetchMyBoards")

        db.collection(collection).get().addOnSuccessListener { result ->
            Log.d(TAG, "db get().addOnSuccessListener")
            myBoards.postValue(result.mapNotNull { it.toObject(Board::class.java) })
        }
    }

    fun observeMyBoards(): LiveData<List<Board>> {
        Log.d(TAG, "observeMyBoards")
        return myBoards
    }
}