package com.example.perfumeshop.presentation.features.main.profile_feature.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.data.utils.isUserConnected
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import kotlinx.coroutines.joinAll


@Composable
fun ProfileScreen(
    onOptionClick: (OptionType) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current

    val isAnonymous = remember {
        mutableStateOf(profileViewModel.auth.currentUser?.isAnonymous == true)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isAnonymous.value)
            NotRegisteredSection(onOptionClick = onOptionClick)
        else
            RegisteredSection(
                profileViewModel = profileViewModel,
                onOptionClick = {optionType ->
                    if (!isUserConnected(context)){
                        showToast(context, "Ошибка.\nВы не подключены к сети.")
                        return@RegisteredSection
                    }
                    onOptionClick(optionType)
                },
                onSignOutClick = {

                        val clearUserDataJob = profileViewModel.userData.clearUserData()

                        clearUserDataJob.join()

                        val cartSaveJob = cartViewModel.saveProductsToRemoteDatabase()
                        val favouriteSaveJob = favouriteViewModel.saveProductsToRemoteDatabase()

                        joinAll(cartSaveJob, favouriteSaveJob)

                        val cartClearJob = cartViewModel.clearData()
                        val favouriteClearJob = favouriteViewModel.clearData()

                        joinAll(cartClearJob, favouriteClearJob)

                        profileViewModel.auth.signOut()
                        profileViewModel.auth.signInAnonymously()

                        isAnonymous.value = true
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

    ProfileSection(
        profileViewModel = profileViewModel,
        onEditProfileClick = { onOptionClick(OptionType.Edit) },
        onSignOutClick = onSignOutClick
    )

    Spacer(modifier = Modifier.height(10.dp))

    OptionsSection(
        sectionTitle = "Личные данные", listOf(
            Option("Избранное", OptionType.Favourite, Icons.Default.Favorite, Icons.Outlined.ArrowForward),
            Option("Заказы", OptionType.Orders, R.drawable.order_icon, Icons.Outlined.ArrowForward)
        ), onOptionClick = onOptionClick
    )

    OptionsSection(
        sectionTitle = "Контакты", listOf(
            Option("Телефон", OptionType.PhoneNumber, Icons.Default.Phone, "+375 (44) 575-43-25"),
            Option("E-mail", OptionType.Gmail, Icons.Default.Email, "goldappsender@gmail.com"),
            Option("Telegram", OptionType.Telegram, R.drawable.telegram_icon, "@n_garkavaia"),
            Option("WhatsApp", OptionType.WhatsApp, R.drawable.whatsapp_icon, "+74951506463")
        ),
        onOptionClick = onOptionClick
    )
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
