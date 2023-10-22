package com.example.perfumeshop.presentation.features.main.profile_feature.edit_profile.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.user.UserData
import com.example.perfumeshop.data.utils.firstLetterToUpperCase
import com.example.perfumeshop.data.utils.getSexByName
import com.example.perfumeshop.presentation.components.InputField
import com.example.perfumeshop.presentation.components.SubmitButton
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.auth.login_register_feature.ui.SexPicker

const val TAG = "EditProfileScreen"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(
//    viewModel: EditProfileViewModel,
//                      onClick : () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    val firstNameState = rememberSaveable {
        mutableStateOf(UserData.user?.firstName?.firstLetterToUpperCase() ?: "")
    }

    val secondNameState = rememberSaveable {
        mutableStateOf(UserData.user?.secondName?.firstLetterToUpperCase() ?: "")
    }

    val sexState = rememberSaveable {
        mutableStateOf(getSexByName(UserData.user?.sex).ordinal)
    }

    val streetState = rememberSaveable {
        mutableStateOf(UserData.user?.street?.firstLetterToUpperCase() ?: "")
    }

    val homeNumberState = rememberSaveable {
        mutableStateOf(UserData.user?.home ?: "")
    }

    val flatNumberState = rememberSaveable {
        mutableStateOf(UserData.user?.flat ?: "")
    }


    val validInputsState by remember(
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
                Log.d(TAG, "OrderMakingScreen: ${nfe.message}")
                homeNumberState.value.isEmpty()
            }
            && try {
                flatNumberState.value.trim().toInt()
                true
            } catch (nfe : NumberFormatException){
                Log.d(TAG, "OrderMakingScreen: ${nfe.message}")
                flatNumberState.value.isEmpty()
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f),
            //verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Ваш профиль",
                style = MaterialTheme.typography.titleMedium
            )

            Divider(modifier = Modifier.padding(top = 15.dp, bottom = 5.dp))

            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                text = "Персональные данные",
                style = MaterialTheme.typography.labelLarge
            )

            Divider(modifier = Modifier.padding(bottom = 5.dp))

            InputField(
                modifier = Modifier
                    .fillMaxWidth().
                    padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp),
                valueState = firstNameState,
                label = "Имя", enabled = true
            )

            InputField(
                modifier = Modifier
                    .fillMaxWidth().
                    padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 10.dp),
                valueState = secondNameState,
                label = "Фамилия",
                enabled = true
            )

            SexPicker(selectedInd = sexState)

            Divider(modifier = Modifier.padding(top = 15.dp, bottom = 5.dp))

            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                text = "Постоянный адрес доставки",
                style = MaterialTheme.typography.labelLarge
            )

            Divider(modifier = Modifier.padding(bottom = 5.dp))

            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
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
                    label = "Кв.",
                    enabled = true
                )
            }
        }


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