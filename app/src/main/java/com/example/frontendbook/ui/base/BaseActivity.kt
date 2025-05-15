package com.example.frontendbook.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VM : ViewModel, S : BaseUiState, VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB
    protected abstract val viewModel: VM
    protected abstract val state: LiveData<S>

    abstract fun getViewBinding(): VB
    abstract fun handleState(state: S)
    open fun setupViews() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        setupViews()
        observeState()
    }

    private fun observeState() {
        state.observe(this) { state ->
            state?.let { handleState(it) }
        }
    }
}
