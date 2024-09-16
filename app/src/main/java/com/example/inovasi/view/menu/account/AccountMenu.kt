package com.example.inovasi.view.menu.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inovasi.R
import com.example.inovasi.model.AccountModel
import com.example.inovasi.viewmodel.AccountViewModel
import com.example.inovasi.viewmodel.ViewModelFactory

@Composable
fun AccountMenu(
    modifier : Modifier,
    accountMenuViewModel : AccountMenuViewModel = viewModel(factory = ViewModelFactory),
    accountViewModel: AccountViewModel = viewModel(factory = ViewModelFactory)
) {

    val account by accountViewModel.accountModel.collectAsState()
    val dialogData by accountMenuViewModel.dialogData.collectAsState()

    dialogData?.let {
        UpdateAccountDialog(
            it.title,
            it.inputTitle,
            it.visualTransformation,
            it.value,
            it.onDismissRequest,
            it.onConfirmation
        )
    }


    val editNameTitle = stringResource(R.string.edit_name)
    val editNameInputTitle = stringResource(R.string.name)

    val editPasswordTitle = stringResource(R.string.edit_password)
    val editPasswordInputTitle = stringResource(R.string.password)


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.name) + ": ${account?.name}",
                modifier = Modifier
                    .weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Button(
                modifier = Modifier,
                enabled = account != null,
                onClick = {
                    account?.name?.let {
                        accountMenuViewModel.setDialogData(
                            DialogData(
                                editNameTitle,
                                editNameInputTitle,
                                it,
                                VisualTransformation.None,
                                fun() {
                                    accountMenuViewModel.setDialogData(null)
                                },
                                fun(newName: String) {
                                    account?.let { ti ->
                                        accountViewModel.setAccount(
                                            AccountModel(
                                            ti.id,
                                            newName,
                                            ti.email)
                                        )
                                    }
                                    accountMenuViewModel.setDialogData(null)
                                }
                            )
                        )
                    }
                },
            ) {
                Text(
                    text = stringResource(R.string.edit),
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.email) + ": ${account?.email}",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier,
            enabled = account != null,
            onClick = {
                accountViewModel.sendResetPasswordEmail()
            }
        ) {
            Text(
                stringResource(R.string.reset_password),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountMenuPreview() {
    AccountMenu(Modifier)
}