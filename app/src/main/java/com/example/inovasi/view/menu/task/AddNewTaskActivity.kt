package com.example.inovasi.view.menu.task

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovasi.model.FileModel
import com.example.inovasi.ui.theme.InovasiTheme
import com.example.inovasi.viewmodel.ViewModelFactory
import com.google.firebase.firestore.Blob
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class AddNewTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InovasiTheme {
                AddNewTaskActivityRender(
                    getFileName = fun(uri: Uri): String {
                        var fileName = ""
                        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            cursor.moveToFirst()
                            fileName = cursor.getString(nameIndex)
                        }
                        return fileName
                    },

                    readFile = fun(uri: Uri): ByteArray {

                        val byteArrayOutputStream = ByteArrayOutputStream()
                        contentResolver.openInputStream(uri)?.use { inputStream ->
                            val buffer = ByteArray(1024)
                            var length: Int
                            while (inputStream.read(buffer).also { length = it } != -1) {
                                byteArrayOutputStream.write(buffer, 0, length)
                            }
                        }
                        return byteArrayOutputStream.toByteArray()
                    },
                    writeFile = fun(uri : Uri, byteArray : ByteArray) : Unit {
                        contentResolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(byteArray)
                        }
                    },
                    finish = {
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTaskActivityRender(
    addNewTaskViewModel: AddNewTaskViewModel = viewModel(factory = ViewModelFactory),
    getFileName : (uri: Uri) -> String,
    readFile : (uri: Uri) -> ByteArray,
    writeFile : (uri: Uri, byteArray: ByteArray) -> Unit,
    finish : () -> Unit,
) {

    val context = LocalContext.current

    val dateState = rememberDatePickerState()

    val subject by addNewTaskViewModel.subject.collectAsState()
    val description by addNewTaskViewModel.description.collectAsState()
    val date by addNewTaskViewModel.date.collectAsState()
    val reminder by addNewTaskViewModel.reminder.collectAsState()
    val fileData by addNewTaskViewModel.fileData.collectAsState()
    val selectedDownload by addNewTaskViewModel.selectedDownload.collectAsState()


    val uploadLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data?.data
            uri?.let {
                val fileName = getFileName(it)
                val byteArray = readFile(it)

                val newFileDate = ArrayList(fileData)
                newFileDate.add(FileModel(fileName, Blob.fromBytes(byteArray)))
                addNewTaskViewModel.setFileData(newFileDate)
            }
        }
    }

    val downloadLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let { it ->
                selectedDownload?.let { ti ->
                    writeFile(it, ti.data.toBytes())
                }
            }
            addNewTaskViewModel.setFileModelDownload(null)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Add New Task",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Column(
            modifier = Modifier
        ) {
            TextField(
                value = subject,
                onValueChange = {
                    addNewTaskViewModel.setSubject(it)
                },
                label = {
                    Text("Subject")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            TextField(
                value = description,
                onValueChange = {
                    addNewTaskViewModel.setDescription(it)
                },
                label = {
                    Text("Description")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            DateTimePicker(
                onChange = { date, time ->
                    addNewTaskViewModel.setDate(
                        Date.from(
                            LocalDateTime.of(
                                date.year,
                                date.month,
                                date.day,
                                time.hour,
                                time.minute,
                                0
                            ).atZone(
                                ZoneId.systemDefault()
                            ).toInstant()
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = reminder.toString(),
                onValueChange = {
                    try {
                        addNewTaskViewModel.setReminder(it.toInt())
                    }
                    catch (_ : Exception) {
                        addNewTaskViewModel.setReminder(0)
                    }
                },
                label = {
                    Text("Reminder in minute")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "File",
                    modifier = Modifier.
                        weight(1f)
                )

                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "*/*"
                            addCategory(Intent.CATEGORY_OPENABLE)
                        }

                        uploadLauncher.launch(intent)
                    },

                ) {
                    Text(
                        text = "Upload File"
                    )
                }
            }
            LazyColumn {
                items(fileData.size) { i ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = fileData[i].title,
                            modifier = Modifier.
                            weight(1f)
                        )

                        TextButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                    type = "*/*"
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    putExtra(Intent.EXTRA_TITLE, fileData[i].title) // Set the desired filename here
                                }

                                addNewTaskViewModel.setFileModelDownload(fileData[i])
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

        Spacer(modifier = Modifier.height(24.dp))

        Button (
            onClick = {
                addNewTaskViewModel.save(context)
                finish()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddNewTaskActivityRenderPreview() {
    InovasiTheme {
        AddNewTaskActivityRender(
            readFile = fun(_ : Uri) : ByteArray{
                return ByteArray(1)
            },
            getFileName = fun(_ : Uri) : String{
                return ""
            },
            writeFile = fun(_ : Uri, _ : ByteArray) : Unit {

            },
            finish = fun() : Unit {

            }
        )
    }
}