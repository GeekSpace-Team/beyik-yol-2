package com.android.beyikyol2.feature_car.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.beyikyol2.R
import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_car.data.remote.dto.GetUserCarsDtoItem
import com.android.beyikyol2.feature_car.data.remote.dto.Image
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.AddCarBody
import com.android.beyikyol2.feature_car.data.remote.dto.addCar.CountResponse
import com.android.beyikyol2.feature_car.domain.use_case.CarInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val carInfo: CarInfo
) : ViewModel() {
    private val _state = mutableStateOf(CarState())
    val state: State<CarState> = _state
    private var job: Job? = null

    fun getProfile(
        token: String
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            carInfo(token)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                cars = result.data
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                cars = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                cars = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getCarById(
        token: String,
        id: String
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            carInfo.getCarById(token,id)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                singleCar = result.data
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                singleCar = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                singleCar = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun deleteCar(
        token: String,
        id: String,
        onSuccess: (GetUserCarsDtoItem)->Unit,
        onError: (Int)->Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            carInfo.deleteCar(token,id)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            result.data?.let { onSuccess(it) }
                        }
                        is Resource.Error -> {
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getAddCarDetails() {
        job?.cancel()
        job = viewModelScope.launch {
            carInfo.getCarDetails()
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                addCarDetails = result.data
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                addCarDetails = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                addCarDetails = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun addCar(
        body: AddCarBody,
        token: String,
        onSuccess: (GetUserCarsDtoItem) -> Unit,
        onError: (Int) -> Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            carInfo.addCar(body,token)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                addCarState = result.data
                            )
                            result.data?.let { onSuccess(it) }
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                addCarState = result.data
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                addCarState = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun updateCar(
        id: String,
        body: AddCarBody,
        token: String,
        onSuccess: (GetUserCarsDtoItem) -> Unit,
        onError: (Int) -> Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            carInfo.updateCar(id,body,token)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                updateCarState = result.data
                            )
                            result.data?.let { onSuccess(it) }
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                updateCarState = result.data
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                updateCarState = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun addCarImage(
        images: List<File?>,
        id: String,
        token: String,
        onSuccess: (CountResponse) -> Unit,
        onError: (Int) -> Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            carInfo.addCarImage(images,id,token)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                addCarImageState = result.data
                            )
                            result.data?.let { onSuccess(it) }
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                addCarImageState = result.data
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                addCarImageState = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun removeCarImage(
        id: String,
        token: String,
        onSuccess: (Image) -> Unit,
        onError: (Int) -> Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            carInfo.removeCarImage(id,token)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                removeCarImageState = result.data
                            )
                            result.data?.let { onSuccess(it) }
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                removeCarImageState = result.data
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                removeCarImageState = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }
}