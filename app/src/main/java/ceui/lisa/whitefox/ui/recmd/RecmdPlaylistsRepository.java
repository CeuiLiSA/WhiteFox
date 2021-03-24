package ceui.lisa.whitefox.ui.recmd;

import org.jetbrains.annotations.NotNull;

import ceui.lisa.whitefox.core.RemoteData;
import ceui.lisa.whitefox.models.DailySongList;
import ceui.lisa.whitefox.models.PlaylistBean;
import ceui.lisa.whitefox.models.RecmdPlayListResponse;
import ceui.lisa.whitefox.models.Song;
import ceui.lisa.whitefox.test.ListShow;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rxhttp.RxHttp;

public class RecmdPlaylistsRepository extends RemoteData<PlaylistBean> {

    @NotNull
    @Override
    public Observable<? extends ListShow<PlaylistBean>> initApi() {
        return RxHttp.get("http://192.243.123.124:3000/recommend/resource")
                .asClass(RecmdPlayListResponse.class)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
