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

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val myBoards = MutableLiveData<List<Board>>()
    private var currentBoard = MutableLiveData<Board>()

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
        val board = myBoards.value?.find { it.firestoreId == boardFirestoreId } ?: return
        currentBoard.postValue(board)
        fetchMyBoards()
    }

    fun observeCurrentBoard(): LiveData<Board> {
        return currentBoard
    }

    fun createCategory(name: String) {
        val category = Category(name)
        currentBoard.value?.let {
            it.categories.add(category)
            updateBoard(it)
        }
    }

    fun createTask(category: Category, name: String) {
        Log.d(TAG, "createTask $category id:${category.firestoreId} $name")

        val task = Task(name)
        currentBoard.value?.let { board ->
            board.categories.find { it == category }
                ?.tasks
                ?.add(task)
            updateBoard(board)
        }
    }

}