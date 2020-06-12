package dev.iamwee.android.tamboon.extensions

import dev.iamwee.android.tamboon.BuildConfig
import java.net.SocketTimeoutException
import java.net.UnknownHostException

val Throwable.errorMessage: String
    get() = when (this) {
        is SocketTimeoutException -> "Couldn't connect to the server, please try again."
        is UnknownHostException -> "Couldn't connect to the server, check your internet connection and try again."
        else -> localizedMessage ?: """
                There is an unexpected error occurred.
                ${if (BuildConfig.DEBUG) toString() else ""}
            """.trimIndent()
    }
