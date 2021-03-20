package ceui.lisa.whitefox.test

import ceui.lisa.whitefox.Params
import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.SongListAdapter
import ceui.lisa.whitefox.databinding.RecySongItemBinding
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.models.ListSongResponse
import ceui.lisa.whitefox.viewmodels.ListViewModel
import ceui.lisa.whitefox.viewmodels.SongListViewModel
import com.google.gson.Gson

class FragmentSongList : FragmentList<Song>() {

    companion object {
        fun newInstance(): FragmentSongList {
            return FragmentSongList()
        }
    }

    override fun modelClass(): Class<out ListViewModel<Song>> {
        return SongListViewModel::class.java
    }

    override fun onViewModelCreated() {
        (mViewModel as SongListViewModel).id =
            requireActivity().intent.getLongExtra("playlistID", 0L)
    }

    override fun initAdapter(): BaseAdapter<Song, RecySongItemBinding> {
        return SongListAdapter(mViewModel.playList)
    }
}