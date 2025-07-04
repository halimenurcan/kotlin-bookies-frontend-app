package com.example.frontendbook.ui.homePage

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentListsBinding

class ListsFragment : Fragment() {

    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ListsViewModel
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) RecyclerView ve Adapter ayarı
        adapter = ListAdapter(emptyList()) { list ->
            // Listeye tıklanınca detay ekranına git
            val bundle = Bundle().apply { putLong("listId", list.id) }
            findNavController().navigate(R.id.action_listsFragment_to_listDetailFragment, bundle)
        }
        binding.listsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.listsRecyclerView.adapter = adapter

        // 2) ViewModel oluştur
        viewModel = ViewModelProvider(
            this,
            ListsViewModelFactory(requireContext())
        ).get(ListsViewModel::class.java)

        // 3) userId al ve listeleri yükle
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        if (userId != -1L) {
            viewModel.loadUserLists(userId)
        } else {
            Toast.makeText(requireContext(), "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
        }

        // 4) LiveData gözlemleri
        viewModel.lists.observe(viewLifecycleOwner) { lists ->
            adapter.submitList(lists)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
