package com.example.frontendbook.ui.signIn

import android.content.Intent
import android.util.Log
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
                // TODO: ProgressBar gösterilebilir
            }
            is SignInState.Success -> {
                if (!state.token.isNullOrEmpty()) {
                    saveTokenToPrefs(state.token)
                    goToMain()
                } else {
                    showToast("Giriş başarılı fakat token alınamadı.")
                }
            }
            is SignInState.Error -> showToast(state.message)
        }
    }

    private fun saveTokenToPrefs(token: String) {
        Log.d("SignInDebug", "Gelen token: $token")
        getSharedPreferences("auth", MODE_PRIVATE)
            .edit()
            .putString("jwt_token", token)
            .apply()
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
