package com.example.frontendbook.data.repository
import com.example.frontendbook.data.model.register.RegisterRequest
import com.example.frontendbook.data.model.register.RegisterResponse
import com.example.frontendbook.data.model.resetPassword.ResetPasswordResponse
import com.example.frontendbook.data.model.signIn.SignInRequest
import com.example.frontendbook.data.model.signIn.SignInResponse
import com.example.frontendbook.domain.model.ResetPasswordParams
import com.example.frontendbook.domain.repository.AuthRepository
import com.example.frontendbook.retrofit.ApiService
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun signIn(request: SignInRequest): Response<SignInResponse> {
        return apiService.signIn(request)
    }

    override suspend fun register(request: RegisterRequest): Response<RegisterResponse> {
        return apiService.register(request)
    }

    override suspend fun resetPassword(request: ResetPasswordParams): Response<ResetPasswordResponse> {
        TODO("Not yet implemented")
    }
}
