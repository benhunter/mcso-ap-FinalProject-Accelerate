package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

// TODO implement board members (self + sharing)
class Board(
    val name: String = "",
    val categories: List<Category> = listOf(),
    @DocumentId var firestoreId: String = "",
    var ownerName: String = "",
    var ownerUid: String = "",
    var uuid: String = "",
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (name != other.name) return false
        if (categories != other.categories) return false
        if (firestoreId != other.firestoreId) return false
        if (ownerName != other.ownerName) return false
        if (ownerUid != other.ownerUid) return false
        if (uuid != other.uuid) return false

        return true
    }
}