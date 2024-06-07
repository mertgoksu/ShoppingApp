package com.mertg.shoppingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mertg.shoppingapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _favoriteItems = MutableStateFlow<List<Product>>(emptyList())
    val favoriteItems: StateFlow<List<Product>> = _favoriteItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getFavoriteItems() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = auth.currentUser?.uid ?: return@launch
            db.collection("favorites").document(userId)
                .collection("items")
                .get()
                .addOnSuccessListener { documents ->
                    val favorites = documents.mapNotNull { it.toObject(Product::class.java) }
                    _favoriteItems.value = favorites
                    _isLoading.value = false
                }
                .addOnFailureListener {
                    _favoriteItems.value = emptyList()
                    _isLoading.value = false
                }
        }
    }

    fun toggleFavorite(product: Product) {
        val userId = auth.currentUser?.uid ?: return
        val docRef = db.collection("favorites").document(userId).collection("items").document(product.id)

        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                docRef.delete()
                    .addOnSuccessListener {
                        getFavoriteItems()
                    }
            } else {
                docRef.set(product)
                    .addOnSuccessListener {
                        getFavoriteItems()
                    }
            }
        }
    }

    fun isFavorite(product: Product): Boolean {
        return favoriteItems.value.any { it.id == product.id }
    }

    fun clearAllFavorites() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("favorites").document(userId).collection("items")
            .get()
            .addOnSuccessListener { documents ->
                val batch = db.batch()
                documents.forEach { document ->
                    batch.delete(document.reference)
                }
                batch.commit().addOnSuccessListener {
                    getFavoriteItems()
                }
            }
    }
}
