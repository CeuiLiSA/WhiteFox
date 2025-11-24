package ceui.lisa.models

data class Comment(
    val comment: String? = null,
    val date: String? = null,
    val has_replies: Boolean = false,
    val id: Long = 0L,
    val stamp: Stamp? = null,
    val user: User = User()
) : ModelObject {
    override val objectUniqueId: Long
        get() {
            return id
        }
}