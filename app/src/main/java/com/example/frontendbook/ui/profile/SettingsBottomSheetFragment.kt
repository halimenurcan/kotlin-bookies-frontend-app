package com.example.frontendbook.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.frontendbook.R
import com.example.frontendbook.ui.signIn.SignInActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val username = sharedPrefs.getString("user_username", "default")
        val name = sharedPrefs.getString("user_name", "")
        val surname = sharedPrefs.getString("user_surname", "")
        val email = sharedPrefs.getString("user_email", "")

        val usernameField = view.findViewById<EditText>(R.id.settingsUsername)
        val nameField = view.findViewById<EditText>(R.id.settingsName)
        val surnameField = view.findViewById<EditText>(R.id.settingsSurname)
        val emailField = view.findViewById<EditText>(R.id.settingsEmail)

        usernameField.setText(username)
        usernameField.isEnabled = false
        usernameField.isFocusable = false

        nameField.setText(name)
        surnameField.setText(surname)
        emailField.setText(email)

        view.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            val newName = nameField.text.toString()
            val newSurname = surnameField.text.toString()
            val newEmail = emailField.text.toString()

            sharedPrefs.edit()
                .putString("user_name", newName)
                .putString("user_surname", newSurname)
                .putString("user_email", newEmail)
                .apply()

            Toast.makeText(requireContext(), "Changes saved", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            sharedPrefs.edit().clear().apply()

            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }


        view.findViewById<Button>(R.id.btnChangePassword).setOnClickListener {
            val bottomSheet = ChangePasswordBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, "ChangePasswordBottomSheet")
        }
    }
}
