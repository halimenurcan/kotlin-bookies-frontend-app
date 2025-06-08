package com.example.frontendbook.ui.main

import androidx.navigation.fragment.NavHostFragment
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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up normal nav for bottom navigation
        binding.bottomNav.setupWithNavController(navController)

        // Intercept the middle "Add" button to open the bottom sheet
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.addBookFragment -> {
                    val addBookSheet = AddBookBottomSheet()
                    addBookSheet.show(supportFragmentManager, addBookSheet.tag)
                    false
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }
}
