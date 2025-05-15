package com.example.frontendbook.ui.signIn

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendbook.databinding.SignInResetPassActivityBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInResetPassActivity : AppCompatActivity() {

    private lateinit var binding: SignInResetPassActivityBinding
    private val viewModel: SignInResetPassViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInResetPassActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goReset.setOnClickListener {
            val email = binding.emailInput.text.toString()
            viewModel.sendResetMail(email)
        }

        viewModel.resultMessage.observe(this) { message ->
            showSnackbar(message)
        }
    }

    private fun showSnackbar(msg: String) {
        val snackbar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0e1d31")))
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(Color.parseColor("#cccccc"))
        snackbar.show()
    }

}
