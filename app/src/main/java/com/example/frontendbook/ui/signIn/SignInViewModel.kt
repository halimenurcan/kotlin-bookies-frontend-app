package com.example.frontendbook.ui.signIn

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
            _signInState.value = SignInState.Loading
            var params = SignInParams(username,password)
            val result = signInUseCase.execute(params)
            _signInState.value = result
        }
    }
}
