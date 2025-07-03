package com.example.frontendbook.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentOtherUserProfileBinding
import com.example.frontendbook.domain.model.UserListType

class OtherUserProfileFragment : Fragment() {

    private var _binding: FragmentOtherUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var followerViewModel: FollowerViewModel

    private var currentUserId: Long = -1L
    private var targetUserId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Aktif kullanıcıyı al
        val prefs = requireContext()
            .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        currentUserId = prefs.getLong("user_id", -1L)
        if (currentUserId == -1L) {
            Toast.makeText(requireContext(), "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        // 2) Görüntülenecek kullanıcıyı al
        targetUserId = arguments?.getLong("user_id") ?: -1L
        if (targetUserId == -1L) {
            Toast.makeText(requireContext(), "Hedef kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        // 3) ViewModel’leri hazırla
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(requireContext())
        ).get(UserViewModel::class.java)

        followerViewModel = ViewModelProvider(
            this,
            FollowerViewModelFactory(requireContext())
        ).get(FollowerViewModel::class.java)

        // 4) Kullanıcı detaylarını gözle
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.otherUsernameText.text = user.username
            user.profileImageUrl?.let {
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.avatar)
                    .circleCrop()
                    .into(binding.otherProfileImage)
            }
        }
        userViewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }

        // 5) Takip durumu gözle
        followerViewModel.isFollowing.observe(viewLifecycleOwner) { following ->
            binding.btnFollowAction.text =
                if (following) getString(R.string.unfollow) else getString(R.string.follow)
        }
        followerViewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }

        // 6) İlk yükleme
        userViewModel.loadUser(targetUserId)
        followerViewModel.loadFollowStatus(currentUserId, targetUserId)

        // 7) Takip et / bırak butonu
        binding.btnFollowAction.setOnClickListener {
            followerViewModel.toggleFollow(currentUserId, targetUserId)
        }

        // 8) Takipçiler ekranına geç
        binding.btnOtherFollowers.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWERS)
                putLong("arg_profile_user_id", targetUserId)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }

        // 9) Takip Ettiklerim ekranına geç
        binding.btnOtherFollowing.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWING)
                putLong("arg_profile_user_id", targetUserId)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }

        // 10) Alt menü navigasyonları
        binding.btnOtherRead.setOnClickListener {
            navigateToThreeColumn("Read", "read")
        }
        binding.btnOtherReadlist.setOnClickListener {
            navigateToThreeColumn("Readlist", "readlist")
        }
        binding.btnOtherLists.setOnClickListener {
            // istersen userId=targetUserId ile kendi listelerini
            findNavController().navigate(R.id.listsFragment)
        }
        binding.btnOtherLikes.setOnClickListener {
            navigateToThreeColumn("Likes", "likes")
        }
    }

    private fun navigateToThreeColumn(title: String, type: String) {
        val bundle = Bundle().apply {
            putString("arg_title", title)
            putString("arg_type", type)
            putLong("user_id", targetUserId)
        }
        findNavController().navigate(R.id.threeColumnFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
