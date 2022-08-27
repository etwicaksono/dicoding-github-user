package com.etwicaksono.githubuser.api

import androidx.viewbinding.BuildConfig
import com.etwicaksono.githubuser.entity.UsersListItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("users")
    suspend fun getUsersList(@Query("since") since: Int = 0): Response<List<UsersListItem>>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val loggingInterceptor = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }

                val client = OkHttpClient.Builder().addInterceptor { chain ->
                    val request = chain.request()
                    val builder = request.newBuilder().method(request.method, request.body)
                    val mutatedRequest = builder.build()
                    val response = chain.proceed(mutatedRequest)
                    response
                }.addInterceptor(loggingInterceptor).build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}