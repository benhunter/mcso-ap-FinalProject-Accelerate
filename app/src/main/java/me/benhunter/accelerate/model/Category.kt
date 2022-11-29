package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

class Category(
    val name: String = "",
    @DocumentId var firestoreId: String = "",
    var boardId: String = "",
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (name != other.name) return false
        if (firestoreId != other.firestoreId) return false
        if (boardId != other.boardId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + firestoreId.hashCode()
        result = 31 * result + boardId.hashCode()
        return result
    }

}
