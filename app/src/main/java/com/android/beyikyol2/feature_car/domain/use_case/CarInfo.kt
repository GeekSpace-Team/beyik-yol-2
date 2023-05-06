package com.android.beyikyol2.feature_car.domain.use_case

import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.GetAddCarDetails
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDto
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDtoItem
import com.android.beyikyol2.feature_car.data.remote.dto.Image
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.AddCarBody
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.CountResponse
import com.android.beyikyol2.feature_car.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import java.io.File


class CarInfo(
    private val repository: CarRepository
){
    operator fun invoke(token: String): Flow<Resource<GetUserCarsDto>>{
        return repository.getUserCars(token)
    }

    fun getCarById(token: String, id: String): Flow<Resource<GetUserCarsDtoItem>>{
        return repository.getCarById(token,id)
    }

    fun deleteCar(token: String, id: String): Flow<Resource<GetUserCarsDtoItem>>{
        return repository.deleteCar(token,id)
    }
    fun getCarDetails(): Flow<Resource<GetAddCarDetails>>{
        return repository.getAddCarDetails()
    }
    fun addCar(body: AddCarBody, token: String): Flow<Resource<GetUserCarsDtoItem>>{
        return repository.addCar(body, token)
    }
    fun updateCar(id: String, body: AddCarBody, token: String): Flow<Resource<GetUserCarsDtoItem>>{
        return repository.updateCar(id, body, token)
    }
    fun addCarImage(images: List<File?>, id: String, token: String): Flow<Resource<CountResponse>>{
        return repository.addCarImage(images,id, token)
    }
    fun removeCarImage(id: String, token: String): Flow<Resource<Image>>{
        return repository.removeCarImage(id, token)
    }
}