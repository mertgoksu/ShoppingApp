package com.mertg.shoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.mertg.shoppingapp.navigation.MainScaffold
import com.mertg.shoppingapp.navigation.NavGraph
import com.mertg.shoppingapp.ui.theme.ShoppingAppTheme
import com.mertg.shoppingapp.viewmodel.AuthViewModel
import com.mertg.shoppingapp.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            /*ShoppingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }*/
            val navController = rememberNavController()
            val authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
            val productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
            ShoppingAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScaffold(
                        navController = navController,
                        authViewModel = authViewModel,
                        productViewModel = productViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShoppingAppTheme {
        Greeting("Android")
    }
}