package com.example.frontendbook.ui.signIn

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.example.frontendbook.databinding.SignInActivityBinding
import com.example.frontendbook.ui.base.BaseActivity
import com.example.frontendbook.ui.main.MainPageActivity
import com.example.frontendbook.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<SignInViewModel, SignInState, SignInActivityBinding>() {

    override val viewModel: SignInViewModel by viewModels()
    override val state get() = viewModel.signInState
    override fun getViewBinding(): SignInActivityBinding = SignInActivityBinding.inflate(layoutInflater)

    override fun setupViews() {
        binding.goMain.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
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
                // TODO: show loading spinner if needed
            }
            is SignInState.Success -> goToMain(state.token)
            is SignInState.Error -> showToast(state.message)
        }
    }

    private fun goToMain(token: String?) {
        val intent = Intent(this, MainPageActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
