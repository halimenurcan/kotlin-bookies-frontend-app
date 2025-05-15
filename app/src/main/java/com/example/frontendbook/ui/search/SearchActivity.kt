package com.example.frontendbook.ui.search

import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.example.frontendbook.databinding.SearchActivityBinding
import com.example.frontendbook.ui.base.BaseActivity

class SearchActivity : BaseActivity<SearchViewModel, SearchUiState, SearchActivityBinding>() {

    override val viewModel: SearchViewModel by viewModels()
    override val state: LiveData<SearchUiState> get() = viewModel.state

    override fun getViewBinding(): SearchActivityBinding {
        return SearchActivityBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        // Browse by filtre tıklamaları burada
    }

    override fun handleState(state: SearchUiState) {
        // Liste güncellemesi burada
    }
}
