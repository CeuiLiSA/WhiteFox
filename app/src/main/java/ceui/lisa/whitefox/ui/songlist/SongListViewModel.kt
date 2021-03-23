package ceui.lisa.whitefox.ui.songlist

import ceui.lisa.whitefox.core.Repository
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.viewmodels.ListViewModel

class SongListViewModel: ListViewModel<Song>() {

    override fun loadRepo(): Repository<Song> {
        return SongListRepo()
    }

    fun setID(id: Long) {
        (repository as SongListRepo).id = id
    }
}