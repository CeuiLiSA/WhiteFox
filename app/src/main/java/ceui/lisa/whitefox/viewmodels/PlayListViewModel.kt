package ceui.lisa.whitefox.viewmodels

import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.core.PlayListRepo
import ceui.lisa.whitefox.core.Repository
import ceui.lisa.whitefox.models.ListPlayListResponse
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class PlayListViewModel : ListViewModel<PlaylistBean>() {

    override fun loadRepo(): Repository<PlaylistBean> {
        return PlayListRepo()
    }
}