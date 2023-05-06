package com.android.beyikyol2.feature_cost.domain.use_case

import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDto
import com.android.beyikyol2.feature_cost.data.remote.dto.CreateCostBody
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDto
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDtoItem
import com.android.beyikyol2.feature_cost.data.remote.dto.change_type.GetChangeType
import com.android.beyikyol2.feature_cost.domain.repository.CostRepository
import kotlinx.coroutines.flow.Flow

class CostInfo(
    private val repository: CostRepository
) {
    operator fun invoke(token: String,id: String,type: String): Flow<Resource<GetCostsDto>> {
        return repository.getCostByCarId(token,id,type)
    }

    fun getCostById(token: String,id: String): Flow<Resource<GetCostsDtoItem>> {
        return repository.getCostById(token,id)
    }

    fun createCost(token: String,body: CreateCostBody): Flow<Resource<GetCostsDtoItem>> {
        return repository.createCost(token,body)
    }
    fun updateCost(token: String,body: CreateCostBody,id: String): Flow<Resource<GetCostsDtoItem>> {
        return repository.updateCost(token,body,id)
    }
    fun deleteCost(token: String,id: String): Flow<Resource<GetCostsDtoItem>> {
        return repository.deleteCost(token,id)
    }
    fun getChangeType(): Flow<Resource<GetChangeType>> {
        return repository.getChangeType()
    }
}