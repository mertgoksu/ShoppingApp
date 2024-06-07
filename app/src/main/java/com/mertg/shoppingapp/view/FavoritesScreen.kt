package com.mertg.shoppingapp.view

import FavoritesViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.util.ProductCard

@Composable
fun FavoritesScreen(navController: NavController, viewModel: FavoritesViewModel) {
    val favoriteItems by viewModel.favoriteItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        viewModel.getFavoriteItems()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (favoriteItems.isEmpty()) {
            Text("No favorites added yet", modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                items(favoriteItems) { product ->
                    ProductCard(product = product, onClick = {
                        navController.navigate(Screen.DetailPage.passItemId(product.id))
                    })
                }
            }
        }
    }
}
