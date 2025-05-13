package com.example.frontendbook.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendbook.databinding.ActivitySignInBinding
import com.example.frontendbook.signIn.MainPageActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goMain.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            viewModel.signIn(username, password)
        }

        observeState()
    }

    private fun observeState() {
        viewModel.signInState.observe(this) { state ->
            when (state) {
                is SignInState.Loading -> showLoading()
                is SignInState.Success -> goToMain(state.token)
                is SignInState.Error -> showError(state.message)
            }
        }
    }

    private fun showLoading() {
        // Progress göster
    }

    private fun goToMain(token: String?) {
        val intent = Intent(this, MainPageActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
        finish()
    }

    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
