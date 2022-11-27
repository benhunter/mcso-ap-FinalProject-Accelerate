package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

class Task(
    val name: String = "",
    @DocumentId var firestoreId: String = "",
)
