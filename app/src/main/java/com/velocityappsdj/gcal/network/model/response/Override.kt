package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Override(
    @SerializedName("method")
    val method: String?,
    @SerializedName("minutes")
    val minutes: Int?
) : Parcelable