package com.android.beyikyol2.feature_cost.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.beyikyol2.R
import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_cost.data.remote.dto.CreateCostBody
import com.android.beyikyol2.feature_cost.data.remote.dto.GetCostsDtoItem
import com.android.beyikyol2.feature_cost.domain.use_case.CostInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CostViewModel @Inject constructor(
    private val costInfo: CostInfo
) : ViewModel() {
    private val _state = mutableStateOf(CostState())
    val state: State<CostState> = _state
    private var job: Job? = null

    fun getCosts(
        token: String,
        id: String,
        type: String
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            costInfo(token,id,type)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                cost = result.data
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                cost = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                cost = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun createCost(
        token: String,
        body: CreateCostBody,
        onSuccess: (GetCostsDtoItem)->Unit,
        onError: (Int)->Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            costInfo.createCost(token,body)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                createCost = result.data
                            )
                            result.data?.let { onSuccess(it) }
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                createCost = result.data
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                createCost = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getChangeTypes() {
        job?.cancel()
        job = viewModelScope.launch {
            costInfo.getChangeType()
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                changeTypes = result.data
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                changeTypes = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                changeTypes = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getCostById(
        token: String,
        id: String,
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            costInfo.getCostById(token,id)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                singleCost = result.data
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                singleCost = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                singleCost = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun updateCost(
        token: String,
        body: CreateCostBody,
        id: String,
        onSuccess: (GetCostsDtoItem)->Unit,
        onError: (Int)->Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            costInfo.updateCost(token,body,id)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                updateCost = result.data
                            )
                            result.data?.let { onSuccess(it) }
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                updateCost = result.data
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                updateCost = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun deleteCost(
        token: String,
        id: String,
        onSuccess: (String)->Unit,
        onError: (Int)->Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            costInfo.deleteCost(token,id)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                deleteCost = result.data
                            )
                            onSuccess(id)
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                deleteCost = result.data
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                deleteCost = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}