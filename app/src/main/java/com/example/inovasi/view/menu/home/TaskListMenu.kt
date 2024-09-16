package com.example.inovasi.view.menu.home

import android.content.Intent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovasi.R
import com.example.inovasi.view.menu.task.TaskListActivity
import com.example.inovasi.viewmodel.ViewModelFactory

@Composable
fun TaskListMenu(
    modifier : Modifier,
    taskListViewModel: TaskListViewModel = viewModel(factory = ViewModelFactory)
) {

    val context = LocalContext.current

    taskListViewModel.load(context)

    val pendingTask by taskListViewModel.pendingTaskModelList.collectAsState()
    val ongoingTask by taskListViewModel.ongoingTaskModelList.collectAsState()
    val doneTask by taskListViewModel.doneTaskModelList.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(16.dp, 24.dp, 16.dp, 16.dp)
            .verticalScroll(ScrollState(0))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.pending),
                modifier = Modifier
                    .weight(1f),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                modifier = Modifier,
                onClick = {
                    val intent = Intent(context, TaskListActivity::class.java)
                    intent.putExtra("status", "pending")
                    context.startActivity(intent)
                }
            ) {
                Text(
                    stringResource(R.string.view),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            for(item in pendingTask) {
                TaskCard(item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.on_progress),
                modifier = Modifier
                    .weight(1f),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                modifier = Modifier,
                onClick = {
                    val intent = Intent(context, TaskListActivity::class.java)
                    intent.putExtra("status", "ongoing")
                    context.startActivity(intent)
                }
            ) {
                Text(
                    stringResource(R.string.view),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            for(item in ongoingTask) {
                TaskCard(item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.done),
                modifier = Modifier
                    .weight(1f),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                modifier = Modifier,
                onClick = {
                    val intent = Intent(context, TaskListActivity::class.java)
                    intent.putExtra("status", "done")
                    context.startActivity(intent)
                }
            ) {
                Text(
                    stringResource(R.string.view),
                    fontWeight = FontWeight.Bold
                )
            }
        }


        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            for(item in doneTask) {
                TaskCard(item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListMenuPreview() {
    TaskListMenu(Modifier)
}