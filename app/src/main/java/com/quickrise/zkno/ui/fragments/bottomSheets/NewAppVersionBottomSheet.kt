package com.quickrise.zkno.ui.fragments.bottomSheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quickrise.zkno.*
import com.quickrise.zkno.databinding.BottomSheetNewAppVersionBinding
import com.quickrise.zkno.foundation.base.viewBinding

import com.google.android.material.R.id as googleMaterialResId

class NewAppVersionBottomSheet : BottomSheetDialogFragment() {
    private val binding by viewBinding(BottomSheetNewAppVersionBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        notNowButtonVisibility()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() = with (binding) {
        val containerBackground = GradientDrawable().also {
            it.color = ContextCompat.getColorStateList(
                requireContext(),
                R.color.background_bottom_sheet
            )
            it.cornerRadius = resources.getDimension(R.dimen.squircleBorderRadius)
        }

        container.background = containerBackground
        description.movementMethod = LinkMovementMethod.getInstance()

        btnDownload.setOnClickListener { download() }
        btnNotNow.setOnClickListener { postponed() }

        user.newAppVersion?.let {
            details.text = "${it.name} • ${it.date}"
            description.text = Utils.formatString(it.whatsNew)
        }
    }

    private fun notNowButtonVisibility() {
        val ignoreCode = Preferences().app?.getInt("ignoreAppUpdateCode", 0)

        if (ignoreCode == BuildConfig.VERSION_CODE) {
            binding.btnNotNow.visibility = View.GONE
        }
    }

    private fun download() {
        val url = Uri.parse(user.newAppVersion?.path)
        val intent = Intent(Intent.ACTION_VIEW, url)

        startActivity(intent)
    }

    private fun postponed() {
        Preferences().app?.edit()?.putInt("ignoreAppUpdateCode", BuildConfig.VERSION_CODE)?.apply()
        Toast.makeText(
            requireActivity().applicationContext,
            "Модальное окно больше не будет показано для этой версии приложения",
            Toast.LENGTH_LONG
        ).show()

        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.apply {
            window?.navigationBarColor = Color.TRANSPARENT
            setOnShowListener { setupBottomSheet(it) }
        }

        return dialog
    }

    private fun setupBottomSheet(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet: View = bottomSheetDialog.findViewById(googleMaterialResId.design_bottom_sheet) ?: return

        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        bottomSheetDialog.window?.setDimAmount(0.3f)
    }
}