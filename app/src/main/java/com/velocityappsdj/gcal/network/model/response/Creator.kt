package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Creator(
    @SerializedName("displayName")
    val displayName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("self")
    val self: Boolean?
) : Parcelable