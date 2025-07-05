package com.example.frontendbook.data.remote

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

    private fun getAuthInterceptor(context: Context): Interceptor = Interceptor { chain ->
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt_token", null)
        Log.d("RetrofitClient", "AuthInterceptor — token from prefs: $token")

        val request = chain.request().newBuilder().apply {
            token?.takeIf { it.isNotEmpty() }?.let {
                addHeader("Authorization", "Bearer $it")
            }
            addHeader("Content-Type", "application/json")
        }.build()

        Log.d("RetrofitClient", "AuthInterceptor — final headers: ${request.headers}")
        chain.proceed(request)
    }

    private fun getLoggingInterceptor() =
        HttpLoggingInterceptor { msg -> Log.d("RetrofitClient", "OkHttp — $msg") }
            .apply { level = HttpLoggingInterceptor.Level.BODY }

    private fun createClient(context: Context): OkHttpClient {
        Log.d("RetrofitClient", "Building OkHttpClient…")
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()
        Log.d("RetrofitClient", "OkHttpClient built; interceptors = ${client.interceptors}")
        return client
    }

    private fun buildRetrofit(context: Context): Retrofit {
        val client = createClient(context)
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL).also {
                Log.d("RetrofitClient", "Retrofit.Builder() baseUrl = $API_BASE_URL")
            }
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        Log.d("RetrofitClient", "Retrofit instance created: baseUrl=${retrofit.baseUrl()}")
        return retrofit
    }
    fun listsApiService(context: Context): ListsApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ListsApiService::class.java)

    fun listFollowsApiService(context: Context): ListFollowsApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ListFollowsApiService::class.java)
    fun userApiService(context: Context): UserApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    fun booksApiService(context: Context): BooksApiService {
        Log.d("RetrofitClient", "Creating BooksApiService…")
        val service = buildRetrofit(context).create(BooksApiService::class.java)
        Log.d("RetrofitClient", "BooksApiService created: $service")
        return service
    }

    fun likedReviewsApiService(context: Context): LikedReviewsApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LikedReviewsApiService::class.java)
    fun notificationApiService(context: Context): NotificationApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationApiService::class.java)
    fun followersApiService(context: Context): FollowersApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FollowersApiService::class.java)
    fun readApiService(context: Context): ReadApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReadApiService::class.java)
    fun reviewsApiService(context: Context): ReviewsApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReviewsApiService::class.java)

    fun likedBooksApiService(context: Context): LikedBooksApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LikedBooksApiService::class.java)
    // Diğer service’ler de aynı mantıkla…
}
