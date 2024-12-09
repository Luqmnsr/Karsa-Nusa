package com.example.karsanusa.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.karsanusa.R
import com.example.karsanusa.view.authentication.welcome.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        lifecycleScope.launch {
            delay(1000)
            val intent = Intent(this@SplashActivity, WelcomeActivity:: class.java)
            startActivity(intent)
            finish()
        }
    }
}
