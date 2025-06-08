package com.example.frontendbook.domain.usecase

import com.example.frontendbook.data.model.signIn.SignInRequest
import com.example.frontendbook.domain.usecase.params.SignInParams
import com.example.frontendbook.retrofit.ApiService
import com.example.frontendbook.ui.signIn.SignInState
import javax.inject.Inject

class SignUseCase @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun execute(params: SignInParams): SignInState {
        return try {
            val response = apiService.signIn(
                SignInRequest(
                    username = params.username,
                    password = params.password
                )
            )
            if (response.isSuccessful) {
                val token = response.body()?.token
                SignInState.Success(token)
            } else {
                SignInState.Error("Hatalı kullanıcı adı veya şifre")
            }
        } catch (e: Exception) {
            SignInState.Error("Giriş sırasında hata oluştu: ${e.message}")
        }
    }
}
