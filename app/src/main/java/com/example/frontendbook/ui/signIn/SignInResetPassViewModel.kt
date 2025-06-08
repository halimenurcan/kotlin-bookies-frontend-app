package com.example.frontendbook.ui.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInResetPassViewModel @Inject constructor() : ViewModel() {

    private val _resultMessage = MutableLiveData<String>()
    val resultMessage: LiveData<String> = _resultMessage

    fun sendResetMail(email: String) {
        if (email.length < 8 || !email.contains("@")) {
            _resultMessage.value = "Invalid e-mail adresse."
        } else {
            _resultMessage.value = "Mail has been sent."
        }
    }
}
