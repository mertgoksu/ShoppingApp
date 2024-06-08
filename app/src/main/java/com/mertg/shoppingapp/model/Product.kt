package com.mertg.shoppingapp.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val price: Double= 0.0,
    val imageUrl: String = ""
)
