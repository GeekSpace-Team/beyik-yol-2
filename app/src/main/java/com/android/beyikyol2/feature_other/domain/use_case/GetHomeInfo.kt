package com.android.beyikyol2.feature_other.domain.use_case

import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_other.data.remote.dto.GetConstantDto
import com.android.beyikyol2.feature_other.data.remote.dto.GetHomeDto
import com.android.beyikyol2.feature_other.data.remote.dto.evacuator.GetEvacuatorDto
import com.android.beyikyol2.feature_other.data.remote.dto.inbox.GetUserInboxDto
import com.android.beyikyol2.feature_other.data.remote.dto.inbox.GetUserInboxDtoItem
import com.android.beyikyol2.feature_other.data.remote.dto.map.SearchFromMap
import com.android.beyikyol2.feature_other.data.remote.dto.map.autocomplete.GetAutoComplete
import com.android.beyikyol2.feature_other.data.remote.dto.map.direction.GetDirection
import com.android.beyikyol2.feature_other.data.remote.dto.speech.SpeechRequest
import com.android.beyikyol2.feature_other.data.remote.dto.speech.response.SpeechResponse
import com.android.beyikyol2.feature_other.data.remote.dto.weather.GetWeather
import com.android.beyikyol2.feature_other.domain.repository.GetHomeRepository
import kotlinx.coroutines.flow.Flow

class GetHomeInfo(
    private val repository: GetHomeRepository
) {
    operator fun invoke(token: String,isSend: Boolean): Flow<Resource<GetHomeDto>> {
        return repository.getHome(token,isSend)
    }
    fun getUserInbox(token: String): Flow<Resource<GetUserInboxDto>>{
        return repository.getUserInbox(token)
    }
    fun getEvacuators(region: String): Flow<Resource<GetEvacuatorDto>>{
        return repository.getEvacuator(region)
    }

    fun getConstantPage(type: String): Flow<Resource<GetConstantDto>>{
        return repository.getConstantPage(type)
    }

    fun getAutoComplete(input: String): Flow<Resource<GetAutoComplete>>{
        return repository.getAutoComplete(input)
    }
    fun searchFromMap(query: String): Flow<Resource<SearchFromMap>>{
        return repository.searchFromMap(query)
    }
    fun textToSpeech(body: SpeechRequest): Flow<Resource<SpeechResponse>>{
        return repository.textToSpeech(body)
    }

    fun getWeather(): Flow<Resource<GetWeather>>{
        return repository.getWeather()
    }
    fun getDirection(params: HashMap<String, Any>): Flow<Resource<GetDirection>>{
        return repository.getDirection(params)
    }

    fun deleteInbox(token: String, id: String): Flow<Resource<GetUserInboxDtoItem>>{
        return repository.deleteInbox(token,id)
    }
}