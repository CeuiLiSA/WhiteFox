package ceui.lisa.whitefox.viewmodels

import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.core.CommentListRepo
import ceui.lisa.whitefox.core.Repository
import ceui.lisa.whitefox.core.SongListRepo
import ceui.lisa.whitefox.models.CommentsBean
import ceui.lisa.whitefox.models.ListSongResponse
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class CommentListViewModel: ListViewModel<CommentsBean>() {

    override fun loadRepo(): Repository<CommentsBean> {
        return CommentListRepo()
    }

    fun setID(id: Long) {
        (repository as CommentListRepo).id = id
    }
}