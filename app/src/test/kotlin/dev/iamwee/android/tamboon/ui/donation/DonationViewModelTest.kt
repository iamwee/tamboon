package dev.iamwee.android.tamboon.ui.donation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.TestObserver
import dev.iamwee.android.tamboon.common.Result
import dev.iamwee.android.tamboon.http.TamboonService
import dev.iamwee.android.tamboon.http.data.DonateBody
import dev.iamwee.android.tamboon.util.CoroutineTestRule
import dev.iamwee.android.tamboon.util.TestCoroutineContextProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class DonationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun testWhenDonateSucceeded() = coroutineTestRule.runBlocking {
        val token = "token"
        val cardName = "card_name"
        val amountInSatang = 1000L
        val service = mockk<TamboonService>()
        val useCase = DonateUseCase(service)
        val viewModel = DonationViewModel(useCase, TestCoroutineContextProvider)

        coEvery { service.donate(DonateBody(amountInSatang, cardName, token)) } returns Unit

        viewModel.donate(token, cardName, amountInSatang)

        TestObserver.test(viewModel.donateResult)
            .assertHasValue()
            .assertHistorySize(2)
            .assertValueHistory(Result.Loading, Result.Success(Unit))
    }

    @Test
    fun testWhenDonateFailed() = coroutineTestRule.runBlocking {
        val token = "token"
        val cardName = "card_name"
        val amountInSatang = 1000L
        val errorMessage = "An error message that thrown from UseCase."
        val service = mockk<TamboonService>()
        val useCase = DonateUseCase(service)
        val viewModel = DonationViewModel(useCase, TestCoroutineContextProvider)
        val exceptionExpected = IllegalStateException(errorMessage)

        coEvery { service.donate(DonateBody(amountInSatang, cardName, token)) } throws exceptionExpected

        viewModel.donate(token, cardName, amountInSatang)

        TestObserver.test(viewModel.donateResult)
            .assertHasValue()
            .assertHistorySize(2)
            .assertValueHistory(Result.Loading, Result.Error(exceptionExpected))
    }
}