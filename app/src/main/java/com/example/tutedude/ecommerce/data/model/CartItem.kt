package com.example.tutedude.ecommerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productId: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val quantity: Int = 1,
    val category: String = ""
)