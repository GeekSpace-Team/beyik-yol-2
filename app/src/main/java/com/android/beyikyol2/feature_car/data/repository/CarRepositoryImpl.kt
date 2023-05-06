package com.android.beyikyol2.feature_car.data.repository

import android.util.Log
import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_car.data.remote.CarApi
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.GetAddCarDetails
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDto
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDtoItem
import com.android.beyikyol2.feature_car.data.remote.dto.Image
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.AddCarBody
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.CountResponse
import com.android.beyikyol2.feature_car.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class CarRepositoryImpl(
    val api: CarApi
) : CarRepository{
    override fun getUserCars(token: String): Flow<Resource<GetUserCarsDto>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getUserCars(token)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun getCarById(token: String, id: String): Flow<Resource<GetUserCarsDtoItem>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getCarById(token,id)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun deleteCar(token: String, id: String): Flow<Resource<GetUserCarsDtoItem>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.deleteCar(token,id)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun getAddCarDetails(): Flow<Resource<GetAddCarDetails>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getAddCarDetails()
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun addCar(body: AddCarBody, token: String): Flow<Resource<GetUserCarsDtoItem>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.addCar(body,token)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            Log.e("Add",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Add",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun addCarImage(
        images: List<File?>,
        id: String,
        token: String
    ): Flow<Resource<CountResponse>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        val list = images.map { file->
            file?.asRequestBody()?.let { MultipartBody.Part.createFormData("image",file?.name, it) }
        }

        try {
            val remote = api.addCarImage(id,list,token)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            Log.e("Add image",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Add image",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun removeCarImage(id: String, token: String): Flow<Resource<Image>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.deleteCarImage(token,id)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            Log.e("Add",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Add",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun updateCar(
        id: String,
        body: AddCarBody,
        token: String
    ): Flow<Resource<GetUserCarsDtoItem>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.updateCar(id,body,token)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            Log.e("Add",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Add",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }
}