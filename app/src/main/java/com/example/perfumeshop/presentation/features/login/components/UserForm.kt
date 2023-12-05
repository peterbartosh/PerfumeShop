package com.example.perfumeshop.presentation.features.login.components


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.isUserConnected
import com.example.perfumeshop.data.utils.showToast
import com.example.perfumeshop.presentation.components.InputField
import com.example.perfumeshop.presentation.components.SubmitButton


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    isCreateAccount: Boolean,
    onDone: (String, String, String, Int, String) -> Unit
){

    val context = LocalContext.current

    val firstName = rememberSaveable { mutableStateOf("") }
    val secondName = rememberSaveable { mutableStateOf("") }
    val phoneNumber =  rememberSaveable { mutableStateOf("") }
    val sexSelectedInd = rememberSaveable { mutableStateOf(2) }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default

    val keyboardController = LocalSoftwareKeyboardController.current

    var processing by remember {
        mutableStateOf(false)
    }

    val validInputsState by remember(
        firstName.value,
        secondName.value,
        phoneNumber.value,
        password.value
    ) {
        mutableStateOf(
            password.value.length >= 6 &&
                    if (isCreateAccount)
                        firstName.value.trim().isNotEmpty() &&
                                secondName.value.trim().isNotEmpty() &&
                                phoneNumber.value.length == 9
                    else
                        phoneNumber.value.length == 9
        )
    }

    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        val label = if (isCreateAccount) "Регистрация аккаунта" else "Вход в аккаунт"

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp, bottom = 15.dp),
            text = label,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        if (isCreateAccount) {

            InputField(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                valueState = firstName,
                label = "Имя",
                enabled = true)

            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                valueState = secondName,
                label = "Фамилия",
                enabled = true
            )

            SexPicker(selectedIndState = sexSelectedInd)

        }

        PhoneInputField(
            phone = phoneNumber,
            mask = "(00) 000-00-00",
            maskNumber = '0',
            enabled = true,
            onPhoneChanged = {
                phoneNumber.value = it
                Log.d("PHONE_PHONE", "UserForm: ${phoneNumber.value}")
            }
        )

        PasswordInputField(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            label = "Пароль",
            enabled = true,
            passwordVisibilityState = passwordVisible,
            onAction = KeyboardActions.Default
        )

        if (isCreateAccount)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    text = stringResource(id = R.string.password_requirements),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

        SubmitButton(
            text = if (isCreateAccount) "Создать аккаунт" else "Войти",
            validInputsState = validInputsState && !processing
        ){
            processing = true
            keyboardController?.hide()

            if (!isUserConnected(context)){
                context.showToast(R.string.connection_lost_error)
                processing = false
                return@SubmitButton
            } else {
                onDone.invoke(
                    firstName.value.trim(),
                    secondName.value.trim(),
                    "+375${phoneNumber.value.trim()}",
                    sexSelectedInd.value,
                    password.value
                )

                processing = false
            }
        }
    }
}
