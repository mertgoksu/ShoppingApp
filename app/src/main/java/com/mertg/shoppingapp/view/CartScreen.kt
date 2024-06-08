package com.mertg.shoppingapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mertg.shoppingapp.model.Product
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.ui.theme.Orange
import com.mertg.shoppingapp.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var totalAmount by remember { mutableStateOf(0.0) }

    // Total amount calculation
    DisposableEffect(cartItems) {
        totalAmount = cartItems.sumOf { it.first.price * it.second }
        onDispose {}
    }

    // Fetch cart items whenever the component is first composed
    DisposableEffect(Unit) {
        viewModel.getCartItems()
        onDispose {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sepet", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator()
            }
        } else if (cartItems.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text("Sepetiniz boş")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { (product, quantity) ->
                        ProductCard(
                            product = product,
                            quantity = quantity,
                            onQuantityChange = { newQuantity ->
                                viewModel.updateQuantity(product.id, newQuantity)
                            },
                            onDelete = {
                                viewModel.removeFromCart(product.id)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Toplam: ${"%.2f".format(totalAmount)} TL",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate(Screen.PaymentPage.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,
                        contentColor = Color.White
                    ),
                ) {
                    Text("Sipariş Ver")
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (quantity > 1) onQuantityChange(quantity - 1)
                        else onDelete()
                    }) {
                        Text(text = "-", style = MaterialTheme.typography.titleLarge)
                    }
                    Text(text = "$quantity", style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = { onQuantityChange(quantity + 1) }) {
                        Text(text = "+", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.align(Alignment.Top)
            ) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Orange)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${"%.2f".format(product.price)} TL",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
