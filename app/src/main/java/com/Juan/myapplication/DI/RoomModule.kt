package com.Juan.myapplication.DI

import android.content.Context
import androidx.room.Room
import com.Juan.myapplication.Data.DataBase.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val ALQUILER_DATABASE_NAME = "alquiler_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, ALQUILER_DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

    @Singleton @Provides fun providePersonaDao(db: AppDatabase) = db.personaDao()
}
