package com.example.frontendbook.data.repository
import android.util.Log
import com.example.frontendbook.data.model.register.RegisterRequest
import com.example.frontendbook.data.model.register.RegisterResponse
import com.example.frontendbook.data.model.resetPassword.ResetPasswordResponse
import com.example.frontendbook.data.model.signIn.SignInRequest
import com.example.frontendbook.data.model.signIn.SignInResponse
import com.example.frontendbook.domain.model.ResetPasswordParams
import com.example.frontendbook.domain.repository.AuthRepository
import com.example.frontendbook.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun signIn(request: SignInRequest): Response<SignInResponse> {

        Log.d("AuthRepo", "→ signIn() called with request: $request")
        val response = apiService.signIn(request)

        Log.d("AuthRepo", "← signIn() response: code=${response.code()}, message=${response.message()}, url=${response.raw().request.url}")
        if (!response.isSuccessful) {

            val errBody = response.errorBody()?.string()
            Log.e("AuthRepo", "!! signIn failed: errorBody=$errBody")
        } else {
            Log.d("AuthRepo", "✓ signIn successful: body=${response.body()}")
        }
        return response
    }

    override suspend fun register(request: RegisterRequest): Response<RegisterResponse> {
        Log.d("AuthRepo", "→ register() called with request: $request")
        val response = apiService.register(request)
        Log.d("AuthRepo", "← register() response: code=${response.code()}, message=${response.message()}, url=${response.raw().request.url}")
        if (!response.isSuccessful) {
            val errBody = response.errorBody()?.string()
            Log.e("AuthRepo", "!! register failed: errorBody=$errBody")
        } else {
            Log.d("AuthRepo", "✓ register successful: body=${response.body()}")
        }
        return response
    }

    override suspend fun resetPassword(request: ResetPasswordParams): Response<ResetPasswordResponse> {
        TODO("Not yet implemented")
    }
}
