package com.example.perfumeshop.presentation.features.main.home_feature.search.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.perfumeshop.data.user_preferences.PreferencesManager
import com.example.perfumeshop.data.utils.ProductType
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.data.utils.round
import com.example.perfumeshop.presentation.components.InputField
import com.example.perfumeshop.presentation.theme.Gold


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
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


@Composable
fun UploadMoreButton(
    searchViewModel: SearchViewModel,
    currentQuery: String,
    currentQueryType: QueryType
) {

    Spacer(modifier = Modifier.height(15.dp))

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        if (searchViewModel.searchProducts.size == (searchViewModel.uploadsAmount + 1) * productsAmountPerPage)
        //if (!searchViewModel.uploadingMore)
            Button(
                onClick = {
                    searchViewModel.uploadMore(currentQuery = currentQuery, currentQueryType = currentQueryType)
                },
                contentPadding = PaddingValues(3.dp),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .height(30.dp)
                    .width(70.dp)
            ) {
                if (searchViewModel.uploadingMore)
                    CircularProgressIndicator(modifier = Modifier.size(25.dp))
                else
                    Text(text = "Ещё", style = MaterialTheme.typography.bodyMedium)
            }
//        else
//            LoadingIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun InputOption(
    ind: Int = 1,
    query: String = "al",
    queryType: QueryType = QueryType.brand,
    onCloseCLick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(50.dp)),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        )
    ) {
        val text =
            when (queryType.name) {
                QueryType.type.name -> "Тип: ${ProductType.valueOf(query).toRus()}"
                QueryType.brand.name -> "В названии: $query"
                else -> ""
            }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.bodySmall
            )

            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            if (ind == 1)
                IconButton(
                    modifier = Modifier.size(25.dp),
                    onClick = onCloseCLick
                ) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "close icon"
                    )
                }
        }
    }
}

@Composable
fun SearchOption(
    applied : Boolean,
    text : String,
    iconId : Int,
    showDialogState : MutableState<Boolean>
) {

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(5.dp)
            //.width(wp * 31)
            //.clipToBounds()
            .clip(RoundedCornerShape(50.dp))
            .clickable { showDialogState.value = !showDialogState.value },
         shape = RoundedCornerShape(50.dp),
         colors = CardDefaults.cardColors(
             containerColor = MaterialTheme.colorScheme.primaryContainer,
             contentColor = if (applied)
                 MaterialTheme.colorScheme.primary
             else
                 MaterialTheme.colorScheme.onBackground
         ),
         border = BorderStroke(
             width = 2.dp,
             color = if (applied)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onBackground
         )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(5.dp),
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                 painter = painterResource(id = iconId),
                 contentDescription = null,
                 modifier = Modifier
                     .size(27.dp)
                     .padding(start = 5.dp, end = 1.dp)
            )

            //Spacer(modifier = Modifier.width(1.dp))


            //Divider(Modifier.width(1.dp))

            Text(
                text = text,
                modifier = Modifier.padding(end = 5.dp),
                style = MaterialTheme.typography.bodyMedium
            )


        }

    }

}
//
//@Composable
//fun PriceDropDownMenu(
//    expanded: MutableState<Boolean>,
//    selected: MutableState<Boolean>
//) {
//    DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = !expanded.value }) {
//
//
//            val width = 100.dp
//            val selectedBorderStroke = BorderStroke(width = 3.dp, color = Gold)
//            val notSelectedBorderStroke = BorderStroke(width = 1.dp, color = Color.LightGray)
//            Row(
//                modifier = Modifier
//                    .width(width)
//                    .border(
//                        if (selected.value) selectedBorderStroke else notSelectedBorderStroke
//                    )
//                    .clickable {
//                        selected.value = true
//                    },
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(text = "Нал.", fontSize = 20.sp,
//                     modifier = Modifier.padding(5.dp),
//                     textAlign = TextAlign.Center)
//            }
//            Row(
//                modifier = Modifier
//                    .width(width)
//                    .border(
//                        if (!selected.value) selectedBorderStroke else notSelectedBorderStroke
//                    )
//                    .clickable {
//                        selected.value = false
//                    },
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(text = "Безнал.", fontSize = 20.sp,
//                     modifier = Modifier.padding(5.dp),
//                     textAlign = TextAlign.Center)
//            }
//        //}
//    }
//}

@Composable
fun SortDialog(
    showDialog: MutableState<Boolean>,
    sortPrioritiesInitValue: List<Int>,
    isAscendingInitValue: Boolean,
    onApplyButtonClick: (Boolean, List<Int>, Boolean) -> Unit
) {

    val N = 2

    val labels = listOf("Цене", "Объёму")

    val priorities = remember {
        sortPrioritiesInitValue.toList().toMutableStateList()
    }

    val sortStates = remember {
        List(N) {ind -> priorities.contains(ind) }.toMutableStateList()
    }

    val isAscending = remember {
        mutableStateOf(isAscendingInitValue)
    }

    val applySort = remember(priorities.size) {
        mutableStateOf(priorities.isNotEmpty())
    }

    val isAscendingRadioButtonEnabled by remember(priorities.size) {
        mutableStateOf(priorities.isNotEmpty())
    }

    Dialog(onDismissRequest = { showDialog.value = false }) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(all = 5.dp)
            ) {

                Text(text = "Сортировать по:", style = MaterialTheme.typography.titleMedium)


                repeat(2){ ind ->
                    Row (
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Spacer(modifier = Modifier.width(10.dp))
                        CustomRadioButton(ind = ind, states = sortStates, priorities = priorities) {
                            sortStates[ind] = !sortStates[ind]
                            if (sortStates[ind])
                                priorities.add(ind)
                            else
                                priorities.remove(ind)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = labels[ind], style = MaterialTheme.typography.bodyMedium)
                    }
                }

                //Spacer(modifier = Modifier.height(20.dp))

                Divider()

                Row (
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                   RadioButton(
                       modifier = Modifier.padding(all = 5.dp),
                       enabled = isAscendingRadioButtonEnabled,
                       selected = isAscending.value,
                       colors = RadioButtonDefaults.colors(
                           selectedColor = MaterialTheme.colorScheme.primary,
                           unselectedColor = MaterialTheme.colorScheme.primary,
                           disabledSelectedColor = Color.LightGray,
                           disabledUnselectedColor = Color.LightGray
                       ),
                       onClick = { isAscending.value = !isAscending.value }
                    )

                    //Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = if (isAscending.value) "По возрастанию" else "По убыванию",
                        color = if (isAscendingRadioButtonEnabled) MaterialTheme.colorScheme.onBackground else Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                //Spacer(modifier = Modifier.height(30.dp))

                Divider(modifier = Modifier.height(2.dp), color = MaterialTheme.colorScheme.onBackground)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 5.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(
                            onClick = {
                                onApplyButtonClick(isAscending.value, priorities, applySort.value)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            //border = BorderStroke(width = 1.dp, color = if (applySort.value) Gold else Color.LightGray),
                            modifier = Modifier.wrapContentSize(),
                            contentPadding = ButtonDefaults.ContentPadding
                        ) {
                            Text(text = "Применить", style = MaterialTheme.typography.bodyMedium)
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            modifier = Modifier.clickable {
                                sortStates.replaceAll { false }
                                priorities.clear()
                                isAscending.value = true
                            },
                            text = "Очистить всё",
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                    }
                }
            }
        }
    }
}
@Composable
fun CustomRadioButton(
    ind: Int,
    states : SnapshotStateList<Boolean>,
    priorities : SnapshotStateList<Int>,
    onClick: () -> Unit
) {

    TextButton(
        onClick = onClick,
        modifier = Modifier
            //.wrapContentSize()
            .size(35.dp)
            .padding(all = 5.dp),
                     //start = 5.dp, end = 5.dp),
        shape = CircleShape,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (states[ind]) MaterialTheme.colorScheme.primary else Color.White
        ),
        contentPadding = PaddingValues(1.dp)
    ) {
        val text = if (states[ind]) (priorities.indexOf(ind) + 1).toString() else ""
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}

@Composable
fun FilterDialog(
    showDialog: MutableState<Boolean>,
    minInitValue: String,
    maxInitValue: String,
    isOnHandOnlyInitValue: Boolean,
    availableVolumes: List<Double>,
    isCompactQueryType: Boolean,
    volumesStatesInitValue: List<Double>,
    //isMaleOnlyInitValue : Boolean,
   // isFemaleOnlyInitValue : Boolean,
    onApplyButtonClick: (String, String, Boolean, List<Double>, Boolean) -> Unit,
) {


    //val context = LocalContext.current

    val moreThanState = remember {
        mutableStateOf(minInitValue)
    }

    val lessThanState = remember {
        mutableStateOf(maxInitValue)
    }

    val isOnHandOnlyState = remember {
        mutableStateOf(isOnHandOnlyInitValue)
    }

    val volumesStates = remember {
        val states = mutableStateListOf<Double>()
        states.addAll(volumesStatesInitValue)
        states
    }

//    val isMaleOnly = remember {
//        mutableStateOf(isMaleOnlyInitValue)
//    }

//    val isFemaleOnly = remember {
//        mutableStateOf(isFemaleOnlyInitValue)
//    }

    val applyFilter = remember(
        moreThanState.value, lessThanState.value,
        isOnHandOnlyState.value,
        arrayOf(volumesStates)
        //isMaleOnly.value, isFemaleOnly.value
    ) {
        mutableStateOf(
            (moreThanState.value != "0.0" && moreThanState.value.isNotEmpty()) ||
            (lessThanState.value != maxProductPrice.toString() && lessThanState.value.isNotEmpty()) ||
            isOnHandOnlyState.value ||
                    volumesStates.isNotEmpty()
                    //|| isFemaleOnly.value || isMaleOnly.value
        )
    }

    Dialog(onDismissRequest = { showDialog.value = false }) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(all = 5.dp)
            ) {

                Text(text = "Фильтровать по:", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(5.dp))

                PriceFilterRow(priceStates = arrayOf(moreThanState, lessThanState))

                Divider()

                //MaleFemale(isMaleOnly, isFemaleOnly)

                if (availableVolumes.isNotEmpty()){

                    VolumeOption(states = volumesStates, volumes = availableVolumes,  isCompactQueryType = isCompactQueryType)

                    Divider()
                }

                //Divider()

                IsOnHandOnlyOption(text = "Только в наличии", selected = isOnHandOnlyState)

                Divider(modifier = Modifier.height(2.dp), color = MaterialTheme.colorScheme.onBackground)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 5.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(
                            onClick = {
                                onApplyButtonClick(
                                    moreThanState.value.ifEmpty { "0.0" },
                                    lessThanState.value.ifEmpty { maxProductPrice.toString() },
                                    isOnHandOnlyState.value,
                                    volumesStates.toList(),
                                    applyFilter.value
                                    //isMaleOnly.value,
                                    //isFemaleOnly.value
                                )
                            },
                            //enabled = applyFilter.value,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                //disabledContainerColor =
                            ),
//                            border = BorderStroke(
//                                width = 1.dp,
//                                color = if (applyFilter.value) Gold else Color.LightGray
//                            ),
                            modifier = Modifier.wrapContentSize(),
                            contentPadding = ButtonDefaults.ContentPadding
                        ) {
                            Text(
                                text = "Применить",
                                style = MaterialTheme.typography.bodyMedium,
                               // color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            modifier = Modifier.clickable {
                                moreThanState.value = "0.0"
                                lessThanState.value = maxProductPrice.toString()
                                isOnHandOnlyState.value = false
                                volumesStates.clear()
//                                isMaleOnly.value = false
//                                isFemaleOnly.value = false
                            },
                            text = "Очистить всё",
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodySmall
                        )

                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceFilterRow(vararg priceStates : MutableState<String>) {

    val context = LocalContext.current

    val preferencesManager = remember {
        PreferencesManager(context = context)
    }

    val textColor = remember(preferencesManager.getThemeData(0)) {
        mutableStateOf(
            if (preferencesManager.getThemeData(0) == 1)
                Color(0xFF212121)
            else
                Color(0xFFFDF5E2)
        )
    }

    val focusStates = remember {
        mutableStateListOf(false, false)
    }

    val placeHolderTexts = listOf("От", "До")

    val extremeValues = listOf("0.0", maxProductPrice.toString())

    val defaultRange = 0f..(maxProductPrice)

    var sliderPosition by remember {
        val range = try {
            val minVal = priceStates[0].value.toFloat().round(1)
            val maxVal = (priceStates[1].value.toFloat()).round(1)
            (minVal..maxVal)
        } catch (e : Exception){
            Log.d(TAG, "PriceFilterRow: $e ${e.message}")
            defaultRange
        }
        mutableStateOf(range)
    }

//    LaunchedEffect(keys = priceStates){
//        val range = try {
//            val minVal = priceStates[0].value.toFloat().round(1)
//            val maxVal = (priceStates[1].value.toFloat()).round(1)
//            (minVal..maxVal)
//        } catch (e : Exception){
//            Log.d(TAG, "PriceFilterRow: $e ${e.message}")
//            defaultRange
//        }
//        if (range == defaultRange)
//            sliderPosition = defaultRange
//    }

    LaunchedEffect(key1 = sliderPosition){
        if (sliderPosition != defaultRange) {
            priceStates[0].value =
                (sliderPosition.start.toDouble()).round(1).toString()
            priceStates[1].value =
                (sliderPosition.endInclusive.toDouble()).round(1).toString()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 5.dp, top = 10.dp, bottom = 5.dp),
        //verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = "Цене:",
            style = MaterialTheme.typography.bodyMedium
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 5.dp, end = 5.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    //.padding(start = 5.dp, top = 10.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                repeat(2) { ind ->
                    BasicTextField(
                        modifier = Modifier
                            .height(30.dp)
                            .width(80.dp)
                            .padding(start = 5.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .onFocusChanged { focus ->
                                focusStates[ind] = focus.isFocused
                            },
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            color = textColor.value
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = if (ind == 0) ImeAction.Next else ImeAction.Done
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        value = if (priceStates[ind].value == extremeValues[ind])
                            if (focusStates[ind]) "" else placeHolderTexts[ind]
                        else
                            priceStates[ind].value,
                        onValueChange = { priceStates[ind].value = it }
                    ) { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            innerTextField()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            RangeSlider(
                value = sliderPosition,
                valueRange = defaultRange,
                onValueChange = { newRange ->
                    sliderPosition = newRange
                },
            )
        }

    }
}
//
//@Composable
//fun MaleFemale(
//    vararg states : MutableState<Boolean>,
////    isMaleOnly : MutableState<Boolean>,
////    isFemaleOnly: MutableState<Boolean>
//) {
//
//    val texts = listOf("Мужское", "Женское")
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 5.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        Text(text = "Полу:", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(end = 5.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//
//            repeat(2) { ind ->
//
//                val shape = if (ind == 0)
//                    RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
//                else
//                    RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
//
//                Button(
//                    modifier = Modifier,
//                    onClick = {
//                        states[ind].value = !states[ind].value
//                    },
//                    shape = shape,
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer,
//                        contentColor = if (states[ind].value)
//                            MaterialTheme.colorScheme.primary
//                        else
//                            MaterialTheme.colorScheme.onBackground
//                    ),
//                    border = BorderStroke(
//                        width = 2.dp, color =
//                        if (states[ind].value)
//                            MaterialTheme.colorScheme.primary
//                        else
//                            Color.LightGray
//                    ),
//                    contentPadding = PaddingValues(5.dp)
//                ) {
//
//                    Text(text = texts[ind], style = MaterialTheme.typography.bodyMedium)
//                }
//            }
//        }
//    }
//
//}

@Composable
fun VolumeOption(states: SnapshotStateList<Double>, volumes : List<Double>, isCompactQueryType: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(modifier = Modifier.padding(end = 5.dp), text = "Объему:", style = MaterialTheme.typography.bodyMedium)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(5.dp)
        ){
            itemsIndexed(volumes){ ind, volume ->
                VolumeButton(volume = volume, states = states, isCompactQueryType = isCompactQueryType)
            }
        }


//        for (ind in states.indices)
//            ValueButton(ind = ind, text = "${volumes[ind]} мл", states = states)

    }
}

@Composable
fun VolumeButton(
    volume: Double,
    states: SnapshotStateList<Double>,
    isCompactQueryType: Boolean
) {

    Button(
        onClick = {
            if (states.contains(volume))
                states.remove(volume)
            else
                states.add(volume)
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(
            width = if (states.contains(volume)) 2.dp else 1.dp,
            color = if (states.contains(volume)) Gold else Color.LightGray
        ),
        contentPadding = ButtonDefaults.ContentPadding,
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {

        val volumeStr = if (isCompactQueryType && volume == 60.0) "3x20" else volume.toInt().toString()

        Text(text = volumeStr, fontSize = 10.sp, color = Color.Black)
    }
}

@Composable
fun IsOnHandOnlyOption(selected : MutableState<Boolean>, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = 5.dp,
                bottom = 5.dp,
                //start = 5.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            modifier = Modifier
                //.padding(end = 5.dp)
            ,
            selected = selected.value,
            onClick = { selected.value = !selected.value },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.primary,
                disabledSelectedColor = MaterialTheme.colorScheme.background,
                disabledUnselectedColor = MaterialTheme.colorScheme.background
            )
        )

        //Spacer(modifier = Modifier.width(30.dp))

        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}