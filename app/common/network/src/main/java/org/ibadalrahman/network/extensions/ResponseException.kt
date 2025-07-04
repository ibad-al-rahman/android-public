package org.ibadalrahman.network.extensions

sealed class ResponseException: Throwable() {
    data object NetworkException: ResponseException()
    data object CanceledException: ResponseException()
    data object Unknown: ResponseException()
}
