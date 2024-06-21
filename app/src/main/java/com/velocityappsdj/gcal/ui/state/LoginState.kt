package com.velocityappsdj.gcal.ui.state

sealed class LoginState {
    object Success : LoginState()
    data class Error(val message: String) : LoginState()

    object Loading : LoginState()

}