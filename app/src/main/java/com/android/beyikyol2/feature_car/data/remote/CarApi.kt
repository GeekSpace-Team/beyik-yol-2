package com.android.beyikyol2.feature_car.data.remote

import com.android.beyikyol2.feature_car.data.remote.dto.addCar.AddCarBody
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.CountResponse
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.GetAddCarDetails
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDto
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDtoItem
import com.android.beyikyol2.feature_car.data.remote.dto.Image
import com.android.beyikyol2.feature_other.data.remote.dto.CarImage
import okhttp3.MultipartBody
import retrofit2.http.*

interface CarApi {

    @GET("/cars/get-user-cars")
    suspend fun getUserCars(@Header("Authorization") token: String): GetUserCarsDto?

    @GET("/cars/get-car-by-id/{id}")
    suspend fun getCarById(@Header("Authorization") token: String, @Path("id") id: String): GetUserCarsDtoItem?

    @DELETE("/cars/delete-car/{id}")
    suspend fun deleteCar(@Header("Authorization") token: String, @Path("id") id: String): GetUserCarsDtoItem?

    @GET("/cars/get-add-car-details")
    suspend fun getAddCarDetails(): GetAddCarDetails?

    @POST("/cars/add-car")
    suspend fun addCar(@Body body: AddCarBody,@Header("Authorization") token: String): GetUserCarsDtoItem?

    @PATCH("/cars/update-car/{id}")
    suspend fun updateCar(@Path("id") id: String,@Body body: AddCarBody,@Header("Authorization") token: String): GetUserCarsDtoItem?

    @Multipart
    @POST("/car-image/add-image/{id}")
    suspend fun addCarImage(
        @Path("id") id: String,
        @Part images: List<MultipartBody.Part?>,
        @Header("Authorization") token: String
    ): CountResponse?

    @DELETE("/car-image/remove-car-image/{id}")
    suspend fun deleteCarImage(@Header("Authorization") token: String,@Path("id") id: String): Image?

}