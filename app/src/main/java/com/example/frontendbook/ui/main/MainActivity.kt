package com.example.frontendbook

import android.content.Intent
import android.graphics.Color                         // ← Arkaplan rengini değiştirmek için gerekli!
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout  // ← İşte eksik olan satır, BUNU EKLEDİK!
import com.example.frontendbook.ui.main.FirstPageActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_activity)
        val layout = findViewById<ConstraintLayout>(R.id.main)

        layout.setBackgroundColor(Color.parseColor("#FAF7F2"))

        // BOOKIES yazısını yukarı kaydıran animasyonu başlat
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        // 1.5 saniye sonra FirstPageActivity'ye geç
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, FirstPageActivity::class.java)
            startActivity(intent)
            finish()
            // Sayfa geçişine fade animasyon (isteğe bağlı)
        }, 2000)
    }
}
