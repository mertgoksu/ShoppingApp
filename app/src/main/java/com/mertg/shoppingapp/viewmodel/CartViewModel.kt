package com.mertg.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mertg.shoppingapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems: StateFlow<List<Product>> = _cartItems

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getCartItems() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            db.collection("carts").document(userId).collection("items")
                .get()
                .addOnSuccessListener { result ->
                    val items = result.map { document ->
                        document.toObject(Product::class.java).copy(id = document.id)
                    }
                    _cartItems.value = items
                    _isLoading.value = false
                }
                .addOnFailureListener {
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
