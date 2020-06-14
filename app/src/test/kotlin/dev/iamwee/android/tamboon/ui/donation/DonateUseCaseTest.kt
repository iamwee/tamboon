package dev.iamwee.android.tamboon.ui.donation

import dev.iamwee.android.tamboon.common.Result
import dev.iamwee.android.tamboon.extensions.errorMessage
import dev.iamwee.android.tamboon.http.TamboonService
import dev.iamwee.android.tamboon.http.data.DonateBody
import dev.iamwee.android.tamboon.util.CoroutineTestRule
import dev.iamwee.android.tamboon.util.TestFlowCollector
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@InternalCoroutinesApi
class DonateUseCaseTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun testWhenSucceeded() = coroutineTestRule.runBlocking {
        val amount = 100L.times(100)
        val name = "test"
        val token = "test_token"
        val body = DonateBody(amount, name, token)

        val service = mockk<TamboonService>()

        coEvery { service.donate(body) } returns Unit

        val useCase = DonateUseCase(service)

        val collector = TestFlowCollector<Result<Unit>>()
        val job = collector.test(this, useCase(body))

        collector.assertValues(
            Result.Loading,
            Result.Success(Unit)
        )

        job.cancel()
    }

    @Test
    fun testWhenThrowHttpException() = coroutineTestRule.runBlocking {
        val body = DonateBody(1, "", "")
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), """
            {
              "success": false,
              "error_code": "insufficient_minerals",
              "error_message": "Card has insufficient balance."
            }
        """.trimIndent())

        val exception = HttpException(Response.error<Any>(
            responseBody,
            okhttp3.Response.Builder().apply {
                code(400)
                protocol(Protocol.HTTP_1_1)
                message("Bad Request")
                request(okhttp3.Request.Builder().url("http://localhost/").build())
            }.build()
        ))

        val service = mockk<TamboonService>()
        coEvery { service.donate(body) } throws exception

        val useCase = DonateUseCase(service)

        val collector = TestFlowCollector<Result<Unit>>()
        val job = collector.test(this, useCase(body))

        collector.assertValues(
            Result.Loading,
            Result.Error(exception)
        )

        Assert.assertEquals(
            "Card has insufficient balance.",
            (collector.getValues(1) as? Result.Error)?.throwable?.errorMessage
        )

        job.cancel()
    }

    @Test
    fun testWhenThrowUnExpectedException() = coroutineTestRule.runBlocking {
        val body = DonateBody(1, "", "")
        val errorMessage = "Test error message that thrown from exception."
        val exceptionExpected = IllegalArgumentException(errorMessage)
        val service = mockk<TamboonService>()

        coEvery { service.donate(body) } throws exceptionExpected

        val useCase = DonateUseCase(service)
        val collector = TestFlowCollector<Result<Unit>>()
        val job = collector.test(this, useCase(body))

        collector.assertValues(Result.Loading, Result.Error(exceptionExpected))

        Assert.assertEquals(
            errorMessage,
            (collector.getValues(1) as? Result.Error)?.throwable?.errorMessage
        )

        job.cancel()
    }
}

