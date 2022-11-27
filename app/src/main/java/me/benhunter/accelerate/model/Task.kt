package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

class Task(
    val name: String = "",
    @DocumentId var firestoreId: String = "",
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (name != other.name) return false
        if (firestoreId != other.firestoreId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + firestoreId.hashCode()
        return result
    }
}
