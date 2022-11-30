package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

// TODO implement board members (self + sharing)
class Board(
    val name: String = "",
    @DocumentId var firestoreId: String = "",
//    var ownerName: String = "",
//    var ownerUid: String = "",
    val position: Int = 0
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (name != other.name) return false
        if (firestoreId != other.firestoreId) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + firestoreId.hashCode()
        result = 31 * result + position
        return result
    }
}