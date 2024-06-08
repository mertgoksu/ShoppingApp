package com.mertg.shoppingapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mertg.shoppingapp.R
import com.mertg.shoppingapp.model.Product
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.ui.theme.Orange
import com.mertg.shoppingapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: ProductViewModel = viewModel()) {
    val productList by viewModel.productList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchText by remember { mutableStateOf("") }

    val categories = listOf("Moda", "Elektronik", "Mutfak", "Kitap", "Spor")

    val filteredProducts = productList.filter { it.name.contains(searchText, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ShoppingApp", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                SearchBar(
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = { /* Do nothing */ }
                )

                Text("Sana Özel Ürünler", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                LazyRow(modifier = Modifier.padding(8.dp)) {
                    items(filteredProducts) { product ->
                        ProductCard(product, onClick = {
                            navController.navigate(Screen.DetailPage.passItemId(product.id))
                        })
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Image(
                    painter = painterResource(id = R.drawable.sale),
                    contentDescription = "Sale",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            // Kategorilere göre ürünleri göster
            categories.forEach { category ->
                val categoryProducts = productList.filter { it.category == category }
                item {
                    CategorySection(category = category, products = categoryProducts, navController = navController)
                }
            }
        }
    }
}

@Composable
fun CategorySection(category: String, products: List<Product>, navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = category,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )
        LazyRow {
            items(products) { product ->
                ProductCard(product, onClick = {
                    navController.navigate(Screen.DetailPage.passItemId(product.id))
                })
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        placeholder = { Text("Ara") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .width(180.dp)
            .height(250.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            val imagePainter = rememberAsyncImagePainter(
                model = product.imageUrl
            )
            Image(
                painter = imagePainter,
                contentDescription = "Product Image",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${"%.2f".format(product.price)} ₺",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
