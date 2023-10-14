package com.example.perfumeshop.presentation.features.auth.code_verification_feature.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.auth.login_register_feature.ui.AuthViewModel

@Composable
fun CodeVerificationScreen(
    authViewModel: AuthViewModel, onSuccess : () -> Unit
) {

    val context = LocalContext.current

    var codeState by remember {
        mutableStateOf("")
    }

    var inputFilled by remember {
        mutableStateOf(false)
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {

            OtpTextField(otpText = codeState, onOtpTextChange = { value, otpInputFilled ->
                codeState = value
                inputFilled = otpInputFilled
                Log.d("TAGGG", "CodeVerificationScreen: $codeState $inputFilled")
            })

            Spacer(modifier = Modifier.height(40.dp))

            Button(modifier = Modifier,
                   enabled = inputFilled,
                   onClick = {
                     try {
                         if (authViewModel.verifyCode(codeState)){
                             authViewModel.register()
                             onSuccess()
                         } else {
                             showToast(context = context, "Неверный код")
                         }
                     } catch (e : Exception){
                         showToast(context = context, "Некорректные данные")
                     }

                 }) {
                Text(text = "Подтвердить")
            }
        }

    }
}
