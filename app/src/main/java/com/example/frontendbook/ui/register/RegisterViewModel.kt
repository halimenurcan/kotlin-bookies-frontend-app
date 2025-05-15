package com.example.frontendbook.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.domain.usecase.RegisterUserUseCase
import com.example.frontendbook.domain.usecase.params.RegisterParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> get() = _registerState

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val params = RegisterParams(username, email, password)
            val result = registerUseCase.execute(params)
            _registerState.value = result
        }
    }
}
