package org.keepgoeat.presentation.sign

import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.keepgoeat.R
import org.keepgoeat.data.service.SignService
import org.keepgoeat.databinding.ActivitySignBinding
import org.keepgoeat.presentation.home.HomeActivity
import org.keepgoeat.util.binding.BindingActivity
import javax.inject.Inject

@AndroidEntryPoint
class SignActivity : BindingActivity<ActivitySignBinding>(R.layout.activity_sign) {
    @Inject
    lateinit var signService: SignService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addListeners()
        // moveMain()
    }

    private fun addListeners() {
        binding.layoutSignIn.setOnClickListener {
            signService.loginKakao()
        }
    }

    private fun moveMain() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
