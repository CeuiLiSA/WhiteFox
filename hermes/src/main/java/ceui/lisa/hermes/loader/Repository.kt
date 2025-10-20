package ceui.lisa.hermes.loader

import ceui.lisa.hermes.loadstate.LoadReason

interface Repository<ValueT> {
    suspend fun load(reason: LoadReason): ValueT
}
