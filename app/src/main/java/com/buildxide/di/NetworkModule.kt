package com.buildxide.di

import com.buildxide.BuildConfig
import com.buildxide.data.remote.api.GitHubActionsApi
import com.buildxide.data.remote.api.GitHubAuthApi
import com.buildxide.data.remote.api.GitHubContentsApi
import com.buildxide.data.remote.api.GitHubRepoApi
import com.buildxide.data.remote.api.GitHubUserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val GITHUB_BASE_URL = "https://api.github.com/"
    private const val GITHUB_AUTH_URL = "https://github.com/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GITHUB_AUTH_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubUserApi(retrofit: Retrofit): GitHubUserApi {
        return retrofit.create(GitHubUserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubRepoApi(retrofit: Retrofit): GitHubRepoApi {
        return retrofit.create(GitHubRepoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubContentsApi(retrofit: Retrofit): GitHubContentsApi {
        return retrofit.create(GitHubContentsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubActionsApi(retrofit: Retrofit): GitHubActionsApi {
        return retrofit.create(GitHubActionsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubAuthApi(authRetrofit: Retrofit): GitHubAuthApi {
        return authRetrofit.create(GitHubAuthApi::class.java)
    }
}
