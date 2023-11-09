package com.example.perfumeshop.presentation.features.main.profile_feature.profile.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.data.utils.firstLetterToUpperCase
import com.example.perfumeshop.data.utils.isUserConnected
import com.example.perfumeshop.presentation.components.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.StringJoiner

const val TAG = "ProfileComponents"

fun composeIntent(context: Context, optionType : OptionType, optionData : String) {

    try {
        when (optionType){

            OptionType.Gmail -> {
                ContextCompat.startActivity(context, Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$optionData")
                }, null)

            }
            OptionType.Telegram -> {
                ContextCompat.startActivity(context, Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("tg://openmessage?user_id=270838226")
                }, null)
            }

            OptionType.WhatsApp -> {
                ContextCompat.startActivity(context, Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://api.whatsapp.com/send?phone=$$optionData")
                }, null)
            }

            OptionType.PhoneNumber -> {
                ContextCompat.startActivity(context, Intent.createChooser(Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$optionData")
                }, ""), null)
            }

            else -> {}
        }
    } catch (e : Exception){
        showToast(context, "Приложение не установлено")
        Log.d(TAG, "composeIntent: ${e.message}")
    }
}

data class Option(val title : String, val type : OptionType, val leadingIcon : Any?, val content : Any?)

@Composable
fun OptionRow(option : Option, onOptionClick: (OptionType) -> Unit) {

    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current

    Card(modifier = Modifier
        .fillMaxWidth(0.95f)
        .wrapContentHeight()
        .padding(start = 5.dp, end = 5.dp, top = 2.dp, bottom = 2.dp)
        .clip(RoundedCornerShape(10.dp))
        //.border(width = 1.dp, color = Color.Black)
        .clickable {
            onOptionClick(option.type)
            composeIntent(context, option.type, option.content.toString())
        },
        //.shadow(elevation = 3.dp, shape = RoundedCornerShape(5.dp))
         colors = CardDefaults.cardColors(
             containerColor = MaterialTheme.colorScheme.primaryContainer,
             contentColor = MaterialTheme.colorScheme.onBackground
         ),
         //shape = RoundedCornerShape(3.dp),
         border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(all = 10.dp)
        ) {


                Row(
                    modifier = Modifier.padding(start = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    when (option.leadingIcon) {
                        null -> {
                            Box(modifier = Modifier.size(25.dp))
                        }
                        is ImageVector -> Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = option.leadingIcon,
                            contentDescription = "icon"
                        )

                        is Int -> Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(id = option.leadingIcon),
                            contentDescription = "icon"
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(text = option.title, style = MaterialTheme.typography.bodyMedium)
                }


            if (option.content != null)
                when (option.content) {
                    is ImageVector -> Icon(
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 5.dp),
                        imageVector = option.content,
                        contentDescription = "arrow front"
                    )

                    is String -> Text(
                        text = option.content,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        //fontSize = 12.sp,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .clickable {
                                clipboardManager.setText(AnnotatedString(option.content))
                                showToast(context, "Скопировано в буфер")
                            }
                    )
                }

        }
    }
}


@Composable
fun OptionsSection(
    sectionTitle : String = "Личные данные",
    options : List<Option> =  listOf(
        Option("Вход / регистрация", OptionType.Edit, null, Icons.Outlined.ArrowForward),
        Option("Понравившиеся", OptionType.Favourite, null, Icons.Outlined.ArrowForward),
        Option("Заказы", OptionType.Orders, null, Icons.Outlined.ArrowForward)
    ),
    onOptionClick: (OptionType) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Divider()

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = sectionTitle, style = MaterialTheme.typography.labelMedium)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(top = 10.dp)
        ) {

            for (option in options)
                OptionRow(option = option , onOptionClick = onOptionClick)

        }
    }
}

@Composable
fun ProfileSection(
    profileViewModel: ProfileViewModel,
    onEditProfileClick: () -> Unit = {},
    onSignOutClick: suspend () -> Unit
) {

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    val notSpecified = "Не указано"
    val shortNotSpecified = "_"

    val firstName = profileViewModel.userData.user?.firstName ?: notSpecified
    val secondName = profileViewModel.userData.user?.secondName ?: notSpecified
    val phoneNumber = profileViewModel.userData.user?.phoneNumber ?: notSpecified
    val address = StringJoiner(", ")
        .add(profileViewModel.userData.user?.street?.firstLetterToUpperCase() ?: notSpecified)
        .add((profileViewModel.userData.user?.home ?: shortNotSpecified))
        .add((profileViewModel.userData.user?.flat ?: shortNotSpecified))
        .toString()


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

                val modifier = Modifier.padding(start = 10.dp)

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
                            text = "Вы уверены, что хотите выйти из учётной записи?",
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
                                    showToast(context, "Ошибка.\nВы не подключены к сети.")
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
