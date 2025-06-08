package com.example.frontendbook.ui.register

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.example.frontendbook.databinding.ActivityRegisterBinding
import com.example.frontendbook.ui.base.BaseActivity
import com.example.frontendbook.ui.signIn.SignInActivity
import com.example.frontendbook.ui.signIn.SignInResetPassActivity
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class RegisterActivity : BaseActivity<RegisterViewModel, RegisterState, ActivityRegisterBinding>() {

    override val viewModel: RegisterViewModel by viewModels()
    override val state get() = viewModel.registerState
    override fun getViewBinding() = ActivityRegisterBinding.inflate(layoutInflater)

    override fun setupViews() {
        binding.registerButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            viewModel.register(username, email, password)
        }
        binding.signInButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        binding.resetPassButton.setOnClickListener {
            startActivity(Intent(this, SignInResetPassActivity::class.java))
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
