package com.mertg.shoppingapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mertg.shoppingapp.model.Product
import com.mertg.shoppingapp.viewmodel.ProductViewModel

@Composable
fun DetailScreen(navController: NavController, productId: String, viewModel: ProductViewModel = viewModel()) {
    var product by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(productId) {
        viewModel.getProductById(productId,
            onSuccess = { product = it },
            onFailure = { product = null }
        )
    }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Product not found")
        }
    } else {
        Column(Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(8f)) {
                Image(
                    painter = rememberAsyncImagePainter(model = product!!.imageUrl),
                    contentDescription = product!!.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(100.dp)
                )
            }

            Column(Modifier.weight(2f)) {
                Text(text = product!!.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product!!.description)
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(
                        onClick = { /* Add to cart logic */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add to Cart")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { /* Add to favorites logic */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add to Favorites")
                    }
                }
            }
        }
    }
}
