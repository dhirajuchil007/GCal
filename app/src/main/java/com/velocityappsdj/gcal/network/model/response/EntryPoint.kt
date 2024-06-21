package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EntryPoint(
    @SerializedName("entryPointType")
    val entryPointType: String?,
    @SerializedName("label")
    val label: String?,
    @SerializedName("pin")
    val pin: String?,
    @SerializedName("regionCode")
    val regionCode: String?,
    @SerializedName("uri")
    val uri: String?
) : Parcelable