package com.example.frontendbook.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentProfileBinding
import com.example.frontendbook.domain.model.UserListType

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel
    private val TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, UserViewModelFactory(requireContext()))
            .get(UserViewModel::class.java)

        // SharedPrefs'ten userId
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        Log.d(TAG, "onViewCreated - userId from prefs = $userId")
        if (userId == -1L) {
            Toast.makeText(requireContext(), "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        // LiveData gözlemleri
        viewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d(TAG, "observe(user) -> $user")
            binding.usernameText.text = user.username
            if (!user.profileImageUrl.isNullOrBlank()) {
                Glide.with(this)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.avatar)
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.avatar)
            }
        }
        viewModel.followersCount.observe(viewLifecycleOwner) { count ->
            Log.d(TAG, "Followers count: $count")
            binding.btnFollowers.text = getString(R.string.followers_count, count)
        }
        viewModel.followingCount.observe(viewLifecycleOwner) { count ->
            Log.d(TAG, "Following count: $count")
            binding.btnFollowing.text = getString(R.string.following_count, count)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Log.e(TAG, "ViewModel error: $it")
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        // Animasyonlar
        val scaleAnim = ScaleAnimation(
            1f, 1.1f, 1f, 1.1f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 600
            interpolator = AccelerateDecelerateInterpolator()
        }
        binding.profileImage.startAnimation(scaleAnim)
        binding.usernameText.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.scale_glow)
        )

        // Veri yükle
        Log.d(TAG, "loadUser: $userId")
        viewModel.loadUser(userId)

        // Profil resmi ve ayarlar butonları
        binding.profileImage.setOnClickListener {
            ChooseProfileImageBottomSheetFragment { selectedResId ->
                binding.profileImage.setImageResource(selectedResId)
            }.show(parentFragmentManager, "ChooseProfile")
        }
        binding.btnSettings.setOnClickListener {
            SettingsBottomSheetFragment().show(parentFragmentManager, "Settings")
        }
        binding.btnRead.setOnClickListener {
            Log.d(TAG, "btnRead clicked - userId = $userId")
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("arg_title", "Read")
                    putString("arg_type",  "read")
                    putLong("arg_profile_user_id", userId)
                }
            )
        }
        binding.btnReadlist.setOnClickListener {
            Log.d(TAG, "btnReadlist clicked - userId = $userId")
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("arg_title", "Readlist")
                    putString("arg_type",  "readlist")
                    putLong("arg_profile_user_id", userId)
                }
            )
        }
        binding.btnLikes.setOnClickListener {
            Log.d(TAG, "btnLikes clicked - userId = $userId")
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("arg_title","Likes")
                    putString("arg_type",  "likes")
                    putLong("arg_profile_user_id", userId)
                }
            )
        }
        binding.btnFollowers.setOnClickListener {
            Log.d(TAG, "btnFollowers clicked - userId = $userId")
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("arg_user_list_type", UserListType.FOLLOWERS)
                    putLong("arg_profile_user_id", userId)
                }
            )
        }
        binding.btnFollowing.setOnClickListener {
            Log.d(TAG, "btnFollowing clicked - userId = $userId")
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("arg_user_list_type", UserListType.FOLLOWING)
                    putLong("arg_profile_user_id", userId)
                }
            )
        }
        // Diğer butonlar
        binding.btnLists.setOnClickListener {
            Log.d(TAG, "btnLists clicked - userId = $userId")
            findNavController().navigate(R.id.listsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
        }
}