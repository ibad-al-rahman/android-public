package com.ibadalrahman.network.extensions

import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

fun Throwable.toResponseException(): ResponseException =
    when (this) {
        is ResponseException -> this
        is SocketException,
        is SocketTimeoutException,
        is SSLException,
        is UnknownHostException -> ResponseException.NetworkException
        else -> ResponseException.Unknown
    }
