package com.example.frontendbook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.frontendbook.databinding.MainActivityBinding
import com.example.frontendbook.ui.base.BaseSimpleActivity
import com.example.frontendbook.ui.main.FirstPageActivity

class MainActivity : BaseSimpleActivity<MainActivityBinding>() {

    override fun getViewBinding(): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater)
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
