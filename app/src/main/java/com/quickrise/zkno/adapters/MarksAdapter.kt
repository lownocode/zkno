package com.quickrise.zkno.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.MarksListItemBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.model.MarksItem

@SuppressLint("NotifyDataSetChanged")
class MarksAdapter (val activity: Activity) : RecyclerView.Adapter<MarksAdapter.Holder>() {
    var list: ArrayList<MarksItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(val binding: MarksListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.viewBinding(MarksListItemBinding::inflate))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]

        val marksAdapter = MarksMarkAdapter(activity)

        with (holder.binding) {
            val containerBackground = GradientDrawable().also {
                it.color = ContextCompat.getColorStateList(
                    activity,
                    R.color.background_bottom_nav
                )
                it.cornerRadius = activity.resources.getDimension(R.dimen.squircleBorderRadius)
            }

            val averageMarkBackground = GradientDrawable().also {
                it.color = ContextCompat.getColorStateList(
                    activity,
                    getAVGColor(item.avg)
                )
                it.cornerRadius = 30f
            }

            container.background = containerBackground
            averageMarkContainer.background = averageMarkBackground

            marksList.adapter = marksAdapter
            marksList.layoutManager = GridLayoutManager(activity, 5)
            marksAdapter.list = item.marks
            averageMark.text = item.avg.toString()
            subjectName.text = item.subjectName

            if (item.marks.none { it.mark != "Н" }) {
                averageMarkContainer.visibility = View.GONE
                marksEmpty.visibility = View.VISIBLE
                subjectName.layoutParams = (subjectName.layoutParams as ViewGroup.MarginLayoutParams).also {
                    it.setMargins(0, 0, 0, 0)
                }
                marksList.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun getAVGColor(mark: Float): Int {
        val colorId = when (mark) {
            in 4.0..4.99 -> R.color.mark_4
            in 3.0..4.0 -> R.color.mark_3
            in 2.0..3.0 -> R.color.mark_2
            else -> R.color.mark_5
        }

        return colorId
    }
}