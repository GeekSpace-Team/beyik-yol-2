package com.android.beyikyol2.feature_car.data.remote.dto.addCar

data class GetAddCarDetails(
    val brand: List<Brand>,
    val engine: List<Engine>,
    val option: List<Option>,
    val transmition: List<Transmition>
)