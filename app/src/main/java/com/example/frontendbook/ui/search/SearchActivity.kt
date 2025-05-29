package com.example.frontendbook.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.example.frontendbook.databinding.SearchActivityBinding
import com.example.frontendbook.ui.base.BaseActivity

class SearchActivity : BaseActivity<SearchViewModel, SearchUiState, SearchActivityBinding>() {

    override val viewModel: SearchViewModel by viewModels()
    override val state: LiveData<SearchUiState> get() = viewModel.state

    private lateinit var adapter: BookAdapter
    private var currentFilterType: String = ""

    override fun getViewBinding(): SearchActivityBinding {
        return SearchActivityBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        adapter = BookAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.visibility = View.GONE

        // Browse buttons
        binding.genreButton.setOnClickListener { showInput("genre") }
        binding.authorButton.setOnClickListener { showInput("author") }
        binding.countryButton.setOnClickListener { showInput("country") }
        binding.languageButton.setOnClickListener { showInput("language") }
        binding.releaseDateButton.setOnClickListener { showInput("year") }
        binding.mostPopularButton.setOnClickListener {
            viewModel.searchBooks("popular")
            hideInput()
        }
        binding.highlyRatedButton.setOnClickListener {
            viewModel.searchBooks("rating")
            hideInput()
        }

        // Browse input submit
        binding.browseSubmitButton.setOnClickListener {
            val value = binding.browseInput.text.toString()
            if (value.isNotBlank()) {
                viewModel.searchBooks("$currentFilterType:$value")
                hideInput()
            }
        }

        // AI Search
        binding.aiSearchButton.setOnClickListener {
            viewModel.getAiRecommendedBooks()
            hideInput()
        }
    }

    private fun showInput(type: String) {
        currentFilterType = type
        binding.browseInput.setText("")
        binding.browseInput.visibility = View.VISIBLE
        binding.browseSubmitButton.visibility = View.VISIBLE
    }

    private fun hideInput() {
        binding.browseInput.visibility = View.GONE
        binding.browseSubmitButton.visibility = View.GONE
    }

    override fun handleState(state: SearchUiState) {
        if (state.isLoading) {
            // TODO: Show loading spinner
        } else {
            // Kitaplar geldiyse listeyi göster
            if (state.books.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(state.books)
            }

            if (state.isEmptyResult) {
                binding.recyclerView.visibility = View.GONE
                Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show()
            }

            state.successMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }

            state.errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}
