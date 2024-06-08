package com.mertg.shoppingapp.view

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mertg.shoppingapp.navigation.Screen
import com.mertg.shoppingapp.ui.theme.Orange
import com.mertg.shoppingapp.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, viewModel: CartViewModel) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var cardNumber by remember { mutableStateOf(TextFieldValue("")) }
    var expirationDate by remember { mutableStateOf(TextFieldValue("")) }
    var cvv by remember { mutableStateOf(TextFieldValue("")) }

    // Handle back press
    BackHandler {
        navController.navigate(Screen.HomePage.route)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ödeme", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
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
                value = cardNumber,
                onValueChange = {
                    val formatted = formatCardNumber(it.text)
                    cardNumber = if (formatted.length <= 19) {
                        TextFieldValue(
                            text = formatted,
                            selection = TextRange(formatted.length)
                        )
                    } else cardNumber
                },
                label = { Text("Kredi Kartı Numarası") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = expirationDate,
                    onValueChange = { newValue ->
                        expirationDate = formatExpirationDate(newValue)
                    },
                    label = { Text("SKT (AA/YY)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Gray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Orange,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Orange
                    ),
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                TextField(
                    value = cvv,
                    onValueChange = {
                        if (it.text.length <= 3) {
                            cvv = TextFieldValue(
                                text = it.text.filter { char -> char.isDigit() },
                                selection = TextRange(it.text.length)
                            )
                        }
                    },
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Gray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Orange,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Orange
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    Toast.makeText(context, "Siparişiniz alındı!", Toast.LENGTH_LONG).show()
                    viewModel.clearCart()
                    navController.popBackStack(Screen.CartPage.route, inclusive = false)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Siparişi Tamamla")
            }
        }
    }
}

fun formatCardNumber(input: String): String {
    val digits = input.filter { it.isDigit() }
    val formatted = StringBuilder()
    for (i in digits.indices) {
        formatted.append(digits[i])
        if ((i + 1) % 4 == 0 && i != digits.lastIndex) {
            formatted.append(" ")
        }
    }
    return formatted.toString()
}

fun formatExpirationDate(value: TextFieldValue): TextFieldValue {
    val digits = value.text.filter { it.isDigit() }
    var formatted = when {
        digits.length <= 2 -> digits
        digits.length <= 4 -> {
            val month = digits.substring(0, minOf(2, digits.length)).toIntOrNull()
            if (month != null && month in 1..12) {
                if (digits.length > 2) "${digits.substring(0, 2)}/${digits.substring(2)}" else digits
            } else {
                digits.substring(0, 1) // Keep the first digit if the month is invalid
            }
        }
        else -> "${digits.substring(0, 2)}/${digits.substring(2, 4)}"
    }

    // Handle deletion of slash
    if (value.selection.start == value.selection.end &&
        value.selection.start < value.text.length &&
        value.text[value.selection.start] == '/') {
        formatted = formatted.replace("/", "")
    }

    val cursorPosition = if (formatted.length == 3 && formatted.contains('/')) 3 else formatted.length
    return TextFieldValue(text = formatted, selection = TextRange(cursorPosition))
}
