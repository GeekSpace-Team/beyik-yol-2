package com.android.beyikyol2.feature_car.data.remote.dto

import com.android.beyikyol2.feature_car.data.remote.dto.addCar.Brand

data class CarModel(
    val brandId: Int,
    val createdAt: String,
    val description: String,
    val id: Int,
    val name: String,
    val status: String,
    val updatedAt: String,
    val brand: Brand
)