package dev.iamwee.android.tamboon.http

import dev.iamwee.android.tamboon.http.data.CharityNetwork
import retrofit2.http.GET

interface TamboonService {

    @GET("charities")
    suspend fun getCharities(): List<CharityNetwork>

}