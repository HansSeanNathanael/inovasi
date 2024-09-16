package com.example.inovasi.view.menu.task

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

class DateTimePickerViewModel : ViewModel() {

    data class Date(val year : Int, val month: Int, val day: Int)
    data class Time(val hour : Int, val minute: Int)

    private val _date : MutableStateFlow<Date>
    private val _time : MutableStateFlow<Time>

    private val _showDatePicker = MutableStateFlow(false)
    private val _showTimePicker = MutableStateFlow(false)

    val date : StateFlow<Date>
    val time : StateFlow<Time>

    val showDatePicker : StateFlow<Boolean> = _showDatePicker
    val showTimePicker : StateFlow<Boolean> = _showTimePicker

    init {
        val now = LocalDateTime.now()

        _date = MutableStateFlow(
            DateTimePickerViewModel.Date(
            now.year,
            now.monthValue,
            now.dayOfMonth
        ))

        _time = MutableStateFlow(
            DateTimePickerViewModel.Time(
            now.hour,
            now.minute
        ))

        date = _date
        time = _time
    }

    fun setDate(year : Int, month : Int, day : Int) {

        _date.value = DateTimePickerViewModel.Date(
            year,
            month,
            day
        )
    }

    fun setTime(hour : Int, minute : Int) {
        _time.value = DateTimePickerViewModel.Time(
            hour,
            minute,
        )
    }

    fun setShowDatePicker(show : Boolean) {
        _showDatePicker.value = show
    }

    fun setShowTimePicker(show : Boolean) {
        _showTimePicker.value = show
    }


}