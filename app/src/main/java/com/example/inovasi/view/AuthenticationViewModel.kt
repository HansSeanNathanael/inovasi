package com.example.inovasi.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inovasi.repository.AccountRepository
import com.example.inovasi.model.AccountModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel : ViewModel() {

    private val firebaseAuthService: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    private val accountRepository : AccountRepository = AccountRepository()

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _process = MutableStateFlow(false)

    val user: StateFlow<FirebaseUser?> = _user
    val email : StateFlow<String> = _email
    val password : StateFlow<String> = _password
    val process : StateFlow<Boolean> = _process

    fun setEmail(email : String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun register(email : String, password : String) {
        viewModelScope.launch {
            _process.value = true

            firebaseAuthService.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        val newAccount = firebaseAuthService.currentUser
                        _user.value = newAccount
                        accountRepository.save(AccountModel(newAccount!!.uid, "", newAccount.email!!))
                    }
                    else {
                        _user.value = null
                    }
                    _process.value = false
                }
                .addOnFailureListener {
                    _process.value = false
                }
        }
    }

    fun login(email : String, password : String) {
        viewModelScope.launch {
            _process.value = true

            firebaseAuthService.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->

                    if (result.isSuccessful) {
                        _user.value = firebaseAuthService.currentUser
                    }
                    else {
                        _user.value = null
                    }
                    _process.value = false
                }
                .addOnFailureListener {
                    _process.value = false
                }
        }
    }
}