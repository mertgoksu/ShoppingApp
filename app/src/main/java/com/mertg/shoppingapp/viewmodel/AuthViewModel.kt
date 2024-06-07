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

    fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String,
        context: Context,
        navController: NavController
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: return@addOnCompleteListener
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "phone" to phone,
                        "address" to address
                    )
                    db.collection("users").document(userId).set(user)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.HomePage.route) {
                                popUpTo(Screen.RegisterPage.route) { inclusive = true }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Registration Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
