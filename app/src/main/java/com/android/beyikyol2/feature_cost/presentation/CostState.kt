package com.android.beyikyol2.feature_cost.presentation

import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDto
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDtoItem
import com.android.beyikyol2.feature_cost.data.remote.dto.change_type.GetChangeType

data class CostState(
    var cost: GetCostsDto? = null,
    val singleCost: GetCostsDtoItem? = null,
    val createCost: GetCostsDtoItem? = null,
    val updateCost: GetCostsDtoItem? = null,
    val deleteCost: GetCostsDtoItem? = null,
    val changeTypes: GetChangeType? = null,
)
