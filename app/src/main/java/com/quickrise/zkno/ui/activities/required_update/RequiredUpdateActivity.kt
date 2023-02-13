package com.quickrise.zkno.ui.activities.required_update

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.ActivityRequiredUpdateBinding
import com.quickrise.zkno.foundation.base.viewBinding

class RequiredUpdateActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityRequiredUpdateBinding::inflate)
    //TODO брать юзера из вьюмодели

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        with(window) {
            ContextCompat.getColor(applicationContext, R.color.background_content).also {
                this.statusBarColor = it
                this.navigationBarColor = it
            }
        }

        val whatsNewBackground = GradientDrawable()

        with(whatsNewBackground) {
            color = ContextCompat.getColorStateList(applicationContext, R.color.accentDarken2)
            cornerRadius = 100f
        }

        with(binding) {
            btnDownload.setOnClickListener { download() }
//            whatsNewDescription.text = user?.newAppVersion?.whatsNew
            whatsNewHead.background = whatsNewBackground
        }
    }

    private fun download() {
//        val url = Uri.parse(user?.newAppVersion?.path)
//        val intent = Intent(Intent.ACTION_VIEW, url)

//        startActivity(intent)
    }
}