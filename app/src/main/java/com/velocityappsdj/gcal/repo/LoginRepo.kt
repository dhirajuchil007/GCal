package com.velocityappsdj.gcal.repo

import com.velocityappsdj.gcal.common.NetworkError
import com.velocityappsdj.gcal.common.Result
import com.velocityappsdj.gcal.network.model.request.TokenGenerationRequest
import com.velocityappsdj.gcal.network.model.request.TokenRefreshRequest
import com.velocityappsdj.gcal.network.model.response.TokenResponse

interface LoginRepo {

    suspend fun getToken(tokenGenerationRequest: TokenGenerationRequest): Result<TokenResponse, NetworkError>

    suspend fun refreshToken(tokenRefreshRequest: TokenRefreshRequest): Result<TokenResponse, NetworkError>
}