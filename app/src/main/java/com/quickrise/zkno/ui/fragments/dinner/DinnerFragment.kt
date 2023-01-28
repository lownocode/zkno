package com.quickrise.zkno.ui.fragments.dinner

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.quickrise.zkno.DIRECTION_NEXT
import com.quickrise.zkno.DIRECTION_PREVIOUS
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.FragmentDinnerBinding
import com.quickrise.zkno.ui.fragments.dialogs.CalendarDialog
import com.quickrise.zkno.Utils.FormatDate
import com.quickrise.zkno.Utils
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.base.viewModelFactory

class DinnerFragment : Fragment(R.layout.fragment_dinner) {
    private val binding by viewBinding(FragmentDinnerBinding::bind)
    private val viewModel by viewModels<DinnerViewModel> { viewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModelObservers()
        setupListeners()
    }

    private fun setupViews() = with (binding) {
        dinnerImage.clipToOutline = true

        btnDayNext.setOnClickListener { viewModel.skipDay(DIRECTION_NEXT) }
        btnDayPrevious.setOnClickListener { viewModel.skipDay(DIRECTION_PREVIOUS) }
        with(btnOpenCalendar) {
            setOnClickListener { openCalendar() }
            setOnLongClickListener { viewModel.goToday() }
        }
    }

    private fun setupViewModelObservers() = with (viewModel) {
        choosedDate.observe(viewLifecycleOwner) {
            updateUI()
        }
        dinnerErrorCode.observe(viewLifecycleOwner) {
            handleDinnerImageErrorCode(it)
        }
        dinnerImage.observe(viewLifecycleOwner) {
            loadDinnerImage(it)
        }
    }

    private fun loadDinnerImage(bytes: ByteArray) {
        Glide.with(this)
            .load(bytes)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.dinnerImage)
    }

    private fun setupListeners() {
        setFragmentResultListener("calendarResult") { _, bundle ->
            viewModel.updateDate(bundle.getLong("choosedDate"), true)
        }
    }

    private fun openCalendar() {
        val scheduleCalendarDialog = CalendarDialog()

        scheduleCalendarDialog.arguments = bundleOf("choosedDate" to viewModel.choosedDate.value!!)
        scheduleCalendarDialog.show(parentFragmentManager, "dinnerCalendar")
    }

    private fun handleDinnerImageErrorCode(code: String) = when (code) {
        "NO_DINNER" -> binding.apply {
            placeholder.visibility = View.VISIBLE
            dinnerImage.visibility = View.GONE
            placeholderIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.no_dinner))
            placeholderTitle.text = getString(R.string.dinnerNotFound)
            placeholderText.text = getString(R.string.dinnerNotFoundDescription)
        }
        else -> binding.apply {
            placeholder.visibility = View.GONE
            dinnerImage.visibility = View.VISIBLE
        }
    }

    private fun updateUI() {
        with(binding) {
            btnOpenCalendar.text = FormatDate().getDateTime(viewModel.choosedDate.value!!)
            toolbarSubtitle.text = Utils.getDayOfWeekName(viewModel.choosedDate.value!!)
        }
    }
}