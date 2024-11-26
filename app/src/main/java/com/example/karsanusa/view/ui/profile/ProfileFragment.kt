package com.example.karsanusa.view.ui.profile

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.karsanusa.data.preference.ThemePreference
import com.example.karsanusa.data.preference.UserPreference
import com.example.karsanusa.data.preference.dataStore
import com.example.karsanusa.data.preference.themeDataStore
import com.example.karsanusa.data.repository.UserRepository
import com.example.karsanusa.databinding.FragmentProfileBinding
import com.example.karsanusa.view.authentication.login.LoginActivity
import com.example.karsanusa.view.vmfactory.ThemeViewModelFactory
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    // Initialize the ViewModel using ViewModelProvider and passing ThemePreference instance
    private val profileViewModel: ProfileViewModel by viewModels {
        ThemeViewModelFactory(
            ThemePreference.getInstance(requireContext().themeDataStore),
            UserRepository.getInstance(
                UserPreference.getInstance(requireContext().dataStore)) // Get UserPreference instance
        )
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

        val selectedLanguageIndex = loadLanguageIndex()
        val languages = arrayOf("English", "Bahasa Indonesia")
        setLanguage(languages[selectedLanguageIndex])

        // Tambahkan listener untuk tombol tema
        binding.buttonProfileTheme.setOnClickListener {
            // Aksi ketika tombol tema ditekan
            Toast.makeText(requireContext(), "Theme button clicked!", Toast.LENGTH_SHORT).show()
            showThemeSelectionDialog()
        }

        // Tambahkan listener untuk tombol bahasa
        binding.buttonProfileLanguange.setOnClickListener {
            // Aksi ketika tombol bahasa ditekan
            Toast.makeText(requireContext(), "Language button clicked!", Toast.LENGTH_SHORT).show()
            showLanguageDialog()
        }

        // Tambahkan listener untuk tombol akun
        binding.buttonProfileSignOut.setOnClickListener {
            // Aksi ketika tombol akun ditekan
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

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "Bahasa Indonesia")
        var selectedLanguageIndex = loadLanguageIndex() // Ambil index bahasa yang disimpan

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Language")
            .setSingleChoiceItems(languages, selectedLanguageIndex) { _, which ->
                selectedLanguageIndex = which // Update selected index
            }
            .setPositiveButton("Apply") { dialog, _ ->
                val selectedLanguage = languages[selectedLanguageIndex]
                setLanguage(selectedLanguage) // Ubah bahasa
                saveLanguageIndex(selectedLanguageIndex) // Simpan pilihan bahasa
                dialog.dismiss()
                Toast.makeText(requireContext(), "Language changed to $selectedLanguage", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setLanguage(language: String) {
        val locale = when (language) {
            "Bahasa Indonesia" -> Locale("id")
            "English" -> Locale("en")
            else -> Locale("id") // Default ke Bahasa Indonesia
        }

        // Set default locale
        Locale.setDefault(locale)

        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        // Terapkan konteks baru
        requireActivity().createConfigurationContext(configuration)

        Log.d("LanguageChange", "Language changed to: $language (Locale: ${locale.language})")
    }

    // Simpan pilihan bahasa yang dipilih ke SharedPreferences
    private fun saveLanguageIndex(index: Int) {
        sharedPreferences.edit().putInt("language_index", index).apply()
    }

    // Ambil pilihan bahasa yang disimpan dari SharedPreferences
    private fun loadLanguageIndex(): Int {
        return sharedPreferences.getInt("language_index", 1) // Default: Bahasa Indonesia (index 1)
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