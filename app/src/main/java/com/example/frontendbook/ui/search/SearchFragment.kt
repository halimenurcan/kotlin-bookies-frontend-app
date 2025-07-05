package com.example.frontendbook.ui.search
import com.example.frontendbook.ui.search.SearchViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentSearchBinding
import com.example.frontendbook.ui.base.adapter.CombinedSearchAdapter
import com.example.frontendbook.ui.bookInfoPage.BookInfoPageFragment
import androidx.navigation.fragment.findNavController
import com.example.frontendbook.ui.search.SearchFragmentDirections


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: CombinedSearchAdapter
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

        adapter = CombinedSearchAdapter(
            onBookClick = { book ->
                val action = SearchFragmentDirections.actionSearchFragmentToBookInfoPageFragment(book)
                findNavController().navigate(action)
            },
            onUserClick = { user ->
                Toast.makeText(requireContext(), "${user.username} profiline gidilecek", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.genreButton.setOnClickListener { showInput("genre") }
        binding.languageButton.setOnClickListener { showInput("language") }

        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
            val isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH
            val isEnterKey = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN

            if (isSearchAction || isEnterKey) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchBooksAndUsers(query)

                    binding.browseContainer.visibility = View.GONE
                    binding.backButton.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.VISIBLE

                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
                }
                true
            } else false
        }

        binding.backButton.setOnClickListener {
            binding.browseContainer.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.backButton.visibility = View.GONE
            binding.searchInput.text.clear()
        }
    }

    private fun observeViewModel() {
        viewModel.combinedResults.observe(viewLifecycleOwner) { results ->


            if (results.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.backButton.visibility = View.VISIBLE
                adapter.submitList(results)
            } else {
                binding.recyclerView.visibility = View.GONE
                Toast.makeText(requireContext(), "Sonuç bulunamadı", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
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
