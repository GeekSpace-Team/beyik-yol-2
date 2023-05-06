package com.android.beyikyol2.feature_cost.data.remote.dto

data class CreateCostBody(
    val carId: Int,
    val costType: String,
    val description: String="",
    val mile: Double=0.0,
    val nextMile: Double=0.0,
    val price: Double=0.0,
    val reminder: Boolean=false,
    val typeIds: List<Int>? = emptyList(),
    val volume: Double=0.0
)