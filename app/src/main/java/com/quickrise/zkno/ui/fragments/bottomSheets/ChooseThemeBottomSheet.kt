package com.quickrise.zkno.ui.fragments.bottomSheets

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quickrise.zkno.Key
import com.quickrise.zkno.Preferences
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.BottomSheetSelectThemeBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.base.viewModelFactory
import com.quickrise.zkno.ui.fragments.settings.SettingsViewModel

class ChooseThemeBottomSheet : BottomSheetDialogFragment() {
    private val binding by viewBinding(BottomSheetSelectThemeBinding::inflate)
    private val viewModel by viewModels<SettingsViewModel> { viewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModelObservers()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        with(dialog) {
            window?.navigationBarColor = Color.TRANSPARENT
            setOnShowListener { setupBottomSheet(it) }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()

        removeViewModelObservers()
    }

    private fun setupViews() = with (binding) {
        val containerBackground = GradientDrawable().also {
            it.color = ContextCompat.getColorStateList(
                requireContext(),
                R.color.background_bottom_sheet
            )
            it.cornerRadius = resources.getDimension(R.dimen.squircleBorderRadius)
        }

        container.background = containerBackground

        btnOn.setOnClickListener { viewModel.changeThemeMode(Key.MODE_ON) }
        btnOff.setOnClickListener { viewModel.changeThemeMode(Key.MODE_OFF) }
    }

    private fun setupViewModelObservers() = with (viewModel) {
        themeMode.observe(viewLifecycleOwner) {
            updateUI()
        }
    }

    private fun removeViewModelObservers() = with (viewModel) {
        themeMode.removeObservers(viewLifecycleOwner)
    }

    private fun setAllButtonsDisable() {
        with(binding) {
            btnOnCheckIndicator.visibility = View.GONE
            btnOffCheckIndicator.visibility = View.GONE
        }
    }

    private fun setupBottomSheet(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet: View = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet) ?: return

        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        bottomSheetDialog.window?.setDimAmount(0.3F)
    }

    private fun selectIndicatorTheme(mode: String) {
        setAllButtonsDisable()

        when (mode) {
            Key.MODE_ON -> {
                binding.btnOnCheckIndicator.visibility = View.VISIBLE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            Key.MODE_OFF -> {
                binding.btnOffCheckIndicator.visibility = View.VISIBLE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun updateUI() {
        selectIndicatorTheme(viewModel.themeMode.value!!)
    }
}