package com.example.frontendbook.domain.usecase

import com.example.frontendbook.data.model.register.RegisterRequest
import com.example.frontendbook.domain.repository.AuthRepository
import com.example.frontendbook.retrofit.ApiService
import com.example.frontendbook.ui.register.RegisterState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(username: String, email: String, password: String): RegisterState {
        return try {
            val response = repository.register(RegisterRequest(username, email, password))
            if (response.isSuccessful) {
                RegisterState.Success(response.body()?.message ?: "Kayıt başarılı")
            } else {
                RegisterState.Error("Kayıt başarısız")
            }
        } catch (e: Exception) {
            RegisterState.Error("Hata: ${e.message}")
        }
    }
}

