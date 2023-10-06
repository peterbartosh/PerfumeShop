package com.example.perfumeshop.ui_layer.features.main.profile_feature.edit_profile.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.ui_layer.components.SubmitButton
import com.example.perfumeshop.ui_layer.components.showToast
import com.example.perfumeshop.ui_layer.features.auth.login_register_feature.ui.InputField
import com.example.perfumeshop.ui_layer.features.auth.login_register_feature.ui.SexPicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import java.util.StringJoiner

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(viewModel: EditProfileViewModel, onClick : () -> Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    val curUser = FirebaseAuth.getInstance().currentUser

    val sep = ","

    //Log.d("SEX_TEST", FirebaseAuth.getInstance().currentUser?.displayName.toString())

    val firstNameState = remember {
        mutableStateOf(curUser?.displayName?.split(sep)?.get(0) ?: "")
    }

    val secondNameState = remember {
        mutableStateOf(curUser?.displayName?.split(sep)?.get(1) ?: "")
    }

    val sexStr = curUser?.displayName?.split(sep)?.get(2)

    val sexState = remember {
        mutableStateOf(if (sexStr.isNullOrEmpty()) 2 else sexStr.toInt())
    }

    val phoneNumberState = remember {
        mutableStateOf(curUser?.phoneNumber ?: "")
    }


    val streetState = remember {
        mutableStateOf(curUser?.displayName?.split(sep)?.get(3) ?: "")
    }

    val houseNumberState = remember {
        mutableStateOf(curUser?.displayName?.split(sep)?.get(4) ?: "")
    }

    val flatNumberState = remember {
        mutableStateOf(curUser?.displayName?.split(sep)?.get(5) ?: "")
    }

    val validInputsState = remember {
        mutableStateOf(true)
    }

//    val validInputsState = remember(streetState, houseNumberState, flatNumberState) {
//        mutableStateOf(streetState.value.trim().isNotEmpty()
//            && try {
//                houseNumberState.value.trim().toInt()
//                true
//            } catch (nfe : NumberFormatException){
//                Log.d("ERROR_ERROR", "OrderMakingScreen: ${nfe.message}")
//                false
//            }
//            && try {
//                houseNumberState.value.trim().toInt()
//                true
//            } catch (nfe : NumberFormatException){
//                Log.d("ERROR_ERROR", "OrderMakingScreen: ${nfe.message}")
//                false
//            }
//        )
//    }

    Column(modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Ваш профиль", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(7.dp))

        Text(text = "Персональные данные")

        Spacer(modifier = Modifier.height(3.dp))

        InputField(valueState = firstNameState, label = "Имя", enabled = true)

        Spacer(modifier = Modifier.height(3.dp))

        InputField(valueState = secondNameState, label = "Фамилия", enabled = true)

        Spacer(modifier = Modifier.height(3.dp))

        SexPicker(selectedInd = sexState)

        Spacer(modifier = Modifier.height(3.dp))

        InputField(valueState = phoneNumberState, label = "Номер телефона", enabled = true)

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
                    valueState = houseNumberState,
                    label = "Дом",
                    enabled = true
                )

                InputField(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(5.dp),
                    keyboardType = KeyboardType.Number,
                    valueState = flatNumberState,
                    label = "Квартира",
                    enabled = true
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))


        SubmitButton(text = "Подтвердить изменения", validInputsState = validInputsState) {
            val joiner = StringJoiner(",")
                .add(firstNameState.value)
                .add(secondNameState.value)
                .add(sexState.value.toString())
                .add(streetState.value)
                .add(houseNumberState.value)
                .add(flatNumberState.value)

            FirebaseAuth.getInstance().currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(joiner.toString()).build()
            )

            keyboardController?.hide()

            showToast(context, "Обновлено")


        }


    }
}