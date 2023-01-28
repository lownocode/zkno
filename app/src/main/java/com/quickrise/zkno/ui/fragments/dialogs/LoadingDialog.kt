package com.quickrise.zkno.ui.fragments.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.DialogLoadingBinding
import com.quickrise.zkno.foundation.base.viewBinding

class LoadingDialog : DialogFragment() {
    private val binding by viewBinding(DialogLoadingBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerContainer = GradientDrawable()

        with(spinnerContainer) {
            color = ContextCompat.getColorStateList(requireContext(), R.color.background_loading_spinner_container)
            cornerRadius = 40f
        }

        with(dialog?.window) {
            this?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            this?.navigationBarColor = Color.TRANSPARENT
        }

        binding.spinnerContainer.background = spinnerContainer
    }
}