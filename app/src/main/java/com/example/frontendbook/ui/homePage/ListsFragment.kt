package com.example.frontendbook.ui.homePage

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentListsBinding
import com.example.frontendbook.domain.model.UserListType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ListsFragment : Fragment() {

    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!

    private val args: ListsFragmentArgs by navArgs()
    private val profileUserId: Long get() = args.profileUserId

    private lateinit var viewModel: ListsViewModel
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add-list butonunu sadece kendi profilinizde göster (isteğe bağlı)
        if (profileUserId == getCurrentUserId()) {
            binding.btnAddList.visibility = View.VISIBLE
            binding.btnAddList.setOnClickListener { showAddListDialog() }
        } else {
            binding.btnAddList.visibility = View.GONE
        }

        // RecyclerView ve Adapter ayarı
        adapter = ListAdapter(emptyList()) { list ->
            val bundle = Bundle().apply { putLong("listId", list.id) }
            findNavController().navigate(
                R.id.action_listsFragment_to_listDetailFragment,
                bundle
            )
        }
        binding.listsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.listsRecyclerView.adapter = adapter

        // ViewModel oluştur
        viewModel = viewModels<ListsViewModel> { ListsViewModelFactory(requireContext()) }.value

        // Options menu (isteğe bağlı: tekrar add dialog)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                inflater.inflate(R.menu.lists_menu, menu)
            }
            override fun onMenuItemSelected(item: MenuItem) =
                if (item.itemId == R.id.action_add_list) {
                    showAddListDialog()
                    true
                } else false
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // userId parametresiyle listeleri yükle
        val userIdToLoad = if (profileUserId != -1L) profileUserId else getCurrentUserId()
        if (userIdToLoad != -1L) {
            viewModel.loadUserLists(userIdToLoad)
        } else {
            Toast.makeText(requireContext(), "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
        }

        // LiveData gözlemleri
        viewModel.lists.observe(viewLifecycleOwner) { lists ->
            adapter.submitList(lists)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }
    }

    private fun getCurrentUserId(): Long =
        requireContext()
            .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getLong("user_id", -1L)

    private fun showAddListDialog() {
        val context = requireContext()
        val inputLayout = TextInputLayout(context).apply {
            hint = "List Name"
            setPadding(50, 0, 50, 0)
        }
        val editText = TextInputEditText(context).apply { isSingleLine = true }
        inputLayout.addView(editText)

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Create New List")
            .setView(inputLayout)
            .setPositiveButton("Create", null)
            .setNegativeButton("Cancel", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.buttonSecondary))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.button_textPrimary))

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val listName = editText.text.toString().trim()
            if (listName.isNotEmpty()) {
                val userId = getCurrentUserId()
                viewModel.createList(userId, listName) { success ->
                    if (success) {
                        Toast.makeText(context, "Liste oluşturuldu", Toast.LENGTH_SHORT).show()
                        viewModel.loadUserLists(userId)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "Liste oluşturulamadı", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                inputLayout.error = "Liste adı boş olamaz"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
