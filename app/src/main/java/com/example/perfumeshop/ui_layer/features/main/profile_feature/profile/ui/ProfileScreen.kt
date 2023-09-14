package com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.utils.OptionType
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(onOptionClick: (OptionType) -> Unit) {

    val isAnonymous = remember {
        mutableStateOf(FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isAnonymous.value)
                NotRegisteredSection(onOptionClick = onOptionClick)
            else
                RegisteredSection(onOptionClick = onOptionClick) { isAnonymous.value = true }

//            ProfileSection(onEditProfileClick = { onOptionClick.invoke(OptionType.Edit) })
//            NotRegisteredSection(onOptionClick = onOptionClick)

        }

    }

}

@Composable
fun RegisteredSection(onOptionClick: (OptionType) -> Unit, onSignOutClick: () -> Unit ) {
    ProfileSection(onEditProfileClick = {
        onOptionClick(OptionType.Edit)
    },
        onSignOutClick = onSignOutClick
    )

    OptionsSection(
        sectionTitle = "Личные данные", listOf(
            Option("Понравившиеся", OptionType.Favourite, null, Icons.Outlined.ArrowForward),
            Option("Заказы", OptionType.Orders, null, Icons.Outlined.ArrowForward)
        ), onOptionClick = onOptionClick
    )

    OptionsSection(
        sectionTitle = "Контакты", listOf(
            Option(
                "Телефон",
                OptionType.PhoneNumber,
                Icons.Default.Phone,
                "+111111111111_link"
            ),
            Option(
                "E-mail",
                OptionType.Gmail,
                Icons.Default.Email,
                "goldpardum@gmail.com_link"
            ),
            Option(
                "Telegram",
                OptionType.Telegram,
                R.drawable.telegram_icon,
                "telegram_link"
            ),
            Option(
                "WhatsApp",
                OptionType.WhatsApp,
                R.drawable.whatsapp_icon,
                "whatsapp_link"
            )
        ), onOptionClick = onOptionClick
    )
}


@Composable
fun NotRegisteredSection(onOptionClick: (OptionType) -> Unit) {


    OptionsSection(
        sectionTitle = "Личные данные", listOf(
            Option(
                "Вход / регистрация",
                OptionType.Auth,
                null,
                Icons.Outlined.ArrowForward
            ),
        ), onOptionClick = onOptionClick
    )

//    OptionsSection(
//        sectionTitle = "Общие настройки", listOf(
//            Option("Тема", OptionType.Theme, null, Icons.Outlined.ArrowForward),
//            Option("О приложении", OptionType.About, null, Icons.Outlined.ArrowForward)
//        ), onOptionClick = onOptionClick
//    )

    OptionsSection(
        sectionTitle = "Контакты", listOf(
            Option(
                "Телефон",
                OptionType.PhoneNumber,
                Icons.Default.Phone,
                "+111111111111_link"
            ),
            Option(
                "E-mail",
                OptionType.Gmail,
                Icons.Default.Email,
                "goldpardum@gmail.com_link"
            ),
            Option(
                "Telegram",
                OptionType.Telegram,
                R.drawable.telegram_icon,
                "telegram_link"
            ),
            Option(
                "WhatsApp",
                OptionType.WhatsApp,
                R.drawable.whatsapp_icon,
                "whatsapp_link"
            )
        ), onOptionClick = onOptionClick
    )
}
