package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConferenceData(
    @SerializedName("conferenceId")
    val conferenceId: String?,
    @SerializedName("conferenceSolution")
    val conferenceSolution: ConferenceSolution?,
    @SerializedName("createRequest")
    val createRequest: CreateRequest?,
    @SerializedName("entryPoints")
    val entryPoints: List<EntryPoint?>?
):Parcelable