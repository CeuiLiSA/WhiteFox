package ceui.lisa.whitefox.test

import androidx.lifecycle.ViewModel
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.ui.recmd.RecmdPlaylistsRepository
import ceui.lisa.whitefox.ui.recmd.RecmdSongsRepository
import kotlin.collections.ArrayList

open class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var isLoaded = false
    var playList: MutableList<PlaylistBean> = ArrayList()
    var listSong: MutableList<Song> = ArrayList()

    var repoSongs = RecmdSongsRepository()
    var repoPlayLists = RecmdPlaylistsRepository()


}