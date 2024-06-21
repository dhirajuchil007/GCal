package com.velocityappsdj.gcal.repo

import com.velocityappsdj.gcal.common.NetworkError
import com.velocityappsdj.gcal.common.Result
import com.velocityappsdj.gcal.common.getNetworkErrorFromCode
import com.velocityappsdj.gcal.network.api.EventsAPIService
import com.velocityappsdj.gcal.network.model.response.EventsResponse

class EventsRepoImpl(private val eventsAPIService: EventsAPIService) : EventsRepo {

    override suspend fun getEvents(
        timeMin: String,
        timeMax: String
    ): Result<EventsResponse, NetworkError> {
        return try {
            val response = eventsAPIService.getEvents(
                timeMax = timeMax,
                timeMin = timeMin,
            )
            if (response.isSuccessful) {
                val data = response.body()

                data?.let {
                    Result.Success(it)
                } ?: Result.Error(NetworkError.UNKNOWN)

            } else {
                Result.Error(getNetworkErrorFromCode(response.code()))
            }

        } catch (e: Exception) {
            Result.Error(NetworkError.UNKNOWN)
        }
    }
}