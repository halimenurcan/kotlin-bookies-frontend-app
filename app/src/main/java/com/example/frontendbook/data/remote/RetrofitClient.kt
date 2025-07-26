package com.example.frontendbook.data.remote

import FollowersApiService
import android.content.Context
import android.util.Log
import com.example.frontendbook.data.api.service.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val API_BASE_URL = "http://10.0.2.2:8080/api/"


    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.d("Network", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }


    private fun getAuthInterceptor(context: Context): Interceptor = Interceptor { chain ->
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt_token", null)
        Log.d("RetrofitClient", "AuthInterceptor — token: $token")

        val request = chain.request().newBuilder().apply {
            token?.takeIf { it.isNotBlank() }?.let {
                addHeader("Authorization", "Bearer $it")
            }
            addHeader("Content-Type", "application/json")
        }.build()

        Log.d("RetrofitClient", "AuthInterceptor — headers: ${request.headers}")
        chain.proceed(request)
    }


    private fun createOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()
    }


    private fun getRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun searchApiService(context: Context): SearchApiService =
        getRetrofit(context).create(SearchApiService::class.java)
    fun statusApiService(context: Context): StatusApiService =
        getRetrofit(context).create(StatusApiService::class.java)

    fun userApiService(context: Context): UserApiService =
        getRetrofit(context).create(UserApiService::class.java)

    fun listsApiService(context: Context): ListsApiService =
        getRetrofit(context).create(ListsApiService::class.java)

    fun listFollowsApiService(context: Context): ListFollowsApiService =
        getRetrofit(context).create(ListFollowsApiService::class.java)

    fun booksApiService(context: Context): BooksApiService =
        getRetrofit(context).create(BooksApiService::class.java)

    fun likedReviewsApiService(context: Context): LikedReviewsApiService =
        getRetrofit(context).create(LikedReviewsApiService::class.java)

    fun notificationApiService(context: Context): NotificationApiService =
        getRetrofit(context).create(NotificationApiService::class.java)

    fun followersApiService(context: Context): FollowersApiService =
        getRetrofit(context).create(FollowersApiService::class.java)

    fun readApiService(context: Context): ReadApiService =
        getRetrofit(context).create(ReadApiService::class.java)

    fun reviewsApiService(context: Context): ReviewsApiService =
        getRetrofit(context).create(ReviewsApiService::class.java)

    fun likedBooksApiService(context: Context): LikedBooksApiService =
        getRetrofit(context).create(LikedBooksApiService::class.java)
}
