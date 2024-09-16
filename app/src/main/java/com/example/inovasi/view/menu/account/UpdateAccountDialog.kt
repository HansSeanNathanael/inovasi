package com.example.inovasi.view.menu.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.inovasi.R

@Composable
fun UpdateAccountDialog(
    title : String,
    inputTitle : String,
    visualTransformation : VisualTransformation,
    value : String,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {

    val accountViewModel = UpdateAccountViewModel(value)

    val name by accountViewModel.name.collectAsState()

    Dialog (
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        accountViewModel.setValue(it)
                    },
                    label = {
                        inputTitle
                    },
                    visualTransformation = visualTransformation,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            onConfirmation(name)
                        },
                        modifier = Modifier.padding(8.dp),

                    ) {
                        Text(stringResource(R.string.save))
                    }
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateAccountDialogPreview() {
    UpdateAccountDialog("Title", "Input Box Title", VisualTransformation.None,  "Joko", fun(): Unit {}, fun(_: String): Unit {})
}

