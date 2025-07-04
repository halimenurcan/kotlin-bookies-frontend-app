package com.example.frontendbook.data.remote

import android.content.Context
import com.example.frontendbook.data.api.service.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/"
    private const val API_BASE_URL   = "http://10.0.2.2:8080/"

    /** JWT ve Content-Type header’larını ekler */
    private fun getAuthInterceptor(context: Context): Interceptor = Interceptor { chain ->
        val prefs = context
            .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt_token", null)

        chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
            }
            addHeader("Content-Type", "application/json")
        }
            .build()
            .let(chain::proceed)
    }

    /** Body-level logging */
    private fun getLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    /** Tüm servisler için ortak client */
    private fun createClient(context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .addInterceptor(getLoggingInterceptor())
            .build()

    /** User, Auth ve etkileşim endpoint’leri */
    fun userApiService(context: Context): UserApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)

    fun readApiService(context: Context): ReadApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReadApiService::class.java)

    /** Google Books harici, token gerektirmez */
    val booksApiService: BooksApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BOOKS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApiService::class.java)
    }

    /** Listeler, takipler, beğeniler, yorum beğenileri, bildirimler… */
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

    fun followersApiService(context: Context): FollowersApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FollowersApiService::class.java)

    fun likedBooksApiService(context: Context): LikedBooksApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LikedBooksApiService::class.java)

    fun likedCommentsApiService(context: Context): LikedReviewsApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LikedReviewsApiService::class.java)

    fun likedReviewsApiService(context: Context): LikedReviewsApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LikedReviewsApiService::class.java)

    fun reviewsApiService(context: Context): ReviewsApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReviewsApiService::class.java)

    fun notificationApiService(context: Context): NotificationApiService =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationApiService::class.java)
}
