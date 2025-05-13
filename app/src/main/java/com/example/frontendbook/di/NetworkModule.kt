package com.example.frontendbook.di
import com.example.frontendbook.data.repository.AuthRepositoryImpl
import com.example.frontendbook.domain.repository.AuthRepository
import com.example.frontendbook.domain.usecase.RegisterUserUseCase
import com.example.frontendbook.domain.usecase.SignInUseCase
import com.example.frontendbook.retrofit.ApiService
import com.example.frontendbook.retrofit.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitClient.api
    }

    @Provides
    @Singleton
    fun provideAuthRepository(apiService: ApiService): AuthRepository {
        return AuthRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(repository: AuthRepository): RegisterUserUseCase {
        return RegisterUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSignInUseCase(repository: AuthRepository): SignInUseCase {
        return SignInUseCase(repository)
    }
}
