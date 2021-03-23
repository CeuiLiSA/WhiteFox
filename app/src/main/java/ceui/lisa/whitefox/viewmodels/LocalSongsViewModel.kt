package ceui.lisa.whitefox.viewmodels

import ceui.lisa.whitefox.core.LocalSongsRepo
import ceui.lisa.whitefox.core.Repository

class LocalSongsViewModel : ListViewModel<String>() {

    override fun loadRepo(): Repository<String> {
        return LocalSongsRepo()
    }
}