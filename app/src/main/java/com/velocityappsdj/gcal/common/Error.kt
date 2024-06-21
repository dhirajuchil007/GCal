package com.velocityappsdj.gcal.common

sealed interface Error


public enum class NetworkError : Error {
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    METHOD_NOT_ALLOWED,
    REQUEST_TIMEOUT,
    UNKNOWN,
    SERVER_ERROR,
    CHECK_CONNECTION
}

fun getNetworkErrorFromCode(code: Int): NetworkError {
    return when (code) {
        400 -> NetworkError.BAD_REQUEST
        401 -> NetworkError.UNAUTHORIZED
        403 -> NetworkError.FORBIDDEN
        404 -> NetworkError.NOT_FOUND
        405 -> NetworkError.METHOD_NOT_ALLOWED
        408 -> NetworkError.REQUEST_TIMEOUT
        500, 502, 503 -> NetworkError.SERVER_ERROR
        else -> NetworkError.UNKNOWN
    }
}
