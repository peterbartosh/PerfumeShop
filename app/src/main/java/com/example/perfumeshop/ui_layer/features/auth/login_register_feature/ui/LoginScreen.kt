package com.example.perfumeshop.ui_layer.features.auth.login_register_feature.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.components.SubmitButton

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel, onCodeSent : () -> Unit, onSuccess : () -> Unit
) {

    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (showLoginForm.value)
                UserForm(isCreateAccount = false) { fn, sn, ph, sexInd ->
                    authViewModel.onLoginClicked(firstName = fn, secondName = sn,
                                                 sexInd = sexInd, phoneNumber = ph,
                                                 onCodeSent = onCodeSent, onSuccess = onSuccess)
                }
            else
                UserForm(isCreateAccount = true) { fn, sn, ph, sexInd ->
                    authViewModel.onLoginClicked(firstName = fn, secondName = sn,
                                                 sexInd = sexInd, phoneNumber = ph,
                                                 onCodeSent = onCodeSent, onSuccess = onSuccess)
                }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val askText = if (showLoginForm.value) "Впервые здесь? " else "Есть аккаунт? "
                val clickableText = if (showLoginForm.value) "Регистрация " else "Вход "

                Text(text = askText)
                Text(text = clickableText,
                     style = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                     modifier = Modifier.clickable {
                         showLoginForm.value = !showLoginForm.value
                     })
            }

            if (authViewModel.isLoading)
                LoadingIndicator()

        }
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    isCreateAccount: Boolean,
    onDone: (String, String, String, Int) -> Unit = { f, s, e, sex -> }
){
    val firstName = remember { mutableStateOf("") }
    val secondName = remember { mutableStateOf("") }
    val phoneNumber =  remember { mutableStateOf("") }
    val sexSelectedInd = remember { mutableStateOf(2) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val validInputsState = remember(firstName.value, secondName.value, phoneNumber.value) {
        mutableStateOf(
            if (isCreateAccount)
            firstName.value.trim().isNotEmpty() &&
                    secondName.value.trim().isNotEmpty() &&
                    phoneNumber.value.length == 9
            else
                phoneNumber.value.length == 9
        )
    }

    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {


        if (isCreateAccount) {

            InputField(valueState = firstName, labelId = "Имя", enabled = true,
                       keyboardActions = KeyboardActions {
                           FocusRequester.Default
                       })

            InputField(valueState = secondName, labelId = "Фамилия", enabled = true,
                       keyboardActions = KeyboardActions {
                           FocusRequester.Default
                       })

            SexPicker(selectedInd = sexSelectedInd)
        }

        PhoneInput(phone = phoneNumber,
                   mask = "(00) 000-00-00",
                   maskNumber = '0',
                   enabled = true,
                   onPhoneChanged = {
                       phoneNumber.value = it
                       Log.d("PHONE_PHONE", "UserForm: ${phoneNumber.value}")
                   })


        SubmitButton(
            text = if (isCreateAccount) "Create Account" else "Login",
            validInputsState = validInputsState
        ){
            onDone.invoke(firstName.value.trim(),
                          secondName.value.trim(),
                             "+375${phoneNumber.value.trim()}", sexSelectedInd.value)

            keyboardController?.hide()
        }
    }

}