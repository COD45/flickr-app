package com.szoldapps.flickrhometask.app.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.szoldapps.flickrhometask.BuildConfig
import com.szoldapps.flickrhometask.data.remote.rest.FlickrFeedApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

/**
 * Module to provide rest specific classes
 */
@Module
@InstallIn(SingletonComponent::class)
internal open class RestModule {

    @Provides
    @Reusable
    fun provideDefaultOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        val cacheDir = File(appContext.cacheDir.absolutePath, HTTP_CACHE_DIRECTORY_NAME)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor())
            .cache(Cache(cacheDir, HTTP_CACHE_SIZE))
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Reusable
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Provides
    fun provideFlickrFeedApi(retrofit: Retrofit): FlickrFeedApi =
        retrofit.create(FlickrFeedApi::class.java)

    private fun loggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    private companion object {
        const val BASE_URL = "https://api.flickr.com/services/feeds/"
        const val HTTP_CACHE_DIRECTORY_NAME = "HttpCache"
        const val HTTP_CACHE_SIZE = 10 * 1024 * 1024L // 10 MiB
    }
}
