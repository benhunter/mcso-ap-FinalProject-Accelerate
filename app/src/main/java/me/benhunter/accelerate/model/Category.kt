package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

class Category(
    val name: String = "",
    @DocumentId val firestoreId: String = "",
    val boardId: String = "",
    var position: Int = 0
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (name != other.name) return false
        if (firestoreId != other.firestoreId) return false
        if (boardId != other.boardId) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + firestoreId.hashCode()
        result = 31 * result + boardId.hashCode()
        result = 31 * result + position
        return result
    }
}
