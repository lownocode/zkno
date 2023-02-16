package com.quickrise.zkno.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.MarksMarkItemBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.model.MarksItem

@SuppressLint("NotifyDataSetChanged")
class MarksMarkAdapter(val activity: Activity) : RecyclerView.Adapter<MarksMarkAdapter.Holder>() {
    var list: ArrayList<MarksItem.Marks> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(val binding: MarksMarkItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.viewBinding(MarksMarkItemBinding::inflate))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]

        val containerBackground = GradientDrawable().also {
            it.cornerRadius = 22f
            it.color = ContextCompat.getColorStateList(
                activity,
                getMarkColor(item.mark)
            )
        }

        with (holder.binding) {
            container.background = containerBackground
            mark.text = item.mark
            date.text = item.date
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun getMarkColor(mark: String): Int = when (mark) {
        "2" -> R.color.mark_2
        "3" -> R.color.mark_3
        "4" -> R.color.mark_4
        "5" -> R.color.mark_5
        else -> R.color.text_secondary
    }
}