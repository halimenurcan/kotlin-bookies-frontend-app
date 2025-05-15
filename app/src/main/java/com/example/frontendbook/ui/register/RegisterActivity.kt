package com.example.frontendbook.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendbook.databinding.RegisterActivityBinding
import com.example.frontendbook.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class RegisterActivity : BaseActivity<RegisterViewModel, RegisterState, RegisterActivityBinding>() {

    override val viewModel: RegisterViewModel by viewModels()
    override val state get() = viewModel.registerState
    override fun getViewBinding() = RegisterActivityBinding.inflate(layoutInflater)

    override fun setupViews() {
        binding.registerButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            viewModel.register(username, email, password)
        }
    }

    override fun handleState(state: RegisterState) {
        when (state) {
            is RegisterState.Loading -> { /* Show loading */ }
            is RegisterState.Success -> showToast(state.message)
            is RegisterState.Error -> showToast(state.errorMessage)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
