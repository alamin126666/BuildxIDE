package com.buildxide.di

import android.content.Context
import com.buildxide.data.local.BuildxDatabase
import com.buildxide.util.SecureTokenStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSecureTokenStorage(
        @ApplicationContext context: Context
    ): SecureTokenStorage {
        return SecureTokenStorage(context)
    }

    @Provides
    @Singleton
    fun provideBuildxDatabase(
        @ApplicationContext context: Context
    ): BuildxDatabase {
        return BuildxDatabase.getInstance(context)
    }
}
