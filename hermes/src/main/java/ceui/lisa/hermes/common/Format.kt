package ceui.lisa.hermes.common

import android.annotation.SuppressLint
import android.os.Build
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
                "yyyy-MM-dd'T'HH:mm:ss'Z'"
            } else {
                "yyyy-MM-dd'T'HH:mm:ssXXX"
            }
            val sdf = java.text.SimpleDateFormat(pattern)
            sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
            sdf.parse(isoString)?.time ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }
}

fun formatRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    if (diff < 0) return "来自未来" // 处理未来时间情况

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    return when {
        minutes < 1 -> "刚刚"
        hours < 1 -> "${minutes} 分钟前"
        days < 1 -> "${hours} 小时前"
        days < 30 -> "${days} 天前"
        else -> {
            val date = Date(timestamp)
            val dateFormat = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, // 日期风格
                DateFormat.SHORT,  // 时间风格
                Locale.getDefault()
            )
            dateFormat.format(date)
        }
    }
}