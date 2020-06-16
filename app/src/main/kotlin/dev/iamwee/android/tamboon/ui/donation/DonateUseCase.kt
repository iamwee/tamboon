package dev.iamwee.android.tamboon.ui.donation

import dev.iamwee.android.tamboon.common.Result
import dev.iamwee.android.tamboon.http.TamboonService
import dev.iamwee.android.tamboon.http.data.DonateBody
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DonateUseCase @Inject constructor(private val service: TamboonService) {

    private suspend fun execute(params: DonateBody) {
        service.donate(params)
    }

    operator fun invoke(params: DonateBody) = flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(execute(params)))
        } catch (t: Throwable) {
            emit(Result.Error(t))
        }
    }

}