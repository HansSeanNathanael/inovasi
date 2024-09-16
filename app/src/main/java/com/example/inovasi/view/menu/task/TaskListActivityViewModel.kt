package com.example.inovasi.view.menu.task

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inovasi.model.TaskModel
import com.example.inovasi.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date


class TaskListActivityViewModel : ViewModel() {
    private val taskRepository = TaskRepository()
    private val firebaseAuthService = FirebaseAuth.getInstance()

    private val _taskModelList = MutableStateFlow(ArrayList<TaskModel>())

    val taskModelList : StateFlow<ArrayList<TaskModel>> = _taskModelList

    fun load(status: String? = null, startTime : Date? = null, endTime: Date? = null) {
        viewModelScope.launch {
            val userId = firebaseAuthService.currentUser!!.uid
            val listOfTask = taskRepository.getBy(userId, status, startTime, endTime)
            val newTaskModelList = ArrayList<TaskModel>()

            for (task in listOfTask) {
                newTaskModelList.add(task)
            }

            _taskModelList.value = newTaskModelList
        }
    }
}