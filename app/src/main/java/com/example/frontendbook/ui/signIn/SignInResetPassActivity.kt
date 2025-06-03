package com.example.frontendbook.ui.signIn

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendbook.databinding.ActivityResetPassBinding
import com.example.frontendbook.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInResetPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPassBinding
    private val viewModel: SignInResetPassViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goReset.setOnClickListener {
            val email = binding.emailInput.text.toString()
            viewModel.sendResetMail(email)
        }

        viewModel.resultMessage.observe(this) { message ->
            showSnackbar(message)
        }
        binding.gobacktoSignin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        binding.gobacktoJoin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showSnackbar(msg: String) {
        val snackbar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#cb6105")))
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(Color.parseColor("#ffffff"))
        snackbar.show()
    }

}
