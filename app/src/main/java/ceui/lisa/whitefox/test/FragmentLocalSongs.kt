package ceui.lisa.whitefox.test

import androidx.databinding.ViewDataBinding
import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.LocalSongsAdapter
import ceui.lisa.whitefox.ui.base.FragmentList
import ceui.lisa.whitefox.viewmodels.ListViewModel
import ceui.lisa.whitefox.viewmodels.LocalSongsViewModel
import java.io.File

class FragmentLocalSongs : FragmentList<File>() {

    override fun modelClass(): Class<out ListViewModel<File>> {
        return LocalSongsViewModel::class.java
    }

    override fun initAdapter(): BaseAdapter<File, out ViewDataBinding> {
        return LocalSongsAdapter(mViewModel.playList)
    }
}