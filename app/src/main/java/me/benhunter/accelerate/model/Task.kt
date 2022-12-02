package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

class Task(
    val name: String = "",
    @DocumentId val firestoreId: String = "",
    val categoryId: String = "",
    var position: Int = 0,
    val dueDate: String = "",
    val dueTime: String = "",
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (name != other.name) return false
        if (firestoreId != other.firestoreId) return false
        if (categoryId != other.categoryId) return false
        if (position != other.position) return false
        if (dueDate != other.dueDate) return false
        if (dueTime != other.dueTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + firestoreId.hashCode()
        result = 31 * result + categoryId.hashCode()
        result = 31 * result + position
        result = 31 * result + dueDate.hashCode()
        result = 31 * result + dueTime.hashCode()
        return result
    }
}
