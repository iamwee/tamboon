package dev.iamwee.android.tamboon.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dev.iamwee.android.tamboon.common.CoroutineContextProvider
import dev.iamwee.android.tamboon.common.CoroutineContextProviderImpl

@InstallIn(ActivityComponent::class)
@Module
abstract class CoroutineContextProviderModule {

    @ActivityScoped
    @Binds
    abstract fun bindCoroutineContextProvider(impl: CoroutineContextProviderImpl): CoroutineContextProvider

}