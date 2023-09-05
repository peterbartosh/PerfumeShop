package com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.InputField


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Поиск",
    onSearch: (String) -> Unit = {}) {

    Column (modifier = modifier) {
        val searchQueryState = rememberSaveable { mutableStateOf("") }


        val keyboardController = LocalSoftwareKeyboardController.current

        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()

        }

        InputField(valueState = searchQueryState,
                   labelId = "Поиск",
                   enabled = !loading,
                   keyboardActions = KeyboardActions {
                       if (!valid) return@KeyboardActions
                       onSearch(searchQueryState.value.trim())
                       searchQueryState.value = ""
                       keyboardController?.hide()
                   })

    }


}