package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OriginalStartTime(
    @SerializedName("dateTime")
    val dateTime: String?,
    @SerializedName("timeZone")
    val timeZone: String?
) : Parcelable