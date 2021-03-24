package ceui.lisa.whitefox.test

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.TemplateActivity
import ceui.lisa.whitefox.adapters.PlayListHorizontalAdapter
import ceui.lisa.whitefox.databinding.FragmentMainBinding
import ceui.lisa.whitefox.ui.songlist.SongListAdapter
import ceui.lisa.whitefox.models.DailySongList
import ceui.lisa.whitefox.models.RecmdPlayListResponse
import ceui.lisa.whitefox.ui.base.BaseFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import rxhttp.RxHttp

class FragmentMain : BaseFragment<FragmentMainBinding>() {

    lateinit var mViewModel: MainViewModel
    lateinit var playListHorizontalAdapter : PlayListHorizontalAdapter
    lateinit var songListAdapter: SongListAdapter

    override fun initModel() {
        mViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun initData() {
        if (!mViewModel.isLoaded) {
            RxHttp.get("http://192.243.123.124:3000/recommend/resource")
                    .asClass(RecmdPlayListResponse::class.java)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mViewModel.isLoaded = true
                        it.getListData()?.let { list ->
                            if (list.isNotEmpty()) {
                                mViewModel.playList.addAll(list)
                                playListHorizontalAdapter.notifyDataSetChanged()
                            }
                        }
                    }) { throwable -> throwable.printStackTrace() }



            RxHttp.get("http://192.243.123.124:3000/recommend/songs")
                    .asClass(DailySongList::class.java)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mViewModel.isLoaded = true
                        it.data?.let { item ->
                            item.dailySongs?.let { listSong ->
                                if (listSong.isNotEmpty()) {
                                    mViewModel.listSong.addAll(listSong)
                                    songListAdapter.notifyDataSetChanged()
                                }
                            }

                        }
                    }) { throwable -> throwable.printStackTrace() }
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_main
    }

    override fun initView() {
        baseBind.localFiles.setOnClickListener {
            val intent = Intent(mContext, TemplateActivity::class.java)
            intent.putExtra(TemplateActivity.NAME, "本地已下载音乐")
            mContext.startActivity(intent)
        }

        baseBind.recmdSongs.setOnClickListener {
            val intent = Intent(mContext, TemplateActivity::class.java)
            intent.putExtra(TemplateActivity.NAME, "本地已下载音乐")
            mContext.startActivity(intent)
        }


        baseBind.list1.addItemDecoration(LinearItemHorizontalDecoration(DensityUtil.dp2px(12.0f)))
        baseBind.list1.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        baseBind.list2.layoutManager = LinearLayoutManager(requireContext())

        playListHorizontalAdapter = PlayListHorizontalAdapter(mViewModel.playList)
        baseBind.list1.adapter = playListHorizontalAdapter

        songListAdapter = SongListAdapter(mViewModel.listSong)
        baseBind.list2.adapter = songListAdapter
    }
}