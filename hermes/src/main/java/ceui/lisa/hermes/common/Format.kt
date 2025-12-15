package ceui.lisa.hermes.common

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ceui.lisa.hermes.R
import timber.log.Timber
import java.text.DateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
fun parseIsoToMillis(isoString: String): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val zdt = ZonedDateTime.parse(isoString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        zdt.toInstant().toEpochMilli()
    } else {
        try {
            val pattern = if (isoString.endsWith("Z")) {
                PTV1
            } else {
                PTV2
            }
            val sdf = java.text.SimpleDateFormat(pattern)
            sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
            sdf.parse(isoString)?.time ?: 0L
        } catch (ex: Exception) {
            Timber.e(ex)
            0L
        }
    }
}

@Composable
fun formatRelativeTime(timestamp: Long, fullTime: Boolean): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    if (diff < 0) return stringResource(R.string.hermes_time_from_future)

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    return when {
        minutes < 1 -> localizedString(R.string.hermes_time_just_now)
        hours < 1 -> localizedString(R.string.hermes_time_minutes_ago, minutes)
        days < 1 -> localizedString(R.string.hermes_time_hours_ago, hours)
        days < 30 -> localizedString(R.string.hermes_time_days_ago, days)
        else -> {
            val date = Date(timestamp)
            if (fullTime) {
                val dateFormat = DateFormat.getDateTimeInstance(
                    DateFormat.MEDIUM,
                    DateFormat.SHORT,
                    Locale.getDefault()
                )
                dateFormat.format(date)
            } else {
                val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
                dateFormat.format(date)
            }
        }
    }
}

private const val PTV1 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val PTV2 = "yyyy-MM-dd'T'HH:mm:ssXXX"