package com.example.frontendbook.domain.repository

import com.example.frontendbook.data.model.register.RegisterRequest
import com.example.frontendbook.data.model.register.RegisterResponse
import com.example.frontendbook.data.model.resetPassword.ResetPasswordResponse
import com.example.frontendbook.data.model.signIn.SignInRequest
import com.example.frontendbook.data.model.signIn.SignInResponse
import com.example.frontendbook.domain.model.ResetPasswordParams
import retrofit2.Response

interface AuthRepository {
    suspend fun signIn(request: SignInRequest): Response<SignInResponse>
    suspend fun register(request: RegisterRequest): Response<RegisterResponse>
    suspend fun resetPassword(request: ResetPasswordParams): Response<ResetPasswordResponse>
}
