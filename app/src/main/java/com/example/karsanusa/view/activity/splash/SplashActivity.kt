package com.example.karsanusa.view.activity.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.karsanusa.R
import com.example.karsanusa.data.preference.ThemePreference
import com.example.karsanusa.data.preference.themeDataStore
import com.example.karsanusa.view.authentication.welcome.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var themePreference: ThemePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        themePreference = ThemePreference.getInstance(this.themeDataStore)
        applyThemeSettings()

        lifecycleScope.launch {
            delay(1000)
            val intent = Intent(this@SplashActivity, WelcomeActivity:: class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun applyThemeSettings() {
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
