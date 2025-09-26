package ceui.lisa.hermes.loader

interface Repository<ValueT> {
    suspend fun load(): ValueT
}
