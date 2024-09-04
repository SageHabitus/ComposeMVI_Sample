package com.example.composemvi.data.source.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://dapi.kakao.com/"

    class AuthorizationInterceptor(private val apiKey: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", "KakaoAK $apiKey")
            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

    @Provides
    fun provideHttpLogInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideAuthorizationInterceptor(): AuthorizationInterceptor =
        AuthorizationInterceptor("a2ca5c471c0a55555598031e7840facb")

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthorizationInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
