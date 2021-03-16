package ceui.lisa.whitefox.test

import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.SongListAdapter
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.models.SongResponse
import ceui.lisa.whitefox.viewmodels.SongListViewModel
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class FragmentSongList : FragmentList<Song>() {

    companion object {
        fun newInstance(): FragmentSongList {
            return FragmentSongList()
        }
    }

    override fun modelClass(): Class<out ListViewModel<Song>> {
        return SongListViewModel::class.java
    }

    override fun initAdapter(): BaseAdapter<Song> {
        return SongListAdapter(mViewModel.playList)
    }

    override fun initApi(): Observable<out ListShow<Song>> {
        val id = requireActivity().intent.getLongExtra("playlistID", 0L)
        return RxHttp.get("http://192.243.123.124:3000/playlist/detail?id=$id")
            .add("uid", App.user.account!!.id)
            .asClass(SongResponse::class.java)
    }
}