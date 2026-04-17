package com.buildxide.di

import com.buildxide.data.local.BuildxDatabase
import com.buildxide.data.local.dao.BuildHistoryDao
import com.buildxide.data.local.dao.OpenFileDao
import com.buildxide.data.local.dao.ProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProjectDao(database: BuildxDatabase): ProjectDao {
        return database.projectDao()
    }

    @Provides
    @Singleton
    fun provideOpenFileDao(database: BuildxDatabase): OpenFileDao {
        return database.openFileDao()
    }

    @Provides
    @Singleton
    fun provideBuildHistoryDao(database: BuildxDatabase): BuildHistoryDao {
        return database.buildHistoryDao()
    }
}
