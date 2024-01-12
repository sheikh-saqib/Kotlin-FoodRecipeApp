package com.cookbook.app.service

import com.cookbook.app.resources.network.SpoonacularApi
import com.cookbook.app.utilities.ProjectConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Network {


    @Singleton
    @Provides
    fun apiService(retrofit: Retrofit): SpoonacularApi {
        return retrofit.create(SpoonacularApi::class.java)
    }

    @Singleton
    @Provides
    fun retroFitObject(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ProjectConstants.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun httpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun converterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

}