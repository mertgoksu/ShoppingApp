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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mertg.shoppingapp.ui.theme.Orange
import com.mertg.shoppingapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Gray,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Orange,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Orange
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CategoryDropdown(category)

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Açıklama") },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Gray,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Orange,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Orange
            ),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Fiyat") },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Gray,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Orange,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Orange
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { imagePicker.launch("image/*") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange, // Buton arka plan rengi
                contentColor = Color.White // Buton içindeki metnin rengi
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
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
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange, // Buton arka plan rengi
                contentColor = Color.White // Buton içindeki metnin rengi
            ),
            modifier = Modifier.fillMaxWidth(),
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
