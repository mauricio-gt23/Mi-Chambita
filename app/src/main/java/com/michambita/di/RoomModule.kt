package com.michambita.di

import android.content.Context
import androidx.room.Room
import com.michambita.data.database.SynchronizationDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val SYNCHRONIZATION_DATABASE_NAME = "synchronization_database"

    @Singleton
    @Provides
    fun provideSynchronizationRoom(@ApplicationContext context: Context)=
        Room.databaseBuilder(context, SynchronizationDB::class.java, SYNCHRONIZATION_DATABASE_NAME)
            .build()
}