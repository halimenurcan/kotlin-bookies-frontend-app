package com.example.frontendbook.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

        // Dummy veriler (örnek amaçlı). Gerçek uygulamada ViewModel veya başka bir kaynaktan gelir.
        val username = "100Lesh"
        val name = "John"
        val surname = "Doe"
        val email = "john.doe@example.com"

        view.findViewById<EditText>(R.id.settingsUsername).setText(username)
        view.findViewById<EditText>(R.id.settingsName).setText(name)
        view.findViewById<EditText>(R.id.settingsSurname).setText(surname)
        view.findViewById<EditText>(R.id.settingsEmail).setText(email)

        view.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
            dismiss()
        }


        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            // TODO: Save işlemi yapılacak
            dismiss()
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val sharedPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().clear().apply()

            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()


            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            requireActivity().finish()  // finish() yerine bu kullanılmalı çünkü fragment içindesin
        }


        view.findViewById<Button>(R.id.btnChangePassword).setOnClickListener {
            val bottomSheet = ChangePasswordBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, "ChangePasswordBottomSheet")
        }

    }
}
