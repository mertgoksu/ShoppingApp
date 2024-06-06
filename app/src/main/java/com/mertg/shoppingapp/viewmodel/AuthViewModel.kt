package com.mertg.shoppingapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mertg.shoppingapp.navigation.Screen

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure()
                }
            }
    }

    fun register(email: String, password: String, context: Context, navController: NavController) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.UploadItem.route) {
                        popUpTo(Screen.RegisterPage.route) { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkAndCreateCart() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.collection("carts").document(userId)

        cartRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                cartRef.set(hashMapOf<String, Any>())
            }
        }
    }
}
