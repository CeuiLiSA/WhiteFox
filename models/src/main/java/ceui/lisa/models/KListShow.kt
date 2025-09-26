package ceui.lisa.models

interface KListShow<T> {

    val displayList: List<T>

    val nextPageUrl: String?
}