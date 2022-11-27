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
    private val myBoardsCollection = "myBoards"
    private val categoryCollection = "categories"

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val myBoards = MutableLiveData<List<Board>>()
    private var currentBoard = MutableLiveData<Board>()
    private val currentCategories = MutableLiveData<List<Category>>()

    init {
        // Update the boards when user logs in.
        // TODO could try to consolidate with MainViewModel to have FirestoreAuth in one place
        FirestoreAuthLiveData().observeForever {
            Log.d(TAG, "FirestoreAuthLiveData $it")
            if (it != null) {
                fetchMyBoards()
            }
        }
    }

    // TODO replace with myBoards from DB
    val boards = List(4) { boardNumber ->
        Board("Board $boardNumber", generateBoard().toMutableList())
    }

    private fun generateBoard(): List<Category> {
        return List(5) { categoryNumber ->
            val tasks = List(5) { taskNumber -> Task("task $taskNumber") }
            Category("category $categoryNumber", tasks)
        }
    }

    // TODO remove?
//    fun getBoardById(boardId: String): Board? {
//        return boards.find { it.name == boardId }
//    }

    fun createBoard(name: String) {
        // add board record to firebase DB
        Log.d(TAG, "createBoard")

        val board = Board(name)
        board.firestoreId =
            db.collection(myBoardsCollection).document().id // generate a new document ID
        db.collection(myBoardsCollection).document(board.firestoreId).set(board)

        fetchMyBoards()
    }

    fun fetchMyBoards() {
        Log.d(TAG, "fetchMyBoards")

        db.collection(myBoardsCollection).get().addOnSuccessListener { result ->
            Log.d(TAG, "db get().addOnSuccessListener")
            myBoards.postValue(result.mapNotNull { it.toObject(Board::class.java) })
        }
    }

    private fun updateBoard(board: Board) {
        db.collection(myBoardsCollection).document(board.firestoreId).set(board)
        fetchMyBoards()
    }

    fun observeMyBoards(): LiveData<List<Board>> {
        Log.d(TAG, "observeMyBoards")
        return myBoards
    }

    fun setCurrentBoard(boardFirestoreId: String) {
        val boardFilterResult = myBoards.value?.filter { it.firestoreId == boardFirestoreId }
        if (boardFilterResult != null) {
            if (boardFilterResult.isNotEmpty()) {
                currentBoard.postValue(boardFilterResult[0])
            }
        }
        fetchMyBoards()
    }

    fun observeCurrentBoard(): LiveData<Board> {
        return currentBoard
    }

    fun createCategory(name: String) {
        val category = Category(name)
//        currentBoard.value?.categories?.add(category)

        currentBoard.value?.let {
            it.categories.add(category)
            updateBoard(it) }
    }

    fun observeCurrentBoardCategories(): LiveData<List<Category>> {
        return currentCategories
    }

}