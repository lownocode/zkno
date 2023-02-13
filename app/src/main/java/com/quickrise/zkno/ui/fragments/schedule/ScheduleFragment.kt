package com.quickrise.zkno.ui.fragments.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.quickrise.zkno.DIRECTION_NEXT
import com.quickrise.zkno.DIRECTION_PREVIOUS
import com.quickrise.zkno.R
import com.quickrise.zkno.Utils
import com.quickrise.zkno.adapters.ScheduleAdapter
import com.quickrise.zkno.databinding.FragmentScheduleBinding
import com.quickrise.zkno.ui.fragments.dialogs.CalendarDialog
import com.quickrise.zkno.Utils.FormatDate
import com.quickrise.zkno.api.ApiRepository
import com.quickrise.zkno.foundation.base.log
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.base.viewModelFactory
import com.quickrise.zkno.foundation.model.ScheduleDateBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    private val binding by viewBinding(FragmentScheduleBinding::bind)
    private val viewModel by viewModels<ScheduleViewModel> { viewModelFactory() }
    private val scheduleAdapter by lazy { ScheduleAdapter(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModelObservers()
    }

    override fun onPause() {
        super.onPause()

        clearFragmentResultListener("calendarResult")
    }

    override fun onResume() {
        super.onResume()

        setupListeners()
    }

    private fun setupViews() = with (binding) {
        btnDayNext.setOnClickListener { viewModel.skipDay(DIRECTION_NEXT) }
        btnDayPrevious.setOnClickListener { viewModel.skipDay(DIRECTION_PREVIOUS) }
        with(btnOpenCalendar) {
            setOnClickListener { openCalendar() }
            setOnLongClickListener { viewModel.goToday() }
        }

        with(scheduleList) {
            adapter = scheduleAdapter
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupViewModelObservers() = with (viewModel) {
        choosedDate.observe(viewLifecycleOwner) {
            updateUI()
        }

        scheduleList.observe(viewLifecycleOwner) {
            scheduleAdapter.list = it
        }

        scheduleErrorCode.observe(viewLifecycleOwner) {
            handleScheduleErrorCode(it)
        }
    }

    private fun setupListeners() {
        setFragmentResultListener("calendarResult") { _, bundle ->
            viewModel.updateDate(bundle.getLong("choosedDate"), true)
        }
    }

    private fun openCalendar() {
        val scheduleCalendarDialog = CalendarDialog()

        scheduleCalendarDialog.arguments = bundleOf("choosedDate" to viewModel.choosedDate.value!!)
        scheduleCalendarDialog.show(parentFragmentManager, "scheduleCalendar")
    }

    private fun handleScheduleErrorCode(code: String) = when (code) {
        "ITS_NOT_WORKING_DAY" -> binding.apply {
            placeholder.visibility = View.VISIBLE
            scheduleList.visibility = View.GONE
            placeholderIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.calendar3d))
            placeholderTitle.text = getString(R.string.weekendTitle)
            placeholderText.text = getString(R.string.weekendDescription)
        }
        "NO_SCHEDULE" -> binding.apply {
            placeholder.visibility = View.VISIBLE
            scheduleList.visibility = View.GONE
            placeholderIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.calendar_red))
            placeholderTitle.text = getString(R.string.scheduleNotFoundTitle)
            placeholderText.text = getString(R.string.scheduleNotFoundDescription)
        }
        else -> binding.apply {
            placeholder.visibility = View.GONE
            scheduleList.visibility = View.VISIBLE
        }
    }

    private fun updateUI() = with (binding) {
        btnOpenCalendar.text = FormatDate().getDateTime(viewModel.choosedDate.value!!)
        toolbarSubtitle.text = Utils.getDayOfWeekName(viewModel.choosedDate.value!!)
    }
}