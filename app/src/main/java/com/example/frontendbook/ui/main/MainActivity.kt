package com.example.frontendbook.ui.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.frontendbook.R
import com.example.frontendbook.databinding.ActivityMainBinding
import com.example.frontendbook.ui.base.BaseSimpleActivity

class MainActivity : BaseSimpleActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)
    }
}
