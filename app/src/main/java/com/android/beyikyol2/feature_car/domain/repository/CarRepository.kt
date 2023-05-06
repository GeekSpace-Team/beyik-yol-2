package com.android.beyikyol2.feature_car.domain.repository

import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.GetAddCarDetails
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDto
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDtoItem
import com.android.beyikyol2.feature_car.data.remote.dto.Image
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.AddCarBody
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.CountResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import java.io.File

interface CarRepository {
    fun getUserCars(token: String): Flow<Resource<GetUserCarsDto>>
    fun getCarById(token: String, id: String): Flow<Resource<GetUserCarsDtoItem>>
    fun deleteCar(token: String, id: String): Flow<Resource<GetUserCarsDtoItem>>
    fun getAddCarDetails(): Flow<Resource<GetAddCarDetails>>
    fun addCar(body: AddCarBody, token: String): Flow<Resource<GetUserCarsDtoItem>>
    fun updateCar(id: String,body: AddCarBody, token: String): Flow<Resource<GetUserCarsDtoItem>>
    fun addCarImage(images: List<File?>,id: String, token: String): Flow<Resource<CountResponse>>
    fun removeCarImage(id: String, token: String): Flow<Resource<Image>>
}