package com.example.frontendbook.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.frontendbook.R
import androidx.navigation.fragment.findNavController // en üste ekle
import com.example.frontendbook.ui.common.ThreeColumnFragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Butonlara tıklama olayları tanımlanıyor
        view.findViewById<View>(R.id.btnRead).setOnClickListener {
            openThreeColumnPage("Read", "read")
        }
        view.findViewById<View>(R.id.btnReadlist).setOnClickListener {
            openThreeColumnPage("Readlist", "readlist")
        }
        view.findViewById<View>(R.id.btnLists).setOnClickListener {
            openThreeColumnPage("Lists", "lists")
        }
        view.findViewById<View>(R.id.btnLikes).setOnClickListener {
            openThreeColumnPage("Likes", "likes")
        }

        return view
    }

    // ThreeColumnFragment'e geçiş yapan fonksiyon

    private fun openThreeColumnPage(title: String, type: String) {
        val bundle = Bundle().apply {
            putString("arg_title", title)
            putString("arg_type", type)
        }
        findNavController().navigate(R.id.threeColumnFragment, bundle)
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
