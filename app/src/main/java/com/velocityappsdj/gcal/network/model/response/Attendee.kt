package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attendee(
    @SerializedName("displayName")
    val displayName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("optional")
    val optional: Boolean?,
    @SerializedName("organizer")
    val organizer: Boolean?,
    @SerializedName("responseStatus")
    val responseStatus: String?,
    @SerializedName("self")
    val self: Boolean?
) : Parcelable