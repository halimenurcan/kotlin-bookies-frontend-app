package com.example.frontendbook.domain.usecase

import android.util.Log
import com.example.frontendbook.data.model.signIn.SignInRequest
import com.example.frontendbook.domain.repository.AuthRepository
import com.example.frontendbook.domain.usecase.params.SignInParams
import com.example.frontendbook.ui.signIn.SignInState
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(params: SignInParams): SignInState {
        Log.d("SignInRequest", "username=${params.username}, password=${params.password}")
        return try {
            val response = repository.signIn(SignInRequest(params.username, params.password))
            if (response.isSuccessful) {
                val body = response.body()
                val token = body?.token
                val userId = body?.id ?: -1L

                Log.d("SignInUseCase", "Token: $token, UserId: $userId")

                if (!token.isNullOrEmpty() && userId != -1L) {
                    SignInState.Success(token, userId)
                } else {
                    SignInState.Error("Could not obtain a valid token or user ID from the server..")
                }
            } else {
                SignInState.Error("Invalid entry: ${response.code()}")
            }
        } catch (e: Exception) {
            SignInState.Error("Error: ${e.message}")
        }
    }
}
