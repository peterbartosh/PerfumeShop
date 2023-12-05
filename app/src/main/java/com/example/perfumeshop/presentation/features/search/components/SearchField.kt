package com.example.perfumeshop.presentation.features.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.presentation.components.InputField


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    initialQuery : String = "",
    loading: Boolean = false,
    hint: String = "Поиск по брендам",
    onSearch: (String) -> Unit = {}
) {

    Column (modifier = modifier) {

        val searchQueryState = remember { mutableStateOf(initialQuery) }


        val keyboardController = LocalSoftwareKeyboardController.current

        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()

        }

        InputField(
            valueState = searchQueryState,
            label = hint,
            enabled = !loading,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear query icon",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            searchQueryState.value = ""
                        }
                )
            },
            keyboardActions = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                //searchQueryState.value = ""
                keyboardController?.hide()
            },
            imeAction = ImeAction.Search
        )

    }


}
