package com.example.inovasi.view.menu.task

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovasi.model.FileModel
import com.example.inovasi.model.TaskModel
import com.example.inovasi.view.menu.task.ui.theme.InovasiTheme
import com.example.inovasi.viewmodel.ViewModelFactory
import com.google.firebase.firestore.Blob
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.concurrent.TimeUnit

class InspectTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getStringExtra("id")

        if (id == null) {
            finish()
        }

        enableEdgeToEdge()
        setContent {
            InovasiTheme {
                InspectTaskActivityRender(
                    writeFile = fun(uri : Uri, byteArray : ByteArray) : Unit {
                        contentResolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(byteArray)
                        }
                    },
                    id = id!!
                )
            }
        }
    }
}

@Composable
fun InspectTaskActivityRender(
    inspectTaskViewModel: InspectTaskViewModel = viewModel(factory = ViewModelFactory),
    writeFile: ((uri : Uri, byteArray : ByteArray) -> Unit)? = null,
    id: String,
) {
    val context = LocalContext.current

    inspectTaskViewModel.load(id)

    val taskModel by inspectTaskViewModel.taskModel.collectAsState()
    val selectedDownload by inspectTaskViewModel.selectedDownload.collectAsState()

    val downloadLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let { it ->
                selectedDownload?.let { ti ->
                    if (writeFile != null) {
                        writeFile(it, ti.data.toBytes())
                    }
                }
            }
            inspectTaskViewModel.setFileModelDownload(null)
        }
    }


    val date = taskModel?.date
    val localDateTime: LocalDateTime? = taskModel?.date?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()

    val dateTime = if (localDateTime != null) {
        "${localDateTime.dayOfMonth}/${localDateTime.monthValue}/${localDateTime.year} - ${localDateTime.hour}:${localDateTime.minute}"
    } else {
        ""
    }

    val reminderDate = taskModel?.reminder
    val reminder = if (date != null && reminderDate != null) {
        TimeUnit.MILLISECONDS.toMinutes(date.time - reminderDate.time)
    }
    else {
        0
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Inspect Task",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Column(
            modifier = Modifier
        ) {

            Text(
                text = "Subject : ${taskModel?.subject}",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Description : ${taskModel?.description}",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = dateTime,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Reminder : ${reminder} minutes",
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status : ${taskModel?.status}",
                    modifier = Modifier
                        .weight(1f)
                )

                taskModel?.let {
                    if (it.status == "ongoing") {
                        TextButton(
                            onClick = {
                                taskModel?.let {
                                    inspectTaskViewModel.setTaskModel(
                                        TaskModel(
                                            it.id,
                                            it.subject,
                                            it.description,
                                            it.date,
                                            "done",
                                            it.reminder,
                                            it.fileData
                                        )
                                    )
                                    inspectTaskViewModel.save(context)
                                }
                            }
                        ) {
                            Text("Finish")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "File",
                modifier = Modifier
                    .fillMaxWidth()
            )

            taskModel?.let {
                LazyColumn {
                    items(it.fileData.size) { i ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = it.fileData[i].title,
                                modifier = Modifier.
                                weight(1f)
                            )

                            TextButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                        type = "*/*"
                                        addCategory(Intent.CATEGORY_OPENABLE)
                                        putExtra(Intent.EXTRA_TITLE, it.fileData[i].title) // Set the desired filename here
                                    }

                                    inspectTaskViewModel.setFileModelDownload(it.fileData[i])
                                    downloadLauncher.launch(intent)
                                }
                            ) {
                                Text(
                                    text = "Download"
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InovasiTheme {
        InspectTaskActivityRender(id = "")
    }
}