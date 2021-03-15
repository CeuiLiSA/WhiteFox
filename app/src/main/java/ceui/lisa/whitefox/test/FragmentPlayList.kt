package ceui.lisa.whitefox.test

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.models.PlayListResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import rxhttp.RxHttp

class FragmentPlayList : Fragment() {

    companion object {
        fun newInstance() = FragmentPlayList()
    }

    private lateinit var viewModel: PlayListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlayListViewModel::class.java)
        // TODO: Use the ViewModel

        if (viewModel.playListResponse == null) {
            Log.d("onActivityCreated ", "去请求playListResponse")

            RxHttp.get("http://192.243.123.124:3000/user/playlist")
                .add("uid", App.user.account!!.id)
                .asClass(PlayListResponse::class.java)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewModel.playListResponse = it
                    Log.d("onActivityCreated ", "更新了playListResponse")
                }
        } else {
            Log.d("onActivityCreated ", "不去请求playListResponse")
        }
    }
}