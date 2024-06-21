package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConferenceSolution(
    @SerializedName("iconUri")
    val iconUri: String?,
    @SerializedName("key")
    val key: Key?,
    @SerializedName("name")
    val name: String?
):Parcelable