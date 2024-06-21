package com.velocityappsdj.gcal.domain

import com.velocityappsdj.gcal.common.NetworkError
import com.velocityappsdj.gcal.common.Result
import com.velocityappsdj.gcal.network.model.request.TokenGenerationRequest
import com.velocityappsdj.gcal.repo.LoginRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenGenerationUseCase @Inject constructor(private val repo: LoginRepo) {

    suspend operator fun invoke(tokenGenerationRequest: TokenGenerationRequest): Result<Boolean, NetworkError> {
        return withContext(Dispatchers.IO) {
            val result = repo.getToken(tokenGenerationRequest = tokenGenerationRequest)
            when (result) {
                is Result.Error -> Result.Error(result.error)
                is Result.Success -> Result.Success(true)
            }
        }
    }

}