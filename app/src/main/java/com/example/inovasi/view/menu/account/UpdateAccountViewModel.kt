package com.example.inovasi.view.menu.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UpdateAccountViewModel(value : String) : ViewModel() {

    private val _value : MutableStateFlow<String> = MutableStateFlow(value)

    val name : StateFlow<String> = _value

    fun setValue(value : String) {
        _value.value = value
    }
}