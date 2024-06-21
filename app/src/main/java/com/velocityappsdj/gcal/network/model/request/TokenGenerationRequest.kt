package com.velocityappsdj.gcal.network.model.request

import com.google.gson.annotations.SerializedName

data class TokenGenerationRequest(
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("grant_type")
    val grantType: String = "authorization_code",
    @SerializedName("code")
    val code: String?
)
