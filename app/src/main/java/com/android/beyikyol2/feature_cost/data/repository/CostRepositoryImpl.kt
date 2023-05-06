package com.android.beyikyol2.feature_cost.data.repository

import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_cost.data.remote.CostApi
import com.android.beyikyol2.feature_cost.data.remote.dto.CreateCostBody
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDto
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDtoItem
import com.android.beyikyol2.feature_cost.data.remote.dto.change_type.GetChangeType
import com.android.beyikyol2.feature_cost.domain.repository.CostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class CostRepositoryImpl(
    val api: CostApi
): CostRepository {
    override fun getCostByCarId(token: String, id: String,type: String): Flow<Resource<GetCostsDto>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getCostsByCarId(token,id,type)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun createCost(token: String, body: CreateCostBody): Flow<Resource<GetCostsDtoItem>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.createCost(token,body)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun getChangeType(): Flow<Resource<GetChangeType>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getChangeTypes()
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun getCostById(token: String, id: String): Flow<Resource<GetCostsDtoItem>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getCostsById(token,id)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun updateCost(
        token: String,
        body: CreateCostBody,
        id: String
    ): Flow<Resource<GetCostsDtoItem>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.updateCost(token,body,id)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun deleteCost(token: String, id: String): Flow<Resource<GetCostsDtoItem>> = flow {
        emit(Resource.Loading())

        val data = try {
            null
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = data))

        try {
            val remote = api.deleteCost(token,id)
            try {
                emit(Resource.Success(remote))
            } catch (_: java.lang.Exception){}
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }
}