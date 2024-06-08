package com.mertg.shoppingapp.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.ui.theme.Orange
import com.mertg.shoppingapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmPasswordState = remember { mutableStateOf("") }
    val phoneState = remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hesap Oluştur", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Ad Soyad") },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Orange,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Orange
                ),
                leadingIcon = { Icon(Icons.Default.Face, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("E-posta") },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Orange,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Orange
                ),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = phoneState.value,
                onValueChange = {
                    if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                        phoneState.value = it
                    }
                },
                label = { Text("Telefon") },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Orange,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Orange
                ),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                placeholder = { Text("5xx xxx xx xx") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Şifre") },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Orange,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Orange
                ),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = confirmPasswordState.value,
                onValueChange = { confirmPasswordState.value = it },
                label = { Text("Şifreyi Onayla") },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Orange,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Orange
                ),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (passwordState.value == confirmPasswordState.value) {
                        if (phoneState.value.length != 10) {
                            Toast.makeText(context, "Telefon numarası 10 haneli olmalıdır", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.register(
                                name = nameState.value,
                                email = emailState.value,
                                password = passwordState.value,
                                phone = phoneState.value,
                                context = context,
                                navController = navController
                            )
                        }
                    } else {
                        Toast.makeText(context, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange, // Buton arka plan rengi
                    contentColor = Color.White // Buton içindeki metnin rengi
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kayıt Ol")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = { navController.navigate(Screen.LoginPage.route) }
            ) {
                Text("Zaten hesabınız var mı? Giriş yapın")
            }
        }
    }
}
