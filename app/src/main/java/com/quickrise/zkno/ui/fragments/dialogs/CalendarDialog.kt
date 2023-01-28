package com.quickrise.zkno.ui.fragments.dialogs

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.DialogCalendarBinding
import com.quickrise.zkno.foundation.base.viewBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarDialog : DialogFragment() {
    private val binding by viewBinding(DialogCalendarBinding::inflate)
    private var choosedDate = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scheduleCalendar.date = arguments?.getLong("choosedDate") ?: choosedDate

        val containerBackground = GradientDrawable().also {
            it.color = ContextCompat.getColorStateList(
                requireContext(),
                R.color.background_schedule_calendar
            )
            it.cornerRadius = resources.getDimension(R.dimen.squircleBorderRadius)
        }

        val calendarMinDate = Calendar.getInstance().also {
            val year = it.get(Calendar.YEAR)
            val isExcessYear = year % 2 == 1

            it.set(if (isExcessYear) year - 1 else year, Calendar.SEPTEMBER, 1)
        }
        val calendarMaxDate = Calendar.getInstance().also {
            val year = it.get(Calendar.YEAR)
            val isExcessYear = year % 2 == 1

            it.set(if (isExcessYear) year else year + 1, Calendar.JUNE, 30)
        }

        with(dialog?.window) {
            this?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            this?.navigationBarColor = Color.TRANSPARENT
        }

        with(binding) {
            container.background = containerBackground

            btnPickDate.setOnClickListener { pickDate() }

            with(scheduleCalendar) {
                minDate = calendarMinDate.timeInMillis
                this.maxDate = calendarMaxDate.timeInMillis
                setOnDateChangeListener { _, year, month, dayOfMonth ->
                    val unix = SimpleDateFormat("dd-MM-yyyy").parse("$dayOfMonth-${month + 1}-$year")?.time

                    choosedDate = unix ?: 0
                }
            }
        }
    }

    private fun pickDate() {
        setFragmentResult("calendarResult", bundleOf("choosedDate" to choosedDate))
        dismiss()
    }
}