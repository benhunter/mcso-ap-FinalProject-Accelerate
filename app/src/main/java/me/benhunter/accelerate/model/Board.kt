package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

// TODO board members (self + sharing)
class Board(
    val name: String = "",
    val categories: List<Category> = listOf(),
    @DocumentId var firestoreId: String = "",
    var ownerName: String = "",
    var ownerUid: String = "",
    var uuid: String = "",
)