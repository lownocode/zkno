package com.quickrise.zkno.ui.fragments.bottomSheets

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quickrise.zkno.Preferences
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.BottomSheetExitConfirmationBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.ui.activities.auth.AuthActivity

class ExitConfirmationBottomSheet : BottomSheetDialogFragment() {
    private val binding by viewBinding(BottomSheetExitConfirmationBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val containerBackground = GradientDrawable()

        with(containerBackground) {
            color = ContextCompat.getColorStateList(
                requireContext(),
                R.color.background_bottom_sheet
            )
            cornerRadius = resources.getDimension(R.dimen.squircleBorderRadius)
        }

        with(binding) {
            container.background = containerBackground

            btnCancel.setOnClickListener { dismiss() }
            btnConfirm.setOnClickListener { exit() }
        }
    }

    private fun exit() {
        val authIntent = Intent(requireContext(), AuthActivity::class.java)

        Preferences().user?.edit()?.remove("token")?.apply()

        with(requireActivity()) {
            startActivity(authIntent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        with(dialog) {
            window?.navigationBarColor = Color.TRANSPARENT
            setOnShowListener { setupBottomSheet(it) }
        }

        return dialog
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setupBottomSheet(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return

        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        bottomSheetDialog.window?.setDimAmount(0.3F)
    }
}