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

        // ViewModel oluşturma
        viewModel = ViewModelProvider(this, UserViewModelFactory(requireContext()))
            .get(UserViewModel::class.java)

        // SharedPrefs'ten userId
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        if (userId == -1L) {
            Toast.makeText(requireContext(), "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        // LiveData gözlemleri
        viewModel.user.observe(viewLifecycleOwner) { user ->
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
            binding.btnFollowers.text = getString(R.string.followers_count, count)
        }
        viewModel.followingCount.observe(viewLifecycleOwner) { count ->
            binding.btnFollowing.text = getString(R.string.following_count, count)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
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
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("arg_title", "Read") //
                    putString("arg_type",  "read")
                }
            )
        }
        binding.btnReadlist.setOnClickListener {
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("arg_title", "Readlist") // "Readlist"
                    putString("arg_type",  "readlist")
                }
            )
        }
        binding.btnLikes.setOnClickListener {
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("arg_title","Likes") // "Likes"
                    putString("arg_type",  "likes")
                }
            )
        }
        // Diğer butonlar
        binding.btnLists.setOnClickListener {
            findNavController().navigate(R.id.listsFragment)
        }
        binding.btnFollowers.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("arg_user_list_type", UserListType.FOLLOWERS)
                }
            )
        }
        binding.btnFollowing.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("arg_user_list_type", UserListType.FOLLOWING)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
