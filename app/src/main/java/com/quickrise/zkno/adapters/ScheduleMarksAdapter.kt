package com.quickrise.zkno.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.quickrise.zkno.BuildConfig
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.ScheduleMarksItemBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.ui.activities.main.MainActivity
import com.quickrise.zkno.withActivity

class ScheduleMarksAdapter(
    private val list: ArrayList<String> = arrayListOf()
) : RecyclerView.Adapter<ScheduleMarksAdapter.ViewHolder>() {
    private val clearList = list.distinct()

    class ViewHolder(val binding: ScheduleMarksItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.viewBinding(ScheduleMarksItemBinding::inflate))
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mark = clearList[position]

        val duplicates = list.groupingBy { it }.eachCount().filter { it.value > 1 }

        val markBackground = GradientDrawable().also {
            it.cornerRadius = 25f
        }

        holder.binding.container.background = markBackground

        withActivity { activity ->
            if (Regex("[2-5]|Н").matches(mark)) {
                val backgroundId = activity.resources.getIdentifier(
                    "mark_$mark",
                    "color",
                    BuildConfig.APPLICATION_ID
                )
                val background = ContextCompat.getColorStateList(activity, backgroundId)

                with (holder.binding) {
                    this.mark.text = mark
                    markBackground.color = background

                    if ((duplicates[mark] ?: 0) > 0) with (counter) {
                        visibility = View.VISIBLE
                        text = duplicates[mark].toString()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return clearList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}