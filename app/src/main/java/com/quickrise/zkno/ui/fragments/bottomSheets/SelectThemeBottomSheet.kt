package com.quickrise.zkno.ui.fragments.bottomSheets

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quickrise.zkno.Key
import com.quickrise.zkno.Preferences
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.BottomSheetSelectThemeBinding
import com.quickrise.zkno.foundation.base.viewBinding

class SelectThemeBottomSheet : BottomSheetDialogFragment() {
    private val binding by viewBinding(BottomSheetSelectThemeBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getThemeMode()

        val containerBackground = GradientDrawable().also {
            it.color = ContextCompat.getColorStateList(
                requireContext(),
                R.color.background_bottom_sheet
            )
            it.cornerRadius = resources.getDimension(R.dimen.squircleBorderRadius)
        }

        with(binding) {
            container.background = containerBackground

            btnOn.setOnClickListener { setupDarkTheme(Key.MODE_ON) }
            btnOff.setOnClickListener { setupDarkTheme(Key.MODE_OFF) }
            btnAuto.setOnClickListener { setupDarkTheme(Key.MODE_AUTO) }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                btnAuto.visibility = View.GONE
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        with(dialog) {
            window?.navigationBarColor = Color.TRANSPARENT
            setOnShowListener { setupBottomSheet(it) }
        }

        return dialog
    }

    private fun setAllButtonsDisable() {
        with(binding) {
            btnOnCheckIndicator.visibility = View.GONE
            btnOffCheckIndicator.visibility = View.GONE
            btnAutoCheckIndicator.visibility = View.GONE
        }
    }

    private fun getThemeMode() {
        setAllButtonsDisable()

        val defaultMode = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            Key.MODE_OFF
        else
            Key.MODE_AUTO

        when(Preferences().settings?.getString(Key.DARK_THEME_MODE, defaultMode)) {
            Key.MODE_ON -> binding.btnOnCheckIndicator.visibility = View.VISIBLE
            Key.MODE_OFF -> binding.btnOffCheckIndicator.visibility = View.VISIBLE
            Key.MODE_AUTO -> binding.btnAutoCheckIndicator.visibility = View.VISIBLE
        }
    }

    private fun setupDarkTheme(mode: String) {
        val darkThemeModeIndicatorText: TextView = requireActivity().findViewById(R.id.darkThemeModeStatus)

        setAllButtonsDisable()

        when(mode) {
            Key.MODE_ON -> {
                darkThemeModeIndicatorText.text = getString(R.string.darkThemeModeOn)
                binding.btnOnCheckIndicator.visibility = View.VISIBLE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Preferences().settings?.edit()?.putString(Key.DARK_THEME_MODE, Key.MODE_ON)?.apply()
            }
            Key.MODE_OFF -> {
                darkThemeModeIndicatorText.text = getString(R.string.darkThemeModeOff)
                binding.btnOffCheckIndicator.visibility = View.VISIBLE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Preferences().settings?.edit()?.putString(Key.DARK_THEME_MODE, Key.MODE_OFF)?.apply()
            }
            Key.MODE_AUTO -> {
                darkThemeModeIndicatorText.text = getString(R.string.darkThemeModeAuto)
                binding.btnAutoCheckIndicator.visibility = View.VISIBLE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                Preferences().settings?.edit()?.putString(Key.DARK_THEME_MODE, Key.MODE_AUTO)?.apply()
            }
        }

        dismiss()
    }

    private fun setupBottomSheet(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet: View = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet) ?: return

        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        bottomSheetDialog.window?.setDimAmount(0.3F)
    }
}