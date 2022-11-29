package me.benhunter.accelerate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class MainViewModel : ViewModel() {

    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    private val email = MutableLiveData<String>()

    fun updateUser() {
        firebaseAuthLiveData.updateUser()
        email.postValue(firebaseAuthLiveData.getCurrentUser()?.email ?: "")
    }

    fun getUser(): FirebaseUser? {
        return firebaseAuthLiveData.getCurrentUser()
    }

    fun observeEmail(): LiveData<String> {
        return email
    }

    fun signOut() {
        firebaseAuthLiveData.signOut()
        email.postValue("")
    }
}