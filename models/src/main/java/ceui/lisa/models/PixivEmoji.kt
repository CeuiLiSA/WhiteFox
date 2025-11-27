package ceui.lisa.models

val emojiMap = mapOf(
    "(normal)" to "101.png",
    "(surprise)" to "102.png",
    "(serious)" to "103.png",
    "(heaven)" to "104.png",
    "(happy)" to "105.png",
    "(excited)" to "106.png",
    "(sing)" to "107.png",
    "(cry)" to "108.png",
    "(normal2)" to "201.png",
    "(shame2)" to "202.png",
    "(love2)" to "203.png",
    "(interesting2)" to "204.png",
    "(blush2)" to "205.png",
    "(fire2)" to "206.png",
    "(angry2)" to "207.png",
    "(shine2)" to "208.png",
    "(panic2)" to "209.png",
    "(normal3)" to "301.png",
    "(satisfaction3)" to "302.png",
    "(surprise3)" to "303.png",
    "(smile3)" to "304.png",
    "(shock3)" to "305.png",
    "(gaze3)" to "306.png",
    "(wink3)" to "307.png",
    "(happy3)" to "308.png",
    "(excited3)" to "309.png",
    "(love3)" to "310.png",
    "(normal4)" to "401.png",
    "(surprise4)" to "402.png",
    "(serious4)" to "403.png",
    "(love4)" to "404.png",
    "(shine4)" to "405.png",
    "(sweat4)" to "406.png",
    "(shame4)" to "407.png",
    "(sleep4)" to "408.png",
    "(heart)" to "501.png",
    "(teardrop)" to "502.png",
    "(star)" to "503.png"
)

fun emojiUrl(fileName: String): String {
    return "https://s.pximg.net/common/images/emoji/$fileName"
}