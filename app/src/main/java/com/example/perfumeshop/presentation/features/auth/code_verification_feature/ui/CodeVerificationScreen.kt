package com.example.perfumeshop.presentation.features.auth.code_verification_feature.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.presentation.components.Loading
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.auth.login_feature.ui.AuthViewModel

const val TAG = "CodeVerificationScreen"
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 10.dp, bottom = 20.dp),
            text = "Подтверждение кода доступа",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        val alertText = buildAnnotatedString {
            withStyle(MaterialTheme.typography.bodyMedium.toSpanStyle()){
                append(stringResource(id = R.string.code_verification_alert_bef))
            }
            withStyle(TextStyle(fontWeight = FontWeight.SemiBold).toSpanStyle()){
                append(" ${authViewModel.phoneNumberState} ")
            }
            withStyle(MaterialTheme.typography.bodyMedium.toSpanStyle()){
                append(stringResource(id = R.string.code_verification_alert_aft))
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight()
                .padding(bottom = 30.dp),
            text = alertText,
            textAlign = TextAlign.Center
        )


        OtpTextField(otpText = codeState) { value, otpInputFilled ->
            codeState = value
            inputFilled = otpInputFilled
        }



        Button(
            modifier = Modifier.padding(top = 40.dp),
            enabled = inputFilled,
            onClick = {
                try {
                    authViewModel.verifyCode(
                        code = codeState,
                        onSuccess = onSuccess,
                        onFailure = { message ->
                            context.showToast(message)
                        }
                     )
                 } catch (e : Exception){
                     context.showToast(R.string.incorrect_input_error)
                 }

             }) {
            Text(text = "Подтвердить", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
        }
    }

    val uiState = authViewModel.uiState.collectAsState()

    if (uiState.value is UiState.Loading)
        Loading()
}
