package me.benhunter.accelerate.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import me.benhunter.accelerate.model.Category

class CategoryViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val categoriesCollection = "categories"
    private val taskCollection = "tasks"

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var categoryId: String = ""
    private val category = MutableLiveData<Category?>()

    fun setCategory(categoryId: String) {
        this.categoryId = categoryId

        db.collection(categoriesCollection).document(categoryId).get()
            .addOnSuccessListener {
                val category = it.toObject(Category::class.java)
                this.category.postValue(category)
            }
    }

    fun observeCategory(): LiveData<Category?> {
        return category
    }

    fun saveName(name: String) {
        category.value?.let {
            val updatedCategory = Category(name, it.firestoreId, it.boardId, it.position)
            db.collection(categoriesCollection).document(categoryId).set(updatedCategory)
                .addOnSuccessListener {
                    category.postValue(updatedCategory)
                }
        }
    }

    fun delete() {
        val position = category.value?.position ?: 0

        db.collection(categoriesCollection).document(categoryId).delete()
            .addOnSuccessListener {
                category.postValue(null)
            }

        // Delete Tasks in this Category
        db.collection(taskCollection).whereEqualTo("categoryId", categoryId).get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.forEach {
                    it.reference.delete()
                }
            }

        // Update other Categories position
        db.collection(categoriesCollection).get().addOnSuccessListener { querySnapshot ->
            val batch = db.batch()
            querySnapshot.forEach {
                val category = it.toObject(Category::class.java)
                if (category.position > position) {
                    // decrement and update
                    category.position -= 1
                    batch.set(it.reference, category)
                }
            }
            batch.commit()
        }
    }
}