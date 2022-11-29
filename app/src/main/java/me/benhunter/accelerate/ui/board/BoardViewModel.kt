package me.benhunter.accelerate.ui.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import me.benhunter.accelerate.model.Board
import me.benhunter.accelerate.model.Category

class BoardViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val categoriesCollection = "categories"
    private val taskCollection = "tasks"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val board = MutableLiveData<Board>()
    private val categories = MutableLiveData<List<Category>>()

    fun observeCategories(): LiveData<List<Category>> {
        return categories
    }
}
