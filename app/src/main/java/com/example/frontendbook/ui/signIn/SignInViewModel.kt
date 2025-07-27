package com.example.frontendbook.ui.signIn

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.domain.usecase.SignInUseCase
import com.example.frontendbook.domain.usecase.params.SignInParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _signInState = MutableLiveData<SignInState>()
    val signInState: LiveData<SignInState> = _signInState

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            try {
                _signInState.value = SignInState.Loading
                val params = SignInParams(username, password)
                val result = signInUseCase.execute(params)
                Log.d("SignInDebug", "Sign-in result: $result")
                _signInState.value = result
            } catch (e: Exception) {
                _signInState.value = SignInState.Error("Login error: ${e.localizedMessage}")
                Log.e("SignInDebug", "Exception during sign-in", e)
            }
        }
    }
}
