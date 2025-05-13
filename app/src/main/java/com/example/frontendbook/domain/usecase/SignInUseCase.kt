package com.example.frontendbook.domain.usecase

import com.example.frontendbook.data.model.signIn.SignInRequest
import com.example.frontendbook.domain.repository.AuthRepository
import com.example.frontendbook.retrofit.ApiService
import com.example.frontendbook.ui.signIn.SignInState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(username: String, password: String): SignInState {
        return try {
            val response = repository.signIn(SignInRequest(username, password))
            if (response.isSuccessful) {
                SignInState.Success(response.body()?.token)
            } else {
                SignInState.Error("Hatalı giriş")
            }
        } catch (e: Exception) {
            SignInState.Error("Hata: ${e.message}")
        }
    }
}

