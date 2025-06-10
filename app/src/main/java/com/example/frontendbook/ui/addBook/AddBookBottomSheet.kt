package com.example.frontendbook.ui.addBook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.widget.Toast
import com.example.frontendbook.databinding.BottomSheetAddBookBinding
import com.example.frontendbook.ui.base.adapter.BookSearchAdapter
import com.example.frontendbook.ui.search.SearchViewModel
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager

import android.view.inputmethod.EditorInfo
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.content.Context



class AddBookBottomSheet : BottomSheetDialogFragment() {

        private val viewModel: SearchViewModel by activityViewModels()

        private lateinit var adapter: BookSearchAdapter
        private var _binding: BottomSheetAddBookBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = BottomSheetAddBookBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // 2) RecyclerView + Adapter
            adapter = BookSearchAdapter()
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@AddBookBottomSheet.adapter
                visibility = View.GONE
            }

            // Cancel button functionality
            binding.cancelButton.setOnClickListener {
                dismiss()
            }

            // 3) Klavyedeki “Search”/Enter tuşuna basılınca arama
            binding.searchInput.setOnEditorActionListener { v, actionId, event ->
                val isSearch = actionId == EditorInfo.IME_ACTION_SEARCH
                val isEnter = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
                if (isSearch || isEnter) {
                    val q = binding.searchInput.text.toString().trim()
                    if (q.isNotEmpty()) {
                        viewModel.searchBooks(q)
                        // klavyeyi kapatmak isterseniz:
                        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                                as InputMethodManager)
                            .hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
                    }
                    true
                } else false
            }
            // 4) Sonuçları gözlemleyip adapter’a verelim
            viewModel.state.observe(viewLifecycleOwner) { state ->
                if (state.books.isNotEmpty()) {
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.submitList(state.books)
                } else {
                    binding.recyclerView.visibility = View.GONE
                    if (state.isEmptyResult) {
                        Toast.makeText(requireContext(), "No books found", Toast.LENGTH_SHORT).show()
                    }
                }
                state.errorMessage?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
        //tiklandiginda ekranin %80ini kaplamasi icin boyutsal
        override fun onStart() {
            super.onStart()
            dialog?.let {
                val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.layoutParams?.height = (resources.displayMetrics.heightPixels * 0.8).toInt()
            }
        }

    }

