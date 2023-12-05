package com.example.perfumeshop.presentation.features.search.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.perfumeshop.data.utils.Constants
import com.example.perfumeshop.presentation.features.search.components.secondary.IsOnHandOnlyOption
import com.example.perfumeshop.presentation.features.search.components.secondary.PriceFilterRow
import com.example.perfumeshop.presentation.features.search.components.secondary.VolumeOption


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
                    (lessThanState.value != Constants.MAX_PRODUCT_PRICE.toString() && lessThanState.value.isNotEmpty()) ||
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
                                    lessThanState.value.ifEmpty { Constants.MAX_PRODUCT_PRICE.toString() },
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
                                lessThanState.value = Constants.MAX_PRODUCT_PRICE.toString()
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
