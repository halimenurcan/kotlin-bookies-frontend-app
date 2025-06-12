package com.example.frontendbook.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.databinding.FragmentSearchBinding
import com.example.frontendbook.ui.base.adapter.BookSearchAdapter

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: BookSearchAdapter
    private var currentFilterType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        adapter = BookSearchAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.visibility = View.GONE

        binding.genreButton.setOnClickListener { showInput("genre") }
        binding.languageButton.setOnClickListener { showInput("language") }

        binding.mostPopularButton.setOnClickListener {
            viewModel.searchBooks("popular")
            hideInput()
        }

        binding.highlyRatedButton.setOnClickListener {
            viewModel.searchBooks("rating")
            hideInput()
        }

        binding.browseSubmitButton.setOnClickListener {
            val value = binding.browseInput.text.toString()
            if (value.isNotBlank()) {
                viewModel.searchBooks("$currentFilterType:$value")
                hideInput()
            }
        }

        binding.aiSearchButton.setOnClickListener {
            val prompt = "bestselling books on personal development"
            viewModel.searchBooks(prompt) // gerçek API çağrısı
            hideInput()
        }

        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
            val isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH
            val isEnterKey = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN

            if (isSearchAction || isEnterKey) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchBooks(query)
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
                }
                true
            } else false
        }
    }

    private fun observeViewModel() {
        viewModel.books.observe(viewLifecycleOwner) { books ->
            if (books.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(books)
            } else {
                binding.recyclerView.visibility = View.GONE
                Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // TODO: ProgressBar görünürlüğü ayarlanabilir
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
