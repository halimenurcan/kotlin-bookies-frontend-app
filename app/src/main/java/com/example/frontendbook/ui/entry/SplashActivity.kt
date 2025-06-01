package com.example.frontendbook

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.frontendbook.databinding.SplashActivityBinding
import com.example.frontendbook.ui.base.BaseSimpleActivity
import com.example.frontendbook.ui.entry.FirstPageActivity

class SplashActivity : BaseSimpleActivity<SplashActivityBinding>() {

    override fun getViewBinding(): SplashActivityBinding {
        return SplashActivityBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        // Lottie zaten otomatik oynuyor (lottie_autoPlay = true)

        // 1.5 saniye sonra FirstPageActivity’ye geç
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, FirstPageActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}
