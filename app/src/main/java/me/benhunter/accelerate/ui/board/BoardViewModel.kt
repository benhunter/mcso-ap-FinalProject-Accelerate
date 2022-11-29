package me.benhunter.accelerate.ui.board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import me.benhunter.accelerate.model.Board
import me.benhunter.accelerate.model.Category

class BoardViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val boardsCollection = "boards" // TODO use same as MyBoardsViewModel
    private val categoriesCollection = "categories"
    private val taskCollection = "tasks"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val board = MutableLiveData<Board>()
    private val categories = MutableLiveData<List<Category>>()

    // Must set a board before interacting with the Categories
    fun setBoard(boardFirestoreId: String) {
        Log.d(TAG, "setBoard get $boardFirestoreId")

        db.collection(boardsCollection).document(boardFirestoreId).get().addOnSuccessListener {
            Log.d(TAG, "setBoard get $boardFirestoreId success. Posting value.")

            val boardResult = it.toObject(Board::class.java)
            boardResult?.let {
                board.postValue(it)
            }
        }
    }

    fun observeCategories(): LiveData<List<Category>> {
        return categories
    }

    fun fetchCategories() {
        Log.d(TAG, "fetchCategories")

        val boardId = board.value?.firestoreId
            ?: throw RuntimeException("Cannot fetch Categories without boardId.")

        db.collection(categoriesCollection).whereEqualTo("boardId", boardId).get()
            .addOnSuccessListener {
                Log.d(TAG, "fetchCategories query success")

                val categoriesResult = it.toObjects(Category::class.java)

                Log.d(TAG, "fetchCategories posting categories")
                categories.postValue(categoriesResult)
            }
            .addOnFailureListener {
                Log.d(TAG, "fetchCategories query failed")
            }
    }

    fun createCategory(name: String) {
        Log.d(TAG, "createCategory name $name")

        val firestoreId = db.collection(categoriesCollection).document().id
        val boardId = board.value?.firestoreId
            ?: throw RuntimeException("Cannot create Category without boardId.")
        val category = Category(name, firestoreId, boardId)

        db.collection(categoriesCollection).document(category.firestoreId).set(category)
            .addOnSuccessListener { fetchCategories() }
    }

    fun observeBoard(): LiveData<Board> {
        return board
    }
}
