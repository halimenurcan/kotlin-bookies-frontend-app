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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.databinding.FragmentSearchBinding
import com.example.frontendbook.ui.base.adapter.CombinedSearchAdapter

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // artık direkt Long, default -1L
    private val currentUserId: Long by lazy {
        requireContext()
            .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getLong("user_id", -1L)
    }

    private val factory by lazy {
        SearchViewModelFactory(
            RetrofitClient.searchApiService(requireContext())
        )
    }
    private val viewModel: SearchViewModel by viewModels { factory }
    private lateinit var adapter: CombinedSearchAdapter

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
                val action = SearchFragmentDirections
                    .actionSearchFragmentToBookInfoPageFragment(book)
                findNavController().navigate(action)
            },
            onUserClick = { user ->
                val clickedUserId = user.id.toLongOrNull() ?: -1L

                if (clickedUserId == currentUserId) {
                    // Kendi profilinize
                    val action = SearchFragmentDirections
                        .actionSearchFragmentToProfileFragment()
                    findNavController().navigate(action)
                } else {
                    // Başka bir kullanıcının profiline — burada SafeArgs Directions kullanıyoruz!
                    val action = SearchFragmentDirections
                        .actionSearchFragmentToOtherUserProfileFragment(clickedUserId)
                    findNavController().navigate(action)
                }
            })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
            val isSearch = actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)

            if (isSearch) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchBooksAndUsers(query)
                    binding.browseContainer.visibility = View.GONE
                    binding.backButton.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.VISIBLE
                    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                            as InputMethodManager)
                        .hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
                }
                true
            } else false
        }

        binding.backButton.setOnClickListener {
            binding.browseContainer.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.backButton.visibility = View.GONE
            binding.searchInput.text?.clear()
        }
    }

    private fun observeViewModel() {
        viewModel.combinedResults.observe(viewLifecycleOwner) { results ->
            if (results.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
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
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
