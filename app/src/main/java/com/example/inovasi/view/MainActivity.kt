package com.example.inovasi.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovasi.R
import com.example.inovasi.ui.theme.InovasiTheme
import com.example.inovasi.view.menu.HomeActivity
import com.example.inovasi.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (!isGranted) {
                    Toast.makeText(this, "Please enable this permission: $permissionName to run the application", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionList = ArrayList<String>()
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }


        createNotificationChannel(this)


        enableEdgeToEdge()
        setContent {
            InovasiTheme {
                MainActivityRender()
                if (permissionList.isNotEmpty()) {
                    AskPermissionDialog(
                        onConfirmation = {
                            requestPermissionLauncher.launch(permissionList.toTypedArray())
                        }
                    )
                }
            }
        }
    }
}

fun createNotificationChannel(context: Context) {
    try {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val channelId = "Notification"
            val channelName = "Notification"

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)

            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    catch (_ : Exception) {}
}

@Composable
fun AskPermissionDialog(
    onConfirmation: (() -> Unit)? = null,
) {
    var show by remember { mutableStateOf(true) }

    if (show) {
        Dialog(
            onDismissRequest = {
                show = false
            }
        ) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Please enable all the required permission to make the app running smoothly")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                onConfirmation?.let {
                                    it()
                                }
                                show = false
                            },
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text("OK")
                        }

                        TextButton(
                            onClick = {
                                show = false
                            },
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text("NO")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainActivityRender(authenticationViewModel: AuthenticationViewModel = viewModel(factory = ViewModelFactory)) {

    val account by authenticationViewModel.user.collectAsState()

    val email by authenticationViewModel.email.collectAsState()
    val password by authenticationViewModel.password.collectAsState()
    val loading by authenticationViewModel.process.collectAsState()

    val context = LocalContext.current

    if (account != null) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.authentication),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Column(
            modifier = Modifier
                .widthIn(max = 240.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    authenticationViewModel.setEmail(it)
                },
                label = {
                    Text(stringResource(R.string.email))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = {
                    authenticationViewModel.setPassword(it)
                },
                label = {
                    Text(stringResource(R.string.password))
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button (
                onClick = {
                    authenticationViewModel.login(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(stringResource(R.string.login))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "-- or --",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))


            Button (
                onClick = {
                    authenticationViewModel.register(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(stringResource(R.string.register))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityRenderPreview() {
    InovasiTheme {
        MainActivityRender()
    }
}