package com.example.frontendbook.domain.usecase

import com.example.frontendbook.domain.model.ResetPasswordParams
import com.example.frontendbook.domain.repository.AuthRepository
import com.example.frontendbook.ui.signIn.SignInState
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<ResetPasswordParams, SignInState>() {

    override suspend fun execute(params: ResetPasswordParams): SignInState {
        val email = params.email

        return try {
            if (email.length < 8 || !email.contains("@")) {
                SignInState.Error("Geçersiz e-posta")
            } else {
                val response = repository.resetPassword(params)
                if (response.isSuccessful) {
                    SignInState.Success(response.body()?.message)
                } else {
                    SignInState.Error("Mail gönderilemedi.")
                }
            }
        } catch (e: Exception) {
            SignInState.Error("Hata: ${e.message}")
        }
    }
}
