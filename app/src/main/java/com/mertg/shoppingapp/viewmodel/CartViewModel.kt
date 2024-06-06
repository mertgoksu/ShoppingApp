package com.mertg.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mertg.shoppingapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<Pair<Product, Int>>>(emptyList())
    val cartItems: StateFlow<List<Pair<Product, Int>>> = _cartItems

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getCartItems() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = auth.currentUser?.uid ?: return@launch
            val cartRef = db.collection("carts").document(userId)

            try {
                val document = cartRef.get().await()
                val productEntries = document.data?.entries ?: emptySet()
                val products = mutableListOf<Pair<Product, Int>>()

                for ((productId, quantity) in productEntries) {
                    val productDoc = db.collection("products").document(productId).get().await()
                    val product = productDoc.toObject(Product::class.java)?.copy(id = productId)
                    if (product != null && quantity is Long) {
                        products.add(Pair(product, quantity.toInt()))
                    }
                }

                _cartItems.value = products
            } catch (e: Exception) {
                _cartItems.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addItemToCart(product: Product) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            db.collection("carts").document(userId).collection("items").document(product.id)
                .set(product)
                .addOnSuccessListener {
                    getCartItems() // Refresh cart items
                }
        }
    }

    fun removeItemFromCart(productId: String) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            db.collection("carts").document(userId).collection("items").document(productId)
                .delete()
                .addOnSuccessListener {
                    getCartItems() // Refresh cart items
                }
        }
    }
}
