package com.costguard.android.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var currentBaseUrl: String = "http://10.0.2.2:8001/"
    private var service: CostGuardApiService? = null

    fun getBaseUrl(): String = currentBaseUrl

    fun updateBaseUrl(newUrl: String) {
        val sanitized = if (newUrl.endsWith("/")) newUrl else "$newUrl/"
        currentBaseUrl = sanitized
        service = createService(sanitized)
    }

    fun getService(): CostGuardApiService {
        if (service == null) {
            service = createService(currentBaseUrl)
        }
        return service!!
    }

    private fun createService(url: String): CostGuardApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CostGuardApiService::class.java)
    }
}
