package com.velocityappsdj.gcal.network.model.request

import com.google.gson.annotations.SerializedName

data class TokenRefreshRequest(
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("grant_type")
    val grantType: String = "refresh_token",
    @SerializedName("refresh_token")
    val refreshToken: String
)
