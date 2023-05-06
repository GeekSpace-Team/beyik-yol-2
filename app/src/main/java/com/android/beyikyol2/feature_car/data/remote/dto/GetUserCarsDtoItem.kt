package com.android.beyikyol2.feature_car.data.remote.dto

data class GetUserCarsDtoItem(
    val CarView: List<Any>,
    val carEngineType: CarEngineType,
    val carModel: CarModel,
    val carOption: CarOption,
    val carShare: List<Any>,
    val carTransmition: CarTransmition,
    val costChange: List<CostChange>,
    val costFuel: List<Any>,
    val costRepair: List<Any>,
    val createdAt: String,
    val enginePower: Double,
    val engineTypeId: Int,
    val id: Int,
    var images: List<Image>,
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