package com.example.frontendbook.ui.main

import android.content.Intent
import android.graphics.Color

import com.example.frontendbook.databinding.FirstpageActivityBinding
import com.example.frontendbook.ui.base.BaseSimpleActivity
import com.example.frontendbook.ui.register.RegisterActivity
import com.example.frontendbook.ui.signIn.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstPageActivity : BaseSimpleActivity<FirstpageActivityBinding>() {

    override fun getViewBinding(): FirstpageActivityBinding {
        return FirstpageActivityBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        // Arkaplan rengi
        binding.main.setBackgroundColor(Color.parseColor("#FAF7F2"))

        binding.signin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.createaccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
