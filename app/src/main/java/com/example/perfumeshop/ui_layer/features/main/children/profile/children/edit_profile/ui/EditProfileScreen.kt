package com.example.perfumeshop.ui_layer.features.main.children.profile.children.edit_profile.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.InputField
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.SexPicker
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.SubmitButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(viewModel: EditProfileViewModel, onClick : () -> Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val curUser = FirebaseAuth.getInstance().currentUser

    //Log.d("SEX_TEST", FirebaseAuth.getInstance().currentUser?.displayName.toString())

    val firstNameState = remember {
        mutableStateOf(curUser?.displayName?.split(" ")?.first() ?: "")
    }

    val secondNameState = remember {
        mutableStateOf(curUser?.displayName?.split(" ")?.last()?.split("|")?.first() ?: "")
    }

    val sexStr =
        curUser?.displayName?.split(" ")?.last()?.split("|")?.last()

    val sexState = remember {
        mutableStateOf(if (sexStr.isNullOrEmpty()) 2 else sexStr.toInt())
    }

    val phoneNumberState = remember {
        mutableStateOf(curUser?.phoneNumber ?: "")
    }

    Column(modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Ваш профиль", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        InputField(valueState = firstNameState, labelId = "Имя", enabled = true)

        Spacer(modifier = Modifier.height(3.dp))

        InputField(valueState = secondNameState, labelId = "Фамилия", enabled = true)

        Spacer(modifier = Modifier.height(3.dp))

        SexPicker(selectedInd = sexState)

        Spacer(modifier = Modifier.height(3.dp))

        InputField(valueState = phoneNumberState, labelId = "Номер телефона", enabled = true)

        SubmitButton(text = "Подтвердить изменения", loading = false, validInputs = true) {
            FirebaseAuth.getInstance().currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(firstNameState.value +
                                            " " + secondNameState.value +
                                            "|" + sexState.value.toString()).build())

            keyboardController?.hide()


        }


    }
}