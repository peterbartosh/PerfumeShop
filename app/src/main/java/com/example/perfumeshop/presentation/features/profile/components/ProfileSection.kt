package com.example.perfumeshop.presentation.features.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.user.UserData
import com.example.perfumeshop.data.utils.firstLetterToUpperCase
import com.example.perfumeshop.data.utils.isUserConnected
import com.example.perfumeshop.data.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@Composable
fun ProfileSection(
    userData: UserData,
    onEditProfileClick: () -> Unit = {},
    onSignOutClick: suspend () -> Unit
) {

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    val notSpecified = "Не указано"
    val shortNotSpecified = "_"

    val firstName = userData.user?.firstName ?: notSpecified
    val secondName = userData.user?.secondName ?: notSpecified
    val phoneNumber = userData.user?.phoneNumber ?: notSpecified
    val address = listOf(
        userData.user?.city?.firstLetterToUpperCase() ?: notSpecified,
        userData.user?.street?.firstLetterToUpperCase() ?: notSpecified,
        userData.user?.home ?: shortNotSpecified,
        userData.user?.flat ?: shortNotSpecified
    ).joinToString(", ") { it }



    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .padding(top = 10.dp, bottom = 2.dp)
            //.clip(RoundedCornerShape(30.dp))
            .shadow(elevation = 15.dp, shape = RoundedCornerShape(30.dp), clip = true)
            .clickable { onEditProfileClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        //elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.Bottom
        ) {

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(0.75f)
                    .padding(start = 20.dp, top = 10.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "${secondName.firstLetterToUpperCase()} ${firstName.firstLetterToUpperCase()}",
                    style = MaterialTheme.typography.titleSmall,
                    //fontSize = 17.sp,
                    //fontWeight = FontWeight.Bold, modifier = modifier
                )

                Spacer(modifier = Modifier.height(5.dp))

                Divider()

                Spacer(modifier = Modifier.height(5.dp))


                Text(
                    text = phoneNumber,
                    style = MaterialTheme.typography.bodyLarge
                    //fontSize = 17.sp,
                    //fontWeight = FontWeight.Bold, modifier = modifier
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyLarge
                    //fontSize = 17.sp,
                    //fontWeight = FontWeight.Bold, modifier = modifier
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .size(25.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sign_out),
                        contentDescription = "sign out icon"
                    )
                }
            }
        }

        if (showDialog)
            AlertDialog(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(10.dp)
                    ),
                containerColor = MaterialTheme.colorScheme.background,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {

                        Text(
                            text = "Выход из аккаунта",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = context.getString(R.string.are_you_sure_you_want_sign_out),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                onDismissRequest = { showDialog = false },
                tonalElevation = 10.dp,
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.background,
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = "Нет", style = MaterialTheme.typography.bodyMedium)
                    }
                },
                confirmButton = {
                    var clicked by remember {
                        mutableStateOf(false)
                    }
                    if (!clicked)
                        Button(
                            onClick = {
                                if (!isUserConnected(context)){
                                    context.showToast(R.string.connection_lost_error)
                                    return@Button
                                } else {
                                    CoroutineScope(Job()).launch {
                                        clicked = true
                                        onSignOutClick()
                                        showDialog = false
                                        clicked = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.background,
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = "Да", style = MaterialTheme.typography.bodyMedium)
                        }
                    else
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                }
            )
    }
}
