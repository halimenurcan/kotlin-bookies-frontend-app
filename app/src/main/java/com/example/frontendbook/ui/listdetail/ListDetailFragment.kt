package com.example.frontendbook.ui.listdetail

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.R
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ListFollowsRepository

class ListDetailFragment : Fragment() {

    private lateinit var viewModel: ListFollowViewModel
    private var listId: Long = -1L
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listId = arguments?.getLong("listId") ?: -1L

        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        userId = prefs.getLong("user_id", -1L)

        if (listId == -1L || userId == -1L) {
            Toast.makeText(requireContext(), "Hata: Liste veya kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val followButton = view.findViewById<Button>(R.id.followListButton)


        // ViewModel kur
        viewModel = ViewModelProvider(
            this,
            ListFollowViewModelFactory(requireContext())
        ).get(ListFollowViewModel::class.java)







        // Hata varsa göster
        viewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        // Butona tıklama işlemi
        followButton.setOnClickListener {
            viewModel.toggleFollow(userId, listId)
        }
    }
}
