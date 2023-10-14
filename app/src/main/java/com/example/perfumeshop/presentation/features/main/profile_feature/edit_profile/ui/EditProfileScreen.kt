package com.example.perfumeshop.presentation.features.main.profile_feature.edit_profile.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.data.user.UserData
import com.example.perfumeshop.data.utils.getSexByName
import com.example.perfumeshop.presentation.components.SubmitButton
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.auth.login_register_feature.ui.InputField
import com.example.perfumeshop.presentation.features.auth.login_register_feature.ui.SexPicker

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(
//    viewModel: EditProfileViewModel,
//                      onClick : () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    val firstNameState = rememberSaveable {
        mutableStateOf(UserData.user?.firstName ?: "")
    }

    val secondNameState = rememberSaveable {
        mutableStateOf(UserData.user?.secondName ?: "")
    }

    val sexState = rememberSaveable {
        mutableStateOf(getSexByName(UserData.user?.sex).ordinal)
    }

//    val phoneNumberState = remember {
//        mutableStateOf(UserData.user?.phoneNumber ?: "")
//    }


    val streetState = rememberSaveable {
        mutableStateOf(UserData.user?.street ?: "")
    }

    val homeNumberState = rememberSaveable {
        mutableStateOf(UserData.user?.home ?: "")
    }

    val flatNumberState = rememberSaveable {
        mutableStateOf(UserData.user?.flat ?: "")
    }


    val validInputsState = remember(
        firstNameState.value,
        secondNameState.value,
        homeNumberState.value,
        flatNumberState.value
    ) {
        mutableStateOf(
            firstNameState.value.trim().isNotEmpty() &&
                    secondNameState.value.trim().isNotEmpty() &&
            try {
                homeNumberState.value.trim().toInt()
                true
            } catch (nfe : NumberFormatException){
                Log.d("ERROR_ERROR", "OrderMakingScreen: ${nfe.message}")
                homeNumberState.value.isEmpty()
            }
            && try {
                flatNumberState.value.trim().toInt()
                true
            } catch (nfe : NumberFormatException){
                Log.d("ERROR_ERROR", "OrderMakingScreen: ${nfe.message}")
                flatNumberState.value.isEmpty()
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Ваш профиль", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(7.dp))

        Text(text = "Персональные данные")

        Spacer(modifier = Modifier.height(3.dp))

        InputField(valueState = firstNameState, label = "Имя", enabled = true)

        Spacer(modifier = Modifier.height(3.dp))

        InputField(valueState = secondNameState, label = "Фамилия", enabled = true)

        Spacer(modifier = Modifier.height(3.dp))

        SexPicker(selectedInd = sexState)

//        Spacer(modifier = Modifier.height(3.dp))
//
//        InputField(valueState = phoneNumberState, label = "Номер телефона", enabled = true)

        Spacer(modifier = Modifier.height(7.dp))

        Text(text = "Постоянный адрес доставки")

        Spacer(modifier = Modifier.height(3.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {

            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
                valueState = streetState,
                label = "Улица",
                enabled = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


                InputField(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(5.dp),
                    keyboardType = KeyboardType.Number,
                    valueState = homeNumberState,
                    label = "Дом",
                    enabled = true
                )

                InputField(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(5.dp),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    valueState = flatNumberState,
                    label = "Квартира",
                    enabled = true
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))


        SubmitButton(text = "Подтвердить изменения", validInputsState = validInputsState) {

            UserData.updateUserData(
                firstName = firstNameState.value.ifEmpty { null },
                secondName = secondNameState.value.ifEmpty { null },
                sexInd = sexState.value,
                streetName = streetState.value.ifEmpty { null },
                homeNumber = homeNumberState.value.ifEmpty { null },
                flatNumber = flatNumberState.value.ifEmpty { null }
            )

            keyboardController?.hide()

            showToast(context, "Обновлено")
        }


    }
}