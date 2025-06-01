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

        // Set up normal nav for bottom navigation
        binding.bottomNav.setupWithNavController(navController)

        // Intercept the middle "Add" button to open the bottom sheet
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.addBookFragment -> {
                    // Show BottomSheetDialogFragment instead of navigating
                    val addBookSheet = AddBookBottomSheet()
                    addBookSheet.show(supportFragmentManager, addBookSheet.tag)
                    false // Prevent navigation to this item
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }
}
