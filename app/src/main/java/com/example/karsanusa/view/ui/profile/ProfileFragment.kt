package com.example.karsanusa.view.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.karsanusa.databinding.FragmentProfileBinding
import com.example.karsanusa.view.authentication.login.LoginActivity
import com.example.karsanusa.view.vmfactory.ThemeViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels {
        ThemeViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonProfileTheme.setOnClickListener {
            Toast.makeText(requireContext(), "Theme button clicked!", Toast.LENGTH_SHORT).show()
            showThemeSelectionDialog()
        }

        binding.buttonProfileSignOut.setOnClickListener {
            Toast.makeText(requireContext(), "Account button clicked!", Toast.LENGTH_SHORT).show()
            showLogoutDialog()
        }
    }

    private fun showThemeSelectionDialog() {
        // Observe the current theme setting dynamically when the dialog is shown
        profileViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            val options = arrayOf("Dark Mode", "Light Mode")
            var selectedTheme = if (isDarkModeActive) 0 else 1 // 0: Dark Mode, 1: Light Mode

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose Theme")
                .setSingleChoiceItems(options, selectedTheme) { _, which ->
                    selectedTheme = which
                }
                .setPositiveButton("Apply") { dialog, _ ->
                    val isDarkMode = (selectedTheme == 0)
                    profileViewModel.saveThemeSetting(isDarkMode)

                    if (isDarkMode) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }

                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Theme applied successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showLogoutDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                // Panggil fungsi logout dari ViewModel
                profileViewModel.logout()

                // Navigasi ke LoginActivity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear stack dan buat LoginActivity menjadi yang pertama
                startActivity(intent)
                // Jika menggunakan Activity, Anda bisa juga memanggil finish() di sini untuk menutup ProfileActivity (jika ada)
                activity?.finish()
            }
            .setNegativeButton("Tidak", null) // Tidak melakukan apa-apa jika memilih "Tidak"
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}