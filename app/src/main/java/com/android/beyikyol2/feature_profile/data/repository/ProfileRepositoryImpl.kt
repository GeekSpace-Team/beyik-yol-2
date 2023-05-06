package com.android.beyikyol2.feature_profile.data.repository

import android.util.Log
import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_profile.data.remote.ProfileApi
import com.android.beyikyol2.feature_profile.data.remote.dto.*
import com.android.beyikyol2.feature_profile.data.remote.dto.profile.GetProfileDto
import com.android.beyikyol2.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class ProfileRepositoryImpl(
    private val api: ProfileApi
): ProfileRepository {
    override fun checkPhoneNumber(phone: String): Flow<Resource<CheckPhoneNumberResponse>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remoteHome = api.checkPhoneNumber(phone)
            try {
                emit(Resource.Success(remoteHome))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }



    }

    override fun sendNumber(phone: String): Flow<Resource<CheckedNumberDto>> = flow {
        Log.e("ProfileRepositoryImpl-1",phone)
        emit(Resource.Loading())
        Log.e("ProfileRepositoryImpl-2",phone)
        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }
        Log.e("ProfileRepositoryImpl-3",phone)
        emit(Resource.Loading(data = null))
        Log.e("ProfileRepositoryImpl-4",phone)
        try {
            Log.e("ProfileRepositoryImpl-5",phone)
            val remote = api.sendNumber(SendNumber(phone))
            Log.e("ProfileRepositoryImpl-6",phone)
            emit(Resource.Success(remote))
            Log.e("ProfileRepositoryImpl-7",phone)
        } catch (e: HttpException){
            Log.e("ProfileRepositoryImpl-8",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("ProfileRepositoryImpl-9",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun checkCode(phone: String, uuid: String): Flow<Resource<CheckCodeDto>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.checkCode(CheckCode(phone,uuid))
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun getProfile(token: String): Flow<Resource<GetProfileDto>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getProfile(token)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun changeImage(file: File, token: String): Flow<Resource<GetProfileDto>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.changeImage(
                image = MultipartBody.Part
                    .createFormData("image",file.name, file.asRequestBody()),
                token
            )
            try {
                emit(Resource.Success(remote))
            } catch (ex: java.lang.Exception){
                Log.e("Error image",ex.message.toString())
            }
        } catch (e: HttpException){
            Log.e("Error image",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Error image",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun editProfile(body: EditProfile, token: String): Flow<Resource<GetProfileDto>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.editProfile(body,token)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){

            }
        } catch (e: HttpException){
            Log.e("Error image",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Error image",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun saveFcmToken(token: String, body: SaveFcmTokenDto): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.saveFcmToken(token,body)
            try {
                emit(Resource.Success("Success"))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }
}