package com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui

//
//@Composable
//fun LazyProductList(
//    modifier: Modifier = Modifier,
//    listOfProducts: List<Product>,
//    onProductClick: (String) -> Unit,
//    onAddToFavouriteClick: (Product) -> Unit,
//    onRemoveFromFavouriteClick: (String) -> Unit,
//    isInFavouriteCheck: (String) -> Boolean,
//    userScrollEnabled: Boolean = true,
//    onAddToCartClick: (ProductWithAmount) -> Unit,
//    onRemoveFromCartClick: (String) -> Unit,
//    isInCartCheck: (String) -> Boolean,
//) {
//
//    LazyColumn(
//        contentPadding = PaddingValues(3.dp),
//        modifier = modifier
//            .fillMaxWidth()
//            .wrapContentHeight(),
//        state = rememberLazyListState(),
//        userScrollEnabled = userScrollEnabled,
//    ) {
//        itemsIndexed(listOfProducts, key = {i, p -> p.id ?: i}) { ind, product ->
//            ProductRow(
//                productWithAmount = ProductWithAmount(product, 1, true),
//                onProductClick = onProductClick,
//                onAddToFavouriteClick = onAddToFavouriteClick,
//                onAddToCartClick = onAddToCartClick,
//                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
//                onRemoveFromCartClick = onRemoveFromCartClick,
//                isInFavouriteCheck = isInFavouriteCheck,
//                isInCartCheck = isInCartCheck,
//                onAmountChange =
//            )
////            ProductRow(
////                product = product,
////                onProductClick = onProductClick,
////                onAddToFavouriteClick = onAddToFavouriteClick,
////                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
////                isInFavouriteCheck = isInFavouriteCheck,
////                onAddToCartClick = onAddToCartClick,
////                onRemoveFromCartClick = onRemoveFromCartClick,
////                isInCartCheck = isInCartCheck,
////            )
//        }
//    }
//}

//
//@Composable
//fun ProductRow(
//    product: Product,
//    onProductClick : (String) -> Unit,
//    onAddToFavouriteClick : (Product) -> Unit,
//    onRemoveFromFavouriteClick : (String) -> Unit,
//    isInFavouriteCheck : (String) -> Boolean,
//    onAddToCartClick: (ProductWithAmount) -> Unit,
//    onRemoveFromCartClick: (String) -> Unit,
//    isInCartCheck: (String) -> Boolean,
//) {
//
//    val context = LocalContext.current
//
//    val isInFavourite = remember {
//        mutableStateOf(isInFavouriteCheck(product.id ?: ""))
//    }
//
//    val isInCart = remember {
//        mutableStateOf(isInCartCheck(product.id ?: ""))
//    }
//
//    Card(
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
//        shape = RoundedCornerShape(10.dp),
//        border = BorderStroke(1.dp, Color.LightGray),
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .clickable { onProductClick(product.id.toString()) }
//    ) {
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//
//            val cashPrice = buildAnnotatedString {
//                withStyle(style = MaterialTheme.typography.bodyLarge.toSpanStyle()) {
//                    append(product.cashPrice?.round(2).toString() + " бр.")
//                }
//                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()){
//                    append(" (нал.)")
//                }
//            }
//
//            val cashlessPrice = buildAnnotatedString {
//                withStyle(style = MaterialTheme.typography.bodyLarge.toSpanStyle()) {
//                    append(product.cashlessPrice?.round(2).toString() + " бр.")
//                }
//                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()){
//                    append(" (безнал.)")
//                }
//            }
//
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(0.75f)
//                    .wrapContentHeight()
//                    .padding(start = 10.dp)
//            ) {
//                Text(text = product.brand.toString(), style = MaterialTheme.typography.bodyLarge)
//
//                Text(text = product.volume.toString(), style = MaterialTheme.typography.bodyMedium)
//
//                Divider()
//
//                Text(text = cashPrice.text + "\t" + cashlessPrice.text)
//
//            }
//
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .padding(start = 10.dp, end = 10.dp),
//                //verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(
//                    modifier = Modifier.size(20.dp),
//                    onClick = {
//                        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
//                            showToast(context, "Вы не авторизованы")
//                        else {
//                            if (isInFavourite.value)
//                                onRemoveFromFavouriteClick(product.id!!)
//                            else
//                                onAddToFavouriteClick(product)
//                            isInFavourite.value = !isInFavourite.value
//                        }
//                    }
//                ) {
//                    Icon(
//                        imageVector = if (isInFavourite.value) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
//                        contentDescription = "fav icon",
//                        tint = if (isInFavourite.value) Gold else Color.Black
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(5.dp))
//
//                IconButton(
//                    modifier = Modifier.size(30.dp),
//                    onClick = {
//                        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
//                            showToast(context, "Вы не авторизованы")
//                        else {
//                            if (isInCart.value) onRemoveFromCartClick(product.id!!)
//                            else onAddToCartClick(
//                                ProductWithAmount(
//                                    product = product,
//                                    amount = 1,
//                                    isCashPrice = true
//                                )
//                            )
//                            isInCart.value = !isInCart.value
//                        }
//                    }
//                ) {
//                    Icon(
//                        imageVector = if (isInCart.value)
//                            Icons.Filled.ShoppingCart
//                        else
//                            Icons.Outlined.ShoppingCart,
//                        contentDescription = "cart icon",
//                        tint = if (isInCart.value) Gold else Color.Black
//                    )
//                }
//
//            }
//        }
//    }
//}