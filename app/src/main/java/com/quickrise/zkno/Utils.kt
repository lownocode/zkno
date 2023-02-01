package com.quickrise.zkno

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.quickrise.zkno.App.Companion.user
import com.quickrise.zkno.foundation.base.ActivityActions
import com.quickrise.zkno.ui.activities.main.MainActivity
import java.text.SimpleDateFormat
import java.util.*

fun String.Companion.empty() = ""

data class DeeplinkData(
    val path: String,
    val values: List<String>
)

object Utils {
    fun hideKeyboard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun decodeDeeplink(string: String): DeeplinkData {
        val clearString = string.substringAfter("zkno://")
        val path = clearString.substringBefore("?")
        val values = clearString.substringAfter("?").split("&").map {
            it.substringAfter("=")
        }

        return DeeplinkData(
            path = path,
            values = values
        )
    }

    fun isAppInstalled(packageName: String): Boolean {
        return try {
            App
                .instance
                .packageManager
                .getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES)

            true
        } catch (e: Exception) {
            false
        }
    }

    fun isDarkTheme(): Boolean {
        var isDarkMode = false

        withActivity {
            isDarkMode = it.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }

        return isDarkMode
    }

    fun vibrate(
        activity: Activity,
        ms: Long,
        effect: Int = VibrationEffect.DEFAULT_AMPLITUDE
    ) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return (activity.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager)
                .defaultVibrator
                .vibrate(VibrationEffect.createOneShot(ms, effect))
        }

        (activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
            .vibrate(VibrationEffect.createOneShot(ms, effect))
    }

    class FormatDate {
        @SuppressLint("SimpleDateFormat")
        fun getDateTime(unix: Long): String {
            val date = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

            return date.format(unix)
        }
    }

    fun formatString(text: String): SpannableString {
        val spannableString = SpannableString(text)

        val emailPattern = Regex(RegexPattern.email, RegexOption.IGNORE_CASE)
        val urlPattern = Regex(RegexPattern.url, RegexOption.IGNORE_CASE)

        val emailMatches = emailPattern.findAll(text)
        val urlMatches = urlPattern.findAll(text)

        for (match in emailMatches) {
            spannableString.setSpan(
                HandleSpanClick("email", match.groupValues[0]),
                match.range.first,
                match.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(App.instance, R.color.accent)),
                match.range.first,
                match.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        for (match in urlMatches) {
            spannableString.setSpan(
                HandleSpanClick("url", match.groupValues[0]),
                match.range.first,
                match.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(App.instance, R.color.accent)),
                match.range.first,
                match.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                UnderlineSpan(),
                match.range.first,
                match.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannableString
    }

    fun getDayOfWeekName(timestamp: Long): String {
        fun getDayOfWeek(day: Int): String {
            return when(day) {
                Calendar.MONDAY -> "Понедельник"
                Calendar.TUESDAY -> "Вторник"
                Calendar.WEDNESDAY -> "Среда"
                Calendar.THURSDAY -> "Четверг"
                Calendar.FRIDAY -> "Пятница"
                Calendar.SATURDAY -> "Суббота"
                Calendar.SUNDAY -> "Воскресенье"
                else -> "Неизвестный день"
            }
        }

        fun getDate(millis: Long): String {
            return SimpleDateFormat("yyyy:MM:dd", Locale.ENGLISH).format(millis)
        }

        val date = getDate(timestamp).split(":").map { it.toInt() }
        val isToday = getDate(timestamp) == getDate(System.currentTimeMillis())

        val dayOfWeek = Calendar.getInstance().also {
            it.set(date[0], date[1] - 1, date[2])
        }.get(Calendar.DAY_OF_WEEK)

        return if (isToday) getDayOfWeek(dayOfWeek) + ", сегодня" else getDayOfWeek(dayOfWeek)
    }
}

private class HandleSpanClick(val type: String, val entity: String) : ClickableSpan() {
    override fun onClick(widget: View) = withActivity { activity ->
        Utils.vibrate(activity, 10)

        when (type) {
            "url" -> {
                val url = when (!entity.startsWith("http://", true)) {
                    true -> "http://${entity.lowercase()}"
                    false -> entity.lowercase()
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

                activity.startActivity(intent)
            }
            "email" -> {
                val subject = "${user?.name}, группа ${user?.group?.name}"
                val intent = Intent(Intent.ACTION_SENDTO).also {
                    it.data = Uri.parse("mailto:$entity?subject=$subject")
                }

                activity.startActivity(
                    Intent.createChooser(intent, String.empty())
                )
            }
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)

        ds.isUnderlineText = false
    }
}

class Preferences(activity: Activity? = null) {
    var user: SharedPreferences? = null
    var app: SharedPreferences? = null
    var settings: SharedPreferences? = null

    init {
        if (activity == null) withActivity { updatePrefs(it) } else updatePrefs(activity)
    }

    private fun updatePrefs(activity: Activity) {
        user = activity.getSharedPreferences("USER", Context.MODE_PRIVATE)
        app = activity.getSharedPreferences("APP", Context.MODE_PRIVATE)
        settings = activity.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
    }
}