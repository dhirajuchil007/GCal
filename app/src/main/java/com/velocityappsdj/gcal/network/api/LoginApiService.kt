package com.velocityappsdj.gcal.network.api

import com.velocityappsdj.gcal.network.model.request.TokenGenerationRequest
import com.velocityappsdj.gcal.network.model.request.TokenRefreshRequest
import com.velocityappsdj.gcal.network.model.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {

    @POST("token")
    suspend fun generateToken(@Body tokenRequest: TokenGenerationRequest): Response<TokenResponse>

    @POST("token")
    suspend fun refreshToken(@Body tokenRequest: TokenRefreshRequest): Response<TokenResponse>

}