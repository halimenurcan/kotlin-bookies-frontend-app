package com.example.frontendbook.ui.main
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.frontendbook.databinding.MainPageActivityBinding
import com.example.frontendbook.ui.base.BaseSimpleActivity

class MainPageActivity : BaseSimpleActivity<MainPageActivityBinding>() {

    override fun getViewBinding(): MainPageActivityBinding {
        return MainPageActivityBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
