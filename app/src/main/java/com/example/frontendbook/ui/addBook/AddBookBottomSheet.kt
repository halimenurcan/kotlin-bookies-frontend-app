package com.example.frontendbook.ui.addBook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.widget.Toast
import com.example.frontendbook.databinding.BottomSheetAddBookBinding

//bottomSheet bu addbokk.kt den her sey degisiyo diye fragment kaldi adi

    class AddBookBottomSheet : BottomSheetDialogFragment() {

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

            // Cancel button functionality
            binding.cancelButton.setOnClickListener {
                dismiss()
            }

            // Dummy add action
            binding.searchInput.setOnEditorActionListener { _, _, _ ->
                Toast.makeText(requireContext(), "Book added!", Toast.LENGTH_SHORT).show()
                dismiss()
                true
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

