package com.example.perfumeshop.ui_layer.features.auth.code_verification_feature.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.ui_layer.features.auth.login_register_feature.ui.AuthViewModel
import com.example.perfumeshop.ui_layer.features.auth.login_register_feature.ui.InputField

//@Preview(showBackground = true)
@Composable
fun CodeVerificationScreen(
    viewModel: AuthViewModel, onSuccess : () -> Unit
) {
    val codeState = rememberSaveable {
        mutableStateOf("")
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {


           // OutlinedTextField(value = codeState, onValueChange = )

            InputField(
                valueState = codeState, onValueChange = {codeState.value = it},
                labelId = "Код", enabled = true
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(modifier = Modifier
                .width(100.dp)
                .height(50.dp),
                 onClick = { viewModel.verifyPhoneNumberWithCode(codeState.value, onSuccess) }) {
                Text(text = "Confirm")
            }
        }

    }
    }
