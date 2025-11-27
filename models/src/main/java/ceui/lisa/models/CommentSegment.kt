package ceui.lisa.models

data class CommentSegment(
    val text: String? = null,
    val emoji: String? = null // filename: "101.png"
)

fun parseComment(comment: String): List<CommentSegment> {
    val result = mutableListOf<CommentSegment>()
    val regex = """\([^)]+\)""".toRegex()
    var lastIndex = 0

    regex.findAll(comment).forEach { match ->
        if (match.range.first > lastIndex) {
            val text = comment.substring(lastIndex, match.range.first)
            result += CommentSegment(text = text)
        }
        val key = match.value
        val emojiFile = emojiMap[key]
        result += if (emojiFile != null) {
            CommentSegment(emoji = emojiFile)
        } else {
            CommentSegment(text = key)
        }
        lastIndex = match.range.last + 1
    }

    if (lastIndex < comment.length) {
        result += CommentSegment(text = comment.substring(lastIndex))
    }

    return result
}