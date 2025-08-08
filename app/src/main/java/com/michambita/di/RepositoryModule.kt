package com.michambita.di

import com.michambita.data.repository.AuthRepository
import com.michambita.data.repository.ProductoRepository
import com.michambita.data.repository.impl.AuthRepositoryImpl
import com.michambita.data.repository.impl.ProductoRepositoryImpl
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
}