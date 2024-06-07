package com.mertg.shoppingapp.view

import ProductViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.mertg.shoppingapp.R
import com.mertg.shoppingapp.model.Product
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.ui.theme.Orange

@Composable
fun HomeScreen(navController: NavController, viewModel: ProductViewModel = viewModel()) {
    val productList by viewModel.productList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchText by remember { mutableStateOf("") }

    val filteredProducts = productList.filter {
        searchText.isEmpty() || it.name.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            SearchBar(
                text = searchText,
                onValueChange = { searchText = it },
                onSearch = {
                    // This is where the search logic is applied, currently it filters as you type.
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Sana Özel Ürünler", style = MaterialTheme.typography.titleMedium,modifier = Modifier.padding(16.dp))
            CategoriesRow()
            if (isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            } else {
                LazyRow(modifier = Modifier.padding(8.dp)) {
                    items(filteredProducts) { product ->
                        ProductCard(product, onClick = {
                            navController.navigate(Screen.DetailPage.passItemId(product.id))
                        })
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))

                Image(painter = painterResource(id = R.drawable.sale), contentDescription = "Sale",
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop)
            }
        }
    }
}

@Composable
fun CategoriesRow() {
    val categories = listOf("Giyim", "Elektronik", "Mutfak", "Kitap", "Spor")
    LazyRow(modifier = Modifier.fillMaxWidth()
        .padding(start = 18.dp)) {
        items(categories) { category ->
            CategoryChip(category = category)
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Button(
        onClick = { /* Kategoriye tıklandığında yapılacak işlem */ },
        colors = ButtonDefaults.buttonColors(containerColor = Orange),
        modifier = Modifier.padding(horizontal = 4.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(category)
    }
}


@Composable
fun SearchBar(
    text: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        placeholder = { Text("Search") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(painter = painterResource(id = R.drawable.search), contentDescription = "Search")
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
            .width(180.dp)  // Adjusted width
            .height(250.dp),  // Adjusted height
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            val imagePainter = rememberImagePainter(data = product.imageUrl)
            Image(
                painter = imagePainter,
                contentDescription = "Product Image",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(

            ) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "${product.price} ₺", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            Spacer(modifier = Modifier.height(10.dp))
            

        }
    }
}



