package com.ananjay.noteitdown.di

import com.ananjay.noteitdown.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("")
            .build()
    }

    @Singleton
    @Provides
    fun providesUserApi(retrofit: Retrofit): UserApi{
        return retrofit.create(UserApi::class.java)
    }
}