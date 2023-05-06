package com.android.beyikyol2.feature_car.data.remote.dto.addCar

data class AddCarBody(
    val enginePower: Double,
    val engineTypeId: Int,
    val lastMile: Int,
    val modelId: Int,
    val name: String,
    val optionId: Int,
    val phoneNumber: String,
    val status: String,
    val transmitionId: Int,
    val usersId: Int,
    val vinCode: String,
    val year: Int
)