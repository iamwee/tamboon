package dev.iamwee.android.tamboon.http

import dev.iamwee.android.tamboon.http.data.CharityNetwork
import dev.iamwee.android.tamboon.http.data.DonateBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TamboonService {

    @GET("charities")
    suspend fun getCharities(): List<CharityNetwork>

    @POST("donations")
    suspend fun donate(@Body body: DonateBody)

}