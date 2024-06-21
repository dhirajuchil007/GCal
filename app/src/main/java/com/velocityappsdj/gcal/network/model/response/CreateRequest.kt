package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateRequest(
    @SerializedName("conferenceSolutionKey")
    val conferenceSolutionKey: ConferenceSolutionKey?,
    @SerializedName("requestId")
    val requestId: String?,
    @SerializedName("status")
    val status: Status?
):Parcelable