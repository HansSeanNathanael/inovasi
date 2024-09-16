package com.example.inovasi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inovasi.view.AuthenticationViewModel
import com.example.inovasi.view.menu.MenuViewModel
import com.example.inovasi.view.menu.account.AccountMenuViewModel
import com.example.inovasi.view.menu.home.TaskListViewModel
import com.example.inovasi.view.menu.task.AddNewTaskViewModel
import com.example.inovasi.view.menu.task.DateTimePickerViewModel
import com.example.inovasi.view.menu.task.InspectTaskViewModel
import com.example.inovasi.view.menu.task.TaskCalendarViewModel
import com.example.inovasi.view.menu.task.TaskListActivityViewModel

object ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            return AuthenticationViewModel() as T
        }
        else if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel() as T
        }
        else if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel() as T
        }
        else if (modelClass.isAssignableFrom(AccountMenuViewModel::class.java)) {
            return AccountMenuViewModel() as T
        }
        else if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            return TaskListViewModel() as T
        }
        else if (modelClass.isAssignableFrom(TaskCalendarViewModel::class.java)) {
            return TaskCalendarViewModel() as T
        }
        else if (modelClass.isAssignableFrom(DateTimePickerViewModel::class.java)) {
            return DateTimePickerViewModel() as T
        }
        else if (modelClass.isAssignableFrom(AddNewTaskViewModel::class.java)) {
            return AddNewTaskViewModel() as T
        }
        else if (modelClass.isAssignableFrom(InspectTaskViewModel::class.java)) {
            return InspectTaskViewModel() as T
        }
        else if (modelClass.isAssignableFrom(TaskListActivityViewModel::class.java)) {
            return TaskListActivityViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}