package ceui.lisa.whitefox.viewmodels

import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.core.Repository
import ceui.lisa.whitefox.core.SongListRepo
import ceui.lisa.whitefox.models.ListSongResponse
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class SongListViewModel: ListViewModel<Song>() {

    override fun loadRepo(): Repository<Song> {
        return SongListRepo()
    }

    fun setID(id: Long) {
        (repository as SongListRepo).id = id
    }
}