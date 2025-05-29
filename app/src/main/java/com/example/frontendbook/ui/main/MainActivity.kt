package com.example.frontendbook.ui.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.frontendbook.R
import com.example.frontendbook.databinding.ActivityMainBinding
import com.example.frontendbook.ui.addBook.AddBookBottomSheet
import com.example.frontendbook.ui.base.BaseSimpleActivity

class MainActivity : BaseSimpleActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        val navController = findNavController(R.id.nav_host_fragment)

        // Normal navigation setup
        binding.bottomNav.setupWithNavController(navController)

        // Manual interception for the Add button (center)
        binding.bottomNav.setOnItemSelectedListener { item ->
            return@setOnItemSelectedListener when (item.itemId) {
                R.id.addBookFragment -> {
                    val addBookSheet = AddBookBottomSheet()
                    addBookSheet.show(supportFragmentManager, addBookSheet.tag)
                    false // Do not trigger navigation
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }
}
