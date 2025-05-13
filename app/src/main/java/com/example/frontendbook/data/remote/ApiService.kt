package com.example.frontendbook.retrofit

import com.example.frontendbook.data.model.register.RegisterRequest
import com.example.frontendbook.data.model.register.RegisterResponse
import com.example.frontendbook.data.model.signIn.SignInRequest
import com.example.frontendbook.data.model.signIn.SignInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/auth/login")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}
