package com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.utils.QueryType


@Composable
fun SearchScreen(query : String, queryType : QueryType,
                 viewModel: SearchViewModel, onProductClick : (String) -> Unit) {

    if (viewModel.initSearchQuery) {
        viewModel.updateQuery(query = query, queryType = queryType)
        viewModel.searchQuery()
    }

        Column(modifier = Modifier) {

            SearchForm() { q ->
                viewModel.updateQuery(q, QueryType.brand)
                viewModel.searchQuery()
            }

           // Log.d("IR_IR", "${viewModel.isInitialized}  ${viewModel.searchList.size}")

            if (viewModel.isInitialized)
                if (viewModel.isFailure) {
                    Text(text = "ERROR")
                } else if (viewModel.isLoading || viewModel.searchList.collectAsState().value.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Loading...")
                        LinearProgressIndicator()
                    }
                } else
                   // Log.d("SIZE_SIZE", "SearchScreen: ${viewModel.searchList.size}")
                    ProductList(onProductClick = onProductClick,
                                listOfProducts = viewModel.searchList.collectAsState().value)
                }

        }



@Composable
fun ProductList( onProductClick : (String) -> Unit, listOfProducts : List<Product>) {


        LazyColumn(contentPadding = PaddingValues(10.dp),
                   modifier = Modifier.fillMaxSize()){
            items(listOfProducts){ product ->
                ProductRow(product = product, onProductClick = onProductClick)
            }

        }


}

@Composable
fun ProductRow(product: Product, onProductClick : (String) -> Unit) {

    Card(modifier = Modifier
        .clickable { onProductClick.invoke(product.id.toString()) }
        .fillMaxWidth()
        .height(70.dp)
        .padding(3.dp),
         shape = RectangleShape,
         elevation = CardDefaults.cardElevation(7.dp)) {
        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {
            //if (book != null)
            //"https://www.pngarts.com/files/8/Standing-Book-Cover-PNG-Photo.png"


//            if (book != null)
//            if (book.isbn != null)

            Image(painter = rememberAsyncImagePainter(model =
            "https://www.google.com/url?sa=i&url=https%3A%2F%2Fvanille.by%2Fgucci-flora-by-gucci-gracious-tuberose&psig=AOvVaw1E9yBoCNg_-1U0HlIRKQ7l&ust=1692929272446000&source=images&cd=vfe&opi=89978449&ved=0CBAQjRxqFwoTCPDipJGb9IADFQAAAAAdAAAAABAE"),
                  contentDescription = "image book",
                  modifier = Modifier
                      .height(30.dp)
                      .width(30.dp)
                      .padding(end = 5.dp))


            Column {

                Text(text = product.brand.toString(), overflow = TextOverflow.Ellipsis)

                Text(
                    text = "Collection: ${product.collection}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium
                )

            }


        }

    }
}