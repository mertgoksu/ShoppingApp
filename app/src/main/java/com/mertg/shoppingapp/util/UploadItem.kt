package com.mertg.shoppingapp.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mertg.shoppingapp.viewmodel.ProductViewModel

@Composable
fun UploadItem(navController: NavController, viewModel: ProductViewModel = viewModel()) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var category = remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        imageUri?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("İsim") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CategoryDropdown(category)

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Açıklama") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Fiyat") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { imagePicker.launch("image/*") }) {
            Text("Resim Seç")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if(price.toDoubleOrNull() != null) {
                    imageUri?.let { uri ->
                        viewModel.uploadProduct(name, description, category.value, price.toDouble(), uri, context, navController)
                    }
                } else {
                    Toast.makeText(context, "Geçerli bir değer girin", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = name.isNotEmpty() && description.isNotEmpty() && category.value.isNotEmpty() && price.isNotEmpty() && imageUri != null
        ) {
            Text("Ürün Yükle")
        }
    }
}



@Composable
fun CategoryDropdown(selectedCategory: MutableState<String>) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Moda", "Elektronik", "Mutfak", "Kitap", "Spor")

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = selectedCategory.value,
            onValueChange = { /* Bu alan kullanıcı tarafından doğrudan düzenlenemez */ },
            label = { Text("Kategori") },
            readOnly = true,  // TextField'ın kullanıcı tarafından düzenlenmesini engeller
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        selectedCategory.value = category
                        expanded = false
                    }
                )
            }
        }
    }
}
