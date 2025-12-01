package com.michambita.di

import com.michambita.data.repository.AuthRepository
import com.michambita.data.repository.MovimientoRepository
import com.michambita.data.repository.ProductoImageRepository
import com.michambita.data.repository.ProductoRepository
import com.michambita.data.repository.SynchronizationRepository
import com.michambita.data.repository.UserRepository
import com.michambita.data.repository.impl.AuthRepositoryImpl
import com.michambita.data.repository.impl.MovimientoRepositoryImpl
import com.michambita.data.repository.impl.ProductoImageRepositoryImpl
import com.michambita.data.repository.impl.ProductoRepositoryImpl
import com.michambita.data.repository.impl.SynchronizationRepositoryImpl
import com.michambita.data.repository.impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository


    @Binds
    @Singleton
    abstract fun bindProductoRepository(
        productoRepositoryImpl: ProductoRepositoryImpl
    ): ProductoRepository

    @Binds
    @Singleton
    abstract fun bindProductoImageRepository(
        productoImageRepositoryImpl: ProductoImageRepositoryImpl
    ): ProductoImageRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindSynchronizationRepository(
        synchronizationRepositoryImpl: SynchronizationRepositoryImpl
    ): SynchronizationRepository

    @Binds
    @Singleton
    abstract fun bindMovimientoRepository(
        movimientoRepositoryImpl: MovimientoRepositoryImpl
    ): MovimientoRepository
}