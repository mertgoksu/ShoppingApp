package com.mertg.shoppingapp.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mertg.shoppingapp.model.Product
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.ui.theme.Orange
import com.mertg.shoppingapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, productId: String, viewModel: ProductViewModel = viewModel()) {
    var product by remember { mutableStateOf<Product?>(null) }
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        viewModel.getProductById(productId,
            onSuccess = { product = it },
            onFailure = { product = null }
        )
        viewModel.checkIfFavorite(productId) { favorite ->
            isFavorite = favorite
        }
    }

    // Handle back press
    BackHandler {
        navController.navigate(Screen.HomePage.route)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (product == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Product not found")
            }
        } else {
            product?.let { p ->
                Column(Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = rememberAsyncImagePainter(model = p.imageUrl),
                        contentDescription = p.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = p.name, style = MaterialTheme.typography.headlineSmall)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = p.description, style = MaterialTheme.typography.titleMedium)

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(text = "${p.price} ₺", style = MaterialTheme.typography.titleMedium)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { viewModel.addToCart(p.id,p.name,context) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Orange, // Buton arka plan rengi
                                    contentColor = Color.White // Buton içindeki metnin rengi
                                )
                            ) {
                                Text("Add to Cart")
                            }
                            IconButton(onClick = {
                                isFavorite = !isFavorite
                                viewModel.toggleFavorite(p, isFavorite)
                            }) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Toggle Favorite",
                                    tint = if (isFavorite) Color.Red else Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
