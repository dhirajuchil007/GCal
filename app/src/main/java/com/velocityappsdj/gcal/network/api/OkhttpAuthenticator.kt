package com.velocityappsdj.gcal.network.api

import com.velocityappsdj.gcal.common.Constants
import com.velocityappsdj.gcal.common.Result
import com.velocityappsdj.gcal.local.DataStoreUtil
import com.velocityappsdj.gcal.network.model.request.TokenRefreshRequest
import com.velocityappsdj.gcal.repo.LoginRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class OkhttpAuthenticator(
    private val dataStoreUtil: DataStoreUtil,
    private val loginRepo: LoginRepo
) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val refreshToken = runBlocking { dataStoreUtil.getRefreshToken().first() }
        val newToken = refreshToken?.let {
            getNewToken(it)
        } ?: run {
            logoutUser()
            ""
        }
        return response.request.newBuilder().header("Authorization", "Bearer $newToken").build()
    }

    fun getNewToken(refreshToken: String): String {
        return runBlocking {
            val result = loginRepo.refreshToken(
                TokenRefreshRequest(
                    clientSecret = Constants.CLIENT_SECRET,
                    clientId = Constants.WEB_CLIENT_ID,
                    refreshToken = refreshToken
                )
            )
            when (result) {
                is Result.Error -> {
                    logoutUser()
                    ""
                }

                is Result.Success -> result.data.accessToken ?: ""
            }
        }

    }

    fun logoutUser() {
        //TODO()
    }
}