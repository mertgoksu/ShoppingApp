package com.mertg.shoppingapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.ui.theme.Orange
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        userId?.let {
            db.collection("users").document(it).get().addOnSuccessListener { document ->
                if (document != null) {
                    name = document.getString("name") ?: ""
                    phone = document.getString("phone") ?: ""
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Düzenle", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("İsim Soyisim") },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Orange,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Orange
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = phone,
                onValueChange = {
                    if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                        phone = it
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
                placeholder = { Text("5xx xxx xx xx") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Yeni Şifre") },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Orange,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Orange
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Yeni Şifreyi Onayla") },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Orange,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Orange
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (password == confirmPassword) {
                        if (phone.length != 10) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Telefon numarası 10 haneli olmalıdır")
                            }
                        } else {
                            userId?.let {
                                val userData = mapOf(
                                    "name" to name,
                                    "phone" to phone
                                )
                                if (password.isNotEmpty()) {
                                    currentUser?.updatePassword(password)
                                        ?.addOnSuccessListener {
                                            db.collection("users").document(userId)
                                                .update(userData)
                                                .addOnSuccessListener {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar("Bilgiler güncellendi")
                                                        navController.navigate(Screen.ProfilePageScreen.route) {
                                                            popUpTo(Screen.EditProfileScreen.route) { inclusive = true }
                                                        }
                                                    }
                                                }
                                                .addOnFailureListener { exception ->
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar("Güncelleme başarısız: ${exception.message}")
                                                    }
                                                }
                                        }
                                        ?.addOnFailureListener { exception ->
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Şifre güncelleme başarısız: ${exception.message}")
                                            }
                                        }
                                } else {
                                    db.collection("users").document(userId)
                                        .update(userData)
                                        .addOnSuccessListener {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Bilgiler güncellendi")
                                                navController.navigate(Screen.HomePage.route) {
                                                    popUpTo(Screen.EditProfileScreen.route) { inclusive = true }
                                                }
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Güncelleme başarısız: ${exception.message}")
                                            }
                                        }
                                }
                            }
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Şifreler uyuşmuyor")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange, // Buton arka plan rengi
                    contentColor = Color.White // Buton içindeki metnin rengi
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Güncelle")
            }
        }
    }
}
