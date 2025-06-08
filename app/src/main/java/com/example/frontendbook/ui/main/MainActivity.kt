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
        // 👇 NavHostFragment üzerinden güvenli şekilde navController alıyoruz
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Bottom nav ile bağla
        binding.bottomNav.setupWithNavController(navController)

        // Orta butona özel işlem
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.addBookFragment -> {
                    val addBookSheet = AddBookBottomSheet()
                    addBookSheet.show(supportFragmentManager, addBookSheet.tag)
                    false
                }
                else -> {
                    // Sadece destination varsa gitmeye çalış
                    if (navController.currentDestination?.id != item.itemId) {
                        navController.navigate(item.itemId)
                    }
                    true
                }
            }
        }
    }
}
