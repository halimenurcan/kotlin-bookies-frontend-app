package com.example.frontendbook.ui.homePage

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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

        binding.btnAddList.setOnClickListener {
            showAddListDialog()
        }

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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.lists_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_list -> {
                        showAddListDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


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
    private fun showAddListDialog() {
        val editText = EditText(requireContext())
        editText.hint = "List Name\n"

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Create New List\n")
            .setView(editText)
            .setPositiveButton("Create\n") { dialog, _ ->
                val listName = editText.text.toString().trim()
                if (listName.isNotEmpty()) {
                    val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    val userId = prefs.getLong("user_id", -1L)

                    viewModel.createList(userId, listName) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Liste oluşturuldu", Toast.LENGTH_SHORT).show()

                            // 👇 YENİ SATIR: Listeyi yeniden yükle
                            viewModel.loadUserLists(userId)

                        } else {
                            Toast.makeText(requireContext(), "Liste oluşturulamadı", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Liste adı boş olamaz", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }









}
