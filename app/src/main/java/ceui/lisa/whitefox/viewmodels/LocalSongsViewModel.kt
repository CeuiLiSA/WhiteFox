package ceui.lisa.whitefox.viewmodels

import ceui.lisa.whitefox.core.LocalSongsRepo
import ceui.lisa.whitefox.core.Repository
import java.io.File

class LocalSongsViewModel : ListViewModel<File>() {

    override fun loadRepo(): Repository<File> {
        return LocalSongsRepo()
    }
}