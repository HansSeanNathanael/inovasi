package com.example.inovasi.view.menu.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inovasi.model.TaskDateModel
import com.example.inovasi.model.TaskModel
import com.example.inovasi.repository.TaskDateRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskCalendarViewModel : ViewModel() {

    private val taskDateRepository = TaskDateRepository()
    private val _setOfTaskDate = MutableStateFlow(HashSet<TaskDateModel>())
    private val firebaseAuthService = FirebaseAuth.getInstance()

    val setOfTaskDate : StateFlow<Set<TaskDateModel>> = _setOfTaskDate

    private fun reload(userId : String) {
        viewModelScope.launch {
            val listOfTaskDate = taskDateRepository.getAll(userId)

            val newSetOfTaskDate = HashSet<TaskDateModel>()

            for (taskDateModel in listOfTaskDate) {
                newSetOfTaskDate.add(taskDateModel)
            }

            _setOfTaskDate.value = newSetOfTaskDate
        }
    }

    fun load() {
        viewModelScope.launch {
            val userId = firebaseAuthService.currentUser!!.uid

            val listOfTaskDate = taskDateRepository.getAll(userId) { _, _ ->
                reload(userId)
            }

            val newSetOfTaskDate = HashSet<TaskDateModel>()

            for (taskDateModel in listOfTaskDate) {
                newSetOfTaskDate.add(taskDateModel)
            }

            _setOfTaskDate.value = newSetOfTaskDate
        }
    }
}