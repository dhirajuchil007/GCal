package com.velocityappsdj.gcal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velocityappsdj.gcal.common.NetworkError
import com.velocityappsdj.gcal.common.Result
import com.velocityappsdj.gcal.domain.TokenGenerationUseCase
import com.velocityappsdj.gcal.network.model.request.TokenGenerationRequest
import com.velocityappsdj.gcal.ui.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val tokenGenerationUseCase: TokenGenerationUseCase) :
    ViewModel() {

    private val _tokenGenerationState = MutableStateFlow<LoginState>(LoginState.Loading)
    val tokenGenerationState = _tokenGenerationState


    fun generateToken(tokenGenerationRequest: TokenGenerationRequest) {
        viewModelScope.launch {
            when (val result = tokenGenerationUseCase(tokenGenerationRequest)) {
                is Result.Error -> {
                    _tokenGenerationState.emit(LoginState.Error(result.error.name))

                }

                is Result.Success -> _tokenGenerationState.emit(LoginState.Success)
            }
        }

    }

    fun setLoginFailed(msg: String) {
        _tokenGenerationState.value = LoginState.Error(msg)
    }

}