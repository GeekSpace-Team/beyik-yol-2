package com.android.beyikyol2.feature_car.data.remote.dto.addCar

data class Brand(
    val createdAt: String,
    val description: String,
    val id: Int,
    val image: String,
    val models: List<Model>,
    val name: String,
    val status: String,
    val updatedAt: String
)