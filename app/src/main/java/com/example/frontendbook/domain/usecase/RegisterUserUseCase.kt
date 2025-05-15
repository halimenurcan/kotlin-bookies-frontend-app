package com.example.frontendbook.domain.usecase

import com.example.frontendbook.data.model.register.RegisterRequest
import com.example.frontendbook.domain.repository.AuthRepository
import com.example.frontendbook.domain.usecase.params.RegisterParams
import com.example.frontendbook.ui.register.RegisterState
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<RegisterParams, RegisterState>() {

    override suspend fun execute(params: RegisterParams): RegisterState {
        return try {
            val response = repository.register(
                RegisterRequest(
                    username = params.username,
                    email = params.email,
                    password = params.password
                )
            )
            if (response.isSuccessful) {
                RegisterState.Success(response.body()?.message ?: "Join successfully")
            } else {
                RegisterState.Error("Error Join")
            }
        } catch (e: Exception) {
            RegisterState.Error("Error: ${e.message}")
        }
    }
}
