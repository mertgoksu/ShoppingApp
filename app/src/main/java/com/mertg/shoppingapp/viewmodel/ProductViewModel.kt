package com.mertg.shoppingapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.mertg.shoppingapp.model.Product
import com.mertg.shoppingapp.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            val db = FirebaseFirestore.getInstance()
            db.collection("products").get()
                .addOnSuccessListener { documents ->
                    val products = documents.mapNotNull { it.toObject(Product::class.java) }
                    _productList.value = products
                    _isLoading.value = false
                }
                .addOnFailureListener {
                    _isLoading.value = false
                }
        }
    }
    fun uploadProduct(
        name: String,
        description: String,
        imageUri: String,
        context: Context,
        navController: NavController
    ) {
        val db = FirebaseFirestore.getInstance()
        val productId = db.collection("products").document().id // Benzersiz kimlik oluşturuluyor
        val product = hashMapOf(
            "id" to productId, // id alanı ekleniyor
            "name" to name,
            "description" to description,
            "imageUrl" to imageUri // URI string olarak kaydediliyor
        )

        db.collection("products")
            .document(productId) // Document ID olarak oluşturulan kimlik kullanılıyor
            .set(product)
            .addOnSuccessListener {
                Toast.makeText(context, "Product uploaded successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.HomePage.route) {
                    popUpTo(Screen.UploadItem.route) { inclusive = true }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error uploading product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    fun getProductById(productId: String, onSuccess: (Product) -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("products").whereEqualTo("id", productId).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val product = documents.documents[0].toObject(Product::class.java)
                    product?.let { onSuccess(it) } ?: onFailure(Exception("Product not found"))
                } else {
                    onFailure(Exception("Product not found"))
                }
            }
            .addOnFailureListener { onFailure(it) }
    }

}
