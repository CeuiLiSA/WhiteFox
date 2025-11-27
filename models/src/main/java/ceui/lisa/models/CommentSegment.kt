package ceui.lisa.models

data class CommentSegment(
    val text: String? = null,
    val emoji: String? = null // filename: "101.png"
)

fun parseComment(comment: String): List<CommentSegment> {
    val result = mutableListOf<CommentSegment>()
    var remaining = comment

    while (true) {
        val start = remaining.indexOf("(")
        if (start == -1) {
            if (remaining.isNotEmpty()) result += CommentSegment(text = remaining)
            break
        }

        val end = remaining.indexOf(")", start + 1)
        if (end == -1) {
            result += CommentSegment(text = remaining)
            break
        }

        val textPart = remaining.take(start)
        val key = remaining.substring(start, end + 1)

        if (textPart.isNotEmpty()) result += CommentSegment(text = textPart)
        if (emojiMap.containsKey(key)) {
            result += CommentSegment(emoji = emojiMap[key])
        } else {
            result += CommentSegment(text = key)
        }

        remaining = remaining.substring(end + 1)
    }

    return result
}