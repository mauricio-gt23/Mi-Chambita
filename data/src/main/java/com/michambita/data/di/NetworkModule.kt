package com.michambita.data.di

import com.michambita.common.network.NetworkState
import com.michambita.data.network.NetworkStateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindNetworkState(networkStateImpl: NetworkStateImpl): NetworkState
}
