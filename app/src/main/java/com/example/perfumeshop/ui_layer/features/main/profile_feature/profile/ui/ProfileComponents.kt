package com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.ui_layer.components.showToast
import com.google.firebase.auth.FirebaseAuth


fun Intent.block(optionType: OptionType
                 //, pi : PackageInfo
){

}

//fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo =
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
//    } else {
//        @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
//    }

fun composeIntent(context: Context, optionType : OptionType, optionData : String) {
    //    val mailAddress = "goldappsender@gmail.com"
//    val telegramUserId = "270838226"
//    val whatsappNumber = "+74951506463"
//    val phoneNumber = "+375445754325"
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
        Log.d("ERROR_ERROR", "composeIntent: ${e.message}")
    }
}

data class Option(val title : String, val type : OptionType, val leadingIcon : Any?, val content : Any?)

@Composable
fun OptionRow(option : Option, onOptionClick: (OptionType) -> Unit) {

    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current

    Card(modifier = Modifier
        .fillMaxWidth(0.9f)
        .height(35.dp)
        .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
        //.border(width = 1.dp, color = Color.Black)
        .clickable {
            onOptionClick(option.type)
            composeIntent(context, option.type, option.content.toString())
        },
        //.shadow(elevation = 3.dp, shape = RoundedCornerShape(5.dp))
         colors = CardDefaults.cardColors(containerColor = Color.White),
         shape = RoundedCornerShape(3.dp),
         border = BorderStroke(1.dp, Color.Black)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {


            if (option.leadingIcon != null)
                Row(modifier = Modifier.padding(start = 5.dp)) {
                    if (option.leadingIcon is ImageVector)
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = option.leadingIcon,
                            contentDescription = "icon"
                        )
                    else if (option.leadingIcon is Int)
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = option.leadingIcon),
                            contentDescription = "icon"
                        )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(text = option.title, fontSize = 15.sp)
                }
            else
                Text(text = option.title, fontSize = 15.sp,
                     modifier = Modifier.padding(start = 5.dp))

            if (option.content != null)
                when (option.content) {
                    is ImageVector -> Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 5.dp),
                        imageVector = option.content,
                        contentDescription = "arrow front"
                    )

                    is String -> Text(text = option.content,
                                      fontSize = 12.sp,
                                      modifier = Modifier
                                          .padding(end = 5.dp)
                                          .clickable {
                                              clipboardManager.setText(AnnotatedString(option.content))
                                              showToast(context, "Скопировано в буфер")
                                          })
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

    Column(modifier = Modifier
        .padding(10.dp)
        .wrapContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = sectionTitle, fontSize = 25.sp)
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
fun ProfileSection(onEditProfileClick: () -> Unit = {}, onSignOutClick: () -> Unit) {

    var showDialog by remember { mutableStateOf(false) }

    val sexes = listOf("Мужской", "Женский", "Не указано")

    val sep = ","
    val notSpecified = "Не указано"

    val curUser = FirebaseAuth.getInstance().currentUser

    val firstName = curUser?.displayName?.split(sep)?.get(0) ?: notSpecified
    val secondName = curUser?.displayName?.split(sep)?.get(1) ?: notSpecified
    val sexInd = curUser?.displayName?.split(sep)?.get(2)?.let { if (it.isEmpty()) 2 else it.toInt() } ?: 2
    val phoneNumber = curUser?.phoneNumber ?: notSpecified
    val address = curUser?.displayName?.split(sep)?.filterIndexed{ i, _ -> i > 2}?.joinToString(separator = ", ") ?: notSpecified

//    val displayNameState = remember {
//        mutableStateOf("$firstName $secondName")
//    }
//
//    val sexState = remember {
//        mutableStateOf(if (sex.isNullOrEmpty()) 2 else sex.toInt())
//    }
//
//    val phoneNumberState = remember {
//        mutableStateOf(curUser?.phoneNumber ?: notSpecified)
//    }

    Card(modifier = Modifier
        .fillMaxWidth(0.9f)
        .height(150.dp)
        .padding(top = 10.dp, bottom = 2.dp)
        // .border(width = 2.dp, color = Color.Black)
        .clickable { onEditProfileClick() },
        //.shadow(elevation = 3.dp, shape = RoundedCornerShape(5.dp))
        //.padding(start = 10.dp, end = 10.dp),
         colors = CardDefaults.cardColors(containerColor = Color.White),
         shape = RoundedCornerShape(3.dp),
         border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f),
                horizontalAlignment = Alignment.Start
            ) {

                val modifier = Modifier.padding(start = 10.dp)

                Text(
                    text = "Инициалы: $firstName $secondName", fontSize = 17.sp,
                    fontWeight = FontWeight.Bold, modifier = modifier
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Пол: ${sexes[sexInd]}", fontSize = 17.sp,
                    fontWeight = FontWeight.Bold, modifier = modifier
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Номер: $phoneNumber", fontSize = 17.sp,
                    fontWeight = FontWeight.Bold, modifier = modifier
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Адрес: $address", fontSize = 17.sp,
                    fontWeight = FontWeight.Bold, modifier = modifier
                )
            }

            Column(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
                   verticalArrangement = Arrangement.Bottom,
                   horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .size(25.dp)
                        .border(width = 1.dp, color = Color.Black, shape = CircleShape)
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
                    title = { Text(text = "Выход из аккаунта") },
                    text = { Text(text = "Вы уверены, что хотите выйти из учётной записи?") },
                    onDismissRequest = {showDialog = false},
                    tonalElevation = 10.dp,
                    dismissButton = {
                        Button(onClick = {
                            showDialog = false
                        }) {
                            Text(text = "Нет")
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            onSignOutClick()
                            showDialog = false
                        }) {
                            Text(text = "Да")
                        }
                })

        }
    }
