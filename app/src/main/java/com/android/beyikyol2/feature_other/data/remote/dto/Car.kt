package com.android.beyikyol2.feature_other.data.remote.dto

data class Car(
    val carEngineType: CarEngineType,
    val carModel: CarModel,
    val carOption: CarOption,
    val carTransmition: CarTransmition,
    val costChange: List<CostChange>,
    val createdAt: String,
    val enginePower: Double,
    val engineTypeId: Int,
    val id: Int,
    val images: List<CarImage>,
    val lastMile: Int,
    val modelId: Int,
    val name: String,
    val optionId: Int,
    val phoneNumber: String,
    val status: String,
    val transmitionId: Int,
    val updatedAt: String,
    val users: Users,
    val usersId: Int,
    val uuid: String,
    val vinCode: String,
    val year: Int
)