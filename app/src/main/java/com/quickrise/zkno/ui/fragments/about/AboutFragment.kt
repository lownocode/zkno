package com.quickrise.zkno.ui.fragments.about

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import com.quickrise.zkno.BuildConfig
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.FragmentAboutBinding
import com.quickrise.zkno.foundation.base.navigator
import com.quickrise.zkno.foundation.base.viewBinding

class AboutFragment : Fragment(R.layout.fragment_about) {
    private val binding by viewBinding(FragmentAboutBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardBackground = GradientDrawable().also {
            it.color = ContextCompat.getColorStateList(requireContext(), R.color.background_bottom_nav)
            it.cornerRadius = resources.getDimension(R.dimen.squircleBorderRadius)
        }
        val aboutBackground = GradientDrawable().also {
            it.color = ContextCompat.getColorStateList(requireContext(), R.color.background_bottom_nav)
            it.cornerRadius = resources.getDimension(R.dimen.squircleBorderRadius)
        }

        with(binding) {
            btnBack.setOnClickListener { navigator().goBack() }
            vkButton.setOnClickListener { redirect("vk") }
            tgButton.setOnClickListener { redirect("tg") }
            with(cardBackground) {
                vkButton.background = this
                tgButton.background = this
            }

            aboutApp.background = aboutBackground
            appVersionInfo.text = getAppVersionInfo()
        }
    }

    private fun redirect(network: String) {
        val link = when(network) {
            "vk" -> "https://vk.com/zknoassistant"
            else -> "https://t.me/zknoassistant_bot" //tg
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))

        startActivity(intent)
    }

    private fun getAppVersionInfo(): String =
        "Версия: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\nДата сборки: ${BuildConfig.BUILD_TIME}"
}