package ceui.lisa.whitefox.ui.playlist

import ceui.lisa.whitefox.core.Repository
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.viewmodels.ListViewModel

class PlayListViewModel : ListViewModel<PlaylistBean>() {

    override fun loadRepo(): Repository<PlaylistBean> {
        return PlayListRepo()
    }
}