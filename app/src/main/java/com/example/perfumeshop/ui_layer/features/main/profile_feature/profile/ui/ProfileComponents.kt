package com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.utils.OptionType
import com.google.firebase.auth.FirebaseAuth


data class Option(val title : String, val type : OptionType, val leadingIcon : Any?, val content : Any?)

@Composable
fun OptionRow(option : Option = Option(
    "Телефон",
    OptionType.PhoneNumber,
    Icons.Default.Phone,
    "+111111111111_link"),
              onOptionClick: (OptionType) -> Unit
) {

    Card(modifier = Modifier
        .fillMaxWidth(0.9f)
        .height(35.dp)
        .padding(top = 2.dp, bottom = 2.dp)
        // .border(width = 2.dp, color = Color.Black)
        .clickable { onOptionClick(option.type) },
        //.shadow(elevation = 3.dp, shape = RoundedCornerShape(5.dp))
        //.padding(start = 10.dp, end = 10.dp),
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
                                          .clickable {})
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
    )
    , onOptionClick: (OptionType) -> Unit
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

    val curUser = FirebaseAuth.getInstance().currentUser

    val displayNameState = remember {
        mutableStateOf(curUser?.displayName?.split("|")?.first() ?: "Аноним")
    }

    val sexStr =
        curUser?.displayName?.split(" ")?.last()?.split("|")?.last()

    val sexState = remember {
        mutableStateOf(if (sexStr.isNullOrEmpty()) 2 else sexStr.toInt())
    }

    val phoneNumberState = remember {
        mutableStateOf(curUser?.phoneNumber ?: "")
    }

    Card(modifier = Modifier
        .fillMaxWidth(0.9f)
        .height(150.dp)
        .padding(top = 2.dp, bottom = 2.dp)
        // .border(width = 2.dp, color = Color.Black)
        .clickable { onEditProfileClick() },
        //.shadow(elevation = 3.dp, shape = RoundedCornerShape(5.dp))
        //.padding(start = 10.dp, end = 10.dp),
         colors = CardDefaults.cardColors(containerColor = Color.White),
         shape = RoundedCornerShape(3.dp),
         border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize(),
               horizontalAlignment = Alignment.CenterHorizontally) {


            Text(text = displayNameState.value, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Пол: ${sexes[sexState.value]}", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = phoneNumberState.value, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.End) {
                    IconButton(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .size(30.dp)
                            .shadow(elevation = 3.dp, shape = CircleShape)
                    ) {
                        Icon(painter = painterResource(id = R.drawable.sign_out), contentDescription = "sign out icon")
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
                            FirebaseAuth.getInstance().signOut()
                            FirebaseAuth.getInstance().signInAnonymously()
                            onSignOutClick()
                            showDialog = false
                        }) {
                            Text(text = "Да")
                        }
                })

        }
    }
}
