package com.example.inovasi.view.menu.task

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovasi.view.menu.home.TaskCard
import com.example.inovasi.view.menu.task.ui.theme.InovasiTheme
import com.example.inovasi.viewmodel.ViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class TaskListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val status = intent.getStringExtra("status")
        val startTime = intent.extras?.get("start_time") as Date?
        val endTime = intent.extras?.get("end_time") as Date?

        enableEdgeToEdge()
        setContent {
            InovasiTheme {
                TaskListActivityRender(s = status, st = startTime, et = endTime)
            }
        }
    }
}

@Composable
fun TaskListActivityRender(
    taskListActivityViewModel: TaskListActivityViewModel = viewModel(factory = ViewModelFactory),
    s: String? = null,
    st: Date? = null,
    et: Date? = null,
) {
    val context = LocalContext.current

    val status by remember { mutableStateOf(s) }
    val (startTime, setStartTime) = remember { mutableStateOf(st) }
    val (endTime, setEndTime) = remember { mutableStateOf(et) }
    val (showDatePickerStart, setShowDatePickerStart) = remember { mutableStateOf(false) }
    val (showDatePickerEnd, setShowDatePickerEnd) = remember { mutableStateOf(false) }

    taskListActivityViewModel.load(status, startTime, endTime)

    val taskList by taskListActivityViewModel.taskModelList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (showDatePickerStart) {

            var localDate: LocalDate = LocalDate.now()
            if (startTime != null) {
                localDate = startTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }

            val datePicker = DatePickerDialog(context, { _, year, month, dayOfMonth ->
                setStartTime(
                    Date.from(
                        LocalDateTime.of(
                            year,
                            month,
                            dayOfMonth,
                            0,
                            0
                        ).atZone(
                            ZoneId.systemDefault()
                        ).toInstant()
                    )
                )
                setShowDatePickerStart(false)
            }, localDate.year, localDate.monthValue, localDate.dayOfMonth)

            datePicker.show()
        }

        if (showDatePickerEnd) {

            var localDate: LocalDate = LocalDate.now()
            if (endTime != null) {
                localDate = endTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }

            val datePicker = DatePickerDialog(context, { _, year, month, dayOfMonth ->
                setEndTime(
                    Date.from(
                        LocalDateTime.of(
                            year,
                            month,
                            dayOfMonth,
                            0,
                            0
                        ).atZone(
                            ZoneId.systemDefault()
                        ).toInstant()
                    )
                )
                setShowDatePickerEnd(false)
            }, localDate.year, localDate.monthValue, localDate.dayOfMonth)

            datePicker.show()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = {
                    setShowDatePickerStart(true)
                },
                modifier = Modifier
                    .weight(1f)
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary))
            ) {
                Text("$startTime")
            }

            TextButton(
                onClick = {
                    setShowDatePickerEnd(true)
                },
                modifier = Modifier
                    .weight(1f)
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary))
            ) {
                Text("$endTime")
            }
        }
        
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(taskList) { item ->
                TaskCard(item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    InovasiTheme {
        TaskListActivityRender()
    }
}