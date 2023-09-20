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
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(
    onOptionClick: (OptionType) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

//  FirebaseAuth.getInstance().signOut()
//    FirebaseAuth.getInstance().signInAnonymously()


    val isAnonymous = remember {
        mutableStateOf(FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isAnonymous.value)
                NotRegisteredSection(onOptionClick = onOptionClick)
            else
                RegisteredSection(onOptionClick = onOptionClick, onSignOutClick = {
                    FirebaseAuth.getInstance().signOut()
                    FirebaseAuth.getInstance().signInAnonymously()
                    isAnonymous.value = true
                    cartViewModel.clearContent()
                    favouriteViewModel.clearContent()
                })

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
                "+375 (44) 575-43-25"
            ),
            Option(
                "E-mail",
                OptionType.Gmail,
                Icons.Default.Email,
                "goldappsender@gmail.com"
            ),
            Option(
                "Telegram",
                OptionType.Telegram,
                R.drawable.telegram_icon,
                "@n_garkavaia"
            ),
            Option(
                "WhatsApp",
                OptionType.WhatsApp,
                R.drawable.whatsapp_icon,
                "+74951506463"
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
