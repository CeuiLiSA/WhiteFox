package ceui.lisa.whitefox.test

import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.adapters.PlayListHorizontalAdapter
import ceui.lisa.whitefox.adapters.SongListAdapter
import ceui.lisa.whitefox.databinding.FragmentMainBinding
import ceui.lisa.whitefox.models.DailySongList
import ceui.lisa.whitefox.models.RecmdPlayListResponse
import ceui.lisa.whitefox.models.SongUrl
import ceui.lisa.whitefox.ui.base.BaseFragment
import com.hjq.toast.ToastUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import rxhttp.RxHttp

class FragmentMain : Fragment() {

    lateinit var mViewModel: MainViewModel
    lateinit var playListHorizontalAdapter : PlayListHorizontalAdapter
    lateinit var songListAdapter: SongListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        mViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        val list1 = view?.findViewById<RecyclerView>(R.id.list_1)
        val list2 = view?.findViewById<RecyclerView>(R.id.list_2)

        playListHorizontalAdapter = PlayListHorizontalAdapter(mViewModel.playList)

        list1?.addItemDecoration(LinearItemHorizontalDecoration(DensityUtil.dp2px(12.0f)))
        list1?.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        list1?.adapter = playListHorizontalAdapter

        songListAdapter = SongListAdapter(mViewModel.listSong)
        list2?.layoutManager = LinearLayoutManager(requireContext())
        list2?.adapter = songListAdapter



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

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


}