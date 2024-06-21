package com.velocityappsdj.gcal.repo

import com.velocityappsdj.gcal.common.NetworkError
import com.velocityappsdj.gcal.common.Result
import com.velocityappsdj.gcal.common.getNetworkErrorFromCode
import com.velocityappsdj.gcal.local.DataStoreUtil
import com.velocityappsdj.gcal.network.api.LoginApiService
import com.velocityappsdj.gcal.network.model.request.TokenGenerationRequest
import com.velocityappsdj.gcal.network.model.request.TokenRefreshRequest
import com.velocityappsdj.gcal.network.model.response.TokenResponse
import javax.inject.Inject

class LoginRepoImpl @Inject constructor(
    private val loginApiService: LoginApiService,
    private val dataStoreUtil: DataStoreUtil
) : LoginRepo {
    override suspend fun getToken(tokenGenerationRequest: TokenGenerationRequest): Result<TokenResponse, NetworkError> {

        try {

            val result = loginApiService.generateToken(tokenGenerationRequest)
            return if (result.isSuccessful) {
                val data = result.body()
                data?.let {
                    dataStoreUtil.saveToken(it.accessToken ?: "")
                    dataStoreUtil.saveRefreshToken(it.refreshToken ?: "")
                    Result.Success(it)
                } ?: Result.Error(NetworkError.UNKNOWN)

            } else {
                Result.Error(getNetworkErrorFromCode(result.code()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun refreshToken(tokenRefreshRequest: TokenRefreshRequest): Result<TokenResponse, NetworkError> {
        try {

            val result = loginApiService.refreshToken(tokenRefreshRequest)
            return if (result.isSuccessful) {
                val data = result.body()
                data?.let {
                    dataStoreUtil.saveToken(it.accessToken ?: "")
                    Result.Success(it)
                } ?: Result.Error(NetworkError.UNKNOWN)
            } else {
                Result.Error(getNetworkErrorFromCode(result.code()))
            }
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }
    }


}