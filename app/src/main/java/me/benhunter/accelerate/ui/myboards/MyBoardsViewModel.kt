package me.benhunter.accelerate.ui.myboards

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import me.benhunter.accelerate.FirestoreAuthLiveData
import me.benhunter.accelerate.model.Board

class MyBoardsViewModel : ViewModel() {

    private val TAG = javaClass.simpleName
    private val boardsCollection = "boards"

    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val myBoards = MutableLiveData<List<Board>>()

    private var currentUserEmail: String? = null

    init {
        // Update the boards when user logs in.
        // TODO could try to consolidate with MainViewModel to have FirestoreAuth in one place
        firebaseAuthLiveData.observeForever {
            Log.d(TAG, "FirestoreAuthLiveData $it")
            if (it != null) {
                fetchMyBoards()
            }

            currentUserEmail = it?.email
        }
    }

    fun createBoard(name: String) {
        // add board record to firebase DB
        Log.d(TAG, "createBoard")

        val boardFirestoreId =
            db.collection(boardsCollection).document().id // generate a new document ID
        val position = myBoards.value?.size ?: 0
        val memberEmails = listOf(currentUserEmail ?: "")
        val board = Board(name, boardFirestoreId, position, memberEmails)
        db.collection(boardsCollection).document(board.firestoreId).set(board)

        fetchMyBoards()
    }

    fun fetchMyBoards() {
        Log.d(TAG, "fetchMyBoards")

        firebaseAuthLiveData.getCurrentUser()?.email?.let { currentUserEmail ->
            db
                .collection(boardsCollection)
                .whereArrayContains("memberEmails", currentUserEmail)
                .get()
                .addOnSuccessListener { result ->
                    Log.d(TAG, "fetchMyBoards db get().addOnSuccessListener")
                    Log.d(TAG, "fetchMyBoards result $result")
                    result.documents.forEach {
                        Log.d(TAG, "fetchMyBoards result.documents $it")
                        Log.d(TAG, "fetchMyBoards result.documents ${it.data}")
                    }
                    myBoards.postValue(result.mapNotNull { it.toObject(Board::class.java) }
                        .sortedBy { it.position })
                }
        }
    }

    fun observeMyBoards(): LiveData<List<Board>> {
        Log.d(TAG, "observeMyBoards")
        return myBoards
    }

}