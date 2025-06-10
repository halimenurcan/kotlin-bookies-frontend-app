package com.example.frontendbook.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.databinding.FragmentUserListBinding
import com.example.frontendbook.domain.model.UserListType
import com.example.frontendbook.domain.model.UserSimple
import com.example.frontendbook.ui.base.adapter.UserListAdapter

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private var listType: UserListType? = null

    companion object {
        private const val ARG_TYPE = "arg_user_list_type"

        fun newInstance(type: UserListType): UserListFragment {
            val fragment = UserListFragment()
            val args = Bundle().apply {
                putSerializable(ARG_TYPE, type)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listType = arguments?.getSerializable(ARG_TYPE) as? UserListType
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyUsers = List(10) {
            UserSimple(userId = "$it", username = "Kullanıcı ${it + 1}")
        }

        val title = when (listType) {
            UserListType.FOLLOWERS -> "Takipçiler"
            UserListType.FOLLOWING -> "Takip Edilenler"
            else -> "Kullanıcılar"
        }
        binding.headerTitle.text = title
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = UserListAdapter(dummyUsers) {
            // kullanıcıya tıklanınca yapılacaklar
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
