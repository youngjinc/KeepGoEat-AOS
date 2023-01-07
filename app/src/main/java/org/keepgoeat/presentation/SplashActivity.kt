package org.keepgoeat.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.keepgoeat.R
import org.keepgoeat.presentation.onboarding.OnboardingActivity
import org.keepgoeat.presentation.sign.SignActivity
import org.keepgoeat.presentation.sign.SignSharedPreferences

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadSplashScreen()
    }

    private fun loadSplashScreen() {
        lifecycleScope.launch {
            delay(2000)
            moveToNext()
        }
    }

    private fun moveToNext() {
        if (SignSharedPreferences(this).isLogin) {
            startActivity(Intent(this, OnboardingActivity::class.java))
        } else {
            startActivity(Intent(this, SignActivity::class.java))
        }
    }
}
