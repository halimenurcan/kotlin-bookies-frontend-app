package com.example.frontendbook.ui.search

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckedTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.databinding.FragmentSearchBinding
import com.example.frontendbook.ui.base.adapter.CombinedSearchAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val currentUserId: Long by lazy {
        requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getLong("user_id", -1L)
    }

    private val factory by lazy {
        SearchViewModelFactory(RetrofitClient.searchApiService(requireContext()))
    }

    private val viewModel: SearchViewModel by viewModels { factory }
    private lateinit var adapter: CombinedSearchAdapter

    private val genreOptions = mutableListOf<String>()
    private val languageOptions = mutableListOf<String>()
    private val selectedGenres = mutableSetOf<String>()
    private val selectedLanguages = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.loadFilterOptions()
    }

    private fun setupViews() {
        adapter = CombinedSearchAdapter(
            onBookClick = { book ->
                val action = SearchFragmentDirections
                    .actionSearchFragmentToBookInfoPageFragment(book)
                findNavController().navigate(action)
            },
            onUserClick = { user ->
                val action = if ((user.id ?: -1L) == currentUserId)
                    SearchFragmentDirections.actionSearchFragmentToProfileFragment()
                else
                    SearchFragmentDirections.actionSearchFragmentToOtherUserProfileFragment(user.id ?: -1L)
                findNavController().navigate(action)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
            val isSearch = actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)

            if (isSearch) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchBooksAndUsers(
                        query,
                        genres = selectedGenres.toList(),
                        languages = selectedLanguages.toList()
                    )
                    binding.browseContainer.visibility = View.GONE
                    binding.backButton.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.VISIBLE
                    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
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

        binding.mostPopularButton.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToThreeColumnFragment(
                title = "Most Popular", type = "popular", listId = 0, userId = 0
            )
            findNavController().navigate(action)
        }

        binding.highlyRatedButton.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToThreeColumnFragment(
                title = "Highly Rated", type = "rated", listId = 0, userId = 0
            )
            findNavController().navigate(action)
        }

        binding.genreButton.setOnClickListener {
            showMultiSelectDialog("Select Genres", genreOptions, selectedGenres)
        }
        binding.aiSearchButton.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1L)

            if (userId == -1L) {
                Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val action = SearchFragmentDirections.actionSearchFragmentToThreeColumnFragment(
                title = "AI Recommendations",
                type = "ai", // özel tür olarak "ai" gönderiyoruz
                listId = 0L,
                userId = userId
            )
            findNavController().navigate(action)
        }

        binding.languageButton.setOnClickListener {
            showMultiSelectDialog("Select Languages", languageOptions, selectedLanguages)
        }



        val chipStyleBackground = MaterialShapeDrawable(
            ShapeAppearanceModel().withCornerSize(16f)
        ).apply {
            fillColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            strokeColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.textSecondary))
            strokeWidth = 1f
        }


    }

    private fun showMultiSelectDialog(
        title: String,
        options: List<String>,
        selectedSet: MutableSet<String>
    ) {
        if (options.isEmpty() || options.any { it.isBlank() }) {
            Toast.makeText(requireContext(), "$title  data could not be loaded", Toast.LENGTH_SHORT).show()
            return
        }

        val checkedItems = options.map { it in selectedSet }.toBooleanArray()

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMultiChoiceItems(options.toTypedArray(), checkedItems) { _, which, isChecked ->
                val item = options[which]
                if (isChecked) selectedSet.add(item) else selectedSet.remove(item)
            }
            .setPositiveButton("OK", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                setOnClickListener {
                    updateFilterChips()
                    dialog.dismiss()
                }
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            dialog.listView.viewTreeObserver.addOnGlobalLayoutListener {
                for (i in 0 until dialog.listView.childCount) {
                    val view = dialog.listView.getChildAt(i)
                    if (view is CheckedTextView) {
                        view.compoundDrawableTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.buttonSecondary)
                    }
                }
            }

            val backgroundColor = ContextCompat.getColor(requireContext(), R.color.backgroundPrimary)
            val shape = MaterialShapeDrawable().apply {
                fillColor = ColorStateList.valueOf(backgroundColor)
                shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(8f)
            }
            dialog.window?.setBackgroundDrawable(shape)
        }

        dialog.show()
    }

    private fun updateFilterChips() {
        binding.chipGroupFilters.removeAllViews()

        val allFilters = selectedGenres.map { "Genre: $it" } + selectedLanguages.map { "Language: $it" }

        if (allFilters.isEmpty()) {
            binding.chipGroupFilters.visibility = View.GONE
            return
        }

        binding.chipGroupFilters.visibility = View.VISIBLE

        allFilters.forEach { label ->
            val chip = Chip(requireContext()).apply {
                text = label
                isCloseIconVisible = true
                chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.white)
                chipStrokeColor = ContextCompat.getColorStateList(context, R.color.textSecondary)
                chipStrokeWidth = 1f
                setTextColor(ContextCompat.getColor(context, R.color.black))
                closeIconTint = ContextCompat.getColorStateList(context, R.color.textSecondary)
                setOnCloseIconClickListener { removeFilter(label) }
            }
            binding.chipGroupFilters.addView(chip)
        }
    }

    private fun removeFilter(label: String) {
        when {
            label.startsWith("Genre: ") -> selectedGenres.remove(label.removePrefix("Genre: "))
            label.startsWith("Language: ") -> selectedLanguages.remove(label.removePrefix("Language: "))
        }
        updateFilterChips()
    }

    private fun observeViewModel() {
        viewModel.combinedResults.observe(viewLifecycleOwner) { results ->
            if (results.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.backButton.visibility = View.VISIBLE
                adapter.submitList(results)
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
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.genres.observe(viewLifecycleOwner) { genres ->
            genreOptions.clear()
            genreOptions.addAll(genres.filterNotNull())
        }

        viewModel.languages.observe(viewLifecycleOwner) { langs ->
            languageOptions.clear()
            languageOptions.addAll(langs.filterNotNull())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
