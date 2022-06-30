package com.ananjay.noteitdown.di

import com.ananjay.noteitdown.api.AuthInterceptor
import com.ananjay.noteitdown.api.NotesApi
import com.ananjay.noteitdown.api.UserApi
import com.ananjay.noteitdown.utils.AppConstants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserApi(retrofitBuilder: Retrofit.Builder): UserApi{
        return retrofitBuilder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providesNotesApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NotesApi{
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(NotesApi::class.java)
    }
}