package com.example.perfumeshop.presentation.features.auth.login_feature.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.presentation.components.Loading
import com.example.perfumeshop.presentation.components.showToast
import kotlin.random.Random

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onCodeSent : () -> Unit,
    onSuccess : () -> Unit
) {

    val context = LocalContext.current

    val showLoginForm = rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(15.dp))

        if (showLoginForm.value)
            UserForm(isCreateAccount = false) { _, _, pn, _, pwd ->
                authViewModel.signIn(
                    phoneNumber = pn,
                    password = pwd,
                    onSuccess = onSuccess,
                    errorCallback = { message ->
                        showToast(context, message)
                    }
                )
            }
        else
            UserForm(isCreateAccount = true) { fn, sn, pn, sexInd, pwd ->
                authViewModel.notifyAdmin(
                    firstName = fn,
                    secondName = sn,
                    phoneNumber = pn,
                    sexInd = sexInd,
                    pwd = pwd,
                    seed = Random.nextInt(),
                    isInBlackList = { showToast(context, "Вы добавлены в чёрный список") },
                    onAdminNotified = onCodeSent,
                    onError = { message -> showToast(context, message) }
                )
            }

        Row(
            modifier = Modifier.padding(30.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val askText = if (showLoginForm.value) "Впервые здесь? " else "Есть аккаунт? "
            val clickableText = if (showLoginForm.value) "Регистрация " else "Вход "

            Text(text = askText)
            Text(
                text = clickableText,
                style = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable {
                    showLoginForm.value = !showLoginForm.value
                }
            )
        }
    }

    val uiState = authViewModel.uiState.collectAsState()

    if (uiState.value is UiState.Loading) Loading()
}