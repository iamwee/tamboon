package dev.iamwee.android.tamboon.ui.charity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.iamwee.android.tamboon.data.CharityRepository
import dev.iamwee.android.tamboon.http.TamboonService
import dev.iamwee.android.tamboon.http.data.CharityNetwork
import dev.iamwee.android.tamboon.util.CoroutineTestRule
import dev.iamwee.android.tamboon.util.TestCoroutineContextProvider
import dev.iamwee.android.tamboon.util.valueTest
import io.mockk.coEvery
import io.mockk.mockk
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class CharityViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun testWhenCharityIsExistsAndErrorIsAbsent() = coroutineTestRule.runBlocking {
        val data = listOf(
            CharityNetwork(id = 1, logoUrl = "http://example.com", name = "charity A")
        )
        val service = mockk<TamboonService>()

        coEvery { service.getCharities() } returns data

        val repository = CharityRepository(service)
        val viewModel = CharityViewModel(repository, TestCoroutineContextProvider)

        viewModel.getCharities()

        val result = viewModel.charities.valueTest()
        val error= viewModel.error.valueTest()

        assertNotNull(result)
        assertEquals(1, result!!.count())
        assertNull(error)
    }

    @Test
    fun testWhenGotAtError() = coroutineTestRule.runBlocking {
        val errorMessageExpected = "Error message for test exception."
        val service = mockk<TamboonService>()

        coEvery { service.getCharities() } throws IllegalStateException(errorMessageExpected)

        val repository = CharityRepository(service)
        val viewModel = CharityViewModel(repository, TestCoroutineContextProvider)

        viewModel.getCharities()

        val result = viewModel.charities.valueTest()
        val error = viewModel.error.valueTest()

        assertNull(result)
        assertNotNull(error)

        assertThat(error, CoreMatchers.instanceOf(IllegalStateException::class.java))
        assertEquals(errorMessageExpected, error!!.localizedMessage)
    }


}