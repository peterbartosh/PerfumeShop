package com.example.perfumeshop.presentation.features.auth.login_feature.ui


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.presentation.components.InputField
import com.example.perfumeshop.presentation.components.SubmitButton


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    isCreateAccount: Boolean,
    onDone: (String, String, String, Int, String) -> Unit
){
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

            SexPicker(selectedInd = sexSelectedInd)

        }

        PhoneInput(
            phone = phoneNumber,
            mask = "(00) 000-00-00",
            maskNumber = '0',
            enabled = true,
            onPhoneChanged = {
                phoneNumber.value = it
                Log.d("PHONE_PHONE", "UserForm: ${phoneNumber.value}")
            }
        )

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            label = "Пароль",
            enabled = true,
            passwordVisibility = passwordVisible,
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    label: String,
    enabled: Boolean = true,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {

    val visualTransformation =
        if (passwordVisibility.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier
            .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value}) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id =
              if (passwordVisibility.value)
                    R.drawable.hide_password
              else
                  R.drawable.show_password
            ),
            contentDescription = "show/hide pwd"
        )

    }

}

@Composable
fun SexPicker(selectedInd : MutableState<Int>) {
    val sexes = listOf("Мужской", "Женский", "Не задано")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Пол:", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 5.dp))

        //Spacer(modifier = Modifier.width(10.dp))

        repeat(3){ ind ->
            SegmentButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(end = 5.dp),
                text = sexes[ind],
                ind = ind,
                selectedInd = selectedInd,
                contentPadding = PaddingValues(10.dp)
            )

        }
    }
}


@Composable
fun SegmentButton(
    modifier : Modifier = Modifier
        .wrapContentWidth()
        .wrapContentHeight(),
    text : String,
    ind : Int,
    defaultInd : Int = -1,
    contentPadding : PaddingValues = PaddingValues(5.dp),
    selectedInd : MutableState<Int>,
    onOptionalClick : () -> Unit = {},
    shape : Shape = RoundedCornerShape(10.dp),
    borderWidth : Int = 2,
    Content : @Composable () -> Unit = {
         Text(text = text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
     }
) {

    Button(
        modifier = modifier,
        onClick = {
            if (selectedInd.value == ind)
                selectedInd.value = defaultInd
            else
                selectedInd.value = ind

            onOptionalClick()
        },
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = BorderStroke(
            width = borderWidth.dp,
            color = if (selectedInd.value == ind)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.LightGray
                        //MaterialTheme.colorScheme.onBackground

        ),
        contentPadding = contentPadding
    ) {

        Content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BelarusCodeBox() {
    Box(
        modifier = Modifier
            .height(40.dp)
            .wrapContentWidth()
            .border(
                width = TextFieldDefaults.UnfocusedBorderThickness,
                color = Color.DarkGray.copy(0.7f),
                shape = RoundedCornerShape(5.dp)
            )
    ) {

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Image(
                painter = painterResource(id = R.drawable.belarus_flag),
                contentDescription = "bel flag image",
                modifier = Modifier
                    .padding(start = 5.dp)
                    .height(20.dp)
                    .wrapContentWidth()
            )

            Text(text = "+375", modifier = Modifier.padding(start = 5.dp, end = 5.dp))
        }
    }
}


@Composable
fun PhoneInput(
    modifier: Modifier = Modifier,
    phone: MutableState<String> = mutableStateOf("22222222"),
    mask: String = " (00) 000-00-00",
    enabled: Boolean = true,
    maskNumber: Char = '0',
    onPhoneChanged: (String) -> Unit = {}
) {

    Row (
        modifier = modifier
            .wrapContentHeight(),
            //.padding(5.dp),
         verticalAlignment = Alignment.CenterVertically
    ){

        BelarusCodeBox()

        Spacer(modifier = Modifier.width(3.dp))

        InputField(
            modifier = modifier,
            valueState = phone,
            onValueChange = { value -> onPhoneChanged(value.take(mask.count { it == maskNumber })) },
            label = "Телефон",
            enabled = enabled,
            keyboardType = KeyboardType.NumberPassword,
            visualTransformation = PhoneVisualTransformation(mask, maskNumber)
        )
    }
}

class PhoneVisualTransformation(private val mask: String, private val maskNumber: Char) : VisualTransformation {

    private val maxLength = mask.count { it == maskNumber }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.length > maxLength) text.take(maxLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString

            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < mask.length) {
                if (mask[maskIndex] != maskNumber) {
                    val nextDigitIndex = mask.indexOf(maskNumber, maskIndex)
                    append(mask.substring(maskIndex, nextDigitIndex))
                    maskIndex = nextDigitIndex
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return TransformedText(annotatedString, PhoneOffsetMapper(mask, maskNumber))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return true
    }

    override fun hashCode(): Int {
        return mask.hashCode()
    }
}

private class PhoneOffsetMapper(val mask: String, val numberChar: Char) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        var noneDigitCount = 0
        var i = 0
        while (i < offset + noneDigitCount) {
            if (mask[i++] != numberChar) noneDigitCount++
        }
        return offset + noneDigitCount
    }

    override fun transformedToOriginal(offset: Int): Int =
        offset - mask.take(offset).count { it != numberChar }
}











