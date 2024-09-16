package com.example.inovasi.view.menu.home

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


class TaskListViewModel : ViewModel() {
    private val taskRepository = TaskRepository()
    private val firebaseAuthService = FirebaseAuth.getInstance()

    private val _pendingTaskModelList = MutableStateFlow(ArrayList<TaskModel>())
    private val _ongoingTaskModelList = MutableStateFlow(ArrayList<TaskModel>())
    private val _doneTaskModelList = MutableStateFlow(ArrayList<TaskModel>())

    val pendingTaskModelList : StateFlow<ArrayList<TaskModel>> = _pendingTaskModelList
    val ongoingTaskModelList : StateFlow<ArrayList<TaskModel>> = _ongoingTaskModelList
    val doneTaskModelList : StateFlow<ArrayList<TaskModel>> = _doneTaskModelList

    fun load(context : Context) {
        viewModelScope.launch {
            val userId = firebaseAuthService.currentUser!!.uid

            val listOfTask = taskRepository.getAll(userId) { snapshot, e ->
                snapshot?.let {
                    val newPendingTaskList = ArrayList(_pendingTaskModelList.value)
                    val newOngoingTaskList = ArrayList(_ongoingTaskModelList.value)
                    val newDoneTaskList = ArrayList(_doneTaskModelList.value)

                    for (dc in it.documentChanges) {
                        val taskModel = dc.document.toObject(TaskModel::class.java)

                        newPendingTaskList.remove(taskModel)
                        newOngoingTaskList.remove(taskModel)
                        newDoneTaskList.remove(taskModel)

                        val today = Date()
                        if (dc.type != DocumentChange.Type.REMOVED) {
                            if (taskModel.status == "pending" && taskModel.date.before(today)) {
                                val newTaskModel = TaskModel(taskModel.id, taskModel.subject, taskModel.description, taskModel.date, "ongoing", taskModel.reminder, taskModel.fileData)
                                taskRepository.save(context, userId, newTaskModel)
                                newPendingTaskList.add(newTaskModel)
                            }
                            else if (taskModel.status == "pending") {
                                newPendingTaskList.add(taskModel)
                            }
                            else if (taskModel.status == "ongoing") {
                                newOngoingTaskList.add(taskModel)
                            }
                            else if (taskModel.status == "done") {
                                newDoneTaskList.add(taskModel)
                            }
                        }

                        _pendingTaskModelList.value = newPendingTaskList
                        _ongoingTaskModelList.value = newOngoingTaskList
                        _doneTaskModelList.value = newDoneTaskList
                    }
                }
            }

            val newPendingTaskList = ArrayList<TaskModel>()
            val newOngoingTaskList = ArrayList<TaskModel>()
            val newDoneTaskList = ArrayList<TaskModel>()

            val today = Date()
            for (task in listOfTask) {
                if (task.status == "pending" && task.date.before(today)) {
                    val newTaskModel = TaskModel(task.id, task.subject, task.description, task.date, "ongoing", task.reminder, task.fileData)
                    taskRepository.save(context, userId, newTaskModel)
                    newPendingTaskList.add(newTaskModel)
                }
                else if (task.status == "pending") {
                    newPendingTaskList.add(task)
                }
                else if (task.status == "ongoing") {
                    newOngoingTaskList.add(task)
                }
                else if (task.status == "done") {
                    newDoneTaskList.add(task)
                }
            }
            _pendingTaskModelList.value = newPendingTaskList
            _ongoingTaskModelList.value = newOngoingTaskList
            _doneTaskModelList.value = newDoneTaskList
        }
    }
}