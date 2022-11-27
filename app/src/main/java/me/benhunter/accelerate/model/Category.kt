package me.benhunter.accelerate.model

import com.google.firebase.firestore.DocumentId

class Category(
    val name: String = "",
    val tasks: MutableList<Task> = listOf<Task>().toMutableList(), // TODO a list of Task IDs from Firestore? Or should Category hold the actual Tasks?
    @DocumentId var firestoreId: String = "",
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (name != other.name) return false
        if (tasks != other.tasks) return false
        if (firestoreId != other.firestoreId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + tasks.hashCode()
        result = 31 * result + firestoreId.hashCode()
        return result
    }
}
