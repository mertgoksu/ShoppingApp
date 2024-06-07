package com.mertg.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mertg.shoppingapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _cartItems = MutableStateFlow<List<Pair<Product, Int>>>(emptyList())
    val cartItems: StateFlow<List<Pair<Product, Int>>> = _cartItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getCartItems() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = auth.currentUser?.uid ?: return@launch
            db.collection("carts").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val productIds = document.data?.keys ?: emptySet()
                    val quantities = document.data?.values ?: emptyList()

                    val cartItems = mutableListOf<Pair<Product, Int>>()
                    for ((index, productId) in productIds.withIndex()) {
                        val quantity = quantities.elementAt(index) as? Long ?: 0
                        db.collection("products").document(productId)
                            .get()
                            .addOnSuccessListener { productDoc ->
                                val product = productDoc.toObject(Product::class.java)?.copy(id = productId)
                                if (product != null) {
                                    cartItems.add(product to quantity.toInt())
                                    _cartItems.value = cartItems
                                }
                            }
                    }
                    _isLoading.value = false
                }
                .addOnFailureListener {
                    _cartItems.value = emptyList()
                    _isLoading.value = false
                }
        }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        val userId = auth.currentUser?.uid ?: return
        if (quantity <= 0) {
            removeFromCart(productId)
        } else {
            db.collection("carts").document(userId).update(productId, quantity)
            getCartItems()
        }
    }

    fun removeFromCart(productId: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("carts").document(userId).update(productId, FieldValue.delete())
            .addOnSuccessListener {
                checkIfCartIsEmpty(userId)
            }
    }

    private fun checkIfCartIsEmpty(userId: String) {
        db.collection("carts").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.data.isNullOrEmpty()) {
                    _cartItems.value = emptyList()
                } else {
                    getCartItems()
                }
            }
    }

    fun clearCart() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("carts").document(userId).delete()
            .addOnSuccessListener {
                _cartItems.value = emptyList()
            }
            .addOnFailureListener {
                // Handle failure if needed
            }
    }
}
