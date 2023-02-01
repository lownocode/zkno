package com.quickrise.zkno.ui.fragments.settings

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.quickrise.zkno.*
import com.quickrise.zkno.databinding.FragmentSettingsBinding
import com.quickrise.zkno.foundation.base.navigator
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.base.viewModelFactory
import com.quickrise.zkno.ui.fragments.bottomSheets.ExitConfirmationBottomSheet
import com.quickrise.zkno.ui.fragments.bottomSheets.ChooseThemeBottomSheet
import com.quickrise.zkno.ui.fragments.profile.ProfileViewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel by viewModels<SettingsViewModel> { viewModelFactory() }
    private val profileViewModel by viewModels<ProfileViewModel> { viewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        updateUI()
    }

    private fun setupViews() = with (binding) {
        val cardBackground = GradientDrawable().also {
            it.color =
                ContextCompat.getColorStateList(requireActivity(), R.color.background_bottom_nav)
            it.cornerRadius = resources.getDimension(R.dimen.squircleBorderRadius)
        }
        val linkTelegramAccountBackground = GradientDrawable().also {
            it.color =
                ContextCompat.getColorStateList(requireActivity(), R.color.background_bottom_nav)
            it.cornerRadius = 25f
        }
        val linkVKAccountBackground = GradientDrawable().also {
            it.color =
                ContextCompat.getColorStateList(requireActivity(), R.color.background_bottom_nav)
            it.cornerRadius = 25f
        }

        btnSelectTheme.setOnClickListener { openThemeChooser() }
        btnExit.setOnClickListener { openExitModal() }
        btnBack.setOnClickListener { navigator().goBack() }
        btnAbout.setOnClickListener { goToAbout() }
        btnChangeCompactMenu.setOnClickListener { changeCompactMenu() }

        appearance.background = cardBackground
        linkTelegramAccountContainer.setOnClickListener { linkAccount("tg") }
        linkTelegramAccountContainer.background = linkTelegramAccountBackground
        linkVKAccountContainer.background = linkVKAccountBackground
        linkVKAccountContainer.setOnClickListener { linkAccount("vk") }
        darkThemeModeStatus.text = getDarkThemeModeStatus()
        switchChangeCompactMenu.isChecked = Preferences()
            .settings
            ?.getBoolean(Key.MENU_IS_COMPACT, false) == true
    }
    private fun linkAccount(account: String) {
        val link = when (account) {
            "tg" -> LINK_TELEGRAM_ACCOUNT
            else -> LINK_VK_ACCOUNT
        } + profileViewModel.user.value?.bindings?.code

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))

        startActivity(intent)
    }

    private fun openExitModal() =
        ExitConfirmationBottomSheet().show(parentFragmentManager, "exitConfirmation")

    private fun openThemeChooser() =
        ChooseThemeBottomSheet().show(parentFragmentManager, "bottomSheetSelectTheme")

    private fun goToAbout() =
        navigator().navigate(fragmentId = FragmentIndex.ABOUT)

    private fun getDarkThemeModeStatus(): String {
        val defaultMode = when(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            true -> Key.MODE_OFF
            false -> Key.MODE_AUTO
        }
        val darkThemeMode = Preferences().settings?.getString(Key.DARK_THEME_MODE, defaultMode).toString()

        return when(darkThemeMode) {
            Key.MODE_ON -> getString(R.string.darkThemeModeOn)
            Key.MODE_OFF -> getString(R.string.darkThemeModeOff)
            else -> getString(R.string.darkThemeModeAuto)
        }
    }

    private fun changeCompactMenu() {
        val menuIsCompact = Preferences().settings?.getBoolean(Key.MENU_IS_COMPACT, false)
        val bottomNavigation: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)

        with (binding) {
            switchChangeCompactMenu.isChecked = !switchChangeCompactMenu.isChecked
        }

        if (menuIsCompact == true) {
            Preferences().settings?.edit()?.putBoolean(Key.MENU_IS_COMPACT, false)?.apply()
            with (bottomNavigation) {
                labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_SELECTED
                layoutParams.height = resources.getDimension(R.dimen.fullSizeBottomNav).toInt()
            }

            return
        }

        Preferences().settings?.edit()?.putBoolean(Key.MENU_IS_COMPACT, true)?.apply()
        with (bottomNavigation) {
            labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_UNLABELED
            layoutParams.height = resources.getDimension(R.dimen.bottomNavCompact).toInt()
        }
    }

    private fun updateThemeMode(mode: String) = when (mode) {
        Key.MODE_ON -> {
            binding.darkThemeModeStatus.text = getString(R.string.darkThemeModeOn)
        }
        Key.MODE_OFF -> {
            binding.darkThemeModeStatus.text = getString(R.string.darkThemeModeOff)
        }
        else -> { //auto
            binding.darkThemeModeStatus.text = getString(R.string.darkThemeModeAuto)
        }
    }

    private fun updateUI() {
        updateThemeMode(viewModel.themeMode.value!!)
    }
}