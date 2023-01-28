package com.quickrise.zkno.ui.activities.error

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.quickrise.zkno.R
import com.quickrise.zkno.api.ApiRepository
import com.quickrise.zkno.databinding.ActivityErrorBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.model.AppSendError
import com.quickrise.zkno.ui.activities.splash_screen.SplashScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ErrorActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityErrorBinding::inflate)
    private val apiRepository get() = ApiRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.apply {
            btnReload.setOnClickListener { reload() }
        }

        with(window) {
            val color = ContextCompat.getColor(this@ErrorActivity, R.color.background_content)

            statusBarColor = color
            navigationBarColor = color
        }

        sendErrorReport()
    }

    private fun sendErrorReport() {
        val error = intent.getStringExtra("error").toString()

        CoroutineScope(Dispatchers.IO).launch {
            apiRepository.appSendReport(AppSendError(error))
        }
    }

    private fun reload() {
        val intent = Intent(this, SplashScreenActivity::class.java)

        finish()
        startActivity(intent)
    }
}