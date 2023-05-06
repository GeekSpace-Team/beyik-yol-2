package com.android.beyikyol2.feature_cost.data.remote

import com.android.beyikyol2.feature_cost.data.remote.dto.CreateCostBody
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDto
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDtoItem
import com.android.beyikyol2.feature_cost.data.remote.dto.change_type.GetChangeType
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CostApi {

    @GET("/costs/get-costs-by-car-id/{id}?")
    suspend fun getCostsByCarId(@Header("Authorization") token: String,@Path("id") id: String, @Query("type") type: String): GetCostsDto?

    @GET("/costs/get-costs-by-id/{id}?")
    suspend fun getCostsById(@Header("Authorization") token: String,@Path("id") id: String): GetCostsDtoItem?

    @POST("/costs/create-cost")
    suspend fun createCost(@Header("Authorization") token: String,@Body body: CreateCostBody): GetCostsDtoItem?

    @PATCH("/costs/update-cost/{id}")
    suspend fun updateCost(@Header("Authorization") token: String,@Body body: CreateCostBody,@Path("id") id: String): GetCostsDtoItem?

    @GET("/change-type/find-all-change-types")
    suspend fun getChangeTypes(): GetChangeType?

    @DELETE("/costs/delete-cost/{id}")
    suspend fun deleteCost(@Header("Authorization") token: String, @Path("id") id: String): GetCostsDtoItem?

}