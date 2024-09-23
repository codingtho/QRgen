package com.codingtho.qrgen.di

import android.app.Application
import androidx.room.Room
import com.codingtho.qrgen.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "qr_code_database")
            .build()

    @Singleton
    @Provides
    fun provideQrCodeDao(db: AppDatabase) = db.getQrCodeDao()
}