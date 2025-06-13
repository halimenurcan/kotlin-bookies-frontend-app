package com.example.frontendbook.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frontendbook.R
import com.example.frontendbook.domain.model.UserListType

class OtherUserProfileFragment : Fragment() {

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // userId parametresi fragment'a aktarılırken alınır
        userId = arguments?.getString("user_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_other_user_profile, container, false)

        val usernameText = view.findViewById<TextView>(R.id.otherUsernameText)
        val profileImage = view.findViewById<ImageView>(R.id.otherProfileImage)
        val followersButton = view.findViewById<Button>(R.id.btnOtherFollowers)
        val followingButton = view.findViewById<Button>(R.id.btnOtherFollowing)

        // Dummy veriler, backend'e bağlandığında buraya API çağrısı gelecek
        usernameText.text = "OtherUserName"
        profileImage.setImageResource(R.drawable.avatar) // ya da Glide ile yüklenebilir

        // Buton click listener'ları
        view.findViewById<View>(R.id.btnOtherRead).setOnClickListener {
            navigateToThreeColumn("Read", "read")
        }
        view.findViewById<View>(R.id.btnOtherReadlist).setOnClickListener {
            navigateToThreeColumn("Readlist", "readlist")
        }
        view.findViewById<View>(R.id.btnOtherLists).setOnClickListener {
            findNavController().navigate(R.id.listsFragment) // veya userId ile özel listeler
        }
        view.findViewById<View>(R.id.btnOtherLikes).setOnClickListener {
            navigateToThreeColumn("Likes", "likes")
        }

        followersButton.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWERS)
                putString("user_id", userId)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }

        followingButton.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWING)
                putString("user_id", userId)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }

        return view
    }

    private fun navigateToThreeColumn(title: String, type: String) {
        val bundle = Bundle().apply {
            putString("arg_title", title)
            putString("arg_type", type)
            putString("user_id", userId)
        }
        findNavController().navigate(R.id.threeColumnFragment, bundle)
    }
}