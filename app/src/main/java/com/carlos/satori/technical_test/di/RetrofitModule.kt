package com.carlos.satori.technical_test.di

import com.carlos.satori.technical_test.api.ApiConstants
import com.carlos.satori.technical_test.api.ApiService
import com.carlos.satori.technical_test.api.Token
import com.carlos.satori.technical_test.util.serializers.BooleanDeserializer
import com.carlos.satori.technical_test.util.serializers.BooleanSerializer
import com.carlos.satori.technical_test.util.serializers.DateDeserializaer
import com.carlos.satori.technical_test.util.serializers.DateSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    //Retrofit configuration
    @Provides
    fun provideRetrofit(): ApiService {
        if(Token.retrofitInstance == null){
            Token.retrofitInstance = Retrofit.Builder()
                .baseUrl(ApiConstants.getServerPath())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .client(provideHttpClient())
                .build()
                .create(ApiService::class.java)
        }
        return Token.retrofitInstance!!
    }

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor

    }

    //The api is set as a query unlike other kinds
    private fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor())
            .addInterceptor { chain ->
                val newRequest = chain.request().url
                    .newBuilder()
                    .addQueryParameter("api_key" , Token.token)
                    .build()
                chain.proceed(chain.request().newBuilder().url(newRequest).build())
            }
            .build()
    }
    private fun gson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, BooleanSerializer())
            .registerTypeAdapter(Boolean::class.java, BooleanDeserializer())
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanSerializer())
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanDeserializer())
            .registerTypeAdapter(Date::class.java, DateSerializer())
            .registerTypeAdapter(Date::class.java, DateDeserializaer())
            .create()
    }
}