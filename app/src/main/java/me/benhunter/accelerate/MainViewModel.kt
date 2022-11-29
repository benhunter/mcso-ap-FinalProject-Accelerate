package me.benhunter.accelerate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class MainViewModel : ViewModel() {

    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    private val email = MutableLiveData<String>()
    private val displayName = MutableLiveData<String>()

    fun updateUser() {
        firebaseAuthLiveData.updateUser()
        email.postValue(firebaseAuthLiveData.getCurrentUser()?.email ?: "")
        displayName.postValue(firebaseAuthLiveData.getCurrentUser()?.displayName ?: "")
    }

    fun getUser(): FirebaseUser? {
        return firebaseAuthLiveData.getCurrentUser()
    }

    fun observeEmail(): LiveData<String> {
        return email
    }

    fun observeDisplayName(): LiveData<String>{
        return displayName
    }

    fun signOut() {
        firebaseAuthLiveData.signOut()
        email.postValue("")
    }
}