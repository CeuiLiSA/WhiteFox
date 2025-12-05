package ceui.lisa.models

data class HomeAllReq(
    val vhi: List<String> = emptyList(),
    val vhm: List<String> = emptyList(),
    val vhn: List<String> = emptyList(),
    val states: List<String> = emptyList(),
    val next_params: NextPageSpec? = null,
)