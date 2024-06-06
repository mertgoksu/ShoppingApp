package com.mertg.shoppingapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mertg.shoppingapp.viewmodel.ProductViewModel
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.util.ProductCard

@Composable
fun HomeScreen(navController: NavController, viewModel: ProductViewModel = viewModel(), modifier: Modifier = Modifier) {
    val productList by viewModel.productList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }

    if (isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
            Text(text = "Loading...")
        }
    } else {
        LazyRow(modifier = modifier.padding(16.dp)) {
            items(productList) { product ->
                ProductCard(product = product, onClick = {
                    navController.navigate(Screen.DetailPage.passItemId(product.id))
                })
            }
        }
    }
}
