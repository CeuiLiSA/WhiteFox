package ceui.lisa.whitefox.test

import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.PlayListAdapter
import ceui.lisa.whitefox.models.ListPlayListResponse
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.viewmodels.PlayListViewModel
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class FragmentPlayList: FragmentList<PlaylistBean>() {

    override fun modelClass(): Class<out ListViewModel<PlaylistBean>> {
        return PlayListViewModel::class.java
    }

    override fun initAdapter(): BaseAdapter<PlaylistBean> {
        return PlayListAdapter(mViewModel.playList)
    }

    override fun initApi(): Observable<out ListShow<PlaylistBean>>? {
        return RxHttp.get("http://192.243.123.124:3000/user/playlist")
                .add("uid", App.user.account!!.id)
                .asClass(ListPlayListResponse::class.java)
    }
}