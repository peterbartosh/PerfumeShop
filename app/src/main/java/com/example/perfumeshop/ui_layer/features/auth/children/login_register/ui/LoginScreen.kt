package com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R

@Composable
fun LoginScreen(
    viewModel: AuthViewModel, onCodeSent : () -> Unit, onSuccess : () -> Unit
) {

    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //ReaderLogo()



            if (showLoginForm.value)
                UserForm(loading = false, isCreateAccount = false) { f, s, p ->

                    viewModel.onLoginClicked(p, onCodeSent = onCodeSent, onSuccess = onSuccess)

//                    viewModel.signInWithEmailAndPassword(email = e, password = p){
//                        onAuthButtonClick.invoke()
//                    }
                }
            else
                UserForm(loading = false, isCreateAccount = true) { f, s, p ->

                    viewModel.onLoginClicked(p, onCodeSent = onCodeSent, onSuccess = onSuccess)
                }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val askText = if (showLoginForm.value) "New user?" else "Already have an account?"
                val clickableText = if (showLoginForm.value) "Sign up " else "Login "

                Text(text = askText)
                Text(text = clickableText,
                     style = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                     modifier = Modifier.clickable {
                         showLoginForm.value = !showLoginForm.value
                     })
            }
        }
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(loading: Boolean,
             isCreateAccount: Boolean,
             onDone: (String, String, String) -> Unit = {f, s, e -> }
){
    val firstName = rememberSaveable { mutableStateOf("") }
    val secondName = rememberSaveable { mutableStateOf("") }
    val phoneNumber =  rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        if (isCreateAccount) {
            Text(
                text = stringResource(id = R.string.create_acct),
                modifier = Modifier.padding(4.dp)
            )


            InputField(valueState = firstName, labelId = "Имя", enabled = !loading,
                       keyboardActions = KeyboardActions {
                           FocusRequester.Default
                       })

            InputField(valueState = secondName, labelId = "Фамилия", enabled = !loading,
                       keyboardActions = KeyboardActions {
                           FocusRequester.Default
                       })
        }

        PhoneInput(phone = phoneNumber,
                   mask = "+375 (00) 000-00-00",
                   maskNumber = '0',
                   enabled = !loading,
                   onPhoneChanged = {
                       phoneNumber.value = it
                       Log.d("PHONE_PHONE", "UserForm: ${phoneNumber.value}")
                   })

        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = true
        ){
            onDone.invoke(firstName.value.trim(),
                          secondName.value.trim(),
                             "+375${phoneNumber.value.trim()}")

            keyboardController?.hide()
        }
    }

}