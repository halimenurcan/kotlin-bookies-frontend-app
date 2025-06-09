package com.example.frontendbook

import android.content.Intent
import android.os.Bundle
import com.example.frontendbook.databinding.ActivitySplashBinding
import com.example.frontendbook.ui.base.BaseSimpleActivity
import com.example.frontendbook.ui.entry.FirstPageActivity
import com.example.frontendbook.ui.main.MainActivity

class SplashActivity : BaseSimpleActivity<ActivitySplashBinding>() {

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        val sharedPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPrefs.getString("token", null)

        val intent = if (!token.isNullOrEmpty()) {
            // ✅ Token varsa ana sayfaya git
            Intent(this, MainActivity::class.java)
        } else {
            // ❌ Token yoksa giriş/kayıt sayfasına yönlendir
            Intent(this, FirstPageActivity::class.java)
        }

        // 1.5 saniye bekletme sonrası yönlendir
        binding.root.postDelayed({
            startActivity(intent)
            finish()
        }, 1500)
    }
}
