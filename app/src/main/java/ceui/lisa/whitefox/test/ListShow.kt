package ceui.lisa.whitefox.test

interface ListShow<T> {

    fun getListData(): List<T>?

    fun hasMore(): Boolean
}