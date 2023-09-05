package com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.SearchForm

@Composable
fun HomeScreen(
    onSearchClick : (String, QueryType) -> Unit,
    onProductClick : (String) -> Unit,
    viewModel: HomeViewModel
) {

    Surface(modifier = Modifier) {

        Column {

            SearchForm { query -> onSearchClick(query, QueryType.brand) }

            Spacer(modifier = Modifier.height(100.dp))

            Button(onClick = {
                onProductClick.invoke("home id")
            }) {}


        }
    }

}



