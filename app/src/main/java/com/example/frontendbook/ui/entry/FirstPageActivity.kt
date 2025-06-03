package com.example.frontendbook.ui.entry

import android.content.Intent

import com.example.frontendbook.databinding.ActivityFirstpageBinding
import com.example.frontendbook.ui.base.BaseSimpleActivity
import com.example.frontendbook.ui.register.RegisterActivity
import com.example.frontendbook.ui.signIn.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstPageActivity : BaseSimpleActivity<ActivityFirstpageBinding>() {

    override fun getViewBinding(): ActivityFirstpageBinding {
        return ActivityFirstpageBinding.inflate(layoutInflater)
    }

    override fun setupViews() {

        binding.signin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.createaccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
