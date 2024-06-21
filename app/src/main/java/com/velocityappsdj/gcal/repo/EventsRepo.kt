package com.velocityappsdj.gcal.repo

import com.velocityappsdj.gcal.common.NetworkError
import com.velocityappsdj.gcal.common.Result
import com.velocityappsdj.gcal.network.model.response.EventsResponse

interface EventsRepo {
    suspend fun getEvents(timeMin: String, timeMax: String): Result<EventsResponse, NetworkError>

}