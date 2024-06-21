package com.velocityappsdj.gcal.network.api

import com.velocityappsdj.gcal.network.model.response.EventsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventsAPIService {

    @GET("calendar/v3/calendars/primary/events")
    suspend fun getEvents(
        @Query("singleEvents") singleEvents: Boolean = true,
        @Query("orderBy") orderBy: String = "startTime",
        @Query("timeMin") timeMin: String,
        @Query("timeMax") timeMax: String
    ): Response<EventsResponse>
}