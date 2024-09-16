package com.example.inovasi.view.menu.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovasi.viewmodel.ViewModelFactory

@Composable
fun DateTimePicker(
    dateTimePickerViewModel: DateTimePickerViewModel = viewModel(factory = ViewModelFactory),
    onChange : ((date : DateTimePickerViewModel.Date, time : DateTimePickerViewModel.Time) -> Unit)? = null
) {

    val date by dateTimePickerViewModel.date.collectAsState()
    val time by dateTimePickerViewModel.time.collectAsState()
    val showDatePicker by dateTimePickerViewModel.showDatePicker.collectAsState()
    val showTimePicker by dateTimePickerViewModel.showTimePicker.collectAsState()

    if (showDatePicker) {
        val datePicker = DatePickerDialog(LocalContext.current, { view, year, month, dayOfMonth ->
            dateTimePickerViewModel.setDate(year, month, dayOfMonth)
            dateTimePickerViewModel.setShowDatePicker(false)
        }, date.year, date.month, date.day)

        datePicker.show()
    }

    if (showTimePicker) {
        val timePicker = TimePickerDialog(LocalContext.current, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            dateTimePickerViewModel.setTime(hourOfDay, minute)
            dateTimePickerViewModel.setShowTimePicker(false)
        }, time.hour, time.minute, true)

        timePicker.show()
    }

    onChange?.let { it(date, time) }


    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(
            onClick = {
                dateTimePickerViewModel.setShowDatePicker(true)
            },
            modifier = Modifier
                .weight(1f)
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary))
        ) {
            Text("${date.day}/${date.month}/${date.year}")
        }

        TextButton(
            onClick = {
                dateTimePickerViewModel.setShowTimePicker(true)
            },
            modifier = Modifier
                .weight(1f)
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary))
        ) {
            var timeString = ""
            if (time.hour < 10) {
                timeString = "0${time.hour}"
            }
            else {
                timeString = "${time.hour}"
            }

            if (time.minute < 10) {
                timeString += ":0${time.minute}"
            }
            else {
                timeString += ":${time.minute}"
            }

            Text(timeString)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DateTimePickerPreview() {
    DateTimePicker()
}
