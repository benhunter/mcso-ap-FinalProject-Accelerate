package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

class Category(
    val name: String = "",
    val tasks: MutableList<Task> = listOf<Task>().toMutableList(), // TODO a list of Task IDs from Firestore? Or should Category hold the actual Tasks?
    @DocumentId var firestoreId: String = "",
)
