package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attachment(
    @SerializedName("fileId")
    val fileId: String?,
    @SerializedName("fileUrl")
    val fileUrl: String?,
    @SerializedName("iconLink")
    val iconLink: String?,
    @SerializedName("mimeType")
    val mimeType: String?,
    @SerializedName("title")
    val title: String?
) : Parcelable