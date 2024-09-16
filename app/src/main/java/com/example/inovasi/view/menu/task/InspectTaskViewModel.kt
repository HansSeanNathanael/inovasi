package com.example.inovasi.view.menu.task

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inovasi.model.FileModel
import com.example.inovasi.model.TaskModel
import com.example.inovasi.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.UUID

class InspectTaskViewModel : ViewModel() {

    private val firebaseAuthService : FirebaseAuth = FirebaseAuth.getInstance()
    private val taskRepository = TaskRepository()

    private val _taskModel = MutableStateFlow<TaskModel?>(null)
    private val _fileModel = MutableStateFlow<FileModel?>(null)

    val taskModel : StateFlow<TaskModel?> = _taskModel
    val selectedDownload : StateFlow<FileModel?> = _fileModel

    fun load(id : String) {
        viewModelScope.launch {
            firebaseAuthService.currentUser?.let { it ->
                _taskModel.value = taskRepository.getById(it.uid, id)
            }
        }
    }

    fun setTaskModel(taskModel: TaskModel) {
        _taskModel.value = taskModel
    }

    fun setFileModelDownload(fileModel: FileModel?) {
        _fileModel.value = fileModel
    }

    fun save(context : Context) {
        firebaseAuthService.currentUser?.let { it ->
            _taskModel.value?.let { ti ->
                taskRepository.save(context, it.uid, ti)
            }
        }
    }
}