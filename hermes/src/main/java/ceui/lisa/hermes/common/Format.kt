package ceui.lisa.hermes.common

import android.os.Build
import java.text.DateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun parseIsoToMillis(isoString: String): Long {
    val zdt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ZonedDateTime.parse(isoString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    return zdt.toInstant().toEpochMilli()
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
            // 超过一个月，按本地化日期格式展示
            val date = Date(timestamp)
            val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
            dateFormat.format(date)
        }
    }
}