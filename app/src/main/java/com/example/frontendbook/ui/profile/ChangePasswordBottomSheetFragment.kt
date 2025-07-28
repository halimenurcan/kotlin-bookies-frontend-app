package com.example.frontendbook.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.frontendbook.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangePasswordBottomSheetFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_change_password_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val oldPasswordInput = view.findViewById<EditText>(R.id.oldPasswordInput)
        val newPasswordInput = view.findViewById<EditText>(R.id.newPasswordInput)
        val btnUpdate = view.findViewById<Button>(R.id.btnUpdatePassword)

        val sharedPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val currentPassword = sharedPrefs.getString("user_password", null)
        Log.d("ChangePassword", "Current password in prefs: $currentPassword")

        btnUpdate.setOnClickListener {
            val oldPassword = oldPasswordInput.text.toString()
            val newPassword = newPasswordInput.text.toString()

            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (oldPassword != currentPassword) {
                Toast.makeText(requireContext(), "Old password is incorrect", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPrefs.edit().putString("user_password", newPassword).apply()
            Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}
