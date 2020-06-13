package dev.iamwee.android.tamboon.data

import dev.iamwee.android.tamboon.http.TamboonService
import dev.iamwee.android.tamboon.http.data.CharityNetwork
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Test
import java.net.SocketTimeoutException

class CharityRepositoryTest {

    @Test
    fun testWhenLogoUrlFieldIsAbsent() = runBlocking {
        val data = listOf(
            CharityNetwork(1, null, "charity A")
        )
        val service = mockk<TamboonService>()

        coEvery { service.getCharities() } returns data

        val repository = CharityRepository(service)

        val result = repository.getCharities()

        assertEquals(1, result.count())
        assertEquals(1, result.first().id)
        assertEquals("charity A", result.first().name)
        assertEquals("", result.first().imageUrl)
    }

    @Test
    fun testWhenNoneAllFieldIsNonNull() = runBlocking {
        val data = listOf(
            CharityNetwork(1, "http://example.com", "charity A")
        )

        val service = mockk<TamboonService>()
        coEvery { service.getCharities() } returns data

        val repository = CharityRepository(service)
        val result = repository.getCharities()

        assertEquals(1, result.count())
        assertEquals(1, result.first().id)
        assertEquals("charity A", result.first().name)
        assertEquals("http://example.com", result.first().imageUrl)
    }

    @Test
    fun testWhenGotAnException() = runBlocking {
        val exceptionExpected = SocketTimeoutException()

        val service = mockk<TamboonService>()
        coEvery { service.getCharities() } throws exceptionExpected

        val repository = CharityRepository(service)
        try {
            repository.getCharities()
        } catch (t: Exception) {
            assertThat(t, CoreMatchers.instanceOf(SocketTimeoutException::class.java))
        }

        return@runBlocking
    }
}