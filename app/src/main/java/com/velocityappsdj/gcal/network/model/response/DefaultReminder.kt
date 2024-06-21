package com.velocityappsdj.gcal.network.model.response


import com.google.gson.annotations.SerializedName

data class DefaultReminder(
    @SerializedName("method")
    val method: String?,
    @SerializedName("minutes")
    val minutes: Int?
)