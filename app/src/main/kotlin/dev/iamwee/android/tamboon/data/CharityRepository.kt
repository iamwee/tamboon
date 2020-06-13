package dev.iamwee.android.tamboon.data

import dev.iamwee.android.tamboon.http.TamboonService
import dev.iamwee.android.tamboon.http.data.asInfo
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CharityRepository @Inject constructor(
    private val service: TamboonService
) {

    suspend fun getCharities(): List<CharityInfo> = service.getCharities().map { it.asInfo }

}