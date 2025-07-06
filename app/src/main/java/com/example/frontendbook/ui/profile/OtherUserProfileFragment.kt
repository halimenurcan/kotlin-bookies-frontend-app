package com.example.frontendbook.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentOtherUserProfileBinding
import com.example.frontendbook.domain.model.UserListType

class OtherUserProfileFragment : Fragment() {

    private var _binding: FragmentOtherUserProfileBinding? = null
    private val binding get() = _binding!!

    // SafeArgs ile gelen userId
    private val args: OtherUserProfileFragmentArgs by navArgs()
    private val targetUserId: Long get() = args.userId

    // Aktif kullanıcının ID'si
    private val currentUserId: Long by lazy {
        requireContext()
            .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getLong("user_id", -1L)
    }

    // ViewModel'ler
    private val userViewModel: UserViewModel by viewModels { UserViewModelFactory(requireContext()) }
    private val followerViewModel: FollowerViewModel by viewModels { FollowerViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ID kontrolleri
        if (currentUserId == -1L) {
            Toast.makeText(requireContext(), "Aktif kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }
        if (targetUserId == -1L) {
            Toast.makeText(requireContext(), "Hedef kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        // Kullanıcı detaylarını gözle
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.otherUsernameText.text = user.username
            if (!user.profileImageUrl.isNullOrBlank()) {
                Glide.with(this)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.avatar)
                    .circleCrop()
                    .into(binding.otherProfileImage)
            } else {
                binding.otherProfileImage.setImageResource(R.drawable.avatar)
            }
        }
        userViewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }

        // Takip durumunu gözle
        followerViewModel.isFollowing.observe(viewLifecycleOwner) { isFollowing ->
            binding.btnFollowAction.text =
                if (isFollowing) getString(R.string.unfollow) else getString(R.string.follow)
        }
        followerViewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }

        // İlk veri yükleme
        userViewModel.loadUser(targetUserId)
        followerViewModel.loadFollowStatus(currentUserId, targetUserId)

        // Takip butonu
        binding.btnFollowAction.setOnClickListener {
            followerViewModel.toggleFollow(currentUserId, targetUserId)
        }

        // Alt menü navigasyonları
        binding.btnOtherFollowers.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("arg_user_list_type", UserListType.FOLLOWERS)
                    putLong("arg_profile_user_id", targetUserId)
                }
            )
        }
        binding.btnOtherFollowing.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("arg_user_list_type", UserListType.FOLLOWING)
                    putLong("arg_profile_user_id", targetUserId)
                }
            )
        }
        binding.btnOtherRead.setOnClickListener {
            navigateToThreeColumn("Read", "read")
        }
        binding.btnOtherReadlist.setOnClickListener {
            navigateToThreeColumn("Readlist", "readlist")
        }
        binding.btnOtherLists.setOnClickListener {
            findNavController().navigate(R.id.listsFragment)
        }
        binding.btnOtherLikes.setOnClickListener {
            navigateToThreeColumn("Likes", "likes")
        }
    }

    private fun navigateToThreeColumn(title: String, type: String) {
        findNavController().navigate(
            R.id.threeColumnFragment,
            Bundle().apply {
                putString("arg_title", title)
                putString("arg_type", type)
                putLong("user_id", targetUserId)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
