package me.benhunter.accelerate

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private var firebaseAuthLiveData = FirestoreAuthLiveData()

    fun updateUser() {
        firebaseAuthLiveData.updateUser()
    }
}