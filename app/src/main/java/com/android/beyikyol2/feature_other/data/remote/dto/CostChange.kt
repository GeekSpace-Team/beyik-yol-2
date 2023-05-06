package com.android.beyikyol2.feature_other.data.remote.dto

data class CostChange(
    val carId: Int,
    val costType: String,
    val createdAt: String,
    val description: String,
    val id: Int,
    val mile: Double,
    val nextMile: Double,
    val price: Double,
    val reminder: Boolean,
    val updatedAt: String,
    val volume: Double
)