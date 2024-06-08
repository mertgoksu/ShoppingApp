package com.mertg.shoppingapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mertg.shoppingapp.R
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    val db = FirebaseFirestore.getInstance()

    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        userId?.let {
            db.collection("users").document(it).get().addOnSuccessListener { document ->
                if (document != null) {
                    userName = document.getString("name") ?: ""
                    userEmail = document.getString("email") ?: ""
                    userPhone = document.getString("phone") ?: ""
                    userAddress = document.getString("address") ?: ""
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp)
                )
                Text(text = "Hoşgeldiniz, $userName", style = MaterialTheme.typography.titleLarge)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Email: $userEmail", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Telefon: $userPhone", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = "Adres: $userAddress", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(onClick = { navController.navigate(Screen.EditProfileScreen.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange, // Buton arka plan rengi
                        contentColor = Color.White // Buton içindeki metnin rengi
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Bilgileri Değiştir")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.LoginPage.route) {
                        popUpTo(Screen.ProfilePageScreen.route) { inclusive = true }
                    }
                },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange, // Buton arka plan rengi
                        contentColor = Color.White // Buton içindeki metnin rengi
                    ),
                    ) {
                    Text("Çıkış Yap")
                }
            }
        }
    }
}
