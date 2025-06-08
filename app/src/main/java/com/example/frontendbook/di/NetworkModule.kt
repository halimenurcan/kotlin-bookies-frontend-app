package com.example.frontendbook.di
import com.example.frontendbook.data.repository.AuthRepositoryImpl
import com.example.frontendbook.domain.repository.AuthRepository
import com.example.frontendbook.domain.usecase.RegisterUserUseCase
import com.example.frontendbook.domain.usecase.SignInUseCase
import com.example.frontendbook.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // ← burayı kendi API adresinle değiştir
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(apiService: ApiService): AuthRepository =
        AuthRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(repository: AuthRepository): RegisterUserUseCase =
        RegisterUserUseCase(repository)

    @Provides
    @Singleton
    fun provideSignInUseCase(repository: AuthRepository): SignInUseCase =
        SignInUseCase(repository)
}
