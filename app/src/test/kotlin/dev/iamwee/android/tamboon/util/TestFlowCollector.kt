package dev.iamwee.android.tamboon.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers
import org.junit.Assert

class TestFlowCollector<T> {

    private val values = mutableListOf<T>()

    @InternalCoroutinesApi
    fun test(scope: CoroutineScope, flow: Flow<T>): Job {
        return scope.launch {
            flow.collect(object : FlowCollector<T> {
                override suspend fun emit(value: T) {
                    values.add(value)
                }
            })
        }
    }

    fun getValues(position: Int): T? = values.getOrNull(position)

    fun assertValues(vararg _values: T) {
        Assert.assertThat(_values.toList(), CoreMatchers.`is`(values.toList()))
    }

}