package com.example.frontendbook.ui.signIn

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.example.frontendbook.databinding.ActivitySignInBinding
import com.example.frontendbook.ui.base.BaseActivity
import com.example.frontendbook.ui.main.MainActivity
import com.example.frontendbook.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<SignInViewModel, SignInState, ActivitySignInBinding>() {

    override val viewModel: SignInViewModel by viewModels()
    override val state get() = viewModel.signInState
    override fun getViewBinding(): ActivitySignInBinding = ActivitySignInBinding.inflate(layoutInflater)

    override fun setupViews() {
        binding.goMain.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            viewModel.signIn(username, password)
        }

        binding.resetPass.setOnClickListener {
            startActivity(Intent(this, SignInResetPassActivity::class.java))
        }

        binding.gobacktoJoin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun handleState(state: SignInState) {
        when (state) {
            is SignInState.Loading -> {
                // loading gösterebilirsin
            }
            is SignInState.Success -> goToMain(state.token)
            is SignInState.Error -> showToast(state.message)
        }
    }

    private fun goToMain(token: String?) {
        if (!token.isNullOrEmpty()) {
            getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .putString("token", token)
                .apply()
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
