package com.example.inovasi.view.menu.task

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.inovasi.model.FileModel
import com.example.inovasi.model.TaskModel
import com.example.inovasi.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date
import java.util.UUID

class AddNewTaskViewModel : ViewModel() {

    private val firebaseAuthService : FirebaseAuth = FirebaseAuth.getInstance()
    private val taskRepository = TaskRepository()

    private val _subject = MutableStateFlow("")
    private val _description  = MutableStateFlow("")
    private val _date  = MutableStateFlow(Date())
    private val _reminder  = MutableStateFlow(0)
    private val _fileData : MutableStateFlow<List<FileModel>>  = MutableStateFlow(ArrayList<FileModel>())
    private val _fileModel = MutableStateFlow<FileModel?>(null)

    val subject : StateFlow<String> = _subject
    val description : StateFlow<String> = _description
    val date : StateFlow<Date> = _date
    val reminder : StateFlow<Int> = _reminder
    val fileData : StateFlow<List<FileModel>> = _fileData
    val selectedDownload : StateFlow<FileModel?> = _fileModel

    fun setSubject(subject: String) {
        _subject.value = subject
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    fun setDate(date: Date) {
        _date.value = date
    }
    fun setReminder(reminder: Int) {
        _reminder.value = reminder
    }

    fun setFileData(fileData: List<FileModel>) {
        _fileData.value = fileData
    }

    fun setFileModelDownload(fileModel: FileModel?) {
        _fileModel.value = fileModel
    }

    fun save(context : Context) {
        firebaseAuthService.currentUser?.let {
            val calendar: Calendar = Calendar.getInstance()
            calendar.setTime(date.value)

            calendar.add(Calendar.MINUTE, -reminder.value)
            val reminderDate = calendar.getTime()

            taskRepository.save(context, it.uid, TaskModel(
                UUID.randomUUID().toString(),
                subject.value,
                description.value,
                date.value,
                "pending",
                reminderDate,
                fileData.value
            ))
        }
    }
}