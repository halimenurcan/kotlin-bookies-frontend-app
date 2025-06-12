package com.example.frontendbook.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frontendbook.R
import com.example.frontendbook.domain.model.UserListType

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Username TextView'e SharedPreferences'ten alınan kullanıcı adını set et
        val sharedPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPrefs.getString("user_username", "default")
        view.findViewById<TextView>(R.id.usernameText).text = username

        // Read
        view.findViewById<View>(R.id.btnRead).setOnClickListener {
            val bundle = Bundle().apply {
                putString("arg_title", "Read")
                putString("arg_type", "read")
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }

        // Readlist
        view.findViewById<View>(R.id.btnReadlist).setOnClickListener {
            val bundle = Bundle().apply {
                putString("arg_title", "Readlist")
                putString("arg_type", "readlist")
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }

        // Lists
        view.findViewById<View>(R.id.btnLists).setOnClickListener {
            findNavController().navigate(R.id.listsFragment)
        }

        // Likes
        view.findViewById<View>(R.id.btnLikes).setOnClickListener {
            val bundle = Bundle().apply {
                putString("arg_title", "Likes")
                putString("arg_type", "likes")
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }

        // Followers
        view.findViewById<View>(R.id.btnFollowers).setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWERS)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }

        // Following
        view.findViewById<View>(R.id.btnFollowing).setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWING)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }

        // Settings BottomSheet
        view.findViewById<View>(R.id.btnSettings).setOnClickListener {
            SettingsBottomSheetFragment().show(parentFragmentManager, "SettingsBottomSheet")
        }

        return view
    }
}
