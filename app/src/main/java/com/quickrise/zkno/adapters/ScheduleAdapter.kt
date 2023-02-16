package com.quickrise.zkno.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quickrise.zkno.BuildConfig
import com.quickrise.zkno.R
import com.quickrise.zkno.Utils
import com.quickrise.zkno.databinding.ScheduleListItemBinding
import com.quickrise.zkno.foundation.base.viewBinding
import com.quickrise.zkno.foundation.model.ScheduleModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

@SuppressLint("NotifyDataSetChanged")
class ScheduleAdapter(val activity: Activity) : RecyclerView.Adapter<ScheduleAdapter.Holder>() {
    var list: ArrayList<ScheduleModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(val binding: ScheduleListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.viewBinding(ScheduleListItemBinding::inflate))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]

        val containerBackground = GradientDrawable().also {
            it.color = ContextCompat.getColorStateList(
                activity,
                R.color.background_bottom_nav
            )
            it.cornerRadius = activity.resources.getDimension(R.dimen.squircleBorderRadius)
        }
        val lessonIdBackground = GradientDrawable().also {
            it.cornerRadius = activity.resources.getDimension(R.dimen.squircleBorderRadius)
        }
        val lessonTypeBackground = GradientDrawable().also {
            it.cornerRadius = activity.resources.getDimension(R.dimen.squircleBorderRadius)
        }

        with (holder.binding) {
            container.background = containerBackground
            lessonId.background = lessonIdBackground
            lessonName.text = item.name
            lessonId.text = "${item.id} пара"
            teacherName.text = item.teacher ?: "Преподаватель неизвестен"
            classroom.text = "Кабинет: " + (item.classroom ?: "неизвестен")
            lessonStartTime.text = item.start
            lessonEndTime.text = item.end
            afterPauseDuration.text = "Перемена ${item.afterPause?.duration} минут"
            afterPausePeriod.text = "${item.afterPause?.start} - ${item.afterPause?.end}"
            topic.text = item.topic
            lessonType.background = lessonTypeBackground

            if (item.topic != null) {
                topicIcon.visibility = View.VISIBLE
                topic.visibility = View.VISIBLE
            } else {
                topicIcon.visibility = View.GONE
                topic.visibility = View.GONE
            }

            lessonIdBackground.color = ContextCompat.getColorStateList(
                activity,
                if (!item.isEnded) R.color.accent else R.color.background_schedule_lesson_not_ended
            )

            if (item.afterPause == null) {
                afterPauseIcon.visibility = View.GONE
                afterPauseDuration.visibility = View.GONE
                afterPausePeriod.visibility = View.GONE
            } else {
                afterPauseIcon.visibility = View.VISIBLE
                afterPauseDuration.visibility = View.VISIBLE
                afterPausePeriod.visibility = View.VISIBLE
            }

            val text = when (item.type) {
                REPLACEMENT_TYPE -> "Заменена"
                ADDED_TYPE -> "Добавлена"
                else -> "Отменена"
            }

            val background = when (item.type) {
                REPLACEMENT_TYPE -> R.color.replacementLesson
                ADDED_TYPE -> R.color.addedLesson
                else -> R.color.cancelledLesson
            }

            when (item.type) {
                REPLACEMENT_TYPE -> {
                    lessonName.text = item.replacement ?: UNKNOWN_KEY
                    replacementLessonIcon.visibility = View.VISIBLE

                    with (replacementLessonName) {
                        this.text = item.name ?: UNKNOWN_KEY
                        this.visibility = View.VISIBLE
                        this.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    }
                }
                ADDED_TYPE -> {
                    lessonName.text = item.replacement ?: UNKNOWN_KEY
                }
                CANCELLED_TYPE -> {
                    lessonName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                else -> {
                    lessonName.paintFlags = 0
                    lessonType.visibility = View.GONE
                    replacementLessonIcon.visibility = View.GONE
                    replacementLessonName.visibility = View.GONE
                }
            }

            if (item.type != DEFAULT_TYPE) {
                with (lessonType) {
                    this.visibility = View.VISIBLE
                    this.text = text
                    lessonTypeBackground.color = ContextCompat.getColorStateList(
                        activity.applicationContext,
                        background
                    )
                }
            }

            if (item.marks.isNotEmpty()) {
                with(marks) {
                    adapter = ScheduleMarksAdapter(item.marks)
                    layoutManager = GridLayoutManager(
                        activity.applicationContext,
                        if (item.marks.distinct().isNotEmpty()) item.marks.distinct().size else 1
                    )
                    visibility = View.VISIBLE
                }
            } else {
                marks.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    companion object {
        const val DEFAULT_TYPE = "default"
        const val REPLACEMENT_TYPE = "replacement"
        const val ADDED_TYPE = "added"
        const val CANCELLED_TYPE = "cancelled"
        const val UNKNOWN_KEY = "Неизвестно"
    }
}