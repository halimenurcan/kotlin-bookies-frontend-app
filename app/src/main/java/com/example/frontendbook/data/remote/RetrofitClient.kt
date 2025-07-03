package com.example.frontendbook.data.remote

import android.content.Context
import com.example.frontendbook.data.api.service.BooksApiService
import com.example.frontendbook.data.api.service.FollowersApiService
import com.example.frontendbook.data.api.service.LikedBooksApiService
import com.example.frontendbook.data.api.service.LikedCommentsApiService
import com.example.frontendbook.data.api.service.LikedReviewsApiService
import com.example.frontendbook.data.api.service.ListFollowsApiService
import com.example.frontendbook.data.api.service.ListsApiService
import com.example.frontendbook.data.api.service.NotificationApiService
import com.example.frontendbook.data.api.service.UserApiService
import com.example.frontendbook.data.api.service.ReadApiService
import com.example.frontendbook.data.api.service.ReviewsApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/"
    // Android emulator localhost adresi için 10.0.2.2 kullanın
    private const val USER_BASE_URL = "http://10.0.2.2:8080/"

    private fun getAuthInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val token = prefs.getString("jwt_token", null)

            val requestBuilder = chain.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            requestBuilder.addHeader("Content-Type", "application/json")

            chain.proceed(requestBuilder.build())
        }
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    fun userApiService(context: Context): UserApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    fun readApiService(context: Context): ReadApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReadApiService::class.java)
    }

    val booksApiService: BooksApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BOOKS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApiService::class.java)
    }
    fun likedBooksApiService(context: Context): LikedBooksApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)   // aynı USER_BASE_URL kullanılıyor
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LikedBooksApiService::class.java)
    }
    fun listFollowsApiService(context: Context): ListFollowsApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ListFollowsApiService::class.java)
    }

    fun listsApiService(context: Context): ListsApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ListsApiService::class.java)
    }
    fun followersApiService(context: Context): FollowersApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FollowersApiService::class.java)
    }
    fun notificationsApiService(context: Context): NotificationApiService {
            val client = OkHttpClient.Builder()
             .addInterceptor(getAuthInterceptor(context))
              .addInterceptor(getLoggingInterceptor())
              .build()
            return Retrofit.Builder()
              .baseUrl(USER_BASE_URL)
              .client(client)
              .addConverterFactory(GsonConverterFactory.create())
              .build()
              .create(NotificationApiService::class.java)
          }
    fun likedCommentsApiService(context: Context): LikedCommentsApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LikedCommentsApiService::class.java)
    }
    fun likedReviewsApiService(context: Context): LikedReviewsApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LikedReviewsApiService::class.java)
    }
    fun reviewsApiService(context: Context): ReviewsApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReviewsApiService::class.java)
    }
}


