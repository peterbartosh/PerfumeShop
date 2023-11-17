package com.example.perfumeshop.presentation.features.main.profile_feature.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.data.utils.isUserConnected
import com.example.perfumeshop.presentation.components.showToast


@Composable
fun ProfileScreen(
    onOptionClick: (OptionType) -> Unit,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current

    var isAnonymous by remember {
        mutableStateOf(profileViewModel.auth.currentUser?.isAnonymous == true)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isAnonymous)
            NotRegisteredSection(onOptionClick = onOptionClick)
        else
            RegisteredSection(
                profileViewModel = profileViewModel,
                onOptionClick = {optionType ->
                    if (!isUserConnected(context)){
                        context.showToast(R.string.connection_lost_error)
                        return@RegisteredSection
                    }
                    onOptionClick(optionType)
                },
                onSignOutClick = {

                    profileViewModel.userData.clearUserData().join()
                    profileViewModel.dataManager.saveProductsToRemote()
                    profileViewModel.auth.signOut()
                    profileViewModel.auth.signInAnonymously()

                    isAnonymous = true
                }
            )
    }
}

@Composable
fun RegisteredSection(
    profileViewModel: ProfileViewModel,
    onOptionClick: (OptionType) -> Unit,
    onSignOutClick: suspend () -> Unit
) {

    val context = LocalContext.current

    ProfileSection(
        profileViewModel = profileViewModel,
        onEditProfileClick = { onOptionClick(OptionType.Edit) },
        onSignOutClick = onSignOutClick
    )

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn{
        item(key = 0) {
            OptionsSection(
                sectionTitle = "Личные данные", listOf(
                    Option(
                        "Избранное",
                        OptionType.Favourite,
                        Icons.Default.Favorite,
                        Icons.Outlined.ArrowForward
                    ),
                    Option(
                        "Заказы",
                        OptionType.Orders,
                        R.drawable.order_icon,
                        Icons.Outlined.ArrowForward
                    )
                ), onOptionClick = onOptionClick
            )
        }

        item(key = 1) {
            OptionsSection(
                sectionTitle = "Контакты", listOf(
                    Option(
                        "Телефон",
                        OptionType.PhoneNumber,
                        Icons.Default.Phone,
                        context.getString(R.string.phone_number)
                    ),
                    Option(
                        "E-mail",
                        OptionType.Gmail,
                        Icons.Default.Email,
                        context.getString(R.string.email)
                    ),
                    Option(
                        "Telegram",
                        OptionType.Telegram,
                        R.drawable.telegram_icon,
                        context.getString(R.string.telegram)
                    ),
                    Option(
                        "WhatsApp",
                        OptionType.WhatsApp,
                        R.drawable.whatsapp_icon,
                        context.getString(R.string.whatsapp)
                    ),
                    Option(
                        "Веб-сайт",
                        OptionType.WebSite,
                        R.drawable.website_icon,
                        context.getString(R.string.website)
                    )

                ), onOptionClick = onOptionClick
            )
        }
    }
}


@Composable
fun NotRegisteredSection(onOptionClick: (OptionType) -> Unit) {


    OptionsSection(
        sectionTitle = "Личные данные", listOf(
            Option(
                "Вход / регистрация",
                OptionType.Auth,
                Icons.Default.AccountBox,
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
