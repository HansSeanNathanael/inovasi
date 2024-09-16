package com.example.inovasi.view.menu.account

import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DialogData(
    val title : String,
    val inputTitle : String,
    val value : String,
    val visualTransformation : VisualTransformation,
    val onDismissRequest : () -> Unit,
    val onConfirmation : (String) -> Unit,
)

class AccountMenuViewModel : ViewModel() {

    private val _dialogData = MutableStateFlow<DialogData?>(null)

    val dialogData : StateFlow<DialogData?> = _dialogData

    fun setDialogData(dialogData : DialogData?) {
        _dialogData.value = dialogData
    }
}