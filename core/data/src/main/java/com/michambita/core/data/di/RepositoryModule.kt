package com.michambita.core.data.di

import com.michambita.core.data.repository.impl.AuthRepositoryImpl
import com.michambita.core.data.repository.impl.EmpresaRepositoryImpl
import com.michambita.core.data.repository.impl.MovimientoRepositoryImpl
import com.michambita.core.data.repository.impl.ProductoImageRepositoryImpl
import com.michambita.core.data.repository.impl.ProductoRepositoryImpl
import com.michambita.core.data.repository.impl.SynchronizationRepositoryImpl
import com.michambita.core.data.repository.impl.UserRepositoryImpl
import com.michambita.core.domain.repository.AuthRepository
import com.michambita.core.domain.repository.EmpresaRepository
import com.michambita.core.domain.repository.MovimientoRepository
import com.michambita.core.domain.repository.ProductoImageRepository
import com.michambita.core.domain.repository.ProductoRepository
import com.michambita.core.domain.repository.SynchronizationRepository
import com.michambita.core.domain.repository.UserRepository
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
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProductoRepository(productoRepositoryImpl: ProductoRepositoryImpl): ProductoRepository

    @Binds
    @Singleton
    abstract fun bindProductoImageRepository(productoImageRepositoryImpl: ProductoImageRepositoryImpl): ProductoImageRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindSynchronizationRepository(synchronizationRepositoryImpl: SynchronizationRepositoryImpl): SynchronizationRepository

    @Binds
    @Singleton
    abstract fun bindMovimientoRepository(movimientoRepositoryImpl: MovimientoRepositoryImpl): MovimientoRepository

    @Binds
    @Singleton
    abstract fun bindEmpresaRepository(empresaRepositoryImpl: EmpresaRepositoryImpl): EmpresaRepository
}
