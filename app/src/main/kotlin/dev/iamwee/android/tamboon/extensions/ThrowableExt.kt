package dev.iamwee.android.tamboon.extensions

import com.google.gson.Gson
import com.google.gson.JsonObject
import dev.iamwee.android.tamboon.BuildConfig
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

val Throwable.errorMessage: String
    get() = when (this) {
        is SocketTimeoutException -> "Couldn't connect to the server, please try again."
        is UnknownHostException -> "Couldn't connect to the server, check your internet connection and try again."
        is HttpException -> {
            val defaultErrorMessage = "There is an unexpected error occurred."
            try {
                val jsonObject = response()!!.errorBody()!!.string()
                val json = Gson().fromJson(jsonObject, JsonObject::class.java)
                json.get("error_message").asString
            } catch (t: Throwable) {
                defaultErrorMessage
            }
        }
        else -> localizedMessage ?: """
                There is an unexpected error occurred.
                ${if (BuildConfig.DEBUG) toString() else ""}
            """.trimIndent()
    }
