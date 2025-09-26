package ceui.lisa.hermes.loader

interface Repository<ValueT> {
    fun load(): ValueT
}
