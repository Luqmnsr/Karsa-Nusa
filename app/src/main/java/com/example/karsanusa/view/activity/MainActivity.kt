package com.example.karsanusa.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.karsanusa.R
import com.example.karsanusa.data.preference.ThemePreference
import com.example.karsanusa.data.preference.themeDataStore
import com.example.karsanusa.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var themePreference: ThemePreference

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize themePreference
        themePreference = ThemePreference.getInstance(this.themeDataStore)
        // Apply theme before setContentView
        applyThemeSettings()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Initialize bottom navigation and navigation controller
        val navView: BottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.activity_main_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_community,
                R.id.fragment_home,
                R.id.fragment_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun applyThemeSettings() {
        // Use lifecycleScope to fetch theme setting
        lifecycleScope.launch {
            themePreference.getThemeSetting().collect { isDarkModeActive ->
                Log.d("MainActivity", "Theme isDarkModeActive: $isDarkModeActive")
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}
