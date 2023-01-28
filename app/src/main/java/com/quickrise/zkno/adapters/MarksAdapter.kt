package com.quickrise.zkno.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.quickrise.zkno.R
import com.quickrise.zkno.databinding.MarksListItemBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.model.MarksItem
import com.quickrise.zkno.withActivity

@SuppressLint("NotifyDataSetChanged")
class MarksAdapter : RecyclerView.Adapter<MarksAdapter.Holder>() {
    var list: ArrayList<MarksItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(val binding: MarksListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.viewBinding(MarksListItemBinding::inflate))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]

        with (holder.binding) {
            withActivity { activity ->
                container.background = GradientDrawable().also {
                    it.color = ContextCompat.getColorStateList(
                        activity,
                        R.color.background_bottom_nav
                    )
                    it.cornerRadius = activity.resources.getDimension(R.dimen.squircleBorderRadius)
                }
            }

            avg.text = item.avg.toString()
            subjectName.text = item.subjectName
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}