package com.example.perfumeshop.presentation.features.edit_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.firstLetterToUpperCase
import com.example.perfumeshop.data.utils.getSexByName
import com.example.perfumeshop.data.utils.showToast
import com.example.perfumeshop.presentation.components.InputField
import com.example.perfumeshop.presentation.components.SubmitButton
import com.example.perfumeshop.presentation.features.login.components.SexPicker

const val TAG = "EditProfileScreen"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(editProfileViewModel: EditProfileViewModel) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    val firstNameState = rememberSaveable {
        mutableStateOf(editProfileViewModel.userData.user?.firstName?.firstLetterToUpperCase() ?: "")
    }

    val secondNameState = rememberSaveable {
        mutableStateOf(editProfileViewModel.userData.user?.secondName?.firstLetterToUpperCase() ?: "")
    }

    val sexState = rememberSaveable {
        mutableStateOf(getSexByName(editProfileViewModel.userData.user?.sex).ordinal)
    }

    val cityState = rememberSaveable {
        mutableStateOf(editProfileViewModel.userData.user?.city?.firstLetterToUpperCase() ?: "")
    }

    val streetState = rememberSaveable {
        mutableStateOf(editProfileViewModel.userData.user?.street?.firstLetterToUpperCase() ?: "")
    }

    val homeNumberState = rememberSaveable {
        mutableStateOf(editProfileViewModel.userData.user?.home ?: "")
    }

    val flatNumberState = rememberSaveable {
        mutableStateOf(editProfileViewModel.userData.user?.flat ?: "")
    }

    val validInputsState by remember(
        firstNameState.value,
        secondNameState.value
    ) {
        mutableStateOf(
            firstNameState.value.trim().isNotEmpty() &&
            secondNameState.value.trim().isNotEmpty()
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f),
            //verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Ваш профиль",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Divider(modifier = Modifier.padding(top = 10.dp, bottom = 5.dp))
            }

            item {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Персональные данные",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            item {
                Divider(modifier = Modifier.padding(bottom = 5.dp))
            }

            item {
                InputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp),
                    valueState = firstNameState,
                    label = "Имя", enabled = true
                )
            }
            item {
                InputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 10.dp),
                    valueState = secondNameState,
                    label = "Фамилия",
                    enabled = true
                )
            }

            item {
                SexPicker(selectedIndState = sexState)
            }

            item {
                Divider(modifier = Modifier.padding(top = 15.dp, bottom = 5.dp))
            }
            item {
                Text(
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    text = "Постоянный адрес доставки",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            item {
                Divider(modifier = Modifier.padding(bottom = 5.dp))
            }

            item {
                InputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
                    valueState = cityState,
                    label = "Город",
                    enabled = true
                )
            }

            item {
                InputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
                    valueState = streetState,
                    label = "Улица",
                    enabled = true
                )
            }

            item {
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
                        keyboardType = KeyboardType.Password,
                        valueState = homeNumberState,
                        label = "Дом",
                        enabled = true
                    )

                    InputField(
                        modifier = Modifier
                            .width(130.dp)
                            .padding(5.dp),
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        valueState = flatNumberState,
                        label = "Кв.",
                        enabled = true
                    )
                }
            }
        }


        SubmitButton(text = "Подтвердить изменения", validInputsState = validInputsState) {

            editProfileViewModel.userData.updateUserData(
                firstName = firstNameState.value.ifEmpty { null },
                secondName = secondNameState.value.ifEmpty { null },
                sexInd = sexState.value,
                city = cityState.value,
                streetName = streetState.value.ifEmpty { null },
                homeNumber = homeNumberState.value.ifEmpty { null },
                flatNumber = flatNumberState.value.ifEmpty { null }
            )

            keyboardController?.hide()
            context.showToast(R.string.content_updated)
        }


    }
}