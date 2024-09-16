package com.example.inovasi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inovasi.repository.AccountRepository
import com.example.inovasi.model.AccountModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {

    private val firebaseAuthService: FirebaseAuth = FirebaseAuth.getInstance()
    private val accountRepository : AccountRepository = AccountRepository()

    private val _accountModel : MutableStateFlow<AccountModel?> = MutableStateFlow(null)
    val accountModel : StateFlow<AccountModel?> = _accountModel

    init {
        viewModelScope.launch {
            val userData = firebaseAuthService.currentUser
            if (userData == null) {
                _accountModel.value = null;
            }
            else {
                val account = accountRepository.get(userData.uid) { snapshot, e ->
                    snapshot?.let { it ->
                        _accountModel.value = it.toObject(AccountModel::class.java)
                    }
                }
                _accountModel.value = account
            }
        }
    }

    fun setAccount(accountModel: AccountModel) {
        _accountModel.value = accountModel
        accountRepository.save(accountModel)
    }

    fun sendResetPasswordEmail() {
        _accountModel.value?.email?.let {
            firebaseAuthService.sendPasswordResetEmail(it)
        }
    }
}