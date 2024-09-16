package com.example.inovasi.view.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovasi.R
import com.example.inovasi.view.menu.account.AccountMenu
import com.example.inovasi.view.menu.gallery.GalleryMenu
import com.example.inovasi.view.menu.gallery.getFileName
import com.example.inovasi.view.menu.home.TaskListMenu
import com.example.inovasi.view.menu.task.AddNewTaskActivity
import com.example.inovasi.view.menu.task.TaskCalendarMenu
import com.example.inovasi.view.ui.theme.InovasiTheme
import com.example.inovasi.viewmodel.ViewModelFactory
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InovasiTheme {
                HomeActivityRender()
            }
        }
    }
}

@Composable
fun HomeActivityRender(homeViewModel: MenuViewModel = viewModel(factory = ViewModelFactory)) {

    val page by homeViewModel.page.collectAsState()
    val context = LocalContext.current

    val (loadFile, setLoadFile) = remember { mutableStateOf(false) }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        thread {
            setLoadFile(true)
            for(uri in uris) {
                try {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    context.contentResolver.openInputStream(uri)?.use {inputStream ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } != -1) {
                            byteArrayOutputStream.write(buffer, 0, length)
                        }
                    }

                    val fileName = getFileName(context, uri)

                    context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                        it.write(byteArrayOutputStream.toByteArray())
                    }
                }
                catch (_ : Exception) {}
            }
            setLoadFile(false)
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                NavigationBarItem(
                    modifier = Modifier.padding(8.dp),
                    icon = {
                        Icon(
                            painterResource(
                                id = R.drawable.home_24
                            ),
                            contentDescription = "Home",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.fillMaxSize())
                    },
                    selected = false,
                    onClick = {
                        homeViewModel.setPage(MenuViewModel.Page.HOME)
                    }
                )
                NavigationBarItem(
                    modifier = Modifier.padding(8.dp),
                    icon = {
                        Icon(
                            painterResource(
                                id = R.drawable.add_circle_24
                            ),
                            contentDescription = "Task",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.fillMaxSize())
                    },
                    selected = false,
                    onClick = {
                        homeViewModel.setPage(MenuViewModel.Page.TASK)
                    }
                )
                NavigationBarItem(
                    modifier = Modifier.padding(8.dp),
                    icon = {
                        Icon(
                            painterResource(
                                id = R.drawable.photo_24
                            ),
                            contentDescription = "Gallery",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.fillMaxSize())
                    },
                    selected = false,
                    onClick = {
                        homeViewModel.setPage(MenuViewModel.Page.GALLERY)
                    }
                )
                NavigationBarItem(
                    modifier = Modifier.padding(8.dp),
                    icon = {
                        Icon(
                            painterResource(
                                id = R.drawable.person_24
                            ),
                            contentDescription = "Account",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.fillMaxSize())
                    },
                    selected = false,
                    onClick = {
                        homeViewModel.setPage(MenuViewModel.Page.ACCOUNT)
                    }
                )
            }
        },
        floatingActionButton = {
            if (page == MenuViewModel.Page.TASK) {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(context, AddNewTaskActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(Icons.Filled.Add, "add")
                }
            }
            else if (page == MenuViewModel.Page.GALLERY) {
                FloatingActionButton(
                    onClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                    }
                ) {
                    Icon(Icons.Filled.Add, "add")
                }
            }

        }
    ) { innerPadding ->
        when (page) {
            MenuViewModel.Page.HOME -> TaskListMenu(Modifier.padding(innerPadding))
            MenuViewModel.Page.TASK -> TaskCalendarMenu(Modifier.padding(innerPadding))
            MenuViewModel.Page.GALLERY -> GalleryMenu(Modifier.padding(innerPadding), loadFile)
            MenuViewModel.Page.ACCOUNT -> AccountMenu(Modifier.padding(innerPadding))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeActivityRenderPreview() {
    InovasiTheme {
        HomeActivityRender()
    }
}