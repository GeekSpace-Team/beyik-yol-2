package com.android.beyikyol2.feature_car.presentation

import com.android.beyikyol2.feature_car.data.remote.dto.addCar.GetAddCarDetails
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDto
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDtoItem
import com.android.beyikyol2.feature_car.data.remote.dto.Image
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.CountResponse

data class CarState(
    val cars: GetUserCarsDto? = null,
    val singleCar: GetUserCarsDtoItem? = null,
    val addCarDetails: GetAddCarDetails? = null,
    val addCarState: GetUserCarsDtoItem? = null,
    val updateCarState: GetUserCarsDtoItem? = null,
    val addCarImageState: CountResponse? = null,
    val removeCarImageState: Image? = null
)