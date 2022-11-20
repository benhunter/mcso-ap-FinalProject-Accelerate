package me.benhunter.accelerate

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class MainViewModel: ViewModel() {

    private var firebaseAuthLiveData = FirestoreAuthLiveData()

    fun updateUser() {
        firebaseAuthLiveData.updateUser()
    }

    fun getUser(): FirebaseUser? {
        return firebaseAuthLiveData.getCurrentUser()
    }

    fun signOut(){
        firebaseAuthLiveData.signOut()
    }
}