package com.example.frontendbook.ui.search
import android.app.AlertDialog
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
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.chip.ChipGroup



class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: CombinedSearchAdapter

    private val genreOptions = listOf("Fantasy", "Mystery", "Science Fiction")
    private val languageOptions = listOf("English", "Turkish", "German")
    private val selectedGenres = mutableSetOf<String>()
    private val selectedLanguages = mutableSetOf<String>()


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
        binding.genreButton.setOnClickListener {
            showMultiSelectDialog(
                title = "Select Genres",
                options = genreOptions,
                selectedSet = selectedGenres
            )
        }

        binding.languageButton.setOnClickListener {
            showMultiSelectDialog(
                title = "Select Languages",
                options = languageOptions,
                selectedSet = selectedLanguages
            )
        }

        binding.applyFiltersButton.setOnClickListener {
            // Filtrelere göre listeleme işlemi burada yapılacak
            Toast.makeText(requireContext(), "Filtreler uygulandı", Toast.LENGTH_SHORT).show()

            // Örnek: viewModel.filterBooks(selectedGenres, selectedLanguages)
        }

    }
    private fun showMultiSelectDialog(
        title: String,
        options: List<String>,
        selectedSet: MutableSet<String>
    ) {
        val checkedItems = options.map { it in selectedSet }.toBooleanArray()

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme)
            .setTitle(title)
            .setMultiChoiceItems(options.toTypedArray(), checkedItems) { _, which, isChecked ->
                val item = options[which]
                if (isChecked) selectedSet.add(item) else selectedSet.remove(item)
            }
            .setPositiveButton("OK", null)  // Henüz listener tanımlamıyoruz
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                updateFilterChips()
                dialog.dismiss() // 🔥 BURASI DIALOG’U KAPATIR
            }
        }

        dialog.show()
    }


    private fun updateFilterChips() {
        binding.chipGroupFilters.removeAllViews()

        val allFilters = selectedGenres.map { "Genre: $it" } + selectedLanguages.map { "Language: $it" }

        if (allFilters.isEmpty()) {
            binding.chipGroupFilters.visibility = View.GONE
            binding.applyFiltersButton.visibility = View.GONE
            return
        }

        binding.chipGroupFilters.visibility = View.VISIBLE
        binding.applyFiltersButton.visibility = View.VISIBLE

        allFilters.forEach { label ->
            val chip = Chip(requireContext()).apply {
                text = label
                isCloseIconVisible = true
                setOnCloseIconClickListener { removeFilter(label) }
            }
            binding.chipGroupFilters.addView(chip)
        }
    }
    private fun removeFilter(label: String) {
        when {
            label.startsWith("Genre: ") -> {
                val value = label.removePrefix("Genre: ")
                selectedGenres.remove(value)
            }
            label.startsWith("Language: ") -> {
                val value = label.removePrefix("Language: ")
                selectedLanguages.remove(value)
            }
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





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
