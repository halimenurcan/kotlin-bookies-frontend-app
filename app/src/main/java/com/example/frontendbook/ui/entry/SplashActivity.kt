package com.example.frontendbook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.frontendbook.databinding.ActivitySplashBinding
import com.example.frontendbook.ui.base.BaseSimpleActivity
import com.example.frontendbook.ui.entry.FirstPageActivity
import com.example.frontendbook.ui.main.MainActivity

class SplashActivity : BaseSimpleActivity<ActivitySplashBinding>() {

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        val sharedPrefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val token = sharedPrefs.getString("jwt_token", null)

        val intent = if (!token.isNullOrEmpty()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, FirstPageActivity::class.java)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, 4000)
    }
}
