package ceui.lisa.hermes.task

import ceui.lisa.hermes.loadstate.LoadReason

abstract class LongTimeTask {

    abstract suspend fun runTask(reason: LoadReason)
}