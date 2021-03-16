package ceui.lisa.whitefox.test

import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.Params
import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.SongListAdapter
import ceui.lisa.whitefox.databinding.RecySongItemBinding
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.models.ListSongResponse
import ceui.lisa.whitefox.viewmodels.SongListViewModel
import com.google.gson.Gson
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

    override fun initAdapter(): BaseAdapter<Song, RecySongItemBinding> {
        return SongListAdapter(mViewModel.playList)
    }

    override fun initApi(): Observable<out ListShow<Song>>? {
        val id = requireActivity().intent.getLongExtra("playlistID", 0L)
        if (id == 0L) {
            return null
        } else {
            return RxHttp.get("http://192.243.123.124:3000/playlist/detail?id=$id")
                .add("uid", App.user.account!!.id)
                .asClass(ListSongResponse::class.java)
        }
    }

    override fun loadFromLocal() {
        val songList = Gson().fromJson(Params.SONG_LIST_JSON, ListSongResponse::class.java)
        mViewModel.playList.addAll(songList.getListData()!!)
        mAdapter.notifyDataSetChanged()
    }
}