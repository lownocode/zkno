package com.quickrise.zkno.adapters

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.ScheduleMarksItemBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.ui.activities.main.MainActivity
import com.quickrise.zkno.withActivity

class ScheduleMarksAdapter(
    list: ArrayList<String> = arrayListOf()
) : RecyclerView.Adapter<ScheduleMarksAdapter.ViewHolder>() {
    private val clearList = list.distinct()

    class ViewHolder(val binding: ScheduleMarksItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.viewBinding(ScheduleMarksItemBinding::inflate))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mark = clearList[position]

        val duplicates = clearList.groupingBy { it }.eachCount().filter { it.value > 1 }

        val markBackground = GradientDrawable().also {
            it.cornerRadius = 25f
        }

        holder.binding.container.background = markBackground

        withActivity { activity ->
            when(mark) {
                "2" -> {
                   with(holder.binding) {
                       val color = activity.resources.getColor(R.color.mark_2, activity.theme)

                       this.mark.setTextColor(color)
                       markBackground.color = ContextCompat.getColorStateList(
                           activity.applicationContext,
                           R.color.mark_2_background
                       )

                       if((duplicates["2"] ?: 0) > 0) with(counter) {
                           visibility = View.VISIBLE
                           text = duplicates["2"].toString()
                           setTextColor(color)
                       }
                   }
                }
                "3" -> {
                    with(holder.binding) {
                        val color = activity.resources.getColor(R.color.mark_3, activity.theme)

                        this.mark.setTextColor(color)
                        markBackground.color  = ContextCompat.getColorStateList(
                            activity.applicationContext,
                            R.color.mark_3_background
                        )

                        if((duplicates["3"] ?: 0) > 0) with(counter) {
                            visibility = View.VISIBLE
                            text = duplicates["3"].toString()
                            setTextColor(color)
                        }
                    }
                }
                "4" -> {
                    with(holder.binding) {
                        val color = activity.resources.getColor(R.color.mark_4, activity.theme)
                        this.mark.setTextColor(color)
                        markBackground.color = ContextCompat.getColorStateList(
                            activity.applicationContext,
                            R.color.mark_4_background
                        )

                        if((duplicates["4"] ?: 0) > 0) with(counter) {
                            visibility = View.VISIBLE
                            text = duplicates["4"].toString()
                            setTextColor(color)
                        }
                    }
                }
                "5" -> {
                    with(holder.binding) {
                        val color = activity.resources.getColor(R.color.mark_5, activity.theme)
                        this.mark.setTextColor(color)
                        markBackground.color  = ContextCompat.getColorStateList(
                            activity.applicationContext,
                            R.color.mark_5_background
                        )

                        if((duplicates["5"] ?: 0) > 0) with(counter) {
                            visibility = View.VISIBLE
                            text = duplicates["5"].toString()
                            setTextColor(color)
                        }
                    }
                }
                "Н" -> {
                    with(holder.binding) {
                        val color = activity.resources.getColor(R.color.mark_n, activity.theme)
                        this.mark.setTextColor(color)
                        markBackground.color  = ContextCompat.getColorStateList(
                            activity.applicationContext,
                            R.color.mark_n_background
                        )

                        if((duplicates["N"] ?: 0) > 0) with(counter) {
                            visibility = View.VISIBLE
                            text = duplicates["N"].toString()
                            setTextColor(color)
                        }
                    }
                }
            }
        }

        holder.binding.mark.text = mark
    }

    override fun getItemCount(): Int {
        return clearList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}