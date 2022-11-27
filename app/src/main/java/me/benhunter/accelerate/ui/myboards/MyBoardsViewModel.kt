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
    private val categoriesCollection = "categories"
    private val taskCollection = "tasks"

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
            Log.d(TAG, "fetchMyBoards db get().addOnSuccessListener")
            Log.d(TAG, "fetchMyBoards result ${result.toString()}")
            myBoards.postValue(result.mapNotNull { it.toObject(Board::class.java) })
        }
    }

    fun fetchCurrentBoard() {
        Log.d(TAG, "fetchCurrentBoard")

        currentBoard.value?.let {
            db.collection(myBoardsCollection).document(it.firestoreId).get()
                .addOnSuccessListener { result ->
                    Log.d(TAG, "fetchCurrentBoard addOnSuccessListener result $result")
                    val board = result.toObject(Board::class.java)
                    if (board != null) {
                        currentBoard.postValue(board!!)
                    }
                }
        }
    }

    private fun updateCurrentBoardAndMyBoards(board: Board) {
        db.collection(myBoardsCollection).document(board.firestoreId).set(board)
            .addOnSuccessListener { result ->
                Log.d(TAG, "updateBoard addOnSuccessListener")

                // Update currentBoard and/or myBoards
                // TODO fetch from DB or set?
//                fetchCurrentBoard()
                // Note: indexOf doesn't work here because board is different than every
                // board in myBoards
                val updatedMyBoards = myBoards.value?.mapIndexed { index, indexedBoard ->
                    if (indexedBoard.firestoreId == board.firestoreId) {
                        return@mapIndexed board
                    } else {
                        return@mapIndexed indexedBoard
                    }
                }
                if (updatedMyBoards != null) {
                    myBoards.postValue(updatedMyBoards!!)
                }

                currentBoard.postValue(board)
            }
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

    fun createCategoryInCurrentBoard(name: String) {
        currentBoard.value?.let {
            val category = Category(name)
            category.firestoreId =
                db.collection(myBoardsCollection)
                    .document(it.firestoreId)
                    .collection(categoriesCollection)
                    .document()
                    .id

            it.categories.add(category)

            // TODO refactor repetition
            db.collection(myBoardsCollection)
                .document(it.firestoreId)
                .collection(categoriesCollection)
                .document(category.firestoreId)
                .set(category)

            updateCurrentBoardAndMyBoards(it)
        }
    }

    fun createTask(board: Board, category: Category, name: String) {
        Log.d(TAG, "createTask $category id:${category.firestoreId} $name")

//        val task = Task(name)
//        currentBoard.value?.let { board ->
//            board.categories.find { it == category }
//                ?.tasks
//                ?.add(task)
//            updateCurrentBoardAndMyBoards(board)
//        }

        val task = Task(name)
        task.firestoreId =
        db.collection(myBoardsCollection)
            .document(board.firestoreId)
            .collection(categoriesCollection)
            .document(category.firestoreId)
            .collection(taskCollection)
            .document()
            .id

        // TODO refactor repetition
        db.collection(myBoardsCollection)
            .document(board.firestoreId)
            .collection(categoriesCollection)
            .document(category.firestoreId)
            .collection(taskCollection)
            .document(task.firestoreId)
            .set(task)

//        updateCurrentBoardAndMyBoards()
        fetchCurrentBoard()
    }

}