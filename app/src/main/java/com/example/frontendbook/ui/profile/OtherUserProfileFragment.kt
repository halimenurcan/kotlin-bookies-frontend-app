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
import com.example.frontendbook.ui.base.threecolumn.ThreeColumnFragment

class OtherUserProfileFragment : Fragment() {

    private var _binding: FragmentOtherUserProfileBinding? = null
    private val binding get() = _binding!!

    private val args: OtherUserProfileFragmentArgs by navArgs()
    private val targetUserId: Long get() = args.userId

    // Şu anki kullanıcıyı çekiyoruz (giriş yapan kullanıcı)
    private val currentUserId: Long by lazy {
        requireContext()
            .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getLong("user_id", -1L)
    }

    private val userViewModel: UserViewModel by viewModels { UserViewModelFactory(requireContext()) }
    private val followerViewModel: FollowerViewModel by viewModels { FollowerViewModelFactory(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currentUserId == -1L || targetUserId == -1L) {
            Toast.makeText(requireContext(), "Kullanıcı bilgisi eksik", Toast.LENGTH_SHORT).show()
            return
        }

        // User bilgi gözlemleri
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
        userViewModel.error.observe(viewLifecycleOwner) { it?.let { m -> Toast.makeText(requireContext(), m, Toast.LENGTH_LONG).show() } }
        userViewModel.followersCount.observe(viewLifecycleOwner) { count ->
            binding.btnOtherFollowers.text = getString(R.string.followers_count, count)
        }
        userViewModel.followingCount.observe(viewLifecycleOwner) { count ->
            binding.btnOtherFollowing.text = getString(R.string.following_count, count)
        }

        // Takip durumu gözlemi
        followerViewModel.isFollowing.observe(viewLifecycleOwner) { following ->
            binding.btnFollowAction.text = if (following) getString(R.string.unfollow) else getString(R.string.follow)
        }
        followerViewModel.error.observe(viewLifecycleOwner) { it?.let { m -> Toast.makeText(requireContext(), m, Toast.LENGTH_LONG).show() } }
        followerViewModel.loadIsFollowing(targetUserId, currentUserId)

        // Kullanıcı ve takip durumunu yükle
        userViewModel.loadUser(targetUserId)
        followerViewModel.loadFollowStatus(currentUserId, targetUserId)

        // Takip/Çık takip butonu
        binding.btnFollowAction.setOnClickListener {
            followerViewModel.toggleFollow(currentUserId, targetUserId)
        }

        // Takipçi listesi
        binding.btnOtherFollowers.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("userListType", UserListType.FOLLOWERS)
                    putLong("profileUserId", targetUserId)
                }
            )
        }

        // Takip edilenler listesi
        binding.btnOtherFollowing.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("userListType", UserListType.FOLLOWING)
                    putLong("profileUserId", targetUserId)

                }
            )
        }

        // Okunan kitaplar
        binding.btnOtherRead.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ThreeColumnFragment.ARG_TITLE, "Read")
                putString(ThreeColumnFragment.ARG_TYPE, "read")
                putLong(ThreeColumnFragment.ARG_USER_ID, targetUserId)
                putLong("listId", 0L)
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
        // Okunacak kitaplar
        binding.btnOtherReadlist.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ThreeColumnFragment.ARG_TITLE, "Readlist")
                putString(ThreeColumnFragment.ARG_TYPE, "readlist")
                putLong(ThreeColumnFragment.ARG_USER_ID, targetUserId)
                putLong("listId", 0L)
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
        // Diğer kullanıcının listeleri (örnek, arg_title kullanıyorsan navigation'da kontrol et)
        binding.btnOtherLists.setOnClickListener {
            findNavController().navigate(
                R.id.listsFragment,
                Bundle().apply {
                    putString("arg_title", "List")             // <-- string
                    putString("arg_type", "list")              // <-- string
                    putLong("arg_list_id", 0L)                 // <-- long
                    putLong("arg_user_id", targetUserId)       // <-- long (diğer user'ın id'si)
                    putLong("profileUserId", targetUserId)     // <-- long
                }
            )
        }

        // Diğer kullanıcının beğendiği kitaplar
        binding.btnOtherLikes.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ThreeColumnFragment.ARG_TITLE, "Likes")
                putString(ThreeColumnFragment.ARG_TYPE, "likes")
                putLong(ThreeColumnFragment.ARG_USER_ID, targetUserId)
                putLong("listId", 0L)
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
