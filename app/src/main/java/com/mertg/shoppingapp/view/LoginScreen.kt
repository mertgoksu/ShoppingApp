package com.mertg.shoppingapp.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = {
                email = it
                emailError = it.isEmpty()
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1F),
                errorLeadingIconColor = MaterialTheme.colorScheme.onError,
                errorTrailingIconColor = MaterialTheme.colorScheme.onError,
                focusedIndicatorColor = if (emailError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        )


        if (emailError) {
            Text(
                text = "Email cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = it.isEmpty()
            },
            label = { Text("Şifre") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1F),
                errorLeadingIconColor = MaterialTheme.colorScheme.onError,
                errorTrailingIconColor = MaterialTheme.colorScheme.onError,
                focusedIndicatorColor = if (passwordError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        )


        if (passwordError) {
            Text(
                text = "Password cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                emailError = email.isEmpty()
                passwordError = password.isEmpty()

                if (!emailError && !passwordError) {
                    isLoading = true
                    viewModel.signInWithEmailAndPassword(email, password,
                        onSuccess = {
                            navController.navigate(Screen.HomePage.route) {
                                popUpTo(Screen.LoginPage.route) { inclusive = true }
                            }
                        },
                        onFailure = {
                            isLoading = false
                            Toast.makeText(context, "Eşleşen kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}



