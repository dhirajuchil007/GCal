package com.velocityappsdj.gcal.network.model.response


import com.google.gson.annotations.SerializedName

data class EventsResponse(
    @SerializedName("accessRole")
    val accessRole: String?,
    @SerializedName("defaultReminders")
    val defaultReminders: List<DefaultReminder?>?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("etag")
    val etag: String?,
    @SerializedName("items")
    val events: List<Event>?,
    @SerializedName("kind")
    val kind: String?,
    @SerializedName("nextSyncToken")
    val nextSyncToken: String?,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("timeZone")
    val timeZone: String?,
    @SerializedName("updated")
    val updated: String?
)