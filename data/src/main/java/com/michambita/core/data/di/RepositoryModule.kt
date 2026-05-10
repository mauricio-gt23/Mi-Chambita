package com.michambita.data.di

import com.michambita.data.local.preferences.BusinessTypePreferencesRepositoryImpl
import com.michambita.data.repository.impl.AuthRepositoryImpl
import com.michambita.data.repository.impl.EmpresaRepositoryImpl
import com.michambita.data.repository.impl.MovimientoRepositoryImpl
import com.michambita.data.repository.impl.ProductoImageRepositoryImpl
import com.michambita.data.repository.impl.ProductoRepositoryImpl
import com.michambita.data.repository.impl.SynchronizationRepositoryImpl
import com.michambita.data.repository.impl.UserRepositoryImpl
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.EmpresaRepository
import com.michambita.domain.repository.MovimientoRepository
import com.michambita.domain.repository.ProductoImageRepository
import com.michambita.domain.repository.ProductoRepository
import com.michambita.domain.repository.SynchronizationRepository
import com.michambita.domain.repository.UserRepository
import com.michambita.domain.repository.preference.BusinessTypePreferencesRepository
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

    // PREF REPOSITORY
    @Binds
    @Singleton
    abstract fun bindBusinessTypeRepository(businessTypePreferenceRepository: BusinessTypePreferencesRepositoryImpl): BusinessTypePreferencesRepository
}
