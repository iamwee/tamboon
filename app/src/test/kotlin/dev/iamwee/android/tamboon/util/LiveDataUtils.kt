package dev.iamwee.android.tamboon.util

import androidx.annotation.Nullable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@VisibleForTesting
inline fun <reified T> LiveData<T>.valueTest(): T {
    val data = arrayOfNulls<Any>(1)
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {

        override fun onChanged(@Nullable o: T) {
            data[0] = o
            latch.countDown()
            removeObserver(this)
        }
    }
    observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)
    //noinspection unchecked
    return data[0] as T
}