package com.example.inovasi.view.menu.task

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovasi.model.TaskDateModel
import com.example.inovasi.viewmodel.ViewModelFactory
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState

@Composable
fun DayContent(dayState: DayState<DynamicSelectionState>, haveTask : Boolean) {
    var modifier = Modifier.fillMaxWidth()

    if (!dayState.isFromCurrentMonth) {
        modifier = modifier.background(Color.Gray)
    }
    else if (haveTask) {
        modifier = modifier.background(Color.Yellow)
    }
    else {
        modifier = modifier.background(Color.White)
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = dayState.date.dayOfMonth.toString(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TaskCalendarMenu(
    modifier : Modifier,
    taskCalendarViewModel: TaskCalendarViewModel = viewModel(factory = ViewModelFactory)
) {
    taskCalendarViewModel.load()
    val setOfTaskDate by taskCalendarViewModel.setOfTaskDate.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SelectableCalendar(
            dayContent = { dayState ->

                DayContent(dayState, setOfTaskDate.contains(TaskDateModel(
                    dayState.date.year,
                    dayState.date.monthValue,
                    dayState.date.dayOfMonth
                )))
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCalendarMenuPreview() {
    TaskCalendarMenu(Modifier)
}