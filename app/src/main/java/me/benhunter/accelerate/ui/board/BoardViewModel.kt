package me.benhunter.accelerate.ui.board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import me.benhunter.accelerate.model.Board
import me.benhunter.accelerate.model.Category
import me.benhunter.accelerate.model.Task

class BoardViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val boardsCollection = "boards" // TODO use same string as MyBoardsViewModel
    private val categoriesCollection = "categories"
    private val taskCollection = "tasks"

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val board = MutableLiveData<Board>()
    private var boardFirestoreId: String? = null
    private val categories = MutableLiveData<List<Category>>()
    private val tasks = MutableLiveData<List<Task>>()

    // Must set a board before interacting with the Categories
    fun setBoard(boardFirestoreId: String) {
        Log.d(TAG, "setBoard get $boardFirestoreId")

        this.boardFirestoreId = boardFirestoreId

        fetchBoardCategoriesAndTasks()
    }

    fun fetchBoard() {
        if (boardFirestoreId == null) return

        db.collection(boardsCollection).document(boardFirestoreId!!).get()
            .addOnSuccessListener { documentSnapshot ->
                Log.d(TAG, "fetchBoard get $boardFirestoreId success. Posting value.")

                val boardResult = documentSnapshot.toObject(Board::class.java)
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

        if (boardFirestoreId == null) return

        db.collection(categoriesCollection).whereEqualTo("boardId", boardFirestoreId).get()
            .addOnSuccessListener {
                Log.d(TAG, "fetchCategories query success")

                val categoriesResult = it.toObjects(Category::class.java)

                Log.d(TAG, "fetchCategories posting categories")
                categories.postValue(categoriesResult.sortedBy { category -> category.position })
            }.addOnFailureListener {
                Log.d(TAG, "fetchCategories query failed")
            }
    }

    fun createCategory(name: String) {
        Log.d(TAG, "createCategory name $name")

        val firestoreId = db.collection(categoriesCollection).document().id
        val boardId = board.value?.firestoreId
            ?: throw RuntimeException("Cannot create Category without boardId.")
        val position = categories.value?.size ?: 0
        val category = Category(name, firestoreId, boardId, position)

        db.collection(categoriesCollection).document(category.firestoreId).set(category)
            .addOnSuccessListener { fetchCategories() }
    }

    fun observeBoard(): LiveData<Board> {
        return board
    }

    fun createTask(name: String, categoryId: String) {
        Log.d(TAG, "createTask name $name")

        val firestoreId = db.collection(taskCollection).document().id
        val position =
            tasks.value?.filter { task: Task -> task.categoryId == categoryId }?.size ?: 0
        val task = Task(name, firestoreId, categoryId, position)

        db.collection(taskCollection).document(task.firestoreId).set(task).addOnSuccessListener {
            fetchTasks()
        }
    }

    fun fetchTasks() {
        Log.d(TAG, "fetchTasks")

        db.collection(taskCollection).get().addOnSuccessListener {
            Log.d(TAG, "fetchTasks query success")

            val taskResult = it.toObjects(Task::class.java)

            Log.d(TAG, "fetchTasks posting tasks")
            tasks.postValue(taskResult)
        }.addOnFailureListener {
            Log.d(TAG, "fetchTasks query failed")
        }
    }

    fun observeTasks(): LiveData<List<Task>> {
        return tasks
    }

    fun shareBoard(email: String) {
        val currentBoard = board.value ?: return

        val memberEmails = currentBoard.memberEmails.toMutableList()

        if (email in memberEmails) return

        val newMemberEmails = currentBoard.memberEmails.toMutableList()
        newMemberEmails.add(email)
        currentBoard.memberEmails = newMemberEmails

        db.collection(boardsCollection).document(currentBoard.firestoreId).set(currentBoard)
            .addOnSuccessListener {
                Log.d(
                    TAG,
                    "shareBoard ${board.value?.firestoreId} to email $email success. Posting value."
                )

                board.postValue(currentBoard)
            }
    }

    fun fetchBoardCategoriesAndTasks() {
        fetchBoard()
        fetchCategories()
        fetchTasks()
    }
}
