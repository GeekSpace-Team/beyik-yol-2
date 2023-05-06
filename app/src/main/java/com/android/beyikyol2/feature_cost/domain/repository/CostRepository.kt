package com.android.beyikyol2.feature_cost.domain.repository

import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDto
import com.android.beyikyol2.feature_cost.data.remote.dto.CreateCostBody
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDto
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDtoItem
import com.android.beyikyol2.feature_cost.data.remote.dto.change_type.GetChangeType
import kotlinx.coroutines.flow.Flow

interface CostRepository {
    fun getCostByCarId(token: String,id: String,type: String): Flow<Resource<GetCostsDto>>
    fun getCostById(token: String,id: String): Flow<Resource<GetCostsDtoItem>>
    fun createCost(token: String,body: CreateCostBody): Flow<Resource<GetCostsDtoItem>>
    fun updateCost(token: String,body: CreateCostBody,id: String): Flow<Resource<GetCostsDtoItem>>
    fun deleteCost(token: String,id: String): Flow<Resource<GetCostsDtoItem>>
    fun getChangeType(): Flow<Resource<GetChangeType>>
}