package com.mertg.shoppingapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.ui.theme.Orange
import com.mertg.shoppingapp.viewmodel.FavoritesViewModel
import com.mertg.shoppingapp.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, viewModel: FavoritesViewModel) {
    val favoriteItems by viewModel.favoriteItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Ekran her gösterildiğinde favori ürünleri güncelle
    LaunchedEffect(key1 = Unit) {
        viewModel.getFavoriteItems()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoriler", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                ),
                actions = {
                    TextButton(
                        onClick = { viewModel.clearAllFavorites() },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Tümünü Kaldır")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (favoriteItems.isEmpty()) {
                Text("Henüz favori eklenmedi", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(favoriteItems) { product ->
                        FavoriteProductRow(
                            product = product,
                            onClick = {
                                navController.navigate(Screen.DetailPage.passItemId(product.id))
                            },
                            onFavoriteClick = {
                                viewModel.toggleFavorite(product)
                            },
                            isFavorite = viewModel.isFavorite(product)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteProductRow(
    product: Product,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FavoriteProductCard(
            product = product,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = {
                onFavoriteClick()
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                tint = if (isFavorite) Color.Red else Color.Gray,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun FavoriteProductCard(
    product: Product,
    onClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(model = product.imageUrl)

    Card(
        modifier = Modifier
            .fillMaxWidth(0.85f) // Kartı biraz daha daraltmak için
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = product.name,
                modifier = Modifier
                    .size(84.dp) // Biraz daha büyük resim
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f) // ColumnScope'da kullanıldığından emin olun
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
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "${"%.2f".format(product.price)} TL",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}
