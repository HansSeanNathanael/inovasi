package com.example.inovasi.view.menu.home

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inovasi.model.FileModel
import com.example.inovasi.model.TaskModel
import com.example.inovasi.view.menu.task.AddNewTaskActivity
import com.example.inovasi.view.menu.task.InspectTaskActivity

@Composable
fun TaskCard(taskModel : TaskModel) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = taskModel.subject,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = taskModel.date.toString(),
                textAlign = TextAlign.End
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = taskModel.description,
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            )  {
                for (item in taskModel.fileData) {
                    TaskFile(item)
                }
            }

            TextButton(
                onClick = {
                    val intent = Intent(context, InspectTaskActivity::class.java)
                    intent.putExtra("id", taskModel.id)
                    context.startActivity(intent)
                }
            ) {
                Text("Inspect")
            }
        }
    }
}

@Composable
fun TaskFile(fileModel : FileModel) {

    val context  = LocalContext.current

    val downloadLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also { uri ->
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(fileModel.data.toBytes())
                }
            }
        }
    }

    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        type = "*/*"
        addCategory(Intent.CATEGORY_OPENABLE)
        putExtra(Intent.EXTRA_TITLE, fileModel.title) // Set the desired filename here
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = fileModel.title,
            modifier = Modifier
                .weight(1f)
        )
        TextButton(
            onClick = {
                downloadLauncher.launch(intent)
            },
            modifier = Modifier
                .padding(8.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Text("download")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
//    TaskCard()
}